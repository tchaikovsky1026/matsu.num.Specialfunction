/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.bessel;

import matsu.num.specialfunction.bessel.basecomponent.InverseFactorialSupplier;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 高次のBessel関数で, besselJの実装を加え完成したクラス.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class BesselOver2 extends BesselHigherImplY {

    /**
     * J(x)についてアルゴリズムを切り替えるxの下側の閾値. <br>
     * 下側はべき級数, 上側は後退漸化式.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION = 2d;

    /**
     * J(x)についてアルゴリズムを切り替えるxの上側の閾値. <br>
     * 下側は後退漸化式, 上側は前進漸化式.
     */
    private final double boundaryX_selectingBackOrForwardRecursion;

    /**
     * {@literal x < 2} の計算はべき級数で行うが, その時に使う項のindex値.<br>
     * 第12項までを計算する, おそらく過剰.
     */
    private static final int K_MAX_BY_POWER = 12;

    /**
     * {@literal 2 <= x < n} では逆方向漸化式を使う.
     * その時のNの値. <br>
     * Nはxに依存したほうが効率は良さそうだが, 今は最悪の場合: {@literal x = n}
     * について考慮し, ｘに依存させない.
     */
    private final int upperN_byBackRecursion;

    /**
     * 1/n!の値.
     */
    private final double invOfNFactorial;

    /**
     * 与えた次数のベッセル関数を生成する.
     * 
     * @param n 次数
     * @param bessel0 0次ベッセル
     * @param bessel1 1次ベッセル
     */
    BesselOver2(int n, Bessel0th bessel0, Bessel1st bessel1) {
        super(n, bessel0, bessel1);
        this.invOfNFactorial = InverseFactorialSupplier.get(n);
        this.upperN_byBackRecursion = this.calcUpperN_byBackRecursion();
        this.boundaryX_selectingBackOrForwardRecursion =
                this.calcBoundaryX_selectingBackOrForwardRecursion();
    }

    @Override
    public final double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_BACK_RECURSION) {
            return this.bJ_byPower(x);
        }

        if (x < this.boundaryX_selectingBackOrForwardRecursion) {
            return this.bJ_byBackRecursion(x);
        }

        return this.bJ_byForwardRecursion(x);
    }

    /**
     * べき級数によりJ(x)を計算する.
     */
    private double bJ_byPower(double x) {

        double halfX = x / 2;
        double minusSquareHalfX = -halfX * halfX;

        //jMax+1からスタート(最初の項は0で考慮されないため)
        double value = 0;
        for (int j = K_MAX_BY_POWER + 1; j >= 1; j--) {
            value *= minusSquareHalfX / (j * (j + order));
            value += 1;
        }

        return Exponentiation.pow(halfX, order) * invOfNFactorial * value;
    }

    /**
     * 逆方向漸化式によりJ(x)を計算する.
     */
    private double bJ_byBackRecursion(double x) {

        final double doubleInvX = 2 / x;
        final double init = 1E-200;

        double j_nu_plus_1 = 0;
        double j_nu = init;
        for (int j = this.upperN_byBackRecursion; j > order; j--) {
            double j_nu_minus_1 = j_nu * j * doubleInvX - j_nu_plus_1;
            j_nu_plus_1 = j_nu;
            j_nu = j_nu_minus_1;

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

        for (int j = order; j > 0; j--) {
            double j_nu_minus_1 = j_nu * j * doubleInvX - j_nu_plus_1;
            if (!Double.isFinite(j_nu_minus_1)) {
                return 0d;
            }
            j_nu_plus_1 = j_nu;
            j_nu = j_nu_minus_1;
        }

        return Math.abs(j_nu_plus_1) > Math.abs(j_nu)
                ? this.bessel1.besselJ(x) / j_nu_plus_1 * j_order
                : this.bessel0.besselJ(x) / j_nu * j_order;
    }

    /**
     * 順方向漸化式によりJ(x)を計算する.
     */
    private double bJ_byForwardRecursion(double x) {

        final double doubleInvX = 2d / x;

        double j_nu_minus_1 = this.bessel0.besselJ(x);
        double j_nu = this.bessel1.besselJ(x);

        for (int j = 1; j < order; j++) {
            double j_nu_plus_1 = j_nu * j * doubleInvX - j_nu_minus_1;
            j_nu_minus_1 = j_nu;
            j_nu = j_nu_plus_1;
        }
        return j_nu;
    }

    /**
     * 逆方向漸化式と順方向漸化式を切り替えるxの閾値.
     */
    private double calcBoundaryX_selectingBackOrForwardRecursion() {
        return this.order;
    }

    /**
     * 逆方向漸化式で使う, J/Jに比べてY/Yが無視できる次数Nを返す.
     */
    private int calcUpperN_byBackRecursion() {
        //n<=100, 倍精度に特化した数式
        //N = n + 8log(n) + 3
        return order + 1 + (int) (8 * Exponentiation.log(order + 3));
    }
}
