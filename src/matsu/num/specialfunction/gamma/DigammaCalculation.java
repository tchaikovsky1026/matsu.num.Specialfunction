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

import matsu.num.commons.Exponentiation;

/**
 * ディガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
public final class DigammaCalculation {

    private static final DigammaCalculation INSTANCE = new DigammaCalculation();

    private DigammaCalculation() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    /**
     * ディガンマ関数
     * <i>&psi;</i>(<i>x</i>) =
     * (d/d<i>x</i>) log<sub>e</sub>&Gamma;(<i>x</i>)
     * を計算する.
     * 
     * @param x
     * @return dgamma(x)
     */
    public double digamma(double x) {
        /*
         * x < 0: NaN
         * x = 0: -inf
         * x = +inf: +inf
         */
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= 10) {
            //10以上は漸近展開を使う.
            return digammaStirlingResidual_largekernel(x) + digammaStirling(x);
        }

        //10以下はx=2における級数展開を使う.
        if (x < 1.5) {
            double shiftValue = 0;
            double shiftX = x;
            while (shiftX <= 1.5) {
                shiftValue -= 1 / shiftX;
                shiftX += 1;
            }
            return digamma_smallkernel(shiftX - 2) + shiftValue;
        } else {
            double shiftValue = 0;
            double shiftX = x;
            while (shiftX > 5.5) {
                double shiftX2 = 2 * shiftX;
                shiftValue += (shiftX2 - 3) / ((shiftX - 1) * (shiftX - 2))
                        + (shiftX2 - 7) / ((shiftX - 3) * (shiftX - 4));
                shiftX -= 4;
            }
            while (shiftX > 2.5) {
                shiftX -= 1;
                shiftValue += 1 / shiftX;
            }
            return digamma_smallkernel(shiftX - 2) + shiftValue;
        }
    }

    /**
     * ディガンマ関数のスターリング近似: log(x) - 0.5/x
     *
     * @param x
     * @return digammaStirling(x)
     */
    private double digammaStirling(double x) {
        return Exponentiation.log(x) - 0.5 / x;
    }

    /**
     * x≧10 のときの ψ(x)-log(x)+0.5/x
     */
    private double digammaStirlingResidual_largekernel(double x) {
        //スターリングの公式によりψ(x)の値を求める
        final double DGL2 = -0.08333333333328739357470911184372542;
        final double DGL4 = 0.008333333267783087213545265242688808;
        final double DGL6 = -0.003968223537336971425914891426605989;
        final double DGL8 = 0.004160473881603943239794112222091733;
        final double DGL10 = -0.006996922395754135301453770623701787;

        final double t = 1 / x;
        final double t2 = t * t;
        final double t4 = t2 * t2;
        final double t8 = t4 * t4;

        final double value0 = t2 * DGL2 + t4 * (DGL4 + t2 * DGL6);
        final double value8 = DGL8 + t2 * DGL10;

        return value0 + t8 * value8;
    }

    /**
     *
     * @param x |x|＜0.5
     * @return ψ(2+x)
     */
    private double digamma_smallkernel(double x) {
        if (x < -0.0625) {
            final double DGS0 = 0.4227843350989869268935081341859300;
            final double DGS1 = 0.6449340668878360083791301083204227;
            final double DGS2 = -0.2020569018268229512768081189492156;
            final double DGS3 = 0.08232325997486556963347228209222169;
            final double DGS4 = -0.03692741608794768694921370000029979;
            final double DGS5 = 0.01734610003169764330967602293218115;
            final double DGS6 = -0.008329762652687279041420284891443240;
            final double DGS7 = 0.004168753978403772810530353137478453;
            final double DGS8 = -0.001694625750348581527057292253880704;
            final double DGS9 = 0.001779416706889607114104719424847390;
            final double DGS10 = 0.0009081297375073063381469503915570035;
            final double DGS11 = 0.001963217921810770020954370174617190;
            final double DGS12 = 0.001201100813324397850027751507656302;
            final double DGS13 = 0.0005823274424193981229438619321738804;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = (DGS0 + x * DGS1) + x2 * (DGS2 + x * DGS3);
            final double value4 = (DGS4 + x * DGS5) + x2 * (DGS6 + x * DGS7);
            final double value8 = (DGS8 + x * DGS9) + x2 * (DGS10 + x * DGS11);
            final double value12 = DGS12 + x * DGS13;
            return (value0 + x4 * value4) + x8 * (value8 + x4 * value12);

        } else {
            final double DGS0 = 0.4227843350984671273000466851314970;
            final double DGS1 = 0.6449340668482268346802984739065999;
            final double DGS2 = -0.2020569031595553524777377953722888;
            final double DGS3 = 0.08232323371046633392485463720375387;
            final double DGS4 = -0.03692775515859214438202225761846589;
            final double DGS5 = 0.01734306233633621246972786345770444;
            final double DGS6 = -0.008349277945778360422869060051804784;
            final double DGS7 = 0.004077314689775532683867896512576123;
            final double DGS8 = -0.002007873283621135004570801122200925;
            final double DGS9 = 0.0009914056031367780032441186761955929;
            final double DGS10 = -0.0004824183789521655651510244300943983;
            final double DGS11 = 0.0002177993956108496467644385849686818;
            final double DGS12 = -0.00007815034253940785821427892294970496;
            final double DGS13 = 0.00001560567542719673471576695610123257;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = (DGS0 + x * DGS1) + x2 * (DGS2 + x * DGS3);
            final double value4 = (DGS4 + x * DGS5) + x2 * (DGS6 + x * DGS7);
            final double value8 = (DGS8 + x * DGS9) + x2 * (DGS10 + x * DGS11);
            final double value12 = DGS12 + x * DGS13;
            return (value0 + x4 * value4) + x8 * (value8 + x4 * value12);
        }
    }

    /**
     * 
     * @return インスタンス.
     */
    public static DigammaCalculation instance() {
        return INSTANCE;
    }

}
