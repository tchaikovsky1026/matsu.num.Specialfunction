/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.common;

/**
 * 指数関数の計算を扱う.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
public final class Exponentiation {
    private Exponentiation() {
        //インスタンス化不可
        throw new AssertionError();
    }

    public static double exp(double x) {
        return Math.exp(x);
    }

    public static double expm1(double x) {
        return Math.expm1(x);
    }

    public static double log(double x) {
        return Math.log(x);
    }

    public static double log1p(double x) {
        return Math.log1p(x);
    }

    public static double sqrt(double x) {
        return Math.sqrt(x);
    }

    /**
     * x^n を返す.
     * 
     * @param x x
     * @param n n, 0以上でなければならない
     * @return x^n
     * @throws IllegalArgumentException n>=0 でない場合
     */
    public static double pow(double x, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0");
        }

        /*
         * 指数をビット解析して,
         * x^(2^k) の積として表現する.
         * xが0やinfであっても問題なく処理される.
         */
        int np = n;
        double xp = x;
        double value = 1d;
        while (np != 0) {
            if ((np & 0x1) == 1) {
                value *= xp;
            }
            xp = xp * xp;
            np = np >>> 1;
        }
        return value;
    }
}
