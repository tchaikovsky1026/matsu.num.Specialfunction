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

import matsu.num.commons.Exponentiation;

/**
 * 次数2から6の変形Bessel関数を扱う. <br>
 * 第1種変形Besselでは,
 * {@literal x <= 24} はべき級数表示を, それ以上では漸近級数を用いる.
 * 
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
final class ModifiedBessel2To6 extends ModifiedBesselHigherOrder {

    /**
     * @param order 次数
     * @param mbessel0 mbessel0
     * @param mbessel1 mbessel1
     */
    ModifiedBessel2To6(int order, ModifiedBessel0thOrder mbessel0, ModifiedBessel1stOrder mbessel1) {
        super(order, mbessel0, mbessel1);

        if (!(2 <= order && order <= 6)) {
            throw new AssertionError("次数が2から6でない");
        }
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x <= 24) {
            return this.besselIByPower(x, 40);
        }

        return this.besselIByAsymptotic(x, 20);
    }

    /**
     * べき級数によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double besselIByPower(double x, int kMax) {

        //級数部分
        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double coeff = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            coeff *= squareHalfX / (k * (k + this.order));
            coeff += 1d;
        }

        //x<<1におけるI_n(x)の振る舞い
        double limit = 1;
        for (int i = 0; i < this.order; i++) {
            limit *= halfX / (i + 1);
        }

        return coeff * limit;
    }

    /**
     * 漸近級数によりIを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double besselIByAsymptotic(double x, int kMax) {

        double asymptotic = Exponentiation.exp(x) / Exponentiation.sqrt(2 * Math.PI * x);
        if (!Double.isFinite(asymptotic)) {
            return Double.POSITIVE_INFINITY;
        }

        final double inv8X = 0.125 / x;

        double value = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - 4 * this.order * this.order) / k * inv8X;
            value += 1d;
        }

        return value * asymptotic;
    }
}
