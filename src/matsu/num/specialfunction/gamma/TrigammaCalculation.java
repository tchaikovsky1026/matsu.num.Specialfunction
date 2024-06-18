/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.gamma;

import java.util.Objects;

/**
 * トリガンマ関数の計算に関する.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class TrigammaCalculation {

    private static final TrigammaCalculation INSTANCE = new TrigammaCalculation();

    private TrigammaCalculation() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    /**
     * トリガンマ関数
     * <i>&psi;</i>'(<i>x</i>) =
     * (d/d<i>x</i>) <i>&psi;</i>(<i>x</i>)
     * を計算する.
     * 
     * @param x
     * @return trigamma(x)
     */
    public double trigamma(double x) {
        /*
         * x < 0; NaN
         * x = 0: +inf
         * x = +inf: 0
         */

        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= 6) {
            //漸近展開により計算.
            double shiftX = x;
            double shiftValue = 0;
            if (x < 10) {
                final double x1 = shiftX + 1;
                final double x2 = shiftX + 2;
                final double x3 = shiftX + 3;
                final double x_sq = shiftX * shiftX;
                final double x1_sq = x1 * x1;
                final double x2_sq = x2 * x2;
                final double x3_sq = x3 * x3;

                shiftX += 4;
                shiftValue = ((x_sq + x1_sq) / (x_sq * x1_sq) + (x2_sq + x3_sq) / (x2_sq * x3_sq));
            }
            return trigamma_largekernel(shiftX) + shiftValue;
        }

        //x = 2における級数展開を使う.
        double shiftValue = 0;
        double shiftX = x;
        if (x < 2) {
            while (shiftX < 1.5) {
                shiftValue += 1 / (shiftX * shiftX);
                shiftX += 1;
            }
        } else {
            while (shiftX > 3.5) {
                final double x1 = shiftX - 1;
                final double x2 = shiftX - 2;
                final double x1_sq = x1 * x1;
                final double x2_sq = x2 * x2;
                shiftValue -= (x1_sq + x2_sq) / (x1_sq * x2_sq);
                shiftX -= 2;
            }
            if (shiftX > 2.5) {
                shiftX -= 1;
                shiftValue -= 1 / (shiftX * shiftX);
            }
        }
        return trigamma_smallkernel(shiftX - 2) + shiftValue;
    }

    /**
     * x ≧ 10で使える, 漸近展開によるψ'(x)を求める
     */
    private static double trigamma_largekernel(double x) {

        final double TGL1 = 1;
        final double TGL3 = 0.1666666666661263898063145225541032;
        final double TGL5 = -0.03333333256159259717377900029417934;
        final double TGL7 = 0.02380916504019969425899586054297418;
        final double TGL9 = -0.03326016771059407656862087319537770;
        final double TGL11 = 0.06889111178216503522310709071736891;

        final double t = 1 / x;
        final double t2 = t * t;
        final double t4 = t2 * t2;
        final double t8 = t4 * t4;

        final double value1 = TGL1 + t2 * TGL3 + t4 * (TGL5 + t2 * TGL7);
        final double value9 = TGL9 + t2 * TGL11;

        return 0.5 * t2 + t * (value1 + t8 * value9);
    }

    /**
     *
     * @param x |x|＜0.5
     * @return ψ'(2+x)
     */
    private static double trigamma_smallkernel(double x) {
        if (x < -0.0625) {
            final double TGS0 = 0.6449340668456999168497507162920213;
            final double TGS1 = -0.4041138065181304139813859121911892;
            final double TGS2 = 0.2469696942023276137942077245599758;
            final double TGS3 = -0.1477111622248231355610802708842988;
            final double TGS4 = 0.08671341131472947710948278387683772;
            final double TGS5 = -0.05011333777810971354117571694548063;
            final double TGS6 = 0.02842353892632418505202635413949106;
            final double TGS7 = -0.01664098807739095464301847756530598;
            final double TGS8 = 0.006906124423289308478155764045926724;
            final double TGS9 = -0.01024718917269064281787466308062876;
            final double TGS10 = -0.007111947206864510219389820213103861;
            final double TGS11 = -0.01390591658700268547873806059173431;
            final double TGS12 = -0.009092274937420192460729556043384463;
            final double TGS13 = -0.004424088202617627097062073470145046;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = (TGS0 + x * TGS1) + x2 * (TGS2 + x * TGS3);
            final double value4 = (TGS4 + x * TGS5) + x2 * (TGS6 + x * TGS7);
            final double value8 = (TGS8 + x * TGS9) + x2 * (TGS10 + x * TGS11);
            final double value12 = TGS12 + x * TGS13;
            return (value0 + x4 * value4) + x8 * (value8 + x4 * value12);

        } else {
            final double TGS0 = 0.6449340668482265608349744266083016;
            final double TGS1 = -0.4041138063191893981202082501004250;
            final double TGS2 = 0.2469697011330286179177876170816620;
            final double TGS3 = -0.1477110205697577884989197627527309;
            final double TGS4 = 0.08671531008686449516664952773340815;
            final double TGS5 = -0.05009566706727851631671089467297858;
            final double TGS6 = 0.02854149120130557101454454802134196;
            final double TGS7 = -0.01606675124687139917402683994480659;
            final double TGS8 = 0.008946857906515484131988362792732850;
            final double TGS9 = -0.004917059859922661799459220490129982;
            final double TGS10 = 0.002618192785043820229543437281011416;
            final double TGS11 = -0.001264562248874002145949616693724137;
            final double TGS12 = 0.0004729449937137596727192831548730589;
            final double TGS13 = -0.00009628890028858592116986189561823887;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = (TGS0 + x * TGS1) + x2 * (TGS2 + x * TGS3);
            final double value4 = (TGS4 + x * TGS5) + x2 * (TGS6 + x * TGS7);
            final double value8 = (TGS8 + x * TGS9) + x2 * (TGS10 + x * TGS11);
            final double value12 = TGS12 + x * TGS13;
            return (value0 + x4 * value4) + x8 * (value8 + x4 * value12);
        }
    }

    /**
     * 
     * @return インスタンス.
     */
    public static TrigammaCalculation instance() {
        return INSTANCE;
    }
}
