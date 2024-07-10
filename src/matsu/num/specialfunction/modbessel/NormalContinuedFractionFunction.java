/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.9
 */
package matsu.num.specialfunction.modbessel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/**
 * 連分数関数を扱うクラス.
 * 
 * @author Matsuura Y.
 * @version 18.2
 */
final class NormalContinuedFractionFunction {

    private final double[] cfCoeff;

    /**
     * べき級数の係数が, <br>
     * [1, c_0, c_0c_1, c_0c_1c_2, ...] <br>
     * であるときの [c_0, c_1, ...] を渡すことで, 等価な連分数表現を計算する仕組みを生成する.
     * 
     * @param powerCoeffRatio [c_0, c_1, ...]
     * @throws IllegalArgumentException 展開に失敗
     * @throws NullPointerException null
     */
    NormalContinuedFractionFunction(BigDecimal[] powerCoeffRatio) {
        if (powerCoeffRatio.length == 0) {
            throw new IllegalArgumentException("サイズ0である");
        }

        try {
            this.cfCoeff = calcCoeffOfContinuedFraction(powerCoeffRatio);
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("無限大またはNaNが含まれる");
        } catch (ArithmeticException nfe) {
            throw new IllegalArgumentException("連分数展開が破綻する");
        }
    }

    /**
     * べき級数の係数が, <br>
     * [1, c_0, c_0c_1, c_0c_1c_2, ...] <br>
     * であるときの [c_0, c_1, ...] を渡すことで, 等価な連分数表現を計算する仕組みを生成する.
     * 
     * @param size size
     * @param numeratorSupplier [c_0, c_1, ...]の分子
     * @param denominatorSupplier [c_0, c_1, ...]の分母
     * @throws IllegalArgumentException 展開に失敗
     * @throws NullPointerException null
     */
    NormalContinuedFractionFunction(int size, IntUnaryOperator numeratorSupplier,
            IntUnaryOperator denominatorSupplier) {
        this(
                calcPowerCoeff(
                        size,
                        intFuncToBigDecimalFunc(numeratorSupplier, denominatorSupplier)));
    }

    /**
     * べき級数の係数が, <br>
     * [1, c_0, c_0c_1, c_0c_1c_2, ...] <br>
     * であるときの [c_0, c_1, ...] を渡すことで, 等価な連分数表現を計算する仕組みを生成する.
     * 
     * @param size
     * @param ratioSupplier [c_0, c_1, ...]
     * @throws IllegalArgumentException 展開に失敗
     * @throws NullPointerException null
     */
    NormalContinuedFractionFunction(int size, IntFunction<BigDecimal> ratioSupplier) {
        this(calcPowerCoeff(size, ratioSupplier));
    }

    /**
     * 連分数から値を計算する.
     * 
     * @param t t
     * @return 値
     */
    double value(double t) {

        double value = 1;
        for (int k = this.cfCoeff.length - 1; k >= 0; k--) {
            value = (this.cfCoeff[k] * t) / value;
            value += 1;
        }
        return 1 / value;
    }

    /**
     * 連分数の係数を返す.
     * ただし, 最初の1は含まず.
     */
    double[] coeffOfContinuedFraction() {
        return this.cfCoeff.clone();
    }

    private static double[] calcCoeffOfContinuedFraction(BigDecimal[] powerCoeffRatio) {
        int tableSize = powerCoeffRatio.length + 1;
        BigDecimal[][] table = new BigDecimal[tableSize][tableSize];

        for (int j = 0; j < tableSize; j++) {
            table[0][j] = BigDecimal.ZERO;
        }
        for (int j = 0; j < tableSize - 1; j++) {
            table[1][j] = powerCoeffRatio[j];
        }

        int step = 2;
        while (step < tableSize) {
            //q -> e
            for (int j = 0; j < tableSize - step; j++) {
                table[step][j] = table[step - 2][j + 1].add(
                        table[step - 1][j + 1].subtract(table[step - 1][j]));
            }
            step++;

            //e -> q
            //もしq->eが最後なら, このループは回らないため問題ない
            for (int j = 0; j < tableSize - step; j++) {
                table[step][j] = table[step - 2][j + 1].multiply(
                        table[step - 1][j + 1]
                                .divide(table[step - 1][j], MathContext.DECIMAL128));
            }

            step++;
        }

        //商差法の結果
        double[] out = new double[tableSize - 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = -table[i + 1][0].doubleValue();
        }

        return out;
    }

    private static BigDecimal[] calcPowerCoeff(
            int size, IntFunction<BigDecimal> ratioSupplier) {
        BigDecimal[] powerCoeffRatio = new BigDecimal[size];
        for (int i = 0; i < size; i++) {
            powerCoeffRatio[i] = ratioSupplier.apply(i);
        }

        return powerCoeffRatio;
    }

    private static IntFunction<BigDecimal> intFuncToBigDecimalFunc(
            IntUnaryOperator numeratorSupplier,
            IntUnaryOperator denominatorSupplier) {

        return k -> new BigDecimal(numeratorSupplier.applyAsInt(k))
                .divide(
                        new BigDecimal(denominatorSupplier.applyAsInt(k)),
                        MathContext.DECIMAL128);
    }
}
