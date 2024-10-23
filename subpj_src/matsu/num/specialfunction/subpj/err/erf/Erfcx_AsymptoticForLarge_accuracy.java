package matsu.num.specialfunction.subpj.err.erf;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * スケーリング相補誤差関数erfcx(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1/8} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfcx(x) = 1/(x*sqrt(pi)) * F(u), <br>
 * F(u) = sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * u^{k} <br>
 * ただし, u = t^2 とする. <br>
 * F(u) の近似を扱う.
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * F'(u) = sum_{k=1}^{inf} (-1)^k * (2k-1)!! / 2^k * u^{k-1} <br>
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
 * F'(u)は漸近級数であり, 収束しない. <br>
 * そこで, 連分数に変形する. <br>
 * F'(u) = -1/2 * G(u) <br>
 * とすると, <br>
 * G(u) = 1 - c_1*u + c_1*c_2*u^2 - ... <br>
 * の形をしており, <br>
 * c_1 = 3/2, c_2 = 5/2, ... <br>
 * である. これを商差法により連分数に変換すれば, 最上段の係数1を除いて, <br>
 * a_1 = 3/2, a_2 = 1, <br>
 * a_3 = 5/2, a_4 = 2, <br>
 * a_5 = 7/2, a_6 = 3, ... <br>
 * となる. ただし, <br>
 * G(u) = 1/(1+) a_1*u/(1+) a_2*u/(1+) ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erfcx_AsymptoticForLarge_accuracy extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double T_MAX = 1d / 8;

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX = 100;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("erfcx(x) = 1/(x*sqrt(pi)) * F(u), u = t^2, t = 1/x としたときの,");
        System.out.println("F(u)を近似する.");
        System.out.println();

        System.out.println("tmax = " + T_MAX);
        new EachApproxExecutor(7).execute(new Erfcx_AsymptoticForLarge_accuracy());

        System.out.println("finished...");
    }

    private Erfcx_AsymptoticForLarge_accuracy() {
        super();

        this.interval = INTERVAL_FACTORY.createInterval(0d, T_MAX * T_MAX);
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * F'(u) = -1/2 * G(u)
         * G(u) = 1/(1+) a_1*u/(1+) a_2*u/(1+) ...
         * a_1 = 3/2, a_2 = 1,
         * a_3 = 5/2, a_4 = 2,
         * a_5 = 7/2, a_6 = 3, ...
         */
        DoubleDoubleFloatElement value = PROVIDER.one();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(u.times(k));
            value = value.plus(PROVIDER.one());
            value = PROVIDER.one().dividedBy(value);

            value = value.times(u.times(k + 0.5));
            value = value.plus(PROVIDER.one());
            value = PROVIDER.one().dividedBy(value);
        }

        return value.times(-0.5);
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
