package matsu.num.specialfunction.bessel.modbessel.subpj.simulation;

import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRationalElement;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;
import matsu.num.specialfunction.fraction.RationalType;

/**
 * 変形Bessel関数の漸近形における連分数部分を扱う. <br>
 * この関数をg(t)とすると, I(x)の連分数がg(1/(8x)),
 * K(x)の連分数がg(-1/(8x))となる.
 * 
 * @author Matsuura Y.
 */
final class FractionFunctionForAsymtotic {

    private final ContinuedFractionFunction<
            RationalType, BigRationalElement> continuedFraction;

    private FractionFunctionForAsymtotic(ContinuedFractionFunction<
            RationalType, BigRationalElement> continuedFraction) {
        super();
        this.continuedFraction = continuedFraction;
    }

    public DoubleContinuedFractionFunction asDoubleFunction() {
        return this.continuedFraction.asDoubleFunction();
    }

    public static FractionFunctionForAsymtotic from(int numeratorOfOrder, int denominatorOfOrder, int kMax) {
        final BigRationalElement sqOrder4 =
                BigRationalElement.of(
                        4 * numeratorOfOrder * numeratorOfOrder,
                        denominatorOfOrder * denominatorOfOrder);

        IntFunction<BigRationalElement> func =
                k -> {
                    final BigRationalElement sq_k2_p_1 =
                            BigRationalElement.of((2 * k + 1) * (2 * k + 1), 1);
                    final BigRationalElement k_p_1 =
                            BigRationalElement.of(k + 1, 1);

                    return sq_k2_p_1.minus(sqOrder4).dividedBy(k_p_1);
                };

        ContinuedFractionFunction<
                RationalType, BigRationalElement> continuedFraction = ContinuedFractionFunction.from(
                        kMax,
                        RationalType.INSTANCE, func,
                        BigRationalElement.ConstantSupplier.INSTANCE);

        return new FractionFunctionForAsymtotic(continuedFraction);
    }
}
