/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.24
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.common.Exponentiation;

/**
 * 連分数とパラメータシフトを併用した, small, middle <i>a</i> に対する正則化不完全ガンマ関数.
 * 
 * <p>
 * xの領域を3分割し, 下と上は連分数で計算する. <br>
 * 中央はパラメータシフトを行い, 前述に帰着させる.
 * </p>
 *
 * @author Matsuura Y.
 */
final class WithShiftingICGamma extends SkeletalICGamma {

    /*
     * x <= lowerではγ(a,x)の連分数展開,
     * x >= upperではΓ(a,x)の連分数展開を使う.
     * 
     * lower < x <= middle ではパラメータを上にシフトし, 相対xがlower側に移す.
     * middle <= x < upper ではパラメータを下にシフトし, 相対xがupper側に移す.
     */
    private final double xLowerThreshold;
    private final double xMiddleThreshold;
    private final double xUpperThreshold;

    private final CFracBasedIcgammaCalculator fractionCoefficient;

    /**
     * @param a パラメータ
     */
    WithShiftingICGamma(double a) {
        super(a);

        final double X_THRESHOLD_MAX = 4;

        this.xMiddleThreshold = Math.max(a, X_THRESHOLD_MAX);
        double halfWidth_of_middleRange = Math.max(0d, 2 * Exponentiation.sqrt(a) - 10);
        this.xLowerThreshold = this.xMiddleThreshold - halfWidth_of_middleRange;
        this.xUpperThreshold = this.xMiddleThreshold + halfWidth_of_middleRange;

        this.fractionCoefficient = CFracBasedIcgammaCalculator.of(a);
    }

    @Override
    double oddsValue(double x) {
        if (x <= this.xMiddleThreshold) {
            if (x <= this.xLowerThreshold) {
                double lcp = this.fractionCoefficient.calcP(x);
                return lcp / (1 - lcp);
            }

            int shift = (int) (x - this.xLowerThreshold) + 1;
            double lcp = this.fractionCoefficient.calcPByShift(x, shift);
            return lcp / (1 - lcp);
        }

        if (x >= this.xUpperThreshold) {
            double ucp = this.fractionCoefficient.calcQ(x);
            return (1 - ucp) / ucp;
        }

        int shift = (int) (this.xUpperThreshold - x) + 1;
        double ucp = this.fractionCoefficient.calcQByShift(x, shift);
        return (1 - ucp) / ucp;
    }
}
