/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.18
 */
package matsu.num.specialfunction.modbessel;

import matsu.num.commons.Exponentiation;

/**
 * <p>
 * 最適化された0次の変形Bessel関数の実装.
 * </p>
 * 
 * <p>
 * 0次MBesselの計算戦略は次の通りである. <br>
 * bessel_Iは, 小さいxではべき級数表示を, 大きいxでは漸近級数を使う. <br>
 * bessel_Kは, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示を区分で分けてminimax近似をする.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.8
 */
final class MBessel0Optimized extends ModifiedBessel0thOrder {

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

    /**
     * 漸近級数によるI(x)で,
     * exp(x)のオーバーフローを回避するために導入するシフト.
     */
    private static final double SHIFT_X_FOR_BESSEL_I = 20;

    /**
     * exp(shift_x)
     */
    private static final double EXP_OF_SHIFT_X_FOR_BESSEL_I = Math.exp(SHIFT_X_FOR_BESSEL_I);

    MBessel0Optimized() {
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
            double scaling_I = scaling_besselI_byAsymptotic(x);
            if (scaling_I == 0d) {
                return Double.POSITIVE_INFINITY;
            }

            /*
             * 漸近級数によるI(x)で,
             * I(x)はオーバーフローしないがexp(x)がオーバーフローする場合に対応するため,
             * xを負の方向にシフトする.
             */
            return (scaling_I * EXP_OF_SHIFT_X_FOR_BESSEL_I) *
                    Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);
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

        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_I_BY_POWER + 1; k >= 1; k--) {
            value *= squareHalfX / (k * k);
            value += 1d;
        }

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

            value *= (double) (k2m1 * k2m1) / k * t;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * べき級数による K(x)
     */
    private static double besselK_byPower(double x) {

        //K0 = -(1/2)log(u)F(u) + G(u)

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        return k0Power_regularTerm(u) - 0.5 * Exponentiation.log(u) * k0Power_logTerm(u);
    }

    private static double k0Power_logTerm(double u) {
        final double C0 = 1.0;
        final double C1 = 0.9999999999999933;
        final double C2 = 0.25000000000021694;
        final double C3 = 0.027777777775377384;
        final double C4 = 0.0017361111239515487;
        final double C5 = 6.944440646310419E-5;
        final double C6 = 1.929077756736081E-6;
        final double C7 = 3.93023683506954E-8;
        final double C8 = 6.499398284291608E-10;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    private static double k0Power_regularTerm(double u) {

        final double C0 = -0.5772156649015329;
        final double C1 = 0.4227843350984675;
        final double C2 = 0.23069608377460774;
        final double C3 = 0.03489215745652949;
        final double C4 = 0.0026147876183248045;
        final double C5 = 1.184803951415775E-4;
        final double C6 = 3.612621203060947E-6;
        final double C7 = 7.935447177820776E-8;
        final double C8 = 1.314170969947254E-9;
        final double C9 = 1.8149358918192343E-11;

        double u2 = u * u;

        double v6 = C6 + C7 * u + (C8 + (C9 * u)) * u2;
        double v2 = C2 + C3 * u + (C4 + (C5 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + C1 * u + u2 * (v2 + u4 * v6);
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_besselK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x) * k0_asymptoticTerm(0.125 / x);
    }

    /**
     * {@literal t <= 1/16} における,
     * 漸近級数部分の計算を扱う.
     * 
     * @param t t
     * @return 漸近級数部分の値
     */
    private static double k0_asymptoticTerm(double t) {
        if (t < 1d / 64) {
            return k0_asymptoticTerm_0_to_1Over64(t);
        }
        if (t < 1d / 32) {
            return k0_asymptoticTerm_1Over64_to_1Over32(t);
        }
        return k0_asymptoticTerm_1Over32_to_1Over16(t);
    }

    private static double k0_asymptoticTerm_0_to_1Over64(double t) {
        final double C0 = 1.0;
        final double C1 = -0.9999999999997432;
        final double C2 = 4.499999996970687;
        final double C3 = -37.49999392024639;
        final double C4 = 459.36989824457663;
        final double C5 = -7439.552091816044;
        final double C6 = 149429.34695351167;
        final double C7 = -3504500.6252953555;
        final double C8 = 8.70009710895411E7;
        final double C9 = -1.9495008701041234E9;
        final double C10 = 3.169291361529231E10;
        final double C11 = -2.6026479800625452E11;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double k0_asymptoticTerm_1Over64_to_1Over32(double t) {
        final double C0 = 0.9999999975337694;
        final double C1 = -0.999998586118138;
        final double C2 = 4.499625695994859;
        final double C3 = -37.439187252787306;
        final double C4 = 452.56867979327046;
        final double C5 = -6882.020534258428;
        final double C6 = 114642.04230612976;
        final double C7 = -1821625.5800381938;
        final double C8 = 2.4379826683326967E7;
        final double C9 = -2.444544797455551E8;
        final double C10 = 1.5848811182150767E9;
        final double C11 = -4.90662090296975E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double k0_asymptoticTerm_1Over32_to_1Over16(double t) {
        final double C0 = 0.99999976052488;
        final double C1 = -0.9999291064240587;
        final double C2 = 4.490206809314238;
        final double C3 = -36.65665667820883;
        final double C4 = 408.1169436791407;
        final double C5 = -5062.504188685543;
        final double C6 = 59727.83909107563;
        final double C7 = -597928.3705232941;
        final double C8 = 4654418.888607533;
        final double C9 = -2.581230794853533E7;
        final double C10 = 8.971391732890145E7;
        final double C11 = -1.4598427306131825E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
