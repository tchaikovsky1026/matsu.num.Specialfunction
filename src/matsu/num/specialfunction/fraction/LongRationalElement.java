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

/**
 * {@code long} 型で分子分母が表現された有理数構造の元.
 * 
 * <p>
 * 分母は必ず正の数により表現される.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.5
 */
public final class LongRationalElement
        implements MathFieldElement<RationalType, LongRationalElement> {

    private static final LongRationalElement ZERO =
            new LongRationalElement(0, 1);
    private static final LongRationalElement ONE =
            new LongRationalElement(1, 1);

    private static final RationalType TYPE = RationalType.INSTANCE;

    private final long numerator;
    private final long denominator;

    /**
     * このコンストラクタは約分をしない.
     * 約分される必要がある場合, staticファクトリを呼ぶ.
     * 
     * @param numerator
     * @param denominator
     */
    private LongRationalElement(long numerator, long denominator) {
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
    public long numerator() {
        return this.numerator;
    }

    /**
     * 分母を返す.
     * 
     * @return 分母
     */
    public long denominator() {
        return this.denominator;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LongRationalElement target)) {
            return false;
        }

        return this.numerator == target.numerator
                && this.denominator == target.denominator;
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(this.numerator);
        result = 31 * result + Long.hashCode(this.denominator);

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s / %s", this.numerator, this.denominator);
    }

    @Override
    public LongRationalElement plus(LongRationalElement augend) {
        return this.plus_kernel(augend.numerator, augend.denominator);
    }

    @Override
    public LongRationalElement minus(LongRationalElement subtrahend) {
        return this.plus_kernel(-subtrahend.numerator, subtrahend.denominator);
    }

    private LongRationalElement plus_kernel(long otherNume, long otherDenomi) {
        long gcd = LongGCD.gcd(this.denominator, otherDenomi);

        long thisDenomi_dividedByGcd = this.denominator / gcd;
        long otherDenomi_dividedByGcd = otherDenomi / gcd;

        long denomi2 = this.denominator * otherDenomi_dividedByGcd;
        long nume2 = this.numerator * otherDenomi_dividedByGcd;
        nume2 += thisDenomi_dividedByGcd * otherNume;

        // 足し算の結果発生する約分を処理するため, staticファクトリを呼ぶ
        return LongRationalElement.of(nume2, denomi2);
    }

    @Override
    public LongRationalElement times(LongRationalElement multiplicand) {
        return this.times_kernel(multiplicand.numerator, multiplicand.denominator);
    }

    @Override
    public LongRationalElement dividedBy(LongRationalElement divisor) {
        return this.times_kernel(divisor.denominator, divisor.numerator);
    }

    private LongRationalElement times_kernel(long otherNume, long otherDenomi) {
        if (otherDenomi == 0) {
            throw new ArithmeticException("ゼロ除算");
        }

        //otherDenomiは負の可能性がある
        long gcd_thisNume_to_otherDenomi = LongGCD.gcd(this.numerator, otherDenomi);
        if (otherDenomi < 0) {
            gcd_thisNume_to_otherDenomi = -gcd_thisNume_to_otherDenomi;
        }
        long thisNume2 = this.numerator / gcd_thisNume_to_otherDenomi;
        long otherDenomi2 = otherDenomi / gcd_thisNume_to_otherDenomi;

        long gcd_thisDenomi_to_otherNume = LongGCD.gcd(this.denominator, otherNume);
        long thisDenomi2 = this.denominator / gcd_thisDenomi_to_otherNume;
        long otherNume2 = otherNume / gcd_thisDenomi_to_otherNume;

        long denomi2 = thisDenomi2 * otherDenomi2;
        long nume2 = thisNume2 * otherNume2;

        return new LongRationalElement(nume2, denomi2);
    }

    @Override
    public LongRationalElement negated() {
        return new LongRationalElement(-this.numerator, this.denominator);
    }

    @Override
    public double doubleValue() {
        return ((double) this.numerator) / this.denominator;
    }

    /**
     * 与えられた {@code long} 型の分子, 分母を持つ有理数を返す.
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
    public static LongRationalElement of(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("分母が0である");
        }
        long gcd = LongGCD.gcd(numerator, denominator);
        if (denominator < 0) {
            gcd = -gcd;
        }
        return new LongRationalElement(numerator / gcd, denominator / gcd);
    }

    /**
     * {@code long} 型有理数構造の定数サプライヤ.
     * 
     * @author Matsuura Y.
     */
    public static final class ConstantSupplier implements MathFieldElement.ConstantSupplier<LongRationalElement> {

        public static final ConstantSupplier INSTANCE = new ConstantSupplier();

        @Override
        public LongRationalElement zero() {
            return LongRationalElement.ZERO;
        }

        @Override
        public LongRationalElement one() {
            return LongRationalElement.ONE;
        }
    }
}
