/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.29
 */
package matsu.num.specialfunction.fraction;

/**
 * 複素数にも対応した連分数関数. <br>
 * {@link DoubleContinuedFractionFunction}
 * の引数を複素数に拡張したものであり, すなわち係数は実数である.
 * 
 * @author Matsuura Y.
 * @version 19.1
 * @see DoubleContinuedFractionFunction
 */
public final class ComplexContinuedFractionFunction {

    private final DoubleContinuedFractionFunction src;
    private final ComplexNumber[] cfCoeff;

    /**
     * 与えたdouble連分数を, 引数がcomplexに対応するように変換する.
     * 
     * @param src 元となるdouble連分数
     */
    public ComplexContinuedFractionFunction(DoubleContinuedFractionFunction src) {
        this.src = src;
        double[] srcCoeffs = src.coeffOfContinuedFraction();

        this.cfCoeff = new ComplexNumber[srcCoeffs.length];
        for (int i = 0; i < srcCoeffs.length; i++) {
            this.cfCoeff[i] = ComplexNumber.ofReal(srcCoeffs[i]);
        }
    }

    /**
     * 連分数から値を計算する.
     * 
     * @param t t
     * @return 値
     */
    public ComplexNumber value(ComplexNumber t) {

        ComplexNumber value = ComplexNumber.ONE;

        for (int i = this.cfCoeff.length - 1; i >= 0; i--) {
            ComplexNumber coeff = this.cfCoeff[i];
            value = coeff.times(t).dividedBy(value);
            value = value.plus(ComplexNumber.ONE);
        }
        return ComplexNumber.ONE.dividedBy(value);
    }

    /**
     * 連分数の係数を返す. <br>
     * 最初の1は含まない.
     * 
     * @return 係数
     */
    public double[] coeffOfContinuedFraction() {
        return this.src.coeffOfContinuedFraction();
    }
}
