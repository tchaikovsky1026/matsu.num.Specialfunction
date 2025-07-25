/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.25
 */
package matsu.num.specialfunction.chebyshev;

import matsu.num.specialfunction.ChebyshevFunction;

/**
 * {@link ChebyshevFunction} の実装を扱う.
 * 
 * @author Matsuura Y.
 */
public final class ChebyshevFunctionImpl extends SkeletalChebyshevFunction {

    private static final StaticChebyshevFunction staticChebyshevFunction =
            new StaticChebyshevFunction();

    /**
     * 唯一の非公開コンストラクタ.
     * 
     * <p>
     * パラメータバリデーションを呼び出しもとですること.
     * </p>
     */
    private ChebyshevFunctionImpl(int degreeN) {
        super(degreeN);
    }

    @Override
    public double chebyshevT(double x) {
        if (!(-1 <= x && x <= 1)) {
            return Double.NaN;
        }

        return staticChebyshevFunction.chebyshevT(degreeN, x);
    }

    @Override
    public double chebyshevU(double x) {
        if (!(-1 <= x && x <= 1)) {
            return Double.NaN;
        }

        return staticChebyshevFunction.chebyshevU(degreeN, x);
    }

    /**
     * 与えられた次数を持つ Chebyshev 関数インスタンスを返す.
     * 
     * <p>
     * パラメータの正当性は {@link ChebyshevFunction#acceptsParameter(int)} により検証され,
     * 不適の場合は例外がスローされる.
     * </p>
     * 
     * @param degreeN 次数 <i>n</i>
     * @return 次数 <i>n</i> の Chebyshev 関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ChebyshevFunction instanceOf(int degreeN) {
        if (!ChebyshevFunction.acceptsParameter(degreeN)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: degreeN = %s"
                            .formatted(degreeN));
        }

        return new ChebyshevFunctionImpl(degreeN);
    }
}
