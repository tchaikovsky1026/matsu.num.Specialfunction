/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.15
 */
package matsu.num.specialfunction.modbessel.subpj.kasymptotic;

import matsu.num.approximation.Approximation;
import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.approximation.PolynomialFunction;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.specialfunction.modbessel.subpj.ConstantStyle;
import matsu.num.specialfunction.modbessel.subpj.ResultDisplayFormat;

/**
 * {@link BesselK0Asymptotic} のminimax近似.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
public final class CoeffiCalc_K1Asymptotic {

    public static void main(String[] args) {

        int order = 11;
        MinimaxPolynomialApproxExecutor approxExecutor =
                MinimaxPolynomialApproxExecutor.of(order);
        EachIntervalExecutor executor = new EachIntervalExecutor(approxExecutor);

        executor.execute(DoubleFiniteClosedInterval.from(1d / 64, 1d / 32));
        executor.execute(DoubleFiniteClosedInterval.from(1d / 32, 1d / 16));

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

            BesselK1Asymptotic target = new BesselK1Asymptotic(interval);
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