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
 * Lambert関数の主枝W0の計算を実行する.
 * 
 * @author Matsuura Y.
 */
final class LambertWpImplByHalley implements LambertWpCalculation {

    /**
     * -1/e, W0の定義域の下限.
     */
    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;

    /**
     * w exp(w) = z の 左辺が (w+1) の2次近似になるようなzの上限.
     */
    private static final double EXTREME_THRESHOLD = NEGATIVE_INVERSE_E + 1E-11;

    /**
     * 逆関数の反復的求解に用いる式を切り替える閾値. <br>
     * 下側では w exp(w) - z = 0 に対する反復,
     * 上側では w + log w - log(z) = 0 に対する反復.
     */
    private static final double ALGORITHM_THRESHOLD = 10d;

    /**
     * 唯一のコンストラクタ.
     */
    public LambertWpImplByHalley() {
        super();
    }

    @Override
    public double wp(double z) {
        if (!(z >= NEGATIVE_INVERSE_E)) {
            return Double.NaN;
        }

        if (z < EXTREME_THRESHOLD) {
            return wp_near_negativeInvE(z);
        }
        if (z <= ALGORITHM_THRESHOLD) {
            return wp_smallZ(z);
        }
        return wp_largeZ(z);
    }

    /**
     * zが-1/eに近いときのW0(z)を計算する. <br>
     * 注意として, z = -1/e のとき, w = -1 は重根であるので有効桁数は半分 (およそ8, 9桁) である.
     * 
     * @param z z
     * @return W0(z)
     */
    private double wp_near_negativeInvE(double z) {
        assert z >= NEGATIVE_INVERSE_E;
        assert z <= EXTREME_THRESHOLD;

        return wp_lower_extreme(z);
    }

    /**
     * {@literal z -> -1/e} のW0(z)の解析値. <br>
     * w*exp(w) = z を w = -1 のまわりで2次近似して計算.
     */
    private double wp_lower_extreme(double z) {
        return -1 + Exponentiation.sqrt((2 * Math.E) * (z - NEGATIVE_INVERSE_E));
    }

    /**
     * zが比較的小さいときのW0(z)を計算する. <br>
     * w exp(w) - z = 0 に対する反復.
     * 
     * @param z z
     * @return W0(z)
     */
    private double wp_smallZ(double z) {
        assert z >= NEGATIVE_INVERSE_E;
        assert z <= ALGORITHM_THRESHOLD;

        /*
         * z sim -1/e と z >> 1 の極限を参考に, 初期値を定める.
         * 閾値は経験則.
         */
        double w0 = z < -0.25
                ? wp_lower_extreme(z)
                : Exponentiation.log1p(z);

        /*
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
     * zが大きいときのW0(z)を計算する. <br>
     * w + log w - log(z) = 0 に対する反復.
     * 
     * @param z z
     * @return W0(z)
     */
    private double wp_largeZ(double z) {
        assert z >= ALGORITHM_THRESHOLD;

        /*
         * z >= 10では,
         * f(w) = w + log w - log(z) = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        final int iteration_z10 = 3;
        double logZ = Exponentiation.log(z);
        if (logZ == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        double w0 = logZ;
        for (int i = 0; i < iteration_z10; i++) {
            w0 += deltaW_log_positive(w0, logZ);
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
     * f(w) = w + log w - log(z) = 0
     * に関するハレー法の更新量.
     */
    private double deltaW_log_positive(double w, double logZ) {
        double logW = Exponentiation.log(w);

        double L = (w + logW - logZ) / (1 + w);
        return -w * L / (1 + L / (2 * (1 + w)));
    }
}
