/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.bessel.bessel;

import java.util.Objects;

import matsu.num.specialfunction.BesselFunction;

/**
 * {@linkplain BesselFunction}向けの文字列表現を提供するユーティリティ.
 * 
 * @author Matsuura Y.
 * @version 18.0
 * @deprecated このクラスは使われていない. ver 19以降に削除予定である.
 */
@Deprecated(forRemoval = true)
final class BesselFunctionToString {

    private BesselFunctionToString() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * {@linkplain BesselFunction}向けの文字列表現を提供する.
     * 
     * <p>
     * 概ね, 次のような表現であろう.
     * ただし, バージョン間の互換性は担保されていない. <br>
     * {@code Bessel(%shape)}
     * </p>
     * 
     * @param bessel bessel
     * @return BesselFunction向け文字列表現
     */
    public static String toString(BesselFunction bessel) {
        if (Objects.isNull(bessel)) {
            return "null";
        }
        return String.format(
                "Bessel(%s)", bessel.order());

    }
}
