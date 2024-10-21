/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.21
 */
package matsu.num.specialfunction.gamma;

/**
 * トリガンマ関数の計算に関する.
 * 
 * @author Matsuura Y.
 * @version 19.9
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

        final double C0 = 0.64493406684822651104967010970811;
        final double C1 = -0.40411380631912271418959357063615;
        final double C2 = 0.24696970114306194998752506172756;
        final double C3 = -0.14771102001613406598891392933619;
        final double C4 = 0.086715326761097685707464862899903;
        final double C5 = -0.050095358553800706697328769453049;
        final double C6 = 0.028545105889374797410518050398704;
        final double C7 = -0.016037994334823109099434062553931;
        final double C8 = 0.0091164038757708646443070127790051;
        final double C9 = -0.0042749005983106522362559012320706;
        final double C10 = 0.0046246226487582881118117515989846;
        final double C11 = 0.0023949561288969974330981325494429;
        final double C12 = 0.0060638024753262051415027883435428;
        final double C13 = 0.0040353096197877509520040248288895;
        final double C14 = 0.0021421047505773921827205599841685;

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

        final double C0 = 0.64493406684822641964204759390171;
        final double C1 = -0.40411380631917513679452956773338;
        final double C2 = 0.24696970113163597665685842768391;
        final double C3 = -0.14771102048065008050420595447648;
        final double C4 = 0.086715307390055442195211989807983;
        final double C5 = -0.050095622814358210277848299921941;
        final double C6 = 0.028541051627172723209021639348882;
        final double C7 = -0.016063931533460383498448986485663;
        final double C8 = 0.0089347751567952946536078595620645;
        final double C9 = -0.0048821023799505355846726349530710;
        final double C10 = 0.0025507170777179991371367293289190;
        final double C11 = -0.0011812651074429264503981179670466;
        final double C12 = 0.00041344723607864334275955177480626;
        final double C13 = -0.000077580582090204599969896044264154;

        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * (v8 + x4 * v12));
    }

    /**
     * トリガンマ関数のスターリング近似: 1/x + 1/(2x^2) <br>
     * xが大きいところで使われることを想定している.
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

        final double C0 = 0.16666666674826500853356013890168;
        final double C1 = -6.0997222683791823645035909883980E-9;
        final double C2 = -0.033333125098900547963590433930119;
        final double C3 = -0.0000042982952631998065468571496287518;
        final double C4 = 0.023869335281809509353691843551882;
        final double C5 = -0.00059123364659808639311559296742206;
        final double C6 = -0.029084470241385328901758321184622;
        final double C7 = -0.022195309355305816474885537031741;
        final double C8 = 0.15737663231080196098331601135535;
        final double C9 = -0.18878192400216195141980490218191;
        final double C10 = -0.10849442258713146574037493990543;
        final double C11 = 0.62220884830129217461283627434155;
        final double C12 = -0.86773718466079703956053487205145;
        final double C13 = 0.59492715438202393224915464299069;
        final double C14 = -0.17193797437085157997059855210164;

        final double t = 1 / x;
        final double t2 = t * t;

        final double v0 = C0 + t * C1 + t2 * (C2 + t * C3);
        final double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        final double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        final double v12 = C12 + t * C13 + t2 * C14;

        final double t4 = t2 * t2;

        return t * t2 * (v0 + t4 * (v4 + t4 * (v8 + t4 * v12)));
    }

    private double trigammaStirRes_10_to_inf(double x) {
        assert x >= 10;

        final double C0 = 0.16666666666666493092326657333609;
        final double C1 = -0.033333333325104953687087552780501;
        final double C2 = 0.023809516678977828609093963539043;
        final double C3 = -0.033330846054822525742457989572643;
        final double C4 = 0.075339498931015221623640452399515;
        final double C5 = -0.21884760962068618585607590239397;

        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5;

        return t * u * (v0 + u2 * u2 * v4);
    }
}
