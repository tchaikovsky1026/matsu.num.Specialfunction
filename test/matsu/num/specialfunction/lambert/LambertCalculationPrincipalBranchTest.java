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
 * {@link LambertCalculationPrincipalBranch}クラス(主枝の計算)のテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class LambertCalculationPrincipalBranchTest {

    @RunWith(Theories.class)
    public static class 正常値セオリー {

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
                { Double.MAX_VALUE, 703.22703310477018687 }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            assertThat(LambertFunction.wp(dataPair[0]),
                    is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-15)));
        }
    }

    public static class 特殊値のテスト {

        @Test
        public void test_m1ByE未満はNaN() {
            assertThat(LambertFunction.wp(Math.nextDown(-1 / Math.E)), is(Double.NaN));

        }

        @Test
        public void test_正の無限大は正の無限大() {
            assertThat(LambertFunction.wp(Double.POSITIVE_INFINITY), is(Double.POSITIVE_INFINITY));
        }

    }

}
