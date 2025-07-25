/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.chebyshev;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalChebyshevFunction} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SkeletalChebyshevFunctionTest {

    public static final Class<?> TEST_CLASS = SkeletalChebyshevFunction.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new ChebyshevImpl(10));
            System.out.println();
        }

        private static final class ChebyshevImpl extends SkeletalChebyshevFunction {

            ChebyshevImpl(int degreeN) {
                super(degreeN);
            }

            @Override
            public double chebyshevT(double x) {
                throw new UnsupportedOperationException();
            }

            @Override
            public double chebyshevU(double x) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
