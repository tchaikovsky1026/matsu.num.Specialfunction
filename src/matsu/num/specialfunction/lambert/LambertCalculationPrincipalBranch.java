/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.14
 */
package matsu.num.specialfunction.lambert;

import matsu.num.commons.Exponentiation;

/**
 * Lambert関数の主枝の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 19.3
 */
public final class LambertCalculationPrincipalBranch {

    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;
    private static final double EXTREME_THRESHOLD = 1E-11;
    private static final double ALGORITHM_THRESHOLD = 10d;

    /**
     * 唯一のコンストラクタ.
     */
    public LambertCalculationPrincipalBranch() {
        super();
    }

    /**
     * 主枝の計算. {@literal z >= -1/e}で意味を持つ.
     * 
     * @param z z
     * @return wp(z)
     */
    public double wp(double z) {
        if (!(z >= NEGATIVE_INVERSE_E)) {
            return Double.NaN;
        }

        //zが-1/eに非常に近い場合は, 2次近似で代用する.
        double z_p_invE = z - NEGATIVE_INVERSE_E;
        if (z_p_invE < EXTREME_THRESHOLD) {
            return -1 + Exponentiation.sqrt((2 * Math.E) * z_p_invE);
        }

        /*
         * z < 10では,
         * f(w) = w exp(w) - z = 0
         * に対してハレー法を用いる.
         * w_new = w_old - f/(f' - f''f/(2f'))
         */
        if (z <= ALGORITHM_THRESHOLD) {
            double w0 = z < 0
                    ? -1 + Exponentiation.sqrt((2 * Math.E) * z_p_invE)
                    : Exponentiation.log(z + 1);
            final int iteration_z0 = 3;
            for (int i = 0; i < iteration_z0; i++) {
                w0 += deltaW_wExpW(w0, z);
            }
            return w0;
        }

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
