package matsu.num.specialfunction.subpj.err.erf;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * スケーリング相補誤差関数erfcx(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfcx(x) = 1/(x*sqrt(pi)) *
 * sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k} <br>
 * erfcx(x) = 1/(x*sqrt(pi)) * F(t) <br>
 * としたときの, 多項式 F(t) を近似する. <br>
 * F(t) = sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k} <br>
 * スケールは 1 とする.
 * </p>
 * 
 * <p>
 * 数値安定性のため, t = s + t_shift として,
 * sを引数に取る. <br>
 * t_shift は近似区間の中央とする.
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
 * F(t)は漸近級数であり, 収束しない. <br>
 * そこで, 連分数に変形する. <br>
 * F(t) = 1 - c_1*t^2 + c_1*c_2*t^4 - ... <br>
 * の形をしており, <br>
 * c_1 = 1/2, c_2 = 3/2, ... <br>
 * である. これを商差法により連分数に変換すれば, 最上段の係数1を除いて, <br>
 * a_1 = 1/2, a_2 = 1, <br>
 * a_3 = 3/2, a_4 = 2, <br>
 * a_5 = 5/2, a_6 = 3, ... <br>
 * となる. ただし, <br>
 * F(t) = 1/(1+) a_1*t^2/(1+) a_2*t^2/(1+) ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erfcx_AsymptoticForLarge_shifted extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final DoubleDoubleFloatElement LOWER_LIMIT_OF_INTERVAL =
            PROVIDER.zero();
    private static final DoubleDoubleFloatElement UPPER_LIMIT_OF_INTERVAL =
            PROVIDER.one();

    private static final int K_MAX = 100;

    private final DoubleDoubleFloatElement t_shift;
    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("erfcxの漸近展開を近似する");
        System.out.println("tをシフトすることで安定性を向上させる");
        System.out.println();

        executeEach(11, 1d / 8, 2d / 8);
        executeEach(10, 2d / 8, 3d / 8);
        executeEach(10, 3d / 8, 4d / 8);
        executeEach(12, 4d / 8, 6d / 8);
        executeEach(11, 6d / 8, 8d / 8);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double t1, double t2) {

        Erfcx_AsymptoticForLarge_shifted erfcx_exe = new Erfcx_AsymptoticForLarge_shifted(t1, t2);

        System.out.println("t_shift = " + erfcx_exe.t_shift);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private Erfcx_AsymptoticForLarge_shifted(double t1, double t2) {
        super();

        this.t_shift = PROVIDER.fromDoubleValue((t1 + t2) * 0.5);
        this.interval = INTERVAL_FACTORY.createInterval(
                t1 - t_shift.asDouble(), t2 - t_shift.asDouble());

        if (!(LOWER_LIMIT_OF_INTERVAL.asDouble() <= t1
                && t1 <= UPPER_LIMIT_OF_INTERVAL.asDouble()
                && LOWER_LIMIT_OF_INTERVAL.asDouble() <= t2
                && t2 <= UPPER_LIMIT_OF_INTERVAL.asDouble())) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement s) {

        /*
         * F(u) = 1/(1+) a_1*t^2/(1+) a_2*t^2/(1+) ...
         * a_1 = 1/2, a_2 = 1,
         * a_3 = 3/2, a_4 = 2,
         * a_5 = 5/2, a_6 = 3, ...
         */

        DoubleDoubleFloatElement t = s.plus(t_shift);
        DoubleDoubleFloatElement t2 = t.times(t);

        DoubleDoubleFloatElement value = PROVIDER.one();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(t2.times(k));
            value = value.plus(PROVIDER.one());
            value = PROVIDER.one().dividedBy(value);

            value = value.times(t2.times(k - 0.5));
            value = value.plus(PROVIDER.one());
            value = PROVIDER.one().dividedBy(value);
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement s) {
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
