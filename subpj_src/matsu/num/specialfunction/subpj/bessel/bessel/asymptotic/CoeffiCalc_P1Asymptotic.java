package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link BesselP1Asymptotic} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_P1Asymptotic {

    public static void main(String[] args) {

        int order = 11;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(0, 1d / 128));
        executor.execute(DoubleFiniteClosedInterval.from(1d / 128, 2d / 128));
        executor.execute(DoubleFiniteClosedInterval.from(2d / 128, 4d / 128));
        executor.execute(DoubleFiniteClosedInterval.from(4d / 128, 6d / 128));
        executor.execute(DoubleFiniteClosedInterval.from(6d / 128, 8d / 128));

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

            BesselP1Asymptotic target = new BesselP1Asymptotic(interval);
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
