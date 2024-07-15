/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.13
 */
package matsu.num.specialfunction.modbessel.subpj.kpower;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.modbessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * K0の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * K0 = -(1/2)log(u)F(u) + G(u) <br>
 * の形 (F, Gはべき級数) に分解したときの, <br>
 * Fの推定に関する. <br>
 * F(u) = Sum_{m=0 to inf} u^m / (m!)^2
 * = I_0(x)
 * </p>
 * 
 * <p>
 * 第0近似を厳密にしたい. <br>
 * そこで, F'(u) = (F(u)-1)/u を多項式近似する. <br>
 * F'(u)の第3次近似をf'(u)とする. <br>
 * f'(u) = 1 + u/4 + u^2/36 + u^3/(24)^2 <br>
 * その残差: <br>
 * F'(u) - f'(u) =
 * Sum_{m=4 to inf} u^m / ((m+1)!)^2 <br>
 * を提供する. <br>
 * スケールは(1/u)である.
 * </p>
 * 
 * <p>
 * 参考: <br>
 * F(u) = Sum_{m=0 to inf} u^m / (m!)^2 <br>
 * G(u) = Sum_{m=0 to inf} u^m (H_m-gamma)/(m!)^2
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
final class BesselK0Power_logTerm implements RawCoefficientCalculableFunction {

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 20;

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 1), // 1 / (0! * 0!)
            1d / (2 * 2), // 1 / (1! * 1!)
            1d / (6 * 6), // 1 / (2! * 2!)
            1d / (24 * 24) // 1 / (3! * 3!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselK0Power_logTerm(DoubleFiniteClosedInterval interval) {
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
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 6; k--) {
            value *= u / (k * k);
            value += 1;
        }

        return value * Exponentiation.pow(u, 4) / (120 * 120);
    }

    @Override
    public double scale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 1 / SCALE_U_THRESHOLD;
        }
        return 1 / u;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F'(u) - f'(u)] の近似多項式の係数を与え,
     * F(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F'(u) - f'(u)]の係数
     * @return F(u)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffFp = thisCoeff.clone();
        for (int i = 0;
                i < coeffFp.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffFp[i] += ESTIMATED_COEFF[i];
        }

        double[] coeffF = new double[coeffFp.length + 1];
        coeffF[0] = 1d;
        System.arraycopy(coeffFp, 0, coeffF, 1, coeffFp.length);

        return coeffF;
    }
}
