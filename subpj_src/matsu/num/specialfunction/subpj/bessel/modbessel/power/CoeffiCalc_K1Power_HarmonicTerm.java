package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link BesselK1Power_HarmonicTerm} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_K1Power_HarmonicTerm {

    public static void main(String[] args) {

        int order = 8;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        BesselK1Power_HarmonicTerm target =
                new BesselK1Power_HarmonicTerm(DoubleFiniteClosedInterval.from(0, 1));
        ApproxResult<DoublePolynomial> approx = approxExecutor.apply(target);
        DoublePolynomial resultPolynomial = approx
                .orElseThrow(() -> new RuntimeException(approx.message()));

        System.out.println(
                new ResultDisplayFormat(new ConstantStyle("C"))
                        .resultToString(target, resultPolynomial));

        System.out.println();

        System.out.println("finished...");
    }
}
