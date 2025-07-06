/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.6
 */
package matsu.num.specialfunction.bessel.sbessel;

import matsu.num.specialfunction.bessel.SphericalBesselFunction;

/**
 * SphericalBessel関数の骨格実装. <br>
 * {@link #toString()} の実装を提供する.
 * 
 * @author Matsuura Y.
 */
abstract class SkeletalSBessel implements SphericalBesselFunction {

    /**
     * この球Bessel関数の次数.
     */
    final int order;

    /**
     * 唯一のコンストラクタ.
     * 
     * <p>
     * 引数のバリデーションは行われていない.
     * </p>
     */
    SkeletalSBessel(int order) {
        super();

        this.order = order;
    }

    @Override
    public final int order() {
        return this.order;
    }

    /**
     * このインスタンスの文字列表現を提供する.
     * 
     * <p>
     * 概ね, 次のような表現であろう.
     * ただし, バージョン間の互換性は担保されていない. <br>
     * {@code SphericalBessel(%order)}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "SphericalBessel(%s)", this.order());
    }
}
