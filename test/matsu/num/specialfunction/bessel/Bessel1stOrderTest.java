package matsu.num.specialfunction.bessel;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.BesselFunction;

/**
 * {@link Bessel1stOrder}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Bessel1stOrderTest {

    public static final Class<?> TEST_CLASS = Bessel1stOrder.class;

    @RunWith(Enclosed.class)
    public static class 第1種ベッセルに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0, 0 },
                    { 0.5, 0.242268457674873886384 },
                    { 1.5, 0.5579365079100996419901 },
                    { 3, 0.3390589585259364589255 },
                    { 6, -0.2766838581275656081728 },
                    { 10, 0.04347274616886143666975 }
            };

            private BesselFunction bessel1;

            @Before
            public void before_1次のベッセルを作成() {
                bessel1 = Bessel1stOrder.instance();
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(bessel1.besselJ(dataPair[0]), is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            private BesselFunction bessel1;

            @Before
            public void before_1次のベッセルを作成() {
                bessel1 = Bessel1stOrder.instance();
            }

            @Test
            public void test_0未満はNaN() {
                assertThat(bessel1.besselJ(-Double.MIN_VALUE), is(Double.NaN));
            }

            @Test
            public void test_正の無限大は0() {
                assertThat(bessel1.besselJ(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class 第2種ベッセルに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.01, -63.6785962820606563743 },
                    { 0.5, -1.471472392670243069189 },
                    { 1.5, -0.4123086269739112959528 },
                    { 3, 0.324674424791799978437 },
                    { 6, -0.1750103443003982506368 },
                    { 10, 0.2490154242069538839233 }
            };

            private BesselFunction bessel1;

            @Before
            public void before_1次のベッセルを作成() {
                bessel1 = Bessel1stOrder.instance();
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(bessel1.besselY(dataPair[0]), is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            private BesselFunction bessel1;

            @Before
            public void before_1次のベッセルを作成() {
                bessel1 = Bessel1stOrder.instance();
            }

            @Test
            public void test_0未満はNaN() {
                assertThat(bessel1.besselY(-Double.MIN_VALUE), is(Double.NaN));
            }

            @Test
            public void test_0は負の無限大() {
                assertThat(bessel1.besselY(0), is(Double.NEGATIVE_INFINITY));
            }

            @Test
            public void test_正の無限大は0() {
                assertThat(bessel1.besselY(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(Bessel1stOrder.instance());
            System.out.println();
        }
    }

}
