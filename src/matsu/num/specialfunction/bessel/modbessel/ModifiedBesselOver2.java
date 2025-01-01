/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.specialfunction.bessel.basecomponent.InverseFactorialSupplier;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 次数2以上の変形Bessel関数を扱う. <br>
 * このクラスでは第1種変形Besselを実装し, クラスが完成する. <br>
 * 第1種変形Besselでは,
 * {@literal x <= 24} はべき級数を,
 * {@literal 24 <= x <= n^2/2} は後退漸化式を,
 * それ以上では漸近級数を用いる. <br>
 * ただし, 次数6以下の場合は後退漸化式の領域は存在しない.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class ModifiedBesselOver2 extends ModifiedBesselHigherOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);

    /**
     * I(x)についてアルゴリズムを切り替えるxの下側の閾値. <br>
     * 下側はべき級数, 上側は後退漸化式.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION = 24d;

    /**
     * I(x)のべき級数の項数.
     */
    private static final int K_MAX_BY_POWER = 40;

    /**
     * I(x)の漸近級数の項数.
     */
    private static final int K_MAX_BY_ASYMPTOTIC = 20;

    /**
     * 漸近級数によるI(x)で,
     * exp(x)のオーバーフローを回避するために導入するシフト.
     */
    private static final double SHIFT_X_FOR_BESSEL_I = 20;

    /**
     * exp(shift_x)
     */
    private static final double EXP_OF_SHIFT_X_FOR_BESSEL_I = Math.exp(SHIFT_X_FOR_BESSEL_I);

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
    ModifiedBesselOver2(int order, ModifiedBessel0thOrder mbessel0, ModifiedBessel1stOrder mbessel1) {
        super(order, mbessel0, mbessel1);
        if (!(order >= 2)) {
            throw new AssertionError("次数が2以上でない");
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

        /*
         * 漸近級数によるI(x)で,
         * I(x)はオーバーフローしないがexp(x)がオーバーフローする場合に対応するため,
         * xを負の方向にシフトする.
         */
        return (scaling * EXP_OF_SHIFT_X_FOR_BESSEL_I) *
                Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);
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

        //アンダーフロー対策にこの順番で
        return Exponentiation.pow(halfX, this.order) * coeff * this.inverseFactorial;
    }

    /**
     * 逆方向漸化式によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double scaling_byBackRecursion(double x) {

        final double doubleInvX = 2d / x;
        final double init = 1E-280;

        double i_nu_plus_1 = 0d;
        double i_nu = init;

        for (int nu = this.upperN_byBackRecursion; nu > this.order; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + nu * doubleInvX * i_nu;

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
            double i_nu_m_1 = i_nu_plus_1 + nu * doubleInvX * i_nu;
            if (!Double.isFinite(i_nu_m_1)) {
                return 0d;
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

        final int squareOrder4 = 4 * this.order * this.order;

        final double t = 0.125 / x;
        double value = 0;
        for (int k = K_MAX_BY_ASYMPTOTIC; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - squareOrder4) / k * t;
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
