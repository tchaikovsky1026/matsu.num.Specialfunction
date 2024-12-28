package matsu.num.specialfunction.subpj.gamma.trigamma;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulliByDoubleDoubleFloat;

/**
 * トリガンマ関数 psi1(x) に対するStirling近似の残差をminimax近似する. <br>
 * {@literal 10 <= x} を扱う.
 * 
 * <p>
 * 漸近展開は, <br>
 * psi(x) = 1/x + 1/(2x^2) + sum_{k=1}^{inf} B_{2k} * x^{-2k-1} <br>
 * Stirling近似の残差とは総和部分であり, t = 1/x として, <br>
 * R(t) = sum_{k=1}^{inf} B_{2k} * t^{2k+1} <br>
 * u = t＾2 の 関数と見て, <br>
 * R(t) = t^3 * G(u), <br>
 * G(u) = sum_{k=1}^{inf} B_{2k} * u^{k-1} <br>
 * としたときのG(u)の近似を考える. <br>
 * スケールは, 1/(6u) とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class TrigammaRes_AsymptoticForLarge_accuracy
        extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    /**
     * 厳密な0.1よりはわずかに大きい.
     */
    private static final double T_MAX = Math.nextUp(0.1d);

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("f(x) = psi1(x) - 1/x - 1/(2x^2) について,");
        System.out.println("t = 1/x としたとき,");
        System.out.println("x^3 * f(x) を u = t^2 の多項式で近似する.");
        System.out.println();

        int order = 5;

        System.out.println("tmax = " + T_MAX);
        new EachApproxExecutor(order).execute(
                new TrigammaRes_AsymptoticForLarge_accuracy());

        System.out.println("finished...");
    }

    private TrigammaRes_AsymptoticForLarge_accuracy() {

        this.interval = INTERVAL_FACTORY.createInterval(0d, T_MAX * T_MAX);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * G(u) = sum_{k=1}^{inf} B_{2k} * u^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value = value.times(u);
            DoubleDoubleFloatElement c =
                    EvenBernoulliByDoubleDoubleFloat.evenBernoulli(k);
            value = value.plus(c);
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {
        if (u.compareTo(SCALE_U_THRESHOLD) <= 0) {
            return PROVIDER.one().dividedBy(SCALE_U_THRESHOLD.times(6));
        }
        return PROVIDER.one().dividedBy(u.times(6));
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
