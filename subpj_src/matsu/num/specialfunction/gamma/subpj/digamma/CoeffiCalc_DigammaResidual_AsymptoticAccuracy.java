package matsu.num.specialfunction.gamma.subpj.digamma;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.bessel.subpj.ConstantStyle;
import matsu.num.specialfunction.bessel.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_DigammaResidual_AsymptoticAccuracy} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_DigammaResidual_AsymptoticAccuracy {

    public static void main(String[] args) {

        int order = 4;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        MinimaxApproxFunc_DigammaResidual_AsymptoticAccuracy target =
                new MinimaxApproxFunc_DigammaResidual_AsymptoticAccuracy(0.1d);
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
