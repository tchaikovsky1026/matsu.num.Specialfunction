/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.24
 */
package matsu.num.specialfunction.bessel.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.commons.Trigonometry;
import matsu.num.specialfunction.GammaFunction;

/**
 * <p>
 * 最適化されたな1次のBessel関数の実装.
 * </p>
 * 
 * <p>
 * 1次Besselの計算戦略は次の通りである. <br>
 * besselJ, besselYともに, 小さいxではべき級数表示を,
 * 大きいxでは漸近級数の連分数表示をminimax近似したものを使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
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

        final double C0 = 1.0;
        final double C1 = -0.4999999999999994;
        final double C2 = 0.0833333333333135;
        final double C3 = -0.0069444444442244655;
        final double C4 = 3.4722222104223096E-4;
        final double C5 = -1.1574070570985904E-5;
        final double C6 = 2.755671274932813E-7;
        final double C7 = -4.914849968943784E-9;
        final double C8 = 6.503498175886429E-11;

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

        final double C0 = 0.5;
        final double C1 = -0.6249999999999974;
        final double C2 = 0.13888888888881984;
        final double C3 = -0.013599537036327625;
        final double C4 = 7.581018481993057E-4;
        final double C5 = -2.7391964740116576E-5;
        final double C6 = 6.948201429036592E-7;
        final double C7 = -1.3049027227019681E-8;
        final double C8 = 1.7995367774385983E-10;

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
        final double C0 = 1.0;
        final double C1 = 1.8028133585109065E-14;
        final double C2 = 7.499999999894075;
        final double C3 = 2.4266005149709E-7;
        final double C4 = -590.6252848049793;
        final double C5 = 0.19440756052322783;
        final double C6 = 177282.64919623727;
        final double C7 = 21736.83660395108;
        final double C8 = -1.1895644531298159E8;
        final double C9 = 2.6466500856222528E8;
        final double C10 = 1.376291484622012E11;
        final double C11 = -3.2530910986912153E12;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 1.0000000000339264;
        final double C1 = -3.293346082780188E-8;
        final double C2 = 7.5000138390168996;
        final double C3 = -0.0031758372046546413;
        final double C4 = -590.241602284164;
        final double C5 = -5.912900295202007;
        final double C6 = 171113.1595741806;
        final double C7 = 1189575.071164131;
        final double C8 = -2.3472477228658158E8;
        final double C9 = 7.27593244129726E9;
        final double C10 = -1.102426949007787E11;
        final double C11 = 7.161700014398915E11;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 1.000000018134829;
        final double C1 = -1.1057913887130982E-5;
        final double C2 = 7.503108126805166;
        final double C3 = -0.5325128595430222;
        final double C4 = -528.7885121327149;
        final double C5 = -5099.222505787415;
        final double C6 = 479106.83171726076;
        final double C7 = -1.2412887685026756E7;
        final double C8 = 1.9543738683472967E8;
        final double C9 = -1.998403936253549E9;
        final double C10 = 1.2337098084785572E10;
        final double C11 = -3.517443005217423E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 0.9999987112682444;
        final double C1 = 4.117478811942639E-4;
        final double C2 = 7.440454423245729;
        final double C3 = 5.085360419931288;
        final double C4 = -867.7185005270713;
        final double C5 = 9357.0780849898;
        final double C6 = 34016.41238819485;
        final double C7 = -2516394.3054168457;
        final double C8 = 3.966030291548673E7;
        final double C9 = -3.4507368773712814E8;
        final double C10 = 1.6899163930720885E9;
        final double C11 = -3.6673254147704825E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p1_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 0.9999922994591925;
        final double C1 = 0.0018968696618412231;
        final double C2 = 7.282512322092441;
        final double C3 = 15.266571874538553;
        final double C4 = -1309.6858120631102;
        final double C5 = 22920.088755996236;
        final double C6 = -266110.97318633803;
        final double C7 = 2270136.03652352;
        final double C8 = -1.4224945028250908E7;
        final double C9 = 6.24743079964777E7;
        final double C10 = -1.7256025192008442E8;
        final double C11 = 2.2608170847200462E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
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
        final double C0 = 6.742317347139188E-19;
        final double C1 = -3.0000000000000253;
        final double C2 = 1.592137262088292E-10;
        final double C3 = 52.499999609807574;
        final double C4 = 4.947421157441845E-4;
        final double C5 = -9095.995900795471;
        final double C6 = 176.76421923381042;
        final double C7 = 4125100.3001062903;
        final double C8 = 1.1717435005627932E7;
        final double C9 = -5.286090727949247E9;
        final double C10 = 1.4102827892888464E11;
        final double C11 = -1.0762749166587991E12;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = -1.1489019929221078E-10;
        final double C1 = -2.9999998723386643;
        final double C2 = -6.477072758550098E-5;
        final double C3 = 52.519832006572074;
        final double C4 = -4.076321363137765;
        final double C5 = -8504.809850300415;
        final double C6 = -61502.938288854326;
        final double C7 = 8740116.889799342;
        final double C8 = -2.2880625231792977E8;
        final double C9 = 2.8546189233838744E9;
        final double C10 = -1.1768978507778105E10;
        final double C11 = -5.985936460790506E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 3.1677173574945787E-8;
        final double C1 = -3.000017135493916;
        final double C2 = 0.004181432875740007;
        final double C3 = 51.896640482702104;
        final double C4 = 56.329771804860826;
        final double C5 = -12537.156149089902;
        final double C6 = 125209.2936523827;
        final double C7 = 2900783.074411482;
        final double C8 = -1.155200269129423E8;
        final double C9 = 1.8311773906213074E9;
        final double C10 = -1.5269895892269712E10;
        final double C11 = 5.5102665440116844E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 3.885997350298778E-7;
        final double C1 = -3.00015771766123;
        final double C2 = 0.029411663104482427;
        final double C3 = 49.16850752970546;
        final double C4 = 254.0407102296055;
        final double C5 = -22627.854547627787;
        final double C6 = 495432.06275492656;
        final double C7 = -6864252.3459210675;
        final double C8 = 6.590173156783584E7;
        final double C9 = -4.2903420524093854E8;
        final double C10 = 1.7151644721312206E9;
        final double C11 = -3.188882386995639E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q1_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = -7.340362255454794E-6;
        final double C1 = -2.998436134668127;
        final double C2 = -0.14591146574669106;
        final double C3 = 59.94819053660787;
        final double C4 = -190.74404116341955;
        final double C5 = -9691.690202448235;
        final double C6 = 224744.87945738126;
        final double C7 = -2788312.880019899;
        final double C8 = 2.261354861698649E7;
        final double C9 = -1.2020062092894305E8;
        final double C10 = 3.8311781323060405E8;
        final double C11 = -5.578450649685867E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
