/**
 * 2023.12.5
 */
package matsu.num.specialfunction.bessel;

import java.util.Objects;

import matsu.num.specialfunction.BesselFunction;

/**
 * {@linkplain BesselFunction}向けの文字列表現を提供するユーティリティ.
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
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
