/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.lambert;

import matsu.num.specialfunction.common.Exponentiation;

/**
 * Lambert関数の-1分枝(Wm)の計算を扱う.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class LambertWmImplByHalley implements LambertWmCalculation {

    /**
     * -1/e, Wmの定義域の下限.
     */
    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;

    /**
     * w exp(w) = z の 左辺が (w+1) の2次近似になるようなzの上限.
     */
    private static final double EXTREME_THRESHOLD = NEGATIVE_INVERSE_E + 1E-11;

    /**
     * 逆関数の反復的求解に用いる式を切り替える閾値. <br>
     * 下側では w exp(w) - z = 0 に対する反復,
     * 上側では w + log(-w) - log(-z) = 0 に対する反復.
     */
    private static final double ALGORITHM_THRESHOLD = -0.270670566;

    /**
     * 唯一のコンストラクタ.
     */
    public LambertWmImplByHalley() {
        super();
    }

    @Override
    public double wm(double z) {
        if (!(z >= NEGATIVE_INVERSE_E && z <= 0d)) {
            return Double.NaN;
        }

        if (z < EXTREME_THRESHOLD) {
            return wm_near_negativeInvE(z);
        }
        if (z < ALGORITHM_THRESHOLD) {
            return wm_smallZ(z);
        }
        return wm_largeZ(z);
    }

    /**
     * zが-1/eに近いときのWm(z)を計算する. <br>
     * 注意として, z = -1/e のとき, w = -1 は重根であるので有効桁数は半分 (およそ8, 9桁) である.
     * 
     * @param z z
     * @return Wm(z)
     */
    private double wm_near_negativeInvE(double z) {
        assert z >= NEGATIVE_INVERSE_E;
        assert z <= EXTREME_THRESHOLD;

        return wm_lower_extreme(z);
    }

    /**
     * {@literal z -> -1/e} のWm(z)の解析値. <br>
     * w*exp(w) = z を w = -1 のまわりで2次近似して計算.
     */
    private double wm_lower_extreme(double z) {
        return -1 - Exponentiation.sqrt((2 * Math.E) * (z - NEGATIVE_INVERSE_E));
    }

    /**
     * zが0から遠いときのWm(z)を計算する. <br>
     * w exp(w) - z = 0 に対する反復.
     * 
     * @param z z
     * @return Wm(z)
     */
    private double wm_smallZ(double z) {
        assert z >= NEGATIVE_INVERSE_E;
        assert z <= ALGORITHM_THRESHOLD;

        double w0 = wm_lower_extreme(z);

        /*
         * z<-0.27のとき,
         * f(w) = w exp(w) - z = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        final int iteration_z0 = 3;
        for (int i = 0; i < iteration_z0; i++) {
            w0 += deltaW_wExpW(w0, z);
        }
        return w0;
    }

    /**
     * zが0に近づいたときのWm(z)を計算する. <br>
     * w + log(-w) - log(-z) = 0 に対する反復.
     * 
     * @param z z
     * @return Wm(z)
     */
    private double wm_largeZ(double z) {
        assert z >= ALGORITHM_THRESHOLD;
        assert z <= 0d;

        /*
         * -0.27 <= z <= 0 では,
         * f(w) = w + log(-w) - log(-z) = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        double logMinusZ = Exponentiation.log(-z);
        if (logMinusZ == Double.NEGATIVE_INFINITY) {
            return Double.NEGATIVE_INFINITY;
        }
        final int iteration_z10 = 3;
        double w0 = logMinusZ - 1;
        for (int i = 0; i < iteration_z10; i++) {
            w0 += deltaW_log_negative(w0, logMinusZ);
        }
        return w0;
    }

    /**
     * f(w) = w exp(w) - z = 0
     * に関するハレー法の更新量.
     */
    private double deltaW_wExpW(double w, double z) {
        double L = (w - z * Exponentiation.exp(-w)) / (1 + w);
        return -L / (1 - L * (2 + w) / (2 + 2 * w));
    }

    /**
     * f(w) = w + log(-w) - log(-z) = 0
     * に関するハレー法の更新量.
     */
    private double deltaW_log_negative(double w, double logMinusZ) {
        double logMinusW = Exponentiation.log(-w);

        double L = (w + logMinusW - logMinusZ) / (1 + w);
        return -w * L / (1 + L / (2 * (1 + w)));
    }
}
