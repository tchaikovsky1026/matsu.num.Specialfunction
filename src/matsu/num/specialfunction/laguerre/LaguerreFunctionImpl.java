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
 * {@link LaguerreFunction} の実装を扱う.
 * 
 * @author Matsuura Y.
 */
public final class LaguerreFunctionImpl extends SkeletalLaguerreFunction {

    /**
     * 唯一の非公開コンストラクタ.
     * 
     * <p>
     * パラメータバリデーションを呼び出しもとですること.
     * </p>
     */
    private LaguerreFunctionImpl(int degreeN, int orderK) {
        super(degreeN, orderK);
    }

    private static final StaticLaguerreFunction staticLaguerreFunction = new StaticLaguerreFunction();

    @Override
    public double laguerreL(double x) {
        if (!(x >= 0d)) {
            return Double.NaN;
        }

        return staticLaguerreFunction.laguerreL(degreeN, orderK, x);
    }

    /**
     * 与えられた次数と階数を持つ Laguerre 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link LaguerreFunction#acceptsParameter(int, int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @param orderK 階数 <i>k</i>
     * @return (<i>n</i>, <i>k</i>) の Laguerre 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数, 階数がサポート外の場合
     */
    public static LaguerreFunction instanceOf(int degreeN, int orderK) {
        if (!LaguerreFunction.acceptsParameter(degreeN, orderK)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: degreeN = %s, orderK = %s"
                            .formatted(degreeN, orderK));
        }

        return new LaguerreFunctionImpl(degreeN, orderK);
    }
}
