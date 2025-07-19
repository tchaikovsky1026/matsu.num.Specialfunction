/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.specialfunction.icgamma;

import java.util.function.DoubleFunction;

import org.junit.Ignore;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * IncompleteGammaFunction(a = 50000) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt50000 {

    private static final double paramA = 50000d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 50000;
    //        numeric xs[] = {49000, 49100, 49200, 49300, 49400, 49500, 49600, 49700, 
    //                       49800, 49820, 49840, 49860, 49880, 49900, 49920, 49940, 49960, 49980, 
    //                       50000, 50020, 50040, 50060, 50080, 50100, 50120, 50140, 50160, 50180,
    //                       50200, 50300, 50400, 50500, 50600, 50700, 50800, 50900, 51000};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 49000, 3.38476568467945370397E-6 },
            { 49100, 2.584737136346176565199E-5 },
            { 49200, 1.61924920126009613725E-4 },
            { 49300, 8.34736383943555849468E-4 },
            { 49400, 0.003557492987580795889917 },
            { 49500, 0.01263599152877904408535 },
            { 49600, 0.03794093805257537659916 },
            { 49700, 0.0984925608132460657232 },
            { 49800, 0.2279367565326113038198 },
            { 49820, 0.2667292143199465855155 },
            { 49840, 0.3112373843467916443055 },
            { 49860, 0.362253249112275352902 },
            { 49880, 0.4206950199558651012722 },
            { 49900, 0.4876325516987818453658 },
            { 49920, 0.5643183985088455962944 },
            { 49940, 0.6522258518384361340695 },
            { 49960, 0.753095661395512090309 },
            { 49980, 0.8689936040792325244676 },
            { 50000, 1.0023816652103802804 },
            { 50020, 1.1562063732214069736 },
            { 50040, 1.334008839330864946049 },
            { 50060, 1.540062372745503846789 },
            { 50080, 1.77954527059348211856 },
            { 50100, 2.058758656300571261453 },
            { 50120, 2.385402244871493047977 },
            { 50140, 2.768924899111119012881 },
            { 50160, 3.220972149074682681757 },
            { 50180, 3.755959946228525604377 },
            { 50200, 4.39181345793173298829 },
            { 50300, 10.10513460021779692128 },
            { 50400, 25.96700685479582768379 },
            { 50500, 76.70707932387637830944 },
            { 50600, 265.9251144597639262361 },
            { 50700, 1095.514828790201560626 },
            { 50800, 5397.957185341835211721 },
            { 50900, 31910.38684965765983244 },
            { 51000, 226656.6990695062679115 },

            { 0d, 0d },
            { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
            { Double.NaN, Double.NaN }
    };

    /**
     * @return パラメータaを与えて, 不完全ガンマを返すファンクション
     */
    abstract DoubleFunction<IncompleteGammaFunction> icgammaGetter();

    /**
     * @return 許容される相対誤差
     */
    abstract double acceptableRelativeError();

    @Theory
    public final void test_検証(double[] dataPair) {
        new DoubleRelativeAssertion(acceptableRelativeError())
                .compareAndAssert(
                        dataPair[1],
                        icgammaGetter().apply(paramA).rigammaOdds(dataPair[0]));
    }
}
