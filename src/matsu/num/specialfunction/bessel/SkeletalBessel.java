/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.19
 */
package matsu.num.specialfunction.bessel;

import matsu.num.specialfunction.BesselFunction;

/**
 * Bessel関数の骨格実装. <br>
 * {@link #toString()} の実装を提供する.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
abstract class SkeletalBessel implements BesselFunction {

    protected SkeletalBessel() {
        super();
    }

    /**
     * このインスタンスの文字列表現を提供する.
     * 
     * <p>
     * 概ね, 次のような表現であろう.
     * ただし, バージョン間の互換性は担保されていない. <br>
     * {@code Bessel(%shape)}
     * </p>
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "Bessel(%s)", this.order());
    }
}