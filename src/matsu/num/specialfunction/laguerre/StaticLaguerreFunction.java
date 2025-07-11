/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.11
 */
package matsu.num.specialfunction.laguerre;

import matsu.num.specialfunction.LaguerreFunction;

/**
 * 計算のたびにパラメータを与える Laguerre 関数.
 * 
 * <p>
 * このクラスは契約が不十分であり, 公開してはならない.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class StaticLaguerreFunction {

    /**
     * 唯一のコンストラクタ.
     */
    StaticLaguerreFunction() {
        super();
    }

    /**
     * <i>L</i><sub><i>n</i></sub><sup><i>k</i></sup> (<i>x</i>)
     * を計算する.
     * 
     * <p>
     * 引数のバリデーションは行われていない. <br>
     * 呼び出し元で対応すること.
     * </p>
     * 
     * @param degreeN 次数 n
     * @param orderK 階数 k
     * @param x 引数 x
     * @return L_n^k(x)
     */
    double laguerreL(int degreeN, int orderK, double x) {
        assert LaguerreFunction.acceptsParameter(degreeN, orderK);
        assert x >= 0;

        /*
         * 以下は, n <= 100, k <= 100 で適用できる.
         * 
         * L(n,k,x)は漸化式により計算する.
         * L(0,k,x) = 1,
         * L(1,k,x) = k+1-x,
         * L(n,k,x) * n = (2n-1+k-x)L(n-1,k,x) - (n-1+k)L(n-2,k,x)
         * 
         * 途中でオーバーフローした場合は符号を(-1)^nとしてinfを返す.
         */
        final double l_0_k = 1;
        if (degreeN == 0) {
            return l_0_k;
        }
        final double l_1_k = (orderK + 1) - x;
        if (degreeN == 1) {
            return l_1_k;
        }

        double l_pm2_k;
        double l_pm1_k = l_0_k;
        double l_p_k = l_1_k;
        for (int p = 2; p <= degreeN; p++) {
            l_pm2_k = l_pm1_k;
            l_pm1_k = l_p_k;

            // オーバーフロー対策で、p除算を先に行う
            l_p_k = (((2 * p + orderK - 1) - x) / p) * l_pm1_k - ((double) (p + orderK - 1) / p) * l_pm2_k;

            if (!Double.isFinite(l_p_k)) {
                return (degreeN & 1) == 0
                        ? Double.POSITIVE_INFINITY
                        : Double.NEGATIVE_INFINITY;
            }
        }
        return l_p_k;
    }
}
