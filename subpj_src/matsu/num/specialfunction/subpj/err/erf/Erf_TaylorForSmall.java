/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.err.erf;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * 誤差関数erf(x)をminimax近似するための関数と, その近似の実行. <br>
 * {@literal 0 <= x <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erf(x) = 2/sqrt(pi) * x * F(u), <br>
 * F(u) = sum_{k=0}^{inf}
 * (-1)^k / (k! * (2k+1)) * u^{k} <br>
 * ただし, u = x^2 である. <br>
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class Erf_TaylorForSmall extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double MIN_X = 0d;
    private static final double MAX_X = 1d;

    private static final DoubleDoubleFloatElement SCALE_U_THRESHOLD =
            DoubleDoubleFloatElement.elementProvider().fromDoubleValue(1d / 1024);

    private static final int K_MAX = 100;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    /**
     * {@link Erf_TaylorForSmall} を用いて近似を実行する.
     * 
     * @param args
     */
    public static void main(String[] args) {

        System.out.println("erf(x) = 2/sqrt(pi) * x * F(u), u = x^2 としたときの,");
        System.out.println("F(u)を近似する.");
        System.out.println();

        System.out.println("xmin = " + MIN_X);
        System.out.println("xmax = " + MAX_X);
        new EachApproxExecutor(10).execute(new Erf_TaylorForSmall());

        System.out.println("finished...");
    }

    private Erf_TaylorForSmall() {
        super();

        this.interval = INTERVAL_FACTORY.createInterval(MIN_X * MIN_X, MAX_X * MAX_X);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return DoubleDoubleFloatElement.elementProvider();
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * F'(u) =
         * sum_{k=1}^{inf} (-1)^k / (k! * (2k+1)) * u^{k-1}
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX + 1; k >= 1; k--) {
            value = value.times(
                    u.negated().dividedBy(k + 1));
            value = value.plus(
                    PROVIDER.one().dividedBy(2 * k + 1));
        }

        return value.negated();
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {

        if (u.compareTo(SCALE_U_THRESHOLD) <= 0) {
            return PROVIDER.one().dividedBy(SCALE_U_THRESHOLD);
        }
        return PROVIDER.one().dividedBy(u);
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
