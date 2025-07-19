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
 * IncompleteGammaFunction(a = 1) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt1 {

    private static final double paramA = 1d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 1;
    //        numeric xs[] = {0.01,0.015,0.02,0.03,0.05,0.07,
    //                        0.1,0.15,0.2,0.3,0.5,0.7,
    //                        1,1.5,2,3,5,7,10};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 0.01, 0.01005016708416805754217 },
            { 0.015, 0.01511306461571897927684 },
            { 0.02, 0.02020134002675581016014 },
            { 0.03, 0.03045453395351685561244 },
            { 0.05, 0.05127109637602403969752 },
            { 0.07, 0.0725081812542164790531 },
            { 0.1, 0.1051709180756476248117 },
            { 0.15, 0.1618342427282831226166 },
            { 0.2, 0.2214027581601698339211 },
            { 0.3, 0.3498588075760031039838 },
            { 0.5, 0.6487212707001281468487 },
            { 0.7, 1.013752707470476521625 },
            { 1, 1.71828182845904523536 },
            { 1.5, 3.481689070338064822602 },
            { 2, 6.38905609893065022723 },
            { 3, 19.08553692318766774093 },
            { 5, 147.4131591025766034211 },
            { 7, 1095.633158428458599264 },
            { 10, 22025.46579480671651696 },

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
