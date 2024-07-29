package matsu.num.specialfunction.bessel.subpj.modbessel.simulation;

import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;

/**
 * 変形Bessel関数の漸近形における連分数部分を扱う. <br>
 * この関数をg(t)とすると, I(x)の連分数がg(1/(8x)),
 * K(x)の連分数がg(-1/(8x))となる.
 * 
 * @author Matsuura Y.
 */
final class FractionFunctionForAsymtotic {

    private final ContinuedFractionFunction<BigRational> continuedFraction;

    private FractionFunctionForAsymtotic(
            ContinuedFractionFunction<BigRational> continuedFraction) {
        super();
        this.continuedFraction = continuedFraction;
    }

    public DoubleContinuedFractionFunction asDoubleFunction() {
        return this.continuedFraction.asDoubleFunction();
    }

    public static FractionFunctionForAsymtotic from(int numeratorOfOrder, int denominatorOfOrder, int kMax) {
        final BigRational sqOrder4 =
                BigRational.of(
                        4 * numeratorOfOrder * numeratorOfOrder,
                        denominatorOfOrder * denominatorOfOrder);

        IntFunction<BigRational> func =
                k -> {
                    final BigRational sq_k2_p_1 =
                            BigRational.of((2 * k + 1) * (2 * k + 1), 1);
                    final BigRational k_p_1 =
                            BigRational.of(k + 1, 1);

                    return sq_k2_p_1.minus(sqOrder4).dividedBy(k_p_1);
                };

        ContinuedFractionFunction<BigRational> continuedFraction =
                ContinuedFractionFunction.from(
                        kMax, func,
                        BigRational.constantSupplier());

        return new FractionFunctionForAsymtotic(continuedFraction);
    }
}
