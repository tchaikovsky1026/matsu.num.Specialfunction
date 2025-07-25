/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.25
 */
package matsu.num.specialfunction;

/**
 * Chebyshev 関数 (多項式) の計算
 * (およそ倍精度).
 * 
 * <p>
 * <i>ここに Chebyshev 多項式の説明を入れる.</i>
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
 * @see <a href="https://en.wikipedia.org/wiki/Chebyshev_polynomials" target=
 *          "_brank">
 *          Wikipedia: Hermite polynomials</a>
 */
public interface ChebyshevFunction {

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
     * 第1種 Chebyshev 多項式
     * <i>T</i><sub><i>n</i></sub> (<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li>|<i>x</i>| &gt; 1 &rarr; NaN</li>
     * </ul>
     * 
     * @param x 引数 <i>x</i>
     * @return <i>T</i><sub><i>n</i></sub> (<i>x</i>)
     */
    public abstract double chebyshevT(double x);

    /**
     * 第2種 Chebyshev 多項式
     * <i>U</i><sub><i>n</i></sub> (<i>x</i>)
     * の値を返す.
     * 
     * <ul>
     * <li>|<i>x</i>| &gt; 1 &rarr; NaN</li>
     * </ul>
     * 
     * @param x 引数 <i>x</i>
     * @return <i>U</i><sub><i>n</i></sub> (<i>x</i>)
     */
    public abstract double chebyshevU(double x);

    /**
     * 指定した次数がサポートされているかを判定する.
     * 
     * @param degreeN 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int degreeN) {
        return LOWER_LIMIT_OF_DEGREE_N <= degreeN
                && degreeN <= UPPER_LIMIT_OF_DEGREE_N;
    }

    /**
     * 与えられた次数を持つ Chebyshev 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link #acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @return 次数 <i>n</i> の Chebyshev 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ChebyshevFunction instanceOf(int degreeN) {
        throw new AssertionError("TODO");
    }
}
