/**
 * 2023.12.6
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.icbeta.ICBetaFactory;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満). <br>
 * 正則化不完全ベータ関数の計算として提供する.
 * 
 * <p>
 * 不完全ベータ関数は, パラメータを <i>a, b</i> &gt; 0 として, <br>
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
 * @author Matsuura Y.
 * @version 17.0
 */
public interface IncompleteBetaFunction {

    /**
     * このインスタンスが扱うパラメータ <i>a</i> の値を返す.
     *
     * @return a
     */
    public abstract double a();

    /**
     * このインスタンスが扱うパラメータ <i>b</i> の値を返す.
     *
     * @return b
     */
    public abstract double b();

    /**
     * 正則化不完全ベータ関数を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 or <i>x</i> &gt; 1 &rarr; NaN</li>
     * </ul>
     *
     * @param x x,引数
     * @return I(a, b, x)
     */
    public abstract double ribeta(double x);

    /**
     * パラメータを反転した正則化不完全ベータ関数
     * <i>I</i>(<i>b</i>, <i>a</i>, <i>x</i>)
     * を返す.
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
     * @param x x,引数
     * @return I(b, a, x) = 1 - I(a, b, 1 - x)
     */
    public abstract double ribetaR(double x);

    /**
     * 正則化不完全ベータ関数のオッズ
     * <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>)
     * /
     * (1 - <i>I</i>(<i>a</i>, <i>b</i>, <i>x</i>))
     * を返す. <br>
     * ただし, 引数として <i>x</i> ではなく
     * <i>x</i> のオッズ
     * <i>o</i><sub><i>x</i></sub> =
     * <i>x</i> / (1 - <i>x</i>)
     * を与える.
     * 
     * <ul>
     * <li><i>o</i><sub><i>x</i></sub> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>o</i><sub><i>x</i></sub> &asymp; +&infin; &rarr; +&infin;.
     * </li>
     * </ul>
     *
     * @param oddsX x のオッズ x / (1 - x)
     * @return I(a, b, x) / (1 - I(a, b, x)) =
     *             I(a, b, o<sub>x</sub> / (1 + o<sub>x</sub>))
     *             / I(b, a, 1 / (1 + o<sub>x</sub>))
     */
    public abstract double ribetaOdds(double oddsX);

    /**
     * 指定したパラメータの不完全ベータ関数計算インスタンスを返す.
     * 
     * <p>
     * 扱えるパラメータ <i>a</i>, <i>b</i> は,
     * {@code 1.0E-2 <= (a,b) <= 1.0E+14} である. <br>
     * 大きなパラメータ a, b に対しては, 精度が低下する.
     * </p>
     *
     * @param a パラメータ a
     * @param b パラメータ b
     * @return パラメータ a, bの不完全ベータ関数計算インスタンス
     * @throws IllegalArgumentException パラメータが仕様の範囲外の場合
     */
    public static IncompleteBetaFunction instanceOf(double a, double b) {
        return ICBetaFactory.instanceOf(a, b);
    }
}
