/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.23
 */
package matsu.num.specialfunction.bessel.modbessel;

/**
 * {@link ModifiedBesselFunction} の骨格実装. <br>
 * 主に, 文字列表現 ({@link #toString()})の実装を提供する.
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
abstract class SkeletalModifiedBessel implements ModifiedBesselFunction {

    protected SkeletalModifiedBessel() {
        super();
    }

    /**
     * <p>
     * このインスタンスの文字列表現を返す.
     * </p>
     * 
     * <p>
     * 文字列表現は頑強でなく, 将来のバージョンで変更されるかもしれない. <br>
     * おそらく次のようである. <br>
     * {@code ModifiedBessel(order)}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "ModifiedBessel(%s)", this.order());
    }
}
