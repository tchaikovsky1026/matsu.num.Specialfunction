/**
 * 2023.3.21
 */
package matsu.num.specialfunction.bessel;

import matsu.num.specialfunction.BesselFunction;

/**
 * Bessel関数のファクトリ.
 *
 * @author Matsuura Y.
 * @version 11.0
 */
public final class BesselFunctionFactory {

    private BesselFunctionFactory() {
        throw new AssertionError();
    }

    /**
     * 指定した次数のBessel関数計算インスタンスを返す. <br>
     * 次数<i>n</i>は, 0 &le; <i>n</i> &le; 100 でなければならない.
     *
     * @param order n, 次数
     * @return n次のBessel関数を計算するインスタンス
     * @throws IllegalArgumentException 引数がサポート外の場合
     */
    public static BesselFunction instanceOf(int order) {
        switch (order) {
        case 0:
            return Bessel0thOrder.instance();
        case 1:
            return Bessel1stOrder.instance();
        default:
            return BesselHigherOrder.instanceOf(order);
        }
    }

}
