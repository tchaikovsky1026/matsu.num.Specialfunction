/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.29
 */
package matsu.num.specialfunction.fraction;

import java.util.Objects;

/**
 * <p>
 * 体 (数学) の元を表現する. <br>
 * 実質的にイミュータブルである. <br>
 * 値に基づく equalty を提供する. <br>
 * comparability は任意である.
 * </p>
 * 
 * <p>
 * 体とは, (0で割ることを除いて) 四則演算が定義された代数系である. <br>
 * 例えば, 有理数や実数の構造である. <br>
 * 定義により, 二項演算ができる型は対称性がなければならない.
 * </p>
 * 
 * <p>
 * この抽象の実装は, 可能な限り {@link Object#toString()} メソッドをオーバーライドし,
 * 文字列表現を提供すべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.1
 * @param <ET> このクラスと二項演算が可能な体構造の元を表す型.
 *            体の定義より, 自身に一致する.
 */
public abstract class MathField<ET extends MathField<ET>> {

    private final MathFieldProperty fieldProperty;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 引数としてこの体の性質を与える. <br>
     * これは, インスタンスでなくクラス全体に共通する性質を表す. <br>
     * したがってサブクラスにおいて, {@link MathFieldProperty}
     * はコンストラクタで指定するのではなく, ハードコーディングされるべきである.
     * </p>
     * 
     * @param fieldProperty 体の性質
     */
    protected MathField(MathFieldProperty fieldProperty) {
        super();
        this.fieldProperty = Objects.requireNonNull(fieldProperty);
    }

    /**
     * このインスタンスの体としての性質を返す.
     * 
     * @return 体としての性質
     */
    protected final MathFieldProperty fieldProperty() {
        return this.fieldProperty;
    }

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET plus(ET augend);

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     * @throws NullPointerException 引数がnullの場合
     */
    public abstract ET minus(ET subtrahend);

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
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
     * @throws NullPointerException 引数がnullの場合
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
     * このインスタンスの型 ({@link #fieldProperty()} が {@code double} 型で解釈可能
     * ({@link MathFieldProperty#canBeInterpretedAsDouble()} が {@code true}
     * の場合に,
     * {@code double} 表現された値を返す. <br>
     * 解釈できない場合は例外をスローする.
     * 
     * @return double表現した値
     * @throws UnsupportedOperationException {@code double} 型として解釈できない場合
     */
    public final double doubleValue() {
        if (!this.fieldProperty.canBeInterpretedAsDouble()) {
            throw new UnsupportedOperationException("このインスタンスはdouble型として解釈可能でない");
        }
        return this.toDouble();
    }

    /**
     * {@link #doubleValue()} から内部的に呼ばれるメソッド,
     * このインスタンスを {@code double} 型に変換する. <br>
     * このインスタンスの型 ({@link #fieldProperty()} が {@code double} 型で解釈できない場合,
     * このメソッドは到達不能である.
     * 
     * @return 自身を {@code double} に変換した値
     */
    protected abstract double toDouble();

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
