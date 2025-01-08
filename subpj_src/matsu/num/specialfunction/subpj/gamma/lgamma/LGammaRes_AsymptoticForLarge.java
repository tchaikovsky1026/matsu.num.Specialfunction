/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.lgamma;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * 対数ガンマ関数に対するStirling近似の残差をminimax近似する. <br>
 * {@literal 2.5 <= x} を扱う.
 * 
 * <p>
 * 残差を f(x) とする. <br>
 * f(x) = sum_{k=1}^{inf} B_{2k}/(2k(2k-1)) * x^{-(2k-1)} <br>
 * {@literal x >> 1}で f(x) = 1/(12x)となることを用い, <br>
 * G(t) = sum_{k=1}^{inf} B_{2k}/(2k(2k-1)) * t^{2k-2} <br>
 * とすれば, G(t)はほとんど 1/12 であり, <br>
 * f(x) = G(t)/x
 * である (ただし, t = 1/x). <br>
 * G(t)のスケールは, 1/12 とする.
 * </p>
 * 
 * <p>
 * このクラスでは, G(t)をtの多項式で近似する. <br>
 * G(t)は偶数次しか持たないが, 漸近級数であるので収束性がよくない. <br>
 * そのため, t^2でなくtの多項式とした.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class LGammaRes_AsymptoticForLarge
        extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double T_MIN = Math.nextUp(0.1d);
    private static final double T_MAX = Math.nextDown(0.4d);

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("f(x) = lgamma(x) - s(x) (s(x)はスターリング近似)について,");
        System.out.println("t = 1/x としたとき,");
        System.out.println("xf(x) を t の多項式で近似する.");
        System.out.println();

        int order = 11;

        System.out.println("tmin = " + T_MIN);
        System.out.println("tmax = " + T_MAX);
        new EachApproxExecutor(order).execute(
                new LGammaRes_AsymptoticForLarge());

        System.out.println("finished...");
    }

    private LGammaRes_AsymptoticForLarge() {

        this.interval = INTERVAL_FACTORY.createInterval(T_MIN, T_MAX);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement t) {
        return LGammaResidual.value(PROVIDER.one().dividedBy(t)).dividedBy(t);
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement t) {
        return PROVIDER.one().dividedBy(12d);
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
