package matsu.num.specialfunction.subpj.gamma.lgamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;

/**
 * 対数ガンマ関数に対するStirling近似の残差を扱う. <br>
 * すなわち, f(x) = logGamma(x) - S(x) の取り扱い.
 * 
 * @author Matsuura Y.
 */
final class LGammaResidual {

    private static final double VALID_X_FOR_ASYMPTOTIC = 10d;

    /**
     * f(x) を計算する.
     * 内部的には再帰である.
     * 
     * @param x x
     * @return f(x)
     */
    public static double value(double x) {
        if (!(x > 0d)) {
            return Double.NaN;
        }

        if (x >= VALID_X_FOR_ASYMPTOTIC) {
            return valueAsymptotic(x);
        }

        return ResidualRecursion.difference(x) + value(x + 1);
    }

    /**
     * {@literal x >> 1} における f(x) の計算. <br>
     * {@literal x >> 10} でおそらくうまくいく.
     * 
     * @param x x
     * @return f(x)
     */
    private static double valueAsymptotic(double x) {

        /*
         * 漸近級数では,
         * f(x) = sum_{k=1}^{inf} B_{2k}/(2k * (2k-1)) * x^{-2k+1}
         * である.
         */

        double invX = 1 / x;

        double value = 0;
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value *= invX * invX;
            value += EvenBernoulli.evenBernoulli(k) / (2 * k * (2 * k - 1));
        }

        return value * invX;
    }

    /**
     * f(x) - f(x+1) を扱う.
     */
    private static final class ResidualRecursion {

        private final static DoubleContinuedFractionFunction fraction;

        static {
            fraction = ContinuedFractionFunction
                    .from(
                            50,
                            k -> BigRational.of(
                                    -(k + 2) * (k + 2), (k + 1) * (k + 4)),
                            BigRational.constantSupplier())
                    .asDoubleFunction();
        }

        static double difference(double x) {
            if (x < 1d) {
                return (x + 0.5) * Exponentiation.log1p(1 / x) - 1;
            }

            return fraction.value(1 / x) / (12 * x * x);
        }
    }
}
