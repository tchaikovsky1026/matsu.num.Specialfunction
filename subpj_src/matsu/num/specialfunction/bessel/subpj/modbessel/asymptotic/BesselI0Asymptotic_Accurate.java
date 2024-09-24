package matsu.num.specialfunction.bessel.subpj.modbessel.asymptotic;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * I0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/192} を引数とする形で計算する. <br>
 * {@literal t -> 0} で厳密に一致するようにしたパターン.
 * </p>
 * 
 * <p>
 * I(x) = sqrt(pi/(2x)) * exp(-x) * F(t) <br>
 * として, F(t) を推定する. <br>
 * ただし, 0次近似を厳密にするため, <br>
 * F'(t) = (F(t) - 1) / t として, F'(t)を推定する. <br>
 * F'(t) = (1^2) / 1! + (1^2 * 3^2) / 2! * t + ... <br>
 * スケールは 1/t とする.
 * </p>
 * 
 * <p>
 * F'(t)の第2次近似をf'(t)とする. <br>
 * f'(t) = (1^2) / 1! + (1^2 * 3^2) / 2! * t
 * + (1^2 * 3^2 * 5^2) / 3! * t^2 <br>
 * F'(t) - f'(t) = (1^2 * 3^2 * 5^2 * 7^2) / 4! * t^3
 * </p>
 * 
 * 
 * @author Matsuura Y.
 */
final class BesselI0Asymptotic_Accurate extends RawCoefficientCalculableFunction {

    private static final double MIN_T = 0d;
    private static final double MAX_T = 1d / 192;

    private static final double SCALE_T_THRESHOLD = 1d / 1024;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 20;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1 / 1d, // 1^2 / 1!
            9 / 2d, // (1^2 * 3^2) / 2!
            9 * 25 / 6d, // (1^2 * 3^2 * 5^2) / 3!
    };

    public BesselI0Asymptotic_Accurate(DoubleFiniteClosedInterval interval) {
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

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 5; k--) {
            value *= t * ((2 * k - 1) * (2 * k - 1)) / k;
            value += 1;
        }

        return value * Exponentiation.pow(t, 3) * (1 * 9 * 25 * 49) / 24;
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
}
