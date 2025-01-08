/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ComplexNumber;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatMathFieldWrapper;

/**
 * 第2種ハンケル関数 H1 (すなわち, P + iQ) の漸近展開部分を連分数により計算する. <br>
 * 係数は実数だが, 複素数を引数に取れるようにする.
 * 
 * <p>
 * H1の漸近展開部分は, u = i/(8x)として <br>
 * sum_{k=0}^{inf} ((1^2-4) * (3^2-4) * ... ((2k-1)^2 - 4))/k! * u^k <br>
 * である.
 * </p>
 * 
 * <p>
 * P1は t = 1/(8x) を引数として, H1の漸近展開の実部を得る関数, <br>
 * Q1は 虚部を得る関数である.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class H1AsymptoticCalc {

    private static final ComplexNumber.Provider<
            DoubleDoubleFloatMathFieldWrapper> COMPLEX_NUMBER_PROVIDER =
                    new ComplexNumber.Provider<>(DoubleDoubleFloatMathFieldWrapper.constantSupplier());

    /**
     * i*tからh1への写像.
     */
    private static final ContinuedFractionFunction<
            ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> IT_TO_H1 =
                    createH1().transformedFrom(
                            COMPLEX_NUMBER_PROVIDER::getValueOfReal,
                            COMPLEX_NUMBER_PROVIDER.constantSupplier());

    private static ContinuedFractionFunction<DoubleDoubleFloatMathFieldWrapper> createH1() {

        final int kMax = 60;

        IntFunction<BigRational> func =
                k -> BigRational.of(((2 * k + 1) * (2 * k + 1)) - 4, k + 1);

        return ContinuedFractionFunction.from(kMax, func, BigRational.constantSupplier())
                .transformedFrom(
                        br -> new DoubleDoubleFloatMathFieldWrapper(
                                DoubleDoubleFloatElement.fromBigRational(br)),
                        DoubleDoubleFloatMathFieldWrapper.constantSupplier());
    }

    /**
     * tからP0を計算するための関数.
     * 
     * @param t t
     * @return P0
     */
    public static DoubleDoubleFloatElement calcP1(DoubleDoubleFloatElement t) {
        return calcH1(t).real().asDoubleDoubleFloatElement();
    }

    /**
     * tからQ0を計算するための関数.
     * 
     * @param t t
     * @return Q0
     */
    public static DoubleDoubleFloatElement calcQ1(DoubleDoubleFloatElement t) {
        return calcH1(t).imaginary().asDoubleDoubleFloatElement();
    }

    private static ComplexNumber<DoubleDoubleFloatMathFieldWrapper> calcH1(DoubleDoubleFloatElement t) {
        ComplexNumber<DoubleDoubleFloatMathFieldWrapper> u =
                COMPLEX_NUMBER_PROVIDER.getValueOfImaginary(
                        new DoubleDoubleFloatMathFieldWrapper(t));

        return IT_TO_H1.value(u);
    }
}
