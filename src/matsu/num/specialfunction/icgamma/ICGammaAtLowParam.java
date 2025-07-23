/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.23
 */
package matsu.num.specialfunction.icgamma;

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

    //計算方法を切り替える閾値
    private final double xThreshold;

    private final CFracBasedIcgammaCalculator fractionCoefficient;

    /**
     * @param a パラメータ
     */
    ICGammaAtLowParam(double a) {
        super(a);

        this.xThreshold = Math.max(a, X_THRESHOLD_MAX);

        this.fractionCoefficient = CFracBasedIcgammaCalculator.of(a);
    }

    @Override
    double oddsValue(double x) {
        if (x < this.xThreshold) {
            double lcp = this.fractionCoefficient.calcP(x);
            return lcp / (1 - lcp);
        }
        double ucp = this.fractionCoefficient.calcQ(x);
        return (1 - ucp) / ucp;
    }
}
