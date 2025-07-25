/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.11
 */
package matsu.num.specialfunction.hermite;

import matsu.num.specialfunction.HermiteFunction;

/**
 * 計算のたびにパラメータを与える Laguerre 関数.
 * 
 * <p>
 * このクラスは契約が不十分であり, 公開してはならない.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class StaticHermiteFunction {

    /**
     * 唯一のコンストラクタ.
     */
    StaticHermiteFunction() {
        super();
    }

    /**
     * <i>H</i><sub><i>n</i></sub> (<i>x</i>)
     * を計算する.
     * 
     * <p>
     * 引数のバリデーションは行われていない. <br>
     * 呼び出し元で対応すること.
     * </p>
     * 
     * @param degreeN 次数 n
     * @param x 引数 x
     * @return H_n(x)
     */
    double hermiteH(int degreeN, double x) {
        assert HermiteFunction.acceptsParameter(degreeN);
        assert x >= 0;

        /*
         * 以下は, n <= 100 で適用できる.
         * 
         * H(n,x)は漸化式により計算する.
         * H(0,x) = 1,
         * H(1,x) = 2x,
         * H(n,x) = 2xH(n-1,x) - 2(n-1)H(n-2,x)
         * 
         * 途中でオーバーフローした場合は+infを返す.
         */
        final double h_0 = 1;
        if (degreeN == 0) {
            return h_0;
        }
        final double h_1 = 2 * x;
        if (degreeN == 1) {
            return h_1;
        }

        double h_pm2;
        double h_pm1 = h_0;
        double h_p = h_1;
        for (int p = 2; p <= degreeN; p++) {
            h_pm2 = h_pm1;
            h_pm1 = h_p;

            h_p = 2 * x * h_pm1 - 2 * (p - 1) * h_pm2;

            if (!Double.isFinite(h_p)) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return h_p;
    }
}
