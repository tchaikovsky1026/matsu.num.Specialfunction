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
 * 誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class ErfCalcOptimized implements ErrorFunctionCalculation {

    /**
     * 1/sqrt(pi)
     */
    // = 1d / Math.sqrt(Math.PI)
    private static final double ONE_OVER_SQRT_PI = 0.5641895835477563;

    /**
     * 唯一のコンストラクタ.
     */
    ErfCalcOptimized() {
        super();
    }

    @Override
    public double erf(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        /*
         * x = +inf: 1
         * x = -inf: -1
         */
        double absX = Math.abs(x);
        if (absX <= 1) {
            return erf_smallX(x);
        }

        double erfAtAbsX = 1 - Exponentiation.exp(-absX * absX) * erfcx_largeX(absX);
        return x < 0 ? -erfAtAbsX : erfAtAbsX;
    }

    @Override
    public double erfc(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        /*
         * x = +inf: 0
         * x = -inf: 2
         */
        if (x > 1) {
            return Exponentiation.exp(-x * x) * erfcx_largeX(x);
        }
        if (x >= -1) {
            return 1 - erf_smallX(x);
        }
        return 2 - Exponentiation.exp(-x * x) * erfcx_largeX(-x);
    }

    @Override
    public double erfcx(double x) {
        if (Double.isNaN(x)) {
            return Double.NaN;
        }

        /*
         * x = +inf: 0
         * x = -inf: +inf
         */
        if (x > 1) {
            return erfcx_largeX(x);
        }
        if (x >= -1) {
            return Exponentiation.exp(x * x) * (1 - erf_smallX(x));
        }
        return 2 * Exponentiation.exp(x * x) - erfcx_largeX(-x);
    }

    /**
     * {@literal -1 <= x <= 1} における erf(x).
     */
    private double erf_smallX(double x) {
        assert x >= -1;
        assert x <= 1;

        final double C0 = 1;
        final double C1 = -0.33333333333333211013305554790822;
        final double C2 = 0.099999999999928688233243155648269;
        final double C3 = -0.023809523808077918230534104983609;
        final double C4 = 0.0046296296149990528350518839973084;
        final double C5 = -0.00075757567146320414005774313508529;
        final double C6 = 0.00010683728756562559474247127038328;
        final double C7 = -0.000013226735562696964608084865454945;
        final double C8 = 0.0000014576513588448841087011976041004;
        final double C9 = -1.4366984692932032396369201147202E-7;
        final double C10 = 1.2169603543560378638938495076569E-8;
        final double C11 = -6.9274587756057320125002529260159E-10;

        final double u = x * x;
        final double u2 = u * u;

        double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        double v8 = C8 + u * C9 + u2 * (C10 + u * C11);

        final double u4 = u2 * u2;
        return ((2 * ONE_OVER_SQRT_PI) * x) *
                (v0 + u4 * (v4 + u4 * v8));
    }

    /**
     * {@literal x >= 1} における erfcx(x).
     */
    private double erfcx_largeX(double x) {
        assert x >= 1;

        final double t = 1 / x;
        final double limit = ONE_OVER_SQRT_PI * t;

        switch ((int) (t * 8)) {
            case 0:
                return limit * erfcx_largeX_Factor_as_t_0_to_1_Over8(t);
            case 1:
                return limit * erfcx_largeX_Factor_as_t_1_to_2_Over8(t);
            case 2:
                return limit * erfcx_largeX_Factor_as_t_2_to_3_Over8(t);
            case 3:
                return limit * erfcx_largeX_Factor_as_t_3_to_4_Over8(t);
            case 4, 5:
                return limit * erfcx_largeX_Factor_as_t_4_to_6_Over8(t);
            default:
                return limit * erfcx_largeX_Factor_as_t_6_to_8_Over8(t);
        }
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_0_to_1_Over8(double t) {
        assert t >= 0;
        assert t <= 1d / 8;

        final double C0 = 1;
        final double C1 = -0.49999999999997939211997360377688;
        final double C2 = 0.74999999988965955899593352379293;
        final double C3 = -1.8749998935912959232914490254705;
        final double C4 = 6.5624567695810062912148171457510;
        final double C5 = -29.522016692393820570035267529673;
        final double C6 = 161.29259636083518663434334012821;
        final double C7 = -975.03881962192086759161088779593;
        final double C8 = 4659.3773896744510709061795677831;

        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8;

        final double u4 = u2 * u2;
        return v0 + u4 * (v4 + u4 * v8);
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_1_to_2_Over8(double t) {
        assert t >= 1d / 8;
        assert t <= 2d / 8;

        final double t_shift = 0.1875;
        final double s = t - t_shift;

        final double C0 = 0.98327606415200059769273338460108;
        final double C1 = -0.16997299152137775611668741630707;
        final double C2 = -0.37016379403729434557985780610570;
        final double C3 = 0.37906708423480455288415797094175;
        final double C4 = 0.14380501529844881797941749431979;
        final double C5 = -0.74844233233969451597166811972991;
        final double C6 = 0.61002618273108639292893507351699;
        final double C7 = 0.84755205886684882776757508291181;
        final double C8 = -2.7988465623695321615237845921616;
        final double C9 = 2.1937165946066621878873548503090;
        final double C10 = 4.8339022473083468553475949082386;
        final double C11 = -15.623447448816319497809069019227;

        final double s2 = s * s;

        final double v0 = C0 + s * C1 + s2 * (C2 + s * C3);
        final double v4 = C4 + s * C5 + s2 * (C6 + s * C7);
        final double v8 = C8 + s * C9 + s2 * (C10 + s * C11);

        final double s4 = s2 * s2;
        return v0 + s4 * (v4 + s4 * v8);
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_2_to_3_Over8(double t) {
        assert t >= 2d / 8;
        assert t <= 3d / 8;

        final double t_shift = 0.3125;
        final double s = t - t_shift;

        final double C0 = 0.95700084784058584007627966687363;
        final double C1 = -0.24441027717049620278972469418972;
        final double C2 = -0.22662694567300403621853750286797;
        final double C3 = 0.36115650120214256825259861001439;
        final double C4 = -0.16070229862285439227227036453607;
        final double C5 = -0.23706622675598633501105236403038;
        final double C6 = 0.56249201895505052282091802098359;
        final double C7 = -0.49505325249203652541931280862023;
        final double C8 = -0.16275643038786530161277937184984;
        final double C9 = 1.2619738601782418256004321598628;
        final double C10 = -2.0255800816363879864431266572922;

        final double s2 = s * s;

        final double v0 = C0 + s * C1 + s2 * (C2 + s * C3);
        final double v4 = C4 + s * C5 + s2 * (C6 + s * C7);
        final double v8 = C8 + s * C9 + s2 * C10;

        final double s4 = s2 * s2;
        return v0 + s4 * (v4 + s4 * v8);
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_3_to_4_Over8(double t) {
        assert t >= 3d / 8;
        assert t <= 4d / 8;

        final double t_shift = 0.4375;
        final double s = t - t_shift;

        final double C0 = 0.92356933988184421513904189001733;
        final double C1 = -0.28559298769513536937409458062677;
        final double C2 = -0.10914744828862020023027189341699;
        final double C3 = 0.26152599236743826779560287197597;
        final double C4 = -0.21025447240743480207410369289533;
        final double C5 = 0.029925239986987813966391163296463;
        final double C6 = 0.17954000142854018268914519486255;
        final double C7 = -0.31275226705250958749801116104868;
        final double C8 = 0.28633040157816320942105920306956;
        final double C9 = -0.062503797144407933449600353231974;
        final double C10 = -0.30003564158986670490312111025603;

        final double s2 = s * s;

        final double v0 = C0 + s * C1 + s2 * (C2 + s * C3);
        final double v4 = C4 + s * C5 + s2 * (C6 + s * C7);
        final double v8 = C8 + s * C9 + s2 * C10;

        final double s4 = s2 * s2;
        return v0 + s4 * (v4 + s4 * v8);
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_4_to_6_Over8(double t) {
        assert t >= 4d / 8;
        assert t <= 6d / 8;

        final double t_shift = 0.625;
        final double s = t - t_shift;

        final double C0 = 0.86766009495471210526080841377347;
        final double C1 = -0.30412764979654441743081068270274;
        final double C2 = -0.0022945101684705126903787855160058;
        final double C3 = 0.12779776988906186158437618900195;
        final double C4 = -0.13831959343255341754026608662504;
        final double C5 = 0.087914275479517055333435445905291;
        final double C6 = -0.017877280314341554948677777777182;
        final double C7 = -0.043331282713478620439099958769815;
        final double C8 = 0.079373057119372917473924922782435;
        final double C9 = -0.084656618301346535777215720335152;
        final double C10 = 0.062031673039912746573043763744298;
        final double C11 = -0.016686111590994506173319549230832;
        final double C12 = -0.033304767861859203584817810703721;

        final double s2 = s * s;

        final double v0 = C0 + s * C1 + s2 * (C2 + s * C3);
        final double v4 = C4 + s * C5 + s2 * (C6 + s * C7);
        final double v8 = C8 + s * C9 + s2 * (C10 + s * C11);
        final double v12 = C12;

        final double s4 = s2 * s2;
        final double s8 = s4 * s4;
        return v0 + s4 * v4 + s8 * (v8 + s4 * v12);
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_6_to_8_Over8(double t) {
        assert t >= 6d / 8;
        assert t <= 8d / 8;

        final double t_shift = 0.875;
        final double s = t - t_shift;

        final double C0 = 0.79302109108861479643257768941727;
        final double C1 = -0.28839027691393153582094803174772;
        final double C2 = 0.053878571640878868720940702690210;
        final double C3 = 0.035911529355976180686315898901373;
        final double C4 = -0.054909444994957211471268183356923;
        final double C5 = 0.044499300358080709382492810168762;
        final double C6 = -0.026152375260935024016148276440807;
        final double C7 = 0.0096729159428531649848783192893150;
        final double C8 = 0.0015619571586281228594336910694049;
        final double C9 = -0.0074140145722840225452041977292677;
        final double C10 = 0.0093508538967263078086536289767563;
        final double C11 = -0.0082489166872836116678911096105687;

        final double s2 = s * s;

        final double v0 = C0 + s * C1 + s2 * (C2 + s * C3);
        final double v4 = C4 + s * C5 + s2 * (C6 + s * C7);
        final double v8 = C8 + s * C9 + s2 * (C10 + s * C11);

        final double s4 = s2 * s2;
        return v0 + s4 * (v4 + s4 * v8);
    }
}
