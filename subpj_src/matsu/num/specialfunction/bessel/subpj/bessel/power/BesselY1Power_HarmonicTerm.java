package matsu.num.specialfunction.bessel.subpj.bessel.power;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.bessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * Y1の調和数部分の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * Y1 = -2/pi * ((x/2)G(u)-(gamma+log(x/2))J1(x) + 1/x) <br>
 * の形に分解したときの, <br>
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} (-1)^m (G_m)u^m / (m!(m+1)!) <br>
 * 
 * </p>
 * 
 * <p>
 * G(u)の第3次近似をg(u)とする. <br>
 * g(u) = G_0/(0!1!) - G_1/(1!2!) * u + G_2/(2!3!) * u^2 - G_3/(3!4!) * u^3 <br>
 * その残差: <br>
 * G(u) - g(u) =
 * Sum_{m=4 to inf} (-1)^m*G_m/(m!(m+1)!) * u^m <br>
 * を提供する. <br>
 * スケールは1とする: 過剰.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselY1Power_HarmonicTerm implements RawCoefficientCalculableFunction {

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 30;

    /**
     * 
     * [G_0, ..., G_kMax], <br>
     * ただし, G_k = (1/2)(H_k + H_{k+1}) <br>
     * Kのべき級数の計算で使用する.
     */
    private static final double[] PSEUDO_HARMONIC_NUMBERS =
            calcPseudoHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    private static final double[] ESTIMATED_COEFF = {
            1d / (1 * 1) * PSEUDO_HARMONIC_NUMBERS[0], // G_0/(0!1!)
            -1d / (1 * 2) * PSEUDO_HARMONIC_NUMBERS[1], // -G_1/(1!2!)
            1d / (2 * 6) * PSEUDO_HARMONIC_NUMBERS[2], // G_2/(2!3!)
            -1d / (6 * 24) * PSEUDO_HARMONIC_NUMBERS[3] // -G_3/(3!4!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselY1Power_HarmonicTerm(DoubleFiniteClosedInterval interval) {
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
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 5; k--) {
            value *= -u / (k * (k + 1));
            value += PSEUDO_HARMONIC_NUMBERS[k - 1];
        }

        return value * Exponentiation.pow(u, 4) / (24 * 120);
    }

    @Override
    public double scale(double u) {
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
     * [G_0, ..., G_kMax] を返す.
     * ただし, G_k = (1/2)(H_k + H_{k+1})
     */
    private static double[] calcPseudoHarmonicNumbers(int kMax) {
        final double[] harmonicNumbers = new double[kMax + 1];

        double current = 0.5;
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current += 0.5 * (1d / (k + 1) + 1d / (k + 2));
        }

        return harmonicNumbers;
    }
}
