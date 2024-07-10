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
 * 高次(2次以上)の変形Bessel関数を表す. <br>
 * 第2種変形Besselについては前進漸化式を使用することが決まっているので実装されている.
 * 
 * @author Matsuura Y.
 * @version 18.3
 */
abstract class ModifiedBesselHigherOrder extends SkeletalModifiedBessel {

    /**
     * Kで計算するか, scalingKで計算するかを切り替える閾値.
     * 下側では生の値, 上側ではスケーリング値を使う.
     * 
     */
    private static final double BOUNDARY_X_SELECTING_RAW_OR_SCALING = 2d;

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

        if (x < BOUNDARY_X_SELECTING_RAW_OR_SCALING) {
            return this.valueKRecursion(x, this.mbessel0.besselK(x), this.mbessel1.besselK(x));
        } else {
            return this.valueKRecursion(x, this.mbessel0.besselKc(x), this.mbessel1.besselKc(x))
                    * Exponentiation.exp(-x);
        }
    }

    @Override
    public final double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        return this.valueKRecursion(x, this.mbessel0.besselKc(x), this.mbessel1.besselKc(x));
    }

    /**
     * Kの前進漸化式に従ってthis.orderでの値を求める.
     * KでもscalingKでも成立.
     * 
     * @param value0 n = 0での値
     * @param value1 n = 1での値
     */
    private double valueKRecursion(double x, double value0, double value1) {

        double v_n_minus_1 = value0;
        double v_n = value1;

        for (int n = 1; n < this.order; n++) {
            double v_n_plus_1 = v_n_minus_1 + (2 * n / x) * v_n;

            if (!Double.isFinite(v_n_plus_1)) {
                return Double.POSITIVE_INFINITY;
            }

            v_n_minus_1 = v_n;
            v_n = v_n_plus_1;
        }
        return v_n;
    }
}
