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
import java.math.BigInteger;
import java.math.MathContext;

/**
 * {@link BigInteger} 型で分子分母が表現された有理数体の元.
 * 
 * <p>
 * 常に約分され, 既約分数である. <br>
 * 分母は必ず正の数により表現される. <br>
 * 0 は 0/1 である.
 * </p>
 * 
 * <p>
 * このインスタンスは分子分母の整数値に基づく equality を提供する. <br>
 * さらに, 既約分数の一意性により, これは有理数の equality に一致する.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.9
 */
public final class BigRational
        extends RealMathField<BigRational> {

    private static final BigRational ZERO =
            new BigRational(BigInteger.ZERO, BigInteger.ONE);
    private static final BigRational ONE =
            new BigRational(BigInteger.ONE, BigInteger.ONE);

    private final BigInteger numerator;
    private final BigInteger denominator;

    /**
     * このコンストラクタは約分をしない.
     * 約分される必要がある場合, staticファクトリを呼ぶ.
     * 
     * @param numerator
     * @param denominator
     */
    private BigRational(BigInteger numerator, BigInteger denominator) {
        super();
        this.numerator = numerator;
        this.denominator = denominator;
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
        if (!(obj instanceof BigRational target)) {
            return false;
        }

        return this.numerator.equals(target.numerator)
                && this.denominator.equals(target.denominator);
    }

    /**
     * このインスタンスのハッシュコードを返す.
     * 
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        int result = this.numerator.hashCode();
        result = 31 * result + this.denominator.hashCode();

        return result;
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format("%s / %s", this.numerator, this.denominator);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public BigRational plus(BigRational augend) {
        return this.plus_kernel(augend.numerator, augend.denominator);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public BigRational minus(BigRational subtrahend) {
        return this.plus_kernel(subtrahend.numerator.negate(), subtrahend.denominator);
    }

    private BigRational plus_kernel(BigInteger otherNume, BigInteger otherDenomi) {
        BigInteger gcd = this.denominator.gcd(otherDenomi);

        BigInteger thisDenomi_dividedByGcd = this.denominator.divide(gcd);
        BigInteger otherDenomi_dividedByGcd = otherDenomi.divide(gcd);

        BigInteger denomi2 = this.denominator.multiply(otherDenomi_dividedByGcd);
        BigInteger nume2 = this.numerator.multiply(otherDenomi_dividedByGcd);
        nume2 = nume2.add(thisDenomi_dividedByGcd.multiply(otherNume));

        // 足し算の結果発生する約分を処理するため, staticファクトリを呼ぶ
        return BigRational.of(nume2, denomi2);
    }

    /**
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public BigRational times(BigRational multiplicand) {
        return this.times_kernel(multiplicand.numerator, multiplicand.denominator);
    }

    /**
     * @throws ArithmeticException 0割りを行った場合
     * @throws NullPointerException {@inheritDoc }
     */
    @Override
    public BigRational dividedBy(BigRational divisor) {
        //ゼロ除算の発生の可能性
        return this.times_kernel(divisor.denominator, divisor.numerator);
    }

    private BigRational times_kernel(BigInteger otherNume, BigInteger otherDenomi) {
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

        return new BigRational(nume2, denomi2);
    }

    @Override
    public BigRational negated() {
        return new BigRational(this.numerator.negate(), this.denominator);
    }

    @Override
    public BigRational abs() {
        return new BigRational(this.numerator.abs(), this.denominator);
    }

    @Override
    public int compareTo(BigRational o) {
        //引き算で表現する
        return this.minus(o).numerator.compareTo(BigInteger.ZERO);
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
    public static BigRational of(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("分母が0である");
        }
        BigInteger gcd = numerator.gcd(denominator);
        if (denominator.compareTo(BigInteger.ZERO) < 0) {
            gcd = gcd.negate();
        }
        return new BigRational(numerator.divide(gcd), denominator.divide(gcd));
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
    public static BigRational of(long numerator, long denominator) {
        return BigRational.of(
                BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    /**
     * このクラスと関連する定数サプライヤを返す.
     * 
     * @return 定数サプライヤ.
     */
    public static MathField.ConstantSupplier<BigRational> constantSupplier() {
        return BigRational.ConstantSupplier.INSTANCE;
    }

    /**
     * {@link BigRational} と関連する定数サプライヤ.
     */
    private static final class ConstantSupplier
            implements MathField.ConstantSupplier<BigRational> {

        static final ConstantSupplier INSTANCE = new ConstantSupplier();

        ConstantSupplier() {
        }

        @Override
        public BigRational zero() {
            return BigRational.ZERO;
        }

        @Override
        public BigRational one() {
            return BigRational.ONE;
        }
    }
}
