/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.18
 */
package matsu.num.specialfunction.modsbessel;

import matsu.num.commons.Exponentiation;

/**
 * 原理的な1次変形球Besselの計算.
 * 
 * @author Matsuura Y.
 * @version 18.8
 */
final class MSBessel1InPrinciple extends MSBessel1 {

    /**
     * i(x)について, べき級数と原理式とを切り替える閾値. <br>
     * 下側はべき級数を考え, 上側は原理式を用いる.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_PRINCIPLE = 1d;

    /**
     * i(x)について, オーバーフロー対策を行うかどうかを切り替える閾値. <br>
     * 下側上側とも原理式を用いるが, 上側はi(x)がオーバーフローしないがexp(x)がする場合を警戒する.
     */
    private static final double BOUNDARY_X_SELECTING_LOWER_OR_UPPER = 24d;

    /**
     * i(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_BY_POWER = 10;

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

        if (x < BOUNDARY_X_SELECTING_POWER_OR_PRINCIPLE) {
            return msbI_byPower(x);
        }

        double invX = 1 / x;

        if (x < BOUNDARY_X_SELECTING_LOWER_OR_UPPER) {
            double exp = Exponentiation.exp(x);
            double invExp = 1 / exp;
            return ((exp + invExp) - (exp - invExp) * invX) * invX * 0.5;
        }

        double exp = Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);

        return ((EXP_OF_SHIFT_X_FOR_BESSEL_I * 0.5) / x) *
                (exp * (1 - invX) + (INV_SQUARE_EXP_OF_SHIFT_X_FOR_BESSEL_I / exp) * (1 + invX));
    }

    @Override
    public double sbesselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x == Double.POSITIVE_INFINITY) {
            return 0d;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_PRINCIPLE) {
            return msbI_byPower(x) * Exponentiation.exp(-x);
        }

        double invX = 1 / x;
        return (1 - invX + (1 + invX) * Exponentiation.exp(-2 * x)) * invX * 0.5;
    }

    private static double msbI_byPower(double x) {
        final double squareX = x * x;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_POWER + 1; k >= 1; k--) {
            value *= squareX / (2 * k * (2 * k + 3));
            value += 1d;
        }

        return value * x / 3;
    }

    /**
     * k(x) = (1/x + 1/x^2)exp(-x) に従って計算する.
     */
    @Override
    public double sbesselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        double expTimesInvX = Exponentiation.exp(-x) / x;
        double expTimesSqinvX = expTimesInvX / x;

        return Double.isFinite(expTimesSqinvX)
                ? expTimesInvX + expTimesSqinvX
                : Double.POSITIVE_INFINITY;
    }

    /**
     * k(x)exp(x) = 1/x + 1/x^2 に従って計算する.
     */
    @Override
    public double sbesselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        double invX = 1d / x;
        double sqinvX = invX / x;

        return Double.isFinite(sqinvX)
                ? invX + sqinvX
                : Double.POSITIVE_INFINITY;
    }
}
