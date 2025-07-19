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
 * IncompleteGammaFunction(a = 20) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt20 {

    private static final double paramA = 20d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 20;
    //        numeric xs[] = {1, 1.5, 2, 3, 5, 7, 10,
    //                      15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 
    //                      30, 50, 70, 100};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 1, 1.587527601073262957485E-19, },
            { 1.5, 3.283434196613645319487E-16, },
            { 2, 6.443731393112509126566E-14, },
            { 3, 8.314423588882995253264E-11, },
            { 5, 3.452137012639044249723E-7, },
            { 7, 4.440444820710751390893E-5, },
            { 10, 0.003466315816081875651285, },
            { 15, 0.1425714543331721724558, },
            { 16, 0.2311502761939163618226, },
            { 17, 0.3581020004882412903479, },
            { 18, 0.53629624004284596464, },
            { 19, 0.7837795557518691455946, },
            { 20, 1.126495581283288189202, },
            { 21, 1.602385846818592414939, },
            { 22, 2.267685311457105663315, },
            { 23, 3.206740938224275222958, },
            { 24, 4.547526615040360700149, },
            { 30, 44.71748658332183929564, },
            { 50, 2087090.271463798379566, },
            { 70, 1969352219538.635699462, },
            { 100, 2.656117576269062196828E+22 },

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
