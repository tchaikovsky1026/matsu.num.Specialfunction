package matsu.num.specialfunction.gamma.subpj.lgamma;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.subpj.ConstantStyle;
import matsu.num.specialfunction.subpj.ResultDisplayFormat;

/**
 * {@link MinimaxApproxFunc_LGammaResidual_Asymptotic} のminimax近似.
 * 
 * @author Matsuura Y.
 */
public final class CoeffiCalc_LGammaResidual_Asymptotic {

    public static void main(String[] args) {

        int order = 11;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        DoubleFiniteClosedInterval interval = DoubleFiniteClosedInterval.from(1d / 10, 1d / 2.5);
        MinimaxApproxFunc_LGammaResidual_Asymptotic target =
                new MinimaxApproxFunc_LGammaResidual_Asymptotic(interval);
        ApproxResult<DoublePolynomial> approx = approxExecutor.apply(target);
        DoublePolynomial resultPolynomial = approx
                .orElseThrow(() -> new RuntimeException(approx.message()));

        System.out.println(interval);
        System.out.println();

        System.out.println(
                new ResultDisplayFormat(new ConstantStyle("C"))
                        .resultToString(target, resultPolynomial));

        System.out.println();

        System.out.println("finished...");
    }
}
