package matsu.num.specialfunction.subpj.bessel.bessel.power;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link BesselJ1Power} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_J1Power {

    public static void main(String[] args) {

        int order = 7;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        BesselJ1Power target =
                new BesselJ1Power(DoubleFiniteClosedInterval.from(0, 1));
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
