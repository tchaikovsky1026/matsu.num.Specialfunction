/**
 * 2023.3.20
 */
package matsu.num.specialfunction.gamma;

import java.util.Objects;

import matsu.num.commons.Exponentiation;

/**
 * 対数ガンマ関数の計算.
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
public final class LGammaCalculation {

    private static final LGammaCalculation INSTANCE = new LGammaCalculation();

    private static final double HALF_LN2PI = 0.5 * Math.log(2 * Math.PI);

    private LGammaCalculation() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError();
        }
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) を計算する.
     * 
     * @param x
     * @return lgamma(x)
     */
    public double lgamma(double x) {
        /*
         * x < 0: NaN x = 0: +inf x = +inf: +inf
         */

        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x >= 6) {
            // スターリング近似ベースの式を使う.
            double shiftX = x;
            double shiftValue = 1;
            if (x < 10) {
                shiftX += 4;
                shiftValue = (x * (x + 1)) * ((x + 2) * (x + 3));
            }
            return lgammaStirlingResidual_largekernel(shiftX) + lgammaStirling(shiftX) - Exponentiation.log(shiftValue);
        }

        // x = 2における級数展開を使う
        double shiftX = x;
        double shiftValue;
        if (x >= 2) {
            double shiftValue_exp = 1;

            while (shiftX > 2.5) {
                shiftX -= 1;
                shiftValue_exp *= shiftX;
            }
            shiftValue = Exponentiation.log(shiftValue_exp);
        } else {
            double shiftValue_exp = 1;

            while (shiftX < 1.5) {
                shiftValue_exp *= shiftX;
                shiftX += 1;
            }
            shiftValue = -Exponentiation.log(shiftValue_exp);
        }
        return lgamma2p_smallkernel(shiftX - 2) + shiftValue;

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
            return lgamma(1 + x);
        }
        return lgamma2p_smallkernel(x) - Exponentiation.log1p(x);
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) のStiring近似残差
     * [log<sub>e</sub>&Gamma;(<i>x</i>) - [(<i>x</i> -
     * 1/2)log<sub>e</sub>(<i>x</i>) - <i>x</i> + (log(2&pi;))/2]] を計算する.
     * 
     * @param x
     * @return f(x)
     */
    public double lgammaStirlingResidual(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        final int intX = (int) x;
        if (intX >= 10) {
            return lgammaStirlingResidual_largekernel(x);
        }

        int n = 1 + (((13 - intX) >> 2) << 2); // 4*(int)[((10-intX)+3)/4]
        double xpN = x + n;
        double invXpN = 1 / xpN;
        double invXpN2 = invXpN * invXpN;
        double invXpN4 = invXpN2 * invXpN2;

        double base = lgammaStirlingResidual_largekernel(xpN) - n - (x + 0.5) * Exponentiation.log(x * invXpN);
        double shift_exp = 1;
        switch (n) {
        case 13:
            shift_exp *= (((x + 9) * (x + 10)) * ((x + 11) * (x + 12))) * invXpN4;
            // fall-through
        case 9:
            shift_exp *= (((x + 5) * (x + 6)) * ((x + 7) * (x + 8))) * invXpN4;
            // fall-through
        case 5:
            shift_exp *= ((x + 1) * (x + 2)) * ((x + 3) * (x + 4)) * invXpN4;
            return base - Exponentiation.log(shift_exp);
        default:
            throw new AssertionError(String.format("バグ:到達不能:n=%d", n));
        }
    }

    /**
     * log<sub>e</sub>&Gamma;(<i>x</i>) の差分 [log<sub>e</sub>&Gamma;(<i>x</i> +
     * <i>y</i>) - log<sub>e</sub>&Gamma;(<i>x</i>)] を計算する.
     * 
     * @param x
     * @param y
     * @return lgamma(x+y)-lgamma(x)
     *
     */
    public double lgammaDiff(double x, double y) {
        /*
         * x < 0 or x + y < 0: NaN x = +inf: NaN x = 0 and x + y = 0: NaN
         */

        if (y < 0) {
            return -lgammaDiff(x + y, -y);
        }

        if (!(x >= 0 && y >= 0)) {
            return Double.NaN;
        }

        if (x >= 10) {
            // xが大きい場合は, スターリング近似の式変形を行い, 丸め誤差が出にくいようにする.
            double x_p_Y = x + y;
            return (x - 0.5) * Exponentiation.log1p(y / x) + y * Exponentiation.log(x_p_Y) - y
                    + lgammaStirlingResidual_largekernel(x_p_Y) - lgammaStirlingResidual_largekernel(x);
        } else {
            return lgamma(x + y) - lgamma(x);
        }

    }

    /**
     * ベータ関数の自然対数 [log<sub>e</sub>B(<i>x</i>,<i>y</i>) =
     * log<sub>e</sub>&Gamma;(<i>x</i>) + log<sub>e</sub>&Gamma;(<i>y</i>) -
     * log<sub>e</sub>&Gamma;(<i>x</i>+<i>y</i>)] を計算する.
     * 
     * @param x
     * @param y
     * @return lbeta(x,y)
     *
     */
    public double lbeta(double x, double y) {
        /*
         * x < 0 or y < 0: NaN x = +inf or y = +inf: NaN x = 0 and y > c: +inf x = 0 and
         * y = 0: +inf
         */

        if (x < y) {
            return lbeta(y, x);
        }
        if (!(Double.isFinite(x) && Double.isFinite(y))) {
            return Double.NaN;
        }
        double lgammaDiff = lgammaDiff(x, y);
        return Double.isFinite(lgammaDiff) ? lgamma(y) - lgammaDiff : Double.POSITIVE_INFINITY;
    }

    /**
     * (x-0.5)log(x)-x+0.5*log(2π) を返す. x≧O(1)の場合.
     */
    private static double lgammaStirling(double x) {
        double xLogX = (x - 0.5) * Exponentiation.log(x);
        if (!Double.isFinite(xLogX)) {
            return Double.POSITIVE_INFINITY;
        }
        return xLogX + (HALF_LN2PI - x);
    }

    /**
     * logΓ(x)-(x-0.5)log(x)+x-0.5*log(2π) を返す. x ≧ 10の場合に使える.
     */
    private static double lgammaStirlingResidual_largekernel(double x) {
        final double LGL1 = 0.08333333333333333064945661182049641;
        final double LGL3 = -0.002777777777758313951279939867832287;
        final double LGL5 = 0.0007936507707643677006619697936172682;
        final double LGL7 = -0.0005952282316677593413675921518520513;
        final double LGL9 = 0.0008398192336598486705535186251284218;
        final double LGL11 = -0.001740747338444707481810762324864536;

        // スターリング近似の残差
        final double t = 1 / x;
        final double t2 = t * t;
        final double t4 = t2 * t2;
        final double t8 = t4 * t4;

        final double value1 = LGL1 + t2 * LGL3 + t4 * (LGL5 + t2 * LGL7);
        final double value9 = LGL9 + t2 * LGL11;

        return t * (value1 + t8 * value9);
    }

    /**
     * logΓ(2+x) を返す. |x|≦0.5 で使える.
     */
    private static double lgamma2p_smallkernel(double x) {
        if (x < -0.0625) {
            // -0.5<t<=-0.0625
            final double LGS1 = 0.4227843350983008596583306096954194;
            final double LGS2 = 0.3224670334121198269627160919252506;
            final double LGS3 = -0.06735230143183775539485815451893468;
            final double LGS4 = 0.02058080149546183005847584496711623;
            final double LGS5 = -0.007385633252732008424693205789583849;
            final double LGS6 = 0.002889842087375803982684119565299280;
            final double LGS7 = -0.001196588034074829019757589856268000;
            final double LGS8 = 0.0004939302483365663973498902045675388;
            final double LGS9 = -0.0002693637914124442588630473514738137;
            final double LGS10 = 0.00000391807776457553849792015060104266;
            final double LGS11 = -0.0001789364720701232262318273030310839;
            final double LGS12 = -0.00009728072647299932200552268430088183;
            final double LGS13 = -0.00006274308056768518203314028855255419;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = x * LGS1 + x2 * (LGS2 + x * LGS3);
            final double value4 = LGS4 + x * LGS5 + x2 * (LGS6 + x * LGS7);
            final double value8 = LGS8 + x * LGS9 + x2 * (LGS10 + x * LGS11);
            final double value12 = LGS12 + x * LGS13;

            return value0 + x4 * value4 + x8 * (value8 + x4 * value12);
        } else {
            // -0.0625<t<=0.5
            final double LGS1 = 0.4227843350984671230072843812432122;
            final double LGS2 = 0.3224670334241127019527856417859692;
            final double LGS3 = -0.06735230105315078763835020607396352;
            final double LGS4 = 0.02058080842794992120727111339632300;
            final double LGS5 = -0.007385551051529638073801622742244391;
            final double LGS6 = 0.002890510507276056578577264444263064;
            final double LGS7 = -0.001192751900465154764759817046008139;
            final double LGS8 = 0.0005096275297911888335260542682144962;
            final double LGS9 = -0.0002228372973376209981440889452782782;
            final double LGS10 = 0.00009809952404901887262848472841667935;
            final double LGS11 = -0.00004130769197103206783157647897397780;
            final double LGS12 = 0.00001436756353174823223009891768382961;
            final double LGS13 = -0.000002877291901600298290729299780325366;

            final double x2 = x * x;
            final double x4 = x2 * x2;
            final double x8 = x4 * x4;

            final double value0 = x * LGS1 + x2 * (LGS2 + x * LGS3);
            final double value4 = LGS4 + x * LGS5 + x2 * (LGS6 + x * LGS7);
            final double value8 = LGS8 + x * LGS9 + x2 * (LGS10 + x * LGS11);
            final double value12 = LGS12 + x * LGS13;

            return value0 + x4 * value4 + x8 * (value8 + x4 * value12);
        }
    }

    /**
     * @return インスタンス
     */
    public static LGammaCalculation instance() {
        return INSTANCE;
    }
}
