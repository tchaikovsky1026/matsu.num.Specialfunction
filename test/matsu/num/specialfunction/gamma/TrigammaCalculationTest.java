package matsu.num.specialfunction.gamma;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link TrigammaCalculation}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class TrigammaCalculationTest {

    @RunWith(Enclosed.class)
    public static class メソッドdigammaに関する {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.5, 4.934802200544679309417 },
                    { 1, 1.644934066848226436472 },
                    { 2, 0.6449340668482264364724 },
                    { 3, 0.3949340668482264364724 },
                    { 4, 0.2838229557371153253613 },
                    { 5, 0.2213229557371153253613 },
                    { 6, 0.1813229557371153253613 },
                    { 7, 0.1535451779593375475835 },
                    { 8, 0.1331370146940314251346 },
                    { 9, 0.1175120146940314251346 },
                    { 10, 0.1051663356816857461222 },
                    { 11, 0.0951663356816857461222 }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(TrigammaCalculation.instance().trigamma(dataPair[0]),
                        is(closeTo(dataPair[1], (Math.abs(dataPair[1]) + 1) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            @Test
            public void test_0は正の無限大() {
                assertThat(TrigammaCalculation.instance().trigamma(0), is(Double.POSITIVE_INFINITY));

            }

            @Test
            public void test_負はNaN() {
                assertThat(TrigammaCalculation.instance().trigamma(-1), is(Double.NaN));

            }

            @Test
            public void test_正の無限大では0() {
                assertThat(TrigammaCalculation.instance().trigamma(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

}
