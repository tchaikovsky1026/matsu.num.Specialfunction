package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;

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
final class ContinuedFractionFactoryOfH1Asymptotic {

    private static final ComplexNumber.Provider<DoubleDoubleFloatMathFieldWrapper> COMPLEX_NUMBER_PROVIDER =
            new ComplexNumber.Provider<>(DoubleDoubleFloatMathFieldWrapper.constantSupplier());

    private static final ContinuedFractionFunction<DoubleDoubleFloatMathFieldWrapper> H_1 = createH1();

    /**
     * tからP1を計算するための関数.
     */
    public static UnaryOperator<DoubleDoubleFloatElement> P1 = createP1();

    /**
     * tからQ1を計算するための関数.
     */
    public static UnaryOperator<DoubleDoubleFloatElement> Q1 = createQ1();

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

    private static UnaryOperator<DoubleDoubleFloatElement> createP1() {

        /*
         * 1. tを与え, u = itを引数とする.
         * 2. Hの漸近展開部分は, uに対する実数係数の漸近級数である.
         * 3. PはHの漸近展開部分の実部をとる.
         */
        final ContinuedFractionFunction<
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> func_u_to_h1 =
                        H_1.transformedFrom(
                                COMPLEX_NUMBER_PROVIDER::getValueOfReal,
                                COMPLEX_NUMBER_PROVIDER.constantSupplier());

        final Function<
                DoubleDoubleFloatElement,
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> func_t_to_u =
                        t -> COMPLEX_NUMBER_PROVIDER.getValueOfImaginary(
                                new DoubleDoubleFloatMathFieldWrapper(t));

        final Function<
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>,
                DoubleDoubleFloatElement> func_h1_to_p1 =
                        h0 -> h0.real().asDoubleDoubleFloatElement();

        final Function<
                DoubleDoubleFloatElement,
                DoubleDoubleFloatElement> func_t_to_p1 =
                        func_t_to_u.andThen(func_u_to_h1::value).andThen(func_h1_to_p1);

        return func_t_to_p1::apply;
    }

    private static UnaryOperator<DoubleDoubleFloatElement> createQ1() {

        /*
         * 1. tを与え, u = itを引数とする.
         * 2. Hの漸近展開部分は, uに対する実数係数の漸近級数である.
         * 3. QはHの漸近展開部分の虚部をとる.
         */
        final ContinuedFractionFunction<
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> func_u_to_h1 =
                        H_1.transformedFrom(
                                COMPLEX_NUMBER_PROVIDER::getValueOfReal,
                                COMPLEX_NUMBER_PROVIDER.constantSupplier());

        final Function<
                DoubleDoubleFloatElement,
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>> func_t_to_u =
                        t -> COMPLEX_NUMBER_PROVIDER.getValueOfImaginary(
                                new DoubleDoubleFloatMathFieldWrapper(t));

        final Function<
                ComplexNumber<DoubleDoubleFloatMathFieldWrapper>,
                DoubleDoubleFloatElement> func_h1_to_q1 =
                        h0 -> h0.imaginary().asDoubleDoubleFloatElement();

        final Function<
                DoubleDoubleFloatElement,
                DoubleDoubleFloatElement> func_t_to_q1 =
                        func_t_to_u.andThen(func_u_to_h1::value).andThen(func_h1_to_q1);

        return func_t_to_q1::apply;
    }
}
