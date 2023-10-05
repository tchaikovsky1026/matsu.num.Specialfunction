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
 * {@link DigammaCalculation}クラスに関するテスト.
 *  
 * @author Matsuura Y.
 */

@RunWith(Enclosed.class)
public class DigammaCalculationTest {

    @RunWith(Enclosed.class)
    public static class メソッドdigammaに関する {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.5, -1.963510026021423479441 },
                    { 1, -0.5772156649015328606065 },
                    { 2, 0.4227843350984671393935 },
                    { 3, 0.9227843350984671393935 },
                    { 4, 1.256117668431800472727 },
                    { 5, 1.506117668431800472727 },
                    { 6, 1.706117668431800472727 },
                    { 7, 1.872784335098467139394 },
                    { 8, 2.015641477955609996536 },
                    { 9, 2.140641477955609996536 },
                    { 10, 2.251752589066721107647 },
                    { 11, 2.351752589066721107647 }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(DigammaCalculation.instance().digamma(dataPair[0]),
                        is(closeTo(dataPair[1], (Math.abs(dataPair[1]) + 1) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            @Test
            public void test_0は負の無限大() {
                assertThat(DigammaCalculation.instance().digamma(0), is(Double.NEGATIVE_INFINITY));

            }

            @Test
            public void test_負はNaN() {
                assertThat(DigammaCalculation.instance().digamma(-1), is(Double.NaN));

            }

            @Test
            public void test_正の無限大では正の無限大() {
                assertThat(DigammaCalculation.instance().digamma(Double.POSITIVE_INFINITY),
                        is(Double.POSITIVE_INFINITY));
            }
        }
    }

}
