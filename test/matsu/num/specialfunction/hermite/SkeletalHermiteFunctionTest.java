/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.hermite;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalHermiteFunction} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SkeletalHermiteFunctionTest {

    public static final Class<?> TEST_CLASS = SkeletalHermiteFunction.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new HermiteImpl(10));
            System.out.println();
        }

        private static final class HermiteImpl extends SkeletalHermiteFunction {

            HermiteImpl(int degreeN) {
                super(degreeN);
            }

            @Override
            public double hermiteH(double x) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
