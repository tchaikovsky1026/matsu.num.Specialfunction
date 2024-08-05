package matsu.num.specialfunction.gamma.subpj.digamma;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_DigammaResidual_Asymptotic} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_DigammaResidual_Asymptotic {

    public static void main(String[] args) {

        int order = 12;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(1d / 10, 1d / 2.5));

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

            MinimaxApproxFunc_DigammaResidual_Asymptotic target =
                    new MinimaxApproxFunc_DigammaResidual_Asymptotic(interval);
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
