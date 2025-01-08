/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.digamma;

import static matsu.num.specialfunction.subpj.gamma.component.GammaConstant.*;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameterByDoubleDoubleFloat;

/**
 * ディガンマ psi(2+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * psi(2+x) = 1-gamma + sum_{m=2}^{inf} (-1)^m * zetam1(m) * z^{m-1}
 * に対して多項式近似する. <br>
 * スケールは, 1 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Digamma2p_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("digamma(2+x)のテイラー展開を近似する");
        System.out.println();

        executeEach(13, -0.5, 0);
        executeEach(12, 0, 0.5);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double x1, double x2) {
        Digamma2p_TaylorForSmall erfcx_exe = new Digamma2p_TaylorForSmall(x1, x2);
        new EachApproxExecutor(order).execute(erfcx_exe);
    }

    private Digamma2p_TaylorForSmall(double x1, double x2) {
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
         * psi(2+x) = 1-gamma + sum_{m=2}^{inf} (-1)^m * zetam1(m) * z^{m-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = RiemannZetaParameterByDoubleDoubleFloat.MAX_M; k >= 2; k--) {
            value = value.times(x);
            value = value.negated();
            DoubleDoubleFloatElement c =
                    RiemannZetaParameterByDoubleDoubleFloat.zetam1(k);
            value = value.plus(c);
        }

        return value.times(x).minus(EULER_MASCHERONI_GAMMA).plus(PROVIDER.one());
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
