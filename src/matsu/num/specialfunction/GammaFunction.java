/**
 * 2023.12.6
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
 * <i>t</i><sup><i>x</i>-1</sup> exp(-<i>t</i>) d<i>t</i>
 * </p>
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public final class GammaFunction {

    /**
     * Euler-Mascheroni 定数: <br>
     * {@code  0.5772156649015329}
     */
    public static final double EULER_MASCHERONI_GAMMA;

    static {
        EULER_MASCHERONI_GAMMA = 0.5772156649015329;
    }

    private GammaFunction() {
        throw new AssertionError();
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return log<sub>e</sub>&Gamma;(x)
     */
    public static double lgamma(double x) {
        return LGammaCalculation.instance().lgamma(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(1 + <i>x</i>) を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; -1 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; -1 &rarr; +&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return log<sub>e</sub>&Gamma;(x)
     */
    public static double lgamma1p(double x) {
        return LGammaCalculation.instance().lgamma1p(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStiring近似残差
     * 
     * [
     * log<sub>e</sub>&Gamma;(<i>x</i>)
     * - (<i>x</i> - 1/2) log<sub>e</sub>(<i>x</i>)
     * + <i>x</i>
     * - (1/2) log(2&pi;)
     * ]
     * 
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return log<sub>e</sub>&Gamma;(x) のStiring近似残差
     */
    public static double lgammaStirlingResidual(double x) {
        return LGammaCalculation.instance().lgammaStirlingResidual(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) の差分
     * 
     * [
     * log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i>)
     * ]
     * 
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 or <i>x</i>+<i>y</i> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 かつ
     * <i>x</i>+ <i>y</i> &asymp; 0 &rarr; NaN.</li>
     * </ul>
     *
     * @param x x, 引数
     * @param y y, 差分
     * @return log<sub>e</sub>&Gamma;(x + y)-log<sub>e</sub>&Gamma;(x)
     */
    public static double lgammaDiff(double x, double y) {
        return LGammaCalculation.instance().lgammaDiff(x, y);
    }

    /**
     * ベータ関数の自然対数
     * 
     * log<sub>e</sub>B(<i>x</i>,<i>y</i>) =
     * log<sub>e</sub>&Gamma;(<i>x</i>)
     * + log<sub>e</sub>&Gamma;(<i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>)
     * 
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 or <i>y</i> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>x</i> &asymp; +&infin; or <i>y</i> &asymp; +&infin; &rarr; NaN.
     * </li>
     * </ul>
     *
     * @param x x, 引数
     * @param y y, 引数
     * @return log<sub>e</sub>B(x,y)
     */
    public static double lbeta(double x, double y) {
        return LGammaCalculation.instance().lbeta(x, y);
    }

    /**
     * &Gamma;(<i>x</i>) を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return &Gamma;(x)
     */
    public static double gamma(double x) {
        return GammaCalculation.instance().gamma(x);
    }

    /**
     * 整数引数の&Gamma;(<i>n</i>) を返す.
     * 
     * <ul>
     * <li><i>n</i> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>n</i> = 0 &rarr; +&infin;.</li>
     * </ul>
     *
     * @param n n, 整数引数
     * @return &Gamma;(n)
     */
    public static double gamma(int n) {
        return GammaCalculation.instance().gamma(n);
    }

    /**
     * ディガンマ関数
     * 
     * <i>&psi;</i>(<i>x</i>) =
     * (d/d<i>x</i>) log<sub>e</sub>&Gamma;(<i>x</i>)
     * 
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 &rarr; -&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return &psi;(x)
     */
    public static double digamma(double x) {
        return DigammaCalculation.instance().digamma(x);
    }

    /**
     * トリガンマ関数
     * 
     * <i>&psi;</i>'(<i>x</i>) =
     * (d/d<i>x</i>) <i>&psi;</i>(<i>x</i>)
     * 
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.</li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return &psi;'(x)
     */
    public static double trigamma(double x) {
        return TrigammaCalculation.instance().trigamma(x);
    }

}
