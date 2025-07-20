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
 * IncompleteGammaFunction(a = 1_000_000) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt100000 {

    private static final double paramA = 100_000d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 100000;
    //        numeric xs[] = {96000, 98000,
    //                        99500, 99550, 99600, 99650, 99700,
    //                        99750, 99800, 99850, 99900, 99950,
    //                        100000, 100050, 100100, 100150, 100200,
    //                        100250, 100300, 100350, 100400, 100450,
    //                        101000, 102000, 104000};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 96000, 6.27356078592503356878824865342E-38 },
            { 98000, 9.69083515909960930652937497528E-11 },
            { 99500, 0.0601551352632406710147887144291 },
            { 99550, 0.0836669174886480495692163476314 },
            { 99600, 0.114625444999575781095469377389 },
            { 99650, 0.15491982981821923734909599052 },
            { 99700, 0.206880155133899777482575383996 },
            { 99750, 0.273419496463437210261877541548 },
            { 99800, 0.358236403885847565212965233372 },
            { 99850, 0.466102403744403154952910320874 },
            { 99900, 0.603270396498799116867731357294 },
            { 99950, 0.778057505299382839854630871758 },
            { 100000, 1.00168350434305901100467306416 },
            { 100050, 1.28948913311511410152528198739 },
            { 100100, 1.66272675914344916345476461654 },
            { 100150, 2.15122459036732577419455729275 },
            { 100200, 2.79740120299293537212429534553 },
            { 100250, 3.66239418046832269257162373204 },
            { 100300, 4.83554198267716315525750630064 },
            { 100350, 6.4492556671006380208154081295 },
            { 100400, 8.7026731043498127306537446773 },
            { 100450, 11.8998256148453314792502804094 },
            { 101000, 1235.97846236316408039025789762 },
            { 102000, 6058380482.55953732403616588717 },
            { 104000, 2.22806131255709285830930031469E+35 },

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
