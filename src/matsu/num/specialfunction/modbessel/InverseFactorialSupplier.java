/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.10
 */
package matsu.num.specialfunction.modbessel;

import matsu.num.specialfunction.ModifiedBesselFunction;

/**
 * 変形Bessel関数の計算で使うための, 1/n! の計算方法を提供する.
 * 
 * @author Matsuura Y.
 * @version 18.4
 */
final class InverseFactorialSupplier {

    private static final double[] INV_FACTORIALS;

    static {
        INV_FACTORIALS = new double[ModifiedBesselFunction.UPPER_LIMIT_OF_ORDER + 1];
        INV_FACTORIALS[0] = 1;
        for (int j = 1; j <= ModifiedBesselFunction.UPPER_LIMIT_OF_ORDER; j++) {
            INV_FACTORIALS[j] = INV_FACTORIALS[j - 1] / j;
        }
    }

    private InverseFactorialSupplier() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられた次数 n に対して, 1/n! の値を返す.
     * 
     * @param order 次数, n
     * @return 1/n!
     * @throws IllegalArgumentException 引数がサポート外の場合
     */
    public static double get(int order) {
        if (!ModifiedBesselFunctionFactory.acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    String.format("次数がサポート外である: order = %s", order));
        }

        return INV_FACTORIALS[order];
    }

}
