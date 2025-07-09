/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.9
 */
package matsu.num.specialfunction.legendre;

import matsu.num.specialfunction.LegendreFunction;

/**
 * 計算のたびにパラメータを与える Legendre 関数.
 * 
 * <p>
 * このクラスは契約が不十分であり, 公開してはならない.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class StaticLegendreFunction {

    /**
     * 唯一のコンストラクタ.
     */
    StaticLegendreFunction() {
        super();
    }

    /**
     * <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
     * を計算する.
     * 
     * <p>
     * 引数のバリデーションは行われていない. <br>
     * 呼び出し元でチェックすること.
     * </p>
     * 
     * 
     * @param degreeL 次数 l
     * @param orderM 階数 m
     * @param x 引数 x
     * @return P_l^m(x)
     */
    double legendreP(int degreeL, int orderM, double x) {
        assert LegendreFunction.acceptsParameter(degreeL, orderM);
        assert -1 <= x && x <= 1;

        return 0d;
    }
}
