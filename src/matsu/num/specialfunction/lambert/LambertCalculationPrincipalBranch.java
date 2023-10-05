/**
 * 2023.3.20
 */
package matsu.num.specialfunction.lambert;

import java.util.Objects;

import matsu.num.commons.Exponentiation;

/**
 * ランベルト関数の主枝を計算する.
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
public final class LambertCalculationPrincipalBranch {
    private static final LambertCalculationPrincipalBranch INSTANCE = new LambertCalculationPrincipalBranch();

    private LambertCalculationPrincipalBranch() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;
    private static final double EXTREME_THRESHOLD = 1E-11;

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

        double z_p_invE = z - NEGATIVE_INVERSE_E;
        if (z_p_invE < EXTREME_THRESHOLD) {
            return -1 + Exponentiation.sqrt((2 * Math.E) * z_p_invE);
        }
        if (z <= 10) {
            /*
             z < 10では, 
             w exp(w) = z
             に対してハレー法を用いる.
             x -= f/(f' - f''f/(2f')) 
             */
            double w0 = z < 0 ? -1 + Exponentiation.sqrt((2 * Math.E) * z_p_invE) : Exponentiation.log(z + 1);
            final int iteration_z0 = 3;
            for (int i = 0; i < iteration_z0; i++) {
                w0 += deltaW_wExpW(w0, z);
            }
            return w0;
        }

        /*
         z >= 10では, 
         w + log w = log(z)
         に対してハレー法を用いる.
         x -= f/(f' - f''f/(2f')) 
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
     * z = O(1)における, w exp(w) = zに関する, ハレー法の更新量.
     */
    private double deltaW_wExpW(double w, double z) {
        double L = (w - z * Exponentiation.exp(-w)) / (1 + w);
        return -L / (1 - L * (2 + w) / (2 + 2 * w));
    }

    /**
     * z>c>1における, w + log w = log(z)に関する, ハレー法の更新量.
     */
    private double deltaW_log_positive(double w, double logZ) {
        double logW = Exponentiation.log(w);

        double L = (w + logW - logZ) / (1 + w);
        return -w * L / (1 + L / (2 * (1 + w)));
    }

    /**
     * 
     * @return インスタンス.
     */
    public static LambertCalculationPrincipalBranch instance() {
        return INSTANCE;
    }
}
