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
 * IncompleteGammaFunction(a = 5) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt5 {

    private static final double paramA = 5d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 5;
    //        numeric xs[] = {1, 1.5, 2, 
    //                    3, 3.2, 3.4, 3.6, 3.8, 4, 4.2, 4.4, 4.6, 4.8,
    //                    5, 5.2, 5.4, 5.6, 5.8, 6, 6.2, 6.4, 6.6, 6.8,
    //                    7, 10, 15, 20};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 1, 0.003673290507955163825337 },
            { 1.5, 0.01892753286549253515642 },
            { 2, 0.05557944270437860389006 },
            { 3, 0.2265976746984835261636 },
            { 3.2, 0.2810453148294212467395 },
            { 3.4, 0.3437579435332805158512 },
            { 3.6, 0.4155514900240573268997 },
            { 3.8, 0.4973565655269022301499 },
            { 4, 0.5902373796061428857702 },
            { 4.2, 0.695412322375516523998 },
            { 4.4, 0.8142768065720472362789 },
            { 4.6, 0.948428978691215908257 },
            { 4.8, 1.099698939677881624406 },
            { 5, 1.27018216600499584583 },
            { 5.2, 1.46227789299903983221 },
            { 5.4, 1.678733315316576051622 },
            { 5.6, 1.922694576101011475135 },
            { 5.8, 2.197765658645551173551 },
            { 6, 2.508076465154218457464 },
            { 6.2, 2.858361570043052857269 },
            { 6.4, 3.254051374690986274744 },
            { 6.6, 3.701377672191868724519 },
            { 6.8, 4.207495961186639268981 },
            { 7, 4.780627235291677219927 },
            { 10, 33.18489259411285543242 },
            { 15, 1166.349862954822350329 },
            { 20, 59014.35037219198126373 },

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
