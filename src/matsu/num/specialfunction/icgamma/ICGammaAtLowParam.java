/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.6
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 不完全ガンマ関数の計算の実装(およそ倍精度未満). <br>
 * 0.01以上11以下のパラメータを対象とする.
 * 
 * <p>
 * 閾値を境にxが小さい場合と大きい場合で別の連分数システムで計算する. <br>
 * 閾値は, {@code max(a, X_THRESHOLD_MAX)}
 * </p>
 * 
 * @author Matsuura Y.
 */
final class ICGammaAtLowParam extends SkeletalICGamma {

    /*
     * xの値によって2種の計算方法を用いる.
     * その閾値はaによって決まり, aと7の大きいほうの値を用いる.
     */
    private static final double X_THRESHOLD_MAX = 4;

    private final double logGammaAp1;

    //計算方法を切り替える閾値
    private final double xThreshold;

    /**
     * @param a パラメータ
     */
    ICGammaAtLowParam(double a) {
        super(a);

        this.logGammaAp1 = GammaFunction.lgamma(a + 1);
        this.xThreshold = Math.max(a, X_THRESHOLD_MAX);
    }

    @Override
    double oddsValue(double x) {
        final double thisA = this.a;
        if (x < this.xThreshold) {
            double lcp = ICGContinuedFractionFactor.factorLCP(x, thisA) * coeffToLCP(x);
            return lcp / (1 - lcp);
        }
        double ucp = ICGContinuedFractionFactor.factorUCP(x, thisA) * this.coeffToLCP(x) * (thisA / x);
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

}
