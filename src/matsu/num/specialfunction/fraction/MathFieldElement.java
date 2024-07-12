/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.12
 */
package matsu.num.specialfunction.fraction;

/**
 * <p>
 * 数学的な体の元を表現するインターフェース. <br>
 * 値に基づく equalty を提供する. <br>
 * comparability は任意である.
 * </p>
 * 
 * <p>
 * このインターフェースの実装は, 可能な限り {@link Object#toString()} メソッドをオーバーライドし,
 * 文字列表現を提供すべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.5
 * @param <T> 体構造を表す型
 * @param <ET> 体の元を表す型
 */
public interface MathFieldElement<T extends MathFieldType<T>, ET extends MathFieldElement<T, ET>> {

    /**
     * このインスタンスの体構造を返す.
     * 
     * @return 体構造
     */
    public abstract T type();

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     */
    public abstract ET plus(ET augend);

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     */
    public abstract ET minus(ET subtrahend);

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
     */
    public abstract ET times(ET multiplicand);

    /**
     * 商を返す.
     * 
     * @param divisor divisor
     * @return 商
     * @throws ArithmeticException 0除算が発生した場合
     */
    public abstract ET dividedBy(ET divisor);

    /**
     * 加法逆元を返す.
     * 
     * @return 加法逆元
     */
    public abstract ET negated();

    /**
     * このインスタンスの型 ({@link #type()} が {@code double} 型で解釈可能
     * ({@link MathFieldType#canBeInterpretedAsDouble()} が {@code true}
     * の場合に,
     * {@code double} 表現された値を返す. <br>
     * 解釈できない場合は例外をスローする.
     * 
     * @return double表現した値
     * @throws UnsupportedOperationException {@code double} 型として解釈できない場合
     */
    public abstract double doubleValue();

    /**
     * 体構造における, 定数のサプライヤ.
     * 
     * @author Matsuura Y.
     * @version 18.5
     * @param <ET> 体の元を表す型
     */
    public static interface ConstantSupplier<ET extends MathFieldElement<?, ET>> {

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
