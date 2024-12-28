package matsu.num.specialfunction.subpj.err.erfi;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * erfi(x)のminimax近似を行うための関数と, 近似の実行. <br>
 * {@literal 0 <= x <= 1} を扱う.
 * 
 * <p>
 * erfi(x) = 2/sqrt(pi) * x * F(u) <br>
 * F(u) = sum_{k=0}^{inf} 1/(k!(2k+1)) * u^k, <br>
 * ただし, u = x^2 とする. <br>
 * F(u) を近似する.
 * </p>
 * 
 * <p>
 * F(u) をu=0で厳密にしたいので, <br>
 * F'(u) = (F(u)-1) / u <br>
 * に対して多項式近似を行う. <br>
 * スケールは1/uである.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erfi_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double X_MIN = 0d;
    private static final double X_MAX = 1d;

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX = 100;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    /**
     * {@link Erfi_TaylorForSmall} を用いて近似を実行する.
     * 
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("erfi(x) = 2/sqrt(pi) * x * F(u), u = x^2 としたときの,");
        System.out.println("F(u)を近似する.");
        System.out.println();

        System.out.println("xmin = " + X_MIN);
        System.out.println("xmax = " + X_MAX);
        new EachApproxExecutor(10).execute(new Erfi_TaylorForSmall());

        System.out.println("finished...");
    }

    private Erfi_TaylorForSmall() {
        this.interval = INTERVAL_FACTORY.createInterval(X_MIN, X_MAX);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * F'(u) =
         * sum_{k=1}^{inf} 1/(k!(2k+1)) * u^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(
                    u.dividedBy(k + 1));
            value = value.plus(
                    PROVIDER.one().dividedBy(2 * k + 1));
        }

        return value;
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
