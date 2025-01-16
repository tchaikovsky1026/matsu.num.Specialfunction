/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.24
 */
package matsu.num.specialfunction.bessel.bessel;

import static matsu.num.specialfunction.bessel.BesselFunction.*;

import matsu.num.specialfunction.bessel.BesselFunction;

/**
 * Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
 */
public final class BesselFunctionFactory {

    private static final Bessel0th BESSEL_0 = new Bessel0Optimized();
    private static final Bessel1st BESSEL_1 = new Bessel1Optimized();

    /**
     * Higher orderを生成する閾値.
     */
    static final int LOWER_LIMIT_OF_HIGHER_ORDER = 2;

    private BesselFunctionFactory() {
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
     * 指定した次数のBessel関数計算インスタンスを返す.
     *
     * @param order n, 次数
     * @return n次のBessel関数を計算するインスタンス
     * @throws IllegalArgumentException 次数がサポート外の場合
     */
    public static BesselFunction instanceOf(int order) {
        if (!acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    String.format("次数がサポート外である: order = %s", order));
        }

        switch (order) {
        case 0:
            return BESSEL_0;
        case 1:
            return BESSEL_1;
        default:
            return new BesselOver2(order, BESSEL_0, BESSEL_1);
        }
    }
}
