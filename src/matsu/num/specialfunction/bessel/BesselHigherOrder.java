/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.6.17
 */
package matsu.num.specialfunction.bessel;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.BesselFunction;

/**
 * 高次のBessel関数, 2から100次までのベッセル関数をサポートする.
 * 
 * @author Matsuura Y.
 * @version 18.1
 */
final class BesselHigherOrder implements BesselFunction {

    private static final double[] invFactorial;

    static {
        invFactorial = new double[101];
        invFactorial[0] = 1;
        for (int j = 1; j <= 100; j++) {
            invFactorial[j] = invFactorial[j - 1] / j;
        }
    }

    private final int n;
    private final double invOfNFactorial;

    /**
     * 与えた次数のベッセル関数を生成する.
     * 
     * @param n 次数
     */
    BesselHigherOrder(int n) {
        this.n = n;
        this.invOfNFactorial = invFactorial[n];
    }

    @Override
    public int order() {
        return this.n;
    }

    @Override
    public final double besselJ(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x < 1) {
            //Taylor級数から求める
            double halfX = x / 2;
            double minusSquareHalfX = -halfX * halfX;

            int jMax = 7;
            double value = 0;
            for (int j = jMax; j >= 1; j--) {
                value += 1;
                value *= minusSquareHalfX / (j * (j + n));
            }
            value += 1;

            return Exponentiation.pow(halfX, n) * invOfNFactorial * value;
        } else if (x < n) {
            double t = x / n;
            int N = getNmin(n, t);
            double j2 = 0;
            double j1 = 1E-200;
            double doubleInvX = 2 / x;
            for (int j = N; j > n; j--) {
                double j0 = j1 * j * doubleInvX - j2;
                j2 = j1;
                j1 = j0;
            }
            double jn = j1;
            for (int j = n; j > 0; j--) {
                double j0 = j1 * j * doubleInvX - j2;
                if (Double.isInfinite(j0)) {
                    return 0;
                }
                j2 = j1;
                j1 = j0;
            }
            double absJ1 = Math.abs(j1);
            double absJ2 = Math.abs(j2);
            if (absJ2 > absJ1) {
                return jn / j2 * Bessel1stOrder.instance().besselJ(x);
            } else {
                return jn / j1 * Bessel0thOrder.instance().besselJ(x);
            }
        } else {
            double j0 = Bessel0thOrder.instance().besselJ(x);
            double j1 = Bessel1stOrder.instance().besselJ(x);

            double doubleInvX = 2 / x;
            for (int j = 1; j < n; j++) {
                double j2 = j1 * j * doubleInvX - j0;
                j0 = j1;
                j1 = j2;
            }
            return j1;
        }
    }

    private int getNmin(int n, double t) {
        if (n < 10) {
            if (t <= 0.5) {
                if (t <= 0.25) {
                    return n + 8;
                } else {
                    return 13 * n / 10 + 9;
                }
            } else {
                if (t <= 0.75) {
                    return 16 * n / 10 + 9;
                } else {
                    return 18 * n / 10 + 10;
                }
            }
        } else {
            if (t <= 0.5) {
                if (t <= 0.25) {
                    return n + 9;
                } else {
                    return 102 * n / 100 + 13;
                }
            } else {
                if (t <= 0.75) {
                    return 107 * n / 100 + 16;
                } else {
                    return 125 * n / 100 + 17;
                }
            }
        }
    }

    @Override
    public double besselY(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        double y0 = Bessel0thOrder.instance().besselY(x);
        double y1 = Bessel1stOrder.instance().besselY(x);

        double doubleInvX = 2 / x;
        for (int j = 1; j < n; j++) {
            double y2 = y1 * j * doubleInvX - y0;
            if (!Double.isFinite(y2)) {
                return Double.NEGATIVE_INFINITY;
            }
            y0 = y1;
            y1 = y2;
        }
        return y1;
    }

    @Override
    public String toString() {
        return BesselFunctionToString.toString(this);
    }
}
