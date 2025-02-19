/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.lambert;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link LambertWpImplByHalley} クラス(主枝の計算)のテスト.
 */
@RunWith(Enclosed.class)
final class LambertWpImplByHalleyTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final LambertWpCalculation WP = new LambertWpImplByHalley();

    @RunWith(Theories.class)
    public static class Wp値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { -1 / Math.E, -1 },
                { -0.1, -0.1118325591589629648336 },
                { 0.5, 0.3517337112491958260249 },
                { 0, 0 },
                { 1, 0.567143290409783873 },
                { 2, 0.8526055020137254913465 },
                { 3, 1.049908894964039959989 },
                { 6, 1.432404775898300311234 },
                { 9, 1.6790164197855981954459 },
                { 10, 1.745528002740699383074 },
                { 20, 2.205003278024059970493 },
                { Double.MAX_VALUE, 703.22703310477018687 },
                { Math.nextDown(-1 / Math.E), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    WP.wp(dataPair[0]));
        }

        @Test
        public void test_特殊値() {
            DOUBLE_RELATIVE_ASSERTION.assertFinite(WP.wp(Math.nextDown(-1 / Math.E + 1E-11)));
            DOUBLE_RELATIVE_ASSERTION.assertFinite(WP.wp(Math.nextUp(-1 / Math.E + 1E-11)));
        }
    }
}
