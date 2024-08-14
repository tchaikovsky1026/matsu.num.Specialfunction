/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.14
 */
package matsu.num.specialfunction.gamma;

/**
 * トリガンマ関数の計算に関する.
 * 
 * @author Matsuura Y.
 * @version 19.3
 */
public final class TrigammaCalculation {

    private static final double BOUNDARY_X_FOR_ASYMPTOTIC = 2.5;

    /**
     * 唯一のコンストラクタ.
     */
    public TrigammaCalculation() {
        super();
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

        if (x >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            //2.5以上は漸近展開を使う.
            return trigammaStirRes_largeX(x) + trigammaStirling(x);
        }

        assert x <= 2.5;
        switch ((int) (x * 2)) {
        case 0: {
            // x = -0d でもうまくいく
            double xp1 = x + 1;
            double shift = 1d / (x * x) + 1 / (xp1 * xp1);
            return trigamma2p_smallX(x) + shift;
        }
        case 1, 2: {
            double shift = 1d / (x * x);
            return trigamma2p_smallX(x - 1) + shift;
        }
        default: {
            return trigamma2p_smallX(x - 2);
        }
        }
    }

    /**
     * {@literal -0.5 <= x <= 0.5} のときの, psi1(2+x)
     */
    private double trigamma2p_smallX(double x) {
        assert x >= -0.5;
        assert x <= 0.5;

        return x <= 0d
                ? trigamma2p_smallX_m0_5_to_0(x)
                : trigamma2p_smallX_0_to_0_5(x);
    }

    private double trigamma2p_smallX_m0_5_to_0(double x) {
        assert x >= -0.5;
        assert x <= 0;

        final double C0 = 0.6449340668482265;
        final double C1 = -0.40411380631910854;
        final double C2 = 0.24696970114416059;
        final double C3 = -0.14771101997597313;
        final double C4 = 0.0867153276089871;
        final double C5 = -0.050095347101076396;
        final double C6 = 0.028545210671990548;
        final double C7 = -0.01603732327121424;
        final double C8 = 0.009119464608960324;
        final double C9 = -0.0042649119475593605;
        final double C10 = 0.004647759044464623;
        final double C11 = 0.0024320723684013107;
        final double C12 = 0.006102992397523436;
        final double C13 = 0.004059796713804028;
        final double C14 = 0.0021489619475108107;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * C14;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    private double trigamma2p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = 0.6449340668482264;
        final double C1 = -0.4041138063191871;
        final double C2 = 0.2469697011331987;
        final double C3 = -0.1477110205610824;
        final double C4 = 0.08671530954865755;
        final double C5 = -0.050095657493126146;
        final double C6 = 0.028541412336164772;
        final double C7 = -0.016066476701837598;
        final double C8 = 0.008947283413123158;
        final double C9 = -0.004925384152746134;
        final double C10 = 0.002655761087858228;
        final double C11 = -0.0013562742139614993;
        final double C12 = 6.040638609517639E-4;
        final double C13 = -1.9980408675742252E-4;
        final double C14 = 3.49811174070382E-5;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * C14;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    /**
     * トリガンマ関数のスターリング近似: 1/x + 1/(2x^2)
     *
     * @param x
     * @return trigammaStirling(x)
     */
    private double trigammaStirling(double x) {
        final double invX = 1 / x;
        return invX + 0.5 * invX * invX;
    }

    /**
     * {@literal x >= 2.5} のときの, psi1(x) - 1/x - 1/(2x^2)
     */
    private double trigammaStirRes_largeX(double x) {
        assert x >= BOUNDARY_X_FOR_ASYMPTOTIC;

        if (x >= 10d) {
            return trigammaStirRes_10_to_inf(x);
        }

        return trigammaStirRes_2_5_to_10(x);
    }

    private double trigammaStirRes_2_5_to_10(double x) {
        assert x >= 2.5;
        assert x <= 10;

        final double C0 = 0.16666666688718998;
        final double C1 = -1.5841221907147984E-8;
        final double C2 = -0.03333281392620971;
        final double C3 = -1.0300525014952065E-5;
        final double C4 = 0.02394745652958498;
        final double C5 = -0.00131719977449279;
        final double C6 = -0.02411544355494644;
        final double C7 = -0.04765507912720703;
        final double C8 = 0.25554800145631473;
        final double C9 = -0.47242245678357;
        final double C10 = 0.4962883749665182;
        final double C11 = -0.30112072399212125;
        final double C12 = 0.08695334800183606;
        final double C13 = -0.0038140894039424447;

        final double t = 1 / x;
        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        final double v12 = C12 + t * C13;

        final double t4 = t2 * t2;

        return t * t2 * (v0 + t4 * (v4 + t4 * (v8 + t4 * v12)));
    }

    private double trigammaStirRes_10_to_inf(double x) {
        assert x >= 10;

        final double C0 = 0.16666666666666666;
        final double C1 = -0.03333333333328928;
        final double C2 = 0.02380952372259268;
        final double C3 = -0.033333276166239975;
        final double C4 = 0.07574022760714248;
        final double C5 = -0.2504265826880604;
        final double C6 = 0.9566446635927073;

        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * C6;

        return t * u * (v0 + u2 * u2 * v4);
    }
}
