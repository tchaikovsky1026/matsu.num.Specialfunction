/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.bessel;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;
import matsu.num.specialfunction.common.Trigonometry;

/**
 * 最適化されたな1次のBessel関数の実装.
 * 
 * <p>
 * 1次Besselの計算戦略は次の通りである. <br>
 * besselJ, besselYともに, 小さいxではべき級数表示を,
 * 大きいxでは漸近級数の連分数表示をminimax近似したものを使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class Bessel1Optimized extends Bessel1st {

    /**
     * アルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数のminimax近似.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC = 2d;

    /**
     * 唯一のコンストラクタ.
     */
    Bessel1Optimized() {
        super();
    }

    @Override
    public double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return bJ_byPower(x);
        }

        return bJ_byAsymptotic(x);
    }

    /**
     * べき級数のminimax近似による J(x)
     */
    private static double bJ_byPower(double x) {

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        final double C0 = 1;
        final double C1 = -0.49999999999999938924672156646327;
        final double C2 = 0.083333333333313517161150595521470;
        final double C3 = -0.0069444444442246577558115930707236;
        final double C4 = 0.00034722222104306580332685993610038;
        final double C5 = -0.000011574070572913302194410348284828;
        final double C6 = 2.7556712992123981692218458531041E-7;
        final double C7 = -4.9148515436004576157332212149455E-9;
        final double C8 = 6.5035392834424757725623614002719E-11;

        double u2 = u * u;

        double v8 = C8;
        double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        double v0 = C0 + u * C1 + u2 * (C2 + u * C3);

        double u4 = u2 * u2;

        return (v0 + u4 * (v4 + u4 * v8)) * halfX;
    }

    /**
     * 漸近級数のminimax近似による J(x)
     */
    private static double bJ_byAsymptotic(double x) {

        double t = 0.125 / x;

        double cos = Trigonometry.cos(x - 3 * Math.PI / 4);
        double sin = Trigonometry.sin(x - 3 * Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (p1_asymptoticTerm(t) * cos
                        + q1_asymptoticTerm(t) * sin);
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC) {
            return bY_byPower(x);
        }

        return bY_byAsymptotic(x);
    }

    /**
     * べき級数のminimax近似による Y(x)
     */
    private static double bY_byPower(double x) {

        final double halfX = x / 2d;
        final double u = halfX * halfX;
        final double gamma_plus_logHalfX =
                GammaFunction.EULER_MASCHERONI_GAMMA + Exponentiation.log(halfX);
        if (Double.isInfinite(gamma_plus_logHalfX)) {
            return Double.NEGATIVE_INFINITY;
        }

        return -(bY_byPower_harmonic(u) * halfX - gamma_plus_logHalfX * bJ_byPower(x) + 1d / x)
                * 2 / Math.PI;
    }

    /**
     * Y(x)の調和数を含む級数部分の計算.
     */
    private static double bY_byPower_harmonic(double u) {

        final double C0 = 0.49999999999999998408566878329610;
        final double C1 = -0.62499999999999741917190824663783;
        final double C2 = 0.13888888888881999184284425525394;
        final double C3 = -0.013599537036328834370748849082430;
        final double C4 = 0.00075810184820435365368353696671095;
        final double C5 = -0.000027391964751473630471605881421293;
        final double C6 = 6.9482015696373839641923637370096E-7;
        final double C7 = -1.3049036242319301388522273728959E-8;
        final double C8 = 1.7995601579165807509457847936898E-10;

        double u2 = u * u;

        double v8 = C8;
        double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        double v0 = C0 + u * C1 + u2 * (C2 + u * C3);

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * v8);
    }

    /**
     * 漸近級数のminimax近似による Y(x)
     */
    private static double bY_byAsymptotic(double x) {

        double t = 0.125 / x;

        double cos = Trigonometry.cos(x - 3 * Math.PI / 4);
        double sin = Trigonometry.sin(x - 3 * Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (p1_asymptoticTerm(t) * sin
                        - q1_asymptoticTerm(t) * cos);
    }

    /**
     * {@literal 0 <= t <= 1/16}について,
     * p1(1/8t)を計算する.
     */
    private static double p1_asymptoticTerm(double t) {
        assert t <= 1d / 16;

        int t128 = (int) (t * 128);
        switch (t128) {
            case 0:
                return p1_asymptoticTerm_0_to_1_over128(t);
            case 1:
                return p1_asymptoticTerm_1_to_2_over128(t);
            case 2, 3:
                return p1_asymptoticTerm_2_to_4_over128(t);
            case 4, 5:
                return p1_asymptoticTerm_4_to_6_over128(t);
            default:
                return p1_asymptoticTerm_6_to_8_over128(t);
        }
    }

    private static double p1_asymptoticTerm_0_to_1_over128(double t) {
        final double C0 = 1.0000000000000000093016881287648;
        final double C1 = 9.5988826600591285648443415548144E-14;
        final double C2 = 7.4999999990051901177792817682820;
        final double C3 = 0.0000024926440609549889712340377071252;
        final double C4 = -590.62788317099007251361941045535;
        final double C5 = 1.8659052395986985049874756267652;
        final double C6 = 176627.80441002437372235280209073;
        final double C7 = 184268.61619401016949792510837235;
        final double C8 = -144646598.00093745740801279449057;
        final double C9 = 2771256932.8897866357242817525082;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 1.0000000003136452484716243136552;
        final double C1 = -3.7580335492413180473290877904183E-7;
        final double C2 = 7.5001913503124186471693375903043;
        final double C3 = -0.055492862177045625426418404508008;
        final double C4 = -580.38349361401195506366059355656;
        final double C5 = -1260.1064514848529361804256660851;
        final double C6 = 281356.87564300938377351437837894;
        final double C7 = -5510240.0193245802840253017839735;
        final double C8 = 40802242.388957131542158963996788;

        double t2 = t * t;

        double v8 = C8;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 0.99999998808887386619735145320127;
        final double C1 = 0.0000038952166643148066494031479477642;
        final double C2 = 7.4997459993475099151145713923829;
        final double C3 = -0.081683844448321357431999351889134;
        final double C4 = -568.84693641910350157238784700097;
        final double C5 = -2622.5652008780666536655892434262;
        final double C6 = 370383.34217449131199912275263459;
        final double C7 = -9023725.3130479976489555941420034;
        final double C8 = 121913339.02359386529333224863319;
        final double C9 = -941149456.39642110209327446449935;
        final double C10 = 3266767805.1289563495985852124924;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 0.99999680966100895754387698750484;
        final double C1 = 0.00093657054123869025433707485762852;
        final double C2 = 7.3748770925773618657379079777523;
        final double C3 = 9.9815081534888727308141178647854;
        final double C4 = -1110.3798677671295933274161782672;
        final double C5 = 17738.155819843360882471437705835;
        final double C6 = -171777.55294648605188254416513529;
        final double C7 = 1075228.9185231119079305803289997;
        final double C8 = -3988666.5033893235428403893584666;
        final double C9 = 6603268.3522762885118849719031584;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 1.0000033977482456195517670566185;
        final double C1 = -0.00020530921606610587517264756560389;
        final double C2 = 7.4623393694510265959277218218765;
        final double C3 = 6.1041743099973273566409905339209;
        final double C4 = -1001.0744043126698609210054581795;
        final double C5 = 15715.070648269092352315613652625;
        final double C6 = -147361.05794830526433006944826955;
        final double C7 = 892012.92398673695924654368500244;
        final double C8 = -3228803.4291110351834230726093529;
        final double C9 = 5333262.0249602311855540810553996;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    /**
     * {@literal 0 <= t <= 1/16}について,
     * q1(1/8t)を計算する.
     */
    private static double q1_asymptoticTerm(double t) {
        assert t <= 1d / 16;

        int t128 = (int) (t * 128);
        switch (t128) {
            case 0:
                return q1_asymptoticTerm_0_to_1_over128(t);
            case 1:
                return q1_asymptoticTerm_1_to_2_over128(t);
            case 2, 3:
                return q1_asymptoticTerm_2_to_4_over128(t);
            case 4, 5:
                return q1_asymptoticTerm_4_to_6_over128(t);
            default:
                return q1_asymptoticTerm_6_to_8_over128(t);
        }
    }

    private static double q1_asymptoticTerm_0_to_1_over128(double t) {
        final double C0 = -1.5320283922161067862764711379673E-16;
        final double C1 = -2.9999999999960585435527220894758;
        final double C2 = -1.6729296379557790919210127849352E-8;
        final double C3 = 52.500027530692130226956045854873;
        final double C4 = -0.022985877051651358806746109025195;
        final double C5 = -9084.6246063344094054127714109656;
        final double C6 = -3189.8900863074880963218068937683;
        final double C7 = 4746545.7249484177295358750516642;
        final double C8 = -58602108.460974265037303807877198;
        final double C9 = -729718865.54949526878852611689763;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 5.7304715577317141442225428586608E-10;
        final double C1 = -3.0000004926580033821943704791302;
        final double C2 = 0.00018502585188094053035214822649515;
        final double C3 = 52.460693399024237607063638543686;
        final double C4 = 5.0347772344592583413920069490062;
        final double C5 = -9458.4806635116938134494317655409;
        final double C6 = 7073.4495317676585993546627345522;
        final double C7 = 5405894.3189169935995194856378304;
        final double C8 = -124741304.50783043113087403073195;
        final double C9 = 1020587297.7252477163185266673027;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 3.1672881965140236406953234540347E-8;
        final double C1 = -3.0000171333472153180267190061041;
        final double C2 = 0.0041809477509501344804656631842739;
        final double C3 = 51.896705862198967281290416050923;
        final double C4 = 56.323933315442807464676237740308;
        final double C5 = -12536.793378224430281826450576454;
        final double C6 = 125193.28961628426179420518839992;
        final double C7 = 2901284.3947542397288926043607773;
        final double C8 = -115530955.00000165496735595037398;
        final double C9 = 1831335282.3164556334925902663139;
        final double C10 = -15271256834.702174138298270046439;
        final double C11 = 55107967583.847467671981145498189;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = -5.3104158599536989151477440620264E-7;
        final double C1 = -2.9998933596043825222087943830605;
        final double C2 = -0.0050588787754502467897355512954451;
        final double C3 = 51.859792340482426496630631123163;
        final double C4 = 114.24756223625221098447370078963;
        final double C5 = -17555.403168304402156012419214068;
        final double C6 = 364232.60945311878203324338811848;
        final double C7 = -4445277.8226007155192810292811978;
        final double C8 = 34745468.002544151513575576871037;
        final double C9 = -162047824.63862736231498759810571;
        final double C10 = 345198684.12226862266793511825227;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = -0.000025010116658851055206011043283455;
        final double C1 = -2.9950435387498609285284914370061;
        final double C2 = -0.44064355016585654800175726853807;
        final double C3 = 75.234427178596986773965857582996;
        final double C4 = -716.37465520396472855429526709572;
        final double C5 = 2882.4487303820612348904371743501;
        final double C6 = 11380.261852114914581260839580859;
        final double C7 = -222771.47820425199472417705553358;
        final double C8 = 1218305.2914617695773435575891531;
        final double C9 = -2543978.9116855367802738916093011;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
