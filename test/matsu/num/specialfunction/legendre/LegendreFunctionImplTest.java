/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.legendre;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.LegendreFunction;

/**
 * {@link LegendreFunctionImpl} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class LegendreFunctionImplTest {

    public static final Class<?> TEST_CLASS = LegendreFunctionImpl.class;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* 符号が逆転する場合がある */
    /* ------------------------------------ */
    // for(x = -1; x <= 1; x = x + 0.1){
    //     println(x, legendreP(l,m,x));
    // }
    /* ------------------------------------ */

    @RunWith(Theories.class)
    public static class 次数5_階数2のテスト {

        private final LegendreFunction function = LegendreFunctionImpl.instanceOf(5, 2);

        @DataPoints
        public static List<LegendrePFixture> fixtures;

        @BeforeClass
        public static void before_l_5_m_2() {
            double[][] data = new double[][] {
                    { -1, 0 },
                    { -0.9, -12.837825 },
                    { -0.8, -13.9104 },
                    { -0.7, -8.808975 },
                    { -0.6, -1.6128 },
                    { -0.5, 4.921875 },
                    { -0.4, 9.1728 },
                    { -0.3, 10.462725 },
                    { -0.2, 8.8704 },
                    { -0.1, 5.041575 },
                    { 0, 0 },
                    { 0.1, -5.041575 },
                    { 0.2, -8.8704 },
                    { 0.3, -10.462725 },
                    { 0.4, -9.1728 },
                    { 0.5, -4.921875 },
                    { 0.6, 1.6128 },
                    { 0.7, 8.808975 },
                    { 0.8, 13.9104 },
                    { 0.9, 12.837825 },
                    { 1, 0 }
            };

            fixtures = Arrays.stream(data)
                    .map(pair -> new LegendrePFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(LegendrePFixture fixture) {
            double x = fixture.x;
            double expected = fixture.legendreP;

            double result = function.legendreP(x);

            assertThat(result, is(closeTo(expected, error(function.degreeL(), function.orderM()) * 1E-12)));
        }
    }

    @RunWith(Theories.class)
    public static class 次数6_階数4のテスト {

        private final LegendreFunction function = LegendreFunctionImpl.instanceOf(6, 4);

        @DataPoints
        public static List<LegendrePFixture> fixtures;

        @BeforeClass
        public static void before_l_6_m_4() {
            double[][] data = new double[][] {
                    { -1, 0 },
                    { -0.9, 134.9228475 },
                    { -0.8, 369.86544 },
                    { -0.7, 539.5189275 },
                    { -0.6, 572.86656 },
                    { -0.5, 465.1171875 },
                    { -0.4, 253.38096 },
                    { -0.3, -3.9127725 },
                    { -0.2, -243.85536 },
                    { -0.1, -412.1565525 },
                    { 0, -472.5 },
                    { 0.1, -412.1565525 },
                    { 0.2, -243.85536 },
                    { 0.3, -3.9127725 },
                    { 0.4, 253.38096 },
                    { 0.5, 465.1171875 },
                    { 0.6, 572.86656 },
                    { 0.7, 539.5189275 },
                    { 0.8, 369.86544 },
                    { 0.9, 134.9228475 },
                    { 1, 0 }
            };

            fixtures = Arrays.stream(data)
                    .map(pair -> new LegendrePFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(LegendrePFixture fixture) {
            double x = fixture.x;
            double expected = fixture.legendreP;

            double result = function.legendreP(x);

            assertThat(result, is(closeTo(expected, error(function.degreeL(), function.orderM()) * 1E-12)));
        }
    }

    @RunWith(Theories.class)
    public static class 次数20_階数8のテスト {

        private final LegendreFunction function = LegendreFunctionImpl.instanceOf(20, 8);

        @DataPoints
        public static List<LegendrePFixture> fixtures;

        @BeforeClass
        public static void before_l_20_m_8() {
            double[][] data = new double[][] {
                    { -1, 0 },
                    { -0.9, 7833098681.651619510092 },
                    { -0.8, -2997952246.169510386363 },
                    { -0.7, -883544935.177254454036 },
                    { -0.6, 4401129357.343235788537 },
                    { -0.5, -4708776236.695979372598 },
                    { -0.4, 608720712.8963584865534 },
                    { -0.3, 4071358761.182278248202 },
                    { -0.2, -3715050869.517752514348 },
                    { -0.1, -1462191649.014459692344 },
                    { 0, 4632336082.3974609375 },
                    { 0.1, -1462191649.014459692344 },
                    { 0.2, -3715050869.517752514348 },
                    { 0.3, 4071358761.182278248202 },
                    { 0.4, 608720712.8963584865534 },
                    { 0.5, -4708776236.695979372598 },
                    { 0.6, 4401129357.343235788537 },
                    { 0.7, -883544935.177254454036 },
                    { 0.8, -2997952246.169510386363 },
                    { 0.9, 7833098681.651619510092 },
                    { 1, 0 }
            };

            fixtures = Arrays.stream(data)
                    .map(pair -> new LegendrePFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(LegendrePFixture fixture) {
            double x = fixture.x;
            double expected = fixture.legendreP;

            double result = function.legendreP(x);

            assertThat(result, is(closeTo(expected, error(function.degreeL(), function.orderM()) * 1E-12)));
        }
    }

    static double error(int l, int m) {
        double log = 0.5 * (GammaFunction.lgamma(l + m + 1) - GammaFunction.lgamma(l - m + 1));
        return Math.exp(log);
    }

    @Ignore
    public static class LegendrePFixture {
        final double x;
        final double legendreP;

        LegendrePFixture(double x, double legendreP) {
            super();
            this.x = x;
            this.legendreP = legendreP;
        }
    }
}
