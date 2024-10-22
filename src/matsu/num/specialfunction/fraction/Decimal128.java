/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.22
 */
package matsu.num.specialfunction.fraction;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * <p>
 * Decimal128 形式 (おおよそ4倍精度) の体構造の元.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.9
 */
public final class Decimal128
        extends RealMathField<Decimal128> {

    private static final Decimal128 ZERO = new Decimal128(BigDecimal.ZERO);
    private static final Decimal128 ONE = new Decimal128(BigDecimal.ONE);

    private final BigDecimal value;

    public Decimal128(BigDecimal value) {
        super();
        this.value = Objects.requireNonNull(value);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Decimal128 plus(Decimal128 augend) {
        return new Decimal128(this.value.add(augend.value));
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Decimal128 minus(Decimal128 subtrahend) {
        return new Decimal128(this.value.subtract(subtrahend.value));
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Decimal128 times(Decimal128 multiplicand) {
        return new Decimal128(this.value.multiply(multiplicand.value));
    }

    /**
     * @throws ArithmeticException 0割りを行った場合
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public Decimal128 dividedBy(Decimal128 divisor) {
        return new Decimal128(this.value.divide(divisor.value, MathContext.DECIMAL128));
    }

    @Override
    public Decimal128 negated() {
        return new Decimal128(this.value.negate());
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    /**
     * 与えられたインスタンスが自身と等価であるかを判定する.
     * 
     * @param obj 比較相手
     * @return 等価なら true
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Decimal128 target)) {
            return false;
        }

        return this.compareTo(target) == 0;
    }

    /**
     * このインスタンスのハッシュコードを返す.
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public Decimal128 abs() {
        return new Decimal128(this.value.abs());
    }

    /**
     * 与えられたインスタンスと自身との順序を判定する.
     * 
     * @param other 比較相手
     * @return 比較結果
     * @throws NullPointerException 引数がnullの場合
     */
    @Override
    public int compareTo(Decimal128 other) {
        return this.value.compareTo(other.value);
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * このクラスと関連する定数サプライヤを返す.
     * 
     * @return 定数サプライヤ.
     */
    public static MathField.ConstantSupplier<Decimal128> constantSupplier() {
        return Decimal128.ConstantSupplier.INSTANCE;
    }

    /**
     * {@link Decimal128} と関連する定数サプライヤ.
     */
    private static final class ConstantSupplier
            implements MathField.ConstantSupplier<Decimal128> {

        static final ConstantSupplier INSTANCE = new ConstantSupplier();

        ConstantSupplier() {
            super();
        }

        @Override
        public Decimal128 zero() {
            return Decimal128.ZERO;
        }

        @Override
        public Decimal128 one() {
            return Decimal128.ONE;
        }
    }
}
