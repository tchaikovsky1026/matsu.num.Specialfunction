/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.8
 */
package matsu.num.specialfunction.modbessel;

import java.util.function.IntUnaryOperator;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;

/**
 * <p>
 * 素朴な1次の変形Bessel関数の実装.
 * </p>
 * 
 * <p>
 * このクラスは1次変形Bessel関数の素朴な (原理に忠実な) 実装を扱う. <br>
 * 計算効率などは全く考慮されていないことに注意すべき. <br>
 * 将来のバージョンでおそらく非推奨になる.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
final class NaiveMBessel1 extends ModifiedBessel1stOrder {

    /**
     * {@literal x >= 2} におけるK(x)の漸近展開部分を連分数に変換した結果.
     */
    private static final NormalContinuedFractionFunction cfFuncOfMBesselK_Over2;

    static {
        cfFuncOfMBesselK_Over2 = calcMBesselK_Over2(50);
    }

    NaiveMBessel1() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < 24) {
            return mBesselI_Under_24(x);
        } else {
            return mBesselI_Over_24(x);
        }
    }

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < 2) {
            return mBesselK_Under_2(x);
        } else {
            return mBesselK_Over_2(x);
        }
    }

    private static double mBesselI_Under_24(double x) {
        //べき級数により計算する

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        final int kMax = 40;
        double value = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            value *= squareHalfX / (k * (k + 1));
            value += 1d;
        }
        value *= halfX;

        return value;
    }

    private static double mBesselI_Over_24(double x) {

        //漸近展開により計算する
        double asymptotic = Exponentiation.exp(x) / Exponentiation.sqrt(2 * Math.PI * x);
        if (!Double.isFinite(asymptotic)) {
            return Double.POSITIVE_INFINITY;
        }

        final double inv8X = 0.125 / x;

        //扱うxの範囲において, 漸近級数の項が増大を始める値:過剰
        //x=24ならｋMax=15でよい
        final int kMax = 15;
        double value = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - 4) / k * inv8X;
            value += 1d;
        }

        return value * asymptotic;
    }

    private static double mBesselK_Under_2(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;
        final double gamma_plus_logHalfX =
                GammaFunction.EULER_MASCHERONI_GAMMA + Exponentiation.log(halfX);
        if (Double.isInfinite(gamma_plus_logHalfX)) {
            return Double.POSITIVE_INFINITY;
        }

        final int kMax = 15;
        final double[] pseudoHarmonicNumbers = calcPseudoHarmonicNumbers(kMax);

        double value = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            value *= squareHalfX / (k * (k + 1));
            value += pseudoHarmonicNumbers[k - 1] - gamma_plus_logHalfX;
        }

        return -halfX * value + 1 / x;
    }

    private static double mBesselK_Over_2(double x) {
        //漸近展開により計算する
        double asymptotic = Exponentiation.exp(-x) * Exponentiation.sqrt(Math.PI / 2 / x);
        if (!Double.isFinite(asymptotic)) {
            return 0d;
        }

        return asymptotic * cfFuncOfMBesselK_Over2.value(0.125 / x);
    }

    /**
     * [G_0, ..., G_kMax] を返す.
     * ただし, G_k = (1/2)(H_k + H_{k+1})
     */
    private static double[] calcPseudoHarmonicNumbers(int kMax) {
        final double[] harmonicNumbers = new double[kMax + 1];

        double current = 0.5;
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current += 0.5 * (1d / (k + 1) + 1d / (k + 2));
        }

        return harmonicNumbers;
    }

    /**
     * {@literal x >= 2} におけるK(x)の漸近展開部分を連分数に変換した結果を返す. <br>
     * 
     * 1 + ((1^2-4)/(1))*(1/8x) + + ((1^2-4)*(3^2-4)/(1*2))*(1/8x)^2 + ... <br>
     * に相当.
     * 
     * @param kMax 使用する項の数
     * @return 連分数
     */
    private static NormalContinuedFractionFunction calcMBesselK_Over2(int kMax) {

        IntUnaryOperator nume = k -> -((2 * k + 1) * (2 * k + 1) - 4);
        IntUnaryOperator denomi = k -> (k + 1);

        return new NormalContinuedFractionFunction(kMax, nume, denomi);
    }
}
