/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.5
 */
package matsu.num.specialfunction.bessel.sbessel;

import static matsu.num.specialfunction.bessel.SphericalBesselFunction.*;

import matsu.num.specialfunction.bessel.SphericalBesselFunction;

/**
 * 球Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
 */
public final class SBesselFunctionFactory {

    private static final SBessel0 S_BESSEL_0 = new SBessel0InPrinciple();
    private static final SBessel1 S_BESSEL_1 = new SBessel1InPrinciple();

    /**
     * Higher orderを生成する閾値.
     */
    static final int LOWER_LIMIT_OF_HIGHER_ORDER = 2;

    private SBesselFunctionFactory() {
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
    public static SphericalBesselFunction instanceOf(int order) {
        if (!acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    "Illegal parameter: order = %s".formatted(order));
        }

        switch (order) {
            case 0:
                return S_BESSEL_0;
            case 1:
                return S_BESSEL_1;
            default:
                return new SBesselOver2(order, S_BESSEL_0, S_BESSEL_1);
        }
    }
}
