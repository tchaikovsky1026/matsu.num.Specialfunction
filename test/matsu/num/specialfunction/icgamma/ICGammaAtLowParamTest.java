/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icgamma;

import org.junit.Test;
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
            new DoubleRelativeAssertion(1E-8);

    @RunWith(Theories.class)
    public static class A_0_1のオッズ値のテスト {

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(0.1);

        @DataPoints
        public static double[][] dataPairs = {
                { 1.00E-03, 1.11313098245192000 },
                { 1.50E-03, 1.21504153074548000 },
                { 2.00E-03, 1.29634456598256000 },
                { 3.00E-03, 1.42619403005842000 },
                { 5.00E-03, 1.62142211595163000 },
                { 7.00E-03, 1.77452159063727000 },
                { 0.01, 1.96402790485576000 },
                { 0.015, 2.22298338285213000 },
                { 0.02, 2.44279977807195000 },
                { 0.03, 2.82022120361930000 },
                { 0.05, 3.45510968896313000 },
                { 0.07, 4.01649285247039000 },
                { 0.1, 4.79884142394502000 },
                { 0.15, 6.04678944885385000 },
                { 0.2, 7.29322362652580000 },
                { 0.3, 9.91202601355128000 },
                { 0.5, 16.06555871129140000 },
                { 0.7, 23.96554531967850000 },
                { 1, 40.44675068017520000 },
                { 1.5, 87.15219793202550000 },

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

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(1d);

        @DataPoints
        public static double[][] dataPairs = {
                { 1.00E-02, 0.01005016708416810 },
                { 1.50E-02, 0.01511306461571900 },
                { 2.00E-02, 0.02020134002675580 },
                { 3.00E-02, 0.03045453395351690 },
                { 5.00E-02, 0.05127109637602400 },
                { 7.00E-02, 0.07250818125421650 },
                { 0.1, 0.10517091807564800 },
                { 0.15, 0.16183424272828300 },
                { 0.2, 0.22140275816017000 },
                { 0.3, 0.34985880757600300 },
                { 0.5, 0.64872127070012800 },
                { 0.7, 1.01375270747048000 },
                { 1, 1.71828182845905000 },
                { 1.5, 3.48168907033807000 },
                { 2, 6.38905609893065000 },
                { 3, 19.08553692318770000 },
                { 5, 147.41315910257600000 },
                { 7, 1095.63315842851000000 },
                { 10, 22025.46579480390000000 },

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

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(5d);

        @DataPoints
        public static double[][] dataPairs = {
                { 0.3, 0.00001578528971310 },
                { 0.5, 0.00017214525884551 },
                { 0.7, 0.00078615300000283 },
                { 1, 0.00367329050795516 },
                { 1.5, 0.01892753286549250 },
                { 2, 0.05557944270437860 },
                { 3, 0.22659767469848400 },
                { 5, 1.27018216600500000 },
                { 7, 4.78062723529168000 },
                { 10, 33.18489259411290000 },
                { 15, 1166.34986295478000000 },
                { 20, 59014.35037245490000000 },

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

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtLowParam(10d);

        @DataPoints
        public static double[][] dataPairs = {
                { 1, 0.00000011142549075 },
                { 1.5, 0.00000409751776598 },
                { 2, 0.00004650023718878 },
                { 3, 0.00110370495172390 },
                { 5, 0.03287438511970090 },
                { 7, 0.20409980971720700 },
                { 10, 1.18374123451093000 },
                { 15, 13.31564201485650000 },
                { 20, 199.18367619765000000 },
                { 30, 140413.90909508000000000 },

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

    public static class toString表示 {

        @Test
        public void test_toSTring() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new ICGammaAtLowParam(5));
            System.out.println();
        }
    }
}
