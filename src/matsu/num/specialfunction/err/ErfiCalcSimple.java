/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.5
 */
package matsu.num.specialfunction.err;

import matsu.num.commons.Exponentiation;

/**
 * 素朴な実装により虚数誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 21.1
 * @deprecated このクラスは使われていない.
 */
@Deprecated
final class ErfiCalcSimple implements ErrorFunctionImaginaryCalculation {

    /**
     * 1/sqrt(pi)
     */
    // = 1d / Math.sqrt(Math.PI)
    private static final double ONE_OVER_SQRT_PI = 0.5641895835477563;

    /**
     * 計算アルゴリズムを切り替えるxの閾値. <br>
     * 下側はerfiを, 上側はerfixを計算する.
     */
    private static final double BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX = 1d;

    /**
     * 計算アルゴリズムを切り替えるxの閾値. <br>
     * 下側はerfixのテイラー級数の連分数を, 上側はerfixの漸近級数を計算する.
     */
    private static final double BOUNDARY_ABS_X_SELECTING_TAYLOR_FRACTION_OR_ASYMPTOTIC = 7d;

    /**
     * xが小さいところのkMax.
     */
    private static final int K_MAX_FOR_SMALL_X_BY_TAYLOR = 50;

    /**
     * xが大きいところ, 連分数でのkMax.
     */
    private static final int K_MAX_FOR_LARGE_X_BY_FRACTION = 1000;

    /**
     * xが大きいところ, 漸近級数でのkMax.
     */
    private static final int K_MAX_FOR_LARGE_X_BY_ASYMPTOTIC = 50;

    /**
     * erfix(x) をもとにした erfi(x) で,
     * exp(x^2)のオーバーフローを回避するために導入するシフト. <br>
     * x^2をシフトする.
     */
    private static final double SHIFT_X2_FOR_ERFIX_TO_ERFI = 20;

    /**
     * exp(shift_x2)
     */
    private static final double EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI =
            Math.exp(SHIFT_X2_FOR_ERFIX_TO_ERFI);

    /**
     * 唯一のコンストラクタ.
     */
    public ErfiCalcSimple() {
        super();
    }

    @Override
    public double erfi(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        final double absX = Math.abs(x);
        if (absX <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x);
        }

        final double x2 = x * x;
        if (x2 < SHIFT_X2_FOR_ERFIX_TO_ERFI) {
            return erfix_largeX(x) * Exponentiation.exp(x2);
        }

        // erfix = 0 なら erfi = inf or -inf: 明らかに exp(x^2) が吹き飛ぶ
        // x^2が或る程度の場合におこる, (小) * (inf) = (finite) に対する対策 
        final double erfix = erfix_largeX(x);
        return erfix == 0d
                ? (x < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)
                : (erfix * Exponentiation.exp(x2 - SHIFT_X2_FOR_ERFIX_TO_ERFI)) * EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI;
    }

    @Override
    public double erfix(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        if (Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x) * Exponentiation.exp(-x * x);
        }

        return erfix_largeX(x);
    }

    /**
     * xが小さいときのerfi(x)
     */
    private double erfi_smallX(double x) {
        assert Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        final double x2 = x * x;

        double value = 0d;
        for (int k = K_MAX_FOR_SMALL_X_BY_TAYLOR + 1; k >= 1; k--) {
            value *= x2 / k;
            value += 1d / (2 * k - 1);
        }

        return (ONE_OVER_SQRT_PI * 2) * x * value;
    }

    /**
     * xが大きいときのerfix(x)
     */
    private double erfix_largeX(double x) {
        assert Math.abs(x) >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        if (Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_TAYLOR_FRACTION_OR_ASYMPTOTIC) {
            return (2 * ONE_OVER_SQRT_PI) * x * erfix_largeX_factor_byFraction(x);
        }

        return (ONE_OVER_SQRT_PI / x) * erfix_largeX_factor_byAsymptotic(x);
    }

    /**
     * erfix(x) = 2/sqrt(pi) * x * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor_byFraction(double x) {
        assert Math.abs(x) >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;
        assert Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_TAYLOR_FRACTION_OR_ASYMPTOTIC;

        final double x2Double = 2 * x * x;

        double value = 1d;
        for (int k = K_MAX_FOR_LARGE_X_BY_FRACTION + 1; k >= 1; k--) {
            value *= x2Double * k / (2 * k - 1) / (2 * k + 1);
            value = k % 2 == 1 ? value : -value;
            value += 1d;
            value = 1 / value;
        }

        return value;
    }

    /**
     * erfix(x) = 1/sqrt(pi) * (1/x) * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor_byAsymptotic(double x) {
        assert Math.abs(x) >= BOUNDARY_ABS_X_SELECTING_TAYLOR_FRACTION_OR_ASYMPTOTIC;

        final double x2Double = 2 * x * x;
        final double t = 1 / x2Double;

        double value = 0d;
        for (int k = K_MAX_FOR_LARGE_X_BY_ASYMPTOTIC + 1; k >= 1; k--) {
            value *= t * (2 * k - 1);
            value += 1d;
        }

        return value;
    }
}
