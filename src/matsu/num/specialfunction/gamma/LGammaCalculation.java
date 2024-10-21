/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.17
 */
package matsu.num.specialfunction.gamma;

import matsu.num.commons.Exponentiation;

/**
 * 対数ガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 19.9
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

        final double C0 = 0.083333333332859401917051526339785;
        final double C1 = 4.2478407811314907805045937799609E-13;
        final double C2 = -0.0027777768519141529159510372577765;
        final double C3 = -3.3616759043439759404539004835143E-8;
        final double C4 = 0.00079425926433896749532787307306112;
        final double C5 = -0.0000068908051837913705466212511329748;
        final double C6 = -0.00054233551859355212684104851084454;
        final double C7 = -0.00028390735937082315925091996842740;
        final double C8 = 0.0019047207456154728962799683387465;
        final double C9 = -0.0026529436592973311299786038124257;
        final double C10 = 0.0018164018019082777245648999219508;
        final double C11 = -0.00053140375184120560626980294568572;

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

        final double C0 = 0.083333333333333333333333333333333;
        final double C1 = -0.0027777777777735458172043084684524;
        final double C2 = 0.00079365078173452591335121543453518;
        final double C3 = -0.00059523105516527764840866895686502;
        final double C4 = 0.00084012197743836144149017178807405;
        final double C5 = -0.0017522750493940258974989358352511;

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

        final double C0 = 0.42278433509846714593245421885653;
        final double C1 = 0.32246703342411825295180865166182;
        final double C2 = -0.067352301052555466470349909697015;
        final double C3 = 0.020580808460065008241493844369675;
        final double C4 = -0.0073855501837695424541224522837008;
        final double C5 = 0.0028905235484419084541376402232713;
        final double C6 = -0.0011926203780987257545591657059652;
        final double C7 = 0.00051058104840478641895733188006454;
        final double C8 = -0.00021884702006650050653702303165604;
        final double C9 = 0.00011365847967714448926913517975974;
        final double C10 = -0.000012580854454467030834683447294545;
        final double C11 = 0.000069765896693095310052215748591934;
        final double C12 = 0.000037068700180942490350629872846327;
        final double C13 = 0.000026855531869052593743569841138654;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x;

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * {@literal 0 <= x <= 0.5}の場合.
     */
    private double lgamma2p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = 0.42278433509846720857969565962271;
        final double C1 = 0.32246703342407263981643490653940;
        final double C2 = -0.067352301049257396413351793602564;
        final double C3 = 0.020580808277724985876007434173793;
        final double C4 = -0.0073855480699163221675513539932788;
        final double C5 = 0.0028904757838066482169756798893877;
        final double C6 = -0.0011924967137207273133148939912563;
        final double C7 = 0.00050839732442854579122509556631816;
        final double C8 = -0.00021889168921960923536697800747101;
        final double C9 = 0.000089758419300445824644829725391721;
        final double C10 = -0.000030143433932051894677962603688863;
        final double C11 = 0.0000057944262732714841156959909566822;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);

        final double x4 = x2 * x2;

        return x * (v0 + x4 * v4 + x4 * x4 * v8);
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

        final double C0 = -0.57720738757530964882323104621249;
        final double C1 = 0.82283289888746679073513082199587;
        final double C2 = -0.39315569629714936674455187530043;
        final double C3 = 0.36632183203341967320193258311880;
        final double C4 = 0.63383899088881902158454033949707;
        final double C5 = 5.5814428580419426815658178664591;
        final double C6 = 26.199136236543142469104467574086;
        final double C7 = 98.959052130961040994198891142738;
        final double C8 = 288.17026806163663553733094506766;
        final double C9 = 654.21687734204621848426478406452;
        final double C10 = 1145.8885130011695335030111449245;
        final double C11 = 1523.8821265610719525348718420130;
        final double C12 = 1490.4862695020048399012447684744;
        final double C13 = 1014.6015657049276127042015241636;
        final double C14 = 430.86693845525063770956498722401;
        final double C15 = 86.692817453055493488825746504165;

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

        final double C0 = -0.57721566490153284754338955014046;
        final double C1 = 0.82246703342413333414368961155219;
        final double C2 = -0.40068563438139629754320273792022;
        final double C3 = 0.27058080894367531777438089057598;
        final double C4 = -0.20738552402318733461739751713360;
        final double C5 = 0.16955802193831389431450712262442;
        final double C6 = -0.14403282468626270686761834199162;
        final double C7 = 0.12574274073122668322736918825970;
        final double C8 = -0.10913137163442662281767223424504;
        final double C9 = 0.11462339019358941909076632100407;
        final double C10 = -0.024793647903413169396485733418083;
        final double C11 = 0.28485755690278760931203372584948;
        final double C12 = 0.30354990517722884290118235202297;
        final double C13 = 0.43921565035563455943240043836102;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x;

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }

    /**
     * {@literal 0 <= x <= 0.5}の場合.
     */
    private double lgamma1p_smallX_0_to_0_5(double x) {
        assert x >= 0;
        assert x <= 0.5;

        final double C0 = -0.57721566490153284681945704389001;
        final double C1 = 0.82246703342409872435558523540982;
        final double C2 = -0.40068563438400092501420336909393;
        final double C3 = 0.27058080825294486191648054276253;
        final double C4 = -0.20738554467299150590068927325056;
        final double C5 = 0.16955703689380779505299202449047;
        final double C6 = -0.14404786119784920933300346765184;
        final double C7 = 0.12548911518640802402899816851138;
        final double C8 = -0.11118487400496793589161038985695;
        final double C9 = 0.099299478535936389713731807540738;
        final double C10 = -0.087749760790492941056055621351512;
        final double C11 = 0.073662625749349564282386669661538;
        final double C12 = -0.054658001466559909753576966974546;
        final double C13 = 0.032229034711636763110197092810958;
        final double C14 = -0.012901117393495117223433082402710;
        final double C15 = 0.0025513231590244702306981595528527;

        final double x2 = x * x;
        final double v0 = C0 + C1 * x + x2 * (C2 + C3 * x);
        final double v4 = C4 + C5 * x + x2 * (C6 + C7 * x);
        final double v8 = C8 + C9 * x + x2 * (C10 + C11 * x);
        final double v12 = C12 + C13 * x + x2 * (C14 + C15 * x);

        final double x4 = x2 * x2;

        return x * (v0 + x4 * (v4 + x4 * (v8 + x4 * v12)));
    }
}
