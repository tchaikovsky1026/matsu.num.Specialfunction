package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link BesselI0Power_ExpTerm} のminimax近似.
 * 
 * @author Matsuura Y.
 */
final class CoeffiCalc_I0Power_ExpTerm {

    public static void main(String[] args) {

        int order = 19;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(0, 4), 2);
        executor.execute(DoubleFiniteClosedInterval.from(0, 4), 6);
        executor.execute(DoubleFiniteClosedInterval.from(0, 4), 10);
        executor.execute(DoubleFiniteClosedInterval.from(0, 4), 14);
        executor.execute(DoubleFiniteClosedInterval.from(0, 6), 18);

        System.out.println("finished...");
    }

    private static final class EachIntervalExecutor {

        private final MinimaxPolynomialApproxExecutor executor;

        EachIntervalExecutor(MinimaxPolynomialApproxExecutor executor) {
            super();
            this.executor = executor;
        }

        void execute(DoubleFiniteClosedInterval interval, double minX) {

            System.out.println("shift=" + minX);
            System.out.println(interval);
            System.out.println();

            BesselI0Power_ExpTerm target = new BesselI0Power_ExpTerm(interval, minX);
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
