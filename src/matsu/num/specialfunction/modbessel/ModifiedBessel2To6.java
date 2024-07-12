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
 * 次数2から6の変形Bessel関数. <br>
 * このクラスでは第1種変形Besselを実装し, クラスが完成する. <br>
 * 第1種変形Besselでは,
 * {@literal x <= 24} はべき級数表示を, それ以上では漸近級数を用いる.
 * 
 * @author Matsuura Y.
 * @version 18.4
 * @deprecated このクラスは使われていない.
 *                 {@link ModifiedBesselOver2} に置き換えられる.
 */
@Deprecated
final class ModifiedBessel2To6 extends ModifiedBesselHigherOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数. <br>
     * 漸近級数ではexp(-x)成分を排除する必要があるため, この閾値は大きくとる.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC = 24d;

    /**
     * I(x)のべき級数の項数.
     */
    private static final int K_MAX_BY_POWER = 40;

    /**
     * I(x)の漸近級数の項数.
     */
    private static final int K_MAX_BY_ASYMPTOTIC = 20;

    /**
     * 次数をnとして, 1/n!
     */
    private final double inverseFactorial;

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

        this.inverseFactorial = InverseFactorialSupplier.get(order);
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x <= BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return this.byPower(x);
        }

        double scaling = this.scaling_byAsymptotic(x);
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
        if (x <= BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return this.byPower(x) * Exponentiation.exp(-x);
        }

        return this.scaling_byAsymptotic(x);
    }

    /**
     * べき級数による I(x)
     */
    private double byPower(double x) {

        //級数部分
        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double coeff = 0;
        for (int k = K_MAX_BY_POWER + 1; k >= 1; k--) {
            coeff *= squareHalfX / (k * (k + this.order));
            coeff += 1d;
        }

        //x<<1におけるI_n(x)の振る舞い
        //(x/2)^n/n!
        double limit = Exponentiation.pow(halfX, this.order) * this.inverseFactorial;

        return coeff * limit;
    }

    /**
     * 漸近級数による I(x)exp(-x)
     */
    private double scaling_byAsymptotic(double x) {

        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double t = 0.125 / x;
        final int squareOrder4 = 4 * this.order * this.order;

        double value = 0;
        for (int k = K_MAX_BY_ASYMPTOTIC + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - squareOrder4) / k * t;
            value += 1d;
        }

        return value * asymptotic;
    }
}
