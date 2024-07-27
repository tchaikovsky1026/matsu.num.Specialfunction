package matsu.num.specialfunction.bessel.subpj.bessel.power;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link BesselY0Power_HarmonicTerm} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_Y0Power_HarmonicTerm {

    public static void main(String[] args) {

        int order = 8;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        BesselY0Power_HarmonicTerm target =
                new BesselY0Power_HarmonicTerm(DoubleFiniteClosedInterval.from(0, 1));
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
