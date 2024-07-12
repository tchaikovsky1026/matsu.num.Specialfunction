package matsu.num.specialfunction.fraction;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * {@link LongGCD} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class LongGCDTest {

    public static final Class<?> TEST_CLASS = LongGCD.class;

    @RunWith(Theories.class)
    public static class 最大公約数の計算のテスト {

        @DataPoints
        public static long[][] data = {
                //{v1,v2,gcd(v1,v2)}
                { 6, 9, 3 },
                { -6, 9, 3 },
                { 6, -9, 3 },
                { -6, -9, 3 },
                { 9, 6, 3 },
                { -9, 6, 3 },
                { 9, -6, 3 },
                { -9, -6, 3 },

                { 0, 2, 2 },
                { 0, -3, 3 },
                { 0, 0, 0 },

                { Long.MIN_VALUE, 0, Long.MIN_VALUE },
                { Long.MIN_VALUE, -32, 32 },
                { Long.MIN_VALUE, 96, 32 }
        };

        @Theory
        public void test_検証(long[] set) {
            assertThat(LongGCD.gcd(set[0], set[1]), is(set[2]));
        }
    }
}
