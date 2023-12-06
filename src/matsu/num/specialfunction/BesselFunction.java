/*
 * 2023.12.5
 */
package matsu.num.specialfunction;

import matsu.num.specialfunction.bessel.BesselFunctionFactory;

/**
 * Bessel関数
 * (<i>J<sub>n</sub></i>(<i>x</i>),<i>Y<sub>n</sub></i>(<i>x</i>))
 * の計算
 * (おおよそ倍精度).
 *
 * @author Matsuura Y.
 * @version 17.0
 */
public interface BesselFunction {

    /**
     * このインスタンスの扱うBessel関数の次数を返す.
     *
     * @return 次数
     */
    public int order();

    /**
     * 第1種Bessel関数 <i>J<sub>n</sub></i>(<i>x</i>)を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return J<sub>n</sub>(x)
     */
    public double besselJ(double x);

    /**
     * 第2種Bessel関数 <i>Y<sub>n</sub></i>(<i>x</i>)を返す.
     *
     * <ul>
     * <li><i>x</i> &lt; 0 &rarr; NaN.
     * </li>
     * <li><i>x</i> &asymp; 0 &rarr; -&infin;.</li>
     * <li><i>x</i> &asymp; +&infin; &rarr; 0.</li>
     * </ul>
     *
     * @param x x, 引数
     * @return Y<sub>n</sub>(x)
     */
    public double besselY(double x);

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
        return BesselFunctionFactory.instanceOf(order);
    }
}