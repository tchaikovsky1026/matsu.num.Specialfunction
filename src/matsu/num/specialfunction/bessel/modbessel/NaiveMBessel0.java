/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.10
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;

/**
 * <p>
 * 素朴な0次の変形Bessel関数の実装.
 * </p>
 * 
 * <p>
 * このクラスは0次変形Bessel関数の素朴な (原理に忠実な) 実装を扱う. <br>
 * 計算効率などは全く考慮されていないことに注意すべき.
 * </p>
 * 
 * <p>
 * 0次MBesselの計算戦略は次の通りである. <br>
 * bessel_Iは, 小さいxではべき級数表示を, 大きいxでは漸近級数を使う. <br>
 * bessel_Kは, 小さいxではべき級数表示を, 大きいxでは漸近級数の連分数表示を使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.4
 * @deprecated {@link MBessel0Optimized} が提供されたため, 使用されていない.
 */
@Deprecated
final class NaiveMBessel0 extends ModifiedBessel0thOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);
    private static final double SQRT_PI_OVER_2 = Math.sqrt(Math.PI / 2);

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数. <br>
     * 漸近級数ではexp(-x)成分を排除する必要があるため, この閾値は大きくとる.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I = 24d;

    /**
     * K(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数の連分数表示.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K = 2d;

    /**
     * I(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_BY_POWER = 40;

    /**
     * I(x)の漸近級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_BY_ASYMPTOTIC = 15;

    /**
     * K(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 15;

    /**
     * {@literal x >= boundaryXForK} におけるK(x)の漸近展開部分を連分数に変換した結果.
     */
    private static final DoubleContinuedFractionFunction ASYMPTOTIC_FRACTION_FOR_BESSEK_K =
            NaiveMBesselContinuedFraction.createK0Asymptotic();

    /**
     * [H_0, ..., H_kMax],
     * Kのべき級数の計算で使用する.
     */
    private static final double[] HARMONIC_NUMBERS =
            calcHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    NaiveMBessel0() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return besselI_byPower(x);
        } else {
            double scaling_I = scaling_besselI_byAsymptotic(x);
            if (scaling_I == 0d) {
                return Double.POSITIVE_INFINITY;
            }
            return scaling_I * Exponentiation.exp(x);
        }
    }

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return besselK_byPower(x);
        } else {
            return scaling_besselK_byAsymptoticFraction(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return besselI_byPower(x) * Exponentiation.exp(-x);
        } else {
            return scaling_besselI_byAsymptotic(x);
        }
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return besselK_byPower(x) * Exponentiation.exp(x);
        } else {
            return scaling_besselK_byAsymptoticFraction(x);
        }
    }

    /**
     * べき級数による I(x)
     */
    private static double besselI_byPower(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_POWER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * k);
            value += 1d;
        }

        return value;
    }

    /**
     * 漸近級数による I(x)exp(-x)
     */
    private static double scaling_besselI_byAsymptotic(double x) {

        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double t = 0.125 / x;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_ASYMPTOTIC + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1) / k * t;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * べき級数による K(x)
     */
    private static double besselK_byPower(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;
        final double gamma_plus_logHalfX =
                GammaFunction.EULER_MASCHERONI_GAMMA + Exponentiation.log(halfX);
        if (Double.isInfinite(gamma_plus_logHalfX)) {
            return Double.POSITIVE_INFINITY;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * k);
            value += HARMONIC_NUMBERS[k - 1] - gamma_plus_logHalfX;
        }

        return value;
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_besselK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x)
                * ASYMPTOTIC_FRACTION_FOR_BESSEK_K.value(0.125 / x);
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
