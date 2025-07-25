/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.25
 */
package matsu.num.specialfunction.chebyshev;

import matsu.num.specialfunction.ChebyshevFunction;

/**
 * 計算のたびに次数を与える Chebyshev 関数.
 * 
 * <p>
 * このクラスは契約が不十分であり, 公開してはならない.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class StaticChebyshevFunction {

    /**
     * 唯一のコンストラクタ.
     */
    StaticChebyshevFunction() {
        super();
    }

    /**
     * <i>T</i><sub><i>n</i></sub> (<i>x</i>)
     * を計算する.
     * 
     * <p>
     * 引数のバリデーションは行われていない. <br>
     * 呼び出し元でチェックすること.
     * </p>
     * 
     * 
     * @param degreeN 次数 n
     * @param x 引数 x
     * @return T_n(x)
     */
    double chebyshevT(int degreeN, double x) {
        assert ChebyshevFunction.acceptsParameter(degreeN);
        assert -1 <= x && x <= 1;

        /*
         * 以下は, n <= 100 で適用できる.
         * 
         * T(n,x)は漸化式により計算する.
         * T(0,x) = 1,
         * T(1,x) = x,
         * T(n,x) = 2xT(n-1,x) - T(n-2,x)
         */
        final double t_0 = 1;
        if (degreeN == 0) {
            return t_0;
        }
        final double t_1 = x;
        if (degreeN == 1) {
            return t_1;
        }

        double t_pm2;
        double t_pm1 = t_0;
        double t_p = t_1;
        for (int p = 2; p <= degreeN; p++) {
            t_pm2 = t_pm1;
            t_pm1 = t_p;

            t_p = 2 * x * t_pm1 - t_pm2;
        }
        return t_p;
    }

    /**
     * <i>U</i><sub><i>n</i></sub> (<i>x</i>)
     * を計算する.
     * 
     * <p>
     * 引数のバリデーションは行われていない. <br>
     * 呼び出し元でチェックすること.
     * </p>
     * 
     * @param degreeN 次数 n
     * @param x 引数 x
     * @return U_n(x)
     */
    double chebyshevU(int degreeN, double x) {
        assert ChebyshevFunction.acceptsParameter(degreeN);
        assert -1 <= x && x <= 1;

        /*
         * 以下は, n <= 100 で適用できる.
         * 
         * U(n,x)は漸化式により計算する.
         * U(0,x) = 1,
         * U(1,x) = 2x,
         * U(n,x) = 2xU(n-1,x) - U(n-2,x)
         */
        final double u_0 = 1;
        if (degreeN == 0) {
            return u_0;
        }
        final double u_1 = 2 * x;
        if (degreeN == 1) {
            return u_1;
        }

        double u_pm2;
        double u_pm1 = u_0;
        double u_p = u_1;
        for (int p = 2; p <= degreeN; p++) {
            u_pm2 = u_pm1;
            u_pm1 = u_p;

            u_p = 2 * x * u_pm1 - u_pm2;
        }
        return u_p;
    }
}
