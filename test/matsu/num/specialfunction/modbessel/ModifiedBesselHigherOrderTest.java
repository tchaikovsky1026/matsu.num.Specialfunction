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
            new DoubleRelativeAssertion(1E-15);

    private static final IntFunction<ModifiedBesselHigherOrder> M_BESSEL_SUPPLIER =
            order -> new ModifiedBesselHigherOrderImpl(order);

    /**
     * 高次ベッセルのダミー実装.
     * K_n(x)しか使えない.
     */
    private static final class ModifiedBesselHigherOrderImpl extends ModifiedBesselHigherOrder {

        ModifiedBesselHigherOrderImpl(int order) {
            super(order, new NaiveMBessel0(), new NaiveMBessel1());
        }

        @Override
        public double besselI(double x) {
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
}
