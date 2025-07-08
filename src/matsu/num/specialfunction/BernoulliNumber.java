/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/*
 * 2025.7.8
 */
package matsu.num.specialfunction;

/**
 * Bernoulli 数の計算 (およそ倍精度).
 * 
 * <p>
 * Bernoulli 数 <i>B</i><sub><i>k</i></sub> は,
 * <i>k</i> &ge; 0 について定義され,
 * <i>k</i> が3以上の奇数の場合は
 * <i>B</i><sub><i>k</i></sub> = 0
 * となる. <br>
 * <i>B</i><sub><i>k</i></sub> は有理数であるが,
 * このクラスでは倍精度浮動小数点数として扱う.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class BernoulliNumber {

    /**
     * 小さいindexに対する偶数BernoulliNumber. <br>
     * B_0, B_2, ...
     */
    private static final double[] EVEN_BERNOULLI;

    /**
     * (2pi)^1, (2pi)^2, (2pi)^4, ..., (2pi)^256
     */
    private static final double[] PI_POWER2 = {
            6.283185307179586476925,
            39.47841760435743447534,
            1558.545456544038995783,
            2429063.940114066945825,
            5900351625162.475409711,
            3.481414930055746472045E+25,
            1.212024991521505810015E+51,
            1.469004580072706230652E+102,
            2.157974456274587971649E+204
    };

    /**
     * <i>B</i><sub>1</sub> の値.
     */
    private static final double BERNOULLI_1 = -0.5;

    static {
        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        for(k = 0; k <= 20; k = k + 1){
        //            println(k, bernoulli(2*k));
        //        }
        /* ------------------------------------ */

        EVEN_BERNOULLI = new double[] {
                1,
                0.1666666666666666666667,
                -0.03333333333333333333333,
                0.02380952380952380952381,
                -0.03333333333333333333333,
                0.07575757575757575757576,
                -0.2531135531135531135531,
                1.166666666666666666667,
                -7.092156862745098039216,
                54.97117794486215538847,
                -529.1242424242424242424,
                6192.123188405797101449,
                -86580.25311355311355311,
                1425517.166666666666667,
                -27298231.06781609195402,
                601580873.9006423683843,
                -15116315767.09215686275,
                429614643061.1666666667,
                -13711655205088.33277216,
                488332318973593.1666667,
                -19296579341940068.14863
        };
    }

    private BernoulliNumber() {
        // インスタンス化不可
        throw new AssertionError();
    }

    /**
     * <i>B</i><sub><i>k</i></sub> の値を返す.
     * 
     * <p>
     * <i>k</i> &lt; 0 の場合は {@link Double#NaN} を返す.
     * </p>
     * 
     * @param k <i>k</i>
     * @return <i>B</i><sub><i>k</i></sub>
     */
    public static double of(int k) {
        if (k < 0) {
            return Double.NaN;
        }

        if ((k & 1) == 0) {
            int halfOfK = k >> 1;
            return even(halfOfK);
        }

        return k == 1
                ? BERNOULLI_1
                : 0d;
    }

    /**
     * B_{2n} の値を返す. <br>
     * 引数は0以上でなければならない.
     * 
     * <p>
     * バリデーションを行わないので呼び出し元でチェック.
     * </p>
     * 
     * @param halfOfIndex n の値
     * @return B_{2n}
     */
    private static double even(int halfOfIndex) {
        if (halfOfIndex < EVEN_BERNOULLI.length) {
            return EVEN_BERNOULLI[halfOfIndex];
        }

        if (halfOfIndex > 150) {
            return (halfOfIndex & 1) == 1
                    ? Double.POSITIVE_INFINITY
                    : Double.NEGATIVE_INFINITY;
        }

        /*
         * B_{2n} = (-1)^{n+1}*2*zeta(2n)*(2n)!/(2pi)^{2n}
         */
        double abs = 2 * RiemannZetaFunction.zeta(halfOfIndex * 2) *
                fact2n_divBy_2piPow2n(halfOfIndex);

        return (halfOfIndex & 1) == 1
                ? abs
                : -abs;
    }

    /**
     * @param n n
     * @return (2n)!/(2pi)^(2n)
     */
    private static double fact2n_divBy_2piPow2n(int n) {

        //2nは最大で300
        double v0 = 1;
        double v1 = 1;
        double v2 = 1;
        double v3 = 1;

        int i = 1;
        for (int len = 2 * n - 3; i <= len; i += 4) {
            v0 *= i;
            v1 *= i + 1;
            v2 *= i + 2;
            v3 *= i + 3;
        }
        for (int len = 2 * n; i <= len; i++) {
            v0 *= i;
        }

        return (((inv2piPow2n(n) * v0) * v1) * v2) * v3;
    }

    /**
     * @param n n
     * @return 1/(2pi)^(2n)
     */
    private static double inv2piPow2n(int n) {

        //2nは最大で300
        int i = 0;
        int n2 = 2 * n;
        double v = 1d;
        while (n2 > 0) {
            if ((n2 & 1) == 1) {
                v *= PI_POWER2[i];
            }
            i++;
            n2 >>= 1;
        }

        return 1d / v;
    }
}
