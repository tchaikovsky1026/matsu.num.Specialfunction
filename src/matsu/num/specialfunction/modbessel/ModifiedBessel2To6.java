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
 * @author Matsuura Y.
 * @version 18.3
 */
final class ModifiedBessel2To6 extends ModifiedBesselHigherOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値.
     * 下側はべき級数, 上側は漸近級数.
     */
    private static final double BOUNDARY_X_FOR_BESSEL_I = 24d;

    /**
     * I(x)の下側xのべき級数の項数.
     */
    private static final int K_MAX_FOR_POWER = 40;
    /**
     * I(x)の上側xの漸近級数の項数.
     */
    private static final int K_MAX_FOR_ASYMPTOTIC = 20;

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
        if (x <= BOUNDARY_X_FOR_BESSEL_I) {
            return this.besselI_byPower(x);
        }

        double scaling = this.scaling_besselI_byAsymptotic(x);
        if (scaling == 0d) {
            return Double.POSITIVE_INFINITY;
        }
        return scaling * Exponentiation.exp(x);
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x <= BOUNDARY_X_FOR_BESSEL_I) {
            return this.besselI_byPower(x) * Exponentiation.exp(-x);
        }

        return this.scaling_besselI_byAsymptotic(x);
    }

    /**
     * べき級数によりIを計算する.
     * 
     * @param x x
     */
    private double besselI_byPower(double x) {

        //級数部分
        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double coeff = 0;
        for (int k = K_MAX_FOR_POWER + 1; k >= 1; k--) {
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
     */
    private double scaling_besselI_byAsymptotic(double x) {

        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double inv8X = 0.125 / x;
        final int squareOrder4 = 4 * this.order * this.order;

        double value = 0;
        for (int k = K_MAX_FOR_ASYMPTOTIC + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - squareOrder4) / k * inv8X;
            value += 1d;
        }

        return value * asymptotic;
    }
}
