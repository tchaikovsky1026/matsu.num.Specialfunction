package matsu.num.specialfunction.bessel.sbessel;

import java.util.function.IntFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.bessel.SphericalBesselFunction;

/**
 * {@link SBesselOver2} クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class SBesselOver2Test {

    public static final Class<?> TEST_CLASS = SBesselOver2.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final IntFunction<
            SphericalBesselFunction> S_BESSEL_SUPPLIER = n -> new SBesselOver2(
                    n, new SBessel0InPrinciple(), new SBessel1InPrinciple());

    @RunWith(Theories.class)
    public static class 次数2におけるjの検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,200};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselj(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 1.E-12, 6.66666666666666667E-26 },
                { 0.01, 6.6666190477513225509060769E-6 },
                { 0.5, 0.016371106607993412616955584 },
                { 1, 0.062035052011373861102194821 },
                { 1.5, 0.12734928368840821564724095 },
                { 3, 0.29863749707573354751258098 },
                { 200, 0.0043296199239579922174203361 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_SUPPLIER.apply(n).sbesselJ(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 次数100におけるyの検証 {

        private final int n = 100;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=100;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,200};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbesselj(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, 0 },
                { 1.E-12, 0 },
                { 0.01, 0 },
                { 0.5, 5.883713724788949162006959E-220 },
                { 1, 7.4447277416610768908497048E-190 },
                { 1.5, 3.0174324725697612051674208E-172 },
                { 3, 3.76197354100477660180952E-142 },
                { 200, -0.0019360972362475567977403233 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_SUPPLIER.apply(n).sbesselJ(dataPair[0]));
        }
    }
}
