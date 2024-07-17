package matsu.num.specialfunction.sbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link SBessel0InPrinciple} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class SBessel0InPrincipleTest {

    public static final Class<?> TEST_CLASS = SBessel0InPrinciple.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final SBessel0 S_BESSEL_0 = new SBessel0InPrinciple();

    @RunWith(Theories.class)
    public static class 第1種球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,6,10,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselj(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, j_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 1 },
                { 1.E-12, 0.9999999999999999999999998333333333333333333 },
                { 0.01, 0.99998333341666646825424382690997290389643853601692 },
                { 0.5, 0.9588510772084060005465758704311427761636067358812 },
                { 1, 0.84147098480789650665250232163029899962256306079837 },
                { 1.5, 0.66499665773603628729448224742765821513776761728141 },
                { 3, 0.047040002686622407366914934269370093282311088084089 },
                { 6, -0.046569249699820978801925907768649126604665810719701 },
                { 10, -0.054402111088936981340474766185137728168364301291622 },
                { 30, -0.03293438746976205966625829690981527168289359827404 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_0.sbesselJ(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,6,10,30};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbessely(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.NEGATIVE_INFINITY },
                { 1.E-12, -999999999999.99999999999950000000000000000000000004 },
                { 0.01, -99.995000041666527778025793375220667321247058398028 },
                { 0.5, -1.7551651237807454322325631652076593039832903942195 },
                { 1, -0.54030230586813971740093660744297660373231042061792 },
                { 1.5, -0.047158134445135273392126567622845806056727351708898 },
                { 3, 0.32999749886681515242385759824375376746455969887186 },
                { 6, -0.1600283811083943367576087163204874009086562798685 },
                { 10, 0.083907152907645245225886394782406483451993016513317 },
                { 30, -0.0051417149962528016906220715538070065586500373368045 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_0.sbesselY(dataPair[0]));
        }
    }
}
