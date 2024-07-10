package matsu.num.specialfunction.modbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ModifiedBessel2To6} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ModifiedBessel2To6Test {

    public static final Class<?> TEST_CLASS = ModifiedBessel2To6.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final IntFunction<ModifiedBesselHigherOrder> M_BESSEL_SUPPLIER =
            order -> new ModifiedBessel2To6(order, new NaiveMBessel0(), new NaiveMBessel1());

    @RunWith(Theories.class)
    public static class 次数2に関するIの値の検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0d },
                { 0.5, 0.031906149177738253813265777 },
                { 1, 0.13574766976703828118285257 },
                { 1.5, 0.33783461833568073067362492 },
                { 2, 0.68894844769873820405495002 },
                { 5, 17.505614966624236014887012 },
                { 10, 2281.5189677260035406016048 },
                { 20, 39312785.221040756253965669 },
                { 23.75, 1557948703.1611696000319225 },
                { 24.25, 2546322270.0602676520035264 },
                { 50, 281643064024519405478.46177 },
                { 100, 1.0523843193243105738955979E+42 },
                { 200, 2.0193413579164039925073988E+85 },
                { 500, 2.4948002629213736965686518E+215 },
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

    @RunWith(Theories.class)
    public static class 次数6に関するIの値の検証 {

        private final int n = 6;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=6;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 3.4212359263825779212788791E-7 },
                { 1, 2.2488661477147573327345164E-5 },
                { 1.5, 2.677691439944762455491774E-4 },
                { 2, 0.0016001733635217266338515825 },
                { 5, 0.79228566899777701640202577 },
                { 10, 449.30225135623163783407724 },
                { 20, 17425864.212058035224428379 },
                { 23.75, 786046654.26393794464479847 },
                { 24.25, 1303138009.0987135721128912 },
                { 50, 203938928199686472289.38574 },
                { 100, 8.9610693954004335793875108E+41 },
                { 200, 1.8637248111750076065288166E+85 },
                { 500, 2.4161540264783425286764094E+215 },
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
