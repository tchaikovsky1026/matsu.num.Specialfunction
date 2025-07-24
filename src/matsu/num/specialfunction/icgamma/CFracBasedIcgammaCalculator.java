/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.24
 */
package matsu.num.specialfunction.icgamma;

import java.util.function.DoubleFunction;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteGammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 連分数をベースとする不完全ガンマ関数の値を計算する方式を提供する.
 * 
 * <p>
 * パッケージ内部で使用されることを想定する.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class CFracBasedIcgammaCalculator {

    private final double a;

    /**
     * x を与えて,
     * x^a e^{-x}/(Γ(a+1)) の値を返す. <br>
     * 必ず有限の値が返る.
     */
    private final CoefficientCalculator coeffToLCPCalc;

    /**
     * 唯一の非公開コンストラクタ.
     * 
     * <p>
     * バリデーションされていない. <br>
     * {@link IncompleteGammaFunction} のパラメータ範囲に従うこと.
     * </p>
     * 
     * @param coeffToLCPCalcCreator
     *            {@literal a -> (x -> (x^a e^(-x)/(Γ(a+1))))}
     */
    private CFracBasedIcgammaCalculator(double a,
            DoubleFunction<? extends CoefficientCalculator> coeffToLCPCalcCreator) {
        super();
        this.a = a;

        this.coeffToLCPCalc = coeffToLCPCalcCreator.apply(a);
    }

    /**
     * 連分数展開を利用して, 第1種不完全ガンマ関数を計算する.
     * 
     * <p>
     * {@literal a >> 1} の場合,
     * {@literal x < a - c * sqrt(a)} でなければうまくいかない.
     * </p>
     * 
     * @param x x
     * @return P(a,x)
     */
    final double calcP(double x) {
        return this.calcPByShift(x, 0);
    }

    /**
     * 連分数展開とパラメータシフトを使用して, 第1種不完全ガンマ関数を計算する.
     * 
     * <p>
     * a が中程度のものを対象とし, {@literal x < a} かつ x が a に近い場合が対象である. <br>
     * パラメータが大きいほうにシフトすることで, 不完全ガンマ関数の中心を大きい方に移動することで,
     * {@literal x < a + s} とする. <br>
     * s はシフト量 (整数)であり, {@literal x < a + s - c * sqrt(a)} となるように定める.
     * </p>
     * 
     * @param x x
     * @param shift シフト量
     * @return P(a,x)
     */
    final double calcPByShift(double x, int shift) {
        assert shift >= 0;

        double out = ICGContinuedFractionFactor.factorLCP(x, a + shift);
        for (int n = shift; n >= 1; n--) {
            out *= x / (a + n);
            out += 1;
        }
        return this.coeffToLCPCalc.calc(x, out);
    }

    /**
     * 連分数展開を利用して, 第2種不完全ガンマ関数を計算する.
     * 
     * <p>
     * {@literal a >> 1} の場合,
     * {@literal x > a + c * sqrt(a)} でなければうまくいかない.
     * </p>
     * 
     * @param x x
     * @return Q(a,x)
     */
    final double calcQ(double x) {
        return this.calcQByShift(x, 0);
    }

    /**
     * 連分数展開とパラメータシフトを使用して, 第2種不完全ガンマ関数を計算する.
     * 
     * <p>
     * a が中程度のものを対象とし, {@literal x > a} かつ x が a に近い場合が対象である. <br>
     * パラメータが小さいほうにシフトすることで, 不完全ガンマ関数の中心を小さい方に移動することで,
     * {@literal x > a - s} とする. <br>
     * s はシフト量 (整数)であり, {@literal x > a - s + c * sqrt(a)} となるように定める.
     * </p>
     * 
     * @param x x
     * @param shift シフト量
     * @return Q(a,x)
     */
    final double calcQByShift(double x, int shift) {
        assert shift >= 0;

        double out = ICGContinuedFractionFactor.factorUCP(x, a - shift);
        double invX = 1 / x;
        for (int n = shift; n >= 1; n--) {
            out *= (a - n) * invX;
            out += 1;
        }
        return this.coeffToLCPCalc.calc(x, out * (a / x));
    }

    /**
     * a に対応するインスタンスを返す.
     * 
     * <p>
     * バリデーションされないので公開してはならない. <br>
     * {@link IncompleteGammaFunction} のパラメータ範囲に従うこと.
     * </p>
     */
    static CFracBasedIcgammaCalculator of(double a) {
        return a < 20
                ? new CFracBasedIcgammaCalculator(a, CoefficientCalculatorLowParam::new)
                : new CFracBasedIcgammaCalculator(a, CoefficientCalculatorHighParam::new);
    }

    private static interface CoefficientCalculator {

        /**
         * (x^a e^(-x)/(Γ(a+1)) を計算し, multiを乗算した値を返す.
         * 
         * @param x x
         * @param multi multi
         * @return multi * (x^a e^(-x)/(Γ(a+1))
         */
        abstract double calc(double x, double multi);
    }

    /**
     * a が小さいときに使える.
     */
    private static final class CoefficientCalculatorLowParam implements CoefficientCalculator {

        private final double a;

        private final double logGammaAp1;

        CoefficientCalculatorLowParam(double a) {
            super();

            this.a = a;
            this.logGammaAp1 = GammaFunction.lgamma(a + 1);
        }

        @Override
        public double calc(double x, double multi) {
            final double logFactor = this.a * Exponentiation.log(x);
            if (logFactor == Double.POSITIVE_INFINITY) {
                return 0;
            }

            double logOut = a * Exponentiation.log(x) - x - this.logGammaAp1;

            // out < 1E-200の場合は, アンダーフローのフォロー
            return logOut < -460
                    ? Exponentiation.exp(logOut + Exponentiation.log(multi))
                    : multi * Exponentiation.exp(logOut);
        }
    }

    /**
     * a が大きいときに使える.
     */
    private static final class CoefficientCalculatorHighParam implements CoefficientCalculator {

        private static final double HALF_LN2PI = 0.5 * Math.log(2 * Math.PI);

        private final double a;

        /*
         * x^a e^{-x}/(aΓ(a))を計算するとき,
         * 直接的計算では対数でa log(a)のオーダーの値が生じるが, 計算結果はa程度になる.
         * これはすなわち, 丸め誤差が怖いことになる.
         * したがって, スターリング近似により或る程度式変形をしておいて, xを代入して計算するようにする.
         * 
         * log(x^a e^{-x}/(aΓ(a))) = alog(x) - x - log(a) - logΓ(a)
         * = alog(x/a) - (x - a) - (1/2)log(a) - (1/2)log(2π) - f(a)
         * f(a)はlogΓ(a)のスターリング近似の残差であり,
         * f(a) = logΓ(a) - (a - 1/2)log(a) + a - (1/2)log(2π)
         * である.
         */
        /**
         * residualLogFactor = - (1/2)log(a) - (1/2)log(2π) - f(a)
         */
        private final double residualLogFactor;

        CoefficientCalculatorHighParam(double a) {
            super();

            this.a = a;
            this.residualLogFactor = -0.5 * Exponentiation.log(a) - HALF_LN2PI
                    - GammaFunction.lgammaStirlingResidual(a);
        }

        @Override
        public double calc(double x, double multi) {
            final double thisA = this.a;

            final double t = (x - thisA) / thisA;

            double logOut = thisA * log1p_m(t) + residualLogFactor;

            // out < 1E-200の場合は, アンダーフローのフォロー
            return logOut < -460
                    ? Exponentiation.exp(logOut + Exponentiation.log(multi))
                    : multi * Exponentiation.exp(logOut);
        }

        /**
         * log(1+t) - t の値を返す. <br>
         * tが0に近いときでも相対精度が維持される.
         * 
         * @param t t
         * @return log(1+t) - t
         */
        private static double log1p_m(double t) {
            if (!(Math.abs(t) <= 0.1)) {

                double log1pt = Exponentiation.log1p(t);

                return log1pt == Double.POSITIVE_INFINITY
                        ? Double.NEGATIVE_INFINITY
                        : log1pt - t;
            }

            int maxN = 17;
            double v = 0d;
            for (int n = maxN; n >= 2; n--) {
                v *= -t;
                v += -1d / n;
            }

            return v * (t * t);
        }
    }
}
