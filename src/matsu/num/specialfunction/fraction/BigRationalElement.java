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
import java.math.BigInteger;
import java.math.MathContext;

/**
 * {@link BigInteger} 型で分子分母が表現された有理数構造の元.
 * 
 * <p>
 * 分母は必ず正の数により表現される.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.5
 */
public final class BigRationalElement
        implements MathFieldElement<RationalType, BigRationalElement> {

    private static final BigRationalElement ZERO =
            new BigRationalElement(BigInteger.ZERO, BigInteger.ONE);
    private static final BigRationalElement ONE =
            new BigRationalElement(BigInteger.ONE, BigInteger.ONE);

    private static final RationalType TYPE = RationalType.INSTANCE;

    private final BigInteger numerator;
    private final BigInteger denominator;

    /**
     * このコンストラクタは約分をしない.
     * 約分される必要がある場合, staticファクトリを呼ぶ.
     * 
     * @param numerator
     * @param denominator
     */
    private BigRationalElement(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public RationalType type() {
        return TYPE;
    }

    /**
     * 分子を返す.
     * 
     * @return 分子
     */
    public BigInteger numerator() {
        return this.numerator;
    }

    /**
     * 分母を返す.
     * 
     * @return 分母
     */
    public BigInteger denominator() {
        return this.denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BigRationalElement target)) {
            return false;
        }

        return this.numerator.equals(target.numerator)
                && this.denominator.equals(target.denominator);
    }

    @Override
    public int hashCode() {
        int result = this.numerator.hashCode();
        result = 31 * result + this.denominator.hashCode();

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s / %s", this.numerator, this.denominator);
    }

    @Override
    public BigRationalElement plus(BigRationalElement augend) {
        return this.plus_kernel(augend.numerator, augend.denominator);
    }

    @Override
    public BigRationalElement minus(BigRationalElement subtrahend) {
        return this.plus_kernel(subtrahend.numerator.negate(), subtrahend.denominator);
    }

    private BigRationalElement plus_kernel(BigInteger otherNume, BigInteger otherDenomi) {
        BigInteger gcd = this.denominator.gcd(otherDenomi);

        BigInteger thisDenomi_dividedByGcd = this.denominator.divide(gcd);
        BigInteger otherDenomi_dividedByGcd = otherDenomi.divide(gcd);

        BigInteger denomi2 = this.denominator.multiply(otherDenomi_dividedByGcd);
        BigInteger nume2 = this.numerator.multiply(otherDenomi_dividedByGcd);
        nume2 = nume2.add(thisDenomi_dividedByGcd.multiply(otherNume));

        // 足し算の結果発生する約分を処理するため, staticファクトリを呼ぶ
        return BigRationalElement.of(nume2, denomi2);
    }

    @Override
    public BigRationalElement times(BigRationalElement multiplicand) {
        return this.times_kernel(multiplicand.numerator, multiplicand.denominator);
    }

    @Override
    public BigRationalElement dividedBy(BigRationalElement divisor) {
        return this.times_kernel(divisor.denominator, divisor.numerator);
    }

    private BigRationalElement times_kernel(BigInteger otherNume, BigInteger otherDenomi) {
        if (otherDenomi.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("ゼロ除算");
        }

        //otherDenomiは負の可能性がある
        BigInteger gcd_thisNume_to_otherDenomi = this.numerator.gcd(otherDenomi);
        if (otherDenomi.compareTo(BigInteger.ZERO) < 0) {
            gcd_thisNume_to_otherDenomi = gcd_thisNume_to_otherDenomi.negate();
        }
        BigInteger thisNume2 = this.numerator.divide(gcd_thisNume_to_otherDenomi);
        BigInteger otherDenomi2 = otherDenomi.divide(gcd_thisNume_to_otherDenomi);

        BigInteger gcd_thisDenomi_to_otherNume = this.denominator.gcd(otherNume);
        BigInteger thisDenomi2 = this.denominator.divide(gcd_thisDenomi_to_otherNume);
        BigInteger otherNume2 = otherNume.divide(gcd_thisDenomi_to_otherNume);

        BigInteger denomi2 = thisDenomi2.multiply(otherDenomi2);
        BigInteger nume2 = thisNume2.multiply(otherNume2);

        return new BigRationalElement(nume2, denomi2);
    }

    @Override
    public BigRationalElement negated() {
        return new BigRationalElement(this.numerator.negate(), this.denominator);
    }

    @Override
    public double doubleValue() {
        return new BigDecimal(numerator)
                .divide(new BigDecimal(denominator), MathContext.DECIMAL128).doubleValue();
    }

    /**
     * 与えられた {@link BigInteger} 型の分子, 分母を持つ有理数を返す.
     * 
     * <p>
     * 必ず約分され, 分母は正に修正される. <br>
     * 分母が0の場合は例外をスローする.
     * </p>
     * 
     * @param numerator 分子
     * @param denominator 分母
     * @return 有理数
     * @throws ArithmeticException 分母が0の場合
     */
    public static BigRationalElement of(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("分母が0である");
        }
        BigInteger gcd = numerator.gcd(denominator);
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            gcd = gcd.negate();
        }
        return new BigRationalElement(numerator.divide(gcd), denominator.divide(gcd));
    }

    /**
     * 与えられた {@code long} 型の分子, 分母を持つ有理数を返す. <br>
     * 引数は{@link BigInteger} 型に変換される.
     * 
     * <p>
     * 必ず約分され, 分母は正に修正される. <br>
     * 分母が0の場合は例外をスローする.
     * </p>
     * 
     * @param numerator 分子
     * @param denominator 分母
     * @return 有理数
     * @throws ArithmeticException 分母が0の場合
     */
    public static BigRationalElement of(long numerator, long denominator) {
        return BigRationalElement.of(
                BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * 有理数構造の定数サプライヤ.
     * 
     * @author Matsuura Y.
     */
    public static final class ConstantSupplier implements MathFieldElement.ConstantSupplier<BigRationalElement> {

        public static final ConstantSupplier INSTANCE = new ConstantSupplier();

        @Override
        public BigRationalElement zero() {
            return BigRationalElement.ZERO;
        }

        @Override
        public BigRationalElement one() {
            return BigRationalElement.ONE;
        }
    }
}
