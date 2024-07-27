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
 * 最適化されたな0次のBessel関数の実装.
 * </p>
 * *
 * <p>
 * 0次Besselの計算戦略は次の通りである. <br>
 * besselJ, besselYともに, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示をminimax近似を使う.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.0
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

        final double C0 = 1.0;
        final double C1 = -0.9999999999999939;
        final double C2 = 0.2499999999998026;
        final double C3 = -0.027777777775588344;
        final double C4 = 0.0017361110993640127;
        final double C5 = -6.944440956087581E-5;
        final double C6 = 1.9289519322561646E-6;
        final double C7 = -3.930680470901215E-8;
        final double C8 = 5.820846987608256E-10;

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

        final double C0 = -1.5572927699771338E-16;
        final double C1 = -0.9999999999999748;
        final double C2 = 0.37499999999932576;
        final double C3 = -0.05092592591899503;
        final double C4 = 0.0036168981124488683;
        final double C5 = -1.5856471147575804E-4;
        final double C6 = 4.725904374965468E-6;
        final double C7 = -1.0189980050842E-7;
        final double C8 = 1.5777331283252337E-9;

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
        final double C0 = 1.0;
        final double C1 = -1.6838473485278904E-14;
        final double C2 = -4.499999999900963;
        final double C3 = -2.2713966139897845E-7;
        final double C4 = 459.3752669499251;
        final double C5 = -0.18253538859786644;
        final double C6 = -150000.59680489675;
        final double C7 = -20538.554023648383;
        final double C8 = 1.0520336351504715E8;
        final double C9 = -2.5841453571722484E8;
        final double C10 = -1.2342019624549278E11;
        final double C11 = 2.945793277555433E12;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 0.9999999999660288;
        final double C1 = 3.340468705294308E-8;
        final double C2 = -4.500014330084117;
        final double C3 = 0.003418743917641187;
        final double C4 = 458.91831864629495;
        final double C5 = 20.826643009432473;
        final double C6 = -145973.0059060838;
        final double C7 = -967072.2942188316;
        final double C8 = 2.0466831134801525E8;
        final double C9 = -6.4473025138152275E9;
        final double C10 = 9.877049602955203E10;
        final double C11 = -6.470110886118195E11;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = 0.9999999851066952;
        final double C1 = 9.17875087099178E-6;
        final double C2 = -4.502608160609948;
        final double C3 = 0.4518154035159707;
        final double C4 = 406.32154068119695;
        final double C5 = 4424.092546907002;
        final double C6 = -414807.6591830584;
        final double C7 = 1.1013496392246846E7;
        final double C8 = -1.7736875072127005E8;
        final double C9 = 1.8518176146616652E9;
        final double C10 = -1.1660624535850773E10;
        final double C11 = 3.3893924234490406E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = 1.000001151076437;
        final double C1 = -3.6967515572725214E-4;
        final double C2 = -4.446191056825497;
        final double C3 = -4.635251073353729;
        final double C4 = 715.1921888760493;
        final double C5 = -8846.06271011304;
        final double C6 = -2862.78891668898;
        final double C7 = 1768687.3409929413;
        final double C8 = -3.033180014029162E7;
        final double C9 = 2.731613787938727E8;
        final double C10 = -1.3644642766990247E9;
        final double C11 = 3.000254208671835E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double p0_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 1.000005640507333;
        final double C1 = -0.0014315720934071144;
        final double C2 = -4.3309785871584685;
        final double C3 = -12.20299380252705;
        final double C4 = 1049.4940145632218;
        final double C5 = -19270.63048095585;
        final double C6 = 231188.80859349313;
        final double C7 = -2012975.9779622487;
        final double C8 = 1.2736785437109483E7;
        final double C9 = -5.592238108700704E7;
        final double C10 = 1.529702907196584E8;
        final double C11 = -1.9677976444027072E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
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
        final double C0 = -6.11773596570153E-19;
        final double C1 = 1.000000000000023;
        final double C2 = -1.4465306367732395E-10;
        final double C3 = -37.49999964524945;
        final double C4 = -4.501340019124056E-4;
        final double C5 = 7442.212731914853;
        final double C6 = -161.10417523304875;
        final double C7 = -3572543.4995144224;
        final double C8 = -1.070438861230114E7;
        final double C9 = 4.762621078947486E9;
        final double C10 = -1.2929081218147691E11;
        final double C11 = 1.0205178644594338E12;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_1_to_2_over128(double t) {
        final double C0 = 1.0330137332849201E-10;
        final double C1 = 0.9999998850688697;
        final double C2 = 5.8397776341800704E-5;
        final double C3 = -37.51791130234124;
        final double C4 = 3.688946256181218;
        final double C5 = 6905.92068447473;
        final double C6 = 55955.68174654103;
        final double C7 = -7786940.055483075;
        final double C8 = 2.1003961962542242E8;
        final double C9 = -2.7647479261422696E9;
        final double C10 = 1.3932571497691063E10;
        final double C11 = 3.127957586460085E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_2_to_4_over128(double t) {
        final double C0 = -2.9114572623848437E-8;
        final double C1 = 1.0000157978281126;
        final double C2 = -0.003870445618223077;
        final double C3 = -36.93845705640281;
        final double C4 = -52.85189030956934;
        final double C5 = 10716.01710893512;
        final double C6 = -123020.35133538257;
        final double C7 = -2051186.6017499375;
        final double C8 = 9.303595898828028E7;
        final double C9 = -1.5235011290613344E9;
        final double C10 = 1.2919316636204483E10;
        final double C11 = -4.712093610315727E10;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_4_to_6_over128(double t) {
        final double C0 = -2.701296743822304E-7;
        final double C1 = 1.0001161127020812;
        final double C2 = -0.02265178041643512;
        final double C3 = -34.83934241553915;
        final double C4 = -209.0192402257614;
        final double C5 = 18855.474514328038;
        final double C6 = -426744.7621980799;
        final double C7 = 6070248.918742979;
        final double C8 = -5.9536239419427454E7;
        final double C9 = 3.9458922079072094E8;
        final double C10 = -1.6019294858656123E9;
        final double C11 = 3.0188747070760603E9;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double q0_asymptoticTerm_6_to_8_over128(double t) {
        final double C0 = 6.223925380869342E-6;
        final double C1 = 0.9986521045507517;
        final double C2 = 0.12824795971047248;
        final double C3 = -44.22971973295276;
        final double C4 = 183.10864649327945;
        final double C5 = 7314.708986545299;
        final double C6 = -182415.70256721333;
        final double C7 = 2348756.0701807407;
        final double C8 = -1.956734961513427E7;
        final double C9 = 1.0631809879331361E8;
        final double C10 = -3.4542905555606306E8;
        final double C11 = 5.118135681979453E8;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
