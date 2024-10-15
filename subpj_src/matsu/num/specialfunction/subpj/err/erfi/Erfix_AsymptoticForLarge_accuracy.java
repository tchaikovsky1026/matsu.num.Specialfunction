package matsu.num.specialfunction.subpj.err.erfi;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * erfix(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1/8} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfix(x) = 1/(x*sqrt(pi)) *
 * sum_{k=0}^{inf} (2k-1)!! / 2^k * t^{2k} <br>
 * そこで, u = t^2 として, <br>
 * erfix(x) = 1/(x*sqrt(pi)) * F(u) <br>
 * としたときの, 多項式 F(u) を近似する. <br>
 * F(u) = sum_{k=0}^{inf} (2k-1)!! / 2^k * u^k
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * 
 * <hr>
 * 
 * <p>
 * 値の計算について
 * </p>
 * 
 * <p>
 * F'(u) は漸近級数であり, 連分数にも変換できない. <br>
 * そこで各項の大きさを考慮し, tを1/8以下に限定して級数の上限を60とする. <br>
 * 倍精度はこれで十分.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erfix_AsymptoticForLarge_accuracy extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double UPPER_LIMIT_OF_T_MAX = 1d / 8;

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX = 60;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    /**
     * {@link Erfix_AsymptoticForLarge_accuracy} を用いて近似を実行する.
     * 
     * @param args
     */
    public static void main(String[] args) {

        int order = 7;
        double tmax = 1d / 8;

        System.out.println("tmax = " + tmax);
        new EachApproxExecutor(order).execute(new Erfix_AsymptoticForLarge_accuracy(tmax));

        System.out.println("finished...");
    }

    private Erfix_AsymptoticForLarge_accuracy(double tmax) {
        super();

        if (!(0 < tmax
                && tmax <= UPPER_LIMIT_OF_T_MAX)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.interval = INTERVAL_FACTORY.createInterval(0d, tmax * tmax);
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * F'(u) = sum_{k=1}^{kMax} (2k-1)!! / 2^k * u^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 2; k--) {
            value = value.times(u).times(2 * k - 1).dividedBy(2);
            value = value.plus(PROVIDER.one());
        }

        return value.dividedBy(2);
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
