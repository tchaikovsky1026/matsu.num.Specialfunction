/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.4.4
 */
package matsu.num.specialfunction.icgamma;

/**
 * 不完全ガンマ関数の計算における連分数ファクターを扱う.
 * 
 * @author Matsuura Y.
 */
final class ICGContinuedFractionFactor {

    private static final int MIN_ROOP_FOR_F = 10;
    private static final int MAX_ROOP_FOR_F = 70;
    private static final int CHECK_INTERVAL_FOR_F = 10;

    private static final double EPSILON_A_FOR_FACTOR_F = 1E-30;
    private static final double EPSILON_R_FOR_FACTOR_F = 1E-12;

    private ICGContinuedFractionFactor() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * (正則化していない)不完全ガンマ関数γ(a,x)を求めるとき,
     * γ(a,x) = [x^a e^{-x}/a ] * f(a,x) とすると,
     * f(a,x)は連分数で計算できる.
     * そのf(a,x)を返す.
     * 
     * @param x
     * @param a
     * @return f(a,x)
     */
    public static double factorLCP(double x, double a) {
        final double amp = 1;
        final double minLimitAbsB = 1E-200;
        final double maxLimitAbsB = 1E+200;
        double invA = 1 / a;
        double xOverA = x / a;

        double a0, b0, a1, b1, a2, b2, a3, b3;
        double q = 1;
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
                q = 1 + qCount * invA;
                p = -(1 + n * invA) * xOverA;
                a2 = q * a1 + p * a0;
                b2 = q * b1 + p * b0;

                qCount++;
                q = 1 + qCount * invA;
                p = (n + 1) * invA * xOverA;
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

    /**
     * (正則化していない)不完全ガンマ関数Γ(a,x)を求めるとき,
     * Γ(a,x) = [x^(a-1) e^{-x}] * F(a,x) とすると,
     * F(a,x)は連分数で計算できる.
     * そのF(a,x)を返す.
     * 
     * @param x
     * @param a
     * @return F(a,x)
     */
    public static double factorUCP(double x, double a) {
        final double amp = 1;
        final double minLimitAbsB = 1E-200;
        final double maxLimitAbsB = 1E+200;

        double aOverX = a / x;
        double invA = 1 / a;

        double a0, b0, a1, b1, a2, b2, a3, b3;
        a0 = 0;
        a1 = amp;
        b0 = amp;
        b1 = amp;
        int n = 0;
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

                double p1 = -(1 - (n + 1) * invA) * aOverX;
                a2 = a1 + p1 * a0;
                b2 = b1 + p1 * b0;
                double p2 = (n + 1) * invA * aOverX;
                a3 = a2 + p2 * a1;
                b3 = b2 + p2 * b1;
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
