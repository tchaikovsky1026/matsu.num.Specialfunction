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
 * {@link HermiteFunction} の実装を扱う.
 * 
 * @author Matsuura Y.
 */
public final class HermiteFunctionImpl extends SkeletalHermiteFunction {

    private static final StaticHermiteFunction staticHermiteFunction = new StaticHermiteFunction();

    /**
     * 唯一の非公開コンストラクタ.
     * 
     * <p>
     * パラメータバリデーションを呼び出しもとですること.
     * </p>
     */
    private HermiteFunctionImpl(int degreeN) {
        super(degreeN);
    }

    @Override
    public double hermiteH(double x) {
        if (!(x >= 0d)) {
            return Double.NaN;
        }

        return staticHermiteFunction.hermiteH(degreeN, x);
    }

    /**
     * 与えられた次数を持つ Hermite 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link HermiteFunction#acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @return 次数が <i>n</i> の Hermite 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static HermiteFunction instanceOf(int degreeN) {
        if (!HermiteFunction.acceptsParameter(degreeN)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: degreeN = %s"
                            .formatted(degreeN));
        }

        return new HermiteFunctionImpl(degreeN);
    }
}
