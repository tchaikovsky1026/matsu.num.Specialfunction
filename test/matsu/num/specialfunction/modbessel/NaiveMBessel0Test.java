package matsu.num.specialfunction.modbessel;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link NaiveMBessel0}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class NaiveMBessel0Test {

    public static final Class<?> TEST_CLASS = NaiveMBessel0.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-15);

    private static final ModifiedBessel0thOrder M_BESSEL_0 = new NaiveMBessel0();

    @RunWith(Theories.class)
    public static class 第1種変形ベッセルの値に関するテスト {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
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
                { 0, 1, Double.POSITIVE_INFINITY },
                { 0.01, 1.000025000156250434028 },
                { 0.5, 1.063483370741323519263 },
                { 1, 1.266065877752008335598 },
                { 1.5, 1.646723189772890844876 },
                { 3, 4.880792585865024085611 },
                { 6, 67.23440697647797532619 },
                { 10, 2815.71662846625447147 },
                { 15, 339649.37329791387952170163 },
                { 23.75, 1697885300.7852367601621023 },
                { 24.25, 2770016089.3283488970557653 },
                { 30, 781672297823.97748971738982 },
                { 200, 2.0396871734097246195416731E+85 },
                { 500, 2.5048094765700780965514121E+215 },
                { 1000, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselI(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 第2種変形ベッセルの値に関するテスト_小引数 {

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=0;
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
                { 0.01, 4.7212447301610949651358777 },
                { 0.5, 0.92441907122766586178192417 },
                { 1, 0.42102443824070833333562738 },
                { 1.5, 0.213805562647525736721621 },
                { 1.875, 0.13290483903459228511813344 },
                { 2.125, 0.097764257654025402892179078 },
                { 5, 0.003691098334042594274735261 },
                { 10, 1.7780062316167651811301193E-5 },
                { 20, 5.7412378153365242927167021E-10 },
                { 50, 3.4101677497894955139206755E-23 },
                { 100, 4.6566282291759020189390053E-45 },
                { 200, 1.2256819797765334516600541E-88 },
                { 500, 3.9923216091177928773566356E-219 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_0.besselK(dataPair[0]));
        }
    }

    public static class toString表示 {
        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(M_BESSEL_0);
            System.out.println();
        }
    }
}
