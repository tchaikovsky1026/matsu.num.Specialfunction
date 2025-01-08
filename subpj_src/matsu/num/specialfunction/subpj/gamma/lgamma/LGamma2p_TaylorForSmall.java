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
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameterByDoubleDoubleFloat;

/**
 * logGamma(2+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * logGamma(2+x) = x * F(x) とし, F(x) に対して多項式近似する. <br>
 * F(x) = (1-gamma)
 * + sum_{k=2}^{inf} (-1)^k (zeta(k)-1)/k * x^{k-1} <br>
 * スケールは, 1-gamma とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class LGamma2p_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("lgamma(2+x)/xのテイラー展開を近似する");
        System.out.println();

        executeEach(13, -0.5, 0);
        executeEach(11, 0, 0.5);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double x1, double x2) {
        LGamma2p_TaylorForSmall erfcx_exe = new LGamma2p_TaylorForSmall(x1, x2);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private LGamma2p_TaylorForSmall(double x1, double x2) {
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
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement x) {

        /*
         * F(x) = (1-gamma) + sum_{k=2}^{inf} (-1)^k (zeta(k)-1)/k * x^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = RiemannZetaParameterByDoubleDoubleFloat.MAX_M; k >= 2; k--) {
            value = value.times(x);
            value = value.negated();
            DoubleDoubleFloatElement c =
                    RiemannZetaParameterByDoubleDoubleFloat.zetam1(k)
                            .dividedBy(k);
            value = value.plus(c);
        }

        return value.times(x).plus(PROVIDER.one().minus(EULER_MASCHERONI_GAMMA));
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement x) {
        return PROVIDER.one().minus(EULER_MASCHERONI_GAMMA);
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
