package matsu.num.specialfunction.subpj.err.erf;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

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
            ApproxResult<DoublePolynomial> approx = executor.apply(target);
            DoublePolynomial resultPolynomial = approx
                    .orElseThrow(() -> new RuntimeException(approx.message()));

            System.out.println(
                    new ResultDisplayFormat(new ConstantStyle("C"))
                            .resultToString(target, resultPolynomial));

            System.out.println();
            System.out.println();
        }
    }
}
