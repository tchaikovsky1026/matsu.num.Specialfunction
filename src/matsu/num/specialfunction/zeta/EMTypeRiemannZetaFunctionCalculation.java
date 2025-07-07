/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.7
 */
package matsu.num.specialfunction.zeta;

/**
 * EM方式の Riemann ゼータ関数の計算.
 * 
 * <p>
 * この計算器は, Euler-Maclaurin (EM) の公式を使用してゼータ関数を計算する.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class EMTypeRiemannZetaFunctionCalculation {

    /**
     * 偶数indexのBernoulli数. <br>
     * [
     * B<sub>0</sub>, B<sub>2</sub>, B<sub>4</sub>, ... ,
     * B<sub>20</sub>
     * ]
     */
    private static final double[] EVEN_BERNOULLI = {
            1d, 0.166666666666666667d, -0.0333333333333333333d,
            0.0238095238095238095d, -0.0333333333333333333d, 0.0757575757575757576d,
            -0.253113553113553114d, 1.16666666666666667d, -7.09215686274509804d,
            54.9711779448621554d, -529.124242424242424d
    };

    /**
     * このNで分岐する. <br>
     * zeta(s) は,
     * kが1から(N-1)までを普通に加え, k = N を(1/2)倍して加え, k = Nの残差分をEM公式で加える.
     */
    private static final int N = 10;

    private static final double LN_N = Math.log(N);
    private static final double INV_SQUARE_N = 1d / (N * N);

    /**
     * 唯一のコンストラクタ
     */
    public EMTypeRiemannZetaFunctionCalculation() {
        super();
    }

    /**
     * zeta(s) を返す.
     * 
     * <p>
     * {@literal s < 1} は NaN.
     * </p>
     *
     * @param s s
     * @return zeta(s)
     */
    public double zeta(double s) {
        return this.zetam1(s) + 1d;
    }

    /**
     * zeta(s) - 1 を返す.
     * 
     * <p>
     * {@literal s < 1} は NaN.
     * </p>
     *
     * @param s s
     * @return zeta(s) - 1
     */
    public double zetam1(double s) {
        if (!(s >= 1)) {
            return Double.NaN;
        }

        double v0 = 0d;
        double v1 = 0d;
        double v2 = 0d;
        double v3 = 0d;

        // k = 2 から (N-1) までの級数の和
        int k;
        for (k = 2; k < N - 3; k += 4) {
            v0 += Math.pow(1d / k, s);
            v1 += Math.pow(1d / (k + 1), s);
            v2 += Math.pow(1d / (k + 2), s);
            v3 += Math.pow(1d / (k + 3), s);
        }
        for (; k < N; k++) {
            v0 += Math.pow(1d / k, s);
        }
        v1 += 0.5 * Math.pow(1d / N, s);

        return (v0 + v1) + (v2 + v3) + zetaN_Res(s);
    }

    private double zetaN_Res(double s) {

        double v = 0d;
        for (int k = EVEN_BERNOULLI.length - 1; k >= 0; k--) {
            double k2 = 2 * k;
            v *= (s + (k2 - 1)) * (s + k2) / ((k2 + 1) * (k2 + 2)) * INV_SQUARE_N;
            v += EVEN_BERNOULLI[k];
        }

        double sm1 = s - 1;
        return v / Math.exp((sm1) * LN_N) / (sm1);
    }
}
