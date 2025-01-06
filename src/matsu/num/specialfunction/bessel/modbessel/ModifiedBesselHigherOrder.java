/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.specialfunction.common.Exponentiation;

/**
 * 高次(2次以上)の変形Bessel関数を表す. <br>
 * 前進漸化式による第2種変形Besselの計算が実装されている.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class ModifiedBesselHigherOrder extends SkeletalModifiedBessel {

    /**
     * 前進漸化式によるK(x)について, 生値で進めるか, スケーリング値で進めるかを切り替えるxの閾値. <br>
     * 下側は生値, 上側はスケーリング値.
     */
    private static final double BOUNDARY_X_SELECTING_RAW_OR_SCALING_FOR_BESSEL_K = 2d;

    protected final int order;

    protected final ModifiedBessel0thOrder mbessel0;
    protected final ModifiedBessel1stOrder mbessel1;

    /**
     * 与えた次数の変形ベッセル関数を生成する.
     * 次数は2以上100以下でなければならない.
     * 
     * @param order 次数
     * @param mbessel0 0次変形ベッセル
     * @param mbessel1 1次変形ベッセル
     */
    ModifiedBesselHigherOrder(int order, ModifiedBessel0thOrder mbessel0, ModifiedBessel1stOrder mbessel1) {
        super();
        this.order = order;
        this.mbessel0 = mbessel0;
        this.mbessel1 = mbessel1;
    }

    @Override
    public final int order() {
        return this.order;
    }

    @Override
    public final double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_RAW_OR_SCALING_FOR_BESSEL_K) {
            return this.besselK_byForwardRecursion(x);
        } else {
            return this.besselKc_byForwardRecursion(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public final double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        return this.besselKc_byForwardRecursion(x);
    }

    /**
     * 前進漸化式に従って K(x) を求める.
     */
    private double besselK_byForwardRecursion(double x) {
        return this.byForwardRecursion(x, this.mbessel0.besselK(x), this.mbessel1.besselK(x));
    }

    /**
     * 前進漸化式に従って K(x)exp(x) を求める.
     */
    private double besselKc_byForwardRecursion(double x) {
        return this.byForwardRecursion(x, this.mbessel0.besselKc(x), this.mbessel1.besselKc(x));
    }

    /**
     * Kの前進漸化式に従ってthis.orderでの値を求める.
     * KでもscalingKでも成立.
     * 
     * @param value0 n = 0での値
     * @param value1 n = 1での値
     */
    private double byForwardRecursion(double x, double value0, double value1) {

        //もしxが非常に小さい場合は逆数はinfになるが, 2次以上のKはすぐにinfになるので問題ない
        final double doubleInvX = 2d / x;

        double v_nu_minus_1 = value0;
        double v_nu = value1;

        for (int n = 1; n < this.order; n++) {
            double v_nu_plus_1 = v_nu_minus_1 + n * doubleInvX * v_nu;

            if (!Double.isFinite(v_nu_plus_1)) {
                return Double.POSITIVE_INFINITY;
            }

            v_nu_minus_1 = v_nu;
            v_nu = v_nu_plus_1;
        }
        return v_nu;
    }
}
