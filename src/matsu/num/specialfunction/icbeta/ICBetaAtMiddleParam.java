/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満). <br>
 * Min(a,b)が11から40000を扱う.
 * 
 * <p>
 * ある閾値を境に, I(a,b,x)とI(b,a,1-x)のどちらを計算するかを切り替える. <br>
 * 分布の裾は連分数システム, 分布の中央の場合は漸化式により裾までシフトしてから連分数で計算.
 * </p>
 *
 * @author Matsuura Y.
 * @version 18.0
 */
final class ICBetaAtMiddleParam extends SkeletalICBeta implements IncompleteBetaFunction {

    private static final double HALF_LN2PI = 0.9189385332046727417804d;

    private final double a;
    private final double b;

    private final double xLowerThreshold;
    private final double xUpperThreshold;

    private final double muX;
    private final double muY;
    private final double combinedAB;

    /*
     * x^a y^b / B(a,b) を計算するとき,
     * 直接的計算では対数でO(a log(a))のオーダーの値が生じるので丸め誤差が怖いことになる.
     * したがって, スターリング近似により或る程度式変形をしておいて, xを代入して計算するようにする.
     * 
     * log(x^a y^b / B(a,b) = a log((a+b)x/a) + b log((a+b)y/b) +
     * (1/2)log(ab/(a+b))
     * - (1/2)log(2π) + f(a+b) - f(a) - f(b)
     * f(a)はlogΓ(a)のスターリング近似の残差であり,
     * f(a) = logΓ(a) - (a - 1/2)log(a) + a - (1/2)log(2π)
     * である.
     */
    /**
     * (1/2)log(ab/(a+b)) - (1/2)log(2π) + f(a+b) - f(a) - f(b)
     */
    private final double residualLogFactor;

    /**
     * パラメータが範囲外ならアサーションエラー
     * 
     * @param a
     * @param b
     */
    ICBetaAtMiddleParam(double a, double b) {
        super();
        if (!(Math.max(a, b) <= ICBetaFactory.UPPER_LIMIT_OF_PARAMETER_AB
                && ICBetaFactory.AB_THRESHOLD_FIRST <= Math.min(a, b)
                && Math.min(a, b) <= ICBetaFactory.AB_THRESHOLD_SECOND)) {
            throw new AssertionError(
                    String.format(
                            "Bug:パラメータが範囲外もしくはthreshold_1st<=Math.min(a, b)<=threshold_2ndでない:(a,b)=(%s,%s)", a, b));
        }

        this.a = a;
        this.b = b;
        this.combinedAB = a + b;
        this.muX = a / this.combinedAB;
        this.muY = b / this.combinedAB;
        double xSigma = Exponentiation.sqrt((this.muX * this.muY) / (this.combinedAB + 1));
        this.xLowerThreshold = this.muX - xSigma;
        this.xUpperThreshold = this.muX + xSigma;
        this.residualLogFactor = 0.5 * Exponentiation.log(this.muX * b) - HALF_LN2PI
                + GammaFunction.lgammaStirlingResidual(this.combinedAB)
                - GammaFunction.lgammaStirlingResidual(a) - GammaFunction.lgammaStirlingResidual(b);
    }

    @Override
    public final double a() {
        return this.a;
    }

    @Override
    public final double b() {
        return this.b;
    }

    @Override
    protected final double oddsValue(double oddsX) {

        double x = 1 / (1 + 1 / oddsX);
        double y = 1 / (1 + oddsX);

        if (x < this.xLowerThreshold) {
            double lcp = ICBContinuedFractionFactor.factorLowerSide(x, this.a, this.b) * coeffToICBeta(x, y);
            return lcp / (1 - lcp);
        } else if (x > this.xUpperThreshold) {
            double ucp = ICBContinuedFractionFactor.factorLowerSide(y, this.b, this.a) * coeffToICBeta(x, y);
            return (1 - ucp) / ucp;
        } else {
            if (x < this.muX) {
                int shift = (int) (this.combinedAB * (x - this.xLowerThreshold)) + 1;
                double lcp = factorLowerSideWithShift(x, y, this.a, this.b, shift) * coeffToICBeta(x, y);
                return lcp / (1 - lcp);
            } else {
                int shift = (int) (this.combinedAB * (this.xUpperThreshold - x)) + 1;
                double ucp = factorLowerSideWithShift(y, x, this.b, this.a, shift) * coeffToICBeta(x, y);
                return (1 - ucp) / ucp;
            }
        }
    }

    /**
     * F(a,b,x)をF(a+s,b-s,x)から計算する. <br>
     * xに制限はないが、x &lt; a/(a+b) を想定し、<br>
     * sによって累積確率 0.15 以下になるように分布を右(高x側)にシフトする.
     *
     * @param y 1-x
     * @param shift s
     */
    private static double factorLowerSideWithShift(double x, double y, double a, double b, int shift) {
        double out = ICBContinuedFractionFactor.factorLowerSide(x, a + shift, b - shift);
        for (int n = shift; n >= 1; n--) {
            out *= (b - n) * x;
            out += 1;
            out /= (a + n - 1) * y;
        }
        return out;
    }

    /*
     * x^a y^b / B(a,b) を返す.
     */
    private double coeffToICBeta(double x, double y) {
        return Exponentiation.exp(
                this.a * Exponentiation.log(x / this.muX) + this.b * Exponentiation.log(y / this.muY)
                        + this.residualLogFactor);
    }
}
