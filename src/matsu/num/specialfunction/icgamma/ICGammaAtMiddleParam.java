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

import matsu.num.specialfunction.common.Exponentiation;

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
 */
final class ICGammaAtMiddleParam extends SkeletalICGamma {

    private final double sqrtA;

    /*
     * x < lowerThresholdではγ(a,x)の連分数展開,
     * x > upperThresholdではγ(a,x)の連分数展開を使う.
     * そうでない場合はerror function近似(正規分布近似)と微小補正を用いる.
     */
    private final double xLowerThreshold;
    private final double xUpperThreshold;

    private final CFracBasedIcgammaCalculator fractionCoefficient;

    /**
     * @param a パラメータ
     */
    ICGammaAtMiddleParam(double a) {
        super(a);

        this.sqrtA = Exponentiation.sqrt(a);
        this.xLowerThreshold = a - this.sqrtA;
        this.xUpperThreshold = a + this.sqrtA;

        this.fractionCoefficient = CFracBasedIcgammaCalculator.of(a);
    }

    @Override
    double oddsValue(double x) {
        final double thisA = this.a;

        if (x < this.xLowerThreshold) {
            double lcp = this.fractionCoefficient.calcP(x);
            return lcp / (1 - lcp);
        }
        if (x > this.xUpperThreshold) {
            double ucp = this.fractionCoefficient.calcQ(x);
            return (1 - ucp) / ucp;
        }

        if (x < thisA) {
            int shift = (int) (x - this.xLowerThreshold) + 1;
            double lcp = this.fractionCoefficient.calcPByShift(x, shift);
            return lcp / (1 - lcp);
        }
        int shift = (int) (this.xUpperThreshold - x) + 1;
        double ucp = this.fractionCoefficient.calcQByShift(x, shift);
        return (1 - ucp) / ucp;
    }
}
