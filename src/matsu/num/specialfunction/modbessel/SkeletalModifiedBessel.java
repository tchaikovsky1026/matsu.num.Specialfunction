/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.19
 */
package matsu.num.specialfunction.modbessel;

import matsu.num.specialfunction.ModifiedBesselFunction;

/**
 * {@link ModifiedBesselFunction} の骨格実装. <br>
 * 主に, 文字列表現 ({@link #toString()})の実装を提供する.
 * 
 * @author Matsuura Y.
 * @version 18.2
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
