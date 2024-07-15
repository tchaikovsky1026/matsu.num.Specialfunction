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
 * 次数7以上の変形Bessel関数を扱う. <br>
 * このクラスでは第1種変形Besselを実装し, クラスが完成する. <br>
 * 第1種変形Besselでは,
 * {@literal x <= n^2/2} は後退漸化式を, それ以上では漸近級数を用いる.
 * 
 * @author Matsuura Y.
 * @version 18.4
 * @deprecated このクラスは使われていない.
 *                 {@link ModifiedBesselOver2} に置き換えられる.
 *                 残す必要も無いため, 19以降で削除されるだろう.
 */
@Deprecated(forRemoval = true)
final class ModifiedBesselOver7 extends ModifiedBesselHigherOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);

    /**
     * I(x)についてアルゴリズムを切り替えるxの下側の閾値. <br>
     * 下側はべき級数, 上側は後退漸化式.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION = 1d;

    /**
     * I(x)のべき級数の項数.
     */
    private static final int K_MAX_BY_POWER = 9;

    /**
     * I(x)の漸近級数の項数.
     */
    private static final int K_MAX_BY_ASYMPTOTIC = 20;

    /**
     * I(x)についてアルゴリズムを切り替えるxの上側の閾値. <br>
     * 下側は後退漸化式, 上側は漸近級数.
     */
    private final double boundaryX_selectingBackRecursionOrAsymptotic;

    /**
     * I(x)の後退漸化式における, 考慮するべき次数.
     */
    private final int upperN_byBackRecursion;

    /**
     * 次数をnとして, 1/n!
     */
    private final double inverseFactorial;

    /**
     * @param order 次数
     * @param mbessel0 mbessel0
     * @param mbessel1 mbessel1
     */
    ModifiedBesselOver7(int order, ModifiedBessel0thOrder mbessel0, ModifiedBessel1stOrder mbessel1) {
        super(order, mbessel0, mbessel1);
        if (!(order >= 7)) {
            throw new AssertionError("次数が7以上でない");
        }

        this.upperN_byBackRecursion =
                this.calcUpperN_byBackRecursion();
        this.boundaryX_selectingBackRecursionOrAsymptotic =
                this.calcBoundaryX_selectingBackRecursionOrAsymptotic();
        this.inverseFactorial = InverseFactorialSupplier.get(order);
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x <= BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.byPower(x);
        }

        if (x <= this.boundaryX_selectingBackRecursionOrAsymptotic) {
            return this.scaling_byBackRecursion(x) * Exponentiation.exp(x);
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

        if (x <= BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.byPower(x) * Exponentiation.exp(-x);
        }

        if (x <= this.boundaryX_selectingBackRecursionOrAsymptotic) {
            return this.scaling_byBackRecursion(x);
        }

        return this.scaling_byAsymptotic(x);
    }

    /**
     * べき級数によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
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
     * 逆方向漸化式によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double scaling_byBackRecursion(double x) {

        final double init = 1E-280;

        double i_nu_plus_1 = 0d;
        double i_nu = init;

        for (int nu = this.upperN_byBackRecursion; nu > this.order; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu / x) * i_nu;

            i_nu_plus_1 = i_nu;
            i_nu = i_nu_m_1;

            if (i_nu >= 1E200) {
                i_nu_plus_1 = (i_nu_plus_1 / i_nu) * init;
                i_nu = init;
            }
        }

        i_nu_plus_1 = (i_nu_plus_1 / i_nu) * init;
        i_nu = init;

        double i_order = i_nu;

        for (int nu = this.order; nu > 1; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu / x) * i_nu;
            if (Double.isInfinite(i_nu_m_1)) {
                return Double.POSITIVE_INFINITY;
            }

            i_nu_plus_1 = i_nu;
            i_nu = i_nu_m_1;
        }

        return this.mbessel1.besselIc(x) / i_nu * i_order;
    }

    /**
     * 漸近級数によりIを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double scaling_byAsymptotic(double x) {

        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double t = 0.125 / x;
        double value = 0;
        for (int k = K_MAX_BY_ASYMPTOTIC; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - 4 * this.order * this.order) / k * t;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * 逆方向漸化式におけるNを計算する
     * 
     * @return N
     */
    private int calcUpperN_byBackRecursion() {
        return (int) (4.6 * this.order) + 3;
    }

    private double calcBoundaryX_selectingBackRecursionOrAsymptotic() {
        return this.order * this.order * 0.5;
    }

}
