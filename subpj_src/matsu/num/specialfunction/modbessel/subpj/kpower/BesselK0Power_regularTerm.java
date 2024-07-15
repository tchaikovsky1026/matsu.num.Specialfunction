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
import matsu.num.specialfunction.GammaFunction;
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
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} (H_m-gamma)u^m / (m!)^2
 * = K0 + (1/2)log(u)*I0(x)
 * </p>
 * 
 * <p>
 * G(u)の第3次近似をg(u)とする. <br>
 * g(u) = -gamma + (1-gamma)*u + (3/2-gamma)*u^2/4 + (11/6-gamma)u^3/36 <br>
 * その残差: <br>
 * G(u) - g(u) =
 * Sum_{m=4 to inf} u^m (H_m-gamma) / (m!)^2 <br>
 * を提供する. <br>
 * スケールは-(1/2)log(u) + 1/2とする. <br>
 * ({@literal u -> 0} では-(1/2)log(u)だが,
 * {@literal u -> 1} でも安定させるためにオフセットを加えている.
 * スケールとしてはK0の良い近似になっている.)
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
final class BesselK0Power_regularTerm implements RawCoefficientCalculableFunction {

    private static final double GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 30;

    /**
     * [H_0, ..., H_kMax],
     * Kのべき級数の計算で使用する.
     */
    private static final double[] HARMONIC_NUMBERS =
            calcHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 1) * (HARMONIC_NUMBERS[0] - GAMMA), // (H_0 - gamma) / (0! * 0!)
            1d / (1 * 1) * (HARMONIC_NUMBERS[1] - GAMMA), // (H_1 - gamma) / (1! * 1!)
            1d / (2 * 2) * (HARMONIC_NUMBERS[2] - GAMMA), // (H_2 - gamma) / (2! * 2!)
            1d / (6 * 6) * (HARMONIC_NUMBERS[3] - GAMMA) // (H_3 - gamma) / (3! * 3!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselK0Power_regularTerm(DoubleFiniteClosedInterval interval) {
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
            value *= u / (k * k);
            value += HARMONIC_NUMBERS[k - 1] - GAMMA;
        }

        return value * Exponentiation.pow(u, 4) / (24 * 24);
    }

    @Override
    public double scale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return -0.5 * Exponentiation.log(SCALE_U_THRESHOLD) + 0.5;
        }
        return -0.5 * Exponentiation.log(u) + 0.5;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [G(u) - g(u)] の近似多項式の係数を与え,
     * G(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [G(u) - g(u)]の係数
     * @return G(u)の係数
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

    /**
     * [H_0, ..., H_kMax] を返す.
     */
    private static double[] calcHarmonicNumbers(int kMax) {
        final double[] harmonicNumbers = new double[kMax + 1];

        double current = 0;
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current += 1d / (k + 1);
        }

        return harmonicNumbers;
    }
}
