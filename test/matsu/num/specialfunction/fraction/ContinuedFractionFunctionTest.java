package matsu.num.specialfunction.fraction;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.math.BigInteger;
import java.util.function.IntFunction;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.fraction.BigRationalElement.ConstantSupplier;

/**
 * {@link ContinuedFractionFunction}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ContinuedFractionFunctionTest {

    public static final Class<?> TEST_CLASS = ContinuedFractionFunction.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    public static class サイズ0のテスト {

        private static int SIZE = 0;
        private ContinuedFractionFunction<
                RationalType, BigRationalElement> continuedFractionFunction;

        @Before
        public void before_関数の準備() {

            //このサプライヤはダミーである.
            //サイズ0なので, 定数関数f(t) = 1が作られる.

            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            IntFunction<BigRationalElement> supplier =
                    k -> BigRationalElement.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            continuedFractionFunction =
                    ContinuedFractionFunction.from(
                            SIZE,
                            RationalType.INSTANCE, supplier,
                            BigRationalElement.ConstantSupplier.INSTANCE);
        }

        @Test
        public void test_項の数のテスト() {
            assertThat(
                    this.continuedFractionFunction.coeffOfContinuedFraction().size(),
                    is(SIZE));
        }

        @Test
        public void test_関数のテスト() {

            BigRationalElement rx = BigRationalElement.of(
                    BigInteger.valueOf(4), BigInteger.valueOf(3));

            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d,
                    continuedFractionFunction.value(rx).doubleValue());
        }
    }

    public static class サイズ1のテスト {

        private static int SIZE = 1;
        private ContinuedFractionFunction<
                RationalType, BigRationalElement> continuedFractionFunction;

        @Before
        public void before_関数の準備() {

            //このサプライヤはダミーである.
            //サイズ1なので, 定数関数f(t) = 1/(1 + (1/2)x)が作られる.

            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            IntFunction<BigRationalElement> supplier =
                    k -> BigRationalElement.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            continuedFractionFunction =
                    ContinuedFractionFunction.from(
                            SIZE,
                            RationalType.INSTANCE, supplier,
                            BigRationalElement.ConstantSupplier.INSTANCE);
        }

        @Test
        public void test_項の数のテスト() {
            assertThat(
                    this.continuedFractionFunction.coeffOfContinuedFraction().size(),
                    is(SIZE));
        }

        @Test
        public void test_関数のテスト() {

            BigRationalElement rx = BigRationalElement.of(
                    BigInteger.valueOf(4), BigInteger.valueOf(3));
            double x = rx.doubleValue();

            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1 / (1 + 0.5 * x),
                    continuedFractionFunction.value(rx).doubleValue());
        }
    }

    public static class 偶数個の係数によるテスト {

        private static int SIZE = 50;
        private ContinuedFractionFunction<
                RationalType, BigRationalElement> continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            IntFunction<BigRationalElement> supplier =
                    k -> BigRationalElement.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            continuedFractionFunction =
                    ContinuedFractionFunction.from(
                            SIZE,
                            RationalType.INSTANCE, supplier,
                            BigRationalElement.ConstantSupplier.INSTANCE);
        }

        @Test
        public void test_項の数のテスト() {
            assertThat(
                    this.continuedFractionFunction.coeffOfContinuedFraction().size(),
                    is(SIZE));
        }

        @Test
        public void test_関数のテスト() {

            int denomi = 10;

            int nume_min = 10;
            int nume_max = 30;
            for (int nume = nume_min; nume <= nume_max; nume++) {
                BigRationalElement rx = BigRationalElement.of(
                        BigInteger.valueOf(nume), BigInteger.valueOf(denomi));
                double x = rx.doubleValue();

                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x,
                        continuedFractionFunction.value(rx).doubleValue());
            }
        }
    }

    public static class 奇数個の係数によるテスト {

        private static int SIZE = 49;
        private ContinuedFractionFunction<
                RationalType, BigRationalElement> continuedFractionFunction;

        @Before
        public void before_関数の準備() {
            //log(1+x)/x = 1 - (1/2)x + (1/3)x^2 + ...
            IntFunction<BigRationalElement> supplier =
                    k -> BigRationalElement.of(BigInteger.valueOf(-(k + 1)), BigInteger.valueOf(k + 2));

            continuedFractionFunction =
                    ContinuedFractionFunction.from(
                            SIZE,
                            RationalType.INSTANCE, supplier,
                            ConstantSupplier.INSTANCE);
        }

        @Test
        public void test_項の数のテスト() {
            assertThat(
                    this.continuedFractionFunction.coeffOfContinuedFraction().size(),
                    is(SIZE));
        }

        @Test
        public void test_関数のテスト() {

            int denomi = 10;

            int nume_min = 10;
            int nume_max = 30;
            for (int nume = nume_min; nume <= nume_max; nume++) {
                BigRationalElement rx = BigRationalElement.of(
                        BigInteger.valueOf(nume), BigInteger.valueOf(denomi));
                double x = rx.doubleValue();

                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        Math.log1p(x) / x,
                        continuedFractionFunction.value(rx).doubleValue());
            }
        }
    }
}
