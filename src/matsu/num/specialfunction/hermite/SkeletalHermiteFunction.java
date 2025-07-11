/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.11
 */
package matsu.num.specialfunction.hermite;

import matsu.num.specialfunction.HermiteFunction;

/**
 * {@link HermiteFunction} の骨格実装.
 * 
 * @author Matsuura Y.
 */
abstract class SkeletalHermiteFunction implements HermiteFunction {

    final int degreeN;

    /**
     * 唯一の非公開のコンストラクタ.
     * 
     * <p>
     * 引数のバリデーションが行われていないので, 呼び出しもとでチェックする.
     * </p>
     */
    SkeletalHermiteFunction(int degreeN) {
        super();
        this.degreeN = degreeN;
    }

    @Override
    public final int degreeN() {
        return this.degreeN;
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * バージョン間の互換性は担保されない. <br>
     * おそらく, つぎのような形式であろう. <br>
     * {@code HermiteFunction(degree(n) = %degreeN)}
     * </p>
     */
    @Override
    public String toString() {
        return "HermiteFunction(degree(n) = %s)"
                .formatted(this.degreeN());
    }
}
