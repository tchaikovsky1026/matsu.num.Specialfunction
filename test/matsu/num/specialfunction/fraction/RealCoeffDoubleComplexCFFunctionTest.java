package matsu.num.specialfunction.fraction;

import java.math.BigInteger;
import java.util.function.IntFunction;

import org.junit.Before;
import org.junit.Test;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link RealCoeffDoubleComplexCFFunction} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
final class RealCoeffDoubleComplexCFFunctionTest {

    public static final Class<?> TEST_CLASS = RealCoeffDoubleComplexCFFunction.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    public static class テスト関数による複素数連分数のテスト {
        /*
         * テスト関数は
         * log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
         * とする.
         */

        private static int SIZE = 50;
        private RealCoeffDoubleComplexCFFunction continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            IntFunction<BigRational> supplier =
                    k -> BigRational.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            ContinuedFractionFunction<BigRational> function =
                    ContinuedFractionFunction.from(
                            SIZE, supplier,
                            BigRational.constantSupplier());
            continuedFractionFunction =
                    new RealCoeffDoubleComplexCFFunction(
                            DoubleContinuedFractionFunction.from(function));
        }

        @Test
        public void test_関数のテスト() {

            double x_min = 1;
            double x_max = 3;
            double x_delta = 0.1;

            for (double x = x_min; x <= x_max; x += x_delta) {

                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x,
                        continuedFractionFunction.value(DoubleComplexNumber.ofReal(x)).real());
            }
        }
    }
}
