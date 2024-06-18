package matsu.num.specialfunction.gamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link DigammaCalculation}クラスに関するテスト.
 * 
 * @author Matsuura Y.
 */

@RunWith(Enclosed.class)
final class DigammaCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final DigammaCalculation DIGAMMA = new DigammaCalculation();

    @RunWith(Theories.class)
    public static class メソッドdigammaに関する値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, -1.963510026021423479441 },
                { 1, -0.5772156649015328606065 },
                { 2, 0.4227843350984671393935 },
                { 3, 0.9227843350984671393935 },
                { 4, 1.256117668431800472727 },
                { 5, 1.506117668431800472727 },
                { 6, 1.706117668431800472727 },
                { 7, 1.872784335098467139394 },
                { 8, 2.015641477955609996536 },
                { 9, 2.140641477955609996536 },
                { 10, 2.251752589066721107647 },
                { 11, 2.351752589066721107647 },

                { 0d, Double.NEGATIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    DIGAMMA.digamma(dataPair[0]));
        }
    }
}
