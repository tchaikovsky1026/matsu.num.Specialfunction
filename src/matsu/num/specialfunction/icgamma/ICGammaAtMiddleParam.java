/**
 * 2023.12.6
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数の計算の実装(およそ倍精度未満). <br>
 * 11から40000のパラメータを対象とする.
 * 
 * <p>
 * xの領域を3分割し, 下と上は連分数システム, 中央はシフト付き連分数. <br>
 * 閾値は, 分布の&pm;&sigma;.
 * </p>
 *
 * @author Matsuura Y.
 * @version 17.0
 */
final class ICGammaAtMiddleParam
        extends SkeletalICGamma implements IncompleteGammaFunction {

    private static final double HALF_LN2PI = 0.5 * Math.log(2 * Math.PI);

    private final double a;
    private final double sqrtA;

    /*
     * x < lowerThresholdではγ(a,x)の連分数展開,
     * x > upperThresholdではγ(a,x)の連分数展開を使う.
     * そうでない場合はerror function近似(正規分布近似)と微小補正を用いる.
     */
    private final double xLowerThreshold;
    private final double xUpperThreshold;

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

    /**
     * 11以上40000以下でない場合, アサーションエラー.
     * 
     * @param a パラメータ
     */
    ICGammaAtMiddleParam(double a) {
        super();
        if (!(ICGammaFactory.K_THRESHOLD_SECOND <= a
                && a <= ICGammaFactory.K_THRESHOLD_THIRD)) {
            throw new AssertionError(
                    String.format(
                            "Bug: 11 <= a <= 40000 でない:a = %s", a));
        }

        this.a = a;
        this.sqrtA = Exponentiation.sqrt(a);
        this.xLowerThreshold = a - this.sqrtA;
        this.xUpperThreshold = a + this.sqrtA;
        this.residualLogFactor = -0.5 * Exponentiation.log(a) - HALF_LN2PI
                - GammaFunction.lgammaStirlingResidual(a);
    }

    @Override
    public double a() {
        return a;
    }

    @Override
    protected double oddsValue(double x) {
        final double thisA = this.a;

        if (x < this.xLowerThreshold) {
            double lcp = ICGContinuedFractionFactor.factorLCP(x, thisA) * coeffToLCP(x);
            return lcp / (1 - lcp);
        }
        if (x > this.xUpperThreshold) {
            double ucp = ICGContinuedFractionFactor.factorUCP(x, thisA) * coeffToLCP(x) * (thisA / x);
            return (1 - ucp) / ucp;
        }

        if (x < thisA) {
            int shift = (int) (x - this.xLowerThreshold) + 1;
            double lcp = factorLCPWithShift(x, thisA, shift) * coeffToLCP(x);
            return lcp / (1 - lcp);
        }
        int shift = (int) (this.xUpperThreshold - x) + 1;
        double ucp = factorUCPWithShift(x, thisA, shift) * coeffToLCP(x) * (thisA / x);
        return (1 - ucp) / ucp;
    }

    private static double factorLCPWithShift(double x, double a, int shift) {
        double out = ICGContinuedFractionFactor.factorLCP(x, a + shift);
        for (int n = shift; n >= 1; n--) {
            out *= x / (a + n);
            out += 1;
        }
        return out;
    }

    private static double factorUCPWithShift(double x, double a, int shift) {
        double out = ICGContinuedFractionFactor.factorUCP(x, a - shift);
        double invX = 1 / x;
        for (int n = shift; n >= 1; n--) {
            out *= (a - n) * invX;
            out += 1;
        }
        return out;
    }

    /**
     * x^a e^{-x}/(aΓ(a))
     */
    private double coeffToLCP(double x) {
        final double thisA = this.a;
        final double logFactor = thisA * Exponentiation.log(x / thisA);
        if (logFactor == Double.POSITIVE_INFINITY) {
            return 0;
        }
        return Exponentiation.exp(logFactor - (x - thisA) + this.residualLogFactor);
    }
}
