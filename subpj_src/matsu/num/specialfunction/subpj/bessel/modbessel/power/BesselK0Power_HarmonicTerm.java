/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.EachApproxExecutor;
import matsu.num.specialfunction.subpj.FiniteClosedIntervalFactory;
import matsu.num.specialfunction.subpj.RawCoeffCalculableFunction;

/**
 * <p>
 * K0の調和数部分の計算の近似のためのターゲット. <br>
 * u = (x/2)^2 として, {@literal 0 <= u <= 1} を引数とする形で計算する. <br>
 * K0(x) = -(gamma + log(x/2))I0(x) + G(u) <br>
 * の形に分解したときの, <br>
 * Gの推定に関する. <br>
 * G(u) = Sum_{m=0 to inf} H_m/(m!)^2 * u^m <br>
 * スケールは 1 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselK0Power_HarmonicTerm extends RawCoeffCalculableFunction<DoubleDoubleFloatElement> {

    private static final PseudoRealNumber.Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();
    private static final FiniteClosedIntervalFactory<DoubleDoubleFloatElement> INTERVAL_FACTORY =
            new FiniteClosedIntervalFactory<>(PROVIDER);

    private static final double U_MIN = 0d;
    private static final double U_MAX = 1d;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 40;

    /**
     * [H_0, ..., H_kMax],
     * Kのべき級数の計算で使用する.
     */
    private static final DoubleDoubleFloatElement[] HARMONIC_NUMBERS =
            calcHarmonicNumbers(K_MAX_FOR_BESSEL_K_BY_POWER);

    private final FiniteClosedInterval<DoubleDoubleFloatElement> interval;

    public static void main(String[] args) {

        System.out.println("K0(x) について,");
        System.out.println("u = (x/2)^2, K0(x) = -(gamma + log(x/2))I0(x) + G(u) としたときの,");
        System.out.println("G(u) に対する多項式近似を扱う.");
        System.out.println();

        int order = 8;

        System.out.println("umin = " + U_MIN);
        System.out.println("umax = " + U_MAX);
        new EachApproxExecutor(order).execute(new BesselK0Power_HarmonicTerm());

        System.out.println("finished...");
    }

    private BesselK0Power_HarmonicTerm() {
        super();
        this.interval = INTERVAL_FACTORY.createInterval(U_MIN, U_MAX);
    }

    @Override
    public PseudoRealNumber.Provider<DoubleDoubleFloatElement> elementProvider() {
        return PROVIDER;
    }

    @Override
    protected DoubleDoubleFloatElement calcValue(DoubleDoubleFloatElement u) {

        /*
         * G(u) = Sum_{m=0 to inf} H_m/(m!)^2 * u^m
         */
        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 1; k--) {
            value = value.times(u)
                    .dividedBy(k)
                    .dividedBy(k);
            value = value.plus(HARMONIC_NUMBERS[k - 1]);
        }

        return value;
    }

    @Override
    protected DoubleDoubleFloatElement calcScale(DoubleDoubleFloatElement u) {
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

    /**
     * [H_0, ..., H_kMax] を返す.
     */
    private static DoubleDoubleFloatElement[] calcHarmonicNumbers(int kMax) {
        final DoubleDoubleFloatElement[] harmonicNumbers = new DoubleDoubleFloatElement[kMax + 1];

        DoubleDoubleFloatElement current = PROVIDER.zero();
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current = current.plus(PROVIDER.one().dividedBy(k + 1));
        }

        return harmonicNumbers;
    }
}
