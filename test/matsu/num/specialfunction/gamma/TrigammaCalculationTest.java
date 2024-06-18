package matsu.num.specialfunction.gamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link TrigammaCalculation}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class TrigammaCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final TrigammaCalculation TRIGAMMA = new TrigammaCalculation();

    @RunWith(Theories.class)
    public static class メソッドtrigammaに関する値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 4.934802200544679309417 },
                { 1, 1.644934066848226436472 },
                { 2, 0.6449340668482264364724 },
                { 3, 0.3949340668482264364724 },
                { 4, 0.2838229557371153253613 },
                { 5, 0.2213229557371153253613 },
                { 6, 0.1813229557371153253613 },
                { 7, 0.1535451779593375475835 },
                { 8, 0.1331370146940314251346 },
                { 9, 0.1175120146940314251346 },
                { 10, 0.1051663356816857461222 },
                { 11, 0.0951663356816857461222 },

                { 0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    TRIGAMMA.trigamma(dataPair[0]));
        }
    }
}
