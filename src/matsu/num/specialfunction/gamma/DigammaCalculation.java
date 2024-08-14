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

import matsu.num.commons.Exponentiation;

/**
 * ディガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 19.3
 */
public final class DigammaCalculation {

    private static final double BOUNDARY_X_FOR_ASYMPTOTIC = 2.5;

    /**
     * 唯一のコンストラクタ.
     */
    public DigammaCalculation() {
        super();
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

        if (x >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            //2.5以上は漸近展開を使う.
            return digammaStirRes_largeX(x) + digammaStirling(x);
        }

        assert x <= 2.5;
        switch ((int) (x * 2)) {
        case 0: {
            //-0dの場合でもうまくいく
            double shift = (2 * x + 1) / (x * x + x);
            return digamma2p_smallX(x) - shift;
        }
        case 1, 2: {
            double shift = 1d / x;
            return digamma2p_smallX(x - 1) - shift;
        }
        default: {
            return digamma2p_smallX(x - 2);
        }
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
     * {@literal -0.5 <= x <= 0.5} のときの, psi(2+x)
     */
    private double digamma2p_smallX(double x) {
        assert x >= -0.5;
        assert x <= 0.5;

        return x <= 0d
                ? digamma2p_smallX_m0_5_to_0(x)
                : digamma2p_smallX_0_to_0_5(x);
    }

    private double digamma2p_smallX_m0_5_to_0(double x) {
        assert x >= -0.5;
        assert x <= 0;

        final double C0 = 0.422784335098467;
        final double C1 = 0.6449340668482;
        final double C2 = -0.20205690316208472;
        final double C3 = 0.08232323359764801;
        final double C4 = -0.0369277581022438;
        final double C5 = 0.017343013406704928;
        final double C6 = -0.008349810725958855;
        final double C7 = 0.004073287492502214;
        final double C8 = -0.00203045815509519;
        final double C9 = 9.086254054588246E-4;
        final double C10 = -7.342042789061258E-4;
        final double C11 = -2.26561445164494E-4;
        final double C12 = -7.54258720738494E-4;
        final double C13 = -4.663864738641112E-4;
        final double C14 = -2.5532839632940173E-4;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * C14;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    private double digamma2p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = 0.42278433509846713;
        final double C1 = 0.6449340668482257;
        final double C2 = -0.20205690315950245;
        final double C3 = 0.08232323370617795;
        final double C4 = -0.03692775500310061;
        final double C5 = 0.017343059596509006;
        final double C6 = -0.008349250858481457;
        final double C7 = 0.004077154229343854;
        final double C8 = -0.0020073056970933995;
        final double C9 = 9.903655621325842E-4;
        final double C10 = -4.82375070815139E-4;
        final double C11 = 2.2208271372339898E-4;
        final double C12 = -8.766805413095233E-5;
        final double C13 = 2.4829945462735492E-5;
        final double C14 = -3.52466477457275E-6;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * C14;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    /**
     * {@literal x >= 2.5} のときの, psi(x)-log(x)+0.5/x
     */
    private double digammaStirRes_largeX(double x) {
        assert x >= BOUNDARY_X_FOR_ASYMPTOTIC;

        if (x >= 10d) {
            return digammaStirRes_10_to_inf(x);
        }

        return digammaStirRes_2_5_to_10(x);
    }

    private double digammaStirRes_2_5_to_10(double x) {
        assert x >= 2.5;
        assert x <= 10;

        final double C0 = -0.08333333336352612;
        final double C1 = 2.0775833112262124E-9;
        final double C2 = 0.008333268275975752;
        final double C3 = 1.2284561305540452E-6;
        final double C4 = -0.003983870462138023;
        final double C5 = 1.411607025008755E-4;
        final double C6 = 0.003234260060141176;
        final double C7 = 0.004537229855589289;
        final double C8 = -0.023642382389413342;
        final double C9 = 0.039485497710652254;
        final double C10 = -0.03703688927580265;
        final double C11 = 0.019709131755747913;
        final double C12 = -0.004684811193911103;

        final double t = 1 / x;
        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        final double v12 = C12;

        final double t4 = t2 * t2;

        return t2 * (v0 + t4 * (v4 + t4 * (v8 + t4 * v12)));
    }

    private double digammaStirRes_10_to_inf(double x) {
        assert x >= 10;

        final double C0 = -0.08333333333333333;
        final double C1 = 0.008333333333159249;
        final double C2 = -0.003968253719301066;
        final double C3 = 0.0041665507604469595;
        final double C4 = -0.007552066489340584;
        final double C5 = 0.018859762071985555;

        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5;

        return u * (v0 + u2 * u2 * v4);
    }
}
