/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.16
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.sbessel.SBesselFunctionFactory;

/**
 * <p>
 * 球Bessel関数
 * (<i>j<sub>n</sub></i>(<i>x</i>), <i>y<sub>n</sub></i>(<i>x</i>))
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
 * @author Matsuura Y.
 * @version 18.7
 * @see <a href=
 *          "https://en.wikipedia.org/wiki/Bessel_function#Spherical_Bessel_functions:_jn,_yn"
 *          target=
 *          "_brank">
 *          Wikipedia: Bessel function#Spherical Bessel functions</a>
 */
public interface SphericalBesselFunction {
    /**
     * 次数 <i>n</i> の下限を表す定数.
     */
    public static final int LOWER_LIMIT_OF_ORDER = 0;

    /**
     * 次数 <i>n</i> の上限を表す定数.
     */
    public static final int UPPER_LIMIT_OF_ORDER = 100;

    /**
     * このインスタンスの扱う球Bessel関数の次数 (<i>n</i>) を返す.
     *
     * @return 次数 <i>n</i>
     */
    public abstract int order();

    /**
     * 第1種球Bessel関数 <i>j<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>j</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double sbesselJ(double x);

    /**
     * 第2種球Bessel関数 <i>y<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; 0 &rarr; -&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>y</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double sbesselY(double x);

    /**
     * <p>
     * 指定したパラメータ (次数) がサポートされているかを判定する.
     * </p>
     * 
     * @param order 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int order) {
        return SBesselFunctionFactory.acceptsParameter(order);
    }

    /**
     * <p>
     * 指定した次数の球Bessel関数計算インスタンスを返す.
     * </p>
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param order <i>n</i>, 次数
     * @return <i>n</i> 次の球Bessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static SphericalBesselFunction instanceOf(int order) {
        return SBesselFunctionFactory.instanceOf(order);
    }
}
