package matsu.num.specialfunction.err;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ErrorFunctionCalculation}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ErrorFunctionCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-10);

    private static final ErrorFunctionCalculation ERR_FUNC = new ErrorFunctionCalculation();

    @RunWith(Theories.class)
    public static class erfに関するテスト {

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
                { 3, 0.9999779095030010 },
                { Double.NEGATIVE_INFINITY, -1d },
                { Double.POSITIVE_INFINITY, 1d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERR_FUNC.erf(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class erfcに関するテスト {

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
                { 3, 0.0000220904969986 },
                { Double.NEGATIVE_INFINITY, 2d },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERR_FUNC.erfc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class erfcxに関するテスト {

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
                { 3, 0.1790011511813900 },
                { Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERR_FUNC.erfcx(dataPair[0]));
        }
    }
}
