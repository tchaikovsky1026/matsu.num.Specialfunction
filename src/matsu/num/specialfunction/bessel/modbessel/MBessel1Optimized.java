/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.27
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;

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
 * @version 19.0
 */
final class MBessel1Optimized extends ModifiedBessel1stOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);
    private static final double SQRT_PI_OVER_2 = Math.sqrt(Math.PI / 2);
    private static final double GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はI(x)のu (= (x/2)^2) に関するべき級数のminimax近似,
     * 上側はI(x)exp(-x)のxに関するべき級数のminimac近似.
     */
    private static final double BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I = 2d;

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
     * 漸近級数によるI(x)で,
     * exp(x)のオーバーフローを回避するために導入するシフト.
     */
    private static final double SHIFT_X_FOR_BESSEL_I = 20;

    /**
     * exp(shift_x)
     */
    private static final double EXP_OF_SHIFT_X_FOR_BESSEL_I = Math.exp(SHIFT_X_FOR_BESSEL_I);

    MBessel1Optimized() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I) {
            final double halfX = x / 2d;
            final double u = halfX * halfX;
            return mbI_PowerTerm(u) * halfX;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x) * Exponentiation.exp(x);
        }

        double scaling = scaling_mbI_byAsymptotic(x);
        if (scaling == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        /*
         * 漸近級数によるI(x)で,
         * I(x)はオーバーフローしないがexp(x)がオーバーフローする場合に対応するため,
         * xを負の方向にシフトする.
         */
        return (scaling * EXP_OF_SHIFT_X_FOR_BESSEL_I) *
                Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);
    }

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x);
        } else {
            return scaling_mbK_byAsymptoticFraction(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I) {
            final double halfX = x / 2d;
            final double u = halfX * halfX;
            return mbI_PowerTerm(u) * halfX * Exponentiation.exp(-x);
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x);
        } else {
            return scaling_mbI_byAsymptotic(x);
        }
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x) * Exponentiation.exp(x);
        } else {
            return scaling_mbK_byAsymptoticFraction(x);
        }
    }

    /**
     * I1(x) = (x/2)F(u) としたときのF(u)
     */
    private static double mbI_PowerTerm(double u) {
        final double C0 = 1.0;
        final double C1 = 0.49999999999999933;
        final double C2 = 0.08333333333335496;
        final double C3 = 0.006944444444205112;
        final double C4 = 3.472222235023415E-4;
        final double C5 = 1.1574070287760461E-5;
        final double C6 = 2.755797132269685E-7;
        final double C7 = 4.914445506868288E-9;
        final double C8 = 7.18208097542048E-11;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    /**
     * {@literal 2 <= x <= 24} について, I(x)exp(-x)をべき級数のminimax近似で計算する.
     */
    private static double scaling_mbI_byPowerExp(double x) {
        assert x >= 2;
        assert x <= 24;

        switch ((int) x) {
        case 2, 3, 4, 5:
            return scaling_mbI_byPowerExp_shifted_2_to_6(x - 2);
        case 6, 7, 8, 9:
            return scaling_mbI_byPowerExp_shifted_6_to_10(x - 6);
        case 10, 11, 12, 13:
            return scaling_mbI_byPowerExp_shifted_10_to_14(x - 10);
        case 14, 15, 16, 17:
            return scaling_mbI_byPowerExp_shifted_14_to_18(x - 14);
        default:
            return scaling_mbI_byPowerExp_shifted_18_to_24(x - 18);
        }
    }

    /**
     * @param u {@literal u = x - 2}
     */
    private static double scaling_mbI_byPowerExp_shifted_2_to_6(double u) {
        final double C0 = 0.21526928924893776;
        final double C1 = -0.01439561131974926;
        final double C2 = -0.008914147006038063;
        final double C3 = 0.007428455833537319;
        final double C4 = -0.0035573477602108094;
        final double C5 = 0.0013131228800252752;
        final double C6 = -4.0329242225912337E-4;
        final double C7 = 1.0680104256006478E-4;
        final double C8 = -2.4924201492011665E-5;
        final double C9 = 5.203921392223091E-6;
        final double C10 = -9.828148365720822E-7;
        final double C11 = 1.6909921316131489E-7;
        final double C12 = -2.6551834190598942E-8;
        final double C13 = 3.778158318455118E-9;
        final double C14 = -4.775662294997401E-10;
        final double C15 = 5.17194760023591E-11;
        final double C16 = -4.542425557710229E-12;
        final double C17 = 2.9849604754239246E-13;
        final double C18 = -1.2857228827093158E-14;
        final double C19 = 2.691821145241169E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    /**
     * @param u {@literal u = x - 6}
     */
    private static double scaling_mbI_byPowerExp_shifted_6_to_10(double u) {
        final double C0 = 0.1520514593085059;
        final double C1 = -0.010735936553441188;
        final double C2 = 0.0010714688142732284;
        final double C3 = -1.0573032102721503E-4;
        final double C4 = 8.07307044539049E-6;
        final double C5 = 7.518988692752148E-8;
        final double C6 = -2.331292363003344E-7;
        final double C7 = 7.210737789355154E-8;
        final double C8 = -1.6515720374661725E-8;
        final double C9 = 3.199964261654611E-9;
        final double C10 = -5.17172090554003E-10;
        final double C11 = 5.497664552789005E-11;
        final double C12 = 3.9398310162316205E-12;
        final double C13 = -4.314130880040043E-12;
        final double C14 = 1.392546924429121E-12;
        final double C15 = -2.907431661143759E-13;
        final double C16 = 4.192795471373361E-14;
        final double C17 = -4.050655293645774E-15;
        final double C18 = 2.3676197835944573E-16;
        final double C19 = -6.3401673630507286E-18;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    /**
     * @param u {@literal u = x - 10}
     */
    private static double scaling_mbI_byPowerExp_shifted_10_to_14(double u) {
        final double C0 = 0.12126268138445552;
        final double C1 = -0.005555612359472155;
        final double C2 = 3.7657231513556346E-4;
        final double C3 = -2.7842213883823998E-5;
        final double C4 = 2.1060600221302598E-6;
        final double C5 = -1.575908323482282E-7;
        final double C6 = 1.1262621490014348E-8;
        final double C7 = -7.25425232718112E-10;
        final double C8 = 4.1344401529598604E-11;
        final double C9 = -6.600224432591169E-12;
        final double C10 = 3.652475526830563E-12;
        final double C11 = -1.326255054304157E-12;
        final double C12 = 1.2736139564461965E-13;
        final double C13 = 1.3226439597319092E-13;
        final double C14 = -8.178739643161974E-14;
        final double C15 = 2.5104879280069183E-14;
        final double C16 = -4.7948854320387236E-15;
        final double C17 = 5.77294237229154E-16;
        final double C18 = -4.038126408517544E-17;
        final double C19 = 1.257181494410686E-18;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    /**
     * @param u {@literal u = x - 14}
     */
    private static double scaling_mbI_byPowerExp_shifted_14_to_18(double u) {
        final double C0 = 0.1036976674631427;
        final double C1 = -0.003489392039810318;
        final double C2 = 1.7505992627752572E-4;
        final double C3 = -9.687278484447036E-6;
        final double C4 = 5.578236216506256E-7;
        final double C5 = -3.2648578196631745E-8;
        final double C6 = 1.8724020951552864E-9;
        final double C7 = -3.05710482162194E-11;
        final double C8 = -9.607939483157049E-11;
        final double C9 = 9.700752356619307E-11;
        final double C10 = -7.07770070617323E-11;
        final double C11 = 3.968248388428452E-11;
        final double C12 = -1.7176810727143017E-11;
        final double C13 = 5.719739295592486E-12;
        final double C14 = -1.4502117801035678E-12;
        final double C15 = 2.7464381180374673E-13;
        final double C16 = -3.7590535884388306E-14;
        final double C17 = 3.5103004259298025E-15;
        final double C18 = -2.0002861211611018E-16;
        final double C19 = 5.24533146351663E-18;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    /**
     * @param u {@literal u = x - 18}
     */
    private static double scaling_mbI_byPowerExp_shifted_18_to_24(double u) {
        final double C0 = 0.09203679687202053;
        final double C1 = -0.002443657041034773;
        final double C2 = 9.699080633129094E-5;
        final double C3 = -4.260676551189654E-6;
        final double C4 = 1.956340731598957E-7;
        final double C5 = -9.189692704477685E-9;
        final double C6 = 4.352730035815067E-10;
        final double C7 = -1.8331449673638002E-11;
        final double C8 = -1.6574295981794648E-12;
        final double C9 = 1.9393613024087163E-12;
        final double C10 = -1.096490353323833E-12;
        final double C11 = 4.56575356089158E-13;
        final double C12 = -1.4388354183335996E-13;
        final double C13 = 3.4402052440117484E-14;
        final double C14 = -6.19888258183969E-15;
        final double C15 = 8.278414078455154E-16;
        final double C16 = -7.942602954356268E-17;
        final double C17 = 5.175233481277015E-18;
        final double C18 = -2.0502537964397078E-19;
        final double C19 = 3.7271205621040715E-21;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    /**
     * 漸近級数のminimax近似による I(x)exp(-x)
     */
    private static double scaling_mbI_byAsymptotic(double x) {
        return scaling_mbI_asymptoticTerm(0.125 / x) * SQRT_INV_2PI / Exponentiation.sqrt(x);
    }

    /**
     * I(x)の漸近級数部分のminimax近似.
     * 
     * {@literal 0 <= t <= 1/192}
     */
    private static double scaling_mbI_asymptoticTerm(double t) {
        final double C0 = 1.0;
        final double C1 = -3.000000000000024;
        final double C2 = -7.499999999367855;
        final double C3 = -52.50000273666424;
        final double C4 = -590.6203914475212;
        final double C5 = -9099.558488884913;
        final double C6 = -175468.81417384805;
        final double C7 = -4714264.998222145;
        final double C8 = -3.042016688878059E7;
        final double C9 = -1.033192096544347E10;

        final double t2 = t * t;
        double v0 = C0 + C1 * t + (C2 + C3 * t) * t2;
        double v4 = C4 + C5 * t + (C6 + C7 * t) * t2;
        double v8 = C8 + C9 * t;

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    /**
     * べき級数のミニマックス近似による K(x)
     */
    private static double mbK_byPower(double x) {

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        double f_and_g = mbK_byPower_HarmonicTerm(u)
                - (GAMMA + Exponentiation.log(halfX)) * mbI_PowerTerm(u);
        if (Double.isInfinite(f_and_g)) {
            return Double.POSITIVE_INFINITY;
        }

        return 1d / x - halfX * f_and_g;
    }

    /**
     * I1(x)の調和数部分の計算
     */
    private static double mbK_byPower_HarmonicTerm(double u) {

        final double C0 = 0.5;
        final double C1 = 0.6249999999999971;
        final double C2 = 0.1388888888889643;
        final double C3 = 0.013599537036263496;
        final double C4 = 7.581018558235424E-4;
        final double C5 = 2.7391963858409945E-5;
        final double C6 = 6.948574962609124E-7;
        final double C7 = 1.3047801156291469E-8;
        final double C8 = 1.9964193229460673E-10;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_mbK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x)
                * mbK_asymptoticTerm(0.125 / x);
    }

    /**
     * {@literal t <= 1/16} における,
     * 漸近級数部分の計算を扱う.
     * 
     * @param t t
     * @return 漸近級数部分の値
     */
    private static double mbK_asymptoticTerm(double t) {
        if (t < 1d / 64) {
            return mbK_asymptoticTerm_0_to_1_Over64(t);
        }
        if (t < 1d / 32) {
            return mbK_asymptoticTerm_1_to_2_Over64(t);
        }
        return mbK_asymptoticTerm_2_to_4_Over64(t);
    }

    private static double mbK_asymptoticTerm_0_to_1_Over64(double t) {
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

    private static double mbK_asymptoticTerm_1_to_2_Over64(double t) {
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

    private static double mbK_asymptoticTerm_2_to_4_Over64(double t) {
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
