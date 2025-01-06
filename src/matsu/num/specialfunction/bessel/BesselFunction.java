/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.23
 */
package matsu.num.specialfunction.bessel;

import matsu.num.specialfunction.bessel.bessel.BesselFunctionFactory;

/**
 * <p>
 * Bessel関数
 * (<i>J<sub>n</sub></i>(<i>x</i>), <i>Y<sub>n</sub></i>(<i>x</i>))
 * の計算
 * (おおよそ倍精度). <br>
 * 0 &le; <i>x</i>
 * を扱う.
 * </p>
 * 
 * <p>
 * サポートされている次数は
 * 0 &le; <i>n</i> &le; 100
 * である.
 * </p>
 * 
 * <p>
 * <i>
 * <u>
 * このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 * 外部で実装することは不可.
 * </u>
 * </i>
 * </p>
 *
 * @author Matsuura Y.
 * @version 20.0
 * @see <a href="https://en.wikipedia.org/wiki/Bessel_function" target=
 *          "_brank">
 *          Wikipedia: Bessel function</a>
 */
public sealed interface BesselFunction permits matsu.num.specialfunction.bessel.bessel.BesselFunction {

    /**
     * 次数 <i>n</i> の下限を表す定数.
     */
    public static final int LOWER_LIMIT_OF_ORDER = 0;

    /**
     * 次数 <i>n</i> の上限を表す定数.
     */
    public static final int UPPER_LIMIT_OF_ORDER = 100;

    /**
     * このインスタンスの扱うBessel関数の次数 (<i>n</i>) を返す.
     *
     * @return 次数 <i>n</i>
     */
    public abstract int order();

    /**
     * 第1種Bessel関数 <i>J<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>J</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double besselJ(double x);

    /**
     * 第2種Bessel関数 <i>Y<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; 0 &rarr; -&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>Y</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double besselY(double x);

    /**
     * <p>
     * 指定したパラメータ (次数) がサポートされているかを判定する.
     * </p>
     * 
     * @param order 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int order) {
        return BesselFunctionFactory.acceptsParameter(order);
    }

    /**
     * <p>
     * 指定した次数のBessel関数計算インスタンスを返す.
     * </p>
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param order <i>n</i>, 次数
     * @return <i>n</i> 次のBessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static BesselFunction instanceOf(int order) {
        return BesselFunctionFactory.instanceOf(order);
    }
}
