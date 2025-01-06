/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.bessel;

/**
 * 高次のBessel関数を表現し, besselYの実装を提供する. <br>
 * 2から100次まで.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class BesselHigherImplY extends SkeletalBessel {

    /**
     * このBessel関数の次数.
     */
    protected final int order;

    protected final Bessel0th bessel0;
    protected final Bessel1st bessel1;

    /**
     * 与えた次数のBessel関数を生成する. <br>
     * 次数は2以上100以下でなければならない.
     * 
     * @param n 次数
     * @param bessel0 0次Bessel
     * @param bessel1 1次Bessel
     */
    BesselHigherImplY(int n, Bessel0th bessel0, Bessel1st bessel1) {
        super();
        this.order = n;
        this.bessel0 = bessel0;
        this.bessel1 = bessel1;
    }

    @Override
    public final int order() {
        return this.order;
    }

    @Override
    public final double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        return this.bY_byForwardRecursion(x);
    }

    /**
     * 前進漸化式によりYを計算する.
     */
    private double bY_byForwardRecursion(double x) {

        //もしxが非常に小さい場合は逆数はinfになるが, 2次以上のYはすぐにinfになるので問題ない
        final double doubleInvX = 2 / x;

        double y0 = this.bessel0.besselY(x);
        double y1 = this.bessel1.besselY(x);

        for (int j = 1; j < order; j++) {
            double y2 = y1 * j * doubleInvX - y0;
            if (!Double.isFinite(y2)) {
                return Double.NEGATIVE_INFINITY;
            }
            y0 = y1;
            y1 = y2;
        }
        return y1;
    }
}
