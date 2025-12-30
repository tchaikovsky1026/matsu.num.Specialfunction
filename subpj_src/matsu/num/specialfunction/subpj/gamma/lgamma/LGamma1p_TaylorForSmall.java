/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.lgamma;

import static matsu.num.specialfunction.subpj.gamma.component.GammaConstant.*;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameterByDoubleDoubleFloat;

/**
 * logGamma(1+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * logGamma(1+x) = x * F(x) とし, F(x) に対して多項式近似する. <br>
 * F(x) = -gamma
 * + sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k-1} <br>
 * スケールは, gamma とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class LGamma1p_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.TypeProvider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("lgamma(1+x)/xのテイラー展開を近似する");
        System.out.println();

        executeEach(15, -0.5, -0.25);
        executeEach(13, -0.25, 0);
        executeEach(15, 0, 0.5);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double x1, double x2) {
        LGamma1p_TaylorForSmall erfcx_exe = new LGamma1p_TaylorForSmall(x1, x2);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private LGamma1p_TaylorForSmall(double x1, double x2) {
        super();

        if (!(LOWER_LIMIT_OF_INTERVAL <= x1
                && x1 <= UPPER_LIMIT_OF_INTERVAL
                && LOWER_LIMIT_OF_INTERVAL <= x2
                && x2 <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.interval = INTERVAL_FACTORY.createInterval(x1, x2);
    }

    @Override
    public TypeProvider<DoubleDoubleFloatElement> elementTypeProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement x) {

        /*
         * F(x) = -gamma + sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k-1}
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

        return value.times(x).minus(EULER_MASCHERONI_GAMMA);
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement x) {
        return EULER_MASCHERONI_GAMMA;
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
