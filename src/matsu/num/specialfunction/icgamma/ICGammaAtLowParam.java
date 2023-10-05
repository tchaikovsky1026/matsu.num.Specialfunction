/**
 * 2023.3.21
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数の計算の実装(およそ倍精度未満), 小さなパラメータに対して. <br>
 * 11以下のパラメータを対象とする. <br>
 * <br>
 * 閾値を境にxが小さい場合と大きい場合で別の連分数システムで計算する. <br>
 * 閾値は, {@code max(a, X_THRESHOLD_MAX)} 
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
final class ICGammaAtLowParam extends SkeletalICGamma implements IncompleteGammaFunction {

    /*
     xの値によって2種の計算方法を用いる. 
     その閾値はaによって決まり, aと7の大きいほうの値を用いる.
     */
    private static final double X_THRESHOLD_MAX = 7;

    private final double a;
    private final double logGammaAp1;

    //計算方法を切り替える閾値
    private final double xThreshold;

    private ICGammaAtLowParam(double a) {
        if (!(a <= ICGammaFactory.K_THRESHOLD_SECOND)) {
            throw new AssertionError(String.format(
                    "Bug:a<=11でない:a=%.16G", a));
        }
        this.a = a;
        this.logGammaAp1 = GammaFunction.lgamma(a + 1);
        this.xThreshold = Math.max(a, X_THRESHOLD_MAX);
    }

    @Override
    public double a() {
        return this.a;
    }

    @Override
    protected double oddsValue(double x) {
        final double thisA = this.a;
        if (x < this.xThreshold) {
            double lcp = ICGContinuedFractionFactor.factorLCP(x, thisA) * coeffToLCP(x);
            return lcp / (1 - lcp);
        }
        double ucp = ICGContinuedFractionFactor.factorUCP(x, thisA) * coeffToLCP(x) * (thisA / x);
        return (1 - ucp) / ucp;
    }

    /**
     * x^a e^{-x}/(Γ(a+1))
     */
    private double coeffToLCP(double x) {
        final double logFactor = this.a * Exponentiation.log(x);
        if (logFactor == Double.POSITIVE_INFINITY) {
            return 0;
        }
        return Exponentiation.exp(this.a * Exponentiation.log(x) - x - this.logGammaAp1);
    }

    /**
     * 
     * @param a パラメータ
     * @return インスタンス
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        return new ICGammaAtLowParam(a);
    }

}
