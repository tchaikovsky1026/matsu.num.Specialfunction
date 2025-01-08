/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.bessel.modsbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.bessel.ModifiedSphericalBesselFunction;

/**
 * {@link MSBesselOver2} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class MSBesselOver2Test {

    public static final Class<?> TEST_CLASS = MSBesselOver2.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final IntFunction<ModifiedSphericalBesselFunction> M_BESSEL_SUPPLIER =
            order -> new MSBesselOver2(order, new MSBessel0InPrinciple(), new MSBessel1InPrinciple());

    @RunWith(Theories.class)
    public static class 次数2に関するIの値の検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 0.01696636036086197946836435 },
                { 0.99, 0.070040668004228636975119216 },
                { 1.01, 0.073104528235463453922749489 },
                { 1.5, 0.17566633204538633849660104 },
                { 2, 0.35185608855341782703802713 },
                { 5, 7.7163253464501406775573888 },
                { 10, 803.96599849134982366372075 },
                { 20, 10400728.876597379024318095 },
                { 23.75, 381760138.26834340987831452 },
                { 24.25, 618112668.00988761139781683 },
                { 50, 48798448435061526031.991111 },
                { 100, 1.3041400313520981127973853E+41 },
                { 200, 1.7795315274091181126247946E+84 },
                { 500, 1.3951875076523346203242606E+214 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数6に関するIの値の検証 {

        private final int n = 6;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=6;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 1.1659220847153854971053269E-7 },
                { 0.99, 7.1978796328382843885688274E-6 },
                { 1.01, 8.1264049314877099794553519E-6 },
                { 1.5, 9.0825874136133624150576919E-5 },
                { 2, 5.4059520862157675746708855E-4 },
                { 5, 0.25646512513279068031914518 },
                { 10, 130.99688270185084244959969 },
                { 20, 4171756.4211453246406419957 },
                { 23.75, 177010676.78442448261113025 },
                { 24.25, 291208025.69083990371647976 },
                { 50, 33941332618694613905.280411 },
                { 100, 1.0884001107971785508517531E+41 },
                { 200, 1.6260164748832311361698256E+84 },
                { 500, 1.3458063196020732024712547E+214 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数20に関するIの値の検証 {

        private final int n = 20;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=20;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,50,100,199,201,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 7.293871274440889705822263E-32 },
                { 0.99, 6.3088134654182179173108445E-26 },
                { 1.01, 9.4161455614071617141394927E-26 },
                { 1.5, 2.6030142509881891863762797E-22 },
                { 2, 8.3767284780476092021171653E-20 },
                { 5, 9.7082664411470049536989738E-12 },
                { 10, 2.371543577256102519639257E-5 },
                { 20, 568.99642862605702586984484 },
                { 23.75, 85794.719212890253254515956 },
                { 50, 790430410376739441.78742932 },
                { 100, 1.6407475505991208311218851E+40 },
                { 199, 2.3209762568271530220474273E+83 },
                { 201, 1.7158831560062220606592155E+84 },
                { 500, 9.2189227793576672973194639E+213 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数100に関するIの値の検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,50,100,200,500,1000,4999,5001,10000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 5.8909641412779681199755902E-220 },
                { 0.99, 2.7383337242944856914955144E-190 },
                { 1.01, 2.023704419708628070273522E-189 },
                { 1.5, 3.0510629527464589130830557E-172 },
                { 2, 9.5542510297070692200471954E-160 },
                { 5, 6.2611369328234648573614815E-120 },
                { 10, 9.5446386609093754939729114E-90 },
                { 20, 2.5221082180182091701591254E-59 },
                { 23.75, 1.093036522318473999880973E-51 },
                { 50, 2.3418937401088286619123467E-17 },
                { 100, 373598874159695115492.11364 },
                { 200, 3.029381074116952139785377E+73 },
                { 500, 5.9040484441551487262158816E+209 },
                { 1000, Double.POSITIVE_INFINITY },
                { 4999, Double.POSITIVE_INFINITY },
                { 5001, Double.POSITIVE_INFINITY },
                { 10000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数2に関するIのスケーリングの検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0d },
                { 0.5, 0.010290617742595889685048367 },
                { 0.99, 0.026025479653984945826097495 },
                { 1.01, 0.026626056675978113445962163 },
                { 1.5, 0.039196456801974304585394289 },
                { 2, 0.047618543402903478511354108 },
                { 5, 0.051992191212080852605535878 },
                { 10, 0.036499999862933284107835904 },
                { 20, 0.021437499999999999877063249 },
                { 23.75, 0.018505321475433736696255437 },
                { 24.25, 0.018172992955856040443819022 },
                { 50, 0.009412 },
                { 100, 0.0048515 },
                { 200, 0.0024626875 },
                { 500, 9.94012E-4 },
                { 1000, 4.985015E-4 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数6に関するIのスケーリングの検証 {

        private final int n = 6;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=6;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 7.071674912159516398843409E-8 },
                { 0.99, 2.6745642963450268780900764E-6 },
                { 1.01, 2.9597909117314486038992276E-6 },
                { 1.5, 2.0265991841616626298840069E-5 },
                { 2, 7.3161605675156817914980313E-5 },
                { 5, 0.0017280484202585657705850531 },
                { 10, 0.0059472492737684950743480481 },
                { 20, 0.0085986308593749997062855562 },
                { 23.75, 0.0085803601532053806210505375 },
                { 24.25, 0.0085617423383461916763348233 },
                { 50, 0.0065464340128 },
                { 100, 0.004048931104475 },
                { 200, 0.0022502385520076171875 },
                { 500, 9.5882999526802528E-4 },
                { 1000, 4.896043723573076975E-4 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数20に関するIのスケーリングの検証 {

        private final int n = 20;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=20;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,50,100,199,201,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 4.4239565559456591474489129E-32 },
                { 0.99, 2.3442080317554264941109975E-26 },
                { 1.01, 3.429538927872645013382773E-26 },
                { 1.5, 5.8081098669163977125147723E-23 },
                { 2, 1.133666921172772753297901E-20 },
                { 5, 6.5413784733448609491708151E-14 },
                { 10, 1.0766791183609912162264831E-9 },
                { 20, 1.1727890500171997603087992E-6 },
                { 23.75, 4.1587863707580766401332922E-6 },
                { 50, 1.5245425338401933395062049E-4 },
                { 100, 6.1037055456988203657681726E-4 },
                { 199, 8.7310967153629323767348542E-4 },
                { 201, 8.7356826471124645788232371E-4 },
                { 500, 6.5680919729381434334486823E-4 },
                { 1000, 4.0525251410925299107971008E-4 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数100に関するIのスケーリングの検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,0.99,1.01,1.5,2,5,10,20,23.75,50,100,200,500,1000,4999,5001,10000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, i_n(x)exp(-x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 3.573050366952793050315921E-220 },
                { 0.99, 1.0175009841874197606505822E-190 },
                { 1.01, 7.370715587006582614046445E-190 },
                { 1.5, 6.807841652692585688504748E-173 },
                { 2, 1.2930272692191046751656827E-160 },
                { 5, 4.2187208807381050827729912E-122 },
                { 10, 4.3332592481358311518506229E-94 },
                { 20, 5.1984524897502877950162098E-68 },
                { 23.75, 5.2983510330970406134356437E-62 },
                { 50, 4.5169271951825540670405488E-39 },
                { 100, 1.3898161964299132789305328E-23 },
                { 200, 4.1923499466324573158960232E-14 },
                { 500, 4.2063844249485366907436149E-8 },
                { 1000, 3.2101923796404594383570633E-6 },
                { 4999, 3.6419383439970239801144197E-5 },
                { 5001, 3.6419530574819279664318652E-5 },
                { 10000, 3.0174645068120473526783053E-5 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).sbesselIc(dataPair[0]));
        }
    }
}
