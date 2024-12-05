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
final class BesselHigherImplYTest {

    public static final Class<?> TEST_CLASS = BesselHigherImplY.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final IntFunction<BesselFunction> BESSEL_SUPPLIER =
            n -> new BesselHigherOrderImpl(
                    n,
                    new Bessel0Optimized(),
                    new Bessel1Optimized());

    private static final class BesselHigherOrderImpl extends BesselHigherImplY {

        BesselHigherOrderImpl(int n, Bessel0th bessel0, Bessel1st bessel1) {
            super(n, bessel0, bessel1);
        }

        @Override
        public double besselJ(double x) {
            throw new UnsupportedOperationException("besselJを呼んではいけない");
        }
    }

    @RunWith(Theories.class)
    public static class ベッセルの2次に関するbesselYのテスト {

        private static final int N = 2;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_N.besselY(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class ベッセルの5次に関するbesselYのテスト {

        private static final int N = 5;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_N.besselY(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class ベッセルの100次に関するbesselYのテスト {

        private static final int N = 100;
        private static final BesselFunction BESSEL_N = BESSEL_SUPPLIER.apply(N);

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
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_N.besselY(dataPair[0]));
        }
    }
}
