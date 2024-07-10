/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.10
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.modbessel.ModifiedBesselFunctionFactory;

/**
 * <p>
 * 変形Bessel関数
 * (<i>I<sub>n</sub></i>(<i>x</i>), <i>K<sub>n</sub></i>(<i>x</i>))
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
 * @version 18.2
 * @see <a href=
 *          "https://en.wikipedia.org/wiki/Bessel_function#Modified_Bessel_functions"
 *          target= "_brank">
 *          Wikipedia: Bessel function#Modified Bessel functions</a>
 */
public interface ModifiedBesselFunction {

    /**
     * 次数 <i>n</i> の下限を表す定数.
     */
    public static final int LOWER_LIMIT_OF_ORDER = 0;

    /**
     * 次数 <i>n</i> の上限を表す定数.
     */
    public static final int UPPER_LIMIT_OF_ORDER = 100;

    /**
     * このインスタンスの扱う変形Bessel関数の次数 (<i>n</i>) を返す.
     *
     * @return 次数 <i>n</i>
     */
    public abstract int order();

    /**
     * 第1種変形Bessel関数 <i>I<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; +&infin;</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>I</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double besselI(double x);

    /**
     * 第2種変形Bessel関数 <i>K<sub>n</sub></i>(<i>x</i>) の値を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN
     * </li>
     * <li><i>x</i> &asymp; 0 &rarr; +&infin;</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0</li>
     * </ul>
     *
     * @param x <i>x</i>, 引数
     * @return <i>K</i><sub><i>n</i></sub>(<i>x</i>)
     */
    public abstract double besselK(double x);

    /**
     * <p>
     * 指定したパラメータ (次数) がサポートされているかを判定する.
     * </p>
     * 
     * @param order 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int order) {
        return ModifiedBesselFunctionFactory.acceptsParameter(order);
    }

    /**
     * <p>
     * 指定した次数の変形Bessel関数計算インスタンスを返す.
     * </p>
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     *
     * @param order <i>n</i>, 次数
     * @return <i>n</i> 次の変形Bessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ModifiedBesselFunction instanceOf(int order) {
        return ModifiedBesselFunctionFactory.instanceOf(order);
    }
}
