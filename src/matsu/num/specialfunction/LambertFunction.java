/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.14
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.lambert.LambertCalculationMinus1Branch;
import matsu.num.specialfunction.lambert.LambertCalculationPrincipalBranch;

/**
 * Lambertの<i>W</i>-関数の計算(おおよそ倍精度).
 * 
 * <p>
 * Lambert関数は初等関数の逆関数であり,
 * <i>W</i>(<i>z</i>) =
 * arg<sub><i>w</i></sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
 * により定義される (e: Napier数). <br>
 * 
 * <i>w</i> e<sup><i>w</i></sup> = <i>z</i> を満たす実数 <i>w</i> が存在するのは,
 * <i>z</i> &ge; -1/e の場合である. <br>
 * このうち -1/e &lt; <i>z</i> &lt; 0 の場合は,
 * <i>w</i> e<sup><i>w</i></sup> = <i>z</i> を満たす相異なる実数 <i>w</i> が2個存在する. <br>
 * これを区別するため, <br>
 * <i>W</i><sub>0</sub>(<i>z</i>) =
 * arg<sub><i>w</i> &ge; -1</sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
 * <br>
 * <i>W</i><sub>-1</sub>(<i>z</i>) =
 * arg<sub><i>w</i> &le; -1</sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
 * <br>
 * とする. <br>
 * <i>W</i><sub>0</sub> を主枝, <i>W</i><sub>-1</sub>を分枝という. <br>
 * 
 * <i>W</i><sub>0</sub>(<i>z</i>) は <i>z</i> &ge; -1/e で,
 * <i>W</i><sub>-1</sub>(<i>z</i>) は -1/e &le; <i>z</i> &lt; 0
 * で定義される.
 * </p>
 *
 * @author Matsuura Y.
 * @version 19.3
 * @see <a href="https://en.wikipedia.org/wiki/Lambert_W_function" target=
 *          "_brank">
 *          Wikipedia: Lambert <i>W</i> function</a>
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
     * 与えられた <i>z</i> に対するLambert関数の主枝
     * <i>W</i><sub>0</sub>(<i>z</i>) =
     * arg<sub><i>w</i> &ge; -1</sub> [<i>w</i> e<sup><i>w</i></sup> = <i>z</i>]
     * の値を計算する.
     * 
     * <ul>
     * <li><i>z</i> &lt; -1/e &rarr; NaN</li>
     * <li><i>z</i> &asymp; &infin; &rarr; &infin;</li>
     * </ul>
     *
     * @param z <i>z</i>, 引数
     * @return <i>W</i><sub>0</sub>(<i>z</i>)
     */
    public static double wp(double z) {
        return LAMBERT_PRINCIPAL.wp(z);
    }

    /**
     * 与えられた <i>z</i> に対するLambert関数の分枝
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
