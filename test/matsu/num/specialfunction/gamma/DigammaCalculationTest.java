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
            new DoubleRelativeAssertion(1E-14);

    private static final DigammaCalculation DIGAMMA = new DigammaCalculation();

    @RunWith(Theories.class)
    public static class メソッドdigammaに関する値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75,3,
        //                       4,7,11,15};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = polygamma(x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.25, -4.22745353337626541 },
                { 0.5, -1.96351002602142348 },
                { 0.75, -1.08586087978647217 },
                { 1, -0.577215664901532861 },
                { 1.25, -0.227453533376265408 },
                { 1.5, 0.0364899739785765206 },
                { 1.75, 0.247472453546861164 },
                { 2, 0.422784335098467139 },
                { 2.25, 0.572546466623734592 },
                { 2.5, 0.703156640645243187 },
                { 2.75, 0.818901024975432592 },
                { 3, 0.922784335098467139 },
                { 4, 1.25611766843180047 },
                { 7, 1.87278433509846714 },
                { 11, 2.35175258906672111 },
                { 15, 2.6743466616607937 },

                { 0d, Double.NEGATIVE_INFINITY },
                { -0d, Double.NEGATIVE_INFINITY },
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
