package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * K1の調和数部分の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する. <br>
 * K1(x) = 1/x + (gamma+log(x/2))I1(x) - (x/2)G(u) <br>
 * の形に分解したときの, <br>
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} G_m / (m!(m+1)!) * u^m <br>
 * スケールは 1 とする (過剰).
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK1Power_HarmonicTerm extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double U_MIN = 0d;
    private static final double U_MAX = 1d;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 40;

    /**
     * [G_0, ..., G_kMax],
     * Kのべき級数の計算で使用する.
     */
    private static final DoubleDoubleFloatElement[] PSEUDO_HARMONIC_NUMBERS =
            calcPseudoHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("K1(x) について,");
        System.out.println("u = (x/2)^2, K1(x) = 1/x + (gamma+log(x/2))I1(x) - (x/2)G(u) としたときの,");
        System.out.println("G(u) に対する多項式近似を扱う.");
        System.out.println();

        int order = 8;

        System.out.println("umin = " + U_MIN);
        System.out.println("umax = " + U_MAX);
        new EachApproxExecutor(order).execute(new BesselK1Power_HarmonicTerm());

        System.out.println("finished...");
    }

    private BesselK1Power_HarmonicTerm() {
        super();
        this.interval = INTERVAL_FACTORY.createInterval(U_MIN, U_MAX);
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * G(u) = Sum_{m=0 to inf} G_m / (m!(m+1)!) * u^m
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 1; k--) {
            value = value.times(u)
                    .dividedBy(k)
                    .dividedBy(k + 1);
            value = value.plus(PSEUDO_HARMONIC_NUMBERS[k - 1]);
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {
        return PROVIDER.one();
    }

    @Override
    public FiniteClosedInterval<DoubleDoubleFloatElement> interval() {
        return this.interval;
    }

    @Override
    public DoubleDoubleFloatElement[] rawCoeff(DoubleDoubleFloatElement[] thisCoeff) {
        return thisCoeff.clone();
    }

    /**
     * [G_0, ..., G_kMax] を返す.
     * ただし, G_k = (1/2)(H_k + H_{k+1})
     */
    private static DoubleDoubleFloatElement[] calcPseudoHarmonicNumbers(int kMax) {
        final DoubleDoubleFloatElement[] harmonicNumbers = new DoubleDoubleFloatElement[kMax + 1];

        DoubleDoubleFloatElement current = PROVIDER.fromDoubleValue(0.5);
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current = current.plus(
                    PROVIDER.fromDoubleValue(0.5)
                            .times(
                                    PROVIDER.one().dividedBy(k + 1)
                                            .plus(PROVIDER.one().dividedBy(k + 2))));
        }

        return harmonicNumbers;
    }
}