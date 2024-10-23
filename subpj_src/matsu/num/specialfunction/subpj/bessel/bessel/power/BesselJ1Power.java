package matsu.num.specialfunction.subpj.bessel.bessel.power;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * J1の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する. <br>
 * F(u) = J1(x)/(x/2) = sum_{m=0 to inf} (-1)^{m} / (m!(m+1)!) * u^m
 * </p>
 * 
 * <p>
 * 第0近似を厳密にしたい. <br>
 * そこで, F'(u) = (F(u)-1)/u を多項式近似する. <br>
 * F'(u) = sum_{m=1 to inf} (-1)^{m} / (m!(m+1)!) * u^{m-1} <br>
 * スケールは(1/u)である.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselJ1Power extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double U_MIN = 0d;
    private static final double U_MAX = 1d;

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 40;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("J1(x) について,");
        System.out.println("u = (x/2)^2, F(u) = J1(x)/(x/2) としたときの,");
        System.out.println("多項式近似を扱う.");
        System.out.println();

        int order = 7;

        System.out.println("umin = " + U_MIN);
        System.out.println("umax = " + U_MAX);
        new EachApproxExecutor(order).execute(new BesselJ1Power());

        System.out.println("finished...");
    }

    private BesselJ1Power() {
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
         * F'(u) = sum_{m=1 to inf} (-1)^{m} / (m!(m+1)!) * u^{m-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 2; k--) {
            value = value.times(u)
                    .dividedBy(k)
                    .dividedBy(k + 1)
                    .negated();
            value = value.plus(PROVIDER.one());
        }

        return value.negated().dividedBy(2d);
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {
        if (u.compareTo(SCALE_U_THRESHOLD) <= 0) {
            return PROVIDER.one().dividedBy(SCALE_U_THRESHOLD);
        }
        return PROVIDER.one().dividedBy(u);
    }

    @Override
    public FiniteClosedInterval<DoubleDoubleFloatElement> interval() {
        return this.interval;
    }

    @Override
    public DoubleDoubleFloatElement[] rawCoeff(DoubleDoubleFloatElement[] thisCoeff) {

        DoubleDoubleFloatElement[] coeffF = new DoubleDoubleFloatElement[thisCoeff.length + 1];
        coeffF[0] = PROVIDER.one();
        System.arraycopy(thisCoeff, 0, coeffF, 1, thisCoeff.length);

        return coeffF;
    }
}
