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
 * {@link ICGammaAtLowParam} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ICGammaAtLowParamTest {

    public static final Class<?> TEST_CLASS = ICGammaAtLowParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-11);

    @RunWith(Theories.class)
    public static class A_0_1のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 0.1;
        //        numeric xs[] = {0.001, 0.0015, 0.002, 0.003, 0.005, 0.007,
        //                        0.01, 0.015, 0.02, 0.03, 0.05, 0.07,
        //                        0.1, 0.15, 0.2, 0.3, 0.5, 0.7,
        //                        1, 1.5, 2, 3, 5, 7, 9};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(0.1);

        @DataPoints
        public static double[][] dataPairs = {
                { 0.001, 1.113130982451917865104 },
                { 0.0015, 1.215041530745478954306 },
                { 0.002, 1.296344565982558692046 },
                { 0.003, 1.426194030058421118899 },
                { 0.005, 1.621422115951627699254 },
                { 0.007, 1.774521590637269310574 },
                { 0.01, 1.964027904855762025928 },
                { 0.015, 2.222983382852128633604 },
                { 0.02, 2.442799778071946974029 },
                { 0.03, 2.820221203619305359729 },
                { 0.05, 3.455109688963134891382 },
                { 0.07, 4.016492852470388843474 },
                { 0.1, 4.798841423945022367626 },
                { 0.15, 6.046789448853855652933 },
                { 0.2, 7.293223626525804796025 },
                { 0.3, 9.912026013551285816577 },
                { 0.5, 16.06555871129138205908 },
                { 0.7, 23.96554531967849082113 },
                { 1, 40.4467506801753387732 },
                { 1.5, 87.15219793202544938393 },
                { 2, 175.2479773003493607189 },
                { 3, 637.8667027585103398339 },
                { 5, 6946.389083403577028583 },
                { 7, 67035.85244074603954809 },
                { 9, 607839.063482643912815 },

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
    public static class A_1のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 1;
        //        numeric xs[] = {0.01,0.015,0.02,0.03,0.05,0.07,
        //                        0.1,0.15,0.2,0.3,0.5,0.7,
        //                        1,1.5,2,3,5,7,10};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(1d);

        @DataPoints
        public static double[][] dataPairs = {
                { 0.01, 0.01005016708416805754217 },
                { 0.015, 0.01511306461571897927684 },
                { 0.02, 0.02020134002675581016014 },
                { 0.03, 0.03045453395351685561244 },
                { 0.05, 0.05127109637602403969752 },
                { 0.07, 0.0725081812542164790531 },
                { 0.1, 0.1051709180756476248117 },
                { 0.15, 0.1618342427282831226166 },
                { 0.2, 0.2214027581601698339211 },
                { 0.3, 0.3498588075760031039838 },
                { 0.5, 0.6487212707001281468487 },
                { 0.7, 1.013752707470476521625 },
                { 1, 1.71828182845904523536 },
                { 1.5, 3.481689070338064822602 },
                { 2, 6.38905609893065022723 },
                { 3, 19.08553692318766774093 },
                { 5, 147.4131591025766034211 },
                { 7, 1095.633158428458599264 },
                { 10, 22025.46579480671651696 },

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
    public static class A_5のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 5;
        //        numeric xs[] = {1, 1.5, 2, 
        //                    3, 3.2, 3.4, 3.6, 3.8, 4, 4.2, 4.4, 4.6, 4.8,
        //                    5, 5.2, 5.4, 5.6, 5.8, 6, 6.2, 6.4, 6.6, 6.8,
        //                    7, 10, 15, 20};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(5d);

        @DataPoints
        public static double[][] dataPairs = {
                { 1, 0.003673290507955163825337 },
                { 1.5, 0.01892753286549253515642 },
                { 2, 0.05557944270437860389006 },
                { 3, 0.2265976746984835261636 },
                { 3.2, 0.2810453148294212467395 },
                { 3.4, 0.3437579435332805158512 },
                { 3.6, 0.4155514900240573268997 },
                { 3.8, 0.4973565655269022301499 },
                { 4, 0.5902373796061428857702 },
                { 4.2, 0.695412322375516523998 },
                { 4.4, 0.8142768065720472362789 },
                { 4.6, 0.948428978691215908257 },
                { 4.8, 1.099698939677881624406 },
                { 5, 1.27018216600499584583 },
                { 5.2, 1.46227789299903983221 },
                { 5.4, 1.678733315316576051622 },
                { 5.6, 1.922694576101011475135 },
                { 5.8, 2.197765658645551173551 },
                { 6, 2.508076465154218457464 },
                { 6.2, 2.858361570043052857269 },
                { 6.4, 3.254051374690986274744 },
                { 6.6, 3.701377672191868724519 },
                { 6.8, 4.207495961186639268981 },
                { 7, 4.780627235291677219927 },
                { 10, 33.18489259411285543242 },
                { 15, 1166.349862954822350329 },
                { 20, 59014.35037219198126373 },

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
    public static class A_10のオッズ値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        a = 10;
        //        numeric xs[] = {1, 1.5, 2, 3, 
        //                       5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5,
        //                       10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5,
        //                       15, 20, 30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = gammap(a,x)/gammaq(a,x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(10d);

        @DataPoints
        public static double[][] dataPairs = {
                { 1, 1.114254907543592837842E-7 },
                { 1.5, 4.097517765977889783188E-6 },
                { 2, 4.65002371887809327475E-5 },
                { 3, 0.001103704951723905026032 },
                { 5, 0.03287438511970089866955 },
                { 5.5, 0.05683385016089113793253 },
                { 6, 0.09161250655166055595621 },
                { 6.5, 0.1397517436172100211447 },
                { 7, 0.2040998097172067845413 },
                { 7.5, 0.2879832490444785740455 },
                { 8, 0.3954314102851736738181 },
                { 8.5, 0.5314553468668537385274 },
                { 9, 0.7023935391599732036281 },
                { 9.5, 0.9163475131284812930491 },
                { 10, 1.183741234510929500656 },
                { 10.5, 1.518050650172490643628 },
                { 11, 1.936765772602380441051 },
                { 11.5, 2.462669348121339764308 },
                { 12, 3.125545946321962523931 },
                { 12.5, 3.964476565177887432569 },
                { 13, 5.030931079250040333969 },
                { 13.5, 6.392950358877277801513 },
                { 14, 8.14082049344213443916 },

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
                new ICGammaAtLowParam(0.1d),
                new ICGammaAtLowParam(1d),
                new ICGammaAtLowParam(5d),
                new ICGammaAtLowParam(10d)
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
