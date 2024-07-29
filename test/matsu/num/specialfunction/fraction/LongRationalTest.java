package matsu.num.specialfunction.fraction;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link LongRational} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("deprecation")
@RunWith(Enclosed.class)
final class LongRationalTest {

    public static final Class<?> TEST_CLASS = LongRational.class;

    @RunWith(Theories.class)
    public static class 生成のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{生成元:分子,分母},{結果:分子,分母}}
                { { 4, 6 }, { 2, 3 } },
                { { 4, -6 }, { -2, 3 } },
                { { -4, 6 }, { -2, 3 } },
                { { 0, 2 }, { 0, 1 } },
                { { 0, -3 }, { 0, 1 } }
        };

        @Theory
        public void test_検証(long[][] pair) {
            long[] input = pair[0];
            LongRational result = LongRational.of(input[0], input[1]);

            long[] expected = pair[1];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }

    public static class 生成の例外テスト {

        @Test(expected = ArithmeticException.class)
        public void test_分母0で例外() {
            LongRational.of(1, 0);
        }

        @Test(expected = ArithmeticException.class)
        public void test_分子分母0で例外() {
            LongRational.of(0, 0);
        }
    }

    @RunWith(Theories.class)
    public static class 符号変換のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{元:分子,分母},{変換結果:分子,分母}}
                { { 2, 3 }, { -2, 3 } },
                { { -2, 3 }, { 2, 3 } },
                { { 0, 1 }, { 0, 1 } }
        };

        @Theory
        public void test_検証(long[][] pair) {
            long[] input = pair[0];
            LongRational result = LongRational.of(input[0], input[1]).negated();

            long[] expected = pair[1];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }

    @RunWith(Theories.class)
    public static class 和のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{operand1:分子,分母},{operand2:分子,分母},{結果:分子,分母}}
                { { -2, 3 }, { 3, 2 }, { 5, 6 } },
                { { 2, 3 }, { 5, 6 }, { 3, 2 } }
        };

        @Theory
        public void test_生成の検証(long[][] pair) {
            long[] operand1 = pair[0];
            long[] operand2 = pair[1];
            LongRational result = LongRational.of(operand1[0], operand1[1])
                    .plus(LongRational.of(operand2[0], operand2[1]));

            long[] expected = pair[2];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }

    @RunWith(Theories.class)
    public static class 差のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{operand1:分子,分母},{operand2:分子,分母},{結果:分子,分母}}
                { { -2, 3 }, { 3, 2 }, { -13, 6 } },
                { { -2, 3 }, { 5, 6 }, { -3, 2 } }
        };

        @Theory
        public void test_生成の検証(long[][] pair) {
            long[] operand1 = pair[0];
            long[] operand2 = pair[1];
            LongRational result = LongRational.of(operand1[0], operand1[1])
                    .minus(LongRational.of(operand2[0], operand2[1]));

            long[] expected = pair[2];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }

    @RunWith(Theories.class)
    public static class 積のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{operand1:分子,分母},{operand2:分子,分母},{結果:分子,分母}}
                { { -2, 3 }, { 3, 2 }, { -1, 1 } },
                { { -2, 3 }, { 5, 6 }, { -5, 9 } }
        };

        @Theory
        public void test_生成の検証(long[][] pair) {
            long[] operand1 = pair[0];
            long[] operand2 = pair[1];
            LongRational result = LongRational.of(operand1[0], operand1[1])
                    .times(LongRational.of(operand2[0], operand2[1]));

            long[] expected = pair[2];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }

    @RunWith(Theories.class)
    public static class 商のテスト {

        @DataPoints
        public static long[][][] data = {
                //{{operand1:分子,分母},{operand2:分子,分母},{結果:分子,分母}}
                { { -2, 3 }, { 2, 3 }, { -1, 1 } },
                { { -2, 3 }, { 6, 5 }, { -5, 9 } },
                //商の計算結果が分母が正に修正されるかを検証
                { { -2, 3 }, { -6, 5 }, { 5, 9 } }
        };

        @Theory
        public void test_生成の検証(long[][] pair) {
            long[] operand1 = pair[0];
            long[] operand2 = pair[1];
            LongRational result = LongRational.of(operand1[0], operand1[1])
                    .dividedBy(LongRational.of(operand2[0], operand2[1]));

            long[] expected = pair[2];
            assertThat(result.numerator(), is(expected[0]));
            assertThat(result.denominator(), is(expected[1]));
        }
    }
}
