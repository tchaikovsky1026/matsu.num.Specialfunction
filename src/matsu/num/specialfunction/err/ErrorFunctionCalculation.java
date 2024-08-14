/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.14
 */
package matsu.num.specialfunction.err;

import matsu.num.commons.Exponentiation;

/**
 * 誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 19.3
 */
public final class ErrorFunctionCalculation {

    /**
     * 1/sqrt(pi)
     */
    // = 1d / Math.sqrt(Math.PI)
    private static final double ONE_OVER_SQRT_PI = 0.5641895835477563;

    /**
     * 唯一のコンストラクタ.
     */
    public ErrorFunctionCalculation() {
        super();
    }

    /**
     * 誤差関数
     * erf(<i>x</i>).
     * 
     * @param x x
     * @return erf(x)
     */
    public double erf(double x) {
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

    /**
     * 相補誤差関数
     * erfc(<i>x</i>) = 1 - erf(<i>x</i>).
     * 
     * @param x x
     * @return erfc(x)
     */
    public double erfc(double x) {
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

    /**
     * スケーリング相補誤差関数
     * erfcx(<i>x</i>) =
     * exp(<i>x</i><sup>2</sup>)erfc(<i>x</i>).
     * 
     * @param x x
     * @return erfcx(x)
     */
    public double erfcx(double x) {
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

        final double C0 = 1.0;
        final double C1 = -0.3333333333333325;
        final double C2 = 0.09999999999994759;
        final double C3 = -0.023809523808403246;
        final double C4 = 0.004629629617726746;
        final double C5 = -7.575756844203689E-4;
        final double C6 = 1.0683732519520164E-4;
        final double C7 = -1.3226804769677749E-5;
        final double C8 = 1.4577322887575652E-6;
        final double C9 = -1.437281699650162E-7;
        final double C10 = 1.219322561041248E-8;
        final double C11 = -6.968611403365222E-10;

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

        final double C0 = 1.0;
        final double C1 = -0.4999999999999794;
        final double C2 = 0.7499999998896585;
        final double C3 = -1.8749998935904393;
        final double C4 = 6.562456769307346;
        final double C5 = -29.52201664856533;
        final double C6 = 161.29259264501337;
        final double C7 = -975.0386603103184;
        final double C8 = 4659.374670704266;

        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8;

        final double u4 = u2 * u2;
        return (v0 + u4 * (v4 + u4 * v8));
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_1_to_2_Over8(double t) {
        assert t >= 1d / 8;
        assert t <= 2d / 8;

        final double C0 = 0.9999999942522338;
        final double C1 = 2.836374561372571E-7;
        final double C2 = -0.5000047413415732;
        final double C3 = -4.910828648887686E-6;
        final double C4 = 0.7515306677948306;
        final double C5 = -0.030119876362158488;
        final double C6 = -1.5448330694503856;
        final double C7 = -2.3736496319818525;
        final double C8 = 18.13917821828396;
        final double C9 = -37.07880559258125;
        final double C10 = 37.05679170026917;
        final double C11 = -15.623222277041659;

        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);

        final double t4 = t2 * t2;
        return (v0 + t4 * (v4 + t4 * v8));
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_2_to_3_Over8(double t) {
        assert t >= 2d / 8;
        assert t <= 3d / 8;

        final double C0 = 0.9999959151429135;
        final double C1 = 1.6896955850103377E-4;
        final double C2 = -0.5032066238001722;
        final double C3 = 0.03685624913922959;
        final double C4 = 0.4652704562228117;
        final double C5 = 1.5459124548341412;
        final double C6 = -7.826647373702869;
        final double C7 = 15.763173417933167;
        final double C8 = -19.044276206224673;
        final double C9 = 14.47588078800862;
        final double C10 = -6.438178428516741;
        final double C11 = 1.2830956748463003;

        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);

        final double t4 = t2 * t2;
        return (v0 + t4 * (v4 + t4 * v8));
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_3_to_4_Over8(double t) {
        assert t >= 3d / 8;
        assert t <= 4d / 8;

        final double C0 = 1.000037610887234;
        final double C1 = -9.49889291500491E-4;
        final double C2 = -0.4895603864571144;
        final double C3 = -0.06296019569752366;
        final double C4 = 0.9515224355334557;
        final double C5 = -0.10932106189777946;
        final double C6 = -3.8129941864191963;
        final double C7 = 8.839691201568407;
        final double C8 = -10.73329700020704;
        final double C9 = 7.880477575224544;
        final double C10 = -3.3352149106360627;
        final double C11 = 0.63091070046683;

        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);

        final double t4 = t2 * t2;
        return (v0 + t4 * (v4 + t4 * v8));
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_4_to_6_Over8(double t) {
        assert t >= 4d / 8;
        assert t <= 6d / 8;

        final double C0 = 1.0004261192886856;
        final double C1 = -0.009051724439852388;
        final double C2 = -0.41223850069230505;
        final double C3 = -0.5088382162240198;
        final double C4 = 2.6778970071496313;
        final double C5 = -4.822007723939706;
        final double C6 = 5.442011537812434;
        final double C7 = -4.234435132422705;
        final double C8 = 2.284138624633327;
        final double C9 = -0.8175678208684948;
        final double C10 = 0.17406049561765485;
        final double C11 = -0.016522425192743776;

        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);

        final double t4 = t2 * t2;
        return (v0 + t4 * (v4 + t4 * v8));
    }

    /**
     * erfcx(x) = (t/sqrt(pi)) * F(t) における F(t).
     */
    private double erfcx_largeX_Factor_as_t_6_to_8_Over8(double t) {
        assert t >= 6d / 8;
        assert t <= 8d / 8;

        final double C0 = 0.9991005672158109;
        final double C1 = 0.008834181718224726;
        final double C2 = -0.5217997602472936;
        final double C3 = -0.10700254960567943;
        final double C4 = 1.6984956197746828;
        final double C5 = -3.1588497966280995;
        final double C6 = 3.43821709777213;
        final double C7 = -2.5264680192960327;
        final double C8 = 1.2789480193198766;
        final double C9 = -0.43096408095641525;
        final double C10 = 0.08748050211597176;
        final double C11 = -0.008119625041864576;

        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);

        final double t4 = t2 * t2;
        return (v0 + t4 * (v4 + t4 * v8));
    }
}
