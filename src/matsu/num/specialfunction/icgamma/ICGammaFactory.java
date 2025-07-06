/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.5
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数のファクトリ.
 *
 * @author Matsuura Y.
 */
public final class ICGammaFactory {

    static final double K_THRESHOLD_SECOND = 11;
    static final double K_THRESHOLD_THIRD = 40000;

    private ICGammaFactory() {
        throw new AssertionError();
    }

    /**
     * 指定したパラメータの不完全ガンマ関数計算インスタンスを返す.
     *
     * @param a パラメータa
     * @return パラメータaの不完全ガンマ関数計算インスタンス
     * @throws IllegalArgumentException パラメータが不正の場合
     */
    public static IncompleteGammaFunction instanceOf(double a) {
        if (!IncompleteGammaFunction.acceptsParameter(a)) {
            throw new IllegalArgumentException(
                    String.format("パラメータがサポート外である: a = %s", a));
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
