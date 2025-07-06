/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.5
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.icgamma.ICGammaFactory;

/**
 * 不完全ガンマ関数の計算(およそ倍精度未満). <br>
 * 正則化不完全ガンマ関数の計算として提供する.
 * 
 * <p>
 * 不完全ガンマ関数は, パラメータを <i>a</i> &gt; 0 として, <br>
 * 
 * &gamma;(<i>a</i>, <i>x</i>) =
 * &int;<sub>0</sub><sup><i>x</i></sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i>, <br>
 * 
 * &Gamma;(<i>a</i>, <i>x</i>) =
 * &int;<sub><i>x</i></sub><sup>&infin;</sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i> <br>
 * 
 * と表される
 * (0 &le; <i>x</i>). <br>
 * &gamma;(<i>a</i>, <i>x</i>), &Gamma;(<i>a</i>, <i>x</i>)
 * をそれぞれ第1種, 第2種不完全ガンマ関数という.
 * </p>
 * 
 * <p>
 * 第1種, 第2種正則化不完全ガンマ関数
 * <i>P</i>(<i>a</i>, <i>x</i>), <i>Q</i>(<i>a</i>, <i>x</i>)
 * は, <br>
 * 
 * <i>P</i>(<i>a</i>, <i>x</i>) =
 * &gamma;(<i>a</i>, <i>x</i>) / &Gamma;(<i>a</i>), <br>
 * 
 * <i>Q</i>(<i>a</i>, <i>x</i>) =
 * &Gamma;(<i>a</i>, <i>x</i>) / &Gamma;(<i>a</i>) <br>
 * 
 * と定義される. <br>
 * &Gamma;(<i>a</i>) はガンマ関数である. <br>
 * &Gamma;(<i>a</i>) =
 * &int;<sub>0</sub><sup>&infin;</sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i>
 * </p>
 * 
 * <p>
 * サポートされているパラメータ <i>a</i> は以下である. <br>
 * {@code 1E-2 <= a <= 1E+14}
 * </p>
 * 
 * @implSpec
 *               このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 *               モジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 * @see <a href="https://en.wikipedia.org/wiki/Incomplete_gamma_function"
 *          target= "_brank">
 *          Wikipedia: Incomplete gamma function</a>
 */
public interface IncompleteGammaFunction {

    /**
     * パラメータ <i>a</i> の下限を表す定数.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     * 
     */
    @Deprecated
    public static final double LOWER_LIMIT_OF_PARAMETER_A = 1E-2;

    /**
     * パラメータ <i>a</i> の上限を表す定数.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final double UPPER_LIMIT_OF_PARAMETER_A = 1E28;

    /**
     * このインスタンスが扱うパラメータ <i>a</i> の値を返す.
     *
     * @return パラメータ <i>a</i>
     */
    public abstract double a();

    /**
     * 第1種正則化不完全ガンマ関数
     * <i>P</i>(<i>a</i>, <i>x</i>) =
     * &gamma;(<i>a</i>, <i>x</i>) / &Gamma;(<i>a</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 1</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>P</i>(<i>a</i>,<i>x</i>)
     */
    public abstract double rigammaP(double x);

    /**
     * 第2種正則化不完全ガンマ関数
     * <i>Q</i>(<i>a</i>, <i>x</i>) =
     * &Gamma;(<i>a</i>, <i>x</i>) / &Gamma;(<i>a</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>Q</i>(<i>a</i>, <i>x</i>)
     */
    public abstract double rigammaQ(double x);

    /**
     * 正則化不完全ガンマ関数のオッズ
     * <i>P</i>(<i>a</i>, <i>x</i>) / <i>Q</i>(<i>a</i>, <i>x</i>) =
     * &gamma;(<i>a</i>, <i>x</i>) / &Gamma;(<i>a</i>, <i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>P</i>(<i>a</i>, <i>x</i>) / <i>Q</i>(<i>a</i>, <i>x</i>)
     */
    public abstract double rigammaOdds(double x);

    /**
     * 指定したパラメータ <i>a</i> がサポートされているかを判定する.
     * 
     * @param a パラメータ <i>a</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(double a) {
        return LOWER_LIMIT_OF_PARAMETER_A <= a
                && a <= UPPER_LIMIT_OF_PARAMETER_A;
    }

    /**
     * 指定したパラメータの不完全ガンマ関数計算インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(double)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param a パラメータ <i>a</i>
     * @return パラメータ <i>a</i> の不完全ガンマ関数計算インスタンス
     * @throws IllegalArgumentException パラメータがサポート外の場合
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        return ICGammaFactory.instanceOf(a);
    }
}
