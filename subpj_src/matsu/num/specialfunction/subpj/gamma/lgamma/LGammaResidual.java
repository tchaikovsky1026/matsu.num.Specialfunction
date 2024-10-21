package matsu.num.specialfunction.subpj.gamma.lgamma;

import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulli;
import matsu.num.specialfunction.subpj.gamma.component.EvenBernoulliByDoubleDoubleFloat;

/**
 * 対数ガンマ関数に対するStirling近似の残差を扱う. <br>
 * すなわち, f(x) = logGamma(x) - S(x) の取り扱い.
 * 
 * <p>
 * x は2.5以上を扱う.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class LGammaResidual {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
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
         * f(x) = sum_{k=1}^{inf} B_{2k}/(2k * (2k-1)) * x^{-2k+1}
         * である.
         * 
         * ベルヌーイ数 B_{2} から B_{30} までを使うと, f(x)は相対誤差でおよそ10^{-22}となる.
         * これはdouble-double浮動小数点数の精度よりも低いが,
         * 10^{-18}程度を狙うdoubleの評価関数としては, 十分な精度である.
         */
        DoubleDoubleFloatElement invX = PROVIDER.one().dividedBy(x);
        DoubleDoubleFloatElement invX2 = invX.times(invX);

        DoubleDoubleFloatElement value = PROVIDER.zero();
        for (int k = EvenBernoulli.MAX_M; k >= 1; k--) {
            value = value.times(invX2);
            DoubleDoubleFloatElement c =
                    EvenBernoulliByDoubleDoubleFloat.evenBernoulli(k)
                            .dividedBy(2 * k)
                            .dividedBy(2 * k - 1);
            value = value.plus(c);
        }

        return value.times(invX);
    }

    /**
     * f(x) - f(x+1) を扱う.
     */
    private static final class ResidualRecursion {

        private static final int K_MAX = 60;

        /**
         * xは2.5以上である.
         */
        static DoubleDoubleFloatElement difference(DoubleDoubleFloatElement x) {

            /*
             * t = 1/x
             * f(x)- f(x+1) = h(t)
             * = sum_{k=2}^{inf} (-1)^k * (k-1)/(2k(k+1)) * t^k
             * 
             * t=0.4でk=60まで計算すれば, 10^{-24}の相対誤差.
             * これはdouble-double浮動小数点数の精度よりも低いが,
             * 10^{-18}程度を狙うdoubleの評価関数としては, 十分な精度である.
             */

            DoubleDoubleFloatElement t = PROVIDER.one().dividedBy(x);

            DoubleDoubleFloatElement value = PROVIDER.zero();
            for (int k = K_MAX; k >= 2; k--) {
                value = value.times(t);
                value = value.negated();
                DoubleDoubleFloatElement c =
                        PROVIDER.fromDoubleValue(k - 1)
                                .dividedBy(2 * k * (k + 1));
                value = value.plus(c);
            }

            return value.times(t).times(t);
        }
    }
}
