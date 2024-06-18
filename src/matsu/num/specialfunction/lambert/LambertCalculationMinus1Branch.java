/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.18
 */
package matsu.num.specialfunction.lambert;

import matsu.num.commons.Exponentiation;

/**
 * ランベルト関数の-1分枝の計算を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.1
 */
public final class LambertCalculationMinus1Branch {

    public LambertCalculationMinus1Branch() {
    }

    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;
    private static final double EXTREME_THRESHOLD = 1E-11;
    private static final double ALGORITHM_THRESHOLD = -0.270670566;

    /**
     * -1分枝の計算. {@literal 0 >= z >= -1/e}で意味を持つ.
     * 
     * @param z z
     * @return wm(z)
     */
    public double wm(double z) {
        if (!(z >= NEGATIVE_INVERSE_E && z <= 0d)) {
            return Double.NaN;
        }
        if (z == 0d) {
            return Double.NEGATIVE_INFINITY;
        }

        //zが-1/eに非常に近い場合は, 2次近似で代用する.
        double z_p_invE = z - NEGATIVE_INVERSE_E;
        if (z_p_invE < EXTREME_THRESHOLD) {
            return -1 - Exponentiation.sqrt((2 * Math.E) * z_p_invE);
        }

        /*
         * z < -0.27では,
         * ｆ（w） = w exp(w) - z = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        if (z < ALGORITHM_THRESHOLD) {
            double w0 = -1 - Exponentiation.sqrt((2 * Math.E) * z_p_invE);
            final int iteration_z0 = 3;
            for (int i = 0; i < iteration_z0; i++) {
                w0 += deltaW_wExpW(w0, z);
            }
            return w0;
        }

        /*
         * -0.27 <= z <= 0 では,
         * f(w) = w + log w - log(z) = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        double logMinusZ = Exponentiation.log(-z);
        if (logMinusZ == Double.POSITIVE_INFINITY) {
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
     * f(w) = w + log w - log(z) = 0
     * に関するハレー法の更新量.
     */
    private double deltaW_log_negative(double w, double logMinusZ) {
        double logMinusW = Exponentiation.log(-w);

        double L = (w + logMinusW - logMinusZ) / (1 + w);
        return -w * L / (1 + L / (2 * (1 + w)));
    }
}
