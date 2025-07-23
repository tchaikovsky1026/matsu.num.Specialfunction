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

import matsu.num.specialfunction.ErrorFuction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 不完全ガンマ関数の計算の実装(およそ倍精度未満). <br>
 * 40000以上のパラメータを対象とする.
 * 
 * <p>
 * xの領域を3分割し, 下と上は連分数システム, 中央はerror function近似(正規分布近似)と微小補正. <br>
 * 閾値は, 分布の&pm;&sigma;.
 * </p>
 *
 * @author Matsuura Y.
 * @deprecated このクラスはプロダクトコード内で使用されていない.
 */
@Deprecated
final class ICGammaAtHighParam extends SkeletalICGamma {

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
     * 40000以上1E28以下でない場合, アサーションエラー.
     * 
     * @param a パラメータ
     */
    ICGammaAtHighParam(double a) {
        super(a);

        this.sqrtA = Exponentiation.sqrt(a);
        this.xLowerThreshold = a - this.sqrtA;
        this.xUpperThreshold = a + this.sqrtA;

        this.fractionCoefficient = CFracBasedIcgammaCalculator.of(a);
    }

    @Override
    double oddsValue(double x) {
        if (x < this.xLowerThreshold) {
            double lcp = this.fractionCoefficient.calcP(x);
            return lcp / (1 - lcp);
        }
        if (x > this.xUpperThreshold) {
            double ucp = this.fractionCoefficient.calcQ(x);
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
}
