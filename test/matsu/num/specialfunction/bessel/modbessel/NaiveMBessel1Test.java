package matsu.num.specialfunction.bessel.modbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NaiveMBessel1}クラスのテスト.
 * 元クラスは非推奨.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings("deprecation")
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
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルの値に関するテスト {

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
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第1種変形ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.01, 0.0049503110471182756055488071 },
                { 0.5, 0.15642080318487169714264553 },
                { 1, 0.20791041534970844886935469 },
                { 1.5, 0.21903938742092567211511011 },
                { 3, 0.19682671329730085363074483 },
                { 6, 0.15205145930850588400161232 },
                { 10, 0.12126268138445551871895497 },
                { 15, 0.10037417504516665529170771 },
                { 23.75, 0.080551008746949244822593534 },
                { 24.25, 0.079743362736530173494062674 },
                { 30, 0.071916330598647554706128737 },
                { 200, 0.028156503394832917822456582 },
                { 500, 0.017827851852898056461382459 },
                { 1000, 0.012610930256928629470237564 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)exp(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 100.97864845824005116018855 },
                { 0.5, 2.7310097082117857053591531 },
                { 1, 1.6361534862632582465133111 },
                { 1.5, 1.2431658735525529948002638 },
                { 1.875, 1.0764715303749825044464619 },
                { 2.125, 0.99499630183382759830773066 },
                { 5, 0.60027385878831258293604566 },
                { 10, 0.41076657059578875113004694 },
                { 20, 0.28542549694072644517352917 },
                { 50, 0.17856655855881557460060567 },
                { 100, 0.12579995047957852932510386 },
                { 200, 0.088788601585003679764258535 },
                { 500, 0.056091923370555569240004394 },
                { 1000, 0.039648130812960210480145928 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.besselKc(dataPair[0]));
        }
    }
}
