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
            new DoubleRelativeAssertion(1E-14);

    private static final GammaCalculation GAMMA = new GammaCalculation(new LGammaCalculation());

    @RunWith(Theories.class)
    public static class メソッドgammaに関する値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {0.25,0.5,0.75,1,1.25,1.5,1.75,
        //                       2,2.25,2.5,2.75,3,3.5,10,20};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,gamma(x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.25, 3.62560990822190831 },
                { 0.5, 1.77245385090551603 },
                { 0.75, 1.22541670246517765 },
                { 1, 1 },
                { 1.25, 0.906402477055477078 },
                { 1.5, 0.886226925452758014 },
                { 1.75, 0.919062526848883234 },
                { 2, 1 },
                { 2.25, 1.13300309631934635 },
                { 2.5, 1.32934038817913702 },
                { 2.75, 1.60835942198554566 },
                { 3, 2 },
                { 3.5, 3.32335097044784255 },
                { 10, 362880 },
                { 20, 121645100408832000d },

                { 0d, Double.POSITIVE_INFINITY },
                { -0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { 200, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    GAMMA.gamma(dataPair[0]));
        }
    }
}
