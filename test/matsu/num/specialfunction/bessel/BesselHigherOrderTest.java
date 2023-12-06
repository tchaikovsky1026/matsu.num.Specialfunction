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
 * {@link BesselHigherOrder}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class BesselHigherOrderTest {

    public static final Class<?> TEST_CLASS = BesselHigherOrder.class;

    @RunWith(Enclosed.class)
    public static class ベッセルの5次に関する {

        @RunWith(Enclosed.class)
        public static class 第1種ベッセルに関するテスト {

            @RunWith(Theories.class)
            public static class 正常値セオリー {

                @DataPoints
                public static double[][] dataPairs = {
                        { 0, 0 },
                        { 0.5, 8.053627241357474085978E-6 },
                        { 1.5, 0.001799421767360611158833 },
                        { 3, 0.04302843487704758392491 },
                        { 6, 0.3620870748871723890797 },
                        { 10, -0.2340615281867936404437 }
                };

                private BesselFunction besselN;

                @Before
                public void before_5次のベッセルを作成() {
                    besselN = new BesselHigherOrder(5);
                }

                @Theory
                public void test_検証(double[] dataPair) {
                    assertThat(
                            besselN.besselJ(dataPair[0]),
                            is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
                }
            }

            public static class 特殊値のテスト {

                private BesselFunction besselN;

                @Before
                public void before_5次のベッセルを作成() {
                    besselN = new BesselHigherOrder(5);
                }

                @Test
                public void test_0未満はNaN() {
                    assertThat(besselN.besselJ(-Double.MIN_VALUE), is(Double.NaN));
                }

                @Test
                public void test_正の無限大は0() {
                    assertThat(besselN.besselJ(Double.POSITIVE_INFINITY), is(0.0));
                }
            }
        }

        @RunWith(Enclosed.class)
        public static class 第2種ベッセルに関するテスト {

            @RunWith(Theories.class)
            public static class 正常値セオリー {

                @DataPoints
                public static double[][] dataPairs = {
                        { 0.01, -2444635204829.711421859 },
                        { 0.5, -7946.30147880747334183 },
                        { 1.5, -37.19030839549808345998 },
                        { 3, -1.905945953828673732218 },
                        { 6, -0.1970608880644373281472 },
                        { 10, 0.135403047689362303197 }
                };

                private BesselFunction besselN;

                @Before
                public void before_5次のベッセルを作成() {
                    besselN = new BesselHigherOrder(5);
                }

                @Theory
                public void test_検証(double[] dataPair) {
                    assertThat(
                            besselN.besselY(dataPair[0]),
                            is(closeTo(dataPair[1], (1 + Math.abs(dataPair[1])) * 1E-12)));
                }
            }

            public static class 特殊値のテスト {

                private BesselFunction besselN;

                @Before
                public void before_5次のベッセルを作成() {
                    besselN = new BesselHigherOrder(5);
                }

                @Test
                public void test_0未満はNaN() {
                    assertThat(besselN.besselY(-Double.MIN_VALUE), is(Double.NaN));
                }

                @Test
                public void test_0は負の無限大() {
                    assertThat(besselN.besselY(0), is(Double.NEGATIVE_INFINITY));
                }

                @Test
                public void test_正の無限大は0() {
                    assertThat(besselN.besselY(Double.POSITIVE_INFINITY), is(0.0));
                }
            }
        }
    }

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new BesselHigherOrder(5));
            System.out.println();
        }
    }

}
