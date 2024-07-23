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

/**
 * 高次(2次以上)の変形球Bessel関数を表し,
 * 前進漸化式による第2種変形球Besselの計算が実装されている.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class MSBesselHigherImplK extends SkeletalMSBessel {

    /**
     * 前進漸化式によるK(x)について, 生値で進めるか, スケーリング値で進めるかを切り替えるxの閾値. <br>
     * 下側は生値, 上側はスケーリング値.
     */
    private static final double BOUNDARY_X_SELECTING_RAW_OR_SCALING_FOR_BESSEL_K = 2d;

    protected final int order;

    protected final MSBessel0 msbessel0;
    protected final MSBessel1 msbessel1;

    /**
     * 与えた次数の変形球Bessel関数を生成する. <br>
     * 次数は2以上100以下でなければならない.
     * 
     * 
     * @param order 次数
     * @param msbessel0 0次MSBessel
     * @param msbessel1 1次MSBessel
     */
    MSBesselHigherImplK(int order, MSBessel0 msbessel0, MSBessel1 msbessel1) {
        super();
        this.order = order;
        this.msbessel0 = msbessel0;
        this.msbessel1 = msbessel1;
    }

    @Override
    public final int order() {
        return this.order;
    }

    @Override
    public final double sbesselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_RAW_OR_SCALING_FOR_BESSEL_K) {
            return this.sbK_byForwardRecursion(x);
        } else {
            return this.sbKc_byForwardRecursion(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public final double sbesselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        return this.sbKc_byForwardRecursion(x);
    }

    /**
     * 前進漸化式に従って k(x) を求める.
     */
    private double sbK_byForwardRecursion(double x) {
        return this.byForwardRecursion(x, this.msbessel0.sbesselK(x), this.msbessel1.sbesselK(x));
    }

    /**
     * 前進漸化式に従って k(x)exp(x) を求める.
     */
    private double sbKc_byForwardRecursion(double x) {
        return this.byForwardRecursion(x, this.msbessel0.sbesselKc(x), this.msbessel1.sbesselKc(x));
    }

    /**
     * kの前進漸化式に従ってthis.orderでの値を求める.
     * kでもscalingKでも成立.
     * 
     * @param value0 n = 0での値
     * @param value1 n = 1での値
     */
    private double byForwardRecursion(double x, double value0, double value1) {

        //もしxが非常に小さい場合は逆数はinfになるが, 2次以上のkはすぐにinfになるので問題ない
        final double invX = 1d / x;

        double v_nu_minus_1 = value0;
        double v_nu = value1;

        for (int nu = 1; nu < this.order; nu++) {
            double v_nu_plus_1 = v_nu_minus_1 + (2 * nu + 1) * invX * v_nu;

            if (!Double.isFinite(v_nu_plus_1)) {
                return Double.POSITIVE_INFINITY;
            }

            v_nu_minus_1 = v_nu;
            v_nu = v_nu_plus_1;
        }
        return v_nu;
    }
}
