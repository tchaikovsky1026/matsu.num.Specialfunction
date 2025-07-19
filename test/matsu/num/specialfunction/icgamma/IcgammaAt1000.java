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
 * IncompleteGammaFunction(a = 1000) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt1000 {

    private static final double paramA = 1000d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 1000;
    //        numeric xs[] = {800, 900, 
    //                        950, 955, 960, 965, 970, 975, 980, 985, 990, 995,
    //                        1000, 1005, 1010, 1015, 1020, 1025, 1030, 1035, 1040, 1045,
    //                        1050, 1100, 1200};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 800, 5.501419776209493759363E-12 },
            { 900, 5.502048245914313051857E-4 },
            { 950, 0.05826229881085093439336 },
            { 955, 0.08193927010922164764607 },
            { 960, 0.1132842669705266095962 },
            { 965, 0.1542374681668764579756 },
            { 970, 0.2071795169639633317312 },
            { 975, 0.2750728821229903714953 },
            { 980, 0.3616632619791457846651 },
            { 985, 0.4717642682754229400194 },
            { 990, 0.6116590731904611322569 },
            { 995, 0.78966894441244916765 },
            { 1000, 1.016963649295814919211 },
            { 1005, 1.308727388318930500521 },
            { 1010, 1.685854011918843090383 },
            { 1015, 2.177439529699910722668 },
            { 1020, 2.824489356615102421548 },
            { 1025, 3.685497327553960544253 },
            { 1030, 4.844942007611093652431 },
            { 1035, 6.42638300384742402422 },
            { 1040, 8.612897135618761503604 },
            { 1045, 11.67936883249292412218 },
            { 1050, 16.04416324362647741875 },
            { 1100, 942.9989127871078590828 },
            { 1200, 776300712.8255635213102 },

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
