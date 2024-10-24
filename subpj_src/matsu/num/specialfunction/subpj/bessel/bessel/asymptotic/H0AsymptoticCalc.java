package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ComplexNumber;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatMathFieldWrapper;

/**
 * 第2種ハンケル関数 H0 (すなわち, P + iQ) の漸近展開部分を連分数により計算する. <br>
 * 係数は実数だが, 複素数を引数に取れるようにする.
 * 
 * <p>
 * H0の漸近展開部分は, u = i/(8x)として <br>
 * sum_{k=0}^{inf} (1^2 * 3^2 * ... (2k-1)^2)/k! * u^k <br>
 * である.
 * </p>
 * 
 * <p>
 * P0は t = 1/(8x) を引数として, H0の漸近展開の実部を得る関数, <br>
 * Q0は 虚部を得る関数である.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class H0AsymptoticCalc {

    private static final ComplexNumber.Provider<DoubleDoubleFloatMathFieldWrapper> COMPLEX_NUMBER_PROVIDER =
            new ComplexNumber.Provider<>(DoubleDoubleFloatMathFieldWrapper.constantSupplier());

    /**
     * i*tからh0への写像.
     */
    private static final ContinuedFractionFunction<
            ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> IT_TO_H0 =
                    createH0().transformedFrom(
                            COMPLEX_NUMBER_PROVIDER::getValueOfReal,
                            COMPLEX_NUMBER_PROVIDER.constantSupplier());

    private static ContinuedFractionFunction<DoubleDoubleFloatMathFieldWrapper> createH0() {

        final int kMax = 60;

        IntFunction<BigRational> func =
                k -> BigRational.of(((2 * k + 1) * (2 * k + 1)), k + 1);

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
    public static DoubleDoubleFloatElement calcP0(DoubleDoubleFloatElement t) {
        return calcH0(t).real().asDoubleDoubleFloatElement();
    }

    /**
     * tからQ0を計算するための関数.
     * 
     * @param t t
     * @return Q0
     */
    public static DoubleDoubleFloatElement calcQ0(DoubleDoubleFloatElement t) {
        return calcH0(t).imaginary().asDoubleDoubleFloatElement();
    }

    private static ComplexNumber<DoubleDoubleFloatMathFieldWrapper> calcH0(DoubleDoubleFloatElement t) {
        ComplexNumber<DoubleDoubleFloatMathFieldWrapper> u =
                COMPLEX_NUMBER_PROVIDER.getValueOfImaginary(
                        new DoubleDoubleFloatMathFieldWrapper(t));

        return IT_TO_H0.value(u);
    }
}
