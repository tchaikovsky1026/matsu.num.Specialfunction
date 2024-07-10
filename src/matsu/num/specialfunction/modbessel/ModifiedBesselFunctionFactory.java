/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.9
 */
package matsu.num.specialfunction.modbessel;

import static matsu.num.specialfunction.ModifiedBesselFunction.*;

import matsu.num.specialfunction.ModifiedBesselFunction;

/**
 * 変形Bessel関数のファクトリを扱う.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
public final class ModifiedBesselFunctionFactory {

    private static final ModifiedBessel0thOrder M_BESSEL_0 = new NaiveMBessel0();
    private static final ModifiedBessel1stOrder M_BESSEL_1 = new NaiveMBessel1();

    /**
     * <p>
     * 指定したパラメータ (次数) が適合するかを判定する.
     * </p>
     * 
     * @param order 次数 <i>n</i>
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int order) {
        return LOWER_LIMIT_OF_ORDER <= order
                && order <= UPPER_LIMIT_OF_ORDER;
    }

    /**
     * 指定した次数の変形Bessel関数計算インスタンスを返す.
     *
     * @param order n, 次数
     * @return n次の変形Bessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ModifiedBesselFunction instanceOf(int order) {
        if (!acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    String.format("次数がサポート外である: order = %s", order));
        }

        switch (order) {
        case 0:
            return M_BESSEL_0;
        case 1:
            return M_BESSEL_1;
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
            return new ModifiedBessel2To6(order, M_BESSEL_0, M_BESSEL_1);
        default:
            return new ModifiedBesselOver7(order, M_BESSEL_0, M_BESSEL_1);
        }
    }
}
