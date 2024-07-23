package matsu.num.specialfunction.fraction;

import java.math.BigInteger;
import java.util.function.IntFunction;

import org.junit.Before;
import org.junit.Test;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.fraction.BigRationalElement.ConstantSupplier;

/**
 * {@link ComplexContinuedFractionFunction} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
final class ComplexContinuedFractionFunctionTest {

    public static final Class<?> TEST_CLASS = ComplexContinuedFractionFunction.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    public static class テスト関数による複素数連分数のテスト {
        /*
         * テスト関数は
         * log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
         * とする.
         */

        private static int SIZE = 50;
        private ComplexContinuedFractionFunction continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            IntFunction<BigRationalElement> supplier =
                    k -> BigRationalElement.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            ContinuedFractionFunction<
                    RationalType, BigRationalElement> function =
                            ContinuedFractionFunction.from(
                                    SIZE,
                                    RationalType.INSTANCE, supplier,
                                    ConstantSupplier.INSTANCE);
            continuedFractionFunction =
                    new ComplexContinuedFractionFunction(function.asDoubleFunction());
        }

        @Test
        public void test_関数のテスト() {

            double x_min = 1;
            double x_max = 3;
            double x_delta = 0.1;

            for (double x = x_min; x <= x_max; x += x_delta) {

                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x,
                        continuedFractionFunction.value(ComplexNumber.ofReal(x)).real());
            }
        }
    }
}
