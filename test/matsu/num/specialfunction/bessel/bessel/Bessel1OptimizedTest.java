package matsu.num.specialfunction.bessel.bessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link Bessel1Optimized}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class Bessel1OptimizedTest {

    public static final Class<?> TEST_CLASS = Bessel1Optimized.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final Bessel1st BESSEL_1 = new Bessel1Optimized();

    @RunWith(Theories.class)
    public static class 第1種ベッセルに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,2.5,3,4,6,8,10,15,24,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselj(n,x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.01, 0.0049999375002604161241326226 },
                { 0.5, 0.24226845767487388638395458 },
                { 1, 0.4400505857449335159596822 },
                { 1.5, 0.55793650791009964199012121 },
                { 2.5, 0.49709410246427403801081628 },
                { 3, 0.3390589585259364589255146 },
                { 4, -0.066043328023549136143185421 },
                { 6, -0.2766838581275656081727748 },
                { 8, 0.23463634685391462438127665 },
                { 10, 0.043472746168861436669748768 },
                { 15, 0.20510403861352276114713741 },
                { 24, -0.15403806518312122128299789 },
                { 30, -0.11875106261662293652023427 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_1.besselJ(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種ベッセルに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,2.5,3,4,6,8,10,15,24,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,bessely(n,x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.01, -63.678596282060656374296621 },
                { 0.5, -1.4714723926702430691885846 },
                { 1, -0.78121282130028871654715 },
                { 1.5, -0.41230862697391129595282982 },
                { 2.5, 0.14591813796678579887875994 },
                { 3, 0.32467442479179997843701284 },
                { 4, 0.39792571055710000525397997 },
                { 6, -0.17501034430039825063678027 },
                { 8, -0.15806046173124749425555527 },
                { 10, 0.24901542420695388392328347 },
                { 15, 0.021073628036873511940451855 },
                { 24, 0.053059776121202168863346045 },
                { 30, 0.084425570661747234890922904 },
                { Math.nextDown(0d), Double.NaN },
                { 0, Double.NEGATIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_1.besselY(dataPair[0]));
        }
    }
}
