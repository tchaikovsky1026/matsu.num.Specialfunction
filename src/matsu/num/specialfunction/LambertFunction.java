/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.18
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.lambert.LambertCalculationMinus1Branch;
import matsu.num.specialfunction.lambert.LambertCalculationPrincipalBranch;

/**
 * Lambertの<i>W</i>-関数の計算(おおよそ倍精度).
 * 
 * <p>
 * Lambert関数は初等関数の逆関数であり,
 * 次式により定義される (e: Napier数). <br>
 * <i>W</i>(<i>z</i>) =
 * arg<sub><i>w</i></sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
 * </p>
 *
 * @author Matsuura Y.
 * @version 18.1
 */
public final class LambertFunction {

    private static final LambertCalculationMinus1Branch LAMBERT_MINUS1 =
            new LambertCalculationMinus1Branch();
    private static final LambertCalculationPrincipalBranch LAMBERT_PRINCIPAL =
            new LambertCalculationPrincipalBranch();

    private LambertFunction() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * Lambert関数の主枝
     * <i>W</i><sub>0</sub>(<i>z</i>) =
     * arg<sub><i>w</i> &ge; -1</sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
     * の値を計算する.
     * 
     * <ul>
     * <li><i>z</i> &lt; -1/e &rarr; NaN</li>
     * <li><i>z</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param z <i>z</i>, 引数
     * @return <i>W</i><sub>0</sub>(<i>z</i>)
     */
    public static double wp(double z) {
        return LAMBERT_PRINCIPAL.wp(z);
    }

    /**
     * Lambert関数の分枝
     * <i>W</i><sub>-1</sub>(<i>z</i>) =
     * arg<sub><i>w</i> &le; -1</sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
     * の値を計算する.
     * 
     * <ul>
     * <li><i>z</i> &lt; -1/e または <i>z</i> &gt; 0 &rarr; NaN</li>
     * <li><i>z</i> &asymp; 0 &rarr; -&infin;</li>
     * </ul>
     *
     * @param z <i>z</i>, 引数
     * @return <i>W</i><sub>-1</sub>(<i>z</i>)
     */
    public static double wm(double z) {
        return LAMBERT_MINUS1.wm(z);
    }
}
