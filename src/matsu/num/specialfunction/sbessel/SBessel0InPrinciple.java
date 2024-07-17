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
 * 原理的な0次球Bessel関数.
 * 
 * @author Matsuura Y.
 * @version 18.7
 */
final class SBessel0InPrinciple extends SBessel0 {

    private static final double BOUNDARY_X_WHEN_J0_EQUALS_1 = 1E-100;

    /**
     * 唯一のコンストラクタ.
     */
    SBessel0InPrinciple() {
        super();
    }

    @Override
    public double sbesselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_WHEN_J0_EQUALS_1) {
            return 1d;
        }

        double sin = Trigonometry.sin(x);

        return Double.isFinite(sin)
                ? sin / x
                : 0d;
    }

    @Override
    public double sbesselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x == 0d) {
            return Double.NEGATIVE_INFINITY;
        }

        double cos = Trigonometry.cos(x);

        return Double.isFinite(cos)
                ? -cos / x
                : 0d;
    }
}
