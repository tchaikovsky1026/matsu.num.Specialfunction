package matsu.num.specialfunction.modbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ModifiedBesselOver7} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ModifiedBesselOver7Test {

    public static final Class<?> TEST_CLASS = ModifiedBesselOver7.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final IntFunction<ModifiedBesselHigherOrder> M_BESSEL_SUPPLIER =
            order -> new ModifiedBesselOver7(order, new NaiveMBessel0(), new NaiveMBessel1());

    @RunWith(Theories.class)
    public static class 次数7に関するIの値の検証 {

        private final int n = 7;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=7;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,24.25,24.75,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 1.2205089791076950548741547E-8 },
                { 1, 1.5992182312009952529319365E-6 },
                { 1.5, 2.8406417141745886411260765E-5 },
                { 2, 2.2463914200134251769755572E-4 },
                { 5, 0.25648894172788162753756664 },
                { 10, 238.0255847757819945064008 },
                { 20, 12562873.686178849566047491 },
                { 24.25, 994504651.84039839008093848 },
                { 24.75, 1656997046.5020243867918668 },
                { 50, 178909488023203436253.80776 },
                { 100, 8.3947655455875292166562543E+41 },
                { 200, 1.8039906128531864890335478E+85 },
                { 500, 2.3849171194724407702533707E+215 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselI(dataPair[0]));
        }
    }

    /**
     * @deprecated I_0がinfだがI_20が有限の場合, 逆方向漸化式で破綻する.
     *                 これを解決する必要がある.
     *                 x=100,199の場合
     */
    @Deprecated
    @RunWith(Theories.class)
    public static class 次数20に関するIの値の検証 {

        private final int n = 20;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=20;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,199,201,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 3.7494538480790195277648693E-31 },
                { 1, 3.9668359858190200557320782E-25 },
                { 1.5, 1.3388331839683479801702603E-21 },
                { 2, 4.3105605761095483321832035E-19 },
                { 5, 5.0242393579718059920596108E-11 },
                { 10, 1.2507997356449475591475018E-4 },
                { 20, 3188.7503288536148015531305 },
                { 50, 5442008402752997526.5214032 },
                //                { 100, 1.448346125642717164103143E+41 },
                //                { 199, 2.7488511382580216517471788E+84 },
                { 201, 2.0413613657484191227140677E+85 },
                { 500, 1.6784410801938620762649637E+215 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselI(dataPair[0]));
        }
    }
}
