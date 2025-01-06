/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.22
 */
package matsu.num.specialfunction.fraction;

/**
 * <p>
 * 体 (数学) の元を表現する. <br>
 * 実質的にイミュータブルである. <br>
 * 値に基づく equalty を提供する.
 * </p>
 * 
 * <p>
 * 体とは, 0で割ることを除いて四則演算が定義された代数系である. <br>
 * 例えば, 有理数や実数の構造である. <br>
 * 定義により, 二項演算ができる型は対称性がなければならない.
 * </p>
 * 
 * <p>
 * 無限大やNaNを表現する手段は持っておらず, 演算等の結果が表現できない場合は例外をスローする.
 * </p>
 * 
 * <p>
 * この抽象の実装は, 可能な限り {@link Object#toString()} メソッドをオーバーライドし,
 * 文字列表現を提供すべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.9
 * @param <ET> このクラスと二項演算が可能な体構造の元を表す型.
 *            体の定義より, 自身に一致する.
 */
public abstract class MathField<ET extends MathField<ET>> {

    /**
     * 唯一のコンストラクタ.
     */
    protected MathField() {
        super();
    }

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     * @throws ArithmeticException 演算結果が表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET plus(ET augend);

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     * @throws ArithmeticException 演算結果が表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET minus(ET subtrahend);

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
     * @throws ArithmeticException 演算結果が表現できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET times(ET multiplicand);

    /**
     * 商を返す.
     * 
     * <p>
     * ゼロ割りを行った場合は例外をスローする. <br>
     * ただしサブタイプによっては, 例外スローでなく, 計算結果を非数により表現する場合がある.
     * </p>
     * 
     * @param divisor divisor
     * @return 商
     * @throws ArithmeticException 演算結果が表現できない場合 (0除算を含む)
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET dividedBy(ET divisor);

    /**
     * 加法逆元を返す.
     * 
     * @return 加法逆元
     */
    public abstract ET negated();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    /**
     * 体構造における, 定数のサプライヤ.
     * 
     * @author Matsuura Y.
     * @version 19.1
     * @param <ET> 体構造の元を表す型
     */
    public static interface ConstantSupplier<ET extends MathField<ET>> {

        /**
         * 加法単位元 (0) を返す.
         * 
         * @return 加法単位元
         */
        public abstract ET zero();

        /**
         * 乗法単位元 (1) を返す.
         * 
         * @return 乗法単位元
         */
        public abstract ET one();
    }
}
