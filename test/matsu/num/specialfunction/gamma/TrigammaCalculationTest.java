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

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75,3,
        //                       4,7,11,15};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = polygamma(1,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.25, 17.1973291545071107 },
                { 0.5, 4.93480220054467931 },
                { 0.75, 2.5418796476716065 },
                { 1, 1.64493406684822644 },
                { 1.25, 1.19732915450711074 },
                { 1.5, 0.934802200544679309 },
                { 1.75, 0.764101869893828721 },
                { 2, 0.644934066848226437 },
                { 2.25, 0.557329154507110739 },
                { 2.5, 0.490357756100234865 },
                { 2.75, 0.437571257648930761 },
                { 3, 0.394934066848226436 },
                { 4, 0.283822955737115325 },
                { 7, 0.153545177959337548 },
                { 11, 0.0951663356816857461 },
                { 15, 0.0689382278476838062 },

                { 0d, Double.POSITIVE_INFINITY },
                { -0d, Double.POSITIVE_INFINITY },
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
