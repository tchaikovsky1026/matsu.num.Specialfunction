package matsu.num.specialfunction.subpj.bessel.modbessel.asymptotic;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * K0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} の一部を引数とする形で計算する.
 * </p>
 * 
 * <p>
 * K0(x) = sqrt(pi/(2x)) * exp(-x) * F(t) <br>
 * として, F(t) を推定する. <br>
 * スケールは1とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK0Asymptotic extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_T = 0d;
    private static final double UPPER_LIMIT_OF_T = 1d / 16;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("K0(x) について,");
        System.out.println("t = 1/(8x), K0(x) = sqrt(pi/(2x)) * exp(-x) * F(t) としたときの");
        System.out.println("F(t)の推定");
        System.out.println();

        executeEach(10, 1d / 64, 2d / 64);
        executeEach(11, 2d / 64, 4d / 64);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double t1, double t2) {
        new EachApproxExecutor(order).execute(new BesselK0Asymptotic(t1, t2));
    }

    private BesselK0Asymptotic(double t1, double t2) {
        super();

        if (!(LOWER_LIMIT_OF_T <= t1
                && t1 <= UPPER_LIMIT_OF_T
                && LOWER_LIMIT_OF_T <= t2
                && t2 <= UPPER_LIMIT_OF_T)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.interval = INTERVAL_FACTORY.createInterval(t1, t2);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement t) {
        return K0PrimeAsymptoticTermCalc.calc(t)
                .times(t)
                .plus(PROVIDER.one());
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement t) {
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
}
