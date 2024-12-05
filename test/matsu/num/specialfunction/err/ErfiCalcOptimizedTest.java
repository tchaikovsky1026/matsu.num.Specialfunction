package matsu.num.specialfunction.err;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ErfiCalcOptimized} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ErfiCalcOptimizedTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final ErrorFunctionImaginaryCalculation ERFI = new ErfiCalcOptimized();

    @RunWith(Theories.class)
    public static class erfiに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-8.25,-7.75,-5,-3,-2.5,-2,-1.25,-0.75,-0.25,0,
        //                       0.25,0.75,1.25,2,2.5,3,5,7.75,8.25};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = erfi(x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -8.25, -2.49683898018411295E+28 },
                { -7.75, -8.92543859395549539E+24 },
                { -5, -8298273880.67680352 },
                { -3, -1629.99462260156565 },
                { -2.5, -130.395755013246927 },
                { -2, -18.5648024145755526 },
                { -1.25, -2.66913428868237504 },
                { -0.75, -1.03575728441196297 },
                { -0.25, -0.288083619794971984 },
                { 0, 0 },
                { 0.25, 0.288083619794971984 },
                { 0.75, 1.03575728441196297 },
                { 1.25, 2.66913428868237504 },
                { 2, 18.5648024145755526 },
                { 2.5, 130.395755013246927 },
                { 3, 1629.99462260156565 },
                { 5, 8298273880.67680352 },
                { 7.75, 8.92543859395549539E+24 },
                { 8.25, 2.49683898018411295E+28 },

                { Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERFI.erfi(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class erfixに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        numeric xs[] = {-8.25,-7.75,-6.75,-5.75,-4.75,-3.75,-2.75,-1.75,-0.75,-0.25,0,
        //                       0.25,0.75,1.25,2.25,3.25,4.25,5.25,6.25,7.25,8.25};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            g = erfi(x)*exp(-x*x);
        //            println(x,g);
        //        }
        /* ------------------------------------ */

        @DataPoints
        public static double[][] dataPairs = {
                { -8.25, -0.0689004980596054789 },
                { -7.75, -0.073420484677026387 },
                { -6.75, -0.0845328794179241116 },
                { -5.75, -0.0996768212265112937 },
                { -4.75, -0.121607128022189005 },
                { -3.75, -0.156512102724399949 },
                { -2.75, -0.223250886981430738 },
                { -1.75, -0.405580568981373555 },
                { -0.75, -0.590156711247878271 },
                { -0.25, -0.270629515617987493 },
                { 0, 0 },
                { 0.25, 0.270629515617987493 },
                { 0.75, 0.590156711247878271 },
                { 1.25, 0.559480940743271414 },
                { 2.25, 0.289490485423674097 },
                { 3.25, 0.183441633165943731 },
                { 4.25, 0.136783921603819067 },
                { 5.25, 0.109531346526862402 },
                { 6.25, 0.091473291696250886 },
                { 7.25, 0.0785817104423815577 },
                { 8.25, 0.0689004980596054789 },

                { Double.NEGATIVE_INFINITY, 0d },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    ERFI.erfix(dataPair[0]));
        }
    }
}
