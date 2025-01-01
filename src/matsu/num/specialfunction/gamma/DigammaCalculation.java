/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.gamma;

import matsu.num.specialfunction.common.Exponentiation;

/**
 * ディガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 22.0
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
     * ディガンマ関数のスターリング近似: log(x) - 0.5/x <br>
     * xが大きいところで使われることを想定している.
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

        final double C0 = 0.42278433509846725176466601956037;
        final double C1 = 0.64493406684831285805230636931359;
        final double C2 = -0.20205690314857628862697509632726;
        final double C3 = 0.082323234263926317272949305406734;
        final double C4 = -0.036927740693301855191786505337812;
        final double C5 = 0.017343287725791731109147016442645;
        final double C6 = -0.0083470003673334697306160150196736;
        final double C7 = 0.0040928714656735521085199843196805;
        final double C8 = -0.0019352272417065691044363554922308;
        final double C9 = 0.0012351326147030845763396060771198;
        final double C10 = 0.000051830302677283607839954738312914;
        final double C11 = 0.0010736027980891113815725404659602;
        final double C12 = 0.00065270950615515981511417126178979;
        final double C13 = 0.00043049198618300994190812723786660;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    private double digamma2p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = 0.42278433509846718451366751555831;
        final double C1 = 0.64493406684819537987214422501740;
        final double C2 = -0.20205690315605135586628098106786;
        final double C3 = 0.082323233552180075304867578599474;
        final double C4 = -0.036927751431210513146027297136319;
        final double C5 = 0.017343010243721809369096907110135;
        final double C6 = -0.0083488124181442892830491258653060;
        final double C7 = 0.0040745371353365149860776732379453;
        final double C8 = -0.0019965665697111953964464062124999;
        final double C9 = 0.00095989306899847488578470619556097;
        final double C10 = -0.00042326021592700025980172168457409;
        final double C11 = 0.00014629173891239196139596387264892;
        final double C12 = -0.000027892856185485909527913609165947;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12;

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

        final double C0 = -0.083333333362789693619359724787445;
        final double C1 = 2.0333422396206845007583760284216E-9;
        final double C2 = 0.0083332694664549124052496916152602;
        final double C3 = 0.0000012094829594364371259337429208697;
        final double C4 = -0.0039836709735012411326276597752342;
        final double C5 = 0.00013970252388276589735041200809467;
        final double C6 = 0.0032418615470544505442801248118861;
        final double C7 = 0.0045087380811329871322930074528790;
        final double C8 = -0.023566120269555429456330248016876;
        final double C9 = 0.039343220478413773829349292947320;
        final double C10 = -0.036861116321993889958836110543141;
        final double C11 = 0.019579898972896443905113076451563;
        final double C12 = -0.0046420088179057340940501537665839;

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

        final double C0 = -0.083333333333319123049483301465388;
        final double C1 = 0.0083333332933865180650915130661907;
        final double C2 = -0.0039682304025291625422981571615222;
        final double C3 = 0.0041612256771827843741058487130821;
        final double C4 = -0.0070259191174259050567228404650920;

        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4;

        return u * (v0 + u2 * u2 * v4);
    }
}
