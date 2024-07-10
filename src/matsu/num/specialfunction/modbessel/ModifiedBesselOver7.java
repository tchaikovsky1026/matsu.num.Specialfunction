/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.10
 */
package matsu.num.specialfunction.modbessel;

import matsu.num.commons.Exponentiation;

/**
 * 次数7以上の変形Bessel関数を扱う. <br>
 * 第1種変形Besselでは,
 * {@literal x <= n^2/2} は後退漸化式を, それ以上では漸近級数を用いる.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
final class ModifiedBesselOver7 extends ModifiedBesselHigherOrder {

    private final int upperN;
    private final double boundaryOfAsymptotic;

    /**
     * @param order 次数
     * @param mbessel0 mbessel0
     * @param mbessel1 mbessel1
     */
    ModifiedBesselOver7(int order, ModifiedBessel0thOrder mbessel0, ModifiedBessel1stOrder mbessel1) {
        super(order, mbessel0, mbessel1);
        if (!(order >= 7)) {
            throw new AssertionError("次数が7以上でない");
        }

        this.upperN = this.calcUpperN();
        this.boundaryOfAsymptotic = this.calcBoundaryOfAsymptotic();
    }

    @Override
    public double besselI(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }

        if (x <= 1) {
            return this.besselIByPower(x, 9);
        }

        if (x <= this.boundaryOfAsymptotic) {
            return this.besselIByBackRecursion(x, this.upperN);
        }

        return this.besselIByAsymptotic(x, 20);
    }

    /**
     * べき級数によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double besselIByPower(double x, int kMax) {

        //級数部分
        //x<<1で1である
        final double halfX = x / 2d;
        final double squareHalfX = halfX * halfX;

        double coeff = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            coeff *= squareHalfX / (k * (k + this.order));
            coeff += 1d;
        }

        //x<<1におけるI_n(x)の振る舞い
        double limit = 1;
        for (int i = 0; i < this.order; i++) {
            limit *= halfX / (i + 1);
        }

        return coeff * limit;
    }

    /**
     * 逆方向漸化式によりIを計算する.
     * 
     * @param x x
     * @param kMax べき級数の項数
     */
    private double besselIByBackRecursion(double x, int upperN) {

        final double init = 1E-280;

        double i_nu_plus_1 = 0d;
        double i_nu = init;

        for (int nu = upperN; nu > this.order; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu / x) * i_nu;

            i_nu_plus_1 = i_nu;
            i_nu = i_nu_m_1;

            if (i_nu >= 1E200) {
                i_nu_plus_1 = (i_nu_plus_1 / i_nu) * init;
                i_nu = init;
            }
        }

        i_nu_plus_1 = (i_nu_plus_1 / i_nu) * init;
        i_nu = init;

        double i_order = i_nu;

        for (int nu = this.order; nu > 1; nu--) {
            double i_nu_m_1 = i_nu_plus_1 + (2 * nu / x) * i_nu;
            if (Double.isInfinite(i_nu_m_1)) {
                return Double.POSITIVE_INFINITY;
            }

            i_nu_plus_1 = i_nu;
            i_nu = i_nu_m_1;
        }

        return this.mbessel1.besselI(x) / i_nu * i_order;
    }

    /**
     * 漸近級数によりIを計算する.
     * 
     * @param x x
     * @param kMax 漸近級数の項数
     */
    private double besselIByAsymptotic(double x, int kMax) {

        double asymptotic = Exponentiation.exp(x) / Exponentiation.sqrt(2 * Math.PI * x);
        if (!Double.isFinite(asymptotic)) {
            return Double.POSITIVE_INFINITY;
        }

        final double inv8X = 0.125 / x;

        double value = 0;
        for (int k = kMax + 1; k >= 1; k--) {
            int k2m1 = 2 * k - 1;

            value *= (double) (k2m1 * k2m1 - 4 * this.order * this.order) / k * inv8X;
            value += 1d;
        }

        return value * asymptotic;
    }

    /**
     * 逆方向漸化式におけるNを計算する
     * 
     * @return N
     */
    private int calcUpperN() {
        return (int) (4.6 * this.order) + 3;
    }

    private double calcBoundaryOfAsymptotic() {
        return this.order * this.order * 0.5;
    }

}
