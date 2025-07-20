/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.20
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.ErrorFuction;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * Temme による漸近展開法を利用した, large <i>a</i> に対する正則化不完全ガンマ関数.
 * 
 * <p>
 * xの領域を3分割し, 下と上は連分数システム, <br>
 * 中央は Temme による漸近展開法: <br>
 * N. M. Temme, The asymptotic expansions of the incomplete gamma functions,
 * SIAM
 * J. Math. Anal. 10 (1979) 757-766.
 * </p>
 * 
 * <p>
 * Temme 法: <br>
 * &lambda; = x/a, &mu; = &lambda;-1, &eta; = sgn(&mu;)
 * &radic;[2(&mu;-log(1+&mu;))]
 * として, <br>
 * P(a,x) = (1/2)erfc(-&eta;&radic;(a/2)) - R(a,&eta;) <br>
 * Q(a,x) = (1/2)erfc(&eta;&radic;(a/2)) + R(a,&eta;) <br>
 * とし, <br>
 * R(a,&eta;) = (1/&radic;(2&pi;a)) exp[-(1/2)a&eta;<sup>2</sup>]
 * &times;
 * &Sigma;<sub>k&ge;0</sub> &Sigma;<sub>n&ge;0</sub>
 * c<sub>n</sub><sup>k</sup> &eta;<sup>n</sup> a<sup>-k</sup>
 * </p>
 * 
 * @author Matsuura Y.
 */
final class TemmeTypeICGamma extends SkeletalICGamma {

    /**
     * (1/2) log(2&pi;)
     */
    private static final double HALF_LN2PI = 0.5 * Math.log(2 * Math.PI);

    private static final double SQRT_2 = 1.41421356237309504887;

    /**
     * 閾値の拡張係数.
     * 
     * <p>
     * &mu; &pm; k&sigma; の外では連分数を使用する. <br>
     * その k を与える.
     * </p>
     */
    private static final double THRESHOLD_EXTEND_COEFF = 5d;

    /**
     * c_n^k を表現する.
     */
    private static final double[][] baseC = {
            {
                    -0.3333333333333333, -0.001851851851851851, 0.004133597883597886, 6.494341563786E-4,
                    -8.618882909167122E-4 },
            {
                    0.08333333333333333, -0.0034722222222222246, -0.0026813271604938273, 2.2947209362139987E-4,
                    7.840392217200663E-4 },
            {
                    -0.014814814814814814, 0.0026455026455026462, 7.716049382716045E-4, -4.691894943952558E-4,
                    -2.9907248030318985E-4 },
            {
                    0.0011574074074074065, -9.902263374485596E-4, 2.009387860082551E-6, 2.677206320628387E-4,
                    -1.463845257884501E-6 },
            {
                    3.527336860670196E-4, 2.0576131687242784E-4, -1.0736653226365163E-4, -7.561801671883968E-5,
                    6.641498215465125E-5 },
            {
                    -1.787551440329218E-4, -4.018775720164103E-7, 5.29234488291201E-5, -2.396505113867617E-7,
                    -3.9683650471794334E-5 },
            {
                    3.9192631785224356E-5, -1.809855033448998E-5, -1.2760635188618715E-5, 1.1082654115347308E-5,
                    1.1375726970678405E-5 },
            {
                    -2.1854485106799848E-6, 7.649160916081106E-6, 3.423578734095679E-8, -5.674952826991595E-6,
                    2.5074972262936054E-10 }
    };

    /*
     * x < lowerThresholdではγ(a,x)の連分数展開,
     * x > upperThresholdではγ(a,x)の連分数展開を使う.
     */
    private final double xLowerThreshold;
    private final double xUpperThreshold;

    /**
     * residualLogFactor = - (1/2)log(a) - (1/2)log(2&pi;) - f(a) <br>
     * (f(a) はスターリング近似の残差)
     */
    private final double residualLogFactor;

    private final double sqrtA;

    /**
     * c_n =
     * &Sigma;<sub>k&ge;0</sub>
     * c<sub>n</sub><sup>k</sup> a<sup>-k</sup> <br>
     * を扱う.
     */
    private final double[] c;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 引数のバリデーションは行われていない.
     * </p>
     */
    TemmeTypeICGamma(double a) {
        super(a);

        double sqrtA = Exponentiation.sqrt(a);
        this.xLowerThreshold = a - THRESHOLD_EXTEND_COEFF * sqrtA;
        this.xUpperThreshold = a + THRESHOLD_EXTEND_COEFF * sqrtA;

        this.residualLogFactor = -0.5 * Exponentiation.log(a) - HALF_LN2PI
                - GammaFunction.lgammaStirlingResidual(a);

        this.c = calcC(a);
        this.sqrtA = sqrtA;
    }

    private static double[] calcC(double a) {
        double invA = 1d / a;

        double[] c = new double[baseC.length];
        for (int n = 0; n < c.length; n++) {
            double[] baseC_n = baseC[n];
            double v = 0;
            for (int k = baseC_n.length - 1; k >= 0; k--) {
                v *= invA;
                v += baseC_n[k];
            }
            c[n] = v;
        }
        return c;
    }

    @Override
    double oddsValue(double x) {
        final double thisA = this.a;
        if (x < this.xLowerThreshold) {
            double lcp = ICGContinuedFractionFactor.factorLCP(x, thisA)
                    * coeffToLCP(x);
            return lcp / (1 - lcp);
        }
        if (x > this.xUpperThreshold) {
            double ucp = ICGContinuedFractionFactor.factorUCP(x, thisA)
                    * coeffToLCP(x) * (thisA / x);
            return (1 - ucp) / ucp;
        }

        // 中間領域の計算
        double lambda = x / thisA;
        double mu = lambda - 1;
        double eta = Exponentiation.sqrt(2 * (mu - Exponentiation.log1p(mu)));
        if (Double.isNaN(eta)) {
            eta = 0d;
        }
        if (mu < 0d) {
            eta = -eta;
        }

        if (x < thisA) {
            double lcp = 0.5 * ErrorFuction.erfc(-eta * sqrtA * (0.5 * SQRT_2)) - this.Ra(eta);
            return lcp / (1 - lcp);
        } else {
            double ucp = 0.5 * ErrorFuction.erfc(eta * sqrtA * (0.5 * SQRT_2)) + this.Ra(eta);
            return (1 - ucp) / ucp;
        }
    }

    private double Ra(double eta) {

        double[] thisC = this.c;
        double v = 0;
        for (int n = thisC.length - 1; n >= 0; n--) {
            v *= eta;
            v += thisC[n];
        }

        double inv_sqrt_2pi = 0.39894228040143267793994605993;
        return v * inv_sqrt_2pi / sqrtA * Exponentiation.exp(-0.5 * a * eta * eta);
    }

    /**
     * @return x^a exp(-x) / (a &Gamma;(a))
     */
    private double coeffToLCP(double x) {

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

        final double thisA = this.a;
        final double logFactor = thisA * Exponentiation.log(x / thisA);
        if (logFactor == Double.POSITIVE_INFINITY) {
            return 0;
        }
        return Exponentiation.exp(logFactor - (x - thisA) + this.residualLogFactor);
    }
}
