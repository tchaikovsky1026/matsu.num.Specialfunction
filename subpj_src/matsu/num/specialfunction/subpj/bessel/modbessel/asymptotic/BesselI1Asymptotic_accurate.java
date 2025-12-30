/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.bessel.modbessel.asymptotic;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;

/**
 * I1の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/192} を引数とする形で計算する
 * (すなわち, {@code x >= 24}). <br>
 * {@literal t -> 0} で厳密に一致するようにしたパターン.
 * 
 * <p>
 * 漸近展開は, <br>
 * I1(x) = sqrt(1/(2*pi*x)) * exp(x) * F(t), <br>
 * F(t) = sum_{k=0}^{inf} ((1^2-4) * (3^2-4) * ... ((2k-1)^2-4))/k! * t^k <br>
 * ただし, 0次近似を厳密にするため, <br>
 * F'(t) = (F(t) - 1) / t として, F'(t)を推定する. <br>
 * F'(t) = sum_{k=1}^{inf} ((1^2-4) * (3^2-4) * ... ((2k-1)^2-4))/k! * t^{k-1}
 * <br>
 * スケールは 1/t とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselI1Asymptotic_accurate
        extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.TypeProvider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    /**
     * 厳密な1/192よりはわずかに大きい.
     */
    private static final double T_MAX = Math.nextUp(1d / 192);

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("t = 1/x としたとき,");
        System.out.println("I1(x) = sqrt(1/(2*pi*x)) * exp(x) * F(t) における");
        System.out.println("F(t)の多項式で近似する.");
        System.out.println();

        int order = 8;

        System.out.println("tmax = " + T_MAX);
        new EachApproxExecutor(order).execute(
                new BesselI1Asymptotic_accurate());

        System.out.println("finished...");
    }

    private BesselI1Asymptotic_accurate() {

        this.interval = INTERVAL_FACTORY.createInterval(0d, T_MAX);
    }

    @Override
    public TypeProvider<DoubleDoubleFloatElement> elementTypeProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement t) {

        /*
         * F'(t) = sum_{k=1}^{inf}
         * ((1^2-4) * (3^2-4) * ... ((2k-1)^2-4))/k! * t^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = EvenBernoulli.MAX_M; k >= 2; k--) {
            DoubleDoubleFloatElement c =
                    PROVIDER.fromDoubleValue(2 * k - 1)
                            .times(2 * k - 1)
                            .minus(4)
                            .dividedBy(k);
            value = value.times(t)
                    .times(c);
            value = value.plus(PROVIDER.one());
        }

        return value.times(-3d);
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement t) {
        if (t.compareTo(SCALE_U_THRESHOLD) <= 0) {
            return PROVIDER.one().dividedBy(SCALE_U_THRESHOLD);
        }
        return PROVIDER.one().dividedBy(t);
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
