/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.modbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NaiveMBessel0} クラスのテスト.
 * 元クラスは非推奨.
 */
@SuppressWarnings("deprecation")
@RunWith(Enclosed.class)
final class NaiveMBessel0Test {

    public static final Class<?> TEST_CLASS = NaiveMBessel0.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final ModifiedBessel0thOrder M_BESSEL_0 = new NaiveMBessel0();

    @RunWith(Theories.class)
    public static class 第1種変形ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.01, 1.000025000156250434028 },
                { 0.5, 1.063483370741323519263 },
                { 1, 1.266065877752008335598 },
                { 1.5, 1.646723189772890844876 },
                { 3, 4.880792585865024085611 },
                { 6, 67.23440697647797532619 },
                { 10, 2815.71662846625447147 },
                { 15, 339649.37329791387952170163 },
                { 23.75, 1697885300.7852367601621023 },
                { 24.25, 2770016089.3283488970557653 },
                { 30, 781672297823.97748971738982 },
                { 200, 2.0396871734097246195416731E+85 },
                { 500, 2.5048094765700780965514121E+215 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 4.7212447301610949651358777 },
                { 0.5, 0.92441907122766586178192417 },
                { 1, 0.42102443824070833333562738 },
                { 1.5, 0.213805562647525736721621 },
                { 1.875, 0.13290483903459228511813344 },
                { 2.125, 0.097764257654025402892179078 },
                { 5, 0.003691098334042594274735261 },
                { 10, 1.7780062316167651811301193E-5 },
                { 20, 5.7412378153365242927167021E-10 },
                { 50, 3.4101677497894955139206755E-23 },
                { 100, 4.6566282291759020189390053E-45 },
                { 200, 1.2256819797765334516600541E-88 },
                { 500, 3.9923216091177928773566356E-219 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第1種変形ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 0.01, 0.99007458514970749900835349 },
                { 0.5, 0.64503527044915006810799663 },
                { 1, 0.46575960759364043650190153 },
                { 1.5, 0.36743360905415833923824151 },
                { 3, 0.24300035416182539847261285 },
                { 6, 0.16665743263981657556308574 },
                { 10, 0.12783333716342860732305029 },
                { 15, 0.10389953144882272143099359 },
                { 23.75, 0.082302760738626168148462948 },
                { 24.25, 0.08144062641694107412221072 },
                { 30, 0.073145946482237293928923418 },
                { 200, 0.028227159949111915670340626 },
                { 500, 0.017845706500153167236536198 },
                { 1000, 0.012617240455891256585716131 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)exp(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 4.7686940285444619045571969 },
                { 0.5, 1.5241093857739095300229151 },
                { 1, 1.1444630798068950146990413 },
                { 1.5, 0.9582100532948964964167549 },
                { 1.875, 0.86664841556116527156024605 },
                { 2.125, 0.8185701073400160042879117 },
                { 5, 0.54780756431351898686820157 },
                { 10, 0.39163193443659866573392106 },
                { 20, 0.27854487665718222393316378 },
                { 50, 0.17680715585742933811176212 },
                { 100, 0.12517562165912657889155812 },
                { 200, 0.088567458339296658233884474 },
                { 500, 0.056035915417234515428362612 },
                { 1000, 0.039628321600754217114725922 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselKc(dataPair[0]));
        }
    }
}
