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
 * IncompleteGammaFunction(a = 100) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt100 {

    private static final double paramA = 100d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 100;
    //        numeric xs[] = {50, 60, 70,
    //                        80, 82, 84, 86, 88, 90, 92, 94, 96, 98,
    //                        100, 102, 104, 106, 108, 110, 112, 114, 116, 118,
    //                        120, 130, 140, 150};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 50, 3.200065325609167102268E-10 },
            { 60, 1.48152982757342492828E-6 },
            { 70, 4.305578952985795883139E-4 },
            { 80, 0.01740610207820858773564 },
            { 82, 0.03047361269397836457874 },
            { 84, 0.0508695764277889804999 },
            { 86, 0.08141732637251389547285 },
            { 88, 0.1256388048063017289762 },
            { 90, 0.1879602450927249695878 },
            { 92, 0.2740621752068080503099 },
            { 94, 0.3914381027793144321818 },
            { 96, 0.5502519977361543902961 },
            { 98, 0.7646349071986094569564 },
            { 100, 1.054648717661380351976 },
            { 102, 1.449291308755931985576 },
            { 104, 1.991159770008967733956 },
            { 106, 2.743794532774575492899 },
            { 108, 3.803417811547624334028 },
            { 110, 5.317970700792289332891 },
            { 112, 7.518435296427623174862 },
            { 114, 10.77111597422582594876 },
            { 116, 15.66617090000329023027 },
            { 118, 23.16971195549476317284 },
            { 120, 34.88893680206235993896 },
            { 130, 362.5823726711897575198 },
            { 140, 6207.975177819513728004 },
            { 150, 168788.4660807166410364 },

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
