/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.modsbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalMSBessel} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SkeletalMSBesselTest {

    public static final Class<?> TEST_CLASS = SkeletalMSBessel.class;

    public static class toString表示 {

        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new SkeletalMSBesselImpl(10));
            System.out.println();
        }
    }

    private static final class SkeletalMSBesselImpl extends SkeletalMSBessel {

        SkeletalMSBesselImpl(int order) {
            super(order);
        }

        @Override
        public double sbesselI(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double sbesselK(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double sbesselIc(double x) {
            throw new UnsupportedOperationException();
        }

        @Override
        public double sbesselKc(double x) {
            throw new UnsupportedOperationException();
        }
    }
}
