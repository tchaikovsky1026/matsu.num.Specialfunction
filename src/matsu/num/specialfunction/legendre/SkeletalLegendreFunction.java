/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.9
 */
package matsu.num.specialfunction.legendre;

import matsu.num.specialfunction.LegendreFunction;

/**
 * {@link LegendreFunction} の骨格実装.
 * 
 * @author Matsuura Y.
 */
abstract class SkeletalLegendreFunction implements LegendreFunction {

    final int degreeL;
    final int orderM;

    /**
     * 唯一の非公開のコンストラクタ.
     * 
     * <p>
     * 引数のバリデーションが行われていないので, 呼び出しもとでチェックする.
     * </p>
     */
    SkeletalLegendreFunction(int degreeL, int orderM) {
        super();
        this.degreeL = degreeL;
        this.orderM = orderM;
    }

    @Override
    public final int degreeL() {
        return this.degreeL;
    }

    @Override
    public final int orderM() {
        return this.orderM;
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * <p>
     * バージョン間の互換性は担保されない. <br>
     * おそらく, つぎのような形式であろう. <br>
     * {@code LegendreFunction(degree(l) = %degreeL, order(m) = %orderM)}
     * </p>
     */
    @Override
    public String toString() {
        return "LegendreFunction(degree(l) = %s, order(m) = %s)"
                .formatted(this.degreeL(), this.orderM());
    }
}
