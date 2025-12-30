/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.lgamma;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulliByDoubleDoubleFloat;

/**
 * 対数ガンマ関数に対するStirling近似の残差をminimax近似する. <br>
 * {@literal 10 <= x} を扱う.
 * 
 * 
 * <p>
 * 残差を R(t) とする. (ただし, t = 1/x) <br>
 * R(t) = sum_{k=1}^{inf} B_{2k}/(2k(2k-1)) * t^{2k-1} <br>
 * u = t＾2 の 関数と見て, <br>
 * R(t) = t * G(u), <br>
 * G(u) = sum_{k=1}^{inf} B_{2k}/((2k)(2k-1)) * u^{k-1} <br>
 * としたときのG(u)の近似を考える.
 * </p>
 * 
 * <p>
 * G(u)を {@literal u -> 0} で厳密にしたいので, <br>
 * G'(u) = (G(u) - 1/12) / u <br>
 * を多項式近似する. <br>
 * G'(u) = sum_{k=2}^{inf} B_{2k}/((2k)(2k-1)) * u^{k-2} <br>
 * スケールは, 1/(12u) とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class LGammaRes_AsymptoticForLarge_accuracy
        extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.TypeProvider<DoubleDoubleFloatElement> PROVIDER =
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

        System.out.println("f(x) = lgamma(x) - s(x) (s(x)はスターリング近似)について,");
        System.out.println("t = 1/x としたとき,");
        System.out.println("xf(x) を u = t^2 の多項式で近似する.");
        System.out.println();

        int order = 4;

        System.out.println("tmax = " + T_MAX);
        new EachApproxExecutor(order).execute(
                new LGammaRes_AsymptoticForLarge_accuracy());

        System.out.println("finished...");
    }

    private LGammaRes_AsymptoticForLarge_accuracy() {

        this.interval = INTERVAL_FACTORY.createInterval(0d, T_MAX * T_MAX);
    }

    @Override
    public TypeProvider<DoubleDoubleFloatElement> elementTypeProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * ベルヌーイ数 B_{2} から B_{30} までを使うと, G(u)は相対誤差でおよそ10^{-22}となる.
         * これはdouble-double浮動小数点数の精度よりも低いが,
         * 10^{-18}程度を狙うdoubleの評価関数としては, 十分な精度である.
         */

        /*
         * G'(u) = sum_{k=2}^{inf} B_{2k}/((2k)(2k-1)) * u^{k-2}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = EvenBernoulli.MAX_M; k >= 2; k--) {
            value = value.times(u);
            DoubleDoubleFloatElement c =
                    EvenBernoulliByDoubleDoubleFloat.evenBernoulli(k)
                            .dividedBy(2 * k)
                            .dividedBy(2 * k - 1);
            value = value.plus(c);
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {
        if (u.compareTo(SCALE_U_THRESHOLD) <= 0) {
            return PROVIDER.one().dividedBy(SCALE_U_THRESHOLD.times(12));
        }
        return PROVIDER.one().dividedBy(u.times(12));
    }

    @Override
    public FiniteClosedInterval<DoubleDoubleFloatElement> interval() {
        return this.interval;
    }

    @Override
    public DoubleDoubleFloatElement[] rawCoeff(DoubleDoubleFloatElement[] thisCoeff) {
        DoubleDoubleFloatElement[] coeffF = new DoubleDoubleFloatElement[thisCoeff.length + 1];
        coeffF[0] = PROVIDER.one().dividedBy(12);
        System.arraycopy(thisCoeff, 0, coeffF, 1, thisCoeff.length);

        return coeffF;
    }

}
