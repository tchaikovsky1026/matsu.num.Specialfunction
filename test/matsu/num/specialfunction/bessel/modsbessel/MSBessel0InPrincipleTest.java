/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.modsbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link MSBessel0InPrinciple} クラスのテスト
 */
@RunWith(Enclosed.class)
final class MSBessel0InPrincipleTest {

    public static final Class<?> TEST_CLASS = MSBessel0InPrinciple.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final MSBessel0 M_BESSEL_0 = new MSBessel0InPrinciple();

    @RunWith(Theories.class)
    public static class 第1種変形球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.01, 1.000016666750000198412974 },
                { 0.5, 1.0421906109874947232448513 },
                { 1, 1.1752011936438014568823819 },
                { 1.5, 1.419519636729878331222925 },
                { 3, 3.3392916424699672996581979 },
                { 6, 33.618859561713204687497011 },
                { 10, 1101.3232874703393377236525 },
                { 15, 108967.24574906015789937844 },
                { 23.75, 434310506.4757089719090023 },
                { 24.25, 701292908.87556162253026285 },
                { 30, 178107909692.07436911650781 },
                { 200, 1.8064934420314373145443693E+84 },
                { 500, 1.4035922178528374107397703E+214 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, k_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 99.004983374916805357390598 },
                { 0.5, 1.2130613194252668472075991 },
                { 1, 0.36787944117144232159552377 },
                { 1.5, 0.14875344009895321928885365 },
                { 1.875, 0.081789315650628513757634388 },
                { 2.125, 0.056203749772573937880412681 },
                { 5, 0.0013475893998170934193272097 },
                { 10, 4.5399929762484851535591516E-6 },
                { 20, 1.0305768112192789139829702E-10 },
                { 50, 3.8574996959278355660346856E-24 },
                { 100, 3.7200759760208359629596958E-46 },
                { 200, 6.9194826336836876532434073E-90 },
                { 500, 1.4249152813482571063098315E-220 },
                { 1000, 0 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.sbesselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第1種変形球ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.01, 0.99006633466223488895929479 },
                { 0.5, 0.63212055882855767840447623 },
                { 1, 0.43233235838169365405300025 },
                { 1.5, 0.31673764387737868567355253 },
                { 3, 0.16625354130388894026282581 },
                { 6, 0.083332821315637222649186776 },
                { 10, 0.049999999896942318878072109 },
                { 15, 0.033333333333330214125677053 },
                { 23.75, 0.021052631578947368421003164 },
                { 24.25, 0.020618556701030927835033724 },
                { 30, 0.016666666666666666666666667 },
                { 200, 0.0025 },
                { 500, 0.001 },
                { 1000, 5.E-4 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.sbesselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形球ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, k_n(x)exp(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 100 },
                { 0.5, 2 },
                { 1, 1 },
                { 1.5, 0.66666666666666666666666667 },
                { 1.875, 0.53333333333333333333333333 },
                { 2.125, 0.47058823529411764705882353 },
                { 5, 0.2 },
                { 10, 0.1 },
                { 20, 0.05 },
                { 50, 0.02 },
                { 100, 0.01 },
                { 200, 0.005 },
                { 500, 0.002 },
                { 1000, 0.001 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.sbesselKc(dataPair[0]));
        }
    }
}
