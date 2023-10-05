package matsu.num.specialfunction.lambert;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.LambertFunction;

/**
 * {@link LambertCalculationMinus1Branch}クラス(-1分枝の計算)のテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class LambertCalculationMinus1BranchTest {

    @RunWith(Theories.class)
    public static class 正常値セオリー {

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
                { -Double.MIN_NORMAL, -714.96865723796647087 }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            assertThat(LambertFunction.wm(dataPair[0]),
                    is(closeTo(dataPair[1], (Math.abs(dataPair[1]) + 1) * 1E-15)));
        }
    }

    public static class 特殊値のテスト {

        @Test
        public void test_m1ByE未満はNaN() {
            assertThat(LambertFunction.wm(Math.nextDown(-1 / Math.E)), is(Double.NaN));

        }

        @Test
        public void test_0は負の無限大() {
            assertThat(LambertFunction.wm(0), is(Double.NEGATIVE_INFINITY));
        }

        @Test
        public void test_0超過はNaN() {
            assertThat(LambertFunction.wm(Double.MIN_VALUE), is(Double.NaN));
        }
    }

}
