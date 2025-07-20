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
 * IncompleteGammaFunction(a = 0.1) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt0_1 {

    private static final double paramA = 0.1d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 0.1;
    //        numeric xs[] = {0.001, 0.0015, 0.002, 0.003, 0.005, 0.007,
    //                        0.01, 0.015, 0.02, 0.03, 0.05, 0.07,
    //                        0.1, 0.15, 0.2, 0.3, 0.5, 0.7,
    //                        1, 1.5, 2, 3, 5, 7, 9};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 0.001, 1.113130982451917865104 },
            { 0.0015, 1.215041530745478954306 },
            { 0.002, 1.296344565982558692046 },
            { 0.003, 1.426194030058421118899 },
            { 0.005, 1.621422115951627699254 },
            { 0.007, 1.774521590637269310574 },
            { 0.01, 1.964027904855762025928 },
            { 0.015, 2.222983382852128633604 },
            { 0.02, 2.442799778071946974029 },
            { 0.03, 2.820221203619305359729 },
            { 0.05, 3.455109688963134891382 },
            { 0.07, 4.016492852470388843474 },
            { 0.1, 4.798841423945022367626 },
            { 0.15, 6.046789448853855652933 },
            { 0.2, 7.293223626525804796025 },
            { 0.3, 9.912026013551285816577 },
            { 0.5, 16.06555871129138205908 },
            { 0.7, 23.96554531967849082113 },
            { 1, 40.4467506801753387732 },
            { 1.5, 87.15219793202544938393 },
            { 2, 175.2479773003493607189 },
            { 3, 637.8667027585103398339 },
            { 5, 6946.389083403577028583 },
            { 7, 67035.85244074603954809 },
            { 9, 607839.063482643912815 },

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
