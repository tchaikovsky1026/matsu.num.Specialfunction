package matsu.num.specialfunction.bessel.modbessel.subpj.kpower;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.bessel.modbessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * K1の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * K1 = (1/x) +(1/2)(x/2)log(u)F(u) - (x/2)G(u) <br>
 * の形 (F, Gはべき級数) に分解したときの, <br>
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} (G_m-gamma)u^m / (m!(m+1)!)
 * = 2/(x^2) + (1/x)log(u)*I1(x) - (2/x)K1(x)
 * </p>
 * 
 * <p>
 * G(u)の第3次近似をg(u)とする. <br>
 * g(u) = 1/2-gamma + (5/4-gamma)*u/(1*2) + (5/3-gamma)*u^2/(2*6) +
 * (47/24-gamma)u^3/(6*24) <br>
 * その残差: <br>
 * G(u) - g(u) =
 * Sum_{m=4 to inf} u^m (G_m-gamma)/ (m!(m+1)!) <br>
 * を提供する. <br>
 * スケールは(1/x)である.
 * </p>
 * 
 * <p>
 * 参考: <br>
 * F(u) = Sum_{m=0 to inf} u^m / (m!(m+1)!) <br>
 * G(u) = Sum_{m=0 to inf} u^m (G_m-gamma)/(m!(m+1)!)
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK1Power_regularTerm implements RawCoefficientCalculableFunction {

    private static final double GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;

    private static final double MIN_U = 0d;
    private static final double MAX_U = 1d;

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

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
            1d / (1 * 1) * (PSEUDO_HARMONIC_NUMBERS[0] - GAMMA), // (G_0 - gamma) / (0! * 1!)
            1d / (1 * 2) * (PSEUDO_HARMONIC_NUMBERS[1] - GAMMA), // (G_1 - gamma) / (1! * 2!)
            1d / (2 * 6) * (PSEUDO_HARMONIC_NUMBERS[2] - GAMMA), // (G_2 - gamma) / (2! * 3!)
            1d / (6 * 24) * (PSEUDO_HARMONIC_NUMBERS[3] - GAMMA) // (G_3 - gamma) / (3! * 4!)
    };

    private final DoubleFiniteClosedInterval interval;

    public BesselK1Power_regularTerm(DoubleFiniteClosedInterval interval) {
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
            value *= u / (k * (k + 1));
            value += PSEUDO_HARMONIC_NUMBERS[k - 1] - GAMMA;
        }

        return value * Exponentiation.pow(u, 4) / (24 * 120);
    }

    @Override
    public double scale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 0.5 / Exponentiation.sqrt(SCALE_U_THRESHOLD);
        }
        return 0.5 / Exponentiation.sqrt(u);
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
