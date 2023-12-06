/*
 * 2023.12.6
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
 * &gamma;(<i>a</i>,<i>x</i>) =
 * &int;<sub>0</sub><sup><i>x</i></sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i>, <br>
 * 
 * &Gamma;(<i>a</i>,<i>x</i>) =
 * &int;<sub><i>x</i></sub><sup>&infin;</sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i> <br>
 * 
 * と表される
 * (0 &le; <i>x</i>). <br>
 * &gamma;(<i>a</i>,<i>x</i>), &Gamma;(<i>a</i>,<i>x</i>)
 * をそれぞれ第1種, 第2種不完全ガンマ関数という.
 * </p>
 * 
 * <p>
 * 第1種, 第2種正則化不完全ガンマ関数
 * <i>P</i>(<i>a</i>,<i>x</i>), <i>Q</i>(<i>a</i>,<i>x</i>)
 * は, <br>
 * 
 * <i>P</i>(<i>a</i>,<i>x</i>) =
 * &gamma;(<i>a</i>,<i>x</i>) / &Gamma;(<i>a</i>), <br>
 * 
 * <i>Q</i>(<i>a</i>,<i>x</i>) =
 * &Gamma;(<i>a</i>,<i>x</i>) / &Gamma;(<i>a</i>) <br>
 * 
 * と定義される. <br>
 * &Gamma;(<i>a</i>)はガンマ関数である. <br>
 * &Gamma;(<i>a</i>) =
 * &int;<sub>0</sub><sup>&infin;</sup>
 * <i>t</i><sup><i>a</i>-1</sup> exp(-<i>t</i>) d<i>t</i>
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
public interface IncompleteGammaFunction {

    /**
     * このインスタンスが扱うパラメータ <i>a</i> の値を返す.
     *
     * @return a
     */
    public abstract double a();

    /**
     * 第1種正則化不完全ガンマ関数
     * <i>P</i>(<i>a</i>,<i>x</i>) =
     * &gamma;(<i>a</i>,<i>x</i>) / &Gamma;(<i>a</i>)
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 1</li>
     * </ul>
     *
     * @param x x, 引数
     * @return P(a,x)
     */
    public abstract double rigammaP(double x);

    /**
     * 第2種正則化不完全ガンマ関数
     * <i>Q</i>(<i>a</i>,<i>x</i>) =
     * &Gamma;(<i>a</i>,<i>x</i>) / &Gamma;(<i>a</i>)
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x x, 引数
     * @return Q(a,x)
     */
    public abstract double rigammaQ(double x);

    /**
     * 正則化不完全ガンマ関数のオッズ
     * <i>P</i>(<i>a</i>,<i>x</i>) / <i>Q</i>(<i>a</i>,<i>x</i>) =
     * &gamma;(<i>a</i>,<i>x</i>) / &Gamma;(<i>a</i>,<i>x</i>)
     * を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x x, 引数
     * @return P(a,x) / Q(a,x)
     */
    public abstract double rigammaOdds(double x);

    /**
     * 指定したパラメータの不完全ガンマ関数計算インスタンスを返す.
     * 
     * <p>
     * 扱える <i>a</i> は, {@code 1.0E-2 <= a <= 1.0E+28} である. <br>
     * 大きなパラメータaに対しては, 精度が低下する.
     * </p>
     *
     * @param a パラメータ a
     * @return パラメータ a の不完全ガンマ関数計算インスタンス
     * @throws IllegalArgumentException パラメータが仕様の範囲外の場合
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        return ICGammaFactory.instanceOf(a);
    }
}
