package matsu.num.specialfunction.bessel.modsbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link MSBesselHigherImplK} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class MSBesselHigherImplKTest {

    public static final Class<?> TEST_CLASS = MSBesselHigherImplK.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final IntFunction<MSBesselHigherImplK> MS_BESSEL_SUPPLIER =
            order -> new MSBesselHigherImplKImpl(order);

    /**
     * 高次ベッセルのダミー実装.
     * K_n(x)しか使えない.
     */
    private static final class MSBesselHigherImplKImpl extends MSBesselHigherImplK {

        MSBesselHigherImplKImpl(int order) {
            super(order, new MSBessel0InPrinciple(), new MSBessel1InPrinciple());
        }

        @Override
        public double sbesselI(double x) {
            throw new UnsupportedOperationException("sbIは呼んではいけない");
        }

        @Override
        public double sbesselIc(double x) {
            throw new UnsupportedOperationException("sbIは呼んではいけない");
        }
    }

    @RunWith(Theories.class)
    public static class 次数2に関するKの値の検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 23.048165069080070096944382 },
                { 1, 2.5751560882000962511686664 },
                { 1.5, 0.64459824042879728358503247 },
                { 2, 0.21991983525949562432774918 },
                { 5, 0.0023178537676854006812428007 },
                { 10, 6.0381906584104852542336716E-6 },
                { 20, 1.192892658986315342935288E-10 },
                { 50, 4.0935786773186191026760084E-24 },
                { 100, 3.8327942780942672926373746E-46 },
                { 200, 7.0237938343864692446160516E-90 },
                { 500, 1.4334818720197228280329662E-220 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数10に関するKの値の検証 {

        private final int n = 10;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=10;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 1332095875326.0714489255217 },
                { 1, 637749952.13897442981374057 },
                { 1.5, 7135618.28078003161934666 },
                { 2, 287934.95213388073012962821 },
                { 5, 7.1060633269525929316405878 },
                { 10, 6.3073886444940276014946587E-4 },
                { 20, 1.434397803897119706639762E-9 },
                { 50, 1.1421239521343081248041154E-23 },
                { 100, 6.4271633706613653850303262E-46 },
                { 200, 9.1029166810820634865395515E-90 },
                { 500, 1.5904206975168576985324232E-220 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数100に関するKの値の検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 1.6890487472156316051516353E+217 },
                { 1, 6.6495803883809950812217783E+186 },
                { 1.5, 1.0869589487814474109447131E+169 },
                { 2, 2.6031027492352630948790677E+156 },
                { 5, 1.5872446343969873200580863E+116 },
                { 10, 5.1868644491662527620890974E+85 },
                { 20, 9.6733237127706738577307596E+54 },
                { 50, 3804001347627.3901320995924 },
                { 100, 9.4397758890859536459165422E-26 },
                { 200, 3.6869406558597449121647876E-79 },
                { 500, 3.3210840321244211317524441E-216 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselK(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数2に関するKのスケーリングの検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 38 },
                { 1, 7 },
                { 1.5, 2.8888888888888888888888889 },
                { 2, 1.625 },
                { 5, 0.344 },
                { 10, 0.133 },
                { 20, 0.057875 },
                { 50, 0.021224 },
                { 100, 0.010303 },
                { 200, 0.005075375 },
                { 500, 0.002012024 },
                { 1000, 0.001003003 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselKc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数10に関するKのスケーリングの検証 {

        private final int n = 10;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=10;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 2196254804262d },
                { 1, 1733584106 },
                { 1.5, 31979622.459076360310928212 },
                { 2, 2127567.51416015625 },
                { 5, 1054.633307136 },
                { 10, 13.89294802325 },
                { 20, 0.6959198908231201171875 },
                { 50, 0.05921576368962464256 },
                { 100, 0.0172769680299275886575 },
                { 200, 0.0065777437150933297628942871 },
                { 500, 0.0022323021141467429616138579 },
                { 1000, 0.0010565110581718461025902088 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselKc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数100に関するKのスケーリングの検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,50,100,200,500,1000};
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
                { 0.5, 2.7847705967838156732228581E+217 },
                { 1, 1.807543353661369946427405E+187 },
                { 1.5, 4.8714120406599652646062359E+169 },
                { 2, 1.9234472245379963772930476E+157 },
                { 5, 2.35567990459471111647598E+118 },
                { 10, 1.1424829237185944750567768E+90 },
                { 20, 4.6931599893685419856087932E+63 },
                { 50, 1.9722626817796403746653012E+34 },
                { 100, 2537522338235460254.6073558 },
                { 200, 266417364.63878862274892963 },
                { 500, 46.614477023251601829642497 },
                { 1000, 0.15497325543253054576309703 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    MS_BESSEL_SUPPLIER.apply(n).sbesselKc(dataPair[0]));
        }
    }
}
