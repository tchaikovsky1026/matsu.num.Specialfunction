/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.16
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.err.ErrorFunctionCalculation;
import matsu.num.specialfunction.err.ErrorFunctionImaginaryCalculation;

/**
 * 誤差関数 (Error function) の計算
 * (おおよそ倍精度).
 * 
 * <p>
 * 誤差関数は次式で定義される1変数関数である. <br>
 * erf(<i>x</i>) = (2/&radic;<i>&pi;</i>)
 * &int;<sub>0</sub><sup><i>x</i></sup>
 * exp(-<i>t</i><sup>2</sup>) d<i>t</i>
 * </p>
 * 
 * <ul>
 * <li>erf(-<i>x</i>) = -erf(<i>x</i>)</li>
 * <li>erf(0) = 0</li>
 * <li>erf(&infin;) = 1</li>
 * <li>erf(-&infin;) = -1</li>
 * </ul>
 * 
 * <p>
 * このクラスでは虚数誤差関数
 * erfi(<i>x</i>) = (2/&radic;<i>&pi;</i>)
 * &int;<sub>0</sub><sup><i>x</i></sup>
 * exp(<i>t</i><sup>2</sup>) d<i>t</i>
 * に関する計算も提供する.
 * </p>
 * 
 * <ul>
 * <li>erfi(-<i>x</i>) = -erfi(<i>x</i>)</li>
 * <li>erfi(0) = 0</li>
 * <li>erfi(&infin;) = &infin;</li>
 * <li>erfi(-&infin;) = -&infin;</li>
 * </ul>
 *
 * @author Matsuura Y.
 * @version 19.6
 * @see <a href="https://en.wikipedia.org/wiki/Error_function" target=
 *          "_brank">
 *          Wikipedia: Error function</a>
 */
public final class ErrorFuction {

    private static final ErrorFunctionCalculation ERR_FUNC =
            ErrorFunctionCalculation.createInstance();
    private static final ErrorFunctionImaginaryCalculation ERFI_CALC =
            ErrorFunctionImaginaryCalculation.createInstance();

    private ErrorFuction() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられた <i>x</i> に対する
     * erf(<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &asymp; &infin; &rarr; 1</li>
     * <li><i>x</i> &asymp; -&infin; &rarr; -1</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return erf(<i>x</i>)
     */
    public static double erf(double x) {
        return ERR_FUNC.erf(x);
    }

    /**
     * 与えられた <i>x</i> に対する相補誤差関数
     * erfc(<i>x</i>) = 1 - erf(<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &asymp; &infin; &rarr; 0</li>
     * <li><i>x</i> &asymp; -&infin; &rarr; 2</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return erfc(<i>x</i>)
     */
    public static double erfc(double x) {
        return ERR_FUNC.erfc(x);
    }

    /**
     * 与えられた <i>x</i> に対するスケーリング相補誤差関数
     * erfcx(<i>x</i>) = exp(<i>x</i><sup>2</sup>) erfc(<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &asymp; &infin; &rarr; 0</li>
     * <li><i>x</i> &asymp; -&infin; &rarr; &infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return erfcx(<i>x</i>)
     */
    public static double erfcx(double x) {
        return ERR_FUNC.erfcx(x);
    }

    /**
     * 与えられた <i>x</i> に対する
     * erfi(<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &asymp; &infin; &rarr; &infin;</li>
     * <li><i>x</i> &asymp; -&infin; &rarr; -&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return erfi(<i>x</i>)
     */
    public static double erfi(double x) {
        return ERFI_CALC.erfi(x);
    }

    /**
     * 与えられた <i>x</i> に対するスケーリング虚数誤差関数
     * erfix(<i>x</i>) = exp(-<i>x</i><sup>2</sup>) erfi(<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &asymp; &pm;&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return erfix(<i>x</i>)
     */
    public static double erfix(double x) {
        return ERFI_CALC.erfix(x);
    }
}
