/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.7
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.zeta.EMTypeRiemannZetaFunctionCalculation;

/**
 * Riemann ゼータ関数の計算 (およそ倍精度).
 * 
 * <p>
 * Riemann ゼータ関数は次式で定義される1変数関数である. <br>
 * <i>&zeta;</i>(<i>s</i>) =
 * &Sigma;<sub><i>n</i> &ge; 1</sub>
 * 1 / (<i>n</i><sup><i>s</i></sup>) <br>
 * <i>s</i> &gt; 1
 * を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class RiemannZetaFunction {

    private static final EMTypeRiemannZetaFunctionCalculation ZETA = new EMTypeRiemannZetaFunctionCalculation();

    private RiemannZetaFunction() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられた <i>s</i> に対する
     * <i>&zeta;</i>(<i>s</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>s</i> &lt; 1 &rarr; NaN</li>
     * <li><i>s</i> &asymp; 1 &rarr; +&infin;</li>
     * <li><i>s</i> &asymp; &infin; &rarr; 1</li>
     * </ul>
     *
     * @param s <i>s</i>, 引数
     * @return <i>&zeta;</i>(<i>s</i>)
     */
    public static double zeta(double s) {
        return ZETA.zeta(s);
    }

    /**
     * 与えられた <i>s</i> に対する
     * (<i>&zeta;</i>(<i>s</i>) - 1)
     * の値を返す.
     * 
     * <ul>
     * <li><i>s</i> &lt; 1 &rarr; NaN</li>
     * <li><i>s</i> &asymp; 1 &rarr; +&infin;</li>
     * <li><i>s</i> &asymp; &infin; &rarr; 0</li>
     * </ul>
     *
     * @param s <i>s</i>, 引数
     * @return <i>&zeta;</i>(<i>s</i>) - 1
     */
    public static double zetam1(double s) {
        return ZETA.zetam1(s);
    }
}
