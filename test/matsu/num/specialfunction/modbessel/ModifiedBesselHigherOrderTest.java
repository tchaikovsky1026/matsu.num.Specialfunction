package matsu.num.specialfunction.modbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link ModifiedBesselHigherOrder}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ModifiedBesselHigherOrderTest {

    public static final Class<?> TEST_CLASS = ModifiedBesselHigherOrder.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-14);

    private static final IntFunction<ModifiedBesselHigherOrder> M_BESSEL_SUPPLIER =
            order -> new ModifiedBesselHigherOrderImpl(order);

    /**
     * 高次ベッセルのダミー実装.
     * K_n(x)しか使えない.
     */
    private static final class ModifiedBesselHigherOrderImpl extends ModifiedBesselHigherOrder {

        ModifiedBesselHigherOrderImpl(int order) {
            super(order, new MBessel0Optimized(), new MBessel1Optimized());
        }

        @Override
        public double besselI(double x) {
            throw new UnsupportedOperationException("bessel_Iは呼んではいけない");
        }

        @Override
        public double besselIc(double x) {
            throw new UnsupportedOperationException("bessel_Iは呼んではいけない");
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
        //            println(x,besselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 7.5501835512408694365677058 },
                { 1, 1.6248388986351774828107074 },
                { 1.5, 0.58365596325665082483543388 },
                { 2, 0.25375975456605586293731838 },
                { 5, 0.0053089437122234599580812697 },
                { 10, 2.1509817006932768730664564E-5 },
                { 20, 6.3295436122922281104817303E-10 },
                { 50, 3.5479318388581977384243496E-23 },
                { 100, 4.7502253038886402046702562E-45 },
                { 200, 1.2379694035112633097805E-88 },
                { 500, 4.0083068568719768907546592E-219 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselK(dataPair[0]));
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
        //            println(x,besselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 188937569319.90025964462418 },
                { 1, 180713289.90102945469159786 },
                { 1.5, 3027483.5236822367062754964 },
                { 2, 162482.40397955914871834793 },
                { 5, 9.7585628291778101317423674 },
                { 10, 0.0016142553003906700234572519 },
                { 20, 6.3162145283215797622802549E-9 },
                { 50, 9.150988209987996111536184E-23 },
                { 100, 7.6554279773881006110979879E-45 },
                { 200, 1.5727481296450991560792174E-88 },
                { 500, 4.4117428767318521938832843E-219 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselK(dataPair[0]));
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
        //            println(x,besselk(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 7.4937399313527486863576434E+215 },
                { 1, 5.900333183638615857074819E+185 },
                { 1.5, 1.4467044226486076488169365E+168 },
                { 2, 4.6194159776012747217482502E+155 },
                { 5, 7.0398601930616765465338662E+115 },
                { 10, 4.5966740842695282400289218E+85 },
                { 20, 1.7081356456876032997781521E+55 },
                { 50, 16394035276269.252222589528 },
                { 100, 7.6171296304940854161829685E-25 },
                { 200, 5.137138007722447969163925E-78 },
                { 500, 8.4258768687230154098218643E-215 },
                { 1000, 0d },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselK(dataPair[0]));
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
        //            println(x,besselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 12.448148218621052351459528 },
                { 1, 4.4167700523334115077256636 },
                { 1.5, 2.61576455136496715615044 },
                { 2, 1.875045062139459991094482 },
                { 5, 0.78791710782884402004261983 },
                { 10, 0.47378524855575641595993045 },
                { 20, 0.30708742635125486845051669 },
                { 50, 0.18394981819978196109578635 },
                { 100, 0.1276916206687181494780602 },
                { 200, 0.089455344355146695031527059 },
                { 500, 0.056260283110716737705322629 },
                { 1000, 0.039707617862380137535686214 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselKc(dataPair[0]));
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
        //            println(x,besselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 311505389372.09950263264479 },
                { 1, 491229652.09901985987626974 },
                { 1.5, 13568239.818715252080031974 },
                { 2, 1200591.5980940752813735205 },
                { 5, 1448.2991377792564036427028 },
                { 10, 35.556339158140534521834328 },
                { 20, 3.0644074558832955753481647 },
                { 50, 0.47445179164359881450397045 },
                { 100, 0.20578687173955779807241273 },
                { 200, 0.11364636728684321562190249 },
                { 500, 0.061922879689485175071705714 },
                { 1000, 0.041659051428005657878964015 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselKc(dataPair[0]));
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
        //            println(x,besselk(n,x)*exp(x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, K_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.POSITIVE_INFINITY },
                { 0.5, 1.2355088421916194883266141E+216 },
                { 1, 1.6038768474938756238346628E+186 },
                { 1.5, 6.4836793989940052244215733E+168 },
                { 2, 3.4133123802792390924533655E+156 },
                { 5, 1.0448078908927582452710873E+118 },
                { 10, 1.0124848448703725016236663E+90 },
                { 20, 8.2872796432645434507477333E+63 },
                { 50, 8.4998245332724685900862237E+34 },
                { 100, 20475736731166756812.959944 },
                { 200, 3712082448.7044181743549815 },
                { 500, 1182.6495201525858169930894 },
                { 1000, 5.8424465521265156526734722 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    M_BESSEL_SUPPLIER.apply(n).besselKc(dataPair[0]));
        }
    }
}
