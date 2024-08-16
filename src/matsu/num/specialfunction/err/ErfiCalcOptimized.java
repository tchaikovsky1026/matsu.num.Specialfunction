/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.15
 */
package matsu.num.specialfunction.err;

import matsu.num.commons.Exponentiation;

/**
 * 速度を最適化した虚数誤差関数の計算.
 * 
 * @author Matsuura Y.
 * @version 19.5
 */
final class ErfiCalcOptimized implements ErrorFunctionImaginaryCalculation {

    /**
     * 1/sqrt(pi)
     */
    // = 1d / Math.sqrt(Math.PI)
    private static final double ONE_OVER_SQRT_PI = 0.5641895835477563;

    /**
     * 計算アルゴリズムを切り替えるxの閾値. <br>
     * 下側はerfiをminimax近似で, 上側はerfixを計算する.
     */
    private static final double BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX = 1d;

    /**
     * erfix(x) をもとにした erfi(x) で,
     * exp(x^2)のオーバーフローを回避するために導入するシフト. <br>
     * x^2をシフトする.
     */
    private static final double SHIFT_X2_FOR_ERFIX_TO_ERFI = 20;

    /**
     * exp(shift_x2)
     */
    private static final double EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI =
            Math.exp(SHIFT_X2_FOR_ERFIX_TO_ERFI);

    /**
     * 唯一のコンストラクタ.
     */
    public ErfiCalcOptimized() {
        super();
    }

    @Override
    public double erfi(double x) {

        final double absX = Math.abs(x);
        if (absX <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x);
        }

        final double x2 = x * x;
        if (x2 < SHIFT_X2_FOR_ERFIX_TO_ERFI) {
            return erfix_largeX(x) * Exponentiation.exp(x2);
        }

        // erfix = 0 なら erfi = inf or -inf: 明らかに exp(x^2) が吹き飛ぶ
        // x^2が或る程度の場合におこる, (小) * (inf) = (finite) に対する対策 
        final double erfix = erfix_largeX(x);
        return erfix == 0d
                ? (x < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY)
                : (erfix * Exponentiation.exp(x2 - SHIFT_X2_FOR_ERFIX_TO_ERFI)) * EXP_OF_SHIFT_X2_FOR_ERFIX_TO_ERFI;
    }

    @Override
    public double erfix(double x) {

        if (Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX) {
            return erfi_smallX(x) * Exponentiation.exp(-x * x);
        }

        return erfix_largeX(x);
    }

    /**
     * xが小さいときのerfi(x)
     */
    private double erfi_smallX(double x) {
        assert Math.abs(x) <= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        final double C0 = 1.0;
        final double C1 = 0.33333333333333337;
        final double C2 = 0.09999999999998523;
        final double C3 = 0.023809523810111064;
        final double C4 = 0.004629629620534737;
        final double C5 = 7.575758297445123E-4;
        final double C6 = 1.0683727326917161E-4;
        final double C7 = 1.3228472074278561E-5;
        final double C8 = 1.4571538571825993E-6;
        final double C9 = 1.4710632676384935E-7;
        final double C10 = 1.1637912499194568E-8;
        final double C11 = 1.6700327225348162E-9;

        final double u = x * x;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8 + u * C9 + u2 * (C10 + u * C11);

        final double u4 = u2 * u2;

        return (ONE_OVER_SQRT_PI * 2) * x *
                (v0 + u4 * (v4 + u4 * v8));
    }

    /**
     * xが大きいときのerfix(x)
     */
    private double erfix_largeX(double x) {
        assert Math.abs(x) >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        return ONE_OVER_SQRT_PI / x * erfix_largeX_factor(x);
    }

    /**
     * erfix(x) = 2/sqrt(pi) * x * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor(double x) {

        final double abs_x = Math.abs(x);

        assert abs_x >= BOUNDARY_ABS_X_SELECTING_ERFI_OR_ERFIX;

        switch ((int) abs_x) {
        case 1:
            return erfix_largeX_factor_1_to_2(abs_x);
        case 2:
            return erfix_largeX_factor_2_to_3(abs_x);
        case 3:
            return erfix_largeX_factor_3_to_4(abs_x);
        case 4:
            return erfix_largeX_factor_4_to_5(abs_x);
        case 5:
            return erfix_largeX_factor_5_to_6(abs_x);
        case 6:
            return erfix_largeX_factor_6_to_7(abs_x);
        case 7:
            return erfix_largeX_factor_7_to_8(abs_x);
        default:
            return erfix_largeX_factor_greater_than_8(abs_x);
        }
    }

    private double erfix_largeX_factor_1_to_2(double abs_x_raw) {
        assert abs_x_raw >= 1d;
        assert abs_x_raw <= 2d;

        final double x_shift = 1.5d;

        final double C0 = 1.2847472132561957;
        final double C1 = 0.0022565024022082047;
        final double C2 = -0.5728791801155888;
        final double C3 = 0.5706226777137008;
        final double C4 = -0.046047554885572606;
        final double C5 = -0.25768280594413023;
        final double C6 = 0.14726042532484526;
        final double C7 = 0.022782658299244814;
        final double C8 = -0.05061791181252281;
        final double C9 = 0.011176965358377842;
        final double C10 = 0.007895421142383442;
        final double C11 = -0.00438862833517662;
        final double C12 = -3.389444210197466E-4;
        final double C13 = 8.093550522980024E-4;
        final double C14 = -1.1905947315746758E-4;
        final double C15 = -9.097596145692447E-5;
        final double C16 = 2.8208429925747382E-5;
        final double C17 = 5.229224772153689E-6;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * (C14 + x * C15);
        final double v16 = C16 + x * C17;

        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        return v0 + x4 * v4 + x8 * (v8 + x4 * v12 + x4 * x4 * v16);
    }

    private double erfix_largeX_factor_2_to_3(double abs_x_raw) {
        assert abs_x_raw >= 2d;
        assert abs_x_raw <= 3d;

        final double x_shift = 2.5d;

        final double C0 = 1.1154186108371775;
        final double C1 = -0.1309256098510247;
        final double C2 = 0.09647680295316813;
        final double C3 = -0.02986906173623429;
        final double C4 = -0.026981541462555363;
        final double C5 = 0.041916072261120964;
        final double C6 = -0.024137443710802012;
        final double C7 = 0.0032690088077237506;
        final double C8 = 0.004853283223580847;
        final double C9 = -0.0035135425355154464;
        final double C10 = 6.782530504736236E-4;
        final double C11 = 3.945762869125124E-4;
        final double C12 = -2.8767403461290046E-4;
        final double C13 = 4.416991959239819E-5;
        final double C14 = 2.836358206827855E-5;
        final double C15 = -1.412667792136202E-5;
        final double C16 = 7.101942894493442E-7;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * (C14 + x * C15);
        final double v16 = C16;

        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        return v0 + x4 * v4 + x8 * (v8 + x4 * v12 + x4 * x4 * v16);
    }

    private double erfix_largeX_factor_3_to_4(double abas_x_raw) {
        assert abas_x_raw >= 3d;
        assert abas_x_raw <= 4d;

        final double x_shift = 3.5d;

        final double C0 = 1.0473511515652956;
        final double C1 = -0.03221487479554688;
        final double C2 = 0.018049758653790854;
        final double C3 = -0.009901228731366672;
        final double C4 = 0.005293977847135521;
        final double C5 = -0.0024609545587853192;
        final double C6 = 7.535224011985381E-4;
        final double C7 = 6.679461796385469E-5;
        final double C8 = -2.737355177570523E-4;
        final double C9 = 1.962180306310517E-4;
        final double C10 = -7.65392387085211E-5;
        final double C11 = 9.404906660613588E-6;
        final double C12 = 8.510512553595854E-6;
        final double C13 = -5.996805490768518E-6;
        final double C14 = 1.4899994265638679E-6;

        final double x = abas_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13 + x2 * C14;

        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        return v0 + x4 * v4 + x8 * (v8 + x4 * v12);
    }

    private double erfix_largeX_factor_4_to_5(double abs_x_raw) {
        assert abs_x_raw >= 4d;
        assert abs_x_raw <= 5d;

        final double x_shift = 4.5d;

        final double C0 = 1.026797492041424;
        final double C1 = -0.013000207919176545;
        final double C2 = 0.004905951553626397;
        final double C3 = -0.0017176467409836665;
        final double C4 = 5.940707875223694E-4;
        final double C5 = -2.1050406778021845E-4;
        final double C6 = 7.812803000719532E-5;
        final double C7 = -3.0282004892150953E-5;
        final double C8 = 1.174226806231268E-5;
        final double C9 = -4.1740560761161205E-6;
        final double C10 = 1.162665894324039E-6;
        final double C11 = -1.0687996038520355E-7;
        final double C12 = -1.7516628862934316E-7;
        final double C13 = 1.1875922879051391E-7;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12 + x * C13;

        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        return v0 + x4 * v4 + x8 * (v8 + x4 * v12);
    }

    private double erfix_largeX_factor_5_to_6(double abs_x_raw) {
        assert abs_x_raw >= 5d;
        assert abs_x_raw <= 6d;

        final double x_shift = 5.5d;

        final double C0 = 1.0174255554183023;
        final double C1 = -0.006694644979816376;
        final double C2 = 0.001969436552374271;
        final double C3 = -5.266223788936255E-4;
        final double C4 = 1.352538413189348E-4;
        final double C5 = -3.4247259559036576E-5;
        final double C6 = 8.685086107622713E-6;
        final double C7 = -2.2322897593761466E-6;
        final double C8 = 5.881154691913272E-7;
        final double C9 = -1.602698236448257E-7;
        final double C10 = 4.500874390937746E-8;
        final double C11 = -1.4744056896143899E-8;
        final double C12 = 5.812106046123581E-9;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * (C10 + x * C11);
        final double v12 = C12;

        final double x4 = x2 * x2;
        final double x8 = x4 * x4;

        return v0 + x4 * v4 + x8 * (v8 + x4 * v12);
    }

    private double erfix_largeX_factor_6_to_7(double abs_x_raw) {
        assert abs_x_raw >= 6d;
        assert abs_x_raw <= 7d;

        final double x_shift = 6.5d;

        final double C0 = 1.0122816468189082;
        final double C1 = -0.003925770673675797;
        final double C2 = 9.542157410510497E-4;
        final double C3 = -2.0916420359405168E-4;
        final double C4 = 4.3639835578141584E-5;
        final double C5 = -8.88148339173303E-6;
        final double C6 = 1.787258736193143E-6;
        final double C7 = -3.585878172731744E-7;
        final double C8 = 7.221055963398107E-8;
        final double C9 = -1.510304421708928E-8;
        final double C10 = 3.1048895122054245E-9;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * C10;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * v8);
    }

    private double erfix_largeX_factor_7_to_8(double abs_x_raw) {
        assert abs_x_raw >= 7d;
        assert abs_x_raw <= 8d;

        final double x_shift = 7.5d;

        final double C0 = 1.00913717466946;
        final double C1 = -0.002505996752633412;
        final double C2 = 5.206263056970371E-4;
        final double C3 = -9.713477606477568E-5;
        final double C4 = 1.71712096748835E-5;
        final double C5 = -2.946236739397698E-6;
        final double C6 = 4.970763602475232E-7;
        final double C7 = -8.312181645395235E-8;
        final double C8 = 1.3967843409307345E-8;
        final double C9 = -2.3627219594016166E-9;
        final double C10 = 2.1751741404796402E-10;

        final double x = abs_x_raw - x_shift;
        final double x2 = x * x;

        final double v0 = C0 + x * C1 + x2 * (C2 + x * C3);
        final double v4 = C4 + x * C5 + x2 * (C6 + x * C7);
        final double v8 = C8 + x * C9 + x2 * C10;

        final double x4 = x2 * x2;

        return v0 + x4 * (v4 + x4 * v8);
    }

    /**
     * erfix(x) = 1/sqrt(pi) * (1/x) * F(x)
     * としたときのF(x).
     */
    private double erfix_largeX_factor_greater_than_8(double abs_x_raw) {
        assert abs_x_raw >= 8d;

        final double C0 = 1.0;
        final double C1 = 0.4999999999999103;
        final double C2 = 0.7500000004534517;
        final double C3 = 1.874999580711923;
        final double C4 = 6.562662487899874;
        final double C5 = 29.498559851750695;
        final double C6 = 166.09787288923178;
        final double C7 = 825.9905602559971;
        final double C8 = 15067.206013055253;

        final double t = 1 / abs_x_raw;
        final double u = t * t;
        final double u2 = u * u;

        final double v0 = C0 + u * C1 + u2 * (C2 + u * C3);
        final double v4 = C4 + u * C5 + u2 * (C6 + u * C7);
        final double v8 = C8;

        final double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * v8);
    }
}
