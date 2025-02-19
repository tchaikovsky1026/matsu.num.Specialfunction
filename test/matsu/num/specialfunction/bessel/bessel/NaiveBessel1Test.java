/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.bessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NaiveBessel1} クラスのテスト.
 */
@SuppressWarnings("deprecation")
@RunWith(Enclosed.class)
final class NaiveBessel1Test {

    public static final Class<?> TEST_CLASS = NaiveBessel1.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final Bessel1st BESSEL_1 = new NaiveBessel1();

    @RunWith(Theories.class)
    public static class 第1種ベッセルに関するテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 0.242268457674873886384 },
                { 1.5, 0.5579365079100996419901 },
                { 3, 0.3390589585259364589255 },
                { 6, -0.2766838581275656081728 },
                { 10, 0.04347274616886143666975 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
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

        @DataPoints
        public static double[][] dataPairs = {
                { 0.01, -63.6785962820606563743 },
                { 0.5, -1.471472392670243069189 },
                { 1.5, -0.4123086269739112959528 },
                { 3, 0.324674424791799978437 },
                { 6, -0.1750103443003982506368 },
                { 10, 0.2490154242069538839233 },
                { Math.nextDown(0d), Double.NaN },
                { 0, Double.NEGATIVE_INFINITY },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    BESSEL_1.besselY(dataPair[0]));
        }
    }
}
