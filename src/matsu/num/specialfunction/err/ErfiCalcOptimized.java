/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.err;

import matsu.num.specialfunction.common.Exponentiation;

/**
 * 速度を最適化した虚数誤差関数の計算.
 * 
 * @author Matsuura Y.
 */
final class ErfiCalcOptimized implements ErrorFunctionImaginaryCalculation {

    /**
     * 1/sqrt(pi)
     */
    // = 1d / Math.sqrt(Math.PI)
    private static final double ONE_OVER_SQRT_PI = 0.5641895835477563;

    /**
     * 計算アルゴリズムを切り替えるxの閾値. <br>
     * 下側はerfiをminimax近似で, 上側はerfixを計算する.
     */
    private static final double BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX = 1d;

    /**
     * erfix(x) をもとにした erfi(x) で,
     * exp(x^2)のオーバーフローを回避するために導入するシフト. <br>
     * x^2をシフトする.
     */
    private static final double SHIFT_X2_FOR_ERFIX_TO_ERFI = 20;

    /**
     * exp(shift_x2)
     */
    private static final double EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI =
            Math.exp(SHIFT_X2_FOR_ERFIX_TO_ERFI);

    /**
     * 唯一のコンストラクタ.
     */
    public ErfiCalcOptimized() {
        super();
    }

    @Override
    public double erfi(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        final double absX = Math.abs(x);
        if (absX <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x);
        }

        final double x2 = x * x;
        if (x2 < SHIFT_X2_FOR_ERFIX_TO_ERFI) {
            return erfix_largeX(x) * Exponentiation.exp(x2);
        }

        // erfix = 0 なら erfi = inf or -inf: 明らかに exp(x^2) が吹き飛ぶ
        // x^2が或る程度の場合におこる, (小) * (inf) = (finite) に対する対策 
        final double erfix = erfix_largeX(x);
        return erfix == 0d
                ? (x < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)
                : (erfix * Exponentiation.exp(x2 - SHIFT_X2_FOR_ERFIX_TO_ERFI)) * EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI;
    }

    @Override
    public double erfix(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        if (Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x) * Exponentiation.exp(-x * x);
        }

        return erfix_largeX(x);
    }

    /**
     * xが小さいときのerfi(x)
     */
    private double erfi_smallX(double x) {
        assert Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        final double C0 = 1;
        final double C1 = 0.33333333333333637432885077985656;
        final double C2 = 0.099999999999824839468932960751292;
        final double C3 = 0.023809523813029984077162129956148;
        final double C4 = 0.0046296295946530662954886559899444;
        final double C5 = 0.00075757596007981814367638850957405;
        final double C6 = 0.00010683687083028228772646852676590;
        final double C7 = 0.000013229261122473078985052835033926;
        final double C8 = 0.0000014561681413998999985196499235970;
        final double C9 = 1.4786625574429245940655171253563E-7;
        final double C10 = 1.1308415983867618934081497512004E-8;
        final double C11 = 1.7314916263556263901037600571597E-9;

        final double u = x * x;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8 + u * C9 + u2 * (C10 + u * C11);

        final double u4 = u2 * u2;

        return (ONE_OVER_SQRT_PI * 2) * x *
                (v0 + u4 * (v4 + u4 * v8));
    }

    /**
     * xが大きいときのerfix(x)
     */
    private double erfix_largeX(double x) {
        assert Math.abs(x) >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        return ONE_OVER_SQRT_PI / x * erfix_largeX_factor(x);
    }

    /**
     * erfix(x) = 2/sqrt(pi) * x * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor(double x) {

        final double abs_x = Math.abs(x);

        assert abs_x >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        switch ((int) abs_x) {
            case 1:
                return erfix_largeX_factor_1_to_2(abs_x);
            case 2:
                return erfix_largeX_factor_2_to_3(abs_x);
            case 3:
                return erfix_largeX_factor_3_to_4(abs_x);
            case 4:
                return erfix_largeX_factor_4_to_5(abs_x);
            case 5:
                return erfix_largeX_factor_5_to_6(abs_x);
            case 6:
                return erfix_largeX_factor_6_to_7(abs_x);
            case 7:
                return erfix_largeX_factor_7_to_8(abs_x);
            default:
                return erfix_largeX_factor_greater_than_8(abs_x);
        }
    }

    private double erfix_largeX_factor_1_to_2(double x) {
        assert x >= 1d;
        assert x <= 2d;

        final double x_shift = 1.5d;

        final double C0 = 1.2847472132561957523067117084161;
        final double C1 = 0.0022565024022096496070324701751125;
        final double C2 = -0.57287918011562552034067168119483;
        final double C3 = 0.57062267771349043877181265931677;
        final double C4 = -0.046047554883281658208144906492685;
        final double C5 = -0.25768280593155655388848429756067;
        final double C6 = 0.14726042527142941031613242883606;
        final double C7 = 0.022782657953644950210624853682701;
        final double C8 = -0.050617911200298465120315240789992;
        final double C9 = 0.011176970329569428813694034908127;
        final double C10 = 0.0078954173503769252064629444114379;
        final double C11 = -0.0043886684284354822763313260391287;
        final double C12 = -0.00033893167612488539479031167887162;
        final double C13 = 0.00080953744656619981626356116372199;
        final double C14 = -0.00011908069961423667118445578548087;
        final double C15 = -0.000091412559625441414544869143947179;
        final double C16 = 0.000028221270220339532577725424598696;
        final double C17 = 0.0000056563667210321735560700555111277;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);
        final double v12 = C12 + y * C13 + y2 * (C14 + y * C15);
        final double v16 = C16 + y * C17;

        final double y4 = y2 * y2;
        final double y8 = y4 * y4;

        return v0 + y4 * v4 + y8 * (v8 + y4 * v12 + y4 * y4 * v16);
    }

    private double erfix_largeX_factor_2_to_3(double x) {
        assert x >= 2d;
        assert x <= 3d;

        final double x_shift = 2.5d;

        final double C0 = 1.1154186108371773908217981851601;
        final double C1 = -0.13092560985101608704024682674139;
        final double C2 = 0.096476802953194986366722339946469;
        final double C3 = -0.029869061737621591888780333040464;
        final double C4 = -0.026981541464455443414241231956394;
        final double C5 = 0.041916072331941379045691185977382;
        final double C6 = -0.024137443648970738871866492708661;
        final double C7 = 0.0032690071061687135276494398424159;
        final double C8 = 0.0048532821683257308092842024462440;
        final double C9 = -0.0035135202732240455684548821655919;
        final double C10 = 0.00067826294495086675736121218564331;
        final double C11 = 0.00039440864389534623433052944704339;
        final double C12 = -0.00028772506967044245862638173332914;
        final double C13 = 0.000044894585609829045156157543976729;
        final double C14 = 0.000028499086316778614990712689558696;
        final double C15 = -0.000015795929454345852784635180765300;
        final double C16 = 5.6565791018370055300313255675257E-7;
        final double C17 = 0.0000015868842756936719942049435539025;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);
        final double v12 = C12 + y * C13 + y2 * (C14 + y * C15);
        final double v16 = C16 + y * C17;

        final double y4 = y2 * y2;
        final double y8 = y4 * y4;

        return v0 + y4 * v4 + y8 * (v8 + y4 * v12 + y4 * y4 * v16);
    }

    private double erfix_largeX_factor_3_to_4(double x) {
        assert x >= 3d;
        assert x <= 4d;

        final double x_shift = 3.5d;

        final double C0 = 1.0473511515652955150822788081860;
        final double C1 = -0.032214874795555451402291664804518;
        final double C2 = 0.018049758653788513428088691186994;
        final double C3 = -0.0099012287299823630278559044950393;
        final double C4 = 0.0052939778470190897504949798042327;
        final double C5 = -0.0024609546192866601186341471963592;
        final double C6 = 0.00075352241091657663308877167038846;
        final double C7 = 0.000066795760819775777665022049240134;
        final double C8 = -0.00027373569704660929638301628651157;
        final double C9 = 0.00019620701660740774106890952569816;
        final double C10 = -0.000076537862834068519246578927408744;
        final double C11 = 0.0000094616831006289833940012359349034;
        final double C12 = 0.0000085057750901424228495099340357275;
        final double C13 = -0.0000061459608643754525921775042161369;
        final double C14 = 0.0000014960485255635614676725956902512;
        final double C15 = 1.5695955732061552896871842965059E-7;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);
        final double v12 = C12 + y * C13 + y2 * (C14 + y * C15);

        final double y4 = y2 * y2;
        final double y8 = y4 * y4;

        return v0 + y4 * v4 + y8 * (v8 + y4 * v12);
    }

    private double erfix_largeX_factor_4_to_5(double x) {
        assert x >= 4d;
        assert x <= 5d;

        final double x_shift = 4.5d;

        final double C0 = 1.0267974920414248220374462800419;
        final double C1 = -0.013000207919172711751321877784980;
        final double C2 = 0.0049059515534305342918662547598025;
        final double C3 = -0.0017176467412269911232884947998189;
        final double C4 = 0.00059407079858445095681232108874131;
        final double C5 = -0.00021050406217244224366640801764995;
        final double C6 = 0.000078127780244063372478733524468572;
        final double C7 = -0.000030282071266394514590323643612671;
        final double C8 = 0.000011745015457467751758765407311368;
        final double C9 = -0.0000041736296426553637074669349189900;
        final double C10 = 0.0000011469817340170561796487378124585;
        final double C11 = -1.0825683192124729932879338229956E-7;
        final double C12 = -1.3048620433095898723168163542343E-7;
        final double C13 = 1.2049002613975522098428177665340E-7;
        final double C14 = -5.0211301080598984421244576004201E-8;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);
        final double v12 = C12 + y * C13 + y2 * C14;

        final double y4 = y2 * y2;
        final double y8 = y4 * y4;

        return v0 + y4 * v4 + y8 * (v8 + y4 * v12);
    }

    private double erfix_largeX_factor_5_to_6(double x) {
        assert x >= 5d;
        assert x <= 6d;

        final double x_shift = 5.5d;

        final double C0 = 1.0174255554183023656553444343328;
        final double C1 = -0.0066946449798153351877183302999490;
        final double C2 = 0.0019694365523836361560053473241615;
        final double C3 = -0.00052662237905419589298015544700736;
        final double C4 = 0.00013525384058559811346902124469829;
        final double C5 = -0.000034247255635737155585526182004211;
        final double C6 = 0.0000086851038610493595434348625678513;
        final double C7 = -0.0000022323248441989486574135901634821;
        final double C8 = 5.8793967268165536127892903624273E-7;
        final double C9 = -1.6013703492606007609925124168954E-7;
        final double C10 = 4.5756054433491478128677455295211E-8;
        final double C11 = -1.4924739501207526525275548568515E-8;
        final double C12 = 4.6759683443809643407471889912342E-9;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);
        final double v12 = C12;

        final double y4 = y2 * y2;
        final double y8 = y4 * y4;

        return v0 + y4 * v4 + y8 * (v8 + y4 * v12);
    }

    private double erfix_largeX_factor_6_to_7(double x) {
        assert x >= 6d;
        assert x <= 7d;

        final double x_shift = 6.5d;

        final double C0 = 1.0122816468189083113533632009084;
        final double C1 = -0.0039257706736684776376787226101377;
        final double C2 = 0.00095421574103354115113201935774169;
        final double C3 = -0.00020916420412697623332773867426368;
        final double C4 = 0.000043639835822328832469103722605157;
        final double C5 = -0.0000088814715367466326505490641355318;
        final double C6 = 0.0000017872579868571555663743300635756;
        final double C7 = -3.5869727595134537960747115831095E-7;
        final double C8 = 7.2207294975740172596748746051714E-8;
        final double C9 = -1.4658690708646669946583715886826E-8;
        final double C10 = 3.1188561813383175715121379089820E-9;
        final double C11 = -6.5681694636855194504443918972819E-10;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * (C10 + y * C11);

        final double y4 = y2 * y2;

        return v0 + y4 * (v4 + y4 * v8);
    }

    private double erfix_largeX_factor_7_to_8(double x) {
        assert x >= 7d;
        assert x <= 8d;

        final double x_shift = 7.5d;

        final double C0 = 1.0091371746694592371032143484251;
        final double C1 = -0.0025059967526280625032301583883979;
        final double C2 = 0.00052062630578741704867471257178842;
        final double C3 = -0.000097134776250682505838623487395034;
        final double C4 = 0.000017171207264519372280447730166312;
        final double C5 = -0.0000029462349676547348355764914526954;
        final double C6 = 4.9710179016162697425888060002926E-7;
        final double C7 = -8.3127969277555215994016878049845E-8;
        final double C8 = 1.3854492118368777468181551799136E-8;
        final double C9 = -2.3559901480855611702403011258602E-9;
        final double C10 = 3.9571723198501373218542517093663E-10;

        final double y = x - x_shift;
        final double y2 = y * y;

        final double v0 = C0 + y * C1 + y2 * (C2 + y * C3);
        final double v4 = C4 + y * C5 + y2 * (C6 + y * C7);
        final double v8 = C8 + y * C9 + y2 * C10;

        final double y4 = y2 * y2;

        return v0 + y4 * (v4 + y4 * v8);
    }

    /**
     * erfix(x) = 1/sqrt(pi) * (1/x) * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor_greater_than_8(double x) {
        assert x >= 8d;

        final double C0 = 1;
        final double C1 = 0.49999999999991029306317458498744;
        final double C2 = 0.75000000045345053035114275982664;
        final double C3 = 1.8749995807128013313304078331843;
        final double C4 = 6.5626624876296709135393464324556;
        final double C5 = 29.498559893607349873714197401481;
        final double C6 = 166.09786943941219030641913966995;
        final double C7 = 825.99070475387625280272752468017;
        final double C8 = 15067.203592985060397320816370068;

        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8;

        final double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * v8);
    }
}
