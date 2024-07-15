/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.14
 */
package matsu.num.specialfunction.modbessel.subpj.kasymptotic;

import java.util.function.IntFunction;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.fraction.BigRationalElement;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;
import matsu.num.specialfunction.fraction.RationalType;
import matsu.num.specialfunction.modbessel.subpj.RawCoefficientCalculableFunction;

/**
 * <p>
 * K0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} を引数とする形で計算する. <br>
 * K0(x) = sqrt(pi/(2x)) * exp(-x) * F(t) <br>
 * として, F(t) を推定する. <br>
 * F(0)からF(1/16)までで2割程度しか変化しない. <br>
 * よって, スケールは1とする.
 * </p>
 * 
 * <p>
 * F(t)の第3次近似をf(t)とする. <br>
 * f(t) = 1 - (1^2 /1!)t + (1^2 * 3^2 /2!)t^2 - (1^2 * 3^2 * 5^2 /3!)t^3
 * </p>
 * 
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
final class BesselK0Asymptotic implements RawCoefficientCalculableFunction {

    private static final DoubleContinuedFractionFunction K0_UPPER4 =
            k0_upper4().asDoubleFunction();

    private static final double MIN_T = 0d;
    private static final double MAX_T = 1d / 16;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d,
            -1d,
            (3 * 3) / 2d,
            -(15 * 15) / 6d
    };

    public BesselK0Asymptotic(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_T <= interval.lower() &&
                interval.upper() <= MAX_T)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    public double value(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        double v = K0_UPPER4.value(t);
        v *= 3 * 3;
        v *= 5 * 5;
        v *= 7 * 7;
        v /= 24;
        v *= Exponentiation.pow(t, 4);

        return v;
    }

    @Override
    public double scale(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        return 1d;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F(t) - f(t)] の近似多項式の係数を与え,
     * F(t) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F(t) - f(t)]の係数
     * @return F(t)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffF = thisCoeff.clone();
        for (int i = 0;
                i < coeffF.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffF[i] += ESTIMATED_COEFF[i];
        }

        return coeffF;
    }

    /**
     * K0の漸近連分数の {@literal m >= 4}でのもの. <br>
     * 4次が定数項1に対応するため, 実際は全体に, <br>
     * (1^2 * 3^2 * 5^2 * 7^2 /4!)t^4
     * <br>
     * が乗算される.
     */
    private static ContinuedFractionFunction<RationalType, BigRationalElement> k0_upper4() {

        final int kMax = 60;

        IntFunction<BigRationalElement> func =
                k -> BigRationalElement.of(-((2 * k + 9) * (2 * k + 9)), k + 5);

        return ContinuedFractionFunction.from(
                kMax,
                RationalType.INSTANCE, func,
                BigRationalElement.ConstantSupplier.INSTANCE);
    }

}
