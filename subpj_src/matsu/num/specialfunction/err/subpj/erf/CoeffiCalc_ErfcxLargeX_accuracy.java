package matsu.num.specialfunction.err.subpj.erf;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_ErfcxLargeX_accuracy} のminimax近似.
 * 
 * @author Matsuura Y.
 */
final class CoeffiCalc_ErfcxLargeX_accuracy {

    public static void main(String[] args) {

        int order = 6;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(1d / 8);

        System.out.println("finished...");
    }

    private static final class EachIntervalExecutor {

        private final MinimaxPolynomialApproxExecutor executor;

        EachIntervalExecutor(MinimaxPolynomialApproxExecutor executor) {
            super();
            this.executor = executor;
        }

        void execute(double tmax) {

            System.out.println("tmax = " + tmax);
            System.out.println();

            MinimaxApproxFunc_ErfcxLargeX_accuracy target =
                    new MinimaxApproxFunc_ErfcxLargeX_accuracy(tmax);
            Approximation<PolynomialFunction> approx = executor.apply(target);
            PolynomialFunction resultPolynomial = approx
                    .orElseThrow(() -> new RuntimeException(approx.message()));

            System.out.println(
                    new ResultDisplayFormat(new ConstantStyle("C"))
                            .resultToString(target, resultPolynomial));

            System.out.println();
            System.out.println();
        }
    }
}
