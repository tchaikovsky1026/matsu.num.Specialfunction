package matsu.num.specialfunction.err.subpj.erf;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_ErfcxLargeX_accuracy} のminimax近似.
 * 
 * @author Matsuura Y.
 */
final class CoeffiCalc_ErfcxLargeX {

    public static void main(String[] args) {

        int order = 11;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(1d / 8, 2d / 8));
        executor.execute(DoubleFiniteClosedInterval.from(2d / 8, 3d / 8));
        executor.execute(DoubleFiniteClosedInterval.from(3d / 8, 4d / 8));
        executor.execute(DoubleFiniteClosedInterval.from(4d / 8, 6d / 8));
        executor.execute(DoubleFiniteClosedInterval.from(6d / 8, 8d / 8));

        System.out.println("finished...");
    }

    private static final class EachIntervalExecutor {

        private final MinimaxPolynomialApproxExecutor executor;

        EachIntervalExecutor(MinimaxPolynomialApproxExecutor executor) {
            super();
            this.executor = executor;
        }

        void execute(DoubleFiniteClosedInterval interval) {

            System.out.println(interval);
            System.out.println();

            MinimaxApproxFunc_ErfcxLargeX target =
                    new MinimaxApproxFunc_ErfcxLargeX(interval);
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
