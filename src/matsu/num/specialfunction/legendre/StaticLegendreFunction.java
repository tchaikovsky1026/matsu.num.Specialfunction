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
import matsu.num.specialfunction.common.Exponentiation;

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

        /*
         * 漸化式によりP(l,m,x)を計算する.
         * 
         * P(m,m,x) = (-1)^m (2m-1)!! (1-x^2)^(m/2)
         * P(m+1,m,x) = (2m+1)xP(m,m,x)
         * P(l,m,x) * (l-m)= (2l-1)xP(l-1,m,x) - (l+m-1)P(l-2,m,x)
         */
        final double p_m_m = legendreP_mm(orderM, x);
        if (degreeL == orderM) {
            return p_m_m;
        }
        final double p_mp1_m = (2 * orderM + 1) * x * p_m_m;
        if (degreeL == orderM + 1) {
            return p_mp1_m;
        }

        double p_km2_m;
        double p_km1_m = p_m_m;
        double p_k_m = p_mp1_m;
        for (int k = orderM + 2; k <= degreeL; k++) {
            p_km2_m = p_km1_m;
            p_km1_m = p_k_m;

            p_k_m = ((2 * k - 1) * x * p_km1_m - (k + orderM - 1) * p_km2_m)
                    / (k - orderM);
        }
        return p_k_m;
    }

    private double legendreP_mm(int m, double x) {

        // sqrt((1-x)(1+x))
        double sqrt_1mx2 = Exponentiation.sqrt((1 - x) * (1 + x));

        double v = 1d;
        for (int i = m; i >= 1; i--) {
            v *= -(2 * i - 1) * sqrt_1mx2;
        }

        return v;
    }
}
