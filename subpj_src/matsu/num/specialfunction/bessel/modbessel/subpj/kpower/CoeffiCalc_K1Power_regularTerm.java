package matsu.num.specialfunction.bessel.modbessel.subpj.kpower;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.modbessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.modbessel.subpj.ResultDisplayFormat;

/**
 * {@link BesselK0Power_logTerm} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_K1Power_regularTerm {

    public static void main(String[] args) {

        int order = 8;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        BesselK1Power_regularTerm target =
                new BesselK1Power_regularTerm(DoubleFiniteClosedInterval.from(0, 1));
        Approximation<PolynomialFunction> approx = approxExecutor.apply(target);
        PolynomialFunction resultPolynomial = approx
                .orElseThrow(() -> new RuntimeException(approx.message()));

        System.out.println(
                new ResultDisplayFormat(new ConstantStyle("C"))
                        .resultToString(target, resultPolynomial));

        System.out.println();

        System.out.println("finished...");
    }
}
