/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

package matsu.num.specialfunction.laguerre;

import org.junit.Test;

/**
 * {@link SkeletalLaguerreFunction} のテスト.
 */
final class SkeletalLaguerreFunctionTest {

    public static final Class<?> TEST_CLASS = SkeletalLaguerreFunction.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new LaguerreImpl(10, 20));
            System.out.println();
        }

        private static final class LaguerreImpl extends SkeletalLaguerreFunction {

            LaguerreImpl(int degreeN, int orderK) {
                super(degreeN, orderK);
            }

            @Override
            public double laguerreL(double x) {
                throw new UnsupportedOperationException();
            }
        }
    }
}
