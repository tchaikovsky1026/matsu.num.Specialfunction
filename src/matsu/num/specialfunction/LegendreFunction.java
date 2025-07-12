/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.9
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.legendre.LegendreFunctionImpl;

/**
 * Legendre (陪)関数 (Ferrers 型) の計算
 * (およそ倍精度).
 * 
 * <p>
 * Ferrers 型の Legendre 陪関数
 * <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
 * は, 非負整数の次数 <i>&ell;</i> と,
 * 0 以上 <i>&ell;</i> 以下の階数 <i>m</i> をパラメータに持つ 1 変数関数
 * である. <br>
 * 通常, |<i>x</i>| &le; 1 で扱い, このインターフェースでもその定義域を採用する. <br>
 * Legendre 多項式
 * <i>P</i><sub><i>&ell;</i></sub> (<i>x</i>)
 * は, <i>m</i> = 0 とすることで計算できる.
 * </p>
 * 
 * <p>
 * Legendre 陪関数には, Ferrers 型と Hobson 型があり,
 * このインターフェースでは Ferrers 型を扱っている. <br>
 * Ferrers 型は次の式により定義される. <br>
 * <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
 * = (1 - <i>x</i><sup>2</sup>)<sup><i>m</i> / 2</sup>
 * (d/d<i>x</i>)<sup><i>m</i></sup>
 * [<i>P</i><sub><i>&ell;</i></sub> (<i>x</i>)]
 * </p>
 * 
 * <p>
 * このインターフェースでは,
 * 0 &le; <i>&ell;</i> &le; 100
 * を扱うことができる.
 * </p>
 * 
 * @implSpec
 *               このインターフェースは実装を隠ぺいして型を公開するためのものである. <br>
 *               モジュール外で継承・実装してはいけない.
 * 
 * @author Matsuura Y.
 * @see
 *          <a href= "https://en.wikipedia.org/wiki/Legendre_polynomials"
 *          target= "_brank">
 *          Wikipedia: Legendre polynomials</a>
 *          <br>
 *          <a href=
 *          "https://en.wikipedia.org/wiki/Associated_Legendre_polynomials"
 *          target= "_brank">
 *          Wikipedia: Associated Legendre polynomials</a>
 */
public interface LegendreFunction {

    /**
     * このインターフェースが扱うことができる次数 <i>&ell;</i> の最小値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int LOWER_LIMIT_OF_DEGREE_L = 0;

    /**
     * このインターフェースが扱うことができる次数 <i>&ell;</i> の最大値.
     * 
     * @deprecated
     *                 モジュール外部で直接この定数に依存すべきではない. <br>
     *                 パラメータの正当性は static メソッドにより検証されるべきである.
     */
    @Deprecated
    public static final int UPPER_LIMIT_OF_DEGREE_L = 100;

    /**
     * このインスタンスが扱う次数 <i>&ell;</i> の値を返す.
     * 
     * @return 次数 <i>&ell;</i>
     */
    public abstract int degreeL();

    /**
     * このインスタンスが扱う階数 <i>m</i> の値を返す.
     * 
     * @return 階数 <i>m</i>
     */
    public abstract int orderM();

    /**
     * Legendre 関数
     * <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li>|<i>x</i>| &gt; 1 &rarr; NaN</li>
     * </ul>
     * 
     * @param x 引数 <i>x</i>
     * @return <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
     */
    public abstract double legendreP(double x);

    /**
     * 指定したパラメータ (次数, 階数) がサポートされているかを判定する.
     * 
     * @param degreeL 次数 <i>&ell;</i>
     * @param orderM 階数 <i>m</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int degreeL, int orderM) {
        return LOWER_LIMIT_OF_DEGREE_L <= degreeL
                && degreeL <= UPPER_LIMIT_OF_DEGREE_L
                && 0 <= orderM
                && orderM <= degreeL;
    }

    /**
     * 与えられた次数と階数を持つ Legendre 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(int, int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeL 次数 <i>&ell;</i>
     * @param orderM 階数 <i>m</i>
     * @return (<i>&ell;</i>, <i>m</i>) の Legendre 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数, 階数がサポート外の場合
     */
    public static LegendreFunction instanceOf(int degreeL, int orderM) {
        return LegendreFunctionImpl.instanceOf(degreeL, orderM);
    }
}
