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
 * {@link LegendreFunction} の実装を扱う.
 * 
 * @author Matsuura Y.
 */
public final class LegendreFunctionImpl extends SkeletalLegendreFunction {

    private static final StaticLegendreFunction staticLegendreFunction = new StaticLegendreFunction();

    /**
     * 唯一の非公開コンストラクタ.
     * 
     * <p>
     * パラメータバリデーションを呼び出しもとですること.
     * </p>
     */
    private LegendreFunctionImpl(int degreeL, int orderM) {
        super(degreeL, orderM);
    }

    @Override
    public double legendreP(double x) {
        if (!(-1 <= x && x <= 1)) {
            return Double.NaN;
        }

        return staticLegendreFunction.legendreP(degreeL, orderM, x);
    }

    /**
     * 与えられた次数と階数を持つ Legendre 関数を構築する. <br>
     * 与えるパラメータは {@link LegendreFunction#acceptsParameter(int, int)}
     * で許可されなければならない.
     * 
     * @param degreeL 次数 l
     * @param orderM 階数 m
     * @return Legendre 関数
     * @throws IllegalArgumentException パラメータが不正の場合
     */
    public static LegendreFunction instanceOf(int degreeL, int orderM) {
        if (!LegendreFunction.acceptsParameter(degreeL, orderM)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: degreeL = %s, orderM = %s"
                            .formatted(degreeL, orderM));
        }

        return new LegendreFunctionImpl(degreeL, orderM);
    }
}
