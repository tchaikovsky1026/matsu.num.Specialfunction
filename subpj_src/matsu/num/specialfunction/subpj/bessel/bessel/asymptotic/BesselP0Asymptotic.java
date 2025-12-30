/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.PseudoRealNumber.TypeProvider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * P0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} を引数とする形で計算する. <br>
 * J0(x) = sqrt(2/(pi*x)) * (P0(t)cos + Q0(t)sin) <br>
 * Y0(x) = sqrt(2/(pi*x)) * (P0(t)sin - Q0(t)cos) <br>
 * として, P0(t) を推定する. <br>
 * (P0(x)^2 + Q0(x)^2) は x が0から1/16までで2割程度しか変化しない. <br>
 * よって, スケールは1とする.
 * </p>
 * 
 * <p>
 * P0はtの0次, 2次, ... しかないが, 奇数次も含むとして推定する.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselP0Asymptotic extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.TypeProvider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double LOWER_LIMIT_OF_T = 0d;
    private static final double UPPER_LIMIT_OF_T = 1d / 16;

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("P0(x) について,");
        System.out.println("t = 1/(8x) としたときの展開");
        System.out.println();

        executeEach(9, 0, 1d / 128);
        executeEach(8, 1d / 128, 2d / 128);
        executeEach(11, 2d / 128, 4d / 128);
        executeEach(10, 4d / 128, 6d / 128);
        executeEach(9, 6d / 128, 8d / 128);

        System.out.println("finished...");
    }

    private static void executeEach(int order, double t1, double t2) {
        new EachApproxExecutor(order).execute(new BesselP0Asymptotic(t1, t2));
    }

    private BesselP0Asymptotic(double t1, double t2) {
        super();

        if (!(LOWER_LIMIT_OF_T <= t1
                && t1 <= UPPER_LIMIT_OF_T
                && LOWER_LIMIT_OF_T <= t2
                && t2 <= UPPER_LIMIT_OF_T)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.interval = INTERVAL_FACTORY.createInterval(t1, t2);
    }

    @Override
    public TypeProvider<DoubleDoubleFloatElement> elementTypeProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement t) {
        return H0AsymptoticCalc.calcP0(t);
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement t) {
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
