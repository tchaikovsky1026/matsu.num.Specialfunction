/*
 * 2023.3.22
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * 不完全ベータ関数のファクトリ.
 *
 * @author Matsuura Y.
 * @version 11.0
 */
public final class ICBetaFactory {

    //パラメータA,Bの下限.
    static final double LOWER_LIMIT_OF_PARAMETER_AB = 1E-2;

    //パラメータA,Bの上限.
    static final double UPPER_LIMIT_OF_PARAMETER_AB = 1E14;

    static final double AB_THRESHOLD_FIRST = 11;
    static final double AB_THRESHOLD_SECOND = 40000;

    /**
     * 指定したパラメータの不完全ベータ関数計算インスタンスを返す. <br>
     * パラメータ <i>a</i>,<i>b</i> は, {@code 1.0E-2 <= a <= 1.0E+14} でなければならない.
     *
     * @param a パラメータa
     * @param b パラメータb
     * @return パラメータa,bの不完全ベータ関数計算インスタンス
     * @throws IllegalArgumentException パラメータが範囲外の場合
     */
    public static IncompleteBetaFunction instanceOf(double a, double b) {
        if (!(LOWER_LIMIT_OF_PARAMETER_AB <= a && a <= UPPER_LIMIT_OF_PARAMETER_AB
                && LOWER_LIMIT_OF_PARAMETER_AB <= b && b <= UPPER_LIMIT_OF_PARAMETER_AB)) {
            throw new IllegalArgumentException(
                    String.format(
                            "パラメータ不正:(a,b)=(%s,%s)", a, b));
        }
        double minAB = Math.min(a, b);
        if (minAB <= AB_THRESHOLD_FIRST) {
            return new ICBetaAtLowParam(a, b);
        }
        if (minAB <= AB_THRESHOLD_SECOND) {
            return new ICBetaAtMiddleParam(a, b);
        }
        return new ICBetaAtHighParam(a, b);
    }

}
