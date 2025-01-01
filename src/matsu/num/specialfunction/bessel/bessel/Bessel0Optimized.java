/*
 * Copyright (c) 2024 Matsuura Y.
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
 * 最適化されたな0次のBessel関数の実装.
 * 
 * <p>
 * 0次Besselの計算戦略は次の通りである. <br>
 * besselJ, besselYともに, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示をminimax近似を使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class Bessel0Optimized extends Bessel0th {

    /**
     * アルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数のminimax近似, 上側は漸近級数のminimax近似.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC = 2d;

    /**
     * 唯一のコンストラクタ.
     */
    public Bessel0Optimized() {
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
        final double C1 = -0.99999999999999391947466951407251;
        final double C2 = 0.24999999999980269370673516397619;
        final double C3 = -0.027777777775589140775088617839519;
        final double C4 = 0.0017361110993674040519694379191559;
        final double C5 = -0.000069444409568530854417615672483121;
        final double C6 = 0.0000019289519416699672325204332647123;
        final double C7 = -3.9306810663324465717265045255668E-8;
        final double C8 = 5.8208621379706647471680291198418E-10;

        double u2 = u * u;

        double v8 = C8;
        double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        double v0 = C0 + u * C1 + u2 * (C2 + u * C3);

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * v8);
    }

    /**
     * 漸近級数のminimax近似による J(x)
     */
    private static double bJ_byAsymptotic(double x) {

        double t = 0.125 / x;

        double cos = Trigonometry.cos(x - Math.PI / 4);
        double sin = Trigonometry.sin(x - Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (p0_asymptoticTerm(t) * cos
                        + q0_asymptoticTerm(t) * sin);
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

        return -(bY_byPower_harmonic(u) - gamma_plus_logHalfX * bJ_byPower(x)) * 2 / Math.PI;
    }

    /**
     * Y(x)の調和数を含む級数部分の計算.
     */
    private static double bY_byPower_harmonic(double u) {

        final double C0 = -1.5562660830972187088825625309398E-16;
        final double C1 = -0.99999999999997475928608449075662;
        final double C2 = 0.37499999999932610531895632290348;
        final double C3 = -0.050925925918997995536093038256596;
        final double C4 = 0.0036168981124614389984702979286684;
        final double C5 = -0.00015856471150446794512869533105299;
        final double C6 = 0.0000047259044110495008105392041962013;
        final double C7 = -1.0189982399253397799634723731467E-7;
        final double C8 = 1.5777393078079274822365765777955E-9;

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

        double cos = Trigonometry.cos(x - Math.PI / 4);
        double sin = Trigonometry.sin(x - Math.PI / 4);

        if (!(Double.isFinite(sin) && Double.isFinite(cos))) {
            return 0d;
        }
        return Exponentiation.sqrt((2 / Math.PI) / x)
                * (p0_asymptoticTerm(t) * sin
                        - q0_asymptoticTerm(t) * cos);
    }

    /**
     * {@literal 0 <= t <= 1/16}について,
     * p0(1/8t)を計算する.
     */
    private static double p0_asymptoticTerm(double t) {
        assert t <= 1d / 16;

        int t128 = (int) (t * 128);
        switch (t128) {
            case 0:
                return p0_asymptoticTerm_0_to_1_over128(t);
            case 1:
                return p0_asymptoticTerm_1_to_2_over128(t);
            case 2, 3:
                return p0_asymptoticTerm_2_to_4_over128(t);
            case 4, 5:
                return p0_asymptoticTerm_4_to_6_over128(t);
            default:
                return p0_asymptoticTerm_6_to_8_over128(t);
        }
    }

    private static double p0_asymptoticTerm_0_to_1_over128(double t) {
        final double C0 = 0.99999999999999998794583825034477;
        final double C1 = 7.8682301670645608759229093296926E-15;
        final double C2 = -4.4999999994440694882704885061916;
        final double C3 = -0.0000017573965540093345963489534740870;
        final double C4 = 459.37723486593162202013036617919;
        final double C5 = -1.5251469465299431315870667850045;
        final double C6 = -149454.47060664129266102965573064;
        final double C7 = -159688.65189681948328066317357132;
        final double C8 = 127629096.70661773965617699134290;
        final double C9 = -2479547540.6137273453462410039302;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 0.99999999983643904348486293256994;
        final double C1 = 2.4006483271039153173566437822551E-7;
        final double C2 = -4.5001363927915978691820304316330;
        final double C3 = 0.042377272014678730925169092944836;
        final double C4 = 451.17450348733922233678062094630;
        final double C5 = 1044.4539007516553166632425148836;
        final double C6 = -238565.96579662272348407794187580;
        final double C7 = 4786111.0912900992311403192237437;
        final double C8 = -36101848.794473091230983344064936;

        double t2 = t * t;

        double v8 = C8;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 0.99999998510741521922348524283478;
        final double C1 = 0.0000091783901426744730927357592058585;
        final double C2 = -4.5026080789668998395286373932354;
        final double C3 = 0.45180438509078238289088536963448;
        final double C4 = 406.32252587941845720026625656355;
        final double C5 = 4424.0312679254195950398840787842;
        final double C6 = -414804.95364428488229204356677734;
        final double C7 = 11013411.600720539979095895197443;
        final double C8 = -177366902.12899685586037330822121;
        final double C9 = 1851790912.7231704636816368551196;
        final double C10 = -11660394541.834224964032437252278;
        final double C11 = 33893029263.865990007822714024122;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 1.0000020119074208305301516542124;
        final double C1 = -0.00061724525650460659277525654731135;
        final double C2 = -4.4138942501216658094232789499229;
        final double C3 = -7.1580138267459023239194722346398;
        final double C4 = 846.29354908299477472768319455614;
        final double C5 = -13605.360656190814424590774246834;
        final double C6 = 120294.37421933624988010016720742;
        final double C7 = -503061.90864972110552528792356142;
        final double C8 = -1058307.1492606430641422519893837;
        final double C9 = 22194257.575175184736496518287531;
        final double C10 = -76113015.094931209010580682555821;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 0.99999535030538717038453169803686;
        final double C1 = 0.00051462200963493869108471182317953;
        final double C2 = -4.4971723066062927012265153784751;
        final double C3 = -3.7525584832737750533153546957631;
        final double C4 = 765.55699377888541633728518891900;
        final double C5 = -12661.233320606309838931708512011;
        final double C6 = 122652.34938566414421099383708377;
        final double C7 = -759182.42857675269720300883169604;
        final double C8 = 2792734.3184330325891745021105557;
        final double C9 = -4669504.0070746577855335190117265;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    /**
     * {@literal 0 <= t <= 1/16}について,
     * q0(1/8t)を計算する.
     */
    private static double q0_asymptoticTerm(double t) {
        assert t <= 1d / 16;

        int t128 = (int) (t * 128);
        switch (t128) {
            case 0:
                return q0_asymptoticTerm_0_to_1_over128(t);
            case 1:
                return q0_asymptoticTerm_1_to_2_over128(t);
            case 2, 3:
                return q0_asymptoticTerm_2_to_4_over128(t);
            case 4, 5:
                return q0_asymptoticTerm_4_to_6_over128(t);
            default:
                return q0_asymptoticTerm_6_to_8_over128(t);
        }
    }

    private static double q0_asymptoticTerm_0_to_1_over128(double t) {
        final double C0 = 1.3811043734188444696246012016499E-16;
        final double C1 = 0.99999999999644536673978789441897;
        final double C2 = 1.5093778052120814188264787137783E-8;
        final double C3 = -37.500024850298012756982451994443;
        final double C4 = 0.020758279447162054374826066350555;
        final double C5 = 7431.9349507954598478971111767894;
        final double C6 = 2884.4179423738620178063356332461;
        final double C7 = -4135380.3429711617356413930447529;
        final double C8 = 53098443.203010117027656732496887;
        final double C9 = 615395277.14355826126643559759846;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = -5.4060543929733107716716728451822E-10;
        final double C1 = 1.0000004666452671653001784859011;
        final double C2 = -0.00017627440340891633457715940454492;
        final double C3 = -37.462210555114925751525313515080;
        final double C4 = -4.9211927621057121587244447266785;
        final double C5 = 7811.2330574340656495958305245774;
        final double C6 = -9564.1964347249566964664332180330;
        final double C7 = -4569494.6634859253942292725249822;
        final double C8 = 107862015.27692910325956694742180;
        final double C9 = -894343088.20499144058723763658949;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = -2.9111646268637144996341710508157E-8;
        final double C1 = 1.0000157963657432755037138763074;
        final double C2 = -0.0038701154645722005748334732589483;
        final double C3 = -36.938501507168857928554082717409;
        final double C4 = -52.847924695105489923576895771357;
        final double C5 = 10715.770952651593725431531478697;
        final double C6 = -123009.50279282594743333317833725;
        final double C7 = -2051526.0813494188885717025700253;
        final double C8 = 93043351.468934212110131683236759;
        final double C9 = -1523607823.6019447147274676613123;
        final double C10 = 12920235278.772800493333649596226;
        final double C11 = -47124511029.817851405492274310453;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 6.0020682665112147235748375966371E-7;
        final double C1 = 0.99986591958158234796341610490790;
        final double C2 = 0.0099727849948655099739012067796149;
        final double C3 = -37.386582067506952137913563157036;
        final double C4 = -76.704187932225167838924351731395;
        final double C5 = 14054.226154077814761414875136331;
        final double C6 = -302556.40168096923844845464506807;
        final double C7 = 3780475.9763202338357152982010404;
        final double C8 = -30043276.467288488400491176841964;
        final double C9 = 141849385.13063883146487329672081;
        final double C10 = -305032444.97617363418604103379471;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 0.000021051656235823151824212862640078;
        final double C1 = 0.99579509496126811610233036235925;
        final double C2 = 0.37743512735098067694043122854472;
        final double C3 = -57.211545894116712300881669911877;
        final double C4 = 631.76677086305978831934165697857;
        final double C5 = -3480.5485380681607375477684929715;
        final double C6 = 1997.8511390541979267948812776464;
        final double C7 = 113833.29774267165939490575611287;
        final double C8 = -754662.42794373412974167867050443;
        final double C9 = 1693268.5006282058711824714790934;

        double t2 = t * t;

        double v8 = C8 + t * C9;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
