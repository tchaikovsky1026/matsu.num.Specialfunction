package matsu.num.specialfunction.subpj;

import java.util.Objects;

import matsu.num.specialfunction.fraction.MathField;
import matsu.num.specialfunction.fraction.RealMathField;

/**
 * {@link DoubleDoubleFloatElement} の {@link RealMathField} へのラッパー.
 * 
 * @author Matsuura Y.
 */
public final class DoubleDoubleFloatMathFieldWrapper extends RealMathField<DoubleDoubleFloatMathFieldWrapper> {

    private final DoubleDoubleFloatElement value;

    public DoubleDoubleFloatMathFieldWrapper(DoubleDoubleFloatElement value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public int compareTo(DoubleDoubleFloatMathFieldWrapper o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper abs() {
        return new DoubleDoubleFloatMathFieldWrapper(value.abs());
    }

    @Override
    public double doubleValue() {
        return value.asDouble();
    }

    public DoubleDoubleFloatElement asDoubleDoubleFloatElement() {
        return this.value;
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper plus(DoubleDoubleFloatMathFieldWrapper augend) {
        return new DoubleDoubleFloatMathFieldWrapper(value.plus(augend.value));
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper minus(DoubleDoubleFloatMathFieldWrapper subtrahend) {
        return new DoubleDoubleFloatMathFieldWrapper(value.minus(subtrahend.value));
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper times(DoubleDoubleFloatMathFieldWrapper multiplicand) {
        return new DoubleDoubleFloatMathFieldWrapper(value.times(multiplicand.value));
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper dividedBy(DoubleDoubleFloatMathFieldWrapper divisor) {
        return new DoubleDoubleFloatMathFieldWrapper(value.dividedBy(divisor.value));
    }

    @Override
    public DoubleDoubleFloatMathFieldWrapper negated() {
        return new DoubleDoubleFloatMathFieldWrapper(value.negated());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoubleDoubleFloatMathFieldWrapper target)) {
            return false;
        }

        return this.value.equals(target.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public static MathField.ConstantSupplier<DoubleDoubleFloatMathFieldWrapper>
            constantSupplier() {

        return ConstantSupplier.INSTANCE;
    }

    private static final class ConstantSupplier
            implements MathField.ConstantSupplier<DoubleDoubleFloatMathFieldWrapper> {

        private static final MathField.ConstantSupplier<
                DoubleDoubleFloatMathFieldWrapper> INSTANCE = new ConstantSupplier();

        private static final DoubleDoubleFloatMathFieldWrapper ZERO =
                new DoubleDoubleFloatMathFieldWrapper(
                        DoubleDoubleFloatElement.elementProvider().zero());
        private static final DoubleDoubleFloatMathFieldWrapper ONE =
                new DoubleDoubleFloatMathFieldWrapper(
                        DoubleDoubleFloatElement.elementProvider().one());;

        private ConstantSupplier() {
            super();
        }

        @Override
        public DoubleDoubleFloatMathFieldWrapper zero() {
            return ZERO;
        }

        @Override
        public DoubleDoubleFloatMathFieldWrapper one() {
            return ONE;
        }

    }

}
