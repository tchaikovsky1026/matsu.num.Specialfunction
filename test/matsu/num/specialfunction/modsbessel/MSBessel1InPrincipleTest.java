package matsu.num.specialfunction.modsbessel;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link MSBessel1InPrinciple} クラスのテスト
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class MSBessel1InPrincipleTest {

    public static final Class<?> TEST_CLASS = MSBessel1InPrinciple.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final MSBessel1 M_BESSEL_1 = new MSBessel1InPrinciple();

    @RunWith(Theories.class)
    public static class 第1種変形球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,0.99,1.01,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
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
                { 0.01, 0.00333336666678571450617309 },
                { 0.5, 0.17087070843777212396274782 },
                { 0.99, 0.36349620977980731622626987 },
                { 1.01, 0.37228514806829283288479601 },
                { 1.5, 0.62192665234224599636316198 },
                { 3, 2.2427901177692661807652461 },
                { 6, 28.016129426790448299318017 },
                { 10, 991.19096326329838019977236 },
                { 15, 101702.76269914320752745333 },
                { 23.75, 416023748.30831069940961162 },
                { 24.25, 672373613.66419825665393864 },
                { 30, 172170979369.00522347929089 },
                { 200, 1.7974609748212801279716474E+84 },
                { 500, 1.4007850334171317359182908E+214 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.sbesselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形球ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, k_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 9999.5033208665973410964504 },
                { 0.5, 3.6391839582758005416227972 },
                { 1, 0.73575888234288464319104754 },
                { 1.5, 0.24792240016492203214808941 },
                { 1.875, 0.12541028399763038776170606 },
                { 2.125, 0.082652573194961673353548061 },
                { 5, 0.0016171072797805121031926516 },
                { 10, 4.9939922738733336689150667E-6 },
                { 20, 1.0821056517802428596821187E-10 },
                { 50, 3.9346496898463922773553793E-24 },
                { 100, 3.7572767357810443225892928E-46 },
                { 200, 6.9540800468521060915096243E-90 },
                { 500, 1.4277651119109536205224511E-220 },
                { 1000, 0 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.sbesselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第1種変形球ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,0.99,1.01,1.5,3,6,10,15,23.75,24.25,30,200,500,1000};
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
                { 0.01, 0.0033001991142762151112263378 },
                { 0.5, 0.10363832351432696478657131 },
                { 0.99, 0.13506671882903616613113557 },
                { 1.01, 0.13559331673906708158655228 },
                { 1.5, 0.13877059353770219054407912 },
                { 3, 0.11166194492814807964956559 },
                { 6, 0.069445041798423240242615427 },
                { 10, 0.045000000113363449234120681 },
                { 15, 0.031111111111114438265944477 },
                { 23.75, 0.020166204986149584487586176 },
                { 24.25, 0.019768306940163673078985504 },
                { 30, 0.016111111111111111111111111 },
                { 200, 0.0024875 },
                { 500, 9.98E-4 },
                { 1000, 4.995E-4 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.sbesselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形球ベッセルのスケーリングに関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=1;
        //        numeric xs[] = {0,0.01,0.5,1,1.5,1.875,2.125,5,10,20,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, k_n(x)exp(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.01, 10100 },
                { 0.5, 6 },
                { 1, 2 },
                { 1.5, 1.1111111111111111111111111 },
                { 1.875, 0.81777777777777777777777778 },
                { 2.125, 0.69204152249134948096885813 },
                { 5, 0.24 },
                { 10, 0.11 },
                { 20, 0.0525 },
                { 50, 0.0204 },
                { 100, 0.0101 },
                { 200, 0.005025 },
                { 500, 0.002004 },
                { 1000, 0.001001 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_1.sbesselKc(dataPair[0]));
        }
    }
}
