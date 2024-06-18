/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteGammaFunction;

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
 * @version 18.0
 */
final class ICGammaAtLowParam
        extends SkeletalICGamma implements IncompleteGammaFunction {

    /*
     * xの値によって2種の計算方法を用いる.
     * その閾値はaによって決まり, aと7の大きいほうの値を用いる.
     */
    private static final double X_THRESHOLD_MAX = 7;

    private final double a;
    private final double logGammaAp1;

    //計算方法を切り替える閾値
    private final double xThreshold;

    /**
     * 0.01以上11以下でない場合, アサーションエラー.
     * 
     * @param a パラメータ
     */
    ICGammaAtLowParam(double a) {
        super();
        if (!(ICGammaFactory.LOWER_LIMIT_OF_PARAMETER_A <= a
                && a <= ICGammaFactory.K_THRESHOLD_SECOND)) {
            throw new AssertionError(
                    String.format(
                            "Bug: 1E-2 <= a <= 11でない: a =%s", a));
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
