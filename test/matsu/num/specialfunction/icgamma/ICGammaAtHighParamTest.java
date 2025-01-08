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
 * {@link ICGammaAtHighParam} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class ICGammaAtHighParamTest {

    public static final Class<?> TEST_CLASS = ICGammaAtHighParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-6);

    @RunWith(Theories.class)
    public static class A_50000のオッズ値のテスト {

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtHighParam(50000);

        @DataPoints
        public static double[][] dataPairs = {
                { 4.9105572809000100E+04, 0.00002877914982021 },
                { 4.9329179606750100E+04, 0.00129922048514770 },
                { 4.9552786404500000E+04, 0.02302642083196370 },
                { 4.9776393202250000E+04, 0.18857227426484500 },
                { 5.0000000000000000E+04, 1.00238166521038000 },
                { 5.0223606797750000E+04, 5.30300631662694000 },
                { 5.0447213595500000E+04, 42.49519530662480000 },
                { 5.0670820393249900E+04, 711.63832668614000000 },
                { 5.0894427190999900E+04, 28762.90666680780000000 },

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
                new ICGammaAtHighParam(50000)
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
            System.out.println(new ICGammaAtHighParam(50000));
            System.out.println();
        }
    }
}
