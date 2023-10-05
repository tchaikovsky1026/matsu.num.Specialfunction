/**
 * 2023.3.22
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.ErrorFuction;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数の計算の実装(およそ倍精度未満), 大パラメータに対して. <br>
 * 40000以上のパラメータを対象とする. <br>
 * <br>
 * xの領域を3分割し, 下と上は連分数システム, 中央はerror function近似(正規分布近似)と微小補正. <br>
 * 閾値は, 分布の&pm;&sigma;.
 *
 * @author Matsuura Y.
 * @version 11.0
 */
final class ICGammaAtHighParam extends SkeletalICGamma implements IncompleteGammaFunction {

    private static final double HALF_LN2PI = 0.5 * Math.log(2 * Math.PI);

    private final double a;
    private final double sqrtA;

    /*
     x < lowerThresholdではγ(a,x)の連分数展開, 
     x > upperThresholdではγ(a,x)の連分数展開を使う.
     そうでない場合はerror function近似(正規分布近似)と微小補正を用いる.
     */
    private final double xLowerThreshold;
    private final double xUpperThreshold;

    /*
     x^a e^{-x}/(aΓ(a))を計算するとき, 
     直接的計算では対数でa log(a)のオーダーの値が生じるが, 計算結果はa程度になる. 
     これはすなわち, 丸め誤差が怖いことになる. 
     したがって, スターリング近似により或る程度式変形をしておいて, xを代入して計算するようにする. 
     
     log(x^a e^{-x}/(aΓ(a))) = alog(x) - x - log(a) - logΓ(a)
                          = alog(x/a) - (x - a) - (1/2)log(a) - (1/2)log(2π) - f(a)
     f(a)はlogΓ(a)のスターリング近似の残差であり, 
     f(a) = logΓ(a) - (a - 1/2)log(a) + a - (1/2)log(2π)
     である.
     */
    /**
     * residualLogFactor = - (1/2)log(a) - (1/2)log(2π) - f(a)
     */
    private final double residualLogFactor;

    private ICGammaAtHighParam(double a) {
        if (!(ICGammaFactory.K_THRESHOLD_THIRD <= a)) {
            throw new AssertionError(String.format(
                    "Bug:40000<=aでない:a=%.16G", a));
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
        return this.a;
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
        double lcp = aproxLCPByNDist(x);
        return lcp / (1 - lcp);
    }

    private double aproxLCPByNDist(double x) {
        final double invSqrt2 = 0.70710678118654752440084436210485d;
        final double thisSqrtA = this.sqrtA;

        double invSqrtK = 1 / thisSqrtA;
        double z = 2 * (Exponentiation.sqrt(x) - (thisSqrtA - 0.125 * invSqrtK));
        double z2 = z * z;
        double z4 = z2 * z2;

        final double N0 = 0.0332161932883759;
        final double N2 = -0.0497165328032498;
        final double N4 = 0.0200458480294013;
        final double N6 = -0.00356414776983789;
        double corr = (N0 + z2 * N2 + z4 * (N4 + z2 * N6)) * invSqrtK;

        return corr + 0.5 * ErrorFuction.erfc(-z * invSqrt2);
    }

    /**
     * x^a e^{-x}/(aΓ(a))を計算する.
     */
    private double coeffToLCP(double x) {
        final double thisA = this.a;
        final double logFactor = thisA * Exponentiation.log(x / thisA);
        if (logFactor == Double.POSITIVE_INFINITY) {
            return 0;
        }
        return Exponentiation.exp(logFactor - (x - thisA) + this.residualLogFactor);
    }

    /**
     * 
     * @param a パラメータ
     * @return インスタンス
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        return new ICGammaAtHighParam(a);
    }

}
