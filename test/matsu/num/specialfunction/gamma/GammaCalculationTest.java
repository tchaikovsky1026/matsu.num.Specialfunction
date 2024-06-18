package matsu.num.specialfunction.gamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link GammaCalculation}区タスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class GammaCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final GammaCalculation GAMMA = new GammaCalculation(new LGammaCalculation());

    @RunWith(Theories.class)
    public static class メソッドgammaに関する値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 1.7724538509055200E+00 },
                { 1, 1.0000000000000000E+00 },
                { 1.5, 8.8622692545275800E-01 },
                { 2, 1.0000000000000000E+00 },
                { 2.5, 1.3293403881791400E+00 },
                { 3, 2.0000000000000000E+00 },
                { 4, 6.0000000000000000E+00 },
                { 5, 2.4000000000000000E+01 },
                { 6, 1.2000000000000000E+02 },
                { 7, 7.2000000000000000E+02 },
                { 8, 5.0400000000000000E+03 },
                { 9, 4.0320000000000000E+04 },
                { 10, 3.6288000000000000E+05 },
                { 11, 3.6288000000000000E+06 },
                { 12, 3.9916800000000000E+07 },
                { 16, 1.3076743680000000E+12 },
                { 20, 1.2164510040883200E+17 },
                { 24, 2.5852016738885000E+22 },
                { 28, 1.0888869450418400E+28 },
                { 32, 8.2228386541779200E+33 },
                { 36, 1.0333147966386100E+40 },
                { 40, 2.0397882081197400E+46 },
                { 44, 6.0415263063373800E+52 },
                { 48, 2.5862324151116800E+59 },

                { 0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { 200, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    GAMMA.gamma(dataPair[0]));
        }
    }
}
