/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.24
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * 不完全ガンマ関数のファクトリ.
 *
 * @author Matsuura Y.
 */
public final class ICGammaFactory {

    static final double A_THRESHOLD = 1000;

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
                    "Illegal parameter: a = %s".formatted(a));
        }

        if (a <= A_THRESHOLD) {
            return new WithShiftingICGamma(a);
        }
        return new TemmeTypeICGamma(a);
    }
}
