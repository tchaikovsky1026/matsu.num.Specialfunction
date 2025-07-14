/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.11
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.hermite.HermiteFunctionImpl;

/**
 * Hermite 関数 (多項式) の計算
 * (およそ倍精度).
 * 
 * <p>
 * Hermite 多項式
 * <i>H</i><sub><i>n</i></sub> (<i>x</i>)
 * は, 非負整数の次数 <i>n</i> をパラメータに持つ 1 変数関数
 * である. <br>
 * 通常, <i>x</i> &ge; 0 で扱い, このインターフェースでもその定義域を採用する.
 * </p>
 * 
 * <p>
 * Hermite 多項式は次の式により定義される. <br>
 * <i>H</i><sub><i>n</i></sub> (<i>x</i>)
 * = (-1)<sup><i>n</i></sup> exp(<i>x</i><sup>2</sup>)
 * (d/d<i>x</i>)<sup><i>n</i></sup>
 * [exp(-<i>x</i><sup>2</sup>)]
 * </p>
 * 
 * <p>
 * このインターフェースでは,
 * 0 &le; <i>n</i> &le; 100
 * を扱うことができる.
 * </p>
 * 
 * @implSpec
 *               このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 *               モジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 * @see <a href="https://en.wikipedia.org/wiki/Hermite_polynomials" target=
 *          "_brank">
 *          Wikipedia: Hermite polynomials</a>
 */
public interface HermiteFunction {

    /**
     * このインターフェースが扱うことができる次数 <i>n</i> の最小値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int LOWER_LIMIT_OF_DEGREE_N = 0;

    /**
     * このインターフェースが扱うことができる次数 <i>n</i> の最大値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int UPPER_LIMIT_OF_DEGREE_N = 100;

    /**
     * このインスタンスが扱う次数 <i>n</i> の値を返す.
     * 
     * @return 次数 <i>n</i>
     */
    public abstract int degreeN();

    /**
     * Hermite 多項式
     * <i>H</i><sub><i>n</i></sub> (<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * </ul>
     * 
     * @param x 引数 <i>x</i>
     * @return <i>H</i><sub><i>n</i></sub> (<i>x</i>)
     */
    public abstract double hermiteH(double x);

    /**
     * 指定したパラメータ (次数) がサポートされているかを判定する.
     * 
     * @param degreeN 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int degreeN) {
        return LOWER_LIMIT_OF_DEGREE_N <= degreeN
                && degreeN <= UPPER_LIMIT_OF_DEGREE_N;
    }

    /**
     * 与えられた次数を持つ Hermite 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link HermiteFunction#acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @return 次数が <i>n</i> の Hermite 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static HermiteFunction instanceOf(int degreeN) {
        return HermiteFunctionImpl.instanceOf(degreeN);
    }
}
