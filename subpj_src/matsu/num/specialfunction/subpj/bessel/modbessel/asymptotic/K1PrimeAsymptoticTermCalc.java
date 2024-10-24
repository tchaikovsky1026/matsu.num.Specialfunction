package matsu.num.specialfunction.subpj.bessel.modbessel.asymptotic;

import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatMathFieldWrapper;

/**
 * K1 の漸近展開部分を連分数により計算する.
 * 
 * <p>
 * K1の漸近展開部分は, t = 1/(8x)として <br>
 * F(t) = sum_{k=0}^{inf} ((1^2-4) * (3^2-4) * ... ((2k-1)^2-4))/k! * (-1)^k t^k
 * <br>
 * である. <br>
 * ここでは, <br>
 * F'(t) = (F(t) - 1)/t =
 * sum_{k=1}^{inf} ((1^2-4) * (3^2-4) * ... ((2k-1)^2-4))/k! * (-1)^k t^{k-1}
 * <br>
 * を計算する.
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class K1PrimeAsymptoticTermCalc {

    private static final ContinuedFractionFunction<
            DoubleDoubleFloatMathFieldWrapper> K1_ASYMPTOTIC_PRIME_KERNEL = createK1AsymptoticPrime_kernel();

    /**
     * F'(t)/3 を計算している.
     */
    private static ContinuedFractionFunction<DoubleDoubleFloatMathFieldWrapper> createK1AsymptoticPrime_kernel() {

        final int kMax = 60;

        IntFunction<BigRational> func =
                k -> BigRational.of(-((2 * k + 3) * (2 * k + 3) - 4), k + 2);

        return ContinuedFractionFunction.from(kMax, func, BigRational.constantSupplier())
                .transformedFrom(
                        br -> fromBigRationalToDDFWrapper(br),
                        DoubleDoubleFloatMathFieldWrapper.constantSupplier());
    }

    private static DoubleDoubleFloatMathFieldWrapper fromBigRationalToDDFWrapper(BigRational br) {
        return new DoubleDoubleFloatMathFieldWrapper(
                DoubleDoubleFloatElement.fromBigRational(br));
    }

    /**
     * F'(t) を計算する.
     * 
     * @param t t
     * @return F'(t)
     */
    public static DoubleDoubleFloatElement calc(DoubleDoubleFloatElement t) {
        return K1_ASYMPTOTIC_PRIME_KERNEL.value(new DoubleDoubleFloatMathFieldWrapper(t))
                .asDoubleDoubleFloatElement().times(3d);
    }
}
