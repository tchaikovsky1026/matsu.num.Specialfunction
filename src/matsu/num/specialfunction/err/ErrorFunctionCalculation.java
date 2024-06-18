/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.18
 */
package matsu.num.specialfunction.err;

import matsu.num.commons.Exponentiation;

/**
 * 誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 18.1
 */
public final class ErrorFunctionCalculation {

    public ErrorFunctionCalculation() {
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
        double valueForAbsX;
        if (absX >= 1) {
            valueForAbsX = 1 - Exponentiation.exp(-absX * absX) * erfcx_over1(absX);
        } else {
            valueForAbsX = erf_0To1(absX);
        }
        return x >= 0 ? valueForAbsX : -valueForAbsX;
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
        if (x >= 1) {
            return Exponentiation.exp(-x * x) * erfcx_over1(x);
        }
        return 1 - erf(x);
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
        if (x >= 1) {
            return erfcx_over1(x);
        }
        return Exponentiation.exp(x * x) * (1 - erf(x));
    }

    /**
     * {@literal 0 <= x <= 1} における erf(x).
     */
    private static double erf_0To1(double x) {
        double DN1 = 1.1283791670955126;
        double DN3 = 0.08827220707874833;
        double DN5 = 0.03818305184693074;
        double DN7 = -1.6069332689136896E-4;
        double DN9 = 1.1876775220564546E-4;
        double DN11 = -3.0841229312069887E-6;

        double DD0 = 1.0;
        double DD2 = 0.41156254001564285;
        double DD4 = 0.0710263619819068;
        double DD6 = 0.0061863130402539;
        double DD8 = 2.3420185312115596E-4;

        double x2 = x * x;
        double x4 = x2 * x2;
        double x8 = x4 * x4;
        double nume1 = DN1 + x2 * DN3 + x4 * (DN5 + x2 * DN7);
        double nume9 = DN9 + x2 * DN11;
        double nume = x * (nume1 + x8 * nume9);

        double denomi0 = DD0 + x2 * DD2 + x4 * (DD4 + x2 * DD6);
        double denomi8 = DD8;
        double denomi = denomi0 + x8 * denomi8;

        return nume / denomi;
    }

    /**
     * {@literal x >= 1} における erf(x).
     */
    private static double erfcx_over1(double x) {
        double t = 1 / x;
        double t2 = t * t;
        double t4 = t2 * t2;
        if (t < 0.5) {
            if (t < 0.25) {
                if (t < 0.125) {

                    double DN1 = 0.5641895835477564;
                    double DN2 = 0.24533720485199798;
                    double DN3 = 3.1306369463403305;
                    double DN4 = 0.5028943894813879;
                    double DN5 = 2.199683855787236;
                    double DN6 = -0.12880523661365864;
                    double DN7 = -0.2391393511033293;

                    double DD0 = 1.0;
                    double DD1 = 0.4348488735107914;
                    double DD2 = 6.0489095111746645;
                    double DD3 = 1.108781553383177;
                    double DD4 = 6.173291621503642;

                    double nume1 = DN1 + t * DN2 + t2 * (DN3 + t * DN4);
                    double nume5 = DN5 + t * DN6 + t2 * DN7;
                    double nume = t * (nume1 + t4 * nume5);

                    double denomi0 = DD0 + t * DD1 + t2 * (DD2 + t * DD3);
                    double denomi4 = DD4;
                    double denomi = denomi0 + t4 * denomi4;

                    return nume / denomi;

                } else {
                    double DN1 = 0.5641895862449408;
                    double DN2 = 1.155098340675547;
                    double DN3 = 3.7709450210010402;
                    double DN4 = 2.7963001302578627;
                    double DN5 = 2.2066812951697043;
                    double DN6 = -0.8334425367726905;
                    double DN7 = 0.15931842295729948;

                    double DD0 = 1.0;
                    double DD1 = 2.0473588320913727;
                    double DD2 = 7.1838156188550695;
                    double DD3 = 5.980190350428227;
                    double DD4 = 6.750621930206149;

                    double nume1 = DN1 + t * DN2 + t2 * (DN3 + t * DN4);
                    double nume5 = DN5 + t * DN6 + t2 * DN7;
                    double nume = t * (nume1 + t4 * nume5);

                    double denomi0 = DD0 + t * DD1 + t2 * (DD2 + t * DD3);
                    double denomi4 = DD4;
                    double denomi = denomi0 + t4 * denomi4;

                    return nume / denomi;

                }
            } else {
                double DN1 = 0.5641899411064255;
                double DN2 = 2.415705309372266;
                double DN3 = 6.6005443321473845;
                double DN4 = 9.091751635396884;
                double DN5 = 6.2725645532813274;
                double DN6 = -0.13683414313949438;
                double DN7 = 0.03530303406849633;
                double DN8 = -0.004435370750455192;

                double DD0 = 1.0;
                double DD1 = 4.281752022713011;
                double DD2 = 12.198671556906127;
                double DD3 = 18.26124790626759;
                double DD4 = 16.422534212601708;
                double DD5 = 5.9268692121165065;

                double nume1 = DN1 + t * DN2 + t2 * (DN3 + t * DN4);
                double nume5 = DN5 + t * DN6 + t2 * (DN7 + t * DN8);
                double nume = t * (nume1 + t4 * nume5);

                double denomi0 = DD0 + t * DD1 + t2 * (DD2 + t * DD3);
                double denomi4 = DD4 + t * DD5;
                double denomi = denomi0 + t4 * denomi4;

                return nume / denomi;

            }
        } else {
            if (t < 0.75) {

                double DN1 = 0.5641436862455586;
                double DN2 = 2.187681945064657;
                double DN3 = 4.390499233617424;
                double DN4 = 3.8793053368301083;
                double DN5 = 0.028217205537368617;
                double DN6 = -0.00631572215317755;
                double DN7 = 6.761378084951808E-4;

                double DD0 = 1.0;
                double DD1 = 3.8757334510493453;
                double DD2 = 8.30106542354672;
                double DD3 = 8.692656985173018;
                double DD4 = 3.959898769006462;

                double nume1 = DN1 + t * DN2 + t2 * (DN3 + t * DN4);
                double nume5 = DN5 + t * DN6 + t2 * DN7;
                double nume = t * (nume1 + t4 * nume5);

                double denomi0 = DD0 + t * DD1 + t2 * (DD2 + t * DD3);
                double denomi4 = DD4;
                double denomi = denomi0 + t4 * denomi4;

                return nume / denomi;

            } else {

                double DN1 = 0.5644872774995844;
                double DN2 = 1.7698076550109454;
                double DN3 = 3.0183998363902824;
                double DN4 = 1.894585851585952;
                double DN5 = 0.020757428421423363;
                double DN6 = -0.004031926993635526;
                double DN7 = 3.6856613459967935E-4;

                double DD0 = 1.0;
                double DD1 = 3.1442404109052617;
                double DD2 = 5.805163908185843;
                double DD3 = 5.078260236825702;
                double DD4 = 1.961703355169866;

                double nume1 = DN1 + t * DN2 + t2 * (DN3 + t * DN4);
                double nume5 = DN5 + t * DN6 + t2 * DN7;
                double nume = t * (nume1 + t4 * nume5);

                double denomi0 = DD0 + t * DD1 + t2 * (DD2 + t * DD3);
                double denomi4 = DD4;
                double denomi = denomi0 + t4 * denomi4;

                return nume / denomi;

            }
        }
    }
}
