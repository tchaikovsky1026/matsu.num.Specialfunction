package matsu.num.specialfunction.modbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * 
 * {@link NaiveMBessel1}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NaiveMBessel1Test {

    public static final Class<?> TEST_CLASS = NaiveMBessel1.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final ModifiedBessel1stOrder M_BESSEL_1 = new NaiveMBessel1();

    @RunWith(Theories.class)
    public static class 第1種変形ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
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
                { 0.01, 0.0050000625002604172092020671 },
                { 0.5, 0.25789430539089631636247966 },
                { 1, 0.56515910399248502720769603 },
                { 1.5, 0.98166642857790758565201091 },
                { 3, 3.9533702174026093964786357 },
                { 6, 61.34193677764023786132931 },
                { 10, 2670.988303701254654341032 },
                { 15, 328124.92197020639673369815 },
                { 23.75, 1661747096.7857975265458855 },
                { 24.25, 2712287558.6254850962583971 },
                { 30, 768532038938.95699949429471 },
                { 200, 2.034581549332062703427428E+85 },
                { 500, 2.5023034121760999956900776E+215 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルの値に関するテスト_小引数 {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 99.973894118296247643039533 },
                { 0.5, 1.6564411200033008936964454 },
                { 1, 0.60190723019723457473754 },
                { 1.5, 0.27738780045684381608535966 },
                { 1.875, 0.16508225585016484516114054 },
                { 2.125, 0.11883536174242290639863282 },
                { 5, 0.0040446134454521642083650218 },
                { 10, 1.8648773453825584596816858E-5 },
                { 20, 5.8830579695570381776502822E-10 },
                { 50, 3.444102226717555612591853E-23 },
                { 100, 4.6798537356369092865625442E-45 },
                { 200, 1.228742373472985812044591E-88 },
                { 500, 3.9963119385460033495059155E-219 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselK(dataPair[0]));
        }
    }

    public static class toString表示 {
        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(M_BESSEL_1);
            System.out.println();
        }
    }
}
