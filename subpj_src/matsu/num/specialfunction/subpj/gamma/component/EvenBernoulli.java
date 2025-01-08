/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.component;

/**
 * 偶数次のBernoulli数 <i>B</i><sub>2<i>m</i></sub> を扱う.
 * 
 * @author Matsuura Y.
 */
public final class EvenBernoulli {

    /**
     * 偶数indexのBernoulli数. <br>
     * [
     * B<sub>0</sub>, B<sub>2</sub>, B<sub>4</sub>, ... ,
     * B<sub>30</sub>
     * ]
     */
    private static final double[] BERNOULLI = {
            1d, 0.166666666666666667d, -0.0333333333333333333d,
            0.0238095238095238095d, -0.0333333333333333333d, 0.0757575757575757576d,
            -0.253113553113553114d, 1.16666666666666667d, -7.09215686274509804d,
            54.9711779448621554d, -529.124242424242424d, 6192.1231884057971d,
            -86580.2531135531136d, 1425517.16666666667d, -27298231.067816092d,
            601580873.900642368d, -15116315767.0921569d, 429614643061.166667d,
            -13711655205088.3328d, 488332318973593.167d, -19296579341940068.2d,
            841693047573682615d, -4.03380718540594554E+19d, 2.11507486380819916E+21d,
            -1.20866265222965259E+23d, 7.50086674607696437E+24d, -5.03877810148106891E+26d,
            3.65287764848181233E+28d, -2.84987693024508822E+30d, 2.38654274996836276E+32d,
            -2.13999492572253337E+34d
    };

    /**
     * サポートしているmの最小値.
     */
    public static final int MIN_M = 0;

    /**
     * サポートしているmの最大値.
     */
    public static final int MAX_M = BERNOULLI.length - 1;

    /**
     * mがサポートされているかを判定する.
     * 
     * @param m m
     * @return サポートされているならtrue
     */
    public static boolean accepts(int m) {
        return m >= MIN_M && m <= MAX_M;
    }

    /**
     * <i>m</i> を与え, 偶数次のBernoulli数 <i>B</i><sub>2<i>m</i></sub> を返す.
     * 
     * @param m <i>m</i>
     * @return <i>B</i><sub>2<i>m</i></sub>
     * @throws IllegalArgumentException <i>m</i> がサポート外
     */
    public static double evenBernoulli(int m) {
        if (!accepts(m)) {
            throw new IllegalArgumentException("サポート外のm");
        }
        return BERNOULLI[m];
    }
}
