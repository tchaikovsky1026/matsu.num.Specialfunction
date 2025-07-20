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
 * IncompleteGammaFunction(a = 10) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt10 {

    private static final double paramA = 10d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 10;
    //        numeric xs[] = {1, 1.5, 2, 3, 
    //                       5, 5.5, 6, 6.5, 7, 7.5, 8, 8.5, 9, 9.5,
    //                       10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5,
    //                       15, 20, 30};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 1, 1.114254907543592837842E-7 },
            { 1.5, 4.097517765977889783188E-6 },
            { 2, 4.65002371887809327475E-5 },
            { 3, 0.001103704951723905026032 },
            { 5, 0.03287438511970089866955 },
            { 5.5, 0.05683385016089113793253 },
            { 6, 0.09161250655166055595621 },
            { 6.5, 0.1397517436172100211447 },
            { 7, 0.2040998097172067845413 },
            { 7.5, 0.2879832490444785740455 },
            { 8, 0.3954314102851736738181 },
            { 8.5, 0.5314553468668537385274 },
            { 9, 0.7023935391599732036281 },
            { 9.5, 0.9163475131284812930491 },
            { 10, 1.183741234510929500656 },
            { 10.5, 1.518050650172490643628 },
            { 11, 1.936765772602380441051 },
            { 11.5, 2.462669348121339764308 },
            { 12, 3.125545946321962523931 },
            { 12.5, 3.964476565177887432569 },
            { 13, 5.030931079250040333969 },
            { 13.5, 6.392950358877277801513 },
            { 14, 8.14082049344213443916 },

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
