package matsu.num.specialfunction.err.subpj.erfi;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link ErfixFuncLargeX_t} のminimax近似.
 * 
 * @author Matsuura Y.
 */
final class CoeffiCalc_ErfixFuncLargeX {

    public static void main(String[] args) {

        EachIntervalExecutor executor = new EachIntervalExecutor();

        executor.execute(1d, 2d, 17);
        executor.execute(2d, 3d, 16);
        executor.execute(3d, 4d, 14);
        executor.execute(4d, 5d, 13);
        executor.execute(5d, 6d, 12);
        executor.execute(6d, 7d, 10);
        executor.execute(7d, 8d, 10);

        System.out.println("finished...");
    }

    private static final class EachIntervalExecutor {

        EachIntervalExecutor() {
            super();
        }

        void execute(double xmin, double xmax, int order) {

            MinimaxPolynomialApproxExecutor executor =
                    MinimaxPolynomialApproxExecutor.of(order);

            ErfixFuncLargeX target =
                    new ErfixFuncLargeX(xmin, xmax);

            System.out.println(target);
            System.out.println();

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
