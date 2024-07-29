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

/**
 * <p>
 * 複素数を表す.
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
 * @version 19.1
 */
public final class ComplexNumber extends MathField<ComplexNumber> {

    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);
    public static final ComplexNumber I = new ComplexNumber(0, 1);

    public static final ComplexNumber NaN = new ComplexNumber(Double.NaN, Double.NaN);

    private final double real;
    private final double imaginary;

    private ComplexNumber(double real, double imaginary) {
        super(DoubleUninterpretable.INSTANCE);
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

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link ComplexNumber} では,
     * オーバーフローを起こす場合は非数 {@link #NaN} が返る.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber plus(ComplexNumber augend) {
        return ComplexNumber.of(
                this.real + augend.real, this.imaginary + augend.imaginary);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link ComplexNumber} では,
     * オーバーフローを起こす場合は非数 {@link #NaN} が返る.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber minus(ComplexNumber subtrahend) {
        return ComplexNumber.of(
                this.real - subtrahend.real, this.imaginary - subtrahend.imaginary);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link ComplexNumber} では,
     * オーバーフローを起こす場合は非数 {@link #NaN} が返る.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber times(ComplexNumber multiplicand) {
        return ComplexNumber.of(
                this.real * multiplicand.real - this.imaginary * multiplicand.imaginary,
                this.real * multiplicand.imaginary + this.imaginary * multiplicand.real);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * {@link ComplexNumber} では,
     * オーバーフローを起こす場合は非数 {@link #NaN} が返る. <br>
     * また, ゼロ割りの場合も例外はスローせず, 非数を返す.
     * </p>
     * 
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber dividedBy(ComplexNumber divisor) {

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

        return ComplexNumber.of(
                (this.real * p + this.imaginary * q) / k,
                (this.imaginary * p - this.real * q) / k);
    }

    @Override
    public ComplexNumber negated() {
        return ComplexNumber.of(-real, -imaginary);
    }

    /**
     * 外部からの呼び出し不可.
     * 
     * @return -
     */
    @Override
    protected double toDouble() {
        throw new AssertionError("Bug:　到達不能のはずである");
    }

    /**
     * 虚部が0の複素数を返す. <br>
     * ただし, 実部が有限でない場合, 非数 ({@link #NaN} を返す.
     * 
     * @param real 実部
     * @return 複素数
     */
    public static ComplexNumber ofReal(double real) {
        return ComplexNumber.of(real, 0d);
    }

    /**
     * 実部が0の複素数を返す. <br>
     * ただし, 虚部が有限でない場合, 非数 ({@link #NaN} を返す.
     * 
     * @param imaginary 虚部
     * @return 複素数
     */
    public static ComplexNumber ofImaginary(double imaginary) {
        return ComplexNumber.of(0d, imaginary);
    }

    /**
     * 与えた値を実部と虚部に持つ複素数を返す. <br>
     * ただし, 実部虚部のいずれかが有限でない場合, 非数 ({@link #NaN} を返す.
     * 
     * @param real 実部
     * @param imaginary 虚部
     * @return 複素数
     */
    public static ComplexNumber of(double real, double imaginary) {

        if (!(Double.isFinite(real) && Double.isFinite(imaginary))) {
            return NaN;
        }
        return new ComplexNumber(real, imaginary);
    }

    /**
     * このクラスと関連する定数サプライヤを返す.
     * 
     * @return 定数サプライヤ.
     */
    public static MathField.ConstantSupplier<ComplexNumber> constantSupplier() {
        return ComplexNumber.ConstantSupplier.INSTANCE;
    }

    /**
     * {@link ComplexNumber} と関連する定数サプライヤ.
     */
    private static final class ConstantSupplier
            implements MathField.ConstantSupplier<ComplexNumber> {

        static final ConstantSupplier INSTANCE = new ConstantSupplier();

        ConstantSupplier() {
        }

        @Override
        public ComplexNumber zero() {
            return ComplexNumber.ZERO;
        }

        @Override
        public ComplexNumber one() {
            return ComplexNumber.ONE;
        }
    }
}
