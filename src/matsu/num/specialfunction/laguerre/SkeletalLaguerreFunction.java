/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.11
 */
package matsu.num.specialfunction.laguerre;

import matsu.num.specialfunction.LaguerreFunction;

/**
 * {@link LaguerreFunction} の骨格実装
 * 
 * @author Matsuura Y.
 */
abstract class SkeletalLaguerreFunction implements LaguerreFunction {

    final int degreeN;
    final int orderK;

    /**
     * 唯一の非公開のコンストラクタ.
     * 
     * <p>
     * 引数のバリデーションが行われていないので, 呼び出しもとでチェックする.
     * </p>
     */
    SkeletalLaguerreFunction(int degreeN, int orderK) {
        super();
        this.degreeN = degreeN;
        this.orderK = orderK;
    }

    @Override
    public final int degreeN() {
        return this.degreeN;
    }

    @Override
    public final int orderK() {
        return this.orderK;
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * バージョン間の互換性は担保されない. <br>
     * おそらく, つぎのような形式であろう. <br>
     * {@code LaguerreFunction(degree(n) = %degreeN, order(k) = %orderK)}
     * </p>
     */
    @Override
    public String toString() {
        return "LaguerreFunction(degree(n) = %s, order(k) = %s)"
                .formatted(this.degreeN(), this.orderK());
    }
}
