package matsu.num.specialfunction.lambert;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link LambertCalculationMinus1Branch}クラス(-1分枝の計算)のテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class LambertCalculationMinus1BranchTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final LambertCalculationMinus1Branch LAMBERT_M1 =
            new LambertCalculationMinus1Branch();

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
                { Double.MIN_VALUE, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    LAMBERT_M1.wm(dataPair[0]));
        }
    }
}
