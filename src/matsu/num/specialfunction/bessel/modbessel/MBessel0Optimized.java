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
 * @version 19.0
 */
final class MBessel0Optimized extends ModifiedBessel0thOrder {

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
     * I(x)exp(-x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数.
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

    MBessel0Optimized() {
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
            return mbI_byPowerAccurate(u);
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x) * Exponentiation.exp(x);
        }

        double scaling_I = scaling_mbI_byAsymptotic(x);
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

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x);
        }
        return scaling_mbK_byAsymptoticFraction(x) * Exponentiation.exp(-x);
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I) {
            final double halfX = x / 2d;
            final double u = halfX * halfX;
            return mbI_byPowerAccurate(u) * Exponentiation.exp(-x);
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x);
        }
        return scaling_mbI_byAsymptotic(x);
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x) * Exponentiation.exp(x);
        }
        return scaling_mbK_byAsymptoticFraction(x);
    }

    /**
     * I(x)の計算.
     * u = (x/2)^2 として, I0(x) = F(u)とし, F(u)をminimax近似で計算する.
     */
    private static double mbI_byPowerAccurate(double u) {

        final double C0 = 1.0;
        final double C1 = 0.9999999999999933;
        final double C2 = 0.2500000000002169;
        final double C3 = 0.027777777775377915;
        final double C4 = 0.0017361111239490425;
        final double C5 = 6.944440646932372E-5;
        final double C6 = 1.92907774838606E-6;
        final double C7 = 3.930237407506752E-8;
        final double C8 = 6.499382599925947E-10;

        double u2 = u * u;

        double v6 = C6 + C7 * u + C8 * u2;
        double v2 = C2 + C3 * u + (C4 + (C5 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + C1 * u + u2 * (v2 + u4 * v6);
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
        final double C0 = 0.30850832255367094;
        final double C1 = -0.09323903330472276;
        final double C2 = 0.039421710992140854;
        final double C3 = -0.0161119526614073;
        final double C4 = 0.005885102090790449;
        final double C5 = -0.0018884898283619998;
        final double C6 = 5.336017128188161E-4;
        final double C7 = -1.3384120576810267E-4;
        final double C8 = 3.0079088123429375E-5;
        final double C9 = -6.110164127981963E-6;
        final double C10 = 1.1303049122370081E-6;
        final double C11 = -1.9138407197906914E-7;
        final double C12 = 2.9676988295159875E-8;
        final double C13 = -4.1820072888183715E-9;
        final double C14 = 5.247349410279729E-10;
        final double C15 = -5.652193630074085E-11;
        final double C16 = 4.945273905976834E-12;
        final double C17 = -3.241134540389332E-13;
        final double C18 = 1.3935953129632203E-14;
        final double C19 = -2.9143036337464186E-16;

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
        final double C0 = 0.1666574326398166;
        final double C1 = -0.014605973331310763;
        final double C2 = 0.0019350183889266306;
        final double C3 = -2.8784985811478806E-4;
        final double C4 = 4.552988372742825E-5;
        final double C5 = -7.491361115830589E-6;
        final double C6 = 1.2610893810507322E-6;
        final double C7 = -2.134579800540363E-7;
        final double C8 = 3.5696461819470206E-8;
        final double C9 = -5.805055072125087E-9;
        final double C10 = 9.053587512821651E-10;
        final double C11 = -1.3331482867377635E-10;
        final double C12 = 1.8033382184232272E-11;
        final double C13 = -2.1102773024936194E-12;
        final double C14 = 1.8428706030291046E-13;
        final double C15 = -6.040016434518589E-15;
        final double C16 = -1.3115712434525822E-15;
        final double C17 = 2.564481380745468E-16;
        final double C18 = -2.0697108344464838E-17;
        final double C19 = 6.851644039057656E-19;

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
        final double C0 = 0.12783333716342862;
        final double C1 = -0.0065706557789739365;
        final double C2 = 5.075217097652747E-4;
        final double C3 = -4.364979830093623E-5;
        final double C4 = 3.95189639720783E-6;
        final double C5 = -3.691676709540225E-7;
        final double C6 = 3.526220608524708E-8;
        final double C7 = -3.4245349276017505E-9;
        final double C8 = 3.2818914180744793E-10;
        final double C9 = -1.876043052419544E-11;
        final double C10 = -1.1683599435375239E-11;
        final double C11 = 1.0556600182297752E-11;
        final double C12 = -5.783297004733947E-12;
        final double C13 = 2.3117023757917432E-12;
        final double C14 = -6.849669992067788E-13;
        final double C15 = 1.487924340877374E-13;
        final double C16 = -2.3026750115551114E-14;
        final double C17 = 2.403651100838598E-15;
        final double C18 = -1.516843152534046E-16;
        final double C19 = 4.371060075391741E-18;

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
        final double C0 = 0.1076152516706951;
        final double C1 = -0.003917584207554797;
        final double C2 = 2.1409608395001747E-4;
        final double C3 = -1.3012053530605155E-5;
        final double C4 = 8.312000412986065E-7;
        final double C5 = -5.469966185490357E-8;
        final double C6 = 3.737386820507195E-9;
        final double C7 = -3.7680525903811503E-10;
        final double C8 = 1.8465238092739345E-10;
        final double C9 = -1.6517131849820264E-10;
        final double C10 = 1.2162346645646777E-10;
        final double C11 = -6.891399108178904E-11;
        final double C12 = 3.001067704279907E-11;
        final double C13 = -1.0019953433069345E-11;
        final double C14 = 2.5417590714887255E-12;
        final double C15 = -4.809846886019941E-13;
        final double C16 = 6.573800434113832E-14;
        final double C17 = -6.128669096994469E-15;
        final double C18 = 3.4868581965265973E-16;
        final double C19 = -9.13183067884623E-18;

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
        final double C0 = 0.0947062952127641;
        final double C1 = -0.0026694983407422887;
        final double C2 = 1.1292064983512532E-4;
        final double C3 = -5.3099477091230755E-6;
        final double C4 = 2.6231726762240666E-7;
        final double C5 = -1.3335188812307922E-8;
        final double C6 = 6.881456318054364E-10;
        final double C7 = -3.2395124292626795E-11;
        final double C8 = -1.8670781849744717E-12;
        final double C9 = 2.621538065276415E-12;
        final double C10 = -1.4637367430312426E-12;
        final double C11 = 6.009511127196217E-13;
        final double C12 = -1.8761896752279542E-13;
        final double C13 = 4.4606611898885566E-14;
        final double C14 = -8.012575315612642E-15;
        final double C15 = 1.0685223586996524E-15;
        final double C16 = -1.024873018454764E-16;
        final double C17 = 6.680965932782901E-18;
        final double C18 = -2.649358161554639E-19;
        final double C19 = 4.822546697964484E-21;

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
        final double C1 = 1.0000000000000222;
        final double C2 = 4.499999999420967;
        final double C3 = 37.50000250602749;
        final double C4 = 459.3707811330658;
        final double C5 = 7445.474612622127;
        final double C6 = 148343.6212808577;
        final double C7 = 4111044.580169656;
        final double C8 = 2.42044217780813E7;
        final double C9 = 9.357899868185549E9;

        final double t2 = t * t;
        double v0 = C0 + C1 * t + (C2 + C3 * t) * t2;
        double v4 = C4 + C5 * t + (C6 + C7 * t) * t2;
        double v8 = C8 + C9 * t;

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    /**
     * べき級数による K(x)
     */
    private static double mbK_byPower(double x) {

        //K0 = -(1/2)log(u)F(u) + G(u)

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        return mbK_byPower_HarmonicTerm(u) - (GAMMA + Exponentiation.log(halfX)) * mbI_byPowerAccurate(u);
    }

    /**
     * K(x)の計算におけるG(u)の項.
     */
    private static double mbK_byPower_HarmonicTerm(double u) {

        final double C0 = 1.728207706348274E-16;
        final double C1 = 0.999999999999972;
        final double C2 = 0.3750000000007446;
        final double C3 = 0.05092592591829258;
        final double C4 = 0.0036168981873372186;
        final double C5 = 1.5856470185083402E-4;
        final double C6 = 4.726271357640921E-6;
        final double C7 = 1.0188646024840386E-7;
        final double C8 = 1.771192798546189E-9;

        double u2 = u * u;

        double v6 = C6 + C7 * u + C8 * u2;
        double v2 = C2 + C3 * u + (C4 + (C5 * u)) * u2;

        double u4 = u2 * u2;

        return C0 + C1 * u + u2 * (v2 + u4 * v6);
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_mbK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x) * mbK_asymptoticTerm(0.125 / x);
    }

    /**
     * {@literal t <= 1/16} における,
     * 漸近級数部分の計算を扱う.
     * 
     * @param t t
     * @return 漸近級数部分の値
     */
    private static double mbK_asymptoticTerm(double t) {
        assert t >= 0;
        assert t <= 1d / 16;

        switch ((int) (t * 64)) {
        case 0:
            return mbK_asymptoticTerm_0_to_1_Over64(t);
        case 1:
            return mbK_asymptoticTerm_1_to_2_Over64(t);
        default:
            return mbK_asymptoticTerm_2_to_4_Over64(t);
        }
    }

    private static double mbK_asymptoticTerm_0_to_1_Over64(double t) {
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

    private static double mbK_asymptoticTerm_1_to_2_Over64(double t) {
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

    private static double mbK_asymptoticTerm_2_to_4_Over64(double t) {
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
