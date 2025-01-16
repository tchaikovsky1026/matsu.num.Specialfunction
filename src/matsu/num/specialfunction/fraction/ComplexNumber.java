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

import java.util.Objects;

/**
 * <p>
 * 実部と虚部に {@link RealMathField} を持つ複素数を表す.
 * </p>
 * 
 * <p>
 * 内部で使用されることが前提である. <br>
 * 公開されることは想定されていない.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <ET> 実部と虚部を表現する実数型を表現する
 */
public final class ComplexNumber<ET extends RealMathField<ET>> extends MathField<ComplexNumber<ET>> {

    private final MathField.ConstantSupplier<ET> realConstantSupplier;

    private final ET real;
    private final ET imaginary;

    /**
     * providerは使いまわさなければならない.
     */
    private ComplexNumber(ET real, ET imaginary, MathField.ConstantSupplier<ET> realConstantSupplier) {
        super();
        this.real = real;
        this.imaginary = imaginary;
        this.realConstantSupplier = realConstantSupplier;
    }

    /**
     * 実部を返す.
     * 
     * @return 実部
     */
    public ET real() {
        return real;
    }

    /**
     * 虚部を返す.
     * 
     * @return 虚部
     */
    public ET imaginary() {
        return imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof ComplexNumber<?> target)) {
            return false;
        }

        return this.real.equals(target.real)
                && this.imaginary.equals(target.imaginary);
    }

    @Override
    public int hashCode() {
        int result = this.real.hashCode();
        result = 31 * result + this.imaginary.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s + i * %s", this.real, this.imaginary);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber<ET> plus(ComplexNumber<ET> augend) {
        return new ComplexNumber<>(
                this.real.plus(augend.real),
                this.imaginary.plus(augend.imaginary),
                this.realConstantSupplier);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber<ET> minus(ComplexNumber<ET> subtrahend) {
        return new ComplexNumber<>(
                this.real.minus(subtrahend.real),
                this.imaginary.minus(subtrahend.imaginary),
                this.realConstantSupplier);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber<ET> times(ComplexNumber<ET> multiplicand) {
        return new ComplexNumber<>(
                this.real.times(multiplicand.real)
                        .minus(this.imaginary.times(multiplicand.imaginary)),
                this.real.times(multiplicand.imaginary)
                        .plus(this.imaginary.times(multiplicand.real)),
                this.realConstantSupplier);
    }

    /**
     * @throws ArithmeticException {@inheritDoc }
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public ComplexNumber<ET> dividedBy(ComplexNumber<ET> divisor) {

        final ET p;
        final ET q;
        final ET k;
        if (divisor.real.abs().compareTo(divisor.imaginary.abs()) > 0) {
            p = this.realConstantSupplier.one();
            q = divisor.imaginary.dividedBy(divisor.real);
            k = divisor.real.times(this.realConstantSupplier.one().plus(q.times(q)));
        } else {
            p = divisor.real.dividedBy(divisor.imaginary);
            q = this.realConstantSupplier.one();
            k = divisor.imaginary.times(this.realConstantSupplier.one().plus(p.times(p)));
        }

        return new ComplexNumber<>(
                this.real.times(p).plus(this.imaginary.times(q))
                        .dividedBy(k),
                this.imaginary.times(p).minus(this.real.times(q))
                        .dividedBy(k),
                this.realConstantSupplier);
    }

    @Override
    public ComplexNumber<ET> negated() {
        return new ComplexNumber<>(real.negated(), imaginary.negated(), this.realConstantSupplier);
    }

    public static final class Provider<ET extends RealMathField<ET>> {

        private final MathField.ConstantSupplier<ET> realConstantSupplier;

        private final MathField.ConstantSupplier<ComplexNumber<ET>> constantSupplier;

        private final ComplexNumber<ET> zero;
        private final ComplexNumber<ET> one;

        /**
         * 
         * @param realConstantSupplier
         */
        public Provider(MathField.ConstantSupplier<ET> realConstantSupplier) {
            this.realConstantSupplier = realConstantSupplier;
            this.zero = new ComplexNumber<>(
                    realConstantSupplier.zero(),
                    realConstantSupplier.zero(),
                    realConstantSupplier);
            this.one = new ComplexNumber<>(
                    realConstantSupplier.one(),
                    realConstantSupplier.zero(),
                    realConstantSupplier);

            this.constantSupplier = new ConstantSupplier();
        }

        /**
         * 虚部が0の複素数を返す.
         * 
         * @param real 実部
         * @return 複素数
         * @throws NullPointerException 引数にnullを含む場合
         */
        public ComplexNumber<ET> getValueOfReal(ET real) {
            return getValueOf(
                    real,
                    realConstantSupplier.zero());
        }

        /**
         * 実部が0の複素数を返す.
         * 
         * @param imaginary 虚部
         * @return 複素数
         * @throws NullPointerException 引数にnullを含む場合
         */
        public ComplexNumber<ET> getValueOfImaginary(ET imaginary) {
            return getValueOf(
                    realConstantSupplier.zero(),
                    imaginary);
        }

        /**
         * 与えた値を実部と虚部に持つ複素数を返す.
         * 
         * @param real 実部
         * @param imaginary 虚部
         * @return 複素数
         * @throws NullPointerException 引数にnullを含む場合
         */
        public ComplexNumber<ET> getValueOf(ET real, ET imaginary) {
            return new ComplexNumber<>(
                    Objects.requireNonNull(real),
                    Objects.requireNonNull(imaginary),
                    this.realConstantSupplier);
        }

        /**
         * このクラスと関連する定数サプライヤを返す.
         * 
         * @return 定数サプライヤ.
         */
        public MathField.ConstantSupplier<ComplexNumber<ET>> constantSupplier() {
            return this.constantSupplier;
        }

        private final class ConstantSupplier
                implements MathField.ConstantSupplier<ComplexNumber<ET>> {

            ConstantSupplier() {
                super();
            }

            @Override
            public ComplexNumber<ET> zero() {
                return zero;
            }

            @Override
            public ComplexNumber<ET> one() {
                return one;
            }
        }
    }
}
