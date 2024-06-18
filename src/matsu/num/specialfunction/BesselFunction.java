/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.17
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.bessel.BesselFunctionFactory;

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
 * @author Matsuura Y.
 * @version 18.0
 */
public interface BesselFunction {

    /**
     * このインスタンスの扱うBessel関数の次数 (<i>n</i>) を返す.
     *
     * @return 次数 <i>n</i>
     */
    public int order();

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
    public double besselJ(double x);

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
    public double besselY(double x);

    /**
     * 指定した次数のBessel関数計算インスタンスを返す.
     * 
     * <p>
     * サポートされている次数は以下である. <br>
     * {@code 0 <= order <= 100}
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
