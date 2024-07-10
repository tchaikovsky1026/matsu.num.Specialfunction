package matsu.num.specialfunction.bessel;

import java.util.function.IntFunction;

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

    private static final IntFunction<BesselFunction> BESSEL_SUPPLIER =
            n -> new BesselHigherOrder(n, new Bessel0thOrder(), new Bessel1stOrder());

    @RunWith(Enclosed.class)
    public static class ベッセルの2次に関する {

        private static final int N = 2;

        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

        @RunWith(Theories.class)
        public static class 第1種ベッセルに関するテスト {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0d, 0 },
                    { 0.5, 0.03060402345868264130741 },
                    { 1d, 0.1149034849319004804696 },
                    { 1.5, 0.2320876721442147272378 },
                    { 1.99, 0.3505895637401508060398 },
                    { 2d, 0.3528340286156377191506 },
                    { 3d, 0.4860912605858910769078 },
                    { 6d, -0.2428732099601854677199 },
                    { 10d, 0.2546303136851206225317 },
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
                    { 0.5, -5.441370837174265719606 },
                    { 1d, -1.650682606816254391077 },
                    { 1.5, -0.9321937597629739052255 },
                    { 1.99, -0.6225247668470591638195 },
                    { 2d, -0.617408104190682666485 },
                    { 3d, -0.1604003934849237296758 },
                    { 6d, 0.2298579025481130705235 },
                    { 10d, -0.005868082442208614639803 },
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

    @RunWith(Enclosed.class)
    public static class ベッセルの5次に関する {

        private static final int N = 5;

        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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

    @RunWith(Enclosed.class)
    public static class ベッセルの100次に関する {

        private static final int N = 100;

        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

        @RunWith(Theories.class)
        public static class 第1種ベッセルに関するテスト {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0d, 0d },
                    { 0.1, 8.45251653512174213274E-289 },
                    { 0.5, 6.663899904277085153292E-219 },
                    { 1d, 8.431828789626708549235E-189 },
                    { 1.5, 3.41746361283273676404E-171 },
                    { 3d, 4.26036018113262524743E-141 },
                    { 6d, 5.051325854150702369585E-111 },
                    { 10d, 6.597316064155380972194E-89 },
                    { 100d, 0.09636667329586155967431 },
                    { 150d, -0.01535952611840539062934 },
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
                    { 0.01, Double.NEGATIVE_INFINITY },
                    { 0.1, -3.765861256019248185439E+285 },
                    { 0.5, -4.776690378041764373368E+215 },
                    { 1d, -3.7752878101105284001E+185 },
                    { 1.5, -9.315262479430634445775E+167 },
                    { 3d, -7.474796102355713093162E+137 },
                    { 6d, -6.312886218584373828866E+107 },
                    { 10d, -4.849148271180607128796E+85 },
                    { 100d, -0.16692141141757650654 },
                    { 150d, 0.07387607124501986831545 },
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
            System.out.println(BESSEL_SUPPLIER.apply(5));
            System.out.println();
        }
    }

}
