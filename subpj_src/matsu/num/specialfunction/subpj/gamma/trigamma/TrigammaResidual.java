package matsu.num.specialfunction.subpj.gamma.trigamma;

import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;

/**
 * トリガンマ関数に対するStirling近似の残差を扱う. <br>
 * すなわち, f(x) = psi1(x) - 1/x - 1/(2x^2) の取り扱い. <br>
 * f(x) = sum_{k=1}^{inf} B_{2k} * x^{-2k-1}
 * 
 * @author Matsuura Y.
 */
final class TrigammaResidual {

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
         * f(x) = sum_{k=1}^{inf} B_{2k} * x^{-2k-1}
         * である.
         */

        double invX = 1 / x;

        double value = 0;
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value *= invX * invX;
            value += EvenBernoulli.evenBernoulli(k);
        }

        return value * invX * invX * invX;
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
     * f(x) + 1/x + 1/(2x^2) = psi1(x)
     * 
     * <p>
     * psi1(x+1) = psi1(x) - 1/(x^2) <br>
     * f(x) = f(x+1) + 1/(2x^2(x+1)^2)
     * <p>
     * 
     */
    private static final class ResidualRecursion {

        static double difference(double x) {
            return 0.5 / (x * x * (x + 1) * (x + 1));
        }
    }
}
