/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.5
 */
package matsu.num.specialfunction.bessel.modsbessel;

import static matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction.*;

import matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction;

/**
 * 変形球Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
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
     * 指定した次数の球Bessel関数計算インスタンスを返す.
     *
     * @param order n, 次数
     * @return n次の球Bessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static ModifiedSphericalBesselFunction instanceOf(int order) {
        if (!acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: order = %s".formatted(order));
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
