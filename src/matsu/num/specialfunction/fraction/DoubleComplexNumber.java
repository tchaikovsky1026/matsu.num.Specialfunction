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
 * {@code double} 複素数を表す.
 * </p>
 * 
 * <p>
 * 内部で使用されることが前提である. <br>
 * 公開されることは想定されていない.
 * </p>
 * 
 * <p>
 * 無限大は表現できず, オーバーフローする場合は常にNaNの表現をする.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class DoubleComplexNumber extends MathField<DoubleComplexNumber> {

    public static final DoubleComplexNumber ZERO = new DoubleComplexNumber(0, 0);
    public static final DoubleComplexNumber ONE = new DoubleComplexNumber(1, 0);
    public static final DoubleComplexNumber I = new DoubleComplexNumber(0, 1);

    private final double real;
    private final double imaginary;

    private DoubleComplexNumber(double real, double imaginary) {
        super();
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * 実部を返す.
     * 
     * @return 実部
     */
    public double real() {
        return real;
    }

    /**
     * 虚部を返す.
     * 
     * @return 虚部
     */
    public double imaginary() {
        return imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof DoubleComplexNumber target)) {
            return false;
        }

        return Double.compare(this.real, target.real) == 0
                && Double.compare(this.imaginary, target.imaginary) == 0;
    }

    @Override
    public int hashCode() {
        int result = Double.hashCode(this.real);
        result = 31 * result + Double.hashCode(this.imaginary);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s + i * %s", this.real, this.imaginary);
    }

    private static DoubleComplexNumber createOrThrowArithmeticExCausedByCalc(
            double real, double imaginary) {
        try {
            return DoubleComplexNumber.of(real, imaginary);
        } catch (IllegalArgumentException iae) {
            throw new ArithmeticException("演算結果が表現できない");
        }
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public DoubleComplexNumber plus(DoubleComplexNumber augend) {
        return createOrThrowArithmeticExCausedByCalc(
                this.real + augend.real, this.imaginary + augend.imaginary);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public DoubleComplexNumber minus(DoubleComplexNumber subtrahend) {
        return createOrThrowArithmeticExCausedByCalc(
                this.real - subtrahend.real, this.imaginary - subtrahend.imaginary);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public DoubleComplexNumber times(DoubleComplexNumber multiplicand) {
        return createOrThrowArithmeticExCausedByCalc(
                this.real * multiplicand.real - this.imaginary * multiplicand.imaginary,
                this.real * multiplicand.imaginary + this.imaginary * multiplicand.real);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public DoubleComplexNumber dividedBy(DoubleComplexNumber divisor) {

        final double p;
        final double q;
        final double k;
        if (Math.abs(divisor.real) > Math.abs(divisor.imaginary)) {
            p = 1d;
            q = divisor.imaginary / divisor.real;
            k = divisor.real * (1 + q * q);
        } else {
            p = divisor.real / divisor.imaginary;
            q = 1d;
            k = divisor.imaginary * (1 + p * p);
        }

        return createOrThrowArithmeticExCausedByCalc(
                (this.real * p + this.imaginary * q) / k,
                (this.imaginary * p - this.real * q) / k);
    }

    @Override
    public DoubleComplexNumber negated() {
        return DoubleComplexNumber.of(-real, -imaginary);
    }

    /**
     * 虚部が0の複素数を返す.
     * 
     * <p>
     * 無限大やNaNを含んではいけない.
     * </p>
     * 
     * @param real 実部
     * @return 複素数
     * @throws IllegalArgumentException 引数が不正
     */
    public static DoubleComplexNumber ofReal(double real) {
        return DoubleComplexNumber.of(real, 0d);
    }

    /**
     * 実部が0の複素数を返す.
     * 
     * <p>
     * 無限大やNaNを含んではいけない.
     * </p>
     * 
     * @param imaginary 虚部
     * @return 複素数
     * @throws IllegalArgumentException 引数が不正
     */
    public static DoubleComplexNumber ofImaginary(double imaginary) {
        return DoubleComplexNumber.of(0d, imaginary);
    }

    /**
     * 与えた値を実部と虚部に持つ複素数を返す.
     * 
     * <p>
     * 無限大やNaNを含んではいけない.
     * </p>
     * 
     * @param real 実部
     * @param imaginary 虚部
     * @return 複素数
     * @throws IllegalArgumentException 引数が不正
     */
    public static DoubleComplexNumber of(double real, double imaginary) {

        if (!(Double.isFinite(real) && Double.isFinite(imaginary))) {
            throw new IllegalArgumentException("引数が不正");
        }
        return new DoubleComplexNumber(real, imaginary);
    }

    /**
     * このクラスと関連する定数サプライヤを返す.
     * 
     * @return 定数サプライヤ.
     */
    public static MathField.ConstantSupplier<DoubleComplexNumber> constantSupplier() {
        return DoubleComplexNumber.ConstantSupplier.INSTANCE;
    }

    /**
     * {@link DoubleComplexNumber} と関連する定数サプライヤ.
     */
    private static final class ConstantSupplier
            implements MathField.ConstantSupplier<DoubleComplexNumber> {

        static final ConstantSupplier INSTANCE = new ConstantSupplier();

        ConstantSupplier() {
        }

        @Override
        public DoubleComplexNumber zero() {
            return DoubleComplexNumber.ZERO;
        }

        @Override
        public DoubleComplexNumber one() {
            return DoubleComplexNumber.ONE;
        }
    }
}
