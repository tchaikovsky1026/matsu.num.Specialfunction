package matsu.num.specialfunction.subpj.gamma.digamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;

/**
 * ディガンマ関数に対するStirling近似の残差を扱う. <br>
 * すなわち, f(x) = psi(x) - log(x) + 1/(2x) の取り扱い. <br>
 * f(x) = sum_{k=1}^{inf} (-B_{2k})/(2k) * x^{-2k}
 * 
 * @author Matsuura Y.
 */
final class DigammaResidual {

    private static final double VALID_X_FOR_ASYMPTOTIC = 10d;

    /**
     * f(x) を計算する.
     * 
     * @param x x
     * @return f(x)
     */
    public static double value(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= VALID_X_FOR_ASYMPTOTIC) {
            return valueAsymptotic(x);
        }

        return ResidualRecursion.difference(x) + value(x + 1);
    }

    /**
     * {@literal x >> 1} における f(x) の計算. <br>
     * {@literal x >= 10} でおそらくうまくいく.
     * 
     * @param x x
     * @return f(x)
     */
    private static double valueAsymptotic(double x) {

        /*
         * 漸近級数では,
         * f(x) = sum_{m=1}^{inf} (-B_{2k})/(2k) * x^{-2k}
         * である.
         */

        double invX = 1 / x;

        double value = 0;
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value *= invX * invX;
            value += -EvenBernoulli.evenBernoulli(k) / (2 * k);
        }

        return value * invX * invX;
    }

    public static void main(String[] args) {

        double xmin = 0.5d;
        double xmax = 15d;
        double delta = 0.25d;
        System.out.println("s\tv");
        for (double x = xmin; x <= xmax; x += delta) {
            System.out.println(x + "\t" + value(x));
        }
    }

    /**
     * f(x) - f(x+1) を扱う.
     * 
     * <p>
     * psi(x+1) = psi(x) + 1/x <br>
     * f(x) = f(x+1) + log(1+1/x)-1/(2(x+1)) - 1/(2x)
     * <p>
     * 
     * <p>
     * t = 1/x として,
     * f(x) - f(x+1) = log(1+t)-(t/2) * (1+t)^{-1} - t/2 <br>
     * = sum_{k=3} (-1)^{k-1}(1/k-1/2) * t^k <br>
     * = sum_{k=0} (-1)^{k-1}(k+1)/(2(k+3)) * t^{k+3}
     * </p>
     * 
     */
    private static final class ResidualRecursion {

        private final static DoubleContinuedFractionFunction fraction;

        static {
            fraction = ContinuedFractionFunction
                    .from(
                            50,
                            k -> BigRational.of(
                                    -(k + 2) * (k + 3), (k + 1) * (k + 4)),
                            BigRational.constantSupplier())
                    .asDoubleFunction();
        }

        static double difference(double x) {
            if (x < 1d) {
                return Exponentiation.log1p(1 / x) - 1d / (2 * (x + 1)) - 1d / (2 * x);
            }

            return fraction.value(1 / x) / (-6 * x * x * x);
        }
    }
}
