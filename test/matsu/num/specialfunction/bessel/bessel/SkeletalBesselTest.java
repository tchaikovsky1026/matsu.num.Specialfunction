/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.bessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalBessel} クラスのテスト
 */
@RunWith(Enclosed.class)
final class SkeletalBesselTest {

    public static final Class<?> TEST_CLASS = SkeletalBessel.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalBesselImpl(10));
            System.out.println();
        }
    }

    private static final class SkeletalBesselImpl extends SkeletalBessel {

        SkeletalBesselImpl(int order) {
            super(order);
        }

        @Override
        public double besselJ(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double besselY(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }
    }
}
