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
 * @deprecated このモジュールで取り扱う有理数はlong型では十分でないので,
 *                 このクラスは不使用である.
 */
@Deprecated
public final class LongRational
        extends MathField<LongRational> {

    private static final LongRational ZERO =
            new LongRational(0, 1);
    private static final LongRational ONE =
            new LongRational(1, 1);

    private final long numerator;
    private final long denominator;

    /**
     * このコンストラクタは約分をしない.
     * 約分される必要がある場合, staticファクトリを呼ぶ.
     * 
     * @param numerator
     * @param denominator
     */
    private LongRational(long numerator, long denominator) {
        super(DoubleInterpretable.INSTANCE);
        this.numerator = numerator;
        this.denominator = denominator;
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
        if (!(obj instanceof LongRational target)) {
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
    public LongRational plus(LongRational augend) {
        return this.plus_kernel(augend.numerator, augend.denominator);
    }

    @Override
    public LongRational minus(LongRational subtrahend) {
        return this.plus_kernel(-subtrahend.numerator, subtrahend.denominator);
    }

    private LongRational plus_kernel(long otherNume, long otherDenomi) {
        long gcd = LongGCD.gcd(this.denominator, otherDenomi);

        long thisDenomi_dividedByGcd = this.denominator / gcd;
        long otherDenomi_dividedByGcd = otherDenomi / gcd;

        long denomi2 = this.denominator * otherDenomi_dividedByGcd;
        long nume2 = this.numerator * otherDenomi_dividedByGcd;
        nume2 += thisDenomi_dividedByGcd * otherNume;

        // 足し算の結果発生する約分を処理するため, staticファクトリを呼ぶ
        return LongRational.of(nume2, denomi2);
    }

    @Override
    public LongRational times(LongRational multiplicand) {
        return this.times_kernel(multiplicand.numerator, multiplicand.denominator);
    }

    @Override
    public LongRational dividedBy(LongRational divisor) {
        return this.times_kernel(divisor.denominator, divisor.numerator);
    }

    private LongRational times_kernel(long otherNume, long otherDenomi) {
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

        return new LongRational(nume2, denomi2);
    }

    @Override
    public LongRational negated() {
        return new LongRational(-this.numerator, this.denominator);
    }

    @Override
    protected double toDouble() {
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
    public static LongRational of(long numerator, long denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("分母が0である");
        }
        long gcd = LongGCD.gcd(numerator, denominator);
        if (denominator < 0) {
            gcd = -gcd;
        }
        return new LongRational(numerator / gcd, denominator / gcd);
    }

    /**
     * {@code long} 型有理数構造の定数サプライヤ.
     * 
     * @author Matsuura Y.
     */
    public static final class ConstantSupplier implements MathField.ConstantSupplier<LongRational> {

        public static final ConstantSupplier INSTANCE = new ConstantSupplier();

        @Override
        public LongRational zero() {
            return LongRational.ZERO;
        }

        @Override
        public LongRational one() {
            return LongRational.ONE;
        }
    }
}
