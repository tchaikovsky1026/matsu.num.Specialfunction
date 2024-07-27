/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modsbessel;

import static matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction.*;

import matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction;

/**
 * 変形球Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
 * @version 18.9
 */
public final class MSBesselFunctionFactory {

    private static final MSBessel0 M_BESSEL_0 = new MSBessel0InPrinciple();
    private static final MSBessel1 M_BESSEL_1 = new MSBessel1InPrinciple();

    /**
     * Higher orderを生成する閾値.
     */
    static final int LOWER_LIMIT_OF_HIGHER_ORDER = 2;

    private MSBesselFunctionFactory() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * <p>
     * 指定したパラメータ (次数) が適合するかを判定する.
     * </p>
     * 
     * @param order 次数 n
     * @return パラメータが適合する場合はtrue
     */
    public static boolean acceptsParameter(int order) {
        return LOWER_LIMIT_OF_ORDER <= order
                && order <= UPPER_LIMIT_OF_ORDER;
    }

    /**
     * 指定した次数の球Bessel関数計算インスタンスを返す.
     *
     * @param order n, 次数
     * @return n次の球Bessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ModifiedSphericalBesselFunction instanceOf(int order) {
        if (!acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    String.format("次数がサポート外である: order = %s", order));
        }

        switch (order) {
        case 0:
            return M_BESSEL_0;
        case 1:
            return M_BESSEL_1;
        default:
            return new MSBesselOver2(order, M_BESSEL_0, M_BESSEL_1);
        }
    }
}
