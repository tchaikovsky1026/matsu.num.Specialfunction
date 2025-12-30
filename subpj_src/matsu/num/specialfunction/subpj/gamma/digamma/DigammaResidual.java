/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.digamma;

import matsu.num.approximation.PseudoRealNumber;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulliByDoubleDoubleFloat;

/**
 * 
 * ディガンマ関数に対するStirling近似の残差を扱う. <br>
 * すなわち, f(x) = psi(x) - log(x) + 1/(2x) の取り扱い. <br>
 * f(x) = sum_{k=1}^{inf} (-B_{2k})/(2k) * x^{-2k}
 * 
 * <p>
 * x は2.5以上を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class DigammaResidual {

    private static final PseudoRealNumber.TypeProvider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();

    private static final DoubleDoubleFloatElement X_MIN =
            PROVIDER.fromDoubleValue(2.5d);
    private static final DoubleDoubleFloatElement VALID_X_FOR_ASYMPTOTIC =
            PROVIDER.fromDoubleValue(10d);

    /**
     * f(x) を計算する.
     * 内部的には再帰である.
     * 
     * @param x x
     * @return f(x)
     */
    public static double value(double x) {
        return value(PROVIDER.fromDoubleValue(x)).asDouble();
    }

    /**
     * f(x) を計算する. <br>
     * 10^{-22}程度の相対誤差を達成する.
     * 
     * @param x x
     * @return f(x)
     * @throws IllegalArgumentException 範囲外の場合
     * @throws NullPointerException null
     */
    public static DoubleDoubleFloatElement value(DoubleDoubleFloatElement x) {
        if (x.compareTo(X_MIN) < 0) {
            throw new IllegalArgumentException("範囲外");
        }

        if (x.compareTo(VALID_X_FOR_ASYMPTOTIC) >= 0) {
            return valueAsymptotic(x);
        }

        return ResidualRecursion.difference(x).plus(value(x.plus(PROVIDER.one())));
    }

    /**
     * {@literal x >> 1} における f(x) の計算. <br>
     * {@literal x >> 10} でおそらくうまくいく.
     * 
     * @param x x
     * @return f(x)
     */
    private static DoubleDoubleFloatElement valueAsymptotic(DoubleDoubleFloatElement x) {

        /*
         * 漸近級数では,
         * f(x) = sum_{k=1}^{inf} (-B_{2k})/(2k) * x^{-2k}
         * である.
         */
        DoubleDoubleFloatElement invX = PROVIDER.one().dividedBy(x);
        DoubleDoubleFloatElement invX2 = invX.times(invX);

        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value = value.times(invX2);
            DoubleDoubleFloatElement c =
                    EvenBernoulliByDoubleDoubleFloat.evenBernoulli(k)
                            .dividedBy(2 * k);
            value = value.plus(c);
        }

        return value.times(invX2).negated();
    }

    /**
     * f(x) - f(x+1) を扱う.
     */
    private static final class ResidualRecursion {

        /**
         * 注:この値が妥当かは不明である.
         */
        private static final int K_MAX = 60;

        /**
         * xは2.5以上である.
         */
        static DoubleDoubleFloatElement difference(DoubleDoubleFloatElement x) {

            /*
             * t = 1/x
             * f(x)- f(x+1) = h(t)
             * = sum_{k=3}^{inf} (-1)^{k} (k-2)/(2k) * t^k
             */

            DoubleDoubleFloatElement t = PROVIDER.one().dividedBy(x);

            DoubleDoubleFloatElement value = PROVIDER.zero();
            for (int k = K_MAX; k >= 3; k--) {
                value = value.times(t);
                value = value.negated();
                DoubleDoubleFloatElement c =
                        PROVIDER.fromDoubleValue(k - 2)
                                .dividedBy(2 * k);
                value = value.plus(c);
            }

            return value.times(t).times(t).times(t).negated();
        }
    }
}
