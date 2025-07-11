/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.hermite;

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

import matsu.num.specialfunction.HermiteFunction;

/**
 * {@link HermiteFunctionImpl} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class HermiteFunctionImplTest {

    public static final Class<?> TEST_CLASS = HermiteFunctionImpl.class;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    // for(x = 0; x <= 100; x = x + 5){
    //     println(x, hermiteH(n,x));
    // }
    /* ------------------------------------ */

    @RunWith(Theories.class)
    public static class 次数5のテスト {

        private final HermiteFunction function = HermiteFunctionImpl.instanceOf(5);

        @DataPoints
        public static List<HermiteHFixture> fixtures;

        @BeforeClass
        public static void before_n_5() {
            double[][] data = new double[][] {
                    { 0, 0 },
                    { 5, 80600 },
                    { 10, 3041200 },
                    { 15, 23761800 },
                    { 20, 101122400 },
                    { 25, 310003000 },
                    { 30, 773283600 },
                    { 35, 1673844200 },
                    { 40, 3266564800d },
                    { 45, 5890325400d },
                    { 50, 9980006000d },
                    { 55, 16078486600d },
                    { 60, 24848647200d },
                    { 65, 37085367800d },
                    { 70, 53727528400d },
                    { 75, 75870009000d },
                    { 80, 104775689600d },
                    { 85, 141887450200d },
                    { 90, 188840170800d },
                    { 95, 247472731400d },
                    { 100, 319840012000d }
            };
            fixtures = Arrays.stream(data)
                    .map(pair -> new HermiteHFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(HermiteHFixture fixture) {
            double x = fixture.x;
            double expected = fixture.hermiteH;

            double result = function.hermiteH(x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-12)));
        }
    }

    @RunWith(Theories.class)
    public static class 次数20のテスト {

        private final HermiteFunction function = HermiteFunctionImpl.instanceOf(20);

        @DataPoints
        public static List<HermiteHFixture> fixtures;

        @BeforeClass
        public static void before_n_20() {
            double[][] data = new double[][] {
                    { 0, 670442572800d },
                    { 5, -225717767163827200d },
                    { 10, 3.653671097088802977697E+25 },
                    { 15, 2.243705873920358135825E+29 },
                    { 20, 8.6213493422351792651E+31 },
                    { 25, 8.17308506058531041492E+33 },
                    { 30, 3.286272623398517728647E+35 },
                    { 35, 7.379438546765106366573E+36 },
                    { 40, 1.086082974423734886665E+38 },
                    { 45, 1.15979734955292786292E+39 },
                    { 50, 9.62576769907173523902E+39 },
                    { 55, 6.51887701205660453495E+40 },
                    { 60, 3.733660290134314992227E+41 },
                    { 65, 1.85814836963327980356E+42 },
                    { 70, 8.205872683564387500362E+42 },
                    { 75, 3.26947737082132846646E+43 },
                    { 80, 1.191087741790558108655E+44 },
                    { 85, 4.011073819972208756013E+44 },
                    { 90, 1.259942417425801535365E+45 },
                    { 95, 3.719596281313308620474E+45 },
                    { 100, 1.03865255451176549451E+46 }
            };
            fixtures = Arrays.stream(data)
                    .map(pair -> new HermiteHFixture(pair[0], pair[1]))
                    .toList();
        }

        @Theory
        public void test(HermiteHFixture fixture) {
            double x = fixture.x;
            double expected = fixture.hermiteH;

            double result = function.hermiteH(x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-12)));
        }
    }
    
    public static class 特殊値に関するテスト {

        @Test
        public void test_n_100_x_1000() {
            assertThat(
                    HermiteFunctionImpl.instanceOf(100).hermiteH(1000d),
                    is(Double.POSITIVE_INFINITY));
        }

        @Test
        public void test_n_75_x_10000() {
            assertThat(
                    HermiteFunctionImpl.instanceOf(75).hermiteH(10000d),
                    is(Double.POSITIVE_INFINITY));
        }
    }

    @Ignore
    public static class HermiteHFixture {
        final double x;
        final double hermiteH;

        HermiteHFixture(double x, double hermiteH) {
            super();
            this.x = x;
            this.hermiteH = hermiteH;
        }
    }
}
