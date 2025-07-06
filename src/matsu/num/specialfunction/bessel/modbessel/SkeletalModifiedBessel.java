/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.6
 */
package matsu.num.specialfunction.bessel.modbessel;

import matsu.num.specialfunction.bessel.ModifiedBesselFunction;

/**
 * {@link ModifiedBesselFunction} の骨格実装. <br>
 * 主に, 文字列表現 ({@link #toString()})の実装を提供する.
 * 
 * @author Matsuura Y.
 */
abstract class SkeletalModifiedBessel implements ModifiedBesselFunction {

    /**
     * この変形Bessel関数の次数.
     */
    final int order;

    /**
     * 唯一のコンストラクタ
     * 
     * <p>
     * 引数のバリデーションは行われていない.
     * </p>
     */
    SkeletalModifiedBessel(int order) {
        super();

        this.order = order;
    }

    @Override
    public final int order() {
        return this.order;
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
