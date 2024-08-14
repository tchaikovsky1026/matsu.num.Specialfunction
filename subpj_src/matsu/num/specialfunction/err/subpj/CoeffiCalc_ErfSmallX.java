package matsu.num.specialfunction.err.subpj;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_ErfSmallX} のminimax近似.
 * 
 * @author Matsuura Y.
 */
final class CoeffiCalc_ErfSmallX {

    public static void main(String[] args) {

        int order = 10;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(0, 1d));

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

            MinimaxApproxFunc_ErfSmallX target = new MinimaxApproxFunc_ErfSmallX(interval);
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
