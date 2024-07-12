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

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

/**
 * <p>
 * Decimal128 形式 (おおよそ4倍精度) の体構造の元.
 * </p>
 * 
 * <p>
 * このクラスの equalty とcomparability は {@link BigDecimal}
 * に準じており, 整合しないことに注意が必要である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.5
 */
public final class Decimal128Element
        implements MathFieldElement<Decimal128Type, Decimal128Element>,
        Comparable<Decimal128Element> {

    private static final Decimal128Element ZERO = new Decimal128Element(BigDecimal.ZERO);
    private static final Decimal128Element ONE = new Decimal128Element(BigDecimal.ONE);

    private static final Decimal128Type TYPE = Decimal128Type.INSTANCE;

    private final BigDecimal value;

    public Decimal128Element(BigDecimal value) {
        super();
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public Decimal128Type type() {
        return TYPE;
    }

    @Override
    public Decimal128Element plus(Decimal128Element augend) {
        return new Decimal128Element(this.value.add(augend.value));
    }

    @Override
    public Decimal128Element minus(Decimal128Element subtrahend) {
        return new Decimal128Element(this.value.subtract(subtrahend.value));
    }

    @Override
    public Decimal128Element times(Decimal128Element multiplicand) {
        return new Decimal128Element(this.value.multiply(multiplicand.value));
    }

    /**
     * @throws ArithmeticException {@inheritDoc}
     */
    @Override
    public Decimal128Element dividedBy(Decimal128Element divisor) {
        return new Decimal128Element(this.value.divide(divisor.value, MathContext.DECIMAL128));
    }

    @Override
    public Decimal128Element negated() {
        return new Decimal128Element(this.value.negate());
    }

    @Override
    public double doubleValue() {
        return this.value.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Decimal128Element target)) {
            return false;
        }

        return this.value.equals(target.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(Decimal128Element other) {
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * Decimal128 形式 (おおよそ4倍精度) の体構造の定数サプライヤ.
     * 
     * @author Matsuura Y.
     */
    public static final class ConstantSupplier implements MathFieldElement.ConstantSupplier<Decimal128Element> {

        public static final ConstantSupplier INSTANCE = new ConstantSupplier();

        private ConstantSupplier() {
        }

        @Override
        public Decimal128Element zero() {
            return Decimal128Element.ZERO;
        }

        @Override
        public Decimal128Element one() {
            return Decimal128Element.ONE;
        }
    }
}
