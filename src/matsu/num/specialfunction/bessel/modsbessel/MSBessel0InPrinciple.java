/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modsbessel;

import matsu.num.commons.Exponentiation;

/**
 * 原理的な0次変形球Besselの計算.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
final class MSBessel0InPrinciple extends MSBessel0 {

    /**
     * i(x)について, オーバーフロー対策を行うかどうかを切り替える閾値. <br>
     * 下側は高精度なsinh(x)を考え,
     * 上側はi(x)がオーバーフローしないがexp(x)がする場合を警戒する.
     */
    private static final double BOUNDARY_X_SELECTING_LOWER_OR_UPPER = 24d;

    /**
     * i_0(x)=1 と無条件に見なす閾値
     */
    private static final double BOUNDARY_X_WHEN_I0_EQUALS_1 = 1E-100;

    /**
     * 漸近級数によるI(x)で,
     * exp(x)のオーバーフローを回避するために導入するシフト.
     */
    private static final double SHIFT_X_FOR_BESSEL_I = 20;

    /**
     * exp(shift_x)
     */
    private static final double EXP_OF_SHIFT_X_FOR_BESSEL_I = Math.exp(SHIFT_X_FOR_BESSEL_I);

    /**
     * 1/exp(2*shift_x)
     */
    private static final double INV_SQUARE_EXP_OF_SHIFT_X_FOR_BESSEL_I =
            1d / (EXP_OF_SHIFT_X_FOR_BESSEL_I * EXP_OF_SHIFT_X_FOR_BESSEL_I);

    /**
     * i(x) = sinh(x)/xに従って計算する.
     */
    @Override
    public double sbesselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x == Double.POSITIVE_INFINITY) {
            return Double.POSITIVE_INFINITY;
        }
        if (x < BOUNDARY_X_WHEN_I0_EQUALS_1) {
            return 1d;
        }

        if (x < BOUNDARY_X_SELECTING_LOWER_OR_UPPER) {
            double expm1 = Exponentiation.expm1(x);
            return (expm1 * (1 + 0.5 * expm1)) / (x * (1 + expm1));
        }

        double exp = Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);

        /*
         * オーバーフローを警戒した結果である.
         */
        return ((EXP_OF_SHIFT_X_FOR_BESSEL_I * 0.5) / x) *
                (exp - (INV_SQUARE_EXP_OF_SHIFT_X_FOR_BESSEL_I / exp));

    }

    @Override
    public double sbesselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_WHEN_I0_EQUALS_1) {
            return 1d;
        }

        if (x == Double.POSITIVE_INFINITY) {
            return 0d;
        }

        double expm1 = Exponentiation.expm1(-2 * x);
        return -expm1 * 0.5 / x;
    }

    /**
     * k(x) = exp(-x)/xに従って計算する.
     */
    @Override
    public double sbesselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        return Exponentiation.exp(-x) / x;
    }

    /**
     * k(x)exp(x) = 1/xに従って計算する.
     */
    @Override
    public double sbesselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        return 1 / x;
    }
}
