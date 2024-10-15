package matsu.num.specialfunction.subpj.bessel.modbessel.asymptotic;

import java.util.function.IntFunction;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * <p>
 * K1の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} を引数とする形で計算する. <br>
 * {@literal t -> 0} で厳密に一致するようにしたパターン.
 * </p>
 * 
 * <p>
 * K1(x) = sqrt(pi/(2x)) * exp(-x) * F(t) <br>
 * として, F(t) を推定する. <br>
 * ただし, F(t) = 1 + tF'(t) として, F'(t)を推定する.
 * F(1)からF(1/16)までで2割程度しか変化しない. <br>
 * よって, F'(t)の推定において, スケールは 1/t とする.
 * </p>
 * 
 * <p>
 * F'(t)の第2次近似をf'(t)とする. <br>
 * f'(t) = -((1^2-4) /1!) + ((1^2-4)(3^2-4) /2!)t
 * - ((1^2-4)(3^2-4)(5^2-4) /3!)t^2
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK1Asymptotic_accurate extends RawCoeffCalculableDoubleFunction {

    private static final DoubleContinuedFractionFunction K1_UPPER4 =
            k1_upper4().asDoubleFunction();

    private static final double MIN_T = 0d;
    private static final double MAX_T = 1d / 16;

    private static final double SCALE_T_THRESHOLD = 1d / 1024;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            -(1 * 1 - 4) / 1d,
            (1 * 1 - 4) * (3 * 3 - 4) / 2d,
            -(1 * 1 - 4) * (3 * 3 - 4) * (5 * 5 - 4) / 6d
    };

    public BesselK1Asymptotic_accurate(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_T <= interval.lower() &&
                interval.upper() <= MAX_T)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    protected double calcValue(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        double v = K1_UPPER4.value(t);
        v *= 1 * 1 - 4;
        v *= 3 * 3 - 4;
        v *= 5 * 5 - 4;
        v *= 7 * 7 - 4;
        v /= 24;
        v *= Exponentiation.pow(t, 3);

        return v;
    }

    @Override
    protected double calcScale(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        if (t < SCALE_T_THRESHOLD) {
            return 1 / SCALE_T_THRESHOLD;
        }

        return 1 / t;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F'(t) - f'(t)] の近似多項式の係数を与え,
     * F(t) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F'(t) - f'(t)]の係数
     * @return F(t)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffFp = thisCoeff.clone();
        for (int i = 0;
                i < coeffFp.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffFp[i] += ESTIMATED_COEFF[i];
        }

        double[] coeffF = new double[coeffFp.length + 1];
        coeffF[0] = 1d;
        System.arraycopy(coeffFp, 0, coeffF, 1, coeffFp.length);

        return coeffF;
    }

    /**
     * K1の漸近連分数F(t)に関し, F'(t) = (F(t)-1)/t とするときの,
     * {@literal m >= 4}でのもの(つまり3次以降). <br>
     * 3次が定数項1に対応するため, 実際は全体に, <br>
     * ((1^2-4) * (3^2-4) * (5^2-4 * (7^2-4) /4!)t^3
     * <br>
     * が乗算される.
     */
    private static ContinuedFractionFunction<BigRational> k1_upper4() {

        final int kMax = 60;

        IntFunction<BigRational> func =
                k -> BigRational.of(-((2 * k + 9) * (2 * k + 9) - 4), k + 5);

        return ContinuedFractionFunction.from(
                kMax, func,
                BigRational.constantSupplier());
    }

}
