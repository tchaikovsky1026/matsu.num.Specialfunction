/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.sbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalSBessel} クラスのテスト
 */
@RunWith(Enclosed.class)
final class SkeletalSBesselTest {

    public static final Class<?> TEST_CLASS = SkeletalSBessel.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalSBesselImpl(10));
            System.out.println();
        }
    }

    private static final class SkeletalSBesselImpl extends SkeletalSBessel {

        SkeletalSBesselImpl(int order) {
            super(order);
        }

        @Override
        public double sbesselJ(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double sbesselY(double x) {
            throw new UnsupportedOperationException();
        }
    }
}
