/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.commons.Trigonometry;
import matsu.num.specialfunction.GammaFunction;

/**
 * 1次のBessel関数.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
final class Bessel1stOrderLegacyOptimized extends Bessel1st {

    private static final double EULER_MASCHERONI_GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;
    private static final double INV_2_PI = 1.0 / (2 * Math.PI);
    private static final double TWO_OVER_PI = 2.0 / Math.PI;
    private static final double SQRT_2_OVER_PI = Math.sqrt(2.0 / Math.PI);
    private static final double LN_2 = Math.log(2);

    Bessel1stOrderLegacyOptimized() {
        super();
    }

    @Override
    public double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < 1) {
            return besselJ1_Under_1(x);
        } else {
            return besselJ1_Over_1(x);
        }
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < 1) {
            return besselY1_Under_1(x);
        } else {
            return besselY1_Over_1(x);
        }
    }

    private static double besselJ1_Under_1(double x) {
        double DN1 = 0.5;
        double DN3 = -0.06249999999999787;
        double DN5 = 0.002604166666634288;
        double DN7 = -5.425347203645548E-5;
        double DN9 = 6.781678893140472E-7;
        double DN11 = -5.650667533993771E-9;
        double DN13 = 3.311181792732023E-11;

        double x2 = x * x;
        double x4 = x2 * x2;
        double x8 = x4 * x4;

        double value_1 = DN1 + DN3 * x2;
        double value_5 = DN5 + DN7 * x2;
        double value_9 = DN9 + DN11 * x2;
        double value_13 = DN13;
        double value_1_5 = value_1 + x4 * value_5;
        double value_9_13 = value_9 + x4 * value_13;

        return (value_1_5 + x8 * value_9_13) * x;
    }

    private static double besselJ1_Over_1(double x) {
        double t = 1 / x;
        double T = T1(t);
        double phiPer2Pi = Phi1(t);

        double theta = x * INV_2_PI - 0.375 + phiPer2Pi;
        double cos = Trigonometry.cospi(theta * 2);

        //cosが不定になるのはxが大きいとき
        //→cosの値は関係ないので0としてよい
        cos = Double.isFinite(cos) ? cos : 0;

        double sqrtT = Exponentiation.sqrt(t);
        return SQRT_2_OVER_PI * sqrtT * T * cos;
    }

    private static double besselY1_Under_1(double x) {
        double DN1 = -0.15915494309189512;
        double DN3 = 0.049735919716209644;
        double DN5 = -0.0027631066508182816;
        double DN7 = 6.763854782496042E-5;
        double DN9 = -9.42621318477785E-7;
        double DN11 = 8.5134335307587E-9;
        double DN13 = -5.307389726609505E-11;

        double x2 = x * x;
        double x4 = x2 * x2;
        double x8 = x4 * x4;
        double logHalfX = Exponentiation.log(x) - LN_2;
        double factorJ1 = (EULER_MASCHERONI_GAMMA + logHalfX) * besselJ1_Under_1(x);

        //factorJ1が不定になるのはlogXが-infになる場合に限られる.
        //→このとき, 1/x=O(E+300)に対してfactorJ1=O(E-300)なので0としてよい
        factorJ1 = Double.isFinite(factorJ1) ? factorJ1 : 0;

        double j1Factor = TWO_OVER_PI * (factorJ1 - 1 / x);

        double value_1 = DN1 + x2 * DN3;
        double value_5 = DN5 + x2 * DN7;
        double value_9 = DN9 + x2 * DN11;
        double value_13 = DN13;

        double value_1_5 = value_1 + x4 * value_5;
        double value_9_13 = value_9 + x4 * value_13;

        double value = x * (value_1_5 + x8 * value_9_13);

        return value + j1Factor;
    }

    private static double besselY1_Over_1(double x) {
        double t = 1 / x;
        double T = T1(t);
        double phiPer2Pi = Phi1(t);

        double theta = x * INV_2_PI - 0.375 + phiPer2Pi;
        double sin = Trigonometry.sinpi(theta * 2);

        //sinが不定になるのはxが大きいとき
        //→sinの値は関係ないので0としてよい
        sin = Double.isFinite(sin) ? sin : 0;

        double sqrtT = Exponentiation.sqrt(t);
        return SQRT_2_OVER_PI * sqrtT * T * sin;
    }

    private static double T1(double t) {
        if (t < 0.25) {
            if (t < 0.125) { // 0<=t<0.125

                double DN0 = 1.231514828947805E-17;
                double DN1 = -3.8615264859576906E-14;
                double DN2 = 0.18750000002009987;
                double DN3 = 1.3245989817601063;
                double DN4 = 7.271162295265799;
                double DN5 = 16.151968356363795;
                double DN6 = 19.280963480982294;

                double DD0 = 1.0;
                double DD1 = 7.064527924607246;
                double DD2 = 39.81077991858104;
                double DD3 = 93.42927288244164;
                double DD4 = 139.5857226018441;
                double DD5 = 66.17404199928103;
                double DD6 = 11.066257900993266;

                double u = t;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5 + u2 * DN6;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5 + u2 * DD6;
                double den = den_0 + u4 * den_4;

                return 1 + nume / den;
            } else { // 0.125<=t<0.25
                double DN0 = 0.0028851812476075283;
                double DN1 = 0.06385280428235576;
                double DN2 = 0.5107142794357947;
                double DN3 = 1.8572721955927474;
                double DN4 = 3.341219859736856;
                double DN5 = 2.350513340934143;

                double DD0 = 1.0;
                double DD1 = 6.364820199243189;
                double DD2 = 17.142336784896614;
                double DD3 = 20.248823006524482;
                double DD4 = 9.567225751791108;
                double DD5 = 0.7662834173802467;

                double u = t - 0.125;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return 1 + nume / den;
            }
        } else {
            if (t < 0.5) { // 0.25<=t<0.5
                double DN0 = 0.011096271436639008;
                double DN1 = 0.13062425866125643;
                double DN2 = 0.560968796657756;
                double DN3 = 1.0681581102860442;
                double DN4 = 0.8551301880596787;
                double DN5 = 0.15460430349189547;

                double DD0 = 1.0;
                double DD1 = 4.152068886716222;
                double DD2 = 6.306881680978829;
                double DD3 = 3.8150989800649873;
                double DD4 = 0.8762688974813422;
                double DD5 = 0.02863117350639426;

                double u = t - 0.25;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return 1 + nume / den;
            } else { // 0.5<=t<=1
                double DN0 = 0.03967290935317425;
                double DN1 = 0.2670894715818194;
                double DN2 = 0.6847245669730885;
                double DN3 = 0.8238910229407653;
                double DN4 = 0.4524236961975215;
                double DN5 = 0.08415518297739755;
                double DN6 = 0.0019113170488403051;

                double DD0 = 1.0;
                double DD1 = 3.222501480063519;
                double DD2 = 3.8738697474563235;
                double DD3 = 2.0471268073462077;
                double DD4 = 0.45868284668826426;
                double DD5 = 0.02638065866398693;

                double u = t - 0.5;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5 + u2 * DN6;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return 1 + nume / den;
            }
        }
    }

    private static double Phi1(double t) {
        if (t < 0.25) {
            if (t < 0.125) { // 0<=t<0.125
                double DN0 = -1.553819168073285E-16;
                double DN1 = 0.05968310365981792;
                double DN2 = 0.44159813400639647;
                double DN3 = 1.8332277823488203;
                double DN4 = 3.632795429997278;
                double DN5 = 2.671003803289661;

                double DD0 = 1.0;
                double DD1 = 7.399047754990647;
                double DD2 = 31.15352597352586;
                double DD3 = 64.10518081474595;
                double DD4 = 57.39254619393123;
                double DD5 = 20.760808755843808;

                double u = t;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return nume / den;
            } else { // 0.125<=t<0.25
                double DN0 = 0.0074110387376415945;
                double DN1 = 0.0972502539056938;
                double DN2 = 0.3807531342933736;
                double DN3 = 0.6658230625101651;
                double DN4 = 0.3945198026800957;
                double DN5 = -0.003050376707285224;

                double DD0 = 1.0;
                double DD1 = 5.2256129673743414;
                double DD2 = 11.301730574417661;
                double DD3 = 9.39776121727083;
                double DD4 = 3.0623799020506355;

                double u = t - 0.125;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4;
                double den = den_0 + u4 * den_4;

                return nume / den;
            }
        } else {
            if (t < 0.5) { // 0.25<=t<0.5
                double DN0 = 0.014556406037394902;
                double DN1 = 0.09266084570560004;
                double DN2 = 0.13477314433754892;
                double DN3 = -0.08994227602147936;
                double DN4 = -0.3629430434798719;
                double DN5 = -0.20257107584798106;

                double DD0 = 1.0;
                double DD1 = 2.5469814467913316;
                double DD2 = 0.49754422295171263;
                double DD3 = -4.991640003807188;
                double DD4 = -4.934900280853978;
                double DD5 = -1.6264975297326167;

                double u = t - 0.25;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return nume / den;
            } else { // 0.5<=t<=1
                double DN0 = 0.027485348253600033;
                double DN1 = 0.14653696175398495;
                double DN2 = 0.2966458427621002;
                double DN3 = 0.28980010111539956;
                double DN4 = 0.13915472833328704;
                double DN5 = 0.02722262734042697;

                double DD0 = 1.0;
                double DD1 = 3.5974936437772245;
                double DD2 = 5.152997417399639;
                double DD3 = 3.730519188768239;
                double DD4 = 1.3908075330316163;
                double DD5 = 0.2177543072897465;

                double u = t - 0.5;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4 + u * DD5;
                double den = den_0 + u4 * den_4;

                return nume / den;
            }
        }
    }
}
