/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.9
 */
package matsu.num.specialfunction.modbessel;

/**
 * 高次(2次以上)の変形Bessel関数を表す. <br>
 * 第2種変形Besselについては前進漸化式を使用することが決まっているので実装されている.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
abstract class ModifiedBesselHigherOrder extends SkeletalModifiedBessel {

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

        double k_n_minus_1 = this.mbessel0.besselK(x);
        double k_n = this.mbessel1.besselK(x);
        if (!(Double.isFinite(k_n_minus_1) && Double.isFinite(k_n))) {
            return Double.POSITIVE_INFINITY;
        }

        for (int n = 1; n < this.order; n++) {
            double k_n_plus_1 = k_n_minus_1 + (2 * n / x) * k_n;

            if (!Double.isFinite(k_n_plus_1)) {
                return Double.POSITIVE_INFINITY;
            }

            k_n_minus_1 = k_n;
            k_n = k_n_plus_1;
        }
        return k_n;
    }
}
