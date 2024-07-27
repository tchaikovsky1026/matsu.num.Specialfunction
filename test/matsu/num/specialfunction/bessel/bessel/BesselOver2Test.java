package matsu.num.specialfunction.bessel.bessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.bessel.BesselFunction;

/**
 * {@link BesselHigherImplY}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class BesselOver2Test {

    public static final Class<?> TEST_CLASS = BesselHigherImplY.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final IntFunction<BesselFunction> BESSEL_SUPPLIER =
            n -> new BesselOver2(
                    n,
                    new Bessel0Optimized(),
                    new Bessel1Optimized());

    @RunWith(Theories.class)
    public static class ベッセルの2次に関するbesselJのテスト {

        private static final int N = 2;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
    public static class ベッセルの5次に関するbesselJのテスト {

        private static final int N = 5;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
    public static class ベッセルの100次に関するbesselJのテスト {

        private static final int N = 100;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
}
