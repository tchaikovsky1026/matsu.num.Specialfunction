/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.16
 */
package matsu.num.specialfunction.sbessel;

import matsu.num.commons.Trigonometry;

/**
 * 原理的な1次球Bessel関数.
 * 
 * @author Matsuura Y.
 * @version 18.7
 */
final class SBessel1InPrinciple extends SBessel1 {

    /**
     * j(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は三角関数.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_TRIGONOMETRY = 2d;

    /**
     * j(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_S_BESSEL_J_BY_POWER = 10;

    /**
     * 唯一のコンストラクタ.
     */
    SBessel1InPrinciple() {
        super();
    }

    @Override
    public double sbesselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_TRIGONOMETRY) {
            return sbJ_byPower(x);
        }

        double sin = Trigonometry.sin(x);
        double cos = Trigonometry.cos(x);
        double invX = 1 / x;

        return Double.isFinite(cos) && Double.isFinite(sin)
                ? -invX * (cos - invX * sin)
                : 0d;
    }

    /**
     * べき級数による j(x)
     */
    private static double sbJ_byPower(double x) {
        final double squareX = x * x;

        double value = 0;
        for (int k = K_MAX_FOR_S_BESSEL_J_BY_POWER + 1; k >= 1; k--) {
            value *= -squareX / ((2 * k) * (2 * k + 3));
            value += 1d;
        }

        return value * x / 3d;
    }

    @Override
    public double sbesselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x == 0d) {
            return Double.NEGATIVE_INFINITY;
        }

        double sin = Trigonometry.sin(x);
        double cos = Trigonometry.cos(x);
        double invX = 1 / x;

        return Double.isFinite(cos) && Double.isFinite(sin)
                ? -invX * (sin + cos * invX)
                : 0d;
    }
}
