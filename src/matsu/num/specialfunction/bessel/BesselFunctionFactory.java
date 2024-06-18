/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.bessel;

import matsu.num.specialfunction.BesselFunction;

/**
 * Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
 * @version 18.0
 */
public final class BesselFunctionFactory {

    /**
     * 次数の上限(含む).
     */
    private static final int UPPER_LIMIT_OF_ORDER = 100;

    private BesselFunctionFactory() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 指定した次数のBessel関数計算インスタンスを返す.
     * 
     * <p>
     * サポートされている次数は {@code 0 <= order <= 100} である.
     * </p>
     *
     * @param order n, 次数
     * @return n次のBessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static BesselFunction instanceOf(int order) {
        if (!(0 <= order && order <= UPPER_LIMIT_OF_ORDER)) {
            throw new IllegalArgumentException("次数が不適である");
        }
        switch (order) {
        case 0:
            return Bessel0thOrder.instance();
        case 1:
            return Bessel1stOrder.instance();
        default:
            return new BesselHigherOrder(order);
        }
    }
}
