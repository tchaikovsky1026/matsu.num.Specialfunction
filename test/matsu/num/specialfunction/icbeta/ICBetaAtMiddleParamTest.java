/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icbeta;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * {@link ICBetaAtMiddleParam} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ICBetaAtMiddleParamTest {

    public static final Class<?> TEST_CLASS = ICBetaAtMiddleParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-8);

    @RunWith(Theories.class)
    public static class A_20_B_50000のオッズ値のテスト {

        private static final IncompleteBetaFunction IC_BETA =
                new ICBetaAtMiddleParam(20, 50000);

        @DataPoints
        public static double[][] dataPairs = {
                { 0.0001000100010001, 0.00000034648363973200 },
                { 0.0001500225033755, 0.0001111445097039990 },
                { 0.0002000400080016, 0.003477229450705430 },
                { 0.0003000900270081, 0.1429430371414360 },
                { 0.0005002501250625, 6.5121071090364200 },
                { 0.0007004903432403, 433.1613101923770000 },
                { 0.0010010010010010, 2133072.86668407 },
                { 3.1049275156958900E-04, 1.8495185915737100E-01 },
                { 3.2838181231393100E-04, 2.7908752728761000E-01 },
                { 3.4627151290804000E-04, 4.0778622997946000E-01 },
                { 3.6416185338624400E-04, 5.8139730539514400E-01 },
                { 3.8205283378287600E-04, 8.1403590215553500E-01 },
                { 3.9994445413227100E-04, 1.1253004035658200E+00 },
                { 4.1783671446876400E-04, 1.5427589409898100E+00 },
                { 4.3572961482669500E-04, 2.1056022822572200E+00 },
                { 4.5362315524040700E-04, 2.8700781018954400E+00 },
                { 4.7151733574424200E-04, 3.9176613547972600E+00 },
                { 4.8941215637254900E-04, 5.3674528891225900E+00 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_BETA.ribetaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 境界値テスト {

        @DataPoints
        public static final IncompleteBetaFunction[] icbetas = {
                new ICBetaAtMiddleParam(20, 50000)
        };

        @Theory
        public void test_0検証_beta(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(0d, icbeta.ribeta(0d));
        }

        @Theory
        public void test_1検証_beta(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, icbeta.ribeta(1d));
        }

        @Theory
        public void test_0検証_betaR(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(0d, icbeta.ribetaR(0d));
        }

        @Theory
        public void test_1検証_betaR(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, icbeta.ribetaR(1d));
        }
    }
}
