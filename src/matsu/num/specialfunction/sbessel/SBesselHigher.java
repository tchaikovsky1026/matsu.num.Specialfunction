/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.16
 */
package matsu.num.specialfunction.sbessel;

/**
 * 高次(2次以上)の球Bessel関数を表す. <br>
 * 前進漸化式による第2種変形Besselの計算が実装されている.
 * 
 * @author Matsuura Y.
 * @version 18.7
 */
abstract class SBesselHigher extends SkeletalSBessel {

    protected final int order;

    protected final SBessel0 sbessel0;
    protected final SBessel1 sbessel1;

    /**
     * 与えた次数の球ベッセル関数を生成する.
     * 次数は2以上100以下でなければならない.
     * 
     * @param order 次数
     * @param sbessel0 0次球ベッセル
     * @param sbessel1 1次球ベッセル
     */
    protected SBesselHigher(int order, SBessel0 sbessel0, SBessel1 sbessel1) {
        super();
        this.order = order;
        this.sbessel0 = sbessel0;
        this.sbessel1 = sbessel1;
    }

    @Override
    public final int order() {
        return this.order;
    }

    @Override
    public final double sbesselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        return sbesselY_byForwardRecursion(x);
    }

    private double sbesselY_byForwardRecursion(double x) {
        double y_nu_minus_1 = this.sbessel0.sbesselY(x);
        double y_nu = this.sbessel1.sbesselY(x);

        for (int n = 1; n < this.order; n++) {
            double y_nu_plus_1 = -y_nu_minus_1 + ((2 * n + 1) / x) * y_nu;

            if (!Double.isFinite(y_nu_plus_1)) {
                return Double.NEGATIVE_INFINITY;
            }

            y_nu_minus_1 = y_nu;
            y_nu = y_nu_plus_1;
        }
        return y_nu;
    }
}
