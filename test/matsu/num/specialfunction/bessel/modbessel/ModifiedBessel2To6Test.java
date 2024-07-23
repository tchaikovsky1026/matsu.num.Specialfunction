package matsu.num.specialfunction.bessel.modbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ModifiedBessel2To6} クラスのテスト.
 * 元クラスは非推奨.
 * 
 * @author Matsuura Y.
 */
@SuppressWarnings({ "deprecation", "removal" })
@RunWith(Enclosed.class)
final class ModifiedBessel2To6Test {

    public static final Class<?> TEST_CLASS = ModifiedBessel2To6.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

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
         * [x, I_n(x)]
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
         * [x, I_n(x)]
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

    @RunWith(Theories.class)
    public static class 次数2に関するIのスケーリングの検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
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
                { 0, 0d },
                { 0.5, 0.01935205770966327953741452 },
                { 1, 0.049938776894223538763192159 },
                { 1.5, 0.075381092492924109751428024 },
                { 2, 0.093239033304733380374879176 },
                { 5, 0.11795190583151141030320159 },
                { 10, 0.10358080088653750357925929 },
                { 20, 0.081029689666497155060311647 },
                { 23.75, 0.075519517896777810689718229 },
                { 24.25, 0.074863854232484977339195242 },
                { 50, 0.05432190169173837654418404 },
                { 100, 0.039149496238594077594085927 },
                { 200, 0.02794559491516358649211606 },
                { 500, 0.017774395092741575010690668 },
                { 1000, 0.012592018595377399326775656 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselIc(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数6に関するIのスケーリングの検証 {

        private final int n = 6;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=6;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,50,100,200,500,1000};
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
                { 0.5, 2.0750844834613875438864618E-7 },
                { 1, 8.2731162169067918833882499E-6 },
                { 1.5, 5.9747371982295452022298295E-5 },
                { 2, 2.1655991537989607768089245E-4 },
                { 5, 0.0053383788458419933411727926 },
                { 10, 0.020398290653699230485184123 },
                { 20, 0.035917383144805844536303721 },
                { 23.75, 0.03810257953547447723920543 },
                { 24.25, 0.038313270517666141336838999 },
                { 50, 0.039334717675906962841729733 },
                { 100, 0.033335858977284710368664995 },
                { 200, 0.025792022929781750198925595 },
                { 500, 0.01721407397210055847161696 },
                { 1000, 0.012392050932874427978137372 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselIc(dataPair[0]));
        }
    }
}
