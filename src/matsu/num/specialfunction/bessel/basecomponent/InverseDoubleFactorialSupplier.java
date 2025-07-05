/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2025.7.5
 */
package matsu.num.specialfunction.bessel.basecomponent;

import matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction;
import matsu.num.specialfunction.bessel.SphericalBesselFunction;

/**
 * 変形球Bessel関数の計算で使うための, 1/(2n+1)!! の計算方法を提供する.
 * 
 * @author Matsuura Y.
 */
public final class InverseDoubleFactorialSupplier {

    /**
     * 次数の下限を表す定数.
     */
    @SuppressWarnings("deprecation")
    private static final int LOWER_LIMIT_OF_ORDER = Math.min(
            SphericalBesselFunction.LOWER_LIMIT_OF_ORDER,
            ModifiedSphericalBesselFunction.LOWER_LIMIT_OF_ORDER);

    /**
     * 次数の上限を表す定数.
     */
    @SuppressWarnings("deprecation")
    private static final int UPPER_LIMIT_OF_ORDER = Math.max(
            SphericalBesselFunction.UPPER_LIMIT_OF_ORDER,
            ModifiedSphericalBesselFunction.UPPER_LIMIT_OF_ORDER);

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
     */
    public static double get(int order) {
        if (!(LOWER_LIMIT_OF_ORDER <= order
                && order <= UPPER_LIMIT_OF_ORDER)) {
            throw new AssertionError(
                    String.format("サポート外である: order = %s", order));
        }

        return INV_FACTORIALS[order];
    }
}
