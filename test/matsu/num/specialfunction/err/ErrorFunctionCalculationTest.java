package matsu.num.specialfunction.err;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.ErrorFuction;

/**
 * {@link ErrorFunctionCalculation}クラスのテスト.
 *  
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ErrorFunctionCalculationTest {

    @RunWith(Enclosed.class)
    public static class erfに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { -3, -0.9999779095030010 },
                    { -1.5, -0.9661051464753110 },
                    { -0.75, -0.7111556336535150 },
                    { -0.25, -0.2763263901682370 },
                    { 0, 0.0000000000000000 },
                    { 0.25, 0.2763263901682370 },
                    { 0.75, 0.7111556336535150 },
                    { 1.5, 0.9661051464753110 },
                    { 3, 0.9999779095030010 }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ErrorFuction.erf(dataPair[0]), is(closeTo(dataPair[1], 1E-10)));
            }
        }

        public static class 特殊値のテスト {

            @Test
            public void test_負の無限大はm1() {
                assertThat(ErrorFuction.erf(Double.NEGATIVE_INFINITY), is(-1.0));

            }

            @Test
            public void test_正の無限大は1() {
                assertThat(ErrorFuction.erf(Double.POSITIVE_INFINITY), is(1.0));
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class erfcに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { -3, 1.9999779095030000 },
                    { -1.5, 1.9661051464753100 },
                    { -0.75, 1.7111556336535200 },
                    { -0.25, 1.2763263901682400 },
                    { 0, 1.0000000000000000 },
                    { 0.25, 0.7236736098317630 },
                    { 0.75, 0.2888443663464850 },
                    { 1.5, 0.0338948535246893 },
                    { 3, 0.0000220904969986 }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ErrorFuction.erfc(dataPair[0]), is(closeTo(dataPair[1], 1E-10)));
            }
        }

        public static class 特殊値のテスト {

            @Test
            public void test_負の無限大は2() {
                assertThat(ErrorFuction.erfc(Double.NEGATIVE_INFINITY), is(2.0));

            }

            @Test
            public void test_正の無限大は0() {
                assertThat(ErrorFuction.erfc(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class erfcxに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { -3, 16205.9888539996000000 },
                    { -1.5, 18.6538862562627000 },
                    { -0.75, 3.0031716636274500 },
                    { -0.25, 1.3586423701047200 },
                    { 0, 1.0000000000000000 },
                    { 0.25, 0.7703465477309970 },
                    { 0.75, 0.5069376502931450 },
                    { 1.5, 0.3215854164543170 },
                    { 3, 0.1790011511813900 }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ErrorFuction.erfcx(dataPair[0]), is(closeTo(dataPair[1], 1E-10)));
            }
        }

        public static class 特殊値のテスト {

            @Test
            public void test_負の無限大は正の無限大() {
                assertThat(ErrorFuction.erfcx(Double.NEGATIVE_INFINITY), is(Double.POSITIVE_INFINITY));

            }

            @Test
            public void test_正の無限大は0() {
                assertThat(ErrorFuction.erfcx(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

}
