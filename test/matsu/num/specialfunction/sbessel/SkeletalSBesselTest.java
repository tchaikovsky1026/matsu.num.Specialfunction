package matsu.num.specialfunction.sbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalSBessel} クラスのテスト
 * 
 * @author Matsuura Y.
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

        private final int order;

        SkeletalSBesselImpl(int order) {
            this.order = order;
        }

        @Override
        public int order() {
            return this.order;
        }

        @Override
        public double sbesselJ(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double sbesselY(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }
    }

}
