/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.27
 */
package matsu.num.specialfunction.bessel.basecomponent;

import matsu.num.specialfunction.bessel.BesselFunction;
import matsu.num.specialfunction.bessel.ModifiedBesselFunction;

/**
 * 変形Bessel関数の計算で使うための, 1/n! の計算方法を提供する.
 * 
 * @author Matsuura Y.
 * @version 19.0
 */
public final class InverseFactorialSupplier {

    /**
     * 次数の下限を表す定数.
     */
    private static final int LOWER_LIMIT_OF_ORDER = Math.min(
            BesselFunction.LOWER_LIMIT_OF_ORDER,
            ModifiedBesselFunction.LOWER_LIMIT_OF_ORDER);

    /**
     * 次数の上限を表す定数.
     */
    private static final int UPPER_LIMIT_OF_ORDER = Math.max(
            BesselFunction.UPPER_LIMIT_OF_ORDER,
            ModifiedBesselFunction.UPPER_LIMIT_OF_ORDER);

    private static final double[] INV_FACTORIALS;

    static {
        INV_FACTORIALS = new double[UPPER_LIMIT_OF_ORDER + 1];
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
     */
    public static double get(int order) {
        if (!(LOWER_LIMIT_OF_ORDER <= order
                && order <= UPPER_LIMIT_OF_ORDER)) {
            throw new AssertionError(
                    String.format("次数がサポート外である: order = %s", order));
        }

        return INV_FACTORIALS[order];
    }
}
