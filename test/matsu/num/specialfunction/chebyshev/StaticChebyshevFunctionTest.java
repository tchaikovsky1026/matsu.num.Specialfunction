/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.chebyshev;

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

/**
 * {@link StaticChebyshevFunction} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class StaticChebyshevFunctionTest {

    public static final Class<?> TEST_CLASS = StaticChebyshevFunction.class;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    // for(x = -1; x <= 1; x = x + 0.1){
    //     println(x, chebyshevT(n,x), chebyshevU(n,x));
    // }
    /* ------------------------------------ */

    @RunWith(Theories.class)
    public static class 次数10のテスト {

        private final StaticChebyshevFunction function = new StaticChebyshevFunction();

        private static final int DEGREE = 10;

        @DataPoints
        public static List<ChebyshevFixture> fixtures;

        @BeforeClass
        public static void before_n_10() {
            double[][] data = new double[][] {
                    { -1, 1, 11 },
                    { -0.9, -0.2007474688, -2.2234571776 },
                    { -0.8, 0.9884965888, 1.1901541376 },
                    { -0.7, -0.0998400512, 0.8754584576 },
                    { -0.6, -0.9884965888, -0.8750642176 },
                    { -0.5, -0.5, -1 },
                    { -0.4, 0.5623462912, 0.2014567424 },
                    { -0.3, 0.9955225088, 1.0252491776 },
                    { -0.2, 0.4284556288, 0.6128946176 },
                    { -0.1, -0.5388927488, -0.4542309376 },
                    { 0, -1, -1 },
                    { 0.1, -0.5388927488, -0.4542309376 },
                    { 0.2, 0.4284556288, 0.6128946176 },
                    { 0.3, 0.9955225088, 1.0252491776 },
                    { 0.4, 0.5623462912, 0.2014567424 },
                    { 0.5, -0.5, -1 },
                    { 0.6, -0.9884965888, -0.8750642176 },
                    { 0.7, -0.0998400512, 0.8754584576 },
                    { 0.8, 0.9884965888, 1.1901541376 },
                    { 0.9, -0.2007474688, -2.2234571776 },
                    { 1, 1, 11 }
            };
            fixtures = Arrays.stream(data)
                    .map(pair -> new ChebyshevFixture(pair[0], pair[1], pair[2]))
                    .toList();
        }

        @Theory
        public void test_chebyshevT(ChebyshevFixture fixture) {
            double x = fixture.x;
            double expected = fixture.chebyshevT;

            double result = function.chebyshevT(DEGREE, x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-14)));
        }

        @Theory
        public void test_chebyshevU(ChebyshevFixture fixture) {
            double x = fixture.x;
            double expected = fixture.chebyshevU;

            double result = function.chebyshevU(DEGREE, x);

            assertThat(result, is(closeTo(expected, Math.abs(expected) * 1E-14)));
        }
    }

    @Ignore
    public static class ChebyshevFixture {
        final double x;
        final double chebyshevT;
        final double chebyshevU;

        ChebyshevFixture(double x, double chebyshevT, double chebyshevU) {
            super();
            this.x = x;
            this.chebyshevT = chebyshevT;
            this.chebyshevU = chebyshevU;
        }
    }
}
