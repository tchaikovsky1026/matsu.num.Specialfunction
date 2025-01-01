/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.sbessel;

import matsu.num.specialfunction.bessel.basecomponent.InverseDoubleFactorialSupplier;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 次数2以上の球Bessel関数を扱う. <br>
 * このクラスでは第1種球Besselを実装し, クラスが完成する. <br>
 * 第1種球Besselでは,
 * {@literal x < 2} はべき級数を,
 * {@literal 2 <= x < n} は後退漸化式を,
 * それ以上では前進漸化式を用いる.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
final class SBesselOver2 extends SBesselHigher {

    /**
     * j(x)についてアルゴリズムを切り替えるxの下側の閾値. <br>
     * 下側はべき級数, 上側は後退漸化式.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION = 2d;

    /**
     * j(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_S_BESSEL_J_BY_POWER = 10;

    /**
     * j(x)についてアルゴリズムを切り替えるxの上側の閾値. <br>
     * 下側は後退漸化式, 上側は前進漸化式.
     */
    private final double boundaryX_selectingBackOrForwardRecursion;

    /**
     * j(x)の後退漸化式における, 考慮するべき次数.
     */
    private final int upperN_byBackRecursion;

    /**
     * 次数をnとして, 1/(2n+1)!!
     */
    private final double inverseDoubleFactorial;

    /**
     * 与えた次数の球ベッセル関数を生成する.
     * 次数は2以上100以下でなければならない.
     * 
     * @param order 次数
     * @param sbessel0 0次球ベッセル
     * @param sbessel1 1次球ベッセル
     */
    protected SBesselOver2(int order, SBessel0 sbessel0, SBessel1 sbessel1) {
        super(order, sbessel0, sbessel1);

        this.upperN_byBackRecursion =
                this.calcUpperN_byBackRecursion();
        this.boundaryX_selectingBackOrForwardRecursion =
                this.calcBoundaryX_selectingBackOrForwardRecursion();
        this.inverseDoubleFactorial = InverseDoubleFactorialSupplier.get(this.order);
    }

    @Override
    public double sbesselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.byPower(x);
        }
        if (x < this.boundaryX_selectingBackOrForwardRecursion) {
            return this.byBackRecursion(x);
        }

        return this.byForwardRecursion(x);
    }

    /**
     * べき級数によりsbeselJを計算する.
     * 
     * @param x x
     * @return sbJ
     */
    private double byPower(double x) {

        final double squareX = x * x;

        final int n2_p_1 = 2 * this.order + 1;
        double value = 0;
        for (int k = K_MAX_FOR_S_BESSEL_J_BY_POWER + 1; k >= 1; k--) {
            value *= -squareX / ((2 * k) * (2 * k + n2_p_1));
            value += 1d;
        }

        return value * Exponentiation.pow(x, this.order) * this.inverseDoubleFactorial;
    }

    /**
     * 後退漸化式によりsbeselJを計算する.
     * 
     * @param x x
     * @return sbJ
     */
    private double byBackRecursion(double x) {

        final double invX = 1d / x;
        final double init = 1E-280;

        double j_nu_plus_1 = 0d;
        double j_nu = init;

        for (int nu = this.upperN_byBackRecursion; nu > this.order; nu--) {
            double j_nu_m_1 = -j_nu_plus_1 + (2 * nu + 1) * invX * j_nu;

            j_nu_plus_1 = j_nu;
            j_nu = j_nu_m_1;

            //オーバーフロー対策
            if (Math.abs(j_nu) >= 1E200) {
                j_nu_plus_1 = (j_nu_plus_1 / j_nu) * init;
                j_nu = init;
            }
        }

        {
            //規格化
            double scale = Math.max(Math.abs(j_nu_plus_1), Math.abs(j_nu));
            j_nu = j_nu / scale * init;
            j_nu_plus_1 = j_nu_plus_1 / scale * init;
        }

        double j_order = j_nu;

        for (int nu = this.order; nu > 0; nu--) {
            double j_nu_m_1 = -j_nu_plus_1 + (2 * nu + 1) * invX * j_nu;
            if (!Double.isFinite(j_nu_m_1)) {
                return 0d;
            }

            j_nu_plus_1 = j_nu;
            j_nu = j_nu_m_1;
        }

        return Math.abs(j_nu) > Math.abs(j_nu_plus_1)
                ? this.sbessel0.sbesselJ(x) / j_nu * j_order
                : this.sbessel1.sbesselJ(x) / j_nu_plus_1 * j_order;
    }

    /**
     * 前進漸化式によりsbeselJを計算する.
     * 
     * @param x x
     * @return sbJ
     */
    private double byForwardRecursion(double x) {
        final double invX = 1d / x;

        double j_nu_minus_1 = this.sbessel0.sbesselJ(x);
        double j_nu = this.sbessel1.sbesselJ(x);

        for (int n = 1; n < this.order; n++) {
            double j_nu_plus_1 = -j_nu_minus_1 + (2 * n + 1) * invX * j_nu;

            if (!Double.isFinite(j_nu_plus_1)) {
                return Double.NEGATIVE_INFINITY;
            }

            j_nu_minus_1 = j_nu;
            j_nu = j_nu_plus_1;
        }
        return j_nu;
    }

    /**
     * 逆方向と順方向の漸化式を切り替えるxを返す.
     * 
     * @return 逆方向と順方向の切り替えx
     */
    private double calcBoundaryX_selectingBackOrForwardRecursion() {
        return this.order;
    }

    /**
     * 逆方向漸化式におけるNを計算する
     * 
     * @return N
     */
    private int calcUpperN_byBackRecursion() {
        //n<=100, 倍精度に特化した数式
        //N = n + ceil(8log(n) + 3)
        return order + 1 + (int) (8 * Exponentiation.log(order + 3));
    }
}
