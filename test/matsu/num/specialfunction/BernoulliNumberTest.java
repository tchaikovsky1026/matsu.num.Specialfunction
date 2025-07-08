/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link BernoulliNumber} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class BernoulliNumberTest {

    @RunWith(Theories.class)
    public static class 値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric ks[] = {0, 1, 2, 3, 4, 20, 100, 200};
        //
        //        for(index = 0; index < kei_length(ks); index = index + 1){
        //            k = ks[index];
        //            println(k, bernoulli(k));
        //        }
        /* ------------------------------------ */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 1, -0.5 },
                { 2, 0.1666666666666666666667 },
                { 3, 0 },
                { 4, -0.03333333333333333333333 },
                { 20, -529.1242424242424242424 },
                { 100, -2.838224957069370695926E+78 },
                { 200, -3.647077264519135436214E+215 }
        };

        @Theory
        public void test_値のテスト(double[] p) {
            int k = (int) (p[0] + 0.5);
            double expected = p[1];

            assertThat(BernoulliNumber.of(k), is(closeTo(expected, Math.abs(expected) * 1E-14)));
        }
    }
}
