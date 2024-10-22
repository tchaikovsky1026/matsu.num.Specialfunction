/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.23
 */
package matsu.num.specialfunction.bessel.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.commons.Trigonometry;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.fraction.RealCoeffDoubleComplexCFFunction;
import matsu.num.specialfunction.fraction.DoubleComplexNumber;

/**
 * <p>
 * 素朴な1次のBessel関数の実装.
 * </p>
 * 
 * <p>
 * このクラスは1次Bessel関数の素朴な (原理に忠実な) 実装を扱う. <br>
 * 計算効率などは全く考慮されていないことに注意すべき.
 * </p>
 * 
 * <p>
 * 1次Besselの計算戦略は次の通りである. <br>
 * besselJ, besselYともに, 小さいxではべき級数表示を, 大きいxでは漸近級数の連分数表示を使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.9
 * @deprecated {@link Bessel1Optimized} が提供されたため, 使用されていない.
 */
@Deprecated
final class NaiveBessel1 extends Bessel1st {

    /**
     * アルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数の連分数.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC = 2d;

    /**
     * J(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_J_BY_POWER = 20;

    /**
     * Y(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_Y_BY_POWER = 20;
    /**
     * 
     * [G_0, ..., G_kMax], <br>
     * ただし, G_k = (1/2)(H_k + H_{k+1}) <br>
     * Kのべき級数の計算で使用する.
     */
    private static final double[] PSEUDO_HARMONIC_NUMBERS =
            calcPseudoHarmonicNumbers(K_MAX_FOR_BESSEL_Y_BY_POWER);

    /**
     * {@literal x >= boundaryX} における, H^{(2)}(x)の漸近展開部分を連分数に変換した結果. <br>
     * t = i/(8x) として, P(x) + iQ(x) を得る.
     */
    private static final RealCoeffDoubleComplexCFFunction ASYMPTOTIC_FRACTION =
            NaiveBesselContinuedFraction.createH1Asymptotic();

    /**
     * 唯一のコンストラクタ.
     */
    NaiveBessel1() {
        super();
    }

    @Override
    public double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return bJ_byPower(x);
        }

        return bJ_byAsymptotic(x);
    }

    /**
     * べき級数による J(x)
     */
    private static double bJ_byPower(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_J_BY_POWER + 1; k >= 1; k--) {
            value *= -squareHalfX / (k * (k + 1));
            value += 1d;
        }

        return value * halfX;
    }

    /**
     * 漸近級数の連分数変換による J(x)
     */
    private static double bJ_byAsymptotic(double x) {

        double t = 0.125 / x;
        DoubleComplexNumber factor = ASYMPTOTIC_FRACTION.value(
                DoubleComplexNumber.I.times(DoubleComplexNumber.ofReal(t)));

        double cos = Trigonometry.cos(x - 3 * Math.PI / 4);
        double sin = Trigonometry.sin(x - 3 * Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (factor.real() * cos + factor.imaginary() * sin);
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return bY_byPower(x);
        }

        return bY_byAsymptotic(x);
    }

    /**
     * べき級数による Y(x)
     */
    private static double bY_byPower(double x) {

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;
        final double gamma_plus_logHalfX =
                GammaFunction.EULER_MASCHERONI_GAMMA + Exponentiation.log(halfX);
        if (Double.isInfinite(gamma_plus_logHalfX)) {
            return Double.NEGATIVE_INFINITY;
        }

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_Y_BY_POWER + 1; k >= 1; k--) {
            value *= -squareHalfX / (k * (k + 1));
            value += PSEUDO_HARMONIC_NUMBERS[k - 1] - gamma_plus_logHalfX;
        }

        return -(value * halfX + 1d / x) * 2 / Math.PI;
    }

    /**
     * 漸近級数の連分数変換による Y(x)
     */
    private static double bY_byAsymptotic(double x) {

        double t = 0.125 / x;
        DoubleComplexNumber factor = ASYMPTOTIC_FRACTION.value(
                DoubleComplexNumber.I.times(DoubleComplexNumber.ofReal(t)));

        double cos = Trigonometry.cos(x - 3 * Math.PI / 4);
        double sin = Trigonometry.sin(x - 3 * Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (factor.real() * sin - factor.imaginary() * cos);
    }

    /**
     * [G_0, ..., G_kMax] を返す.
     * ただし, G_k = (1/2)(H_k + H_{k+1})
     */
    private static double[] calcPseudoHarmonicNumbers(int kMax) {
        final double[] harmonicNumbers = new double[kMax + 1];

        double current = 0.5;
        for (int k = 0; k <= kMax; k++) {
            harmonicNumbers[k] = current;
            current += 0.5 * (1d / (k + 1) + 1d / (k + 2));
        }

        return harmonicNumbers;
    }

}
