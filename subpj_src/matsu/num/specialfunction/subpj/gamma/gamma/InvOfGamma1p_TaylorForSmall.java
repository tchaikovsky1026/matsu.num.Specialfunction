/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.gamma;

import static matsu.num.specialfunction.subpj.gamma.component.GammaConstant.*;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameterByDoubleDoubleFloat;

/**
 * 1/Gamma(1+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * 
 * <hr>
 * 
 * <p>
 * 値の計算について
 * </p>
 * 
 * <p>
 * logGamma(1+x) = -gamma * x
 * + sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k} <br>
 * を計算し, 1/exp(logGamma(1+x)) により得る.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class InvOfGamma1p_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double X_MIN = Math.nextDown(-0.5);
    private static final double X_MAX = Math.nextUp(0.5);

    private static final int K_MAX_FOR_EXP = 40;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("1/Gamma(1+x)のテイラー展開を近似する");
        System.out.println();

        new EachApproxExecutor(14).execute(new InvOfGamma1p_TaylorForSmall());

        System.out.println("finished...");
    }

    private InvOfGamma1p_TaylorForSmall() {
        super();

        this.interval = INTERVAL_FACTORY.createInterval(X_MIN, X_MAX);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement x) {
        return PROVIDER.one().dividedBy(exp(lgamma1p(x)));
    }

    private DoubleDoubleFloatElement lgamma1p(DoubleDoubleFloatElement x) {

        /*
         * logGamma(1+x) = -gamma * x + sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = RiemannZetaParameterByDoubleDoubleFloat.MAX_M; k >= 2; k--) {
            value = value.times(x);
            value = value.negated();
            DoubleDoubleFloatElement c =
                    RiemannZetaParameterByDoubleDoubleFloat.zeta(k)
                            .dividedBy(k);
            value = value.plus(c);
        }

        return value.times(x).minus(EULER_MASCHERONI_GAMMA).times(x);
    }

    private DoubleDoubleFloatElement exp(DoubleDoubleFloatElement y) {

        /*
         * exp(y) = sum_{k=0}^{inf} x^k/k!
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX_FOR_EXP; k >= 1; k--) {
            value = value.times(y).dividedBy(k);
            value = value.plus(PROVIDER.one());
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement x) {
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
