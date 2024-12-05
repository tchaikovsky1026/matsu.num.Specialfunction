package matsu.num.specialfunction.bessel.bessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link Bessel0Optimized}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class Bessel0OptimizedTest {

    public static final Class<?> TEST_CLASS = Bessel0Optimized.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final Bessel0th BESSEL_0 = new Bessel0Optimized();

    @RunWith(Theories.class)
    public static class 第1種ベッセルに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,2.5,3,4,6,8,10,15,24,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselj(n,x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.01, 0.99997500015624956597290039 },
                { 0.5, 0.93846980724081290422840467 },
                { 1, 0.76519768655796655144971753 },
                { 1.5, 0.51182767173591812874905174 },
                { 2.5, -0.048383776468197996327287779 },
                { 3, -0.2600519549019334376241547 },
                { 4, -0.39714980986384737228659077 },
                { 6, 0.15064525725099693166232795 },
                { 8, 0.17165080713755390609086941 },
                { 10, -0.24593576445134833519776086 },
                { 15, -0.014224472826780773233864271 },
                { 24, -0.056230274166859267014776118 },
                { 30, -0.086367983581040211335962324 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
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

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,2.5,3,4,6,8,10,15,24,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,bessely(n,x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.01, -3.0054556370836459577788581 },
                { 0.5, -0.44451873350670655714839848 },
                { 1, 0.088256964215676957982926766 },
                { 1.5, 0.38244892379775884395506855 },
                { 2.5, 0.49807035961523188782747235 },
                { 3, 0.37685001001279038196711019 },
                { 4, -0.016940739325064991903635135 },
                { 6, -0.28819468398157915406912776 },
                { 8, 0.2235214893875662205273234 },
                { 10, 0.055671167283599391424459877 },
                { 15, 0.20546429603891826479192936 },
                { 24, -0.15283402879758777874157286 },
                { 30, -0.11729573168666402525124788 },
                { Math.nextDown(0d), Double.NaN },
                { 0d, Double.NEGATIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_0.besselY(dataPair[0]));
        }
    }
}
