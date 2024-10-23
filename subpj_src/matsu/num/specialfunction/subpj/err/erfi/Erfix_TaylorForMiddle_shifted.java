package matsu.num.specialfunction.subpj.err.erfi;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * スケーリング相補誤差関数erfcx(x)のテイラー展開をminimax近似する. <br>
 * {@literal 0.5 <= x <= 8.5} の部分区間を扱う.
 * </p>
 * 
 * <p>
 * erfix(x) = 1/(x*sqrt(pi)) * F(x), <br>
 * F(x) = sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^{k+1} <br>
 * について, F(x) を近似する. <br>
 * F(x)は {@literal 1 <= x <= 8} で2倍も変化しないことがわかっており,
 * スケールを1とする.
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
 * 数値安定性のため, y = x - x_shift として,
 * yを引数に取る. <br>
 * x_shift は近似区間の中央とする.
 * </p>
 * 
 * <p>
 * F(x) = sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^{k+1}
 * の級数は交代的で素性が良くない. <br>
 * そこで, この右辺を連分数に変換した, 次を用いる (u = 2x^2 とする). <br>
 * F(x) = u/(1+) (a_1*u)/(1+) (a_2*u)/(1+) ..., <br>
 * a_1 = 1/(1*3), a_2 = -2/(3*5), <br>
 * a_3 = 3/(5*7), a_4 = -4/(7*9), <br>
 * a_5 = 5/(9*11), a_6 = -6/(11*13), <br>
 * ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erfix_TaylorForMiddle_shifted extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_X_MIN = 1d;
    private static final double UPPER_LIMIT_OF_X_MAX = 8d;

    private static final int K_MAX = 1000;

    private final DoubleDoubleFloatElement x_shift;
    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("erfix(x) = 1/(x*sqrt(pi)) * F(x) としたときの,");
        System.out.println("F(x)を近似する.");
        System.out.println("ただし, 安定性を得るために, 区間中央が0になるようにシフトする");
        System.out.println();

        executeEach(17, 1d, 2d);
        executeEach(17, 2d, 3d);
        executeEach(15, 3d, 4d);
        executeEach(14, 4d, 5d);
        executeEach(12, 5d, 6d);
        executeEach(11, 6d, 7d);
        executeEach(10, 7d, 8d);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double x1, double x2) {

        Erfix_TaylorForMiddle_shifted erfcx_exe = new Erfix_TaylorForMiddle_shifted(x1, x2);

        System.out.println("x_shift = " + erfcx_exe.x_shift);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private Erfix_TaylorForMiddle_shifted(double x1, double x2) {
        super();

        this.x_shift = PROVIDER.fromDoubleValue((x1 + x2) * 0.5);
        this.interval = INTERVAL_FACTORY.createInterval(
                x1 - x_shift.asDouble(), x2 - x_shift.asDouble());

        if (!(LOWER_LIMIT_OF_X_MIN <= x1
                && x1 <= UPPER_LIMIT_OF_X_MAX
                && LOWER_LIMIT_OF_X_MIN <= x2
                && x2 <= UPPER_LIMIT_OF_X_MAX)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    public Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement y) {

        /*
         * u = 2x^2 とする.
         * F(t) = u/(1+) (a_1*u)/(1+) (a_2*u)/(1+) ...,
         * a_1 = 1/(1*3), a_2 = -2/(3*5),
         * a_3 = 3/(5*7), a_4 = -4/(7*9),
         * a_5 = 5/(9*11), a_6 = -6/(11*13),
         * ...
         */

        DoubleDoubleFloatElement x = y.plus(x_shift);
        DoubleDoubleFloatElement u = x.times(x).times(2);

        DoubleDoubleFloatElement value = PROVIDER.one();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(u)
                    .times(k).dividedBy(2 * k - 1).dividedBy(2 * k + 1);
            value = k % 2 == 1 ? value : value.negated();
            value = value.plus(PROVIDER.one());
            value = PROVIDER.one().dividedBy(value);
        }

        return value.times(u);
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
