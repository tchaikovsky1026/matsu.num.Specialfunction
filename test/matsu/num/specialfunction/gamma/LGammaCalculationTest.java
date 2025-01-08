/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.gamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link LGammaCalculation} クラスのテスト.
 */
final class LGammaCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final LGammaCalculation L_GAMMA = new LGammaCalculation();

    @RunWith(Theories.class)
    public static class メソッドlgammaに関する {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75,3,
        //                       4,7,11,15};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,lngamma(x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.25, 1.28802252469807746 },
                { 0.5, 0.572364942924700087 },
                { 0.75, 0.203280951431295371 },
                { 1, 0 },
                { 1.25, -0.0982718364218131615 },
                { 1.5, -0.120782237635245222 },
                { 1.75, -0.084401121020485556 },
                { 2, 0 },
                { 2.25, 0.124871714892396594 },
                { 2.5, 0.28468287047291916 },
                { 2.75, 0.47521466691493713 },
                { 3, 0.693147180559945309 },
                { 4, 1.791759469228055 },
                { 7, 6.579251212010101 },
                { 11, 15.1044125730755153 },
                { 15, 25.1912211827386815 },

                { 0d, Double.POSITIVE_INFINITY },
                { -0d, Double.POSITIVE_INFINITY },
                { -1d, Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgamma(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgamma1pに関する {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-0.75,-0.5,-0.25,-0.0001,0,0.0001,0.25,0.5,0.75,1,
        //                       1.25,1.5,1.75,2};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,lngamma(1+x));
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -0.75, 1.28802252469807746 },
                { -0.5, 0.572364942924700087 },
                { -0.25, 0.203280951431295371 },
                { -1.E-4, 5.772979156120022E-5 },
                { 0, 0 },
                { 1.E-4, -5.7713342220477623E-5 },
                { 0.25, -0.0982718364218131615 },
                { 0.5, -0.120782237635245222 },
                { 0.75, -0.084401121020485556 },
                { 1, 0 },
                { 1.25, 0.124871714892396594 },
                { 1.5, 0.28468287047291916 },
                { 1.75, 0.47521466691493713 },
                { 2, 0.693147180559945309 },

                { -1d, Double.POSITIVE_INFINITY },
                { Math.nextDown(-1d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgamma1p(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaStirlingに関する {

        @DataPoints
        public static double[][] dataPairs = {
                { 1d, -1d + 0.5 * Math.log(2 * Math.PI) },
                { 2d, 1.5 * Math.log(2) - 2d + 0.5 * Math.log(2 * Math.PI) },
                { 3d, 2.5 * Math.log(3) - 3d + 0.5 * Math.log(2 * Math.PI) },

                { 0d, Double.POSITIVE_INFINITY },
                { -0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgammaStirling(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaStirlingResidualに関する {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {0.25,0.5,0.75,1,1.25,1.5,1.75,2,2.25,2.5,2.75,3,
        //                       4,7,11,15};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = lngamma(x) - (x - 1/2)*ln(x) + x - 1/2*ln(pi*2);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.25, 0.272510401213432061 },
                { 0.5, 0.153426409720027345 },
                { 0.75, 0.106262936339567862 },
                { 1, 0.081061466795327258 },
                { 1.25, 0.06543196688785678 },
                { 1.5, 0.054814121051917654 },
                { 1.75, 0.047140610855563344 },
                { 2, 0.0413406959554092941 },
                { 2.25, 0.036805303309148516 },
                { 2.5, 0.033162873519936287 },
                { 2.75, 0.030174082433684557 },
                { 3, 0.027677925684998339 },
                { 4, 0.02079067210376509 },
                { 7, 0.01189670994589177 },
                { 11, 0.00757367548795184 },
                { 15, 0.0055547335519628 },

                { 0d, Double.POSITIVE_INFINITY },
                { -0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgammaStirlingResidual(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaDiffに関する値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        xmin = 0.5;
        //        xmax = 13.5;
        //
        //        for(x = xmin; x <= xmax; x = x + 1){
        //            y = 1;
        //            g = lngamma(x+y) - lngamma(x);
        //            println(x,y,g);
        //        }
        //        for(x = xmin; x <= xmax; x = x + 1){
        //            y = 11;
        //            g = lngamma(x+y) - lngamma(x);
        //            println(x,y,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 1, -0.693147180559945309 },
                { 1.5, 1, 0.405465108108164382 },
                { 2.5, 1, 0.916290731874155065 },
                { 3.5, 1, 1.252762968495368 },
                { 4.5, 1, 1.50407739677627407 },
                { 5.5, 1, 1.70474809223842524 },
                { 6.5, 1, 1.87180217690159143 },
                { 7.5, 1, 2.01490302054226476 },
                { 8.5, 1, 2.14006616349627077 },
                { 9.5, 1, 2.25129179860649515 },
                { 10.5, 1, 2.35137525716347769 },
                { 11.5, 1, 2.44234703536920438 },
                { 12.5, 1, 2.52572864430825544 },
                { 13.5, 1, 2.60268968544438376 },
                { 0.5, 11, 15.7196355336425412 },
                { 1.5, 11, 18.8551297495716909 },
                { 2.5, 11, 20.975393285771782 },
                { 3.5, 11, 22.6617922393420107 },
                { 4.5, 11, 24.0831779202731714 },
                { 5.5, 11, 25.3199405474220983 },
                { 6.5, 11, 26.418552836090208 },
                { 7.5, 11, 27.4089515401180849 },
                { 8.5, 11, 28.3118192516600993 },
                { 9.5, 11, 29.1421675537335296 },
                { 10.5, 11, 29.911300641271397 },
                { 11.5, 11, 30.6279783192415364 },
                { 12.5, 11, 31.2991465930827065 },
                { 13.5, 11, 31.9304183699245643 },

                { Math.nextDown(0d), 1d, Double.NaN },
                { 1d, Math.nextDown(-1d), Double.NaN },

                { Double.POSITIVE_INFINITY, 1d, Double.NaN },
                { Double.POSITIVE_INFINITY, -1d, Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN },

                { 0d, 1d, Double.NEGATIVE_INFINITY },
                { Double.NaN, 1d, Double.NaN },
                { 1d, Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[2],
                    L_GAMMA.lgammaDiff(dataPair[0], dataPair[1]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaDiffに関する境界での有限評価 {

        @DataPoints
        public static double[][] dataPairs = {
                { Double.MIN_VALUE, 0 },
                { Double.MIN_VALUE, Double.MIN_VALUE },
                { Double.MIN_VALUE, 2 * Double.MIN_VALUE },
                { 2 * Double.MIN_VALUE, Double.MIN_VALUE },
                { Double.MIN_NORMAL, Double.MIN_VALUE },
                { Double.MIN_VALUE, Double.MIN_NORMAL }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.assertFinite(
                    L_GAMMA.lgammaDiff(dataPair[0], dataPair[1]));
        }
    }

    @RunWith(Enclosed.class)
    public static class メソッドlbetaに関する {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        xmin = 0.5;
        //        xmax = 13.5;
        //
        //        for(x = xmin; x <= xmax; x = x + 1){
        //            y = 1;
        //            g = ln(beta(x,y));
        //            println(x,y,g);
        //        }
        //        for(x = xmin; x <= xmax; x = x + 1){
        //            y = 11;
        //            g = ln(beta(x,y));
        //            println(x,y,g);
        //        }
        /* ------------------------------------ */

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.5, 1, 0.693147180559945309 },
                    { 1.5, 1, -0.405465108108164382 },
                    { 2.5, 1, -0.916290731874155065 },
                    { 3.5, 1, -1.252762968495368 },
                    { 4.5, 1, -1.50407739677627407 },
                    { 5.5, 1, -1.70474809223842524 },
                    { 6.5, 1, -1.87180217690159143 },
                    { 7.5, 1, -2.01490302054226476 },
                    { 8.5, 1, -2.14006616349627077 },
                    { 9.5, 1, -2.25129179860649515 },
                    { 10.5, 1, -2.35137525716347769 },
                    { 11.5, 1, -2.44234703536920438 },
                    { 12.5, 1, -2.52572864430825544 },
                    { 13.5, 1, -2.60268968544438376 },
                    { 0.5, 11, -0.61522296056702594 },
                    { 1.5, 11, -3.75071717649617563 },
                    { 2.5, 11, -5.87098071269626669 },
                    { 3.5, 11, -7.55737966626649539 },
                    { 4.5, 11, -8.97876534719765611 },
                    { 5.5, 11, -10.215527974346583 },
                    { 6.5, 11, -11.3141402630146927 },
                    { 7.5, 11, -12.3045389670425696 },
                    { 8.5, 11, -13.207406678584584 },
                    { 9.5, 11, -14.0377549806580143 },
                    { 10.5, 11, -14.8068880681958817 },
                    { 11.5, 11, -15.5235657461660211 },
                    { 12.5, 11, -16.1947340200071912 },
                    { 13.5, 11, -16.826005796849049 },

                    { Math.nextDown(0d), 1d, Double.NaN },
                    { Double.POSITIVE_INFINITY, 1d, Double.NaN },
                    { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN },
                    { 0d, 1d, Double.POSITIVE_INFINITY },
                    { 0d, 0d, Double.POSITIVE_INFINITY },
                    { Double.NaN, 1d, Double.NaN },
                    { 1d, Double.NaN, Double.NaN }

            };

            @Theory
            public void test_検証(double[] dataPair) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        dataPair[2],
                        L_GAMMA.lbeta(dataPair[0], dataPair[1]));
            }
        }
    }
}
