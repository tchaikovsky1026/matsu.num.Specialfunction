/*
 * Copyright © 2025 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icgamma;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalICGamma} クラスのテスト
 */
@RunWith(Enclosed.class)
final class SkeletalICGammaTest {

    public static final Class<?> TEST_CLASS = SkeletalICGamma.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalICGammaImpl());
            System.out.println();
        }
    }

    private static final class SkeletalICGammaImpl extends SkeletalICGamma {

        SkeletalICGammaImpl() {
            super(3);
        }

        @Override
        protected double oddsValue(double oddsX) {
            throw new UnsupportedOperationException();
        }
    }
}
