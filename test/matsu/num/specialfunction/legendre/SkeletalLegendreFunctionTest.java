/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.legendre;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalLegendreFunction} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SkeletalLegendreFunctionTest {

    public static final Class<?> TEST_CLASS = SkeletalLegendreFunction.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new LegendreImpl(20, 18));
            System.out.println();
        }

        private static final class LegendreImpl extends SkeletalLegendreFunction {

            LegendreImpl(int degreeL, int orderM) {
                super(degreeL, orderM);
            }

            @Override
            public double legendreP(double x) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
