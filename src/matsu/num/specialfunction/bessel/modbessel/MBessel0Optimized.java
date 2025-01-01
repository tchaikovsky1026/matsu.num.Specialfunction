/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 最適化された0次の変形Bessel関数の実装.
 * 
 * <p>
 * 0次MBesselの計算戦略は次の通りである. <br>
 * bessel_Iは, 小さいxではべき級数表示を, 大きいxでは漸近級数を使う. <br>
 * bessel_Kは, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示を区分で分けてminimax近似をする.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 22.0
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

        final double C0 = 1;
        final double C1 = 0.99999999999999329900091589918609;
        final double C2 = 0.25000000000021694242378466031309;
        final double C3 = 0.027777777775377465773065843704979;
        final double C4 = 0.0017361111239511942586383244922064;
        final double C5 = 0.000069444406463918807462659807871518;
        final double C6 = 0.0000019290777557158875906656313571966;
        final double C7 = 3.9302369008512502333001291457261E-8;
        final double C8 = 6.4993965756108446051660454144974E-10;

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

        switch ((int) (x * 0.5)) {
            case 1, 2:
                return scaling_mbI_byPowerExp_2_to_6(x);
            case 3, 4:
                return scaling_mbI_byPowerExp_6_to_10(x);
            case 5, 6:
                return scaling_mbI_byPowerExp_10_to_14(x);
            case 7, 8:
                return scaling_mbI_byPowerExp_14_to_18(x);
            default:
                return scaling_mbI_byPowerExp_18_to_24(x);
        }
    }

    private static double scaling_mbI_byPowerExp_2_to_6(double x) {

        double u = x - 4d;

        final double C0 = 0.20700192122398664367842450357426;
        final double C1 = -0.028251081721551271290969377832664;
        final double C2 = 0.0059072267837496615223937653183847;
        final double C3 = -0.0013913037712635397534370075359708;
        final double C4 = 0.00034118521941327945372386151535946;
        final double C5 = -0.000082972051667657566537374321832206;
        final double C6 = 0.000019354303271259306269149484078767;
        final double C7 = -0.0000042444577592700393691195919152601;
        final double C8 = 8.6634863983571508502336792507779E-7;
        final double C9 = -1.6397835985020347819918718910737E-7;
        final double C10 = 2.8780006772981334519548929933049E-8;
        final double C11 = -4.6927288929656852142678101821870E-9;
        final double C12 = 7.1281569221973437766628269490664E-10;
        final double C13 = -1.0120584553768230177879962904039E-10;
        final double C14 = 1.3495927494133716523243943891570E-11;
        final double C15 = -1.6901213307523143837148915517982E-12;
        final double C16 = 1.9496404518451577305577177638370E-13;
        final double C17 = -2.1792324794956584701822516144358E-14;
        final double C18 = 2.8668855999555654178509579689319E-15;
        final double C19 = -2.8915912626898005224474423440814E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + (C18 + C19 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    private static double scaling_mbI_byPowerExp_6_to_10(double x) {

        double u = x - 8d;

        final double C0 = 0.14343178185685031679803944117320;
        final double C1 = -0.0092892885641518765865089746762258;
        final double C2 = 0.00090538273335827609816207290488206;
        final double C3 = -0.000098456514762425820085972722146889;
        final double C4 = 0.000011303204943943708808759709080230;
        final double C5 = -0.0000013441299399056970062411407749755;
        final double C6 = 1.6418666492829845543366071056537E-7;
        final double C7 = -2.0497667672964911327725098833564E-8;
        final double C8 = 2.6020017048477260756638168146002E-9;
        final double C9 = -3.3352584899629955432802244058880E-10;
        final double C10 = 4.2792641417208040065129669821958E-11;
        final double C11 = -5.4490569129149735098227068534860E-12;
        final double C12 = 6.8064683360215639423250025078804E-13;
        final double C13 = -8.0971662274532109215259184507669E-14;
        final double C14 = 9.5204504479446211272326905885393E-15;
        final double C15 = -1.3436453469237505541538458128030E-15;
        final double C16 = 1.4750544735453359344219120969043E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    private static double scaling_mbI_byPowerExp_10_to_14(double x) {

        double u = x - 12d;

        final double C0 = 0.11642622121344049472599276356687;
        final double C1 = -0.0049619219232595297299011478261257;
        final double C2 = 0.00031757611950066095066179303705735;
        final double C3 = -0.000022613783948393348005963412338504;
        final double C4 = 0.0000016933048824911556585958183410907;
        final double C5 = -1.3064024555433565346448712211838E-7;
        final double C6 = 1.0286357816178678740518969016977E-8;
        final double C7 = -8.2242538527496075579027811089487E-10;
        final double C8 = 6.6588461660722922912436037143859E-11;
        final double C9 = -5.4508415370188025643595870225160E-12;
        final double C10 = 4.4876645606086757186790474824334E-13;
        final double C11 = -3.7417301968285719725233167042153E-14;
        final double C12 = 3.5194155714922950112077942293933E-15;
        final double C13 = -3.0419902853451523219691901746413E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * v12));
    }

    private static double scaling_mbI_byPowerExp_14_to_18(double x) {

        double u = x - 16d;

        final double C0 = 0.10054412736125186270368678403666;
        final double C1 = -0.0031945126047838194482660317310080;
        final double C2 = 0.00015233714364711743423869557263319;
        final double C3 = -0.0000080769578492647849443568862613954;
        final double C4 = 4.4997319951526817262232395187680E-7;
        final double C5 = -2.5804489265883949091947830115634E-8;
        final double C6 = 1.5084917735078502662171106859340E-9;
        final double C7 = -8.9412792642744809949738029836057E-11;
        final double C8 = 5.3520202678262786615023698167051E-12;
        final double C9 = -3.2324887739205796983885594600604E-13;
        final double C10 = 2.0611489205545998491699250265822E-14;
        final double C11 = -1.2726299532654956113356314506919E-15;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * v8);
    }

    private static double scaling_mbI_byPowerExp_18_to_24(double x) {

        double u = x - 21d;

        final double C0 = 0.087589159654227858194575502332169;
        final double C1 = -0.0021115632089980833713108441460321;
        final double C2 = 0.000076382341254481042814385872946591;
        final double C3 = -0.0000030710825666836055461286568446675;
        final double C4 = 1.2970015970647591488595292906314E-7;
        final double C5 = -5.6363128843806395254243371342743E-9;
        final double C6 = 2.4957470424847988779557438444631E-10;
        final double C7 = -1.1199638693427030835122019587842E-11;
        final double C8 = 5.0767069450821460833003893449973E-13;
        final double C9 = -2.3194455305967296863231928797341E-14;
        final double C10 = 1.0644033213161588989335363263548E-15;
        final double C11 = -4.9170915693766195096821680833349E-17;
        final double C12 = 2.4540156294722514837446498204933E-18;
        final double C13 = -1.1563734220780178610221204802528E-19;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * v12));
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
        final double C0 = 1;
        final double C1 = 1.0000000000000221270661163971606;
        final double C2 = 4.4999999994204167893873967309626;
        final double C3 = 37.500002508203771826608108062845;
        final double C4 = 459.37077782014606351419198280914;
        final double C5 = 7445.4771307513898293372994543065;
        final double C6 = 148342.56540198297524980564726567;
        final double C7 = 4111293.2851891434072560529084572;
        final double C8 = 24173538.114728048097059807863735;
        final double C9 = 9359475722.2235800088361601311045;

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

        final double C0 = 1.7260310377283270362578133560040E-16;
        final double C1 = 0.99999999999997207024468366295509;
        final double C2 = 0.37500000000074391069149646046095;
        final double C3 = 0.050925925918298537816403407239283;
        final double C4 = 0.0036168981873125355018702239306847;
        final double C5 = 0.00015856470190577495783647278166744;
        final double C6 = 0.0000047262712904973201098694121518264;
        final double C7 = 1.0188650265646674322883363825077E-7;
        final double C8 = 1.7711819849813956942663628043561E-9;

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
        final double C0 = 1;
        final double C1 = -0.99999999999997069093741955181449;
        final double C2 = 4.4999999995777375827753904929428;
        final double C3 = -37.499998969775848941523825971471;
        final double C4 = 459.37395825215879960482838044128;
        final double C5 = -7441.3034827419571092112324370348;
        final double C6 = 149884.58722345269749476059397371;
        final double C7 = -3580052.1509334210069383752502514;
        final double C8 = 95200381.417708602493342334805485;
        final double C9 = -2529230330.1802231955723368309706;
        final double C10 = 57430214980.150741902567928912277;
        final double C11 = -911816923340.23082958489740136402;
        final double C12 = 7175515694143.8802917049207941455;

        double t2 = t * t;

        double v12 = C12;
        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * (v8 + t4 * v12));
    }

    private static double mbK_asymptoticTerm_1_to_2_Over64(double t) {
        final double C0 = 0.99999999339852011545490609239749;
        final double C1 = -0.99999652553827841419271410761366;
        final double C2 = 4.4991618068341394536362682412350;
        final double C3 = -37.376906396586243530288082016681;
        final double C4 = 447.02780871020815497552134243909;
        final double C5 = -6539.0243647850314905847423213105;
        final double C6 = 99566.166691996128395775039862135;
        final double C7 = -1351100.2697813499435178048027491;
        final double C8 = 14159937.559436384746257441422362;
        final double C9 = -97319365.704107650395628763312879;
        final double C10 = 321096658.57724168046192059238714;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double mbK_asymptoticTerm_2_to_4_Over64(double t) {
        final double C0 = 0.99999976056531305650711562616174;
        final double C1 = -0.99992911649156308226664171603449;
        final double C2 = 4.4902079417055561265612820346400;
        final double C3 = -36.656732629723134935128321599829;
        final double C4 = 408.12031894179505522531318257736;
        final double C5 = -5062.6085437694743801039873822490;
        final double C6 = 59730.129686920924091049982110906;
        final double C7 = -597964.06799555491399470644205724;
        final double C8 = 4654806.0008852461834407296538004;
        final double C9 = -25815090.174497735415293773429094;
        final double C10 = 89725845.810523431809075813802246;
        final double C11 = -146007387.51870687729567747771732;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
