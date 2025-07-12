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

import matsu.num.specialfunction.laguerre.LaguerreFunctionImpl;

/**
 * Laguerre 関数 (陪多項式) の計算
 * (およそ倍精度).
 * 
 * <p>
 * Laguerre 陪多項式
 * <i>L</i><sub><i>n</i></sub><sup><i>k</i></sup> (<i>x</i>)
 * は, 非負整数の次数 <i>n</i>, 階数 <i>k</i> をパラメータに持つ 1 変数関数
 * である. <br>
 * 通常, <i>x</i> &ge; 0 で扱い, このインターフェースでもその定義域を採用する. <br>
 * Laguerre 多項式
 * <i>L</i><sub><i>n</i></sub> (<i>x</i>)
 * は, <i>k</i> = 0 とすることで計算できる.
 * </p>
 * 
 * <p>
 * Laguerre 多項式, Laguerre 陪多項式は次の式により定義される. <br>
 * <i>L</i><sub><i>n</i></sub> (<i>x</i>)
 * = (1 / <i>n</i>!) exp(<i>x</i>)
 * (d/d<i>x</i>)<sup><i>n</i></sup>
 * [<i>x</i><sup><i>n</i></sup>exp(-<i>x</i>)]
 * <br>
 * <i>L</i><sub><i>n</i></sub><sup><i>k</i></sup> (<i>x</i>)
 * = (-1)<sup><i>k</i></sup>
 * (d/d<i>x</i>)<sup><i>k</i></sup>
 * [<i>L</i><sub><i>n</i>+<i>k</i></sub> (<i>x</i>)]
 * </p>
 * 
 * <p>
 * このインターフェースでは,
 * 0 &le; <i>n</i> &le; 100,
 * 0 &le; <i>k</i> &le; 100
 * を扱うことができる.
 * </p>
 * 
 * @implSpec
 *               このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 *               モジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 * @see <a href="https://en.wikipedia.org/wiki/Laguerre_polynomials" target=
 *          "_brank">
 *          Wikipedia: Laguerre polynomials</a>
 */
public interface LaguerreFunction {

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
     * このインターフェースが扱うことができる階数 <i>k</i> の最小値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int LOWER_LIMIT_OF_ORDER_K = 0;

    /**
     * このインターフェースが扱うことができる階数 <i>k</i> の最大値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int UPPER_LIMIT_OF_ORDER_K = 100;

    /**
     * このインスタンスが扱う次数 <i>n</i> の値を返す.
     * 
     * @return 次数 <i>n</i>
     */
    public abstract int degreeN();

    /**
     * このインスタンスが扱う階数 <i>k</i> の値を返す.
     * 
     * @return 階数 <i>k</i>
     */
    public abstract int orderK();

    /**
     * Laguerre 陪多項式
     * <i>L</i><sub><i>n</i></sub><sup><i>k</i></sup> (<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN</li>
     * </ul>
     * 
     * @param x 引数 <i>x</i>
     * @return <i>L</i><sub><i>n</i></sub><sup><i>k</i></sup> (<i>x</i>)
     */
    public abstract double laguerreL(double x);

    /**
     * 指定したパラメータ (次数, 階数) がサポートされているかを判定する.
     * 
     * @param degreeN 次数 <i>n</i>
     * @param orderK 階数 <i>k</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int degreeN, int orderK) {
        return LOWER_LIMIT_OF_DEGREE_N <= degreeN
                && degreeN <= UPPER_LIMIT_OF_DEGREE_N
                && LOWER_LIMIT_OF_ORDER_K <= orderK
                && orderK <= UPPER_LIMIT_OF_ORDER_K;
    }

    /**
     * 与えられた次数と階数を持つ Laguerre 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link LaguerreFunction#acceptsParameter(int, int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @param orderK 階数 <i>k</i>
     * @return (<i>n</i>, <i>k</i>) の Laguerre 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数, 階数がサポート外の場合
     */
    public static LaguerreFunction instanceOf(int degreeN, int orderK) {
        return LaguerreFunctionImpl.instanceOf(degreeN, orderK);
    }
}
