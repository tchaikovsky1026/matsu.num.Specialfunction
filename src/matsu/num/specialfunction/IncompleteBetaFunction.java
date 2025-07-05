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

import matsu.num.specialfunction.icbeta.ICBetaFactory;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満). <br>
 * 正則化不完全ベータ関数の計算として提供する.
 * 
 * <p>
 * 不完全ベータ関数は, パラメータを <i>a</i>, <i>b</i> &gt; 0 として, <br>
 * 
 * B(<i>a</i>, <i>b</i>, <i>x</i>) =
 * &int;<sub>0</sub><sup><i>x</i></sup>
 * <i>t</i><sup><i>a</i>-1</sup>
 * (1 - <i>t</i>)<sup><i>b</i>-1</sup> d<i>t</i>
 * <br>
 * 
 * と表される
 * (0 &le; <i>x</i> &le; 1).
 * </p>
 * 
 * <p>
 * 正則化不完全ベータ関数
 * <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>) は,
 * <br>
 * 
 * <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>) =
 * B(<i>a</i>, <i>b</i>, <i>x</i>) / B(<i>a</i>, <i>b</i>)
 * <br>
 * 
 * と定義される. <br>
 * B(<i>a</i>, <i>b</i>) はベータ関数である. <br>
 * B(<i>a</i>, <i>b</i>) =
 * &int;<sub>0</sub><sup>1</sup>
 * <i>t</i><sup><i>a</i>-1</sup>
 * (1 - <i>t</i>)<sup><i>b</i>-1</sup> d<i>t</i>
 * </p>
 * 
 * <p>
 * サポートされているパラメータ <i>a</i>, <i>b</i> は以下である. <br>
 * {@code 1E-2 <= a <= 1E+14} <br>
 * {@code 1E-2 <= b <= 1E+14}
 * </p>
 * 
 * @implSpec
 *               このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 *               モジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 * @see <a href=
 *          "https://en.wikipedia.org/wiki/Beta_function#Incomplete_beta_function"
 *          target= "_brank">
 *          Wikipedia: Beta function#Incomplete beta function</a>
 */
public interface IncompleteBetaFunction {

    /**
     * パラメータ <i>a</i>, <i>b</i> の下限を表す定数.
     */
    public static final double LOWER_LIMIT_OF_PARAMETER_AB = 1E-2;

    /**
     * パラメータ <i>a</i>, <i>b</i> の上限を表す定数.
     */
    public static final double UPPER_LIMIT_OF_PARAMETER_AB = 1E14;

    /**
     * このインスタンスが扱うパラメータ <i>a</i> の値を返す.
     *
     * @return パラメータ <i>a</i>
     */
    public abstract double a();

    /**
     * このインスタンスが扱うパラメータ <i>b</i> の値を返す.
     *
     * @return パラメータ <i>b</i>
     */
    public abstract double b();

    /**
     * 正則化不完全ベータ関数
     * <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 または <i>x</i> &gt; 1 &rarr; NaN</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>)
     */
    public abstract double ribeta(double x);

    /**
     * パラメータを反転した正則化不完全ベータ関数
     * <i>I</i>(<i>b</i>, <i>a</i>, <i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0, <i>x</i> &gt; 1 &rarr; NaN</li>
     * </ul>
     * 
     * <p>
     * <i>I</i>(<i>b</i>, <i>a</i>, <i>x</i>) =
     * 1 - <i>I</i>(<i>a</i>, <i>b</i>, 1 - <i>x</i>)
     * が成立する.
     * </p>
     *
     * @param x <i>x</i>, 引数
     * @return <i>I</i>(<i>b</i>, <i>a</i>, <i>x</i>) =
     *             1 - <i>I</i>(<i>a</i>, <i>b</i>, 1 - <i>x</i>)
     */
    public abstract double ribetaR(double x);

    /**
     * 正則化不完全ベータ関数のオッズ
     * <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>)
     * /
     * (1 - <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>))
     * の値を返す. <br>
     * ただし, 引数として <i>x</i> ではなく
     * <i>x</i> のオッズ
     * <i>o</i><sub><i>x</i></sub> =
     * <i>x</i> / (1 - <i>x</i>)
     * を与える.
     * 
     * <ul>
     * <li><i>o</i><sub><i>x</i></sub> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>o</i><sub><i>x</i></sub> &asymp; +&infin; &rarr; +&infin;
     * </li>
     * </ul>
     *
     * @param oddsX <i>o</i><sub><i>x</i></sub> =
     *            <i>x</i> / (1 - <i>x</i>), <i>x</i> のオッズ
     * @return <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>) /
     *             (1 - <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>))
     */
    public abstract double ribetaOdds(double oddsX);

    /**
     * <p>
     * 指定したパラメータ (<i>a</i>, <i>b</i>) がサポートされているかを判定する.
     * </p>
     * 
     * @param a パラメータ <i>a</i>
     * @param b パラメータ <i>b</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(double a, double b) {
        return ICBetaFactory.acceptsParameter(a, b);
    }

    /**
     * 指定したパラメータの不完全ベータ関数計算インスタンスを返す.
     *
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(double, double)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param a パラメータ <i>a</i>
     * @param b パラメータ <i>b</i>
     * @return パラメータ (<i>a</i>, <i>b</i>) の不完全ベータ関数計算インスタンス
     * @throws IllegalArgumentException パラメータがサポート外の場合
     */
    public static IncompleteBetaFunction instanceOf(double a, double b) {
        return ICBetaFactory.instanceOf(a, b);
    }
}
