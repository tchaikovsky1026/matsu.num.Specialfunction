package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.generalfield.FiniteClosedInterval;
import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * I0(x)のべき級数をminimax近似する. <br>
 * {@literal 2 <= x <= 24} の部分区間を扱う.
 * </p>
 * 
 * <p>
 * 厳密式:
 * I0(x) = sum_{m=0 to inf} 1 / (m!)^2 * (x/2)^{2m} <br>
 * に対し, 漸近形である I0(x) = exp(x) * F(x) を適用し,
 * F(x) を多項式で表す. <br>
 * スケールは1である.
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
 * @author Matsuura Y.
 */
final class BesselI0Power_ExpTerm_shifted extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_X_MIN = 2d;
    private static final double UPPER_LIMIT_OF_X_MAX = 24d;

    private static final int K_MAX = 200;

    private final DoubleDoubleFloatElement x_shift;
    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("I0を漸近形で規格化したものを近似する(ただし引数はTaylorの形)");
        System.out.println("xをシフトすることで安定性を向上させる");
        System.out.println();

        executeEach(19, 2d, 6d);
        executeEach(16, 6d, 10d);
        executeEach(13, 10d, 14d);
        executeEach(11, 14d, 18d);
        executeEach(13, 18d, 24d);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double x1, double x2) {

        BesselI0Power_ExpTerm_shifted erfcx_exe = new BesselI0Power_ExpTerm_shifted(x1, x2);

        System.out.println("x_shift = " + erfcx_exe.x_shift);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private BesselI0Power_ExpTerm_shifted(double x1, double x2) {
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
         * I0(x) = sum_{m=0 to inf} 1 / (m!)^2 * (x/2)^{2m},
         * F(x) = exp(-x) * I0(x)
         */

        DoubleDoubleFloatElement x = y.plus(x_shift);
        DoubleDoubleFloatElement u = x.times(x).dividedBy(4);

        DoubleDoubleFloatElement value_besselI = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value_besselI = value_besselI.times(u)
                    .dividedBy(k)
                    .dividedBy(k);
            value_besselI = value_besselI.plus(PROVIDER.one());
        }

        DoubleDoubleFloatElement value_exp = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value_exp = value_exp.times(x)
                    .dividedBy(k);
            value_exp = value_exp.plus(PROVIDER.one());
        }

        return value_besselI.dividedBy(value_exp);
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
