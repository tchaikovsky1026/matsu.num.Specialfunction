/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.14
 */
package matsu.num.specialfunction.modbessel;

import matsu.num.commons.Exponentiation;

/**
 * <p>
 * 最適化された1次の変形Bessel関数の実装.
 * </p>
 * 
 * <p>
 * 1次MBesselの計算戦略は次の通りである. <br>
 * bessel_Iは, 小さいxではべき級数表示を, 大きいxでは漸近級数を使う. <br>
 * bessel_Kは, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示を区分で分けてminimax近似をする.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
final class MBessel1Optimized extends ModifiedBessel1stOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);
    private static final double SQRT_PI_OVER_2 = Math.sqrt(Math.PI / 2);

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数. <br>
     * 漸近級数ではexp(-x)成分を排除する必要があるため, この閾値は大きくとる.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I = 24d;

    /**
     * K(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数の連分数表示.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K = 2d;

    /**
     * I(x)のべき級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_BY_POWER = 40;

    /**
     * I(x)の漸近級数の項数.
     */
    private static final int K_MAX_FOR_BESSEL_I_BY_ASYMPTOTIC = 15;

    MBessel1Optimized() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return besselI_byPower(x);
        } else {
            double scaling = scaling_besselI_byAsymptotic(x);
            if (scaling == 0d) {
                return Double.POSITIVE_INFINITY;
            }
            return scaling * Exponentiation.exp(x);
        }
    }

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return besselK_byPower(x);
        } else {
            return scaling_besselK_byAsymptoticFraction(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return besselI_byPower(x) * Exponentiation.exp(-x);
        } else {
            return scaling_besselI_byAsymptotic(x);
        }
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return besselK_byPower(x) * Exponentiation.exp(x);
        } else {
            return scaling_besselK_byAsymptoticFraction(x);
        }
    }

    /**
     * べき級数による I(x)
     */
    private static double besselI_byPower(double x) {
        //べき級数により計算する

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_POWER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * (k + 1));
            value += 1d;
        }
        value *= halfX;

        return value;
    }

    /**
     * 漸近級数による I(x)exp(-x)
     */
    private static double scaling_besselI_byAsymptotic(double x) {

        double asymptotic = SQRT_INV_2PI / Exponentiation.sqrt(x);

        final double t = 0.125 / x;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_ASYMPTOTIC + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - 4) / k * t;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * べき級数のミニマックス近似による K(x)
     */
    private static double besselK_byPower(double x) {

        //K1 = (1/x) + (1/2)(x/2)log(u)F(u) - (x/2)G(u)
        //K1 = (1/x) - (x/2)*(-(1/2)log(u)F(u) + G(u))

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        double f_and_g = k1Power_regularTerm(u)
                - 0.5 * Exponentiation.log(u) * k1Power_logTerm(u);
        if (Double.isInfinite(f_and_g)) {
            return Double.POSITIVE_INFINITY;
        }

        return 1d / x - halfX * f_and_g;
    }

    private static double k1Power_logTerm(double u) {
        final double C0 = 1.0;
        final double C1 = 0.49999999999999656;
        final double C2 = 0.08333333333339428;
        final double C3 = 0.006944444443957555;
        final double C4 = 3.472222243282794E-4;
        final double C5 = 1.1574068725115107E-5;
        final double C6 = 2.755813966758E-7;
        final double C7 = 4.913483312969411E-9;
        final double C8 = 7.204724240081212E-11;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    private static double k1Power_regularTerm(double u) {

        final double C0 = -0.07721566490153273;
        final double C1 = 0.3363921675492256;
        final double C2 = 0.09078758348056844;
        final double C3 = 0.009591094918542157;
        final double C4 = 5.576797508407867E-4;
        final double C5 = 2.0711226110891653E-5;
        final double C6 = 5.357918745424145E-7;
        final double C7 = 1.0209217875635326E-8;
        final double C8 = 1.5863002766041774E-10;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_besselK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x)
                * k1_asymptoticTerm(0.125 / x);
    }

    /**
     * {@literal t <= 1/16} における,
     * 漸近級数部分の計算を扱う.
     * 
     * @param t t
     * @return 漸近級数部分の値
     */
    private static double k1_asymptoticTerm(double t) {
        if (t < 1d / 64) {
            return k1_asymptoticTerm_0_to_1Over64(t);
        }
        if (t < 1d / 32) {
            return k1_asymptoticTerm_1Over64_to_1Over32(t);
        }
        return k1_asymptoticTerm_1Over32_to_1Over16(t);
    }

    private static double k1_asymptoticTerm_0_to_1Over64(double t) {
        final double C0 = 1.0;
        final double C1 = 2.999999999999715;
        final double C2 = -7.4999999966397874;
        final double C3 = 52.49999325888449;
        final double C4 = -590.6193457328459;
        final double C5 = 9093.05183935913;
        final double C6 = -176646.8247234349;
        final double C7 = 4049332.8298530686;
        final double C8 = -9.902846697566415E7;
        final double C9 = 2.1988533612929516E9;
        final double C10 = -3.555868452683058E10;
        final double C11 = 2.911183666084078E11;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double k1_asymptoticTerm_1Over64_to_1Over32(double t) {
        final double C0 = 1.0000000028184965;
        final double C1 = 2.999998385327426;
        final double C2 = -7.499572918174702;
        final double C3 = 52.43069018247646;
        final double C4 = -582.8789854481608;
        final double C5 = 8459.728312752948;
        final double C6 = -137225.8369656045;
        final double C7 = 2147852.7531960756;
        final double C8 = -2.8496961917234033E7;
        final double C9 = 2.842391272935273E8;
        final double C10 = -1.8366198297759774E9;
        final double C11 = 5.672938728487763E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double k1_asymptoticTerm_1Over32_to_1Over16(double t) {
        final double C0 = 1.0000002811709148;
        final double C1 = 2.9999168674834347;
        final double C2 = -7.488535140841639;
        final double C3 = 51.514914579114006;
        final double C4 = -530.9388055950313;
        final double C5 = 6337.306538269181;
        final double C6 = -73284.9355827139;
        final double C7 = 725606.696764786;
        final double C8 = -5611755.852491752;
        final double C9 = 3.0994322276068732E7;
        final double C10 = -1.074293956133725E8;
        final double C11 = 1.744729278001707E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
