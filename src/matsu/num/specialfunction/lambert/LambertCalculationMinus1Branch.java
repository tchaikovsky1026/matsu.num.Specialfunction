/**
 * 2023.3.20
 */
package matsu.num.specialfunction.lambert;

import java.util.Objects;

import matsu.num.commons.Exponentiation;

/**
 * ランベルト関数の計算を扱う.
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
public final class LambertCalculationMinus1Branch {

    private static final LambertCalculationMinus1Branch INSTANCE = new LambertCalculationMinus1Branch();

    private LambertCalculationMinus1Branch() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    private static final double NEGATIVE_INVERSE_E = -1 / Math.E;
    private static final double EXTREME_THRESHOLD = 1E-11;

    /**
     * -1分枝の計算. {@literal 0 >= z >= -1/e}で意味を持つ.
     * 
     * @param z z
     * @return wm(z)
     */
    public double wm(double z) {
        if (!(z >= NEGATIVE_INVERSE_E && z <= 0)) {
            return Double.NaN;
        }
        if (z > -Double.MIN_NORMAL) {
            return Double.NEGATIVE_INFINITY;
        }

        double z_p_invE = z - NEGATIVE_INVERSE_E;
        if (z_p_invE < EXTREME_THRESHOLD) {
            return -1 - Exponentiation.sqrt((2 * Math.E) * z_p_invE);
        }
        final double z_w2 = -0.270670566;
        if (z < z_w2) {
            /*
             -1/e+threshold < z < -0.27では, 
             w exp(w) = z
             に対してハレー法を用いる.
             x -= f/(f' - f''f/(2f')) 
             */
            double w0 = -1 - Exponentiation.sqrt((2 * Math.E) * z_p_invE);
            final int iteration_z0 = 3;
            for (int i = 0; i < iteration_z0; i++) {
                w0 += deltaW_wExpW(w0, z);
            }
            return w0;
        }
        /*
         0 >= z >= -0.27では, 
         w + log w = log(z)
         に対してハレー法を用いる.
         x -= f/(f' - f''f/(2f')) 
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
     * z = O(1)における, w exp(w) = zに関する, ハレー法の更新量.
     */
    private double deltaW_wExpW(double w, double z) {
        double L = (w - z * Exponentiation.exp(-w)) / (1 + w);
        return -L / (1 - L * (2 + w) / (2 + 2 * w));
    }

    /**
     * z>c>1における, w + log w = log(z)に関する, ハレー法の更新量.
     */
    private double deltaW_log_negative(double w, double logMinusZ) {
        double logMinusW = Exponentiation.log(-w);

        double L = (w + logMinusW - logMinusZ) / (1 + w);
        return -w * L / (1 + L / (2 * (1 + w)));
    }

    /**
     * 
     * @return インスタンス.
     */
    public static LambertCalculationMinus1Branch instance() {
        return INSTANCE;
    }
}
