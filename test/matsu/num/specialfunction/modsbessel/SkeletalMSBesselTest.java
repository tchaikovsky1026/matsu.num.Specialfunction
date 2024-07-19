package matsu.num.specialfunction.modsbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * {@link SkeletalMSBessel} クラスのテスト.
 * 
 * @author Matsuura Y.
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

        private final int order;

        SkeletalMSBesselImpl(int order) {
            this.order = order;
        }

        @Override
        public int order() {
            return this.order;
        }

        @Override
        public double sbesselI(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double sbesselK(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double sbesselIc(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }

        @Override
        public double sbesselKc(double x) {
            throw new UnsupportedOperationException("呼んではいけない");
        }
    }
}
