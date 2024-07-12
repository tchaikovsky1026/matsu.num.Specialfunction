package matsu.num.specialfunction.modbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ModifiedBesselOver2} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ModifiedBesselOver2Test {

    public static final Class<?> TEST_CLASS = ModifiedBesselOver2.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final IntFunction<ModifiedBesselHigherOrder> M_BESSEL_SUPPLIER =
            order -> new ModifiedBesselOver2(order, new NaiveMBessel0(), new NaiveMBessel1());

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

    @RunWith(Theories.class)
    public static class 次数7に関するIの値の検証 {

        private final int n = 7;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=7;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,24.75,50,100,200,500,1000};
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
                { 23.75, 596493509.59290147422063692 },
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

    @RunWith(Theories.class)
    public static class 次数20に関するIの値の検証 {

        private final int n = 20;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=20;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,50,100,199,201,500,1000};
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
                { 23.75, 493668.64926561252518124133 },
                { 50, 5442008402752997526.5214032 },
                { 100, 1.448346125642717164103143E+41 },
                { 199, 2.7488511382580216517471788E+84 },
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

    @RunWith(Theories.class)
    public static class 次数100に関するIの値の検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,50,100,200,500,1000,4999,5001,10000};
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
                { 0.5, 6.6721524108446570183412808E-219 },
                { 1, 8.4736740081380788652655517E-189 },
                { 1.5, 3.4557422099691972918525357E-171 },
                { 2, 1.0821714745498605306876851E-158 },
                { 5, 7.0935514885313117501373461E-119 },
                { 10, 1.0823442017492016448309218E-88 },
                { 20, 2.8703193216428772482632608E-58 },
                { 23.75, 1.2463523819855180551585915E-50 },
                { 50, 2.7278879470966916083316219E-16 },
                { 100, 4641534941616199113517.8489 },
                { 200, 4.3527504497270219143872902E+74 },
                { 500, 1.1637732868604370887898067E+211 },
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
         * [x, K_n(x)]
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
         * [x, K_n(x)]
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

    @RunWith(Theories.class)
    public static class 次数7に関するIのスケーリングの検証 {

        private final int n = 7;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=7;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,24.25,24.75,50,100,200,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 7.4027611628338300592455719E-9 },
                { 1, 5.8831950920540457858724012E-7 },
                { 1.5, 6.3383284060808619512431881E-6 },
                { 2, 3.0401601908781348106227271E-5 },
                { 5, 0.0017282088952139872520627176 },
                { 10, 0.010806344830494886139503473 },
                { 20, 0.025894012606505573719672977 },
                { 23.75, 0.028914239718939470842353193 },
                { 24.25, 0.029239209884908098964822701 },
                { 24.75, 0.029548354940625933003009341 },
                { 50, 0.034507164782405599718050007 },
                { 100, 0.031229165630467613268044223 },
                { 200, 0.02496536343393203319630522 },
                { 500, 0.016991524241426739233588445 },
                { 1000, 0.012311724329574369804332509 },
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
    public static class 次数20に関するIのスケーリングの検証 {

        private final int n = 20;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=20;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,50,100,199,201,500,1000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 2.2741587160374397306500871E-31 },
                { 1, 1.4593174056818685960679868E-25 },
                { 1.5, 2.9873406275088970021768812E-22 },
                { 2, 5.8337093647636210402362137E-20 },
                { 5, 3.3853058504733224061538687E-13 },
                { 10, 5.6786220145215239128014839E-9 },
                { 20, 6.5725042913687706741617656E-6 },
                { 23.75, 2.3929939617168367262036212E-5 },
                { 50, 0.0010496272879428207032812955 },
                { 100, 0.0053879576269663273677696626 },
                { 199, 0.010340685336038587038919336 },
                { 201, 0.010392715259680305317620055 },
                { 500, 0.011958181720054547742880982 },
                { 1000, 0.010329157758475194371041681 },
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
    public static class 次数100に関するIのスケーリングの検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,0.5,1,1.5,2,5,10,20,23.75,50,100,200,500,1000,4999,5001,10000};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,besseli(n,x)*exp(-x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, I_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 0.5, 4.0468650034528473833100849E-219 },
                { 1, 3.1172904587828122481666449E-189 },
                { 1.5, 7.7108031274211581186586754E-172 },
                { 2, 1.4645598301878817821528932E-159 },
                { 5, 4.779597396500780017493446E-121 },
                { 10, 4.9138350738246488463155396E-93 },
                { 20, 5.9161690673596004380275848E-67 },
                { 23.75, 6.0415295334219920076802289E-61 },
                { 50, 5.2614134632253477360609733E-38 },
                { 100, 1.7266862628167695784749896E-22 },
                { 200, 6.0237562291289978935082641E-13 },
                { 500, 8.2913917023616281971922332E-7 },
                { 1000, 8.5155875815481560662771115E-5 },
                { 4999, 0.0020752433734753077256904431 },
                { 5001, 0.0020756585259550656857115906 },
                { 10000, 0.0024196870787293211690425838 },
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
