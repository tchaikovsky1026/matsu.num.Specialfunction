/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.23
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
 * @author Matsuura Y.
 * @version 18.9
 */
public final class ComplexNumber {

    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);
    public static final ComplexNumber I = new ComplexNumber(0, 1);

    private final double real;
    private final double imaginary;

    private ComplexNumber(double real, double imaginary) {
        super();
        this.real = real;
        this.imaginary = imaginary;
    }

    public double real() {
        return real;
    }

    public double imaginary() {
        return imaginary;
    }

    /**
     * 和を返す.
     * 
     * @param augend augend
     * @return 和
     */
    public ComplexNumber plus(ComplexNumber augend) {
        return new ComplexNumber(
                this.real + augend.real, this.imaginary + augend.imaginary);
    }

    /**
     * 差を返す.
     * 
     * @param subtrahend subtrahend
     * @return 差
     */
    public ComplexNumber minus(ComplexNumber subtrahend) {
        return new ComplexNumber(
                this.real - subtrahend.real, this.imaginary - subtrahend.imaginary);
    }

    /**
     * 積を返す.
     * 
     * @param multiplicand multiplicand
     * @return 積
     */
    public ComplexNumber times(ComplexNumber multiplicand) {
        return new ComplexNumber(
                this.real * multiplicand.real - this.imaginary * multiplicand.imaginary,
                this.real * multiplicand.imaginary + this.imaginary * multiplicand.real);
    }

    /**
     * 商を返す.
     * 
     * @param divisor divisor
     * @return 商
     */
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

        return new ComplexNumber(
                (this.real * p + this.imaginary * q) / k,
                (this.imaginary * p - this.real * q) / k);
    }

    /**
     * 加法逆元を返す.
     * 
     * @return 加法逆元
     */
    public ComplexNumber negated() {
        return new ComplexNumber(-real, -imaginary);
    }

    /**
     * 虚部が0の複素数を返す.
     * 
     * @param real 実部
     * @return 複素数
     */
    public static ComplexNumber ofReal(double real) {
        return new ComplexNumber(real, 0d);
    }

    /**
     * 実部が0の複素数を返す.
     * 
     * @param imaginary 虚部
     * @return 複素数
     */
    public static ComplexNumber ofImaginary(double imaginary) {
        return new ComplexNumber(0d, imaginary);
    }

    /**
     * 与えた値を実部と虚部に持つ複素数を返す.
     * 
     * @param real 実部
     * @param imaginary 虚部
     * @return 複素数
     */
    public static ComplexNumber of(double real, double imaginary) {
        return new ComplexNumber(real, imaginary);
    }
}
