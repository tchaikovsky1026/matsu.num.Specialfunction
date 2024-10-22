package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * Q0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} を引数とする形で計算する. <br>
 * J0(x) = sqrt(2/(pi*x)) * (P0(t)cos + Q0(t)sin) <br>
 * Y0(x) = sqrt(2/(pi*x)) * (P0(t)sin - Q0(t)cos) <br>
 * として, P0(t) を推定する. <br>
 * (P0(x)^2 + Q0(x)^2) は x が0から1/16までで2割程度しか変化しない. <br>
 * よって, スケールは1とする.
 * </p>
 * 
 * <p>
 * P0はtの1次, 3次, ... しかないが, 偶数次も含むとして推定する.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselQ0Asymptotic extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_T = 0d;
    private static final double UPPER_LIMIT_OF_T = 1d / 16;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("Q0(x) について,");
        System.out.println("t = 1/(8x) としたときの展開");
        System.out.println();

        executeEach(9, 0, 1d / 128);
        executeEach(9, 1d / 128, 2d / 128);
        executeEach(11, 2d / 128, 4d / 128);
        executeEach(10, 4d / 128, 6d / 128);
        executeEach(9, 6d / 128, 8d / 128);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double t1, double t2) {
        new EachApproxExecutor(order).execute(new BesselQ0Asymptotic(t1, t2));
    }

    private BesselQ0Asymptotic(double t1, double t2) {
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
        return ContinuedFractionFactoryOfH0Asymptotic.Q0.apply(t);
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
