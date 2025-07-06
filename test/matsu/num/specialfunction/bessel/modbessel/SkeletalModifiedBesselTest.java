/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.modbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalModifiedBessel} クラスのテスト
 */
@RunWith(Enclosed.class)
final class SkeletalModifiedBesselTest {

    public static final Class<?> TEST_CLASS = SkeletalModifiedBessel.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalModifiedBesselImpl(10));
            System.out.println();
        }
    }

    private static final class SkeletalModifiedBesselImpl extends SkeletalModifiedBessel {

        SkeletalModifiedBesselImpl(int order) {
            super(order);
        }

        @Override
        public double besselI(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double besselK(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double besselIc(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double besselKc(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }
    }
}
