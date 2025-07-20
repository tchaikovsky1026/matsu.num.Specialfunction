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
 * IncompleteGammaFunction(a = 20000) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt20000 {

    private static final double paramA = 20000d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 20000;
    //        numeric xs[] = {19500, 19600, 19700, 19800, 19900,
    //                      19920, 19940, 19960, 19980, 20000, 20020, 20040, 20060, 20080,
    //                      20100, 20200, 20300, 20400, 20500};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 19500, 1.833036459566997007983E-4 },
            { 19600, 0.002224494974370533278327 },
            { 19700, 0.01688002121474178926139 },
            { 19800, 0.08495230233763860081096 },
            { 19900, 0.3159896550790526153994 },
            { 19920, 0.4012443823641510719091 },
            { 19940, 0.5069126950084219724292 },
            { 19960, 0.6379485074260839299679 },
            { 19980, 0.8007670861202953573738 },
            { 20000, 1.003768351819778525879 },
            { 20020, 1.258068217776953959314 },
            { 20040, 1.578530061086693466842 },
            { 20060, 1.985233618501813675729 },
            { 20080, 2.505587801895351012323 },
            { 20100, 3.17740094330837334713 },
            { 20200, 11.6594505284562745632 },
            { 20300, 56.82502297707549040724 },
            { 20400, 405.3973537446052551281 },
            { 20500, 4442.89072297371981101 },

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
