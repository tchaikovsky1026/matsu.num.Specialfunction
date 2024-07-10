/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.10
 */
package matsu.num.specialfunction.modbessel;

import java.util.function.IntUnaryOperator;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;

/**
 * <p>
 * 素朴な0次の変形Bessel関数の実装.
 * </p>
 * 
 * <p>
 * このクラスは0次変形Bessel関数の素朴な (原理に忠実な) 実装を扱う. <br>
 * 計算効率などは全く考慮されていないことに注意すべき. <br>
 * 将来のバージョンでおそらく非推奨になる.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.3
 */
final class NaiveMBessel0 extends ModifiedBessel0thOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);
    private static final double SQRT_PI_OVER_2 = Math.sqrt(Math.PI / 2);

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値.
     * 下側はべき級数, 上側は漸近級数.
     */
    private static final double BOUNDARY_X_FOR_BESSEL_I = 24d;

    /**
     * K(x)についてアルゴリズムを切り替えるxの閾値.
     * 下側はべき級数, 上側は漸近級数の連分数展開.
     */
    private static final double BOUNDARY_X_FOR_BESSEL_K = 2d;

    /**
     * I(x)の下側xのべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_UNDER = 40;
    /**
     * I(x)の上側xの漸近級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_OVER = 15;
    /**
     * K(x)の下側xのべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_K_UNDER = 15;
    /**
     * K(x)の上側xの連分数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_K_OVER = 50;

    /**
     * {@literal x >= boundaryXForK} におけるK(x)の漸近展開部分を連分数に変換した結果.
     */
    private static final NormalContinuedFractionFunction cfFuncOfMBesselKOver =
            calcMBesselK_Over(K_MAX_FOR_BESSEL_K_OVER);

    /**
     * [H_0, ..., H_kMax]
     */
    private static final double[] harmonicNumbers = calcHarmonicNumbers(K_MAX_FOR_BESSEL_K_UNDER);

    NaiveMBessel0() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_FOR_BESSEL_I) {
            return mBesselI_Under(x);
        } else {
            double scaling_I = scaling_mBesselI_Over(x);
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

        if (x < BOUNDARY_X_FOR_BESSEL_K) {
            return mBesselK_Under(x);
        } else {
            return scaling_mBesselK_Over(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_FOR_BESSEL_I) {
            return mBesselI_Under(x) * Exponentiation.exp(-x);
        } else {
            return scaling_mBesselI_Over(x);
        }
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_FOR_BESSEL_K) {
            return mBesselK_Under(x) * Exponentiation.exp(x);
        } else {
            return scaling_mBesselK_Over(x);
        }
    }

    /**
     * 下側xのI(x)
     */
    private static double mBesselI_Under(double x) {
        //べき級数により計算する

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_UNDER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * k);
            value += 1d;
        }

        return value;
    }

    /**
     * 上側xにおける, I(x)exp(-x)
     */
    private static double scaling_mBesselI_Over(double x) {

        //漸近展開により計算する
        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double inv8X = 0.125 / x;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_OVER + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1) / k * inv8X;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * 下側xのK(x)
     */
    private static double mBesselK_Under(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;
        final double gamma_plus_logHalfX =
                GammaFunction.EULER_MASCHERONI_GAMMA + Exponentiation.log(halfX);
        if (Double.isInfinite(gamma_plus_logHalfX)) {
            return Double.POSITIVE_INFINITY;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_UNDER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * k);
            value += harmonicNumbers[k - 1] - gamma_plus_logHalfX;
        }

        return value;
    }

    /**
     * 上側xにおける, K(x)exp(x)
     */
    private static double scaling_mBesselK_Over(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x) * cfFuncOfMBesselKOver.value(0.125 / x);
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

    /**
     * {@literal x >= 2} におけるK(x)の漸近展開部分を連分数に変換した結果を返す. <br>
     * 
     * 1 + ((1^2)/(1))*(1/8x) + + ((1^2)*(3^2)/(1*2))*(1/8x)^2 + ... <br>
     * に相当.
     * 
     * @param kMax 使用する項の数
     * @return 連分数
     */
    private static NormalContinuedFractionFunction calcMBesselK_Over(int kMax) {

        IntUnaryOperator nume = k -> -((2 * k + 1) * (2 * k + 1));
        IntUnaryOperator denomi = k -> (k + 1);

        return new NormalContinuedFractionFunction(kMax, nume, denomi);
    }
}
