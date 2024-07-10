package matsu.num.specialfunction.modbessel;

import java.util.function.IntUnaryOperator;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NormalContinuedFractionFunction}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NormalContinuedFractionFunctionTest {

    public static final Class<?> TEST_CLASS = NormalContinuedFractionFunction.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    public static class 偶数個の係数によるテスト {

        private NormalContinuedFractionFunction continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            int size = 50;
            IntUnaryOperator nume = k -> -(k + 1);
            IntUnaryOperator denomi = k -> (k + 2);

            continuedFractionFunction = new NormalContinuedFractionFunction(size, nume, denomi);
        }

        @Test
        public void test_関数のテスト() {
            double x_min = 1d;
            double x_max = 3d;
            double delta_x = 0.1;
            for (double x = x_min; x <= x_max; x += delta_x) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x, continuedFractionFunction.value(x));
            }
        }
    }

    public static class 奇数個の係数によるテスト {

        private NormalContinuedFractionFunction continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            int size = 49;
            IntUnaryOperator nume = k -> -(k + 1);
            IntUnaryOperator denomi = k -> (k + 2);

            continuedFractionFunction = new NormalContinuedFractionFunction(size, nume, denomi);
        }

        @Test
        public void test_関数のテスト() {
            double x_min = 1d;
            double x_max = 3d;
            double delta_x = 0.1;
            for (double x = x_min; x <= x_max; x += delta_x) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x, continuedFractionFunction.value(x));
            }
        }
    }
}
