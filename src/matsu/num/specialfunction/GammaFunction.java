/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.5
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.gamma.DigammaCalculation;
import matsu.num.specialfunction.gamma.GammaCalculation;
import matsu.num.specialfunction.gamma.LGammaCalculation;
import matsu.num.specialfunction.gamma.TrigammaCalculation;

/**
 * ガンマ関数(Gamma function)の計算(おおよそ倍精度).
 * 
 * <p>
 * ガンマ関数は次式で定義される1変数関数である. <br>
 * &Gamma;(<i>x</i>) =
 * &int;<sub>0</sub><sup>&infin;</sup>
 * <i>t</i><sup><i>x</i>-1</sup> exp(-<i>t</i>) d<i>t</i> <br>
 * 0 &le; <i>x</i>
 * を扱う.
 * </p>
 *
 * @author Matsuura Y.
 * @version 19.2
 * @see <a href="https://en.wikipedia.org/wiki/Gamma_function" target= "_brank">
 *          Wikipedia: Gamma function</a>
 * @see <a href="https://en.wikipedia.org/wiki/Stirling%27s_approximation"
 *          target= "_brank">
 *          Wikipedia: Stirling&#x27;s approximation</a>
 * @see <a href="https://en.wikipedia.org/wiki/Polygamma_function" target=
 *          "_brank">
 *          Wikipedia: Polygamma function</a>
 */
public final class GammaFunction {

    private static final LGammaCalculation LGAMMA = new LGammaCalculation();
    private static final GammaCalculation GAMMA = new GammaCalculation(LGAMMA);
    private static final DigammaCalculation DIGAMMA = new DigammaCalculation();
    private static final TrigammaCalculation TRIGAMMA = new TrigammaCalculation();

    /**
     * Euler-Mascheroni 定数: <br>
     * {@code  0.5772156649015329}
     */
    public static final double EULER_MASCHERONI_GAMMA = 0.5772156649015329;

    private GammaFunction() {
        throw new AssertionError();
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return log<sub>e</sub>&Gamma;(<i>x</i>)
     */
    public static double lgamma(double x) {
        return LGAMMA.lgamma(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(1 + <i>x</i>) の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; -1 &rarr; NaN</li>
     * <li><i>x</i> &asymp; -1 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return log<sub>e</sub>&Gamma;(1 + <i>x</i>)
     */
    public static double lgamma1p(double x) {
        return LGAMMA.lgamma1p(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStirling近似 <br>
     * <i>S</i>(<i>x</i>) =
     * (<i>x</i> - 1/2) log<sub>e</sub>(<i>x</i>)
     * - <i>x</i>
     * + (1/2) log(2&pi;) <br>
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>S</i>(<i>x</i>)
     */
    public static double lgammaStirling(double x) {
        return LGAMMA.lgammaStirling(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStirling近似残差: <br>
     * log<sub>e</sub>&Gamma;(<i>x</i>) - <i>S</i>(<i>x</i>) <br>
     * を返す. <br>
     * (<i>S</i>(<i>x</i>) は log<sub>e</sub>&Gamma;(<i>x</i>) の Stirling近似)
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return log<sub>e</sub>&Gamma;(<i>x</i>) - <i>S</i>(<i>x</i>)
     * @see #lgammaStirling(double)
     */
    public static double lgammaStirlingResidual(double x) {
        return LGAMMA.lgammaStirlingResidual(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) の差分
     * 
     * [
     * log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i>)
     * ]
     * 
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 または <i>x</i> + <i>y</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 かつ
     * <i>x</i> + <i>y</i> &asymp; 0 &rarr; NaN</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @param y <i>y</i>, 差分
     * @return log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>) -
     *             log<sub>e</sub>&Gamma;(<i>x</i>)
     */
    public static double lgammaDiff(double x, double y) {
        return LGAMMA.lgammaDiff(x, y);
    }

    /**
     * ベータ関数の自然対数
     * 
     * log<sub>e</sub>B(<i>x</i>, <i>y</i>) =
     * [
     * log<sub>e</sub>&Gamma;(<i>x</i>)
     * + log<sub>e</sub>&Gamma;(<i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>)
     * ]
     * 
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 または <i>y</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; +&infin; または <i>y</i> &asymp; +&infin;
     * &rarr; NaN
     * </li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @param y <i>y</i>, 引数
     * @return log<sub>e</sub>B(<i>x</i>, <i>y</i>)
     */
    public static double lbeta(double x, double y) {
        return LGAMMA.lbeta(x, y);
    }

    /**
     * &Gamma;(<i>x</i>) の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return &Gamma;(<i>x</i>)
     */
    public static double gamma(double x) {
        return GAMMA.gamma(x);
    }

    /**
     * 整数引数の&Gamma;(<i>n</i>) の値を返す.
     * 
     * <ul>
     * <li><i>n</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>n</i> = 0 &rarr; +&infin;</li>
     * </ul>
     *
     * @param n <i>n</i>, 整数引数
     * @return &Gamma;(<i>n</i>)
     */
    public static double gamma(int n) {
        return GAMMA.gamma(n);
    }

    /**
     * ディガンマ関数
     * 
     * <i>&psi;</i>(<i>x</i>) =
     * (d/d<i>x</i>) log<sub>e</sub>&Gamma;(<i>x</i>)
     * 
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; -&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>&psi;</i>(<i>x</i>)
     */
    public static double digamma(double x) {
        return DIGAMMA.digamma(x);
    }

    /**
     * トリガンマ関数
     * 
     * <i>&psi;</i>'(<i>x</i>) =
     * (d/d<i>x</i>) <i>&psi;</i>(<i>x</i>)
     * 
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>&psi;</i>'(<i>x</i>)
     */
    public static double trigamma(double x) {
        return TRIGAMMA.trigamma(x);
    }

}
