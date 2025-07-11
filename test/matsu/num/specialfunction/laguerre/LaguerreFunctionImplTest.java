/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.specialfunction.laguerre;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.LaguerreFunction;

/**
 * {@link LaguerreFunctionImpl} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class LaguerreFunctionImplTest {

    public static final Class<?> TEST_CLASS = LaguerreFunctionImpl.class;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* 符号が逆転する場合がある */
    /* ------------------------------------ */
    // for(x = 0; x <= 100; x = x + 5){
    //     println(x, laguerreL(n,k,x));
    // }
    /* ------------------------------------ */

    @RunWith(Theories.class)
    public static class 次数5_階数2のテスト {

        private final LaguerreFunction function = LaguerreFunctionImpl.instanceOf(5, 2);

        @DataPoints
        public static List<LaguerreLFixture> fixtures;

        @BeforeClass
        public static void before_n_5_k_2() {
            double[][] data = new double[][] {
                    { 0, 21 },
                    { 5, 2.25 },
                    { 10, 4.333333333333333333333 },
                    { 15, 58.5 },
                    { 20, -1679 },
                    { 25, -12051.91666666666666667 },
                    { 30, -46029 },
                    { 35, -129829 },
                    { 40, -304045.6666666666666667 },
                    { 45, -626772.75 },
                    { 50, -1176729 },
                    { 55, -2056383.166666666666667 },
                    { 60, -3395079 },
                    { 65, -5352160.25 },
                    { 70, -8120095.666666666666667 },
                    { 75, -11927604 },
                    { 80, -17042779 },
                    { 85, -23776214.41666666666667 },
                    { 90, -32484129 },
                    { 95, -43571491.5 },
                    { 100, -57495145.66666666666667 }
            };

            fixtures = Arrays.stream(data)
                    .map(pair -> new LaguerreLFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(LaguerreLFixture fixture) {
            double x = fixture.x;
            double expected = fixture.laguerreL;

            double result = function.laguerreL(x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-12)));
        }
    }

    @RunWith(Theories.class)
    public static class 次数20_階数10のテスト {

        private final LaguerreFunction function = LaguerreFunctionImpl.instanceOf(20, 10);

        @DataPoints
        public static List<LaguerreLFixture> fixtures;

        @BeforeClass
        public static void before_n_20_k_10() {
            double[][] data = new double[][] {
                    { 0, 30045015 },
                    { 5, -1795.486420939170528845 },
                    { 10, -1755.62268612585945497 },
                    { 15, -2249.94696548555371 },
                    { 20, -9081.67266447379402 },
                    { 25, 9118.0292450556846 },
                    { 30, 95771.650912113097 },
                    { 35, -795849.072902565757 },
                    { 40, 5089151.92727792429 },
                    { 45, -29207130.4657050015 },
                    { 50, 97400399.4502068047 },
                    { 55, 786004293.969999285 },
                    { 60, -16009352450.477026291 },
                    { 65, 73107994132.67810774 },
                    { 70, 842271286905.0100437 },
                    { 75, -6920208501480.5718387 },
                    { 80, -79901725466796.9200102 },
                    { 85, 142069583521459.533128 },
                    { 90, 7944103675858642.63309 },
                    { 95, 83332213632934622.44873 },
                    { 100, 574298218933882834.1516 }
            };

            fixtures = Arrays.stream(data)
                    .map(pair -> new LaguerreLFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(LaguerreLFixture fixture) {
            double x = fixture.x;
            double expected = fixture.laguerreL;

            double result = function.laguerreL(x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-12)));
        }
    }

    public static class 特殊値に関するテスト {

        @Test
        public void test_n_100_k_100_x_100000() {
            assertThat(
                    LaguerreFunctionImpl.instanceOf(100, 100).laguerreL(100000d),
                    is(Double.POSITIVE_INFINITY));
        }

        @Test
        public void test_n_100_k_97_x_100000() {
            assertThat(
                    LaguerreFunctionImpl.instanceOf(100, 97).laguerreL(100000d),
                    is(Double.POSITIVE_INFINITY));
        }

        @Test
        public void test_n_97_k_100_x_100000() {
            assertThat(
                    LaguerreFunctionImpl.instanceOf(97, 100).laguerreL(100000d),
                    is(Double.NEGATIVE_INFINITY));
        }

        @Test
        public void test_n_97_k_97_x_100000() {
            assertThat(
                    LaguerreFunctionImpl.instanceOf(97, 97).laguerreL(100000d),
                    is(Double.NEGATIVE_INFINITY));
        }
    }

    @Ignore
    public static class LaguerreLFixture {
        final double x;
        final double laguerreL;

        LaguerreLFixture(double x, double laguerreL) {
            super();
            this.x = x;
            this.laguerreL = laguerreL;
        }
    }
}
