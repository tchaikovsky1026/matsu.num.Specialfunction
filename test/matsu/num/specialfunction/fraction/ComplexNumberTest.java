/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.fraction;

import java.math.BigDecimal;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ComplexNumber} クラスのテスト.
 */
final class ComplexNumberTest {

    public static final Class<?> TEST_CLASS = DoubleComplexNumber.class;

    private static final ComplexNumber.Provider<Decimal128> COMPLEX_PROVIDER =
            new ComplexNumber.Provider<>(Decimal128.constantSupplier());

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    @RunWith(Theories.class)
    public static class 符号変換のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{元:re,im},{変換結果:re,im}}
                { { 2, 3 }, { -2, -3 } },
                { { -2, 3 }, { 2, -3 } },
                { { 0, 1 }, { 0, -1 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] input = pair[0];
            ComplexNumber<Decimal128> result =
                    COMPLEX_PROVIDER.getValueOf(
                            new Decimal128(new BigDecimal(input[0])),
                            new Decimal128(new BigDecimal(input[1])))
                            .negated();

            double[] expected = pair[1];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real().doubleValue());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary().doubleValue());
        }
    }

    @RunWith(Theories.class)
    public static class 和のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{operand1:re,im},{operand2:re,im},{結果:re,im}}
                { { -2, 3 }, { 3, 2 }, { 1, 5 } },
                { { 2, 3 }, { 5, 6 }, { 7, 9 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] operand1 = pair[0];
            double[] operand2 = pair[1];
            ComplexNumber<Decimal128> ope1 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand1[0])),
                    new Decimal128(new BigDecimal(operand1[1])));
            ComplexNumber<Decimal128> ope2 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand2[0])),
                    new Decimal128(new BigDecimal(operand2[1])));

            ComplexNumber<Decimal128> result = ope1.plus(ope2);

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real().doubleValue());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary().doubleValue());
        }
    }

    @RunWith(Theories.class)
    public static class 差のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{operand1:re,im},{operand2:re,im},{結果:re,im}}
                { { -2, 3 }, { 3, 2 }, { -5, 1 } },
                { { 2, 3 }, { 5, 6 }, { -3, -3 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] operand1 = pair[0];
            double[] operand2 = pair[1];
            ComplexNumber<Decimal128> ope1 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand1[0])),
                    new Decimal128(new BigDecimal(operand1[1])));
            ComplexNumber<Decimal128> ope2 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand2[0])),
                    new Decimal128(new BigDecimal(operand2[1])));

            ComplexNumber<Decimal128> result = ope1.minus(ope2);

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real().doubleValue());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary().doubleValue());
        }
    }

    @RunWith(Theories.class)
    public static class 積のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{operand1:re,im},{operand2:re,im},{結果:re,im}}
                { { -2, 3 }, { 3, 2 }, { -12, 5 } },
                { { 2, 3 }, { 5, 6 }, { -8, 27 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] operand1 = pair[0];
            double[] operand2 = pair[1];
            ComplexNumber<Decimal128> ope1 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand1[0])),
                    new Decimal128(new BigDecimal(operand1[1])));
            ComplexNumber<Decimal128> ope2 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand2[0])),
                    new Decimal128(new BigDecimal(operand2[1])));

            ComplexNumber<Decimal128> result = ope1.times(ope2);

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real().doubleValue());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary().doubleValue());
        }
    }

    @RunWith(Theories.class)
    public static class 商のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{operand1:re,im},{operand2:re,im},{結果:re,im}}
                { { -2, 3 }, { 3, 1 }, { -0.3, 1.1 } },
                { { 2, 3 }, { 5, 6 }, { 28d / 61, 3d / 61 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] operand1 = pair[0];
            double[] operand2 = pair[1];
            ComplexNumber<Decimal128> ope1 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand1[0])),
                    new Decimal128(new BigDecimal(operand1[1])));
            ComplexNumber<Decimal128> ope2 = COMPLEX_PROVIDER.getValueOf(
                    new Decimal128(new BigDecimal(operand2[0])),
                    new Decimal128(new BigDecimal(operand2[1])));

            ComplexNumber<Decimal128> result = ope1.dividedBy(ope2);

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real().doubleValue());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary().doubleValue());
        }
    }
}
