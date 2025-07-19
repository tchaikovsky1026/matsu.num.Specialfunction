/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icgamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@link ICGammaAtMiddleParam} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ICGammaAtMiddleParamTest {

    public static final Class<?> TEST_CLASS = ICGammaAtMiddleParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-9);

    @RunWith(Theories.class)
    public static class A_20のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 20;
        //        numeric xs[] = {1, 1.5, 2, 3, 5, 7, 10,
        //                      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
        //                      30, 50, 70, 100};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtMiddleParam(20);

        @DataPoints
        public static double[][] dataPairs = {
                { 1, 1.587527601073262957485E-19, },
                { 1.5, 3.283434196613645319487E-16, },
                { 2, 6.443731393112509126566E-14, },
                { 3, 8.314423588882995253264E-11, },
                { 5, 3.452137012639044249723E-7, },
                { 7, 4.440444820710751390893E-5, },
                { 10, 0.003466315816081875651285, },
                { 15, 0.1425714543331721724558, },
                { 16, 0.2311502761939163618226, },
                { 17, 0.3581020004882412903479, },
                { 18, 0.53629624004284596464, },
                { 19, 0.7837795557518691455946, },
                { 20, 1.126495581283288189202, },
                { 21, 1.602385846818592414939, },
                { 22, 2.267685311457105663315, },
                { 23, 3.206740938224275222958, },
                { 24, 4.547526615040360700149, },
                { 30, 44.71748658332183929564, },
                { 50, 2087090.271463798379566, },
                { 70, 1969352219538.635699462, },
                { 100, 2.656117576269062196828E+22 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_GAMMA.rigammaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class A_20000のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 20000;
        //        numeric xs[] = {19500, 19600, 19700, 19800, 19900,
        //                      19920, 19940, 19960, 19980, 20000, 20020, 20040, 20060, 20080,
        //                      20100, 20200, 20300, 20400, 20500};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtMiddleParam(20000);

        @DataPoints
        public static double[][] dataPairs = {
                { 19500, 1.833036459566997007983E-4 },
                { 19600, 0.002224494974370533278327 },
                { 19700, 0.01688002121474178926139 },
                { 19800, 0.08495230233763860081096 },
                { 19900, 0.3159896550790526153994 },
                { 19920, 0.4012443823641510719091 },
                { 19940, 0.5069126950084219724292 },
                { 19960, 0.6379485074260839299679 },
                { 19980, 0.8007670861202953573738 },
                { 20000, 1.003768351819778525879 },
                { 20020, 1.258068217776953959314 },
                { 20040, 1.578530061086693466842 },
                { 20060, 1.985233618501813675729 },
                { 20080, 2.505587801895351012323 },
                { 20100, 3.17740094330837334713 },
                { 20200, 11.6594505284562745632 },
                { 20300, 56.82502297707549040724 },
                { 20400, 405.3973537446052551281 },
                { 20500, 4442.89072297371981101 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_GAMMA.rigammaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 境界値テスト {

        @DataPoints
        public static final IncompleteGammaFunction[] icGammas = {
                new ICGammaAtMiddleParam(20)
        };

        @Theory
        public void test_0検証_P(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    0d, icgamma.rigammaP(0d));
        }

        @Theory
        public void test_正の無限大検証_P(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d, icgamma.rigammaP(Double.POSITIVE_INFINITY));
        }

        @Theory
        public void test_0検証_Q(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d, icgamma.rigammaQ(0d));
        }

        @Theory
        public void test_正の無限大検証_Q(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    0d, icgamma.rigammaQ(Double.POSITIVE_INFINITY));
        }
    }
}
