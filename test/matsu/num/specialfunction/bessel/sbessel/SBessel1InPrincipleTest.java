/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.sbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link SBessel0InPrinciple} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SBessel1InPrincipleTest {

    public static final Class<?> TEST_CLASS = SBessel0InPrinciple.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final SBessel1 S_BESSEL_1 = new SBessel1InPrinciple();

    @RunWith(Theories.class)
    public static class 第1種球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,1.99,2.01,3,6,10,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselj(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, j_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 1.E-12, 3.333333333333333333333333E-13 },
                { 0.01, 0.0033333000001190473985893157766230683967952036637992 },
                { 0.5, 0.16253703063606656886058857565462624834392307754292 },
                { 1, 0.30116867893975678925156571418732239589025264018045 },
                { 1.5, 0.39617297071222225147086159732892633736845105981204 },
                { 1.99, 0.43519341496254226539196695693464544520385570638911 },
                { 2.01, 0.43557844014813674520928645677241586942066248725256 },
                { 3, 0.34567749976235595487949590966687713189199672823323 },
                { 6, -0.16778992272503116655792970094859558867610058165512 },
                { 10, 0.078466941798751547091838918163892710635156586384155 },
                { 30, -0.0062395279119115370128306814508008489480798239459392 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_1.sbesselJ(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,6,10,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbessely(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.NEGATIVE_INFINITY },
                { 1.E-12, -1000000000000000000000000.4999999999999999999999999 },
                { 0.01, -10000.499987500069444270833581348976705028602278339 },
                { 0.5, -4.4691813247698968650117022008464613841301875243202 },
                { 1, -1.3817732906760362240534389290732756033548734814163 },
                { 1.5, -0.69643541403279313622256662584288875250891918508734 },
                { 3, 0.062959163602315976774370931811881162539208811539866 },
                { 6, 0.019897852848421922675657788381901226453223097408284 },
                { 10, 0.062792826379701505863063405663378376513563602942954 },
                { 30, 0.032762996969886966276570894524688371464271930362814 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_1.sbesselY(dataPair[0]));
        }
    }
}
