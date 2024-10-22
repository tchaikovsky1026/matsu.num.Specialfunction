package matsu.num.specialfunction.fraction;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link DoubleComplexNumber} クラスのテスト
 * 
 * @author Matsuura Y.
 */
final class DoubleComplexNumberTest {

    public static final Class<?> TEST_CLASS = DoubleComplexNumber.class;

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
            DoubleComplexNumber result = DoubleComplexNumber.of(input[0], input[1]).negated();

            double[] expected = pair[1];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary());
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
            DoubleComplexNumber result = DoubleComplexNumber.of(operand1[0], operand1[1])
                    .plus(DoubleComplexNumber.of(operand2[0], operand2[1]));

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary());
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
            DoubleComplexNumber result = DoubleComplexNumber.of(operand1[0], operand1[1])
                    .minus(DoubleComplexNumber.of(operand2[0], operand2[1]));

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary());
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
            DoubleComplexNumber result = DoubleComplexNumber.of(operand1[0], operand1[1])
                    .times(DoubleComplexNumber.of(operand2[0], operand2[1]));

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary());
        }
    }

    @RunWith(Theories.class)
    public static class 商のテスト {

        @DataPoints
        public static double[][][] data = {
                //{{operand1:re,im},{operand2:re,im},{結果:re,im}}
                { { -2, 3 }, { 3, 2 }, { 0, 1 } },
                { { 2, 3 }, { 5, 6 }, { 28d / 61, 3d / 61 } }
        };

        @Theory
        public void test_検証(double[][] pair) {
            double[] operand1 = pair[0];
            double[] operand2 = pair[1];
            DoubleComplexNumber result = DoubleComplexNumber.of(operand1[0], operand1[1])
                    .dividedBy(DoubleComplexNumber.of(operand2[0], operand2[1]));

            double[] expected = pair[2];
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[0], result.real());
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(expected[1], result.imaginary());
        }
    }
}
