package matsu.num.specialfunction.bessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.BesselFunction;
import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link BesselHigherOrder}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class BesselHigherOrderTest {

    public static final Class<?> TEST_CLASS = BesselHigherOrder.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    @RunWith(Enclosed.class)
    public static class ベッセルの5次に関する {

        private static final int N = 5;

        private static final BesselFunction BESSEL_N = new BesselHigherOrder(N);

        @RunWith(Theories.class)
        public static class 第1種ベッセルに関するテスト {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0, 0 },
                    { 0.5, 8.053627241357474085978E-6 },
                    { 1.5, 0.001799421767360611158833 },
                    { 3, 0.04302843487704758392491 },
                    { 6, 0.3620870748871723890797 },
                    { 10, -0.2340615281867936404437 },
                    { Math.nextDown(0d), Double.NaN },
                    { Double.POSITIVE_INFINITY, 0d }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        dataPair[1],
                        BESSEL_N.besselJ(dataPair[0]));
            }
        }

        @RunWith(Theories.class)
        public static class 第2種ベッセルに関するテスト {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.01, -2444635204829.711421859 },
                    { 0.5, -7946.30147880747334183 },
                    { 1.5, -37.19030839549808345998 },
                    { 3, -1.905945953828673732218 },
                    { 6, -0.1970608880644373281472 },
                    { 10, 0.135403047689362303197 },
                    { Math.nextDown(0d), Double.NaN },
                    { 0d, Double.NEGATIVE_INFINITY },
                    { Double.POSITIVE_INFINITY, 0d }
            };

            @Theory
            public void test_検証(double[] dataPair) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        dataPair[1],
                        BESSEL_N.besselY(dataPair[0]));
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
