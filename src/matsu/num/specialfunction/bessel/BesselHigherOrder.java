/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.17
 */
package matsu.num.specialfunction.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.BesselFunction;

/**
 * 高次のBessel関数, 2から100次までのベッセル関数をサポートする.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
final class BesselHigherOrder extends SkeletalBessel implements BesselFunction {

    private static final double[] invFactorial;

    static {
        invFactorial = new double[101];
        invFactorial[0] = 1;
        for (int j = 1; j <= 100; j++) {
            invFactorial[j] = invFactorial[j - 1] / j;
        }
    }

    /**
     * このBessel関数の次数.
     */
    private final int order_n;

    /**
     * 1/n!の値.
     */
    private final double invOfNFactorial;

    /**
     * {@literal x < 2} の計算はべき級数で行うが, その時に使う項のindex値.<br>
     * 第12項までを計算する, おそらく過剰.
     */
    private final int jMaxInPower = 12;

    /**
     * {@literal 2 <= x < n} では逆方向漸化式を使う.
     * その時のNの値. <br>
     * Nはxに依存したほうが効率は良さそうだが, 今は最悪の場合: {@literal x = n}
     * について考慮し, ｘに依存させない.
     */
    private final int capitalN_inReverseRecursion;

    private final BesselFunction bessel0;
    private final BesselFunction bessel1;

    /**
     * 与えた次数のベッセル関数を生成する.
     * 
     * @param n 次数
     * @param bessel0 0次ベッセル
     * @param bessel1 1次ベッセル
     */
    BesselHigherOrder(int n, BesselFunction bessel0, BesselFunction bessel1) {
        this.order_n = n;
        this.invOfNFactorial = invFactorial[n];
        this.bessel0 = bessel0;
        this.bessel1 = bessel1;
        this.capitalN_inReverseRecursion = this.calcNMin();
    }

    @Override
    public int order() {
        return this.order_n;
    }

    @Override
    public final double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < 2) {
            //Taylor級数から求める

            double halfX = x / 2;
            double minusSquareHalfX = -halfX * halfX;

            //jMax+1からスタート(最初の項は0で考慮されないため)
            double value = 0;
            for (int j = this.jMaxInPower + 1; j >= 1; j--) {
                value *= minusSquareHalfX / (j * (j + order_n));
                value += 1;
            }

            return Exponentiation.pow(halfX, order_n) * invOfNFactorial * value;
        }

        if (x < order_n) {
            //逆方向の漸化式から求める

            int N = getNmin();
            double j2 = 0;
            double j1 = 1E-300;
            double doubleInvX = 2 / x;
            for (int j = N; j > order_n; j--) {
                double j0 = j1 * j * doubleInvX - j2;
                j2 = j1;
                j1 = j0;
            }
            double jn = j1;
            for (int j = order_n; j > 0; j--) {
                double j0 = j1 * j * doubleInvX - j2;
                if (!Double.isFinite(j0)) {
                    return 0d;
                }
                j2 = j1;
                j1 = j0;
            }
            double absJ1 = Math.abs(j1);
            double absJ2 = Math.abs(j2);
            if (absJ2 > absJ1) {
                return jn / j2 * this.bessel1.besselJ(x);
            } else {
                return jn / j1 * this.bessel0.besselJ(x);
            }
        }

        //順方向の漸化式から求める
        double j0 = this.bessel0.besselJ(x);
        double j1 = this.bessel1.besselJ(x);

        double doubleInvX = 2 / x;
        for (int j = 1; j < order_n; j++) {
            double j2 = j1 * j * doubleInvX - j0;
            j0 = j1;
            j1 = j2;
        }
        return j1;
    }

    private int getNmin() {
        return this.capitalN_inReverseRecursion;
    }

    private int calcNMin() {
        //n<=100, 倍精度に特化した数式
        //N = n + 8log(n) + 3
        return order_n + 1 + (int) (8 * Exponentiation.log(order_n + 3));
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        double y0 = this.bessel0.besselY(x);
        double y1 = this.bessel1.besselY(x);

        double doubleInvX = 2 / x;
        for (int j = 1; j < order_n; j++) {
            double y2 = y1 * j * doubleInvX - y0;
            if (!Double.isFinite(y2)) {
                return Double.NEGATIVE_INFINITY;
            }
            y0 = y1;
            y1 = y2;
        }
        return y1;
    }

}
