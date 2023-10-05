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
 * {@link Bessel0thOrder}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class Bessel0thOrderTest {

    @RunWith(Enclosed.class)
    public static class 第1種ベッセルに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0, 1 },
                    { 0.5, 0.9384698072408129042284 },
                    { 1.5, 0.5118276717359181287491 },
                    { 3, -0.2600519549019334376242 },
                    { 6, 0.1506452572509969316623 },
                    { 10, -0.2459357644513483351978 }
            };

            private BesselFunction bessel0;

            @Before
            public void before_0次のベッセルを作成() {
                bessel0 = Bessel0thOrder.instance();
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(bessel0.besselJ(dataPair[0]), is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            private BesselFunction bessel0;

            @Before
            public void before_0次のベッセルを作成() {
                bessel0 = Bessel0thOrder.instance();
            }

            @Test
            public void test_0未満はNaN() {
                assertThat(bessel0.besselJ(-Double.MIN_VALUE), is(Double.NaN));
            }

            @Test
            public void test_正の無限大は0() {
                assertThat(bessel0.besselJ(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

    @RunWith(Enclosed.class)
    public static class 第2種ベッセルに関するテスト {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.01, -3.005455637083645957779 },
                    { 0.5, -0.4445187335067065571484 },
                    { 1.5, 0.3824489237977588439551 },
                    { 3, 0.3768500100127903819671 },
                    { 6, -0.2881946839815791540691 },
                    { 10, 0.05567116728359939142446 }
            };

            private BesselFunction bessel0;

            @Before
            public void before_0次のベッセルを作成() {
                bessel0 = Bessel0thOrder.instance();
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(bessel0.besselY(dataPair[0]), is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
            }
        }

        public static class 特殊値のテスト {

            private BesselFunction bessel0;

            @Before
            public void before_0次のベッセルを作成() {
                bessel0 = Bessel0thOrder.instance();
            }

            @Test
            public void test_0未満はNaN() {
                assertThat(bessel0.besselY(-Double.MIN_VALUE), is(Double.NaN));
            }

            @Test
            public void test_0は負の無限大() {
                assertThat(bessel0.besselY(0), is(Double.NEGATIVE_INFINITY));
            }

            @Test
            public void test_正の無限大は0() {
                assertThat(bessel0.besselY(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }

}
