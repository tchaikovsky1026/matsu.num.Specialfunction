package matsu.num.specialfunction.err;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ErfCalcOptimized}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ErfCalcOptimizedTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final ErrorFunctionCalculation ERF = new ErfCalcOptimized();

    @RunWith(Theories.class)
    public static class erfに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-10,-7,-5,-3,-2.5,-2,-1.25,-0.75,-0.25,0,
        //                       0.25,0.75,1.25,2,2.5,3,5,7,10};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = erf(x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -10, -1 },
                { -7, -1 },
                { -5, -0.99999999999846254 },
                { -3, -0.999977909503001415 },
                { -2.5, -0.999593047982555041 },
                { -2, -0.995322265018952734 },
                { -1.25, -0.92290012825645823 },
                { -0.75, -0.711155633653515132 },
                { -0.25, -0.276326390168236933 },
                { 0, 0 },
                { 0.25, 0.276326390168236933 },
                { 0.75, 0.711155633653515132 },
                { 1.25, 0.92290012825645823 },
                { 2, 0.995322265018952734 },
                { 2.5, 0.999593047982555041 },
                { 3, 0.999977909503001415 },
                { 5, 0.99999999999846254 },
                { 7, 1 },
                { 10, 1 },

                { Double.NEGATIVE_INFINITY, -1d },
                { Double.POSITIVE_INFINITY, 1d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERF.erf(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class erfcに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-10,-7,-5,-3,-2.5,-2,-1.25,-0.75,-0.25,0,
        //                       0.25,0.75,1.25,2,2.5,3,5,7,10};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = erfc(x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -10, 2 },
                { -7, 2 },
                { -5, 1.99999999999846254 },
                { -3, 1.99997790950300142 },
                { -2.5, 1.99959304798255504 },
                { -2, 1.99532226501895273 },
                { -1.25, 1.92290012825645823 },
                { -0.75, 1.71115563365351513 },
                { -0.25, 1.27632639016823693 },
                { 0, 1 },
                { 0.25, 0.723673609831763067 },
                { 0.75, 0.288844366346484868 },
                { 1.25, 0.0770998717435417699 },
                { 2, 0.00467773498104726584 },
                { 2.5, 4.0695201744495894E-4 },
                { 3, 2.20904969985854414E-5 },
                { 5, 1.53745979442803485E-12 },
                { 7, 4.1838256077794144E-23 },
                { 10, 2.08848758376254476E-45 },

                { Double.NEGATIVE_INFINITY, 2d },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERF.erfc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class erfcxに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-10,-7,-5,-3,-2.5,-2,-1.25,-0.75,-0.25,0,
        //                       0.25,0.75,1.25,2,2.5,3,5,7,10};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = erfc(x)*exp(x*x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -10, 5.3762342836322709E+43 },
                { -7, 3.81469314499019938E+21 },
                { -5, 144009798674.66104 },
                { -3, 16205.9888539995866 },
                { -2.5, 1035.81484297262291 },
                { -2, 108.940904389977972 },
                { -1.25, 9.17364344748284443 },
                { -0.75, 3.00317166362745231 },
                { -0.25, 1.35864237010472212 },
                { 0, 1 },
                { 0.25, 0.770346547730996744 },
                { 0.75, 0.506937650293144806 },
                { 1.25, 0.367822916452361093 },
                { 2, 0.255395676310505744 },
                { 2.5, 0.210806364061143581 },
                { 3, 0.17900115118138995 },
                { 5, 0.110704637733068626 },
                { 7, 0.0798000543291529335 },
                { 10, 0.0561409927438225859 },

                { Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERF.erfcx(dataPair[0]));
        }
    }
}
