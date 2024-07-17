/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.17
 */
package matsu.num.specialfunction.sbessel;

import static matsu.num.specialfunction.ModifiedBesselFunction.*;

/**
 * 球Bessel関数の計算で使うための, 1/(2n+1)!! の計算方法を提供する.
 * 
 * @author Matsuura Y.
 * @version 18.7
 */
final class InverseDoubleFactorialSupplier {

    private static final double[] INV_FACTORIALS;

    static {
        INV_FACTORIALS = new double[UPPER_LIMIT_OF_ORDER + 1];
        INV_FACTORIALS[0] = 1;
        for (int j = 1; j <= UPPER_LIMIT_OF_ORDER; j++) {
            INV_FACTORIALS[j] = INV_FACTORIALS[j - 1] / (2 * j + 1);
        }
        if (INV_FACTORIALS[UPPER_LIMIT_OF_ORDER] == 0d) {
            throw new AssertionError("二重階乗がアンダーフロー");
        }
    }

    private InverseDoubleFactorialSupplier() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えられた次数 n に対して, 1/(2n+1)!! の値を返す.
     * 
     * @param order 次数, n
     * @return 1/(2n+1)!!
     * @throws IllegalArgumentException 引数がサポート外の場合
     */
    public static double get(int order) {
        if (!SBesselFunctionFactory.acceptsParameter(order)) {
            throw new IllegalArgumentException(
                    String.format("次数がサポート外である: order = %s", order));
        }

        return INV_FACTORIALS[order];
    }

}
