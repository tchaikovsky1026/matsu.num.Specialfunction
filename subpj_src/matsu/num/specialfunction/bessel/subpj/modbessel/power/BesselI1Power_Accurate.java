package matsu.num.specialfunction.bessel.subpj.modbessel.power;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.bessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * I1の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * I1(x) = (x/2)F(u) としてF(u)を推定する. <br>
 * F(u) = Sum_{m=0 to inf} 1 / (m!(m+1)!) * u^m
 * </p>
 * 
 * <p>
 * 第0近似を厳密にしたい. <br>
 * そこで, F'(u) = (F(u)-1)/u を多項式近似する. <br>
 * F'(u)の第2次近似をf(u)とする. <br>
 * f'(u) = 1/(1*2) + 1/(2*6) * u + 1/(6*24) * u^2 <br>
 * その残差: <br>
 * F'(u) - f'(u) =
 * Sum_{m=3 to inf} u^m / ((m+1)!(m+2)!) <br>
 * を提供する. <br>
 * スケールは(1/u)である.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselI1Power_Accurate implements RawCoefficientCalculableFunction {

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 50;

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 2), // 1/(1!2!)
            1d / (2 * 6), // 1/(2!3!)
            1d / (6 * 24), // 1/(3!4!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselI1Power_Accurate(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_U <= interval.lower() &&
                interval.upper() <= MAX_U)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    public double value(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 4; k--) {
            value *= u / ((k + 1) * (k + 2));
            value += 1;
        }

        return value * Exponentiation.pow(u, 3) / (24 * 120);
    }

    @Override
    public double scale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 1 / SCALE_U_THRESHOLD;
        }
        return 1 / u;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F'(u) - f'(u)] の近似多項式の係数を与え,
     * F(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F'(u) - f'(u)]の係数
     * @return F(u)の係数
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
