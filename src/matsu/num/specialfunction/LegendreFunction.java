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

/**
 * Legendre (陪)関数の計算
 * (およそ倍精度).
 * 
 * <p>
 * Legendre (陪)関数
 * <i>P</i><sub><i>&ell;</i></sub><sup><i>m</i></sup> (<i>x</i>)
 * は, 非負整数の次数 <i>&ell;</i> と,
 * 0 以上 <i>&ell;</i> 以下の階数 <i>m</i> をパラメータに持つ 1 変数関数
 * である. <br>
 * 通常, |<i>x</i>| &le; 1 で扱い, このインターフェースでもその定義域を採用する. <br>
 * Legendre 多項式は, <i>m</i> = 0 とすることで計算できる.
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
}
