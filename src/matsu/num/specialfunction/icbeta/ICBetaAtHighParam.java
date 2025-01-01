/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.specialfunction.ErrorFuction;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満). <br>
 * Min(a,b)が40000以上を扱う.
 * 
 * <p>
 * ある閾値を境に, I(a,b,x)とI(b,a,1-x)のどちらを計算するかを切り替える. <br>
 * 分布の裾は連分数システム, 分布の中央の場合はerror function近似(正規分布近似)と微小補正.
 * </p>
 *
 * @author Matsuura Y.
 * @version 22.0
 */
final class ICBetaAtHighParam extends SkeletalICBeta {

    private static final double HALF_LN2PI = 0.9189385332046727417804d;

    private static final double INV_SQRT_2 = 0.70710678118654752440084436210485d;

    private final double a;
    private final double b;

    private final double xSigma;
    private final double xLowerThreshold;
    private final double xUpperThreshold;

    private final double muX;
    private final double muY;

    private final double normApproxCoeff;

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
     * パラメータが範囲外でアサーションエラー
     * 
     * @param a
     * @param b
     */
    ICBetaAtHighParam(double a, double b) {
        super();

        this.a = a;
        this.b = b;
        double combinedAB = a + b;
        this.muX = a / combinedAB;
        this.muY = b / combinedAB;
        this.xSigma = Exponentiation.sqrt((this.muX * this.muY) / (combinedAB + 1));
        this.xLowerThreshold = this.muX - this.xSigma;
        this.xUpperThreshold = this.muX + this.xSigma;
        this.residualLogFactor = 0.5 * Exponentiation.log(this.muX * b) - HALF_LN2PI
                + GammaFunction.lgammaStirlingResidual(combinedAB)
                - GammaFunction.lgammaStirlingResidual(a) - GammaFunction.lgammaStirlingResidual(b);
        this.normApproxCoeff = (a - b) / (this.muX * this.muY * combinedAB) * this.xSigma;
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
            double lcp = aproximate_by_normalDistribution(x, y);
            return lcp / (1 - lcp);
        }
    }

    private double aproximate_by_normalDistribution(double x, double y) {
        double delta = this.muX < 0.5 ? x - this.muX : this.muY - y;
        double z = delta / this.xSigma;
        double z2 = z * z;
        double z4 = z2 * z2;

        final double N0 = -0.132924510014094;
        final double N2 = 0.198147272614993;
        final double N4 = -0.0778949172830381;
        final double N6 = 0.0126451190908071;
        double corr = (N0 + z2 * N2 + z4 * (N4 + z2 * N6)) * this.normApproxCoeff;

        return corr + 0.5 * ErrorFuction.erfc(-z * INV_SQRT_2);
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
