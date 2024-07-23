/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modsbessel;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.bessel.basecomponent.InverseDoubleFactorialSupplier;

/**
 * 次数2以上の変形球Bessel関数を扱う. <br>
 * このクラスでは第1種球変形Besselを実装し, クラスが完成する. <br>
 * 第1種球変形Besselでは,
 * {@literal x <= 1} はべき級数を,
 * {@literal 1 <= x <= n^2/2} は後退漸化式を,
 * それ以上では漸近級数(理論式)を用いる. <br>
 * ただし,
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
final class MSBesselOver2 extends MSBesselHigherImplK {

    /**
     * i(x)についてアルゴリズムを切り替えるxの下側の閾値. <br>
     * 下側はべき級数, 上側は後退漸化式.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION = 1d;

    /**
     * i(x)の漸近級数について, exp(-x)の項を無視できるxの閾値. <br>
     * 上側は無視できる.
     */
    private static final double BOUNDARY_X_IGNORING_EXP_MX = 24d;

    /**
     * i(x)のべき級数の項数.
     */
    private static final int K_MAX_BY_POWER = 10;

    /**
     * i(x)の漸近級数の項数の最大値.
     */
    private static final int REQUIRED_K_MAX_BY_ASYMPTOTIC = 20;

    /**
     * i(x)の漸近級数の項数.
     */
    private final int kMax_byAsymptotic;

    /**
     * i(x)についてアルゴリズムを切り替えるxの上側の閾値. <br>
     * 下側は後退漸化式, 上側は漸近級数.
     */
    private final double boundaryX_selectingBackRecursionOrAsymptotic;

    /**
     * i(x)の後退漸化式における, 考慮するべき次数.
     */
    private final int upperN_byBackRecursion;

    private final double inverseDoubleFactorial;

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
     * @param order
     * @param msbessel0
     * @param msbessel1
     */
    MSBesselOver2(int order, MSBessel0 msbessel0, MSBessel1 msbessel1) {
        super(order, msbessel0, msbessel1);

        this.upperN_byBackRecursion = this.calcUpperN_byBackRecursion();
        this.boundaryX_selectingBackRecursionOrAsymptotic = this.calcBoundaryX_selectingBackRecursionOrAsymptotic();

        this.kMax_byAsymptotic = this.calcKMax_byAsymptotic();
        this.inverseDoubleFactorial = InverseDoubleFactorialSupplier.get(order);
    }

    @Override
    public double sbesselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.sbI_byPower(x);
        }
        if (x <= this.boundaryX_selectingBackRecursionOrAsymptotic) {
            return this.sbIc_byBackRecursion(x) * Exponentiation.exp(x);
        }

        double scaling = this.sbIc_byAsymptotic(x);

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
    public double sbesselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.sbI_byPower(x) * Exponentiation.exp(-x);
        }

        if (x <= this.boundaryX_selectingBackRecursionOrAsymptotic) {
            return this.sbIc_byBackRecursion(x);
        }

        return this.sbIc_byAsymptotic(x);
    }

    /**
     * べき級数によりiを計算する.
     */
    private double sbI_byPower(double x) {

        final int n2_p_1 = 2 * this.order + 1;

        //級数部分
        final double squareX = x * x;

        double coeff = 0;
        for (int k = K_MAX_BY_POWER + 1; k >= 1; k--) {
            coeff *= squareX / ((2 * k) * (2 * k + n2_p_1));
            coeff += 1d;
        }

        //アンダーフロー対策にこの順番で
        return (Exponentiation.pow(x, this.order) * coeff) * this.inverseDoubleFactorial;
    }

    /**
     * 逆方向漸化式によりicを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double sbIc_byBackRecursion(double x) {

        final double invX = 1d / x;
        final double init = 1E-280;

        double i_nu_plus_1 = 0d;
        double i_nu = init;

        for (int nu = this.upperN_byBackRecursion; nu > this.order; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu + 1) * invX * i_nu;

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
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu + 1) * invX * i_nu;
            if (!Double.isFinite(i_nu_m_1)) {
                return 0d;
            }

            i_nu_plus_1 = i_nu;
            i_nu = i_nu_m_1;
        }

        return this.msbessel1.sbesselIc(x) / i_nu * i_order;
    }

    /**
     * 漸近級数によりicを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double sbIc_byAsymptotic(double x) {

        double value = sbIc_byAsymptotic_principalFactor(x);
        if (x < BOUNDARY_X_IGNORING_EXP_MX) {
            double res = Exponentiation.exp(-2 * x) * sbIc_byAsymptotic_residualFactor(x);
            value += (this.order & 1) == 1
                    ? res
                    : -res;
        }

        return value * 0.5 / x;
    }

    /**
     * 漸近級数によりicを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double sbIc_byAsymptotic_principalFactor(double x) {

        final int constOrder = (2 * this.order + 1) * (2 * this.order + 1);

        final double t = 0.125 / x;
        double value = 0;
        for (int k = this.kMax_byAsymptotic + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - constOrder) / k * t;
            value += 1d;
        }

        return value;
    }

    /**
     * 漸近級数によりicを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double sbIc_byAsymptotic_residualFactor(double x) {

        final int constOrder = (2 * this.order + 1) * (2 * this.order + 1);

        final double t = 0.125 / x;
        double value = 0;
        for (int k = this.kMax_byAsymptotic + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= -(double) (k2m1 * k2m1 - constOrder) / k * t;
            value += 1d;
        }

        return value;
    }

    /**
     * 漸近級数で必要な項数を計算する. <br>
     * ただし, 項数より解析解の項数が少ない場合はそれを返す.
     * 
     * @return kMax
     */
    private int calcKMax_byAsymptotic() {
        return Math.min(order, REQUIRED_K_MAX_BY_ASYMPTOTIC);
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
