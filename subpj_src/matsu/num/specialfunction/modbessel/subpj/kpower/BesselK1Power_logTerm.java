/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.14
 */
package matsu.num.specialfunction.modbessel.subpj.kpower;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.modbessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * K1の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * K1 = (1/x) +(1/2)(x/2)log(u)F(u) - (x/2)G(u) <br>
 * の形 (F, Gはべき級数) に分解したときの, <br>
 * Fの推定に関する. <br>
 * F(u) = Sum_{m=0 to inf} u^m / (m!(m+1)!)
 * = I_1(x)/(x/2)
 * </p>
 * 
 * <p>
 * F(u)の第3次近似をf(u)とする. <br>
 * f(u) = 1 + u/(1*2) + u^2/(2*6) + u^3/(6*24) <br>
 * その残差: <br>
 * F(u) - f(u) =
 * Sum_{m=4 to inf} u^m / (m!(m+1)!) <br>
 * を提供する. <br>
 * スケールは(1/x)である.
 * </p>
 * 
 * <p>
 * 参考: <br>
 * F(u) = Sum_{m=0 to inf} u^m / (m!(m+1)!) <br>
 * G(u) = Sum_{m=0 to inf} u^m (G_m-gamma)/(m!(m+1)!)
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
final class BesselK1Power_logTerm implements RawCoefficientCalculableFunction {

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 50;

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 1), // 1 / (0! * 1!)
            1d / (1 * 2), // 1 / (1! * 2!)
            1d / (2 * 6), // 1 / (2! * 3!)
            1d / (6 * 24) // 1 / (3! * 4!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselK1Power_logTerm(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_U <= interval.lower() &&
                interval.upper() <= MAX_U)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    public double value(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 5; k--) {
            value *= u / (k * (k + 1));
            value += 1;
        }

        return value * Exponentiation.pow(u, 4) / (24 * 120);
    }

    @Override
    public double scale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 0.5 / Exponentiation.sqrt(SCALE_U_THRESHOLD);
        }
        return 0.5 / Exponentiation.sqrt(u);
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F(u) - f(u)] の近似多項式の係数を与え,
     * F(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F(u) - f(u)]の係数
     * @return F(u)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffF = thisCoeff.clone();
        for (int i = 0;
                i < coeffF.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffF[i] += ESTIMATED_COEFF[i];
        }

        return coeffF;
    }
}
