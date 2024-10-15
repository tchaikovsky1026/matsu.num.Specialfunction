package matsu.num.specialfunction.subpj;

import matsu.num.approximation.generalfield.PseudoRealNumber;
import matsu.num.mathtype.DoubleDoubleFloat;

/**
 * double-double浮動小数点数を用いた体構造の実現
 * 
 * @author Matsuura Y.
 */
public final class DoubleDoubleFloatElement extends PseudoRealNumber<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<
            DoubleDoubleFloatElement> PROVIDER = new Provider();

    private static final DoubleDoubleFloatElement ZERO =
            new DoubleDoubleFloatElement(DoubleDoubleFloat.POSITIVE_0);
    private static final DoubleDoubleFloatElement ONE =
            new DoubleDoubleFloatElement(DoubleDoubleFloat.POSITIVE_1);

    private final DoubleDoubleFloat value;

    /**
     * {@code DoubleDoubleFloat} から生成するコンストラクタ.
     * 
     * @throws IllegalArgumentException 有限でない
     * @throws NullPointerException null
     */
    private DoubleDoubleFloatElement(DoubleDoubleFloat value) {
        super();

        if (!value.isFinite()) {
            throw new IllegalArgumentException("有限でない");
        }
        //-0を排除
        if (value.equals(DoubleDoubleFloat.NEGATIVE_0)) {
            value = DoubleDoubleFloat.POSITIVE_0;
        }

        this.value = value;
    }

    /**
     * {@code double} から生成するコンストラクタ.
     * 
     * @throws IllegalArgumentException 有限でない
     */
    private DoubleDoubleFloatElement(double value) {
        this(DoubleDoubleFloat.valueOf(value));
    }

    @Override
    protected PseudoRealNumber.Provider<DoubleDoubleFloatElement> provider() {
        return PROVIDER;
    }

    @Override
    public DoubleDoubleFloatElement plus(DoubleDoubleFloatElement augend) {
        return createOrThrowArithmeticException(this.value.plus(augend.value));
    }

    @Override
    public DoubleDoubleFloatElement minus(DoubleDoubleFloatElement subtrahend) {
        return createOrThrowArithmeticException(this.value.minus(subtrahend.value));
    }

    @Override
    public DoubleDoubleFloatElement times(DoubleDoubleFloatElement multiplicand) {
        return createOrThrowArithmeticException(this.value.times(multiplicand.value));
    }

    @Override
    public DoubleDoubleFloatElement dividedBy(DoubleDoubleFloatElement divisor) {
        return createOrThrowArithmeticException(this.value.dividedBy(divisor.value));
    }

    /**
     * 値を生成するか, 不正値 (無限大, NaN) の場合はArithmeticExcptionをスローする.
     * 
     * @throws ArithmeticException 不正値の場合
     */
    private static DoubleDoubleFloatElement createOrThrowArithmeticException(DoubleDoubleFloat value) {
        try {
            return new DoubleDoubleFloatElement(value);
        } catch (IllegalArgumentException iae) {
            throw new ArithmeticException(String.format("扱えない値: value = %s", value));
        }
    }

    @Override
    public DoubleDoubleFloatElement negated() {
        return new DoubleDoubleFloatElement(this.value.negated());
    }

    @Override
    public DoubleDoubleFloatElement abs() {
        return new DoubleDoubleFloatElement(this.value.abs());
    }

    @Override
    public double asDouble() {
        return this.value.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoubleDoubleFloatElement target)) {
            return false;
        }

        return this.value.equals(target.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(DoubleDoubleFloatElement o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public static PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    private static final class Provider implements PseudoRealNumber.Provider<DoubleDoubleFloatElement> {

        /**
         * 唯一のコンストラクタ.
         */
        Provider() {
            super();
        }

        @Override
        public DoubleDoubleFloatElement fromDoubleValue(double value) {
            return new DoubleDoubleFloatElement(value);
        }

        @Override
        public DoubleDoubleFloatElement zero() {
            return ZERO;
        }

        @Override
        public DoubleDoubleFloatElement one() {
            return ONE;
        }

        @Override
        public DoubleDoubleFloatElement[] createArray(int length) {
            if (length < 0) {
                throw new IllegalArgumentException("サイズが負");
            }
            return new DoubleDoubleFloatElement[length];
        }
    }
}
