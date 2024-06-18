/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.icbeta;

/**
 * 不完全ベータ関数の計算における連分数ファクターを扱う.
 * 
 * @author Matsuura Y.
 * @version 18.0
 */
final class ICBContinuedFractionFactor {

    private static final int MIN_ROOP_FOR_F = 15;
    private static final int MAX_ROOP_FOR_F = 70;
    private static final int CHECK_INTERVAL_FOR_F = 5;

    private static final double EPSILON_A_FOR_FACTOR_F = 1E-100;
    private static final double EPSILON_R_FOR_FACTOR_F = 1E-14;

    private ICBContinuedFractionFactor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 不完全ベータ関数B(a,b,x)を <br>
     * B(a,b,x) = x^a (1-x)^b F(a,b,x) <br>
     * と表すと, F(a,b,x)は連分数で計算できる. <br>
     * ただし, <br>
     * {@literal x < a/(a+b) (a or b = O(1)) } <br>
     * {@literal x << a/(a+b) (a and b >> 1) } <br>
     * で使用可能.
     * 
     * @param x
     * @param a
     * @param b
     * @return F(a,b,x)
     */
    public static double factorLowerSide(double x, double a, double b) {
        final double amp = 1E-100;
        final double minLimitAbsB = 1E-200;
        final double maxLimitAbsB = 1E+200;
        final double apb = a + b;

        double a0, b0, a1, b1, a2, b2, a3, b3;
        double q = a;
        double p = 1;
        a0 = 0;
        a1 = p * amp;
        b0 = amp;
        b1 = q * amp;
        int n = 0;
        int qCount = 0;
        double f0;
        double f1;
        while (n < MAX_ROOP_FOR_F) {
            for (int i = 0; i < CHECK_INTERVAL_FOR_F; i++) {
                double absB = Math.abs(b1);
                if (absB < minLimitAbsB) {
                    a0 *= maxLimitAbsB;
                    a1 *= maxLimitAbsB;
                    b0 *= maxLimitAbsB;
                    b1 *= maxLimitAbsB;
                } else if (absB > maxLimitAbsB) {
                    a0 *= minLimitAbsB;
                    a1 *= minLimitAbsB;
                    b0 *= minLimitAbsB;
                    b1 *= minLimitAbsB;
                }

                qCount++;
                q = a + qCount;
                p = -(a + n) * (apb + n) * x;
                a2 = q * a1 + p * a0;
                b2 = q * b1 + p * b0;

                qCount++;
                q = a + qCount;
                p = (n + 1) * (b - (n + 1)) * x;
                a3 = q * a2 + p * a1;
                b3 = q * b2 + p * b1;

                a0 = a2;
                b0 = b2;
                a1 = a3;
                b1 = b3;
                n++;
            }
            if (n < MIN_ROOP_FOR_F) {
                continue;
            }
            f0 = a0 / b0;
            f1 = a1 / b1;
            if (Math.abs(f1 - f0) < EPSILON_A_FOR_FACTOR_F + Math.abs(f1) * EPSILON_R_FOR_FACTOR_F) {
                return f1;
            }
        }
        return a1 / b1;
    }

}
