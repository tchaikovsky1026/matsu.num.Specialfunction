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
 * {@link LambertWmImplByHalley} クラス(-1分枝の計算)のテスト.
 */
@RunWith(Enclosed.class)
final class LambertWmImplByHalleyTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final LambertWmCalculation WM = new LambertWmImplByHalley();

    @RunWith(Theories.class)
    public static class Wm値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { -1 / Math.E, -1 },
                { -0.3, -1.781337023421627611974 },
                { -0.2, -2.542641357773526424294 },
                { -0.1, -3.577152063957297218409 },
                { -0.05, -4.499755288523487535975 },
                { -0.02, -5.642317974976494741208 },
                { -0.001, -9.118006470402740121258 },
                { -1E-10, -26.29523881924692569411 },
                { -1E-40, -96.67475603368003636615 },
                { -Double.MIN_NORMAL, -714.96865723796647087 },
                { -Double.MIN_VALUE, -751.0615595398791 },
                { Math.nextDown(-1 / Math.E), Double.NaN },
                { 0d, Double.NEGATIVE_INFINITY },
                { -0d, Double.NEGATIVE_INFINITY },
                { Double.MIN_VALUE, Double.NaN },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    WM.wm(dataPair[0]));
        }

        @Test
        public void test_特殊値() {
            DOUBLE_RELATIVE_ASSERTION.assertFinite(WM.wm(Math.nextDown(-1 / Math.E + 1E-11)));
            DOUBLE_RELATIVE_ASSERTION.assertFinite(WM.wm(Math.nextUp(-1 / Math.E + 1E-11)));
        }
    }
}
