/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.14
 */
package matsu.num.specialfunction.modbessel.subpj.kpower;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.modbessel.subpj.ConstantStyle;
import matsu.num.specialfunction.modbessel.subpj.ResultDisplayFormat;

/**
 * {@link BesselK0Power_logTerm} のminimax近似.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
public final class CoeffiCalc_K1Power_logTerm {

    public static void main(String[] args) {

        int order = 8;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        BesselK1Power_logTerm target =
                new BesselK1Power_logTerm(DoubleFiniteClosedInterval.from(0, 1));
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
