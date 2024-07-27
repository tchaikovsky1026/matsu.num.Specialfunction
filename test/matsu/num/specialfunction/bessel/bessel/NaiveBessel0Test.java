package matsu.num.specialfunction.bessel.bessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NaiveBessel0}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@SuppressWarnings("deprecation")
@RunWith(Enclosed.class)
final class NaiveBessel0Test {

    public static final Class<?> TEST_CLASS = NaiveBessel0.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final Bessel0th BESSEL_0 = new NaiveBessel0();

    @RunWith(Theories.class)
    public static class 第1種ベッセルに関するテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.5, 0.9384698072408129042284 },
                { 1.5, 0.5118276717359181287491 },
                { 3, -0.2600519549019334376242 },
                { 6, 0.1506452572509969316623 },
                { 10, -0.2459357644513483351978 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_0.besselJ(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種ベッセルに関するテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.01, -3.005455637083645957779 },
                { 0.5, -0.4445187335067065571484 },
                { 1.5, 0.3824489237977588439551 },
                { 3, 0.3768500100127903819671 },
                { 6, -0.2881946839815791540691 },
                { 10, 0.05567116728359939142446 },
                { Math.nextDown(0d), Double.NaN },
                { 0d, Double.NEGATIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_0.besselY(dataPair[0]));
        }
    }
}
