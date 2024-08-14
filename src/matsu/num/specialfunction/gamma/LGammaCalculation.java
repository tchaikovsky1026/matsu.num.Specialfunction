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
 * 対数ガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 19.3
 */
public final class LGammaCalculation {

    private static final double BOUNDARY_X_FOR_ASYMPTOTIC = 2.5;

    // = 0.5 * Math.log(2 * Math.PI)
    private static final double HALF_LN2PI = 0.9189385332046727;

    /**
     * 唯一のコンストラクタ.
     */
    public LGammaCalculation() {
        super();
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) を計算する.
     * 
     * @param x
     * @return lgamma(x)
     */
    public double lgamma(double x) {
        /*
         * x < 0: NaN
         * x = 0: +inf
         * x = +inf: +inf
         */

        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            return lgammaStirlRes_largeX(x) + lgammaStirling(x);
        }

        // ここに来た段階で2.5を下回っていてほしい
        // BOUNDARY_X_FOR_ASYMPTOTIC == 2.5 であるはず.
        assert x <= 2.5;

        switch ((int) (x * 2)) {
        case 0:
            return lgamma1p_smallX(x) - Exponentiation.log(x);
        case 1, 2:
            return lgamma1p_smallX(x - 1d);
        default:
            return lgamma2p_smallX(x - 2d);
        }
    }

    /**
     * log<sub>e</sub>&Gamma;(1 + <i>x</i>) を計算する.
     * 
     * @param x
     * @return lgamma1p(x)
     */
    public double lgamma1p(double x) {
        /*
         * x < -1: NaN x = -1: +inf x = +inf: +inf
         */

        // xが0付近でないならlgammaを呼び出してよい
        if (!(Math.abs(x) <= 0.5)) {
            return lgamma(1d + x);
        }

        return lgamma1p_smallX(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStirling近似: <br>
     * <i>S</i>(<i>x</i>) =
     * (<i>x</i> - 1/2) log<sub>e</sub>(<i>x</i>)
     * - <i>x</i>
     * + (1/2) log(2&pi;) <br>
     * の値を返す.
     *
     * @param x <i>x</i>, 引数
     * @return <i>S</i>(<i>x</i>)
     */
    public double lgammaStirling(double x) {
        return (x - 0.5) * (Exponentiation.log(x) - 1) - 0.5 + HALF_LN2PI;
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStirling近似残差:
     * log<sub>e</sub>&Gamma;(<i>x</i>) - <i>S</i>(<i>x</i>)
     * を計算する.
     * 
     * @param x <i>x</i>, 引数
     * @return log<sub>e</sub>&Gamma;(<i>x</i>) - <i>S</i>(<i>x</i>)
     */
    public double lgammaStirlingResidual(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            return lgammaStirlRes_largeX(x);
        }

        // ここに来た段階で2.5を下回っていてほしい
        // BOUNDARY_X_FOR_ASYMPTOTIC == 2.5 であるはず.
        assert x <= 2.5;

        switch ((int) (x * 2)) {
        case 0: {
            //x=-0dに対応するため, log(1/x)でなくlog(x)の形で計算させる
            double xp3 = x + 3;
            return lgammaStirlRes_largeX(xp3)
                    - (x + 0.5) * Exponentiation.log(x / xp3)
                    - Exponentiation.log(((x + 1) * (x + 2)) / (xp3 * xp3))
                    - 3;
        }
        case 1, 2: {
            double xp2 = x + 2;
            return lgammaStirlRes_largeX(xp2)
                    - (x + 0.5) * Exponentiation.log(x / xp2)
                    - Exponentiation.log((x + 1) / xp2)
                    - 2;
        }
        default: {
            double xp1 = x + 1;
            return lgammaStirlRes_largeX(xp1)
                    - (x + 0.5) * Exponentiation.log(x / xp1)
                    - 1;
        }
        }
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) の差分
     * [log<sub>e</sub>&Gamma;(<i>x</i> + <i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i>)]
     * を計算する.
     * 
     * @param x
     * @param y
     * @return lgamma(x+y)-lgamma(x)
     *
     */
    public double lgammaDiff(double x, double y) {
        /*
         * x < 0 or x + y < 0: NaN
         * x = +inf: NaN
         * x = 0 and x + y = 0: NaN
         */

        //y = NaNの場合は通過できるように条件式を設定
        if (y < 0) {
            return -lgammaDiff(x + y, -y);
        }

        if (!(x >= 0 && y >= 0)) {
            return Double.NaN;
        }

        if (x >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            // xが大きい場合は, スターリング近似の式変形を行い, 丸め誤差が出にくいようにする.
            double x_p_Y = x + y;
            return (x - 0.5) * Exponentiation.log1p(y / x)
                    + y * (Exponentiation.log(x_p_Y) - 1)
                    + lgammaStirlRes_largeX(x_p_Y) - lgammaStirlRes_largeX(x);
        } else {
            return lgamma(x + y) - lgamma(x);
        }

    }

    /**
     * ベータ関数の自然対数
     * 
     * [
     * log<sub>e</sub>B(<i>x</i>,<i>y</i>) =
     * log<sub>e</sub>&Gamma;(<i>x</i>)
     * + log<sub>e</sub>&Gamma;(<i>y</i>)
     * - log<sub>e</sub>&Gamma;(<i>x</i>+<i>y</i>)
     * ]
     * 
     * を計算する.
     * 
     * @param x
     * @param y
     * @return lbeta(x,y)
     */
    public double lbeta(double x, double y) {
        /*
         * x < 0 or y < 0: NaN
         * x = +inf or y = +inf: NaN
         * x = 0 and y > c: +inf
         * x = 0 and y = 0: +inf
         */

        //NaNの場合は通過できるように条件式を設定
        if (x < y) {
            return this.lbeta(y, x);
        }

        //x >= yの状態で, ここに入る
        if (!(Double.isFinite(x) && Double.isFinite(y))) {
            return Double.NaN;
        }

        //x = y = 0d
        if (x == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        //x >= y >= 2.5
        // x,yが十分大きい場合(+infを含む)のフォローのため, 
        // infになり得るすべての項が負になるように設計している
        if (y >= BOUNDARY_X_FOR_ASYMPTOTIC) {
            return HALF_LN2PI - (x - 0.5) * Exponentiation.log1p(y / x)
                    - y * Exponentiation.log(1 + x / y)
                    - 0.5 * Exponentiation.log(y)
                    + lgammaStirlRes_largeX(x)
                    + lgammaStirlRes_largeX(y)
                    - lgammaStirlRes_largeX(x + y);
        }

        //x > 0d ならば第2項は有限なので, y = 0dでも正しい値(+inf)を返せる. 
        return lgamma(y) - lgammaDiff(x, y);
    }

    /**
     * logΓ(x)-(x-0.5)log(x)+x-0.5*log(2π) を返す.
     * {@literal 2.5 <= x}の場合.
     */
    private double lgammaStirlRes_largeX(double x) {
        assert x >= BOUNDARY_X_FOR_ASYMPTOTIC;
        return x >= 10
                ? lgammaStirlRes_largeX_10_to_inf(x)
                : lgammaStirlRes_largeX_2_5_to_10(x);
    }

    /**
     * {@literal 2.5 <= x <= 10}の場合を扱う.
     */
    private double lgammaStirlRes_largeX_2_5_to_10(double x) {
        assert x >= 2.5;
        assert x <= 10;

        final double C0 = 0.08333333333294952;
        final double C1 = -4.439624178270013E-12;
        final double C2 = -0.00277777673585762;
        final double C3 = -3.523535901917528E-8;
        final double C4 = 7.94273954226552E-4;
        final double C5 = -6.982064923098387E-6;
        final double C6 = -5.419388640448249E-4;
        final double C7 = -2.8511534429890325E-4;
        final double C8 = 0.0019072501981359;
        final double C9 = -0.002656416054632048;
        final double C10 = 0.0018192173670392206;
        final double C11 = -5.324262511491241E-4;

        // スターリング近似の残差
        final double t = 1 / x;
        final double t2 = t * t;

        final double v0 = C0 + C1 * t + (C2 + C3 * t) * t2;
        final double v4 = C4 + C5 * t + (C6 + C7 * t) * t2;
        final double v8 = C8 + C9 * t + (C10 + C11 * t) * t2;

        final double t4 = t2 * t2;

        return t * (v0 + t4 * (v4 + t4 * v8));
    }

    /**
     * {@literal x >= 10}の場合を扱う.
     */
    private double lgammaStirlRes_largeX_10_to_inf(double x) {
        assert x >= 10;

        final double C0 = 0.08333333333333333;
        final double C1 = -0.0027777777777640935;
        final double C2 = 7.936507741043498E-4;
        final double C3 = -5.952290087063716E-4;
        final double C4 = 8.398978291417126E-4;
        final double C5 = -0.0017436283897677022;

        // スターリング近似の残差
        final double t = 1 / x;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        final double v4 = C4 + C5 * u;

        return t * (v0 + (u2 * u2) * v4);
    }

    /**
     * logΓ(2+x) を返す.
     * {@literal -0.5 <= x <= 0.5}の場合.
     */
    private double lgamma2p_smallX(double x) {
        assert x >= -0.5;
        assert x <= 0.5;

        return x >= 0
                ? lgamma2p_smallX_0_to_0_5(x)
                : lgamma2p_smallX_m0_5_to_0(x);
    }

    /**
     * {@literal -0.5 <= x <= 0}の場合.
     */
    private double lgamma2p_smallX_m0_5_to_0(double x) {
        assert x >= -0.5;
        assert x <= 0;

        final double C0 = 0.422784335098467;
        final double C1 = 0.3224670334240405;
        final double C2 = -0.0673523010606992;
        final double C3 = 0.020580808113500566;
        final double C4 = -0.007385557953811039;
        final double C5 = 0.002890419139216718;
        final double C6 = -0.0011935243149276515;
        final double C7 = 5.053265484057518E-4;
        final double C8 = -2.3977423887661155E-4;
        final double C9 = 5.641077194899485E-5;
        final double C10 = -1.1832891190122412E-4;
        final double C11 = -5.625806457289888E-5;
        final double C12 = -5.039747249560982E-5;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12;

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * {@literal 0 <= x <= 0.5}の場合.
     */
    private double lgamma2p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = 0.42278433509846713;
        final double C1 = 0.3224670334241082;
        final double C2 = -0.06735230105267337;
        final double C3 = 0.020580808405866215;
        final double C4 = -0.007385550548636189;
        final double C5 = 0.0028905040338157418;
        final double C6 = -0.0011927006140505167;
        final double C7 = 5.093654304627994E-4;
        final double C8 = -2.2195650455474533E-4;
        final double C9 = 9.616496612784415E-5;
        final double C10 = -3.863449062116486E-5;
        final double C11 = 1.2258656713889036E-5;
        final double C12 = -2.1529675102404024E-6;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12;

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * logΓ(1+x) を返す.
     * {@literal -0.5 <= x <= 0.5}の場合.
     */
    private double lgamma1p_smallX(double x) {
        assert x >= -0.5;
        assert x <= 0.5;

        return x >= 0
                ? lgamma1p_smallX_0_to_0_5(x)
                : (x >= -0.25
                        ? lgamma1p_smallX_m0_25_to_0(x)
                        : lgamma1p_smallX_m0_5_to_m0_25(x));
    }

    /**
     * {@literal -0.5 <= x <= -0.25}の場合.
     */
    private double lgamma1p_smallX_m0_5_to_m0_25(double x) {
        assert x >= -0.5;
        assert x <= -0.25;

        final double C0 = -0.5772096956018644;
        final double C1 = 0.8227365269233822;
        final double C2 = -0.3950261798449056;
        final double C3 = 0.34393604708099973;
        final double C4 = 0.44908790068364024;
        final double C5 = 4.467642801819388;
        final double C6 = 21.131963174903742;
        final double C7 = 81.24404834051674;
        final double C8 = 240.18547900866594;
        final double C9 = 553.5089590109171;
        final double C10 = 983.4559205488115;
        final double C11 = 1326.1510245464938;
        final double C12 = 1314.6301604257812;
        final double C13 = 906.7237858845696;
        final double C14 = 390.0502505280806;
        final double C15 = 79.51202685352627;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x + x2 * (C14 + C15 * x);

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * {@literal -0.25 <= x <= 0}の場合.
     */
    private double lgamma1p_smallX_m0_25_to_0(double x) {
        assert x >= -0.25;
        assert x <= 0;

        final double C0 = -0.5772156649015321;
        final double C1 = 0.8224670334244353;
        final double C2 = -0.40068563433607307;
        final double C3 = 0.2705808124728384;
        final double C4 = -0.20738535824000642;
        final double C5 = 0.16956313046569893;
        final double C6 = -0.1439242027431659;
        final double C7 = 0.1273872751503112;
        final double C8 = -0.09107280756619565;
        final double C9 = 0.2596358544001649;
        final double C10 = 0.8255373211043266;
        final double C11 = 3.880959944770197;
        final double C12 = 10.97248817275992;
        final double C13 = 21.49020564561275;
        final double C14 = 24.790998589600992;
        final double C15 = 13.178165491047393;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x + x2 * (C14 + C15 * x);

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * {@literal 0 <= x <= 0.5}の場合.
     */
    private double lgamma1p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = -0.5772156649015329;
        final double C1 = 0.8224670334240923;
        final double C2 = -0.400685634383037;
        final double C3 = 0.2705808081947211;
        final double C4 = -0.20738554282549457;
        final double C5 = 0.16955700165797613;
        final double C6 = -0.14404742454120273;
        final double C7 = 0.1254854247209554;
        final double C8 = -0.11116295873250238;
        final double C9 = 0.09920656109667852;
        final double C10 = -0.08746734190292502;
        final double C11 = 0.07305247759434627;
        final double C12 = -0.053744035827317765;
        final double C13 = 0.03132728728894874;
        final double C14 = -0.01237442224959489;
        final double C15 = 0.0024134442327868474;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x + x2 * (C14 + C15 * x);

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }
}
