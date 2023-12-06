/**
 * 2023.12.6
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数のファクトリ.
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public final class ICGammaFactory {

    //パラメータAの下限.
    static final double LOWER_LIMIT_OF_PARAMETER_A = 1E-2;

    //パラメータAの上限.
    static final double UPPER_LIMIT_OF_PARAMETER_A = 1E28;

    static final double K_THRESHOLD_SECOND = 11;
    static final double K_THRESHOLD_THIRD = 40000;

    private ICGammaFactory() {
        throw new AssertionError();
    }

    /**
     * 指定したパラメータの不完全ガンマ関数計算インスタンスを返す. <br>
     * パラメータ <i>a</i> は, {@code 1.0E-2 <= a <= 1.0E+28} でなければならない.
     *
     * @param a パラメータa
     * @return パラメータaの不完全ガンマ関数計算インスタンス
     * @throws IllegalArgumentException パラメータが範囲外の場合
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        if (!(LOWER_LIMIT_OF_PARAMETER_A <= a && a <= UPPER_LIMIT_OF_PARAMETER_A)) {
            throw new IllegalArgumentException(
                    String.format(
                            "パラメータ不正:a=%s", a));
        }

        if (a <= K_THRESHOLD_SECOND) {
            return new ICGammaAtLowParam(a);
        }

        if (a <= K_THRESHOLD_THIRD) {
            return new ICGammaAtMiddleParam(a);
        }

        return new ICGammaAtHighParam(a);
    }

}
