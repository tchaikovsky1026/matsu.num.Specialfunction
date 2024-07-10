/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.8
 */
package matsu.num.specialfunction.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.commons.Trigonometry;
import matsu.num.specialfunction.BesselFunction;
import matsu.num.specialfunction.GammaFunction;

/**
 * 0次のBessel関数.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
final class Bessel0thOrder extends SkeletalBessel implements BesselFunction {

    private static final double EULER_MASCHERONI_GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;
    private static final double INV_2_PI = 1.0 / (2 * Math.PI);
    private static final double TWO_OVER_PI = 2.0 / Math.PI;
    private static final double SQRT_2_OVER_PI = Math.sqrt(2.0 / Math.PI);
    private static final double LN_2 = Math.log(2);

    Bessel0thOrder() {
        super();
    }

    @Override
    public int order() {
        return 0;
    }

    @Override
    public double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < 1) {
            return besselJ_Under_1(x);
        } else {
            return besselJ_Over_1(x);
        }
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < 1) {
            return besselY_Under_1(x);
        } else {
            return besselY_Over_1(x);
        }
    }

    private static double besselJ_Under_1(double x) {
        double DN0 = 1.0;
        double DN2 = -0.25000000000000183;
        double DN4 = 0.015625000000030337;
        double DN6 = -4.3402777796842327E-4;
        double DN8 = 6.781684601356977E-6;
        double DN10 = -6.781771878466556E-8;
        double DN12 = 4.716028514802942E-10;
        double DN14 = -2.578990051114032E-12;

        double x2 = x * x;
        double x4 = x2 * x2;
        double x8 = x4 * x4;

        double value_0 = DN0 + DN2 * x2;
        double value_4 = DN4 + DN6 * x2;
        double value_8 = DN8 + DN10 * x2;
        double value_12 = DN12 + DN14 * x2;
        double value_0_4 = value_0 + x4 * value_4;
        double value_8_12 = value_8 + x4 * value_12;

        return value_0_4 + x8 * value_8_12;
    }

    private static double besselJ_Over_1(double x) {
        double t = 1 / x;
        double T = T0(t);
        double phiPer2Pi = Phi0(t);

        double theta = x * INV_2_PI - 0.125 + phiPer2Pi;
        double cos = Trigonometry.cospi(theta * 2);

        //cosが不定になるのはxが大きいとき
        //→cosの値は関係ないので0としてよい
        cos = Double.isFinite(cos) ? cos : 0;

        double sqrtT = Exponentiation.sqrt(t);
        return SQRT_2_OVER_PI * sqrtT * T * cos;
    }

    private static double besselY_Under_1(double x) {

        double DN2 = 0.15915494309189487;
        double DN4 = -0.014920775914855204;
        double DN6 = 5.065695525914719E-4;
        double DN8 = -8.994487545604707E-6;
        double DN10 = 9.857915315611361E-8;
        double DN12 = -7.341687897124155E-10;
        double DN14 = 3.815957871942435E-12;

        double x2 = x * x;
        double x4 = x2 * x2;
        double x8 = x4 * x4;
        double logHalfX = Exponentiation.log(x) - LN_2;
        double j0Factor = TWO_OVER_PI * (EULER_MASCHERONI_GAMMA + logHalfX) * besselJ_Under_1(x);

        double value_2 = DN2 + x2 * DN4;
        double value_6 = DN6 + x2 * DN8;
        double value_10 = DN10 + x2 * DN12;
        double value_14 = DN14;

        double value_2_6 = value_2 + x4 * value_6;
        double value_10_14 = value_10 + x4 * value_14;

        double value = x2 * (value_2_6 + x8 * value_10_14);

        return value + j0Factor;
    }

    private static double besselY_Over_1(double x) {
        double t = 1 / x;
        double T = T0(t);
        double phiPer2Pi = Phi0(t);
        double theta = x * INV_2_PI - 0.125 + phiPer2Pi;
        double sin = Trigonometry.sinpi(theta * 2);

        //sinが不定になるのはxが大きいとき
        //→sinの値は関係ないので0としてよい
        sin = Double.isFinite(sin) ? sin : 0;

        double sqrtT = Exponentiation.sqrt(t);
        return SQRT_2_OVER_PI * sqrtT * T * sin;
    }

    private static double T0(double t) {
        if (t < 0.25) {
            if (t < 0.125) { // 0<=t<0.125
                double DN0 = -8.909279578510242E-18;
                double DN1 = 2.7967682194742047E-14;
                double DN2 = -0.06250000001463403;
                double DN3 = -0.4619042117301315;
                double DN4 = -2.660599491800881;
                double DN5 = -6.3708673420387685;
                double DN6 = -8.209427065700893;

                double DD0 = 1.0;
                double DD1 = 7.390467435699518;
                double DD2 = 44.225836751246554;
                double DD3 = 114.17466484544445;
                double DD4 = 195.90094258478507;
                double DD5 = 125.28305371825144;
                double DD6 = 26.814790446678053;

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
                double DN0 = -9.530839551793967E-4;
                double DN1 = -0.019305745483929413;
                double DN2 = -0.1321036312208104;
                double DN3 = -0.35995108608956317;
                double DN4 = -0.4141384244245471;
                double DN5 = -0.06712544775869904;

                double DD0 = 1.0;
                double DD1 = 4.623876053914274;
                double DD2 = 9.308797069153215;
                double DD3 = 7.318590812197892;
                double DD4 = 2.2693254508192022;

                double u = t - 0.125;
                double u2 = u * u;
                double u4 = u2 * u2;

                double nume_0 = DN0 + u * DN1 + u2 * (DN2 + u * DN3);
                double nume_4 = DN4 + u * DN5;
                double nume = nume_0 + u4 * nume_4;

                double den_0 = DD0 + u * DD1 + u2 * (DD2 + u * DD3);
                double den_4 = DD4;
                double den = den_0 + u4 * den_4;

                return 1 + nume / den;
            }
        } else {
            if (t < 0.5) { // 0.25<=t<0.5
                double DN0 = -0.0035877982660793965;
                double DN1 = -0.04343374429298641;
                double DN2 = -0.1936610381744293;
                double DN3 = -0.38691512930837746;
                double DN4 = -0.3261027029190887;
                double DN5 = -0.06283058134648359;

                double DD0 = 1.0;
                double DD1 = 4.683933849454917;
                double DD2 = 8.228559602577628;
                double DD3 = 6.159385994721739;
                double DD4 = 1.7462984201885594;
                double DD5 = 0.12005137412877204;

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
                double DN0 = -0.01216834603080112;
                double DN1 = -0.08596457427731326;
                double DN2 = -0.23084545425464092;
                double DN3 = -0.28679997732519813;
                double DN4 = -0.1544445674451757;
                double DN5 = -0.022485077356303604;
                double DN6 = -1.8494138897624584E-4;

                double DD0 = 1.0;
                double DD1 = 3.765242037342655;
                double DD2 = 5.152731211857939;
                double DD3 = 3.0412411744911814;
                double DD4 = 0.7012860963782352;
                double DD5 = 0.04566705302895489;

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

    private static double Phi0(double t) {
        if (t < 0.25) {
            if (t < 0.125) { // 0<=t<0.125
                double DN0 = 5.429300719220337E-17;
                double DN1 = -0.019894367886617508;
                double DN2 = -0.12764687267583288;
                double DN3 = -0.6215945353552048;
                double DN4 = -1.2087434909779304;
                double DN5 = -1.2716091407380936;

                double DD0 = 1.0;
                double DD1 = 6.416231641826688;
                double DD2 = 31.765582146608708;
                double DD3 = 64.09989377820744;
                double DD4 = 78.78456834982848;
                double DD5 = 22.672714871867218;

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
                double DN0 = -0.0024674730274882417;
                double DN1 = -0.03645328508626195;
                double DN2 = -0.17334682081387323;
                double DN3 = -0.35272324838963043;
                double DN4 = -0.286474916309609;
                double DN5 = -0.012516367401732204;

                double DD0 = 1.0;
                double DD1 = 6.893459409316663;
                double DD2 = 17.29280627308048;
                double DD3 = 18.75518705356561;
                double DD4 = 6.046023520328592;

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
                double DN0 = -0.004835005366298126;
                double DN1 = -0.05356510108420975;
                double DN2 = -0.2144514560752365;
                double DN3 = -0.3770487910145377;
                double DN4 = -0.2794032475863411;
                double DN5 = -0.04806976220082437;

                double DD0 = 1.0;
                double DD1 = 7.278954676663231;
                double DD2 = 17.718304648722352;
                double DD3 = 18.545813405557347;
                double DD4 = 7.32731430178768;
                double DD5 = 0.701192819109254;

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
                double DN0 = -0.009104394765701023;
                double DN1 = -0.04360355556792304;
                double DN2 = -0.07382533040627408;
                double DN3 = -0.05267171010092439;
                double DN4 = -0.01360932591324551;
                double DN5 = -8.225018041278017E-4;

                double DD0 = 1.0;
                double DD1 = 3.0559842472818177;
                double DD2 = 3.366077859765347;
                double DD3 = 1.5382549806006134;
                double DD4 = 0.2618214483030532;
                double DD5 = 0.010734079897320973;

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
