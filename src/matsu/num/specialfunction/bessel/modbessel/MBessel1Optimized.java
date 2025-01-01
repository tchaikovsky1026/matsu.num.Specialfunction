/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.common.Exponentiation;

/**
 * 最適化された1次の変形Bessel関数の実装.
 * 
 * <p>
 * 1次MBesselの計算戦略は次の通りである. <br>
 * bessel_Iは, 小さいxではべき級数表示を, 大きいxでは漸近級数を使う. <br>
 * bessel_Kは, 小さいxではべき級数表示のminimax近似を,
 * 大きいxでは漸近級数の連分数表示を区分で分けてminimax近似をする.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
final class MBessel1Optimized extends ModifiedBessel1stOrder {

    private static final double SQRT_INV_2PI = 1d / Math.sqrt(2 * Math.PI);
    private static final double SQRT_PI_OVER_2 = Math.sqrt(Math.PI / 2);
    private static final double GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はI(x)のu (= (x/2)^2) に関するべき級数のminimax近似,
     * 上側はI(x)exp(-x)のxに関するべき級数のminimac近似.
     */
    private static final double BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I = 2d;

    /**
     * I(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数. <br>
     * 漸近級数ではexp(-x)成分を排除する必要があるため, この閾値は大きくとる.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I = 24d;

    /**
     * K(x)についてアルゴリズムを切り替えるxの閾値. <br>
     * 下側はべき級数, 上側は漸近級数の連分数表示.
     */
    private static final double BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K = 2d;

    /**
     * 漸近級数によるI(x)で,
     * exp(x)のオーバーフローを回避するために導入するシフト.
     */
    private static final double SHIFT_X_FOR_BESSEL_I = 20;

    /**
     * exp(shift_x)
     */
    private static final double EXP_OF_SHIFT_X_FOR_BESSEL_I = Math.exp(SHIFT_X_FOR_BESSEL_I);

    MBessel1Optimized() {
        super();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I) {
            final double halfX = x / 2d;
            final double u = halfX * halfX;
            return mbI_PowerTerm(u) * halfX;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x) * Exponentiation.exp(x);
        }

        double scaling = scaling_mbI_byAsymptotic(x);
        if (scaling == 0d) {
            return Double.POSITIVE_INFINITY;
        }

        /*
         * 漸近級数によるI(x)で,
         * I(x)はオーバーフローしないがexp(x)がオーバーフローする場合に対応するため,
         * xを負の方向にシフトする.
         */
        return (scaling * EXP_OF_SHIFT_X_FOR_BESSEL_I) *
                Exponentiation.exp(x - SHIFT_X_FOR_BESSEL_I);
    }

    @Override
    public double besselK(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x);
        } else {
            return scaling_mbK_byAsymptoticFraction(x) * Exponentiation.exp(-x);
        }
    }

    @Override
    public double besselIc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < BOUNDARY_X_SELECTING_ACCURATE_OR_EXP_FOR_BESSEL_I) {
            final double halfX = x / 2d;
            final double u = halfX * halfX;
            return mbI_PowerTerm(u) * halfX * Exponentiation.exp(-x);
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_I) {
            return scaling_mbI_byPowerExp(x);
        } else {
            return scaling_mbI_byAsymptotic(x);
        }
    }

    @Override
    public double besselKc(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        if (x < BOUNDARY_X_SELECTING_POWER_OR_ASYMPTOTIC_FOR_BESSEL_K) {
            return mbK_byPower(x) * Exponentiation.exp(x);
        } else {
            return scaling_mbK_byAsymptoticFraction(x);
        }
    }

    /**
     * I1(x) = (x/2)F(u) としたときのF(u)
     */
    private static double mbI_PowerTerm(double u) {
        final double C0 = 1;
        final double C1 = 0.49999999999999933284305986001953;
        final double C2 = 0.083333333333354934515231556578770;
        final double C3 = 0.0069444444442054155391890164755313;
        final double C4 = 0.00034722222350104441504009338516982;
        final double C5 = 0.000011574070290688636204507528381170;
        final double C6 = 2.7557970963279049807648621918918E-7;
        final double C7 = 4.9144477719850609245326194999588E-9;
        final double C8 = 7.1820236166041190409525073852568E-11;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    /**
     * {@literal 2 <= x <= 24} について, I(x) * exp(-x)をべき級数のminimax近似で計算する.
     */
    private static double scaling_mbI_byPowerExp(double x) {
        assert x >= 2;
        assert x <= 24;

        /*
         * I(x)/((x/2)exp(x)) を表現する.
         */
        double factor;

        switch ((int) (x * 0.5)) {
            case 1, 2:
                factor = factor_scaling_mbI_byPowerExp_2_to_6(x);
                break;
            case 3, 4:
                factor = factor_scaling_mbI_byPowerExp_6_to_10(x);
                break;
            case 5, 6:
                factor = factor_scaling_mbI_byPowerExp_10_to_14(x);
                break;
            case 7, 8:
                factor = factor_scaling_mbI_byPowerExp_14_to_18(x);
                break;
            default:
                factor = factor_scaling_mbI_byPowerExp_18_to_24(x);
                break;
        }

        return factor * (x * 0.5);
    }

    private static double factor_scaling_mbI_byPowerExp_2_to_6(double x) {

        double u = x - 4d;

        final double C0 = 0.089375419751217635460938046057142;
        final double C1 = -0.030562169014834619521128587398184;
        final double C2 = 0.0085071999886902193974419123471475;
        final double C3 = -0.0021400814439099889543491675332263;
        final double C4 = 0.00049818284150144585759860373624194;
        final double C5 = -0.00010796882671216447820970369411913;
        final double C6 = 0.000021813756190803083759356685875368;
        final double C7 = -0.0000041102726975303635525983822922942;
        final double C8 = 7.2283983076701238087970692600592E-7;
        final double C9 = -1.1879987003317312013714174739552E-7;
        final double C10 = 1.8279993731044388896049873859743E-8;
        final double C11 = -2.6389557174117273165753339133688E-9;
        final double C12 = 3.5829538723434382723975087787373E-10;
        final double C13 = -4.5912333652070651385085988921723E-11;
        final double C14 = 5.5534295407268444851857291918064E-12;
        final double C15 = -6.2415353269802295017151572509103E-13;
        final double C16 = 6.7849184953924530617655607547227E-14;
        final double C17 = -8.5283577699520139130668196393391E-15;
        final double C18 = 8.4262820895475981747432797083824E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;
        double v16 = C16 + C17 * u + C18 * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12 + u4 * v16)));
    }

    private static double factor_scaling_mbI_byPowerExp_6_to_10(double x) {

        double u = x - 8d;

        final double C0 = 0.033535623323174355639998248308046;
        final double C1 = -0.0060615836897553183353850192864099;
        final double C2 = 0.00091020125849558448241057177677695;
        final double C3 = -0.00012708608106147098193297353642806;
        final double C4 = 0.000017031398898138867567572210857860;
        final double C5 = -0.0000022186773421482827104771825424076;
        final double C6 = 2.8251050972963255561620026473575E-7;
        final double C7 = -3.5234236371910695022936908416043E-8;
        final double C8 = 4.3042515492310985762784502760996E-9;
        final double C9 = -5.1442572657755611512442304522521E-10;
        final double C10 = 6.0068755535287687234641023301495E-11;
        final double C11 = -6.8307383381028380032127997233175E-12;
        final double C12 = 7.4496290950230794696056953259379E-13;
        final double C13 = -7.9739113617065990843514561711137E-14;
        final double C14 = 9.8191149732276233959522533746172E-15;
        final double C15 = -9.9340001705200457927913307159380E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u + (C14 + C15 * u) * u2;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * (v12)));
    }

    private static double factor_scaling_mbI_byPowerExp_10_to_14(double x) {

        double u = x - 12d;

        final double C0 = 0.018577383215030244495822455639707;
        final double C1 = -0.0022692435486285417239375360263605;
        final double C2 = 0.00023072609032623589741231433913071;
        final double C3 = -0.000021867268266446924349817695283037;
        final double C4 = 0.0000019956229729030379256790479460425;
        final double C5 = -1.7778892428092540190401850726225E-7;
        final double C6 = 1.5570627979002633241842340727576E-8;
        final double C7 = -1.3458449816842696709752098704527E-9;
        final double C8 = 1.1508443729841301178785683644634E-10;
        final double C9 = -9.7479391396169729199676058831743E-12;
        final double C10 = 8.1526402765939464378576724036465E-13;
        final double C11 = -6.7789432149715030101830143185946E-14;
        final double C12 = 6.1704597973659763517174647593269E-15;
        final double C13 = -5.0992571385383657148880688032900E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12 + C13 * u;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * v12));
    }

    private static double factor_scaling_mbI_byPowerExp_14_to_18(double x) {

        double u = x - 16d;

        final double C0 = 0.012168701844558512585177972837679;
        final double C1 = -0.0011217736549717049212246898504031;
        final double C2 = 0.000086124137198021425697036803603110;
        final double C3 = -0.0000061673917029196484436147895990059;
        final double C4 = 4.2558082593603529968998494592637E-7;
        final double C5 = -2.8692998987506239986464988867907E-8;
        final double C6 = 1.9036373473106788955652717143973E-9;
        final double C7 = -1.2479902679914766697311015194773E-10;
        final double C8 = 8.1054583911123617705299021074938E-12;
        final double C9 = -5.2183984471981852326947123080995E-13;
        final double C10 = 3.3404641192162705834398473908021E-14;
        final double C11 = -2.2444349627006958637585710323105E-15;
        final double C12 = 1.4325129971458747920423252945824E-16;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * v12));
    }

    private static double factor_scaling_mbI_byPowerExp_18_to_24(double x) {

        double u = x - 21d;

        final double C0 = 0.0081407234709742753127861084020090;
        final double C1 = -0.00057420573923549793179176912389701;
        final double C2 = 0.000033740186968690638579240358894755;
        final double C3 = -0.0000018497500403077906765913101478497;
        final double C4 = 9.7751773013381477323076510236617E-8;
        final double C5 = -5.0490229834006713192870740306235E-9;
        final double C6 = 2.5673220527271804661611044056957E-10;
        final double C7 = -1.2905536898215974379291116794582E-11;
        final double C8 = 6.4302962901621734094381697651014E-13;
        final double C9 = -3.1749171577414998728715540953065E-14;
        final double C10 = 1.5605768057876982092318450482406E-15;
        final double C11 = -8.2127567947837112665263604851973E-17;
        final double C12 = 4.0438865764219600629080284487258E-18;

        double u2 = u * u;
        double v0 = C0 + C1 * u + (C2 + C3 * u) * u2;
        double v4 = C4 + C5 * u + (C6 + C7 * u) * u2;
        double v8 = C8 + C9 * u + (C10 + C11 * u) * u2;
        double v12 = C12;

        double u4 = u2 * u2;

        return v0 + u4 * (v4 + u4 * (v8 + u4 * v12));
    }

    /**
     * 漸近級数のminimax近似による I(x)exp(-x)
     */
    private static double scaling_mbI_byAsymptotic(double x) {
        return scaling_mbI_asymptoticTerm(0.125 / x) * SQRT_INV_2PI / Exponentiation.sqrt(x);
    }

    /**
     * I(x)の漸近級数部分のminimax近似.
     * 
     * {@literal 0 <= t <= 1/192}
     */
    private static double scaling_mbI_asymptoticTerm(double t) {
        final double C0 = 1;
        final double C1 = -3.0000000000000241489963977213912;
        final double C2 = -7.4999999993672799887507076217255;
        final double C3 = -52.500002738939635362379994728854;
        final double C4 = -590.62038798344635924450194141275;
        final double C5 = -9099.5611220913880793111863821155;
        final double C6 = -175467.70997135826723997442545862;
        final double C7 = -4714525.1009710469159270569316806;
        final double C8 = -30387866.053911177423059328013274;
        final double C9 = -10333569222.529218035005023883561;

        final double t2 = t * t;
        double v0 = C0 + C1 * t + (C2 + C3 * t) * t2;
        double v4 = C4 + C5 * t + (C6 + C7 * t) * t2;
        double v8 = C8 + C9 * t;

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    /**
     * べき級数のミニマックス近似による K(x)
     */
    private static double mbK_byPower(double x) {

        final double halfX = x / 2d;
        final double u = halfX * halfX;

        double f_and_g = mbK_byPower_HarmonicTerm(u)
                - (GAMMA + Exponentiation.log(halfX)) * mbI_PowerTerm(u);
        if (Double.isInfinite(f_and_g)) {
            return Double.POSITIVE_INFINITY;
        }

        return 1d / x - halfX * f_and_g;
    }

    /**
     * I1(x)の調和数部分の計算
     */
    private static double mbK_byPower_HarmonicTerm(double u) {

        final double C0 = 0.50000000000000001748150397761035;
        final double C1 = 0.62499999999999717093447798714317;
        final double C2 = 0.13888888888896424943900094084761;
        final double C3 = 0.013599537036264264030329877630524;
        final double C4 = 0.00075810185582039220235650518238038;
        final double C5 = 0.000027391963865366940349418405925716;
        final double C6 = 6.9485748781398167581920025110105E-7;
        final double C7 = 1.3047806468174486410833880432694E-8;
        final double C8 = 1.9964058030373836878360988877266E-10;

        double u2 = u * u;

        double v5 = C5 + C6 * u + (C7 + (C8 * u)) * u2;
        double v1 = C1 + C2 * u + (C3 + (C4 * u)) * u2;
        double u4 = u2 * u2;

        return C0 + u * (v1 + u4 * v5);
    }

    /**
     * 漸近級数の連分数表示による K(x)exp(x)
     */
    private static double scaling_mbK_byAsymptoticFraction(double x) {
        return SQRT_PI_OVER_2 / Exponentiation.sqrt(x)
                * mbK_asymptoticTerm(0.125 / x);
    }

    /**
     * {@literal t <= 1/16} における,
     * 漸近級数部分の計算を扱う.
     * 
     * @param t t
     * @return 漸近級数部分の値
     */
    private static double mbK_asymptoticTerm(double t) {
        if (t < 1d / 64) {
            return mbK_asymptoticTerm_0_to_1_Over64(t);
        }
        if (t < 1d / 32) {
            return mbK_asymptoticTerm_1_to_2_Over64(t);
        }
        return mbK_asymptoticTerm_2_to_4_Over64(t);
    }

    private static double mbK_asymptoticTerm_0_to_1_Over64(double t) {
        final double C0 = 1;
        final double C1 = 2.9999999999999676958245900511540;
        final double C2 = -7.4999999995347455113748212936037;
        final double C3 = 52.499998865276962200704082797839;
        final double C4 = -590.62385299848927036116102916290;
        final double C5 = 9094.9959989006409884526923913051;
        final double C6 = -177152.13495647714160938156826582;
        final double C7 = 4133188.7706354807605208575060738;
        final double C8 = -108128646.09159884088304340981891;
        final double C9 = 2842240319.5767913229930543493796;
        final double C10 = -64120892523.542760179269319550660;
        final double C11 = 1014157419363.5623459002662888254;
        final double C12 = -7962531379373.4766542039939388408;

        double t2 = t * t;

        double v12 = C12;
        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * (v8 + t4 * v12));
    }

    private static double mbK_asymptoticTerm_1_to_2_Over64(double t) {
        final double C0 = 1.0000000076005107930740389486319;
        final double C1 = 2.9999960025088856018875778261670;
        final double C2 = -7.4990364943489117690270147690113;
        final double C3 = 52.358672241595043345196691025402;
        final double C4 = -576.47195988783972107986172422914;
        final double C5 = 8063.1216763946789685148770424398;
        final double C6 = -119793.90283417027224103164674201;
        final double C7 = 1603803.2882296494195495150789989;
        final double C8 = -16680321.834077347579584629830936;
        final double C9 = 114118626.50914855225806585465865;
        final double C10 = -375432434.10190000650229442737410;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * C10;
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }

    private static double mbK_asymptoticTerm_2_to_4_Over64(double t) {
        final double C0 = 1.0000002811024438973674879916141;
        final double C1 = 2.9999168845942354533365118130627;
        final double C2 = -7.4885370723223687406892458618151;
        final double C3 = 51.515044577391141890058867792970;
        final double C4 = -530.94460218830097744670181388104;
        final double C5 = 6337.4863404251805477831326328138;
        final double C6 = -73288.894653520013132160264731969;
        final double C7 = 725668.58231870611732909225599412;
        final double C8 = -5612428.8846266863348103735235367;
        final double C9 = 30999172.651678236701042686736748;
        final double C10 = -107450244.69621186422161130918412;
        final double C11 = 174513426.13707129627494691680936;

        double t2 = t * t;

        double v8 = C8 + t * C9 + t2 * (C10 + t * C11);
        double v4 = C4 + t * C5 + t2 * (C6 + t * C7);
        double v0 = C0 + t * C1 + t2 * (C2 + t * C3);

        double t4 = t2 * t2;

        return v0 + t4 * (v4 + t4 * v8);
    }
}
