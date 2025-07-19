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
 * IncompleteGammaFunction(a = 10000) をテストするためのクラス. <br>
 * 継承して抽象メソッドを実装すればJUnit自動テストが動作する.
 * 
 * @author Matsuura Y.
 */
@Ignore
@RunWith(Theories.class)
abstract class IcgammaAt10000 {

    private static final double paramA = 10000d;

    /* 値の生成コード(https://keisan.casio.jp/calculator) */
    /* ------------------------------------ */
    //        a = 10000;
    //        numeric xs[] = {9500, 9600, 9700, 9800, 9900,
    //                      9920, 9940, 9960, 9980, 10000, 10020, 10040, 10060, 10080,
    //                      10100, 10200, 10300, 10400, 10500};
    //
    //        for(index = 0; index < kei_length(xs); index = index + 1){
    //            x = xs[index];
    //            g = gammap(a,x)/gammaq(a,x);
    //            println(x,g);
    //        }
    /* ------------------------------------ */

    @DataPoints
    public static double[][] dataPairs = {
            { 9500, 1.862454998668952688729E-7 },
            { 9600, 2.547096293727499757917E-5 },
            { 9700, 0.001235700656047898010169 },
            { 9800, 0.02271191976730140416869 },
            { 9900, 0.1885676793281493483966 },
            { 9920, 0.2693592206101196916321 },
            { 9940, 0.3792411052348384101982 },
            { 9960, 0.5281405807154739993246 },
            { 9980, 0.7300804150102332827297 },
            { 10000, 1.005333418207847330127 },
            { 10020, 1.383849839500994648626 },
            { 10040, 1.910804542421377909215 },
            { 10060, 2.655747701314629667442 },
            { 10080, 3.728004754079995622603 },
            { 10100, 5.303133462980170921378 },
            { 10200, 41.94182019997937856317 },
            { 10300, 679.0431626508141823275 },
            { 10400, 25719.05177027244610031 },
            { 10500, 2338702.996693583015964 },

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
