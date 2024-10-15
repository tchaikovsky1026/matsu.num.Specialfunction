package matsu.num.specialfunction.subpj.err.erf;

import java.util.Objects;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * 誤差関数erf(x)をminimax近似するための関数と, その近似の実行. <br>
 * {@literal 0 <= x <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erf(x) = 2/sqrt(pi) * sum_{k=0}^{inf}
 * (-1)^k / (k! * (2k+1)) * x^{2k+1} <br>
 * そこで, u = x^2 として, <br>
 * erf(x) = 2/sqrt(pi) * x F(u) <br>
 * としたときの, 多項式 F(u) を近似する. <br>
 * F(u) = sum_{k=0}^{inf} (-1)^k / (k! * (2k+1)) * u^k
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erf_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final DoubleDoubleFloatElement LOWER_LIMIT_OF_INTERVAL =
            PROVIDER.zero();
    private static final DoubleDoubleFloatElement UPPER_LIMIT_OF_INTERVAL =
            PROVIDER.one();

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX = 100;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    /**
     * {@link Erf_TaylorForSmall} を用いて近似を実行する.
     * 
     * @param args
     */
    public static void main(String[] args) {

        int order = 10;

        new EachApproxExecutor(order).execute(
                new Erf_TaylorForSmall(INTERVAL_FACTORY.createInterval(0d, 1d)));

        System.out.println("finished...");
    }

    private Erf_TaylorForSmall(FiniteClosedInterval<DoubleDoubleFloatElement> interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(interval.lower().compareTo(LOWER_LIMIT_OF_INTERVAL) >= 0
                && interval.upper().compareTo(UPPER_LIMIT_OF_INTERVAL) <= 0)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return DoubleDoubleFloatElement.elementProvider();
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * F'(u) =
         * sum_{k=1}^{inf} (-1)^k / (k! * (2k+1)) * u^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(
                    u.negated().dividedBy(k + 1));
            value = value.plus(
                    PROVIDER.one().dividedBy(2 * k + 1));
        }

        return value.negated();
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
