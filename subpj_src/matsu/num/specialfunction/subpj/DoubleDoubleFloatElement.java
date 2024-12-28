package matsu.num.specialfunction.subpj;

import java.math.BigDecimal;
import java.math.MathContext;

import matsu.num.approximation.PseudoRealNumber;
import matsu.num.mathtype.DoubleDoubleFloat;
import matsu.num.specialfunction.fraction.BigRational;

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

    /**
     * {@link DoubleDoubleFloatElement} のプロバイダを返す.
     * 
     * @return プロバイダ
     */
    public static PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    /**
     * 10進文字列からインスタンスを生成する.
     * 
     * <p>
     * 文字列の規約は {@link java.math.BigDecimal} にしたがう.
     * </p>
     * 
     * @param decimalExpression 文字列
     * @return インスタンス
     * @throws NumberFormatException 文字列が10進数値として評価できない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static DoubleDoubleFloatElement fromDecimalExpression(String decimalExpression) {
        BigDecimal value = new BigDecimal(decimalExpression);
        double high = value.doubleValue();
        double low = value.subtract(new BigDecimal(high)).doubleValue();

        return PROVIDER.fromDoubleValue(high).plus(low);
    }

    /**
     * {@link BigRational} からインスタンスを得る.
     * 
     * @param src 元の値
     * @return 変換後
     * @throws IllegalArgumentException 引数が扱えない場合
     * @throws NullPointerException 引数がnullの場合
     */
    public static DoubleDoubleFloatElement fromBigRational(BigRational src) {
        BigDecimal decimal = new BigDecimal(src.numerator(), MathContext.DECIMAL128)
                .divide(new BigDecimal(src.denominator(), MathContext.DECIMAL128), MathContext.DECIMAL128);

        double high = decimal.doubleValue();
        double low = decimal.subtract(new BigDecimal(high), MathContext.DECIMAL128).doubleValue();

        try {
            return PROVIDER.fromDoubleValue(high).plus(low);
        } catch (ArithmeticException ae) {
            throw new IllegalArgumentException("扱えない値");
        }
    }

    /**
     * プロバイダの実装.
     */
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
            return new DoubleDoubleFloatElement[length];
        }
    }
}
