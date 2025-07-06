/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icbeta;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalICBeta} クラスのテスト
 */
@RunWith(Enclosed.class)
final class SkeletalICBetaTest {

    public static final Class<?> TEST_CLASS = SkeletalICBeta.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalICBetaImpl());
            System.out.println();
        }
    }

    private static final class SkeletalICBetaImpl extends SkeletalICBeta {

        SkeletalICBetaImpl() {
            super(3, 2);
        }

        @Override
        protected double oddsValue(double oddsX) {
            throw new UnsupportedOperationException();
        }
    }
}
