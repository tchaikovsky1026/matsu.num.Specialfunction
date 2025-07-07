/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.7
 */
package matsu.num.specialfunction.zeta;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link EMTypeZetaFunctionCalculation} のテスト.
 */
final class EMTypeZetaFunctionCalculationTest {

    private static final EMTypeZetaFunctionCalculation ZETA_FUNC = new EMTypeZetaFunctionCalculation();

    @RunWith(Theories.class)
    public static class zetam1の値のテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric ss[] = {1.01, 2.0, 4.0, 6.0, 8.0, 10.0, 15.0, 30.0, 50.0, 100.0};
        //
        //        for(index = 0; index < kei_length(ss); index = index + 1){
        //            s = ss[index];
        //            println(s, zeta(s) - 1);
        //        }
        /* ------------------------------------ */
        @DataPoints
        public static double[][] dataPairs = {
                { 1.01, 99.57794333849687249028 },
                { 2, 0.6449340668482264364724 },
                { 4, 0.082323233711138191516 },
                { 6, 0.01734306198444913971452 },
                { 8, 0.004077356197944339378685 },
                { 10, 9.94575127818085337146E-4 },
                { 15, 3.058823630702049355173E-5 },
                { 30, 9.313274324196681828718E-10 },
                { 50, 8.881784210930815903096E-16 },
                { 100, 7.888609052210118073521E-31 }
        };

        @Theory
        public void test_値のテスト(double[] p) {
            double s = p[0];
            double expected = p[1];

            assertThat(ZETA_FUNC.zetam1(s), is(closeTo(expected, Math.abs(expected) * 1E-14)));
        }
    }
}
