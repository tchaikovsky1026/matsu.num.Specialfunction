package matsu.num.specialfunction.bessel.subpj.modbessel.power;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * K0の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * K0(x) = -(gamma + log(x/2))F(u) + G(u) <br>
 * に分解したときの, <br>
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} H_m/(m!)^2 * u^m
 * </p>
 * 
 * <p>
 * G(u)の第3次近似をg(u)とする. <br>
 * g(u) = H_0/(0!)^2 + H_1/(1!)^2 * u + H_2/(2!)^2 * u^2 + H_3/(3!)^2 * u^3 <br>
 * その残差: <br>
 * G(u) - g(u) =
 * Sum_{m=4 to inf} H_m/(m!)^2 * u^m <br>
 * を提供する. <br>
 * スケールは1とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK0Power_HarmonicTerm extends RawCoefficientCalculableFunction {

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 30;

    /**
     * [H_0, ..., H_kMax],
     * Kのべき級数の計算で使用する.
     */
    private static final double[] HARMONIC_NUMBERS =
            calcHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 1) * HARMONIC_NUMBERS[0], // H_0/(0!)^2
            1d / (1 * 1) * HARMONIC_NUMBERS[1], // H_1/(1!)^2
            1d / (2 * 2) * HARMONIC_NUMBERS[2], // H_2/(2!)^2
            1d / (6 * 6) * HARMONIC_NUMBERS[3] // H_3/(3!)^2
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselK0Power_HarmonicTerm(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_U <= interval.lower() &&
                interval.upper() <= MAX_U)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 5; k--) {
            value *= u / (k * k);
            value += HARMONIC_NUMBERS[k - 1];
        }

        return value * Exponentiation.pow(u, 4) / (24 * 24);
    }

    @Override
    protected double calcScale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        return 1d;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [G(u) - g(u)] の近似多項式の係数を与え,
     * G(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [G(u) - g(u)]の係数
     * @return G(u)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffF = thisCoeff.clone();
        for (int i = 0;
                i < coeffF.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffF[i] += ESTIMATED_COEFF[i];
        }
        return coeffF;
    }

    /**
     * [H_0, ..., H_kMax] を返す.
     */
    private static double[] calcHarmonicNumbers(int kMax) {
        final double[] harmonicNumbers = new double[kMax + 1];

        double current = 0;
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current += 1d / (k + 1);
        }

        return harmonicNumbers;
    }
}
