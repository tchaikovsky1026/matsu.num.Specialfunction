/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
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
 * {@link SBesselHigher} クラスのテスト.
 */
@RunWith(Enclosed.class)
final class SBesselHigherTest {

    public static final Class<?> TEST_CLASS = SBesselHigher.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final IntFunction<
            SphericalBesselFunction> S_BESSEL_SUPPLIER = n -> new SBesselHigherImpl(
                    n, new SBessel0InPrinciple(), new SBessel1InPrinciple());

    private static final class SBesselHigherImpl extends SBesselHigher {

        public SBesselHigherImpl(int order, SBessel0 sbessel0, SBessel1 sbessel1) {
            super(order, sbessel0, sbessel1);
        }

        @Override
        public double sbesselJ(double x) {
            throw new UnsupportedOperationException("sbesselJを呼んではいけない");
        }
    }

    @RunWith(Theories.class)
    public static class 次数2におけるyの検証 {

        private final int n = 2;

        /* 値の生成コード(https://keisan.casio.jp/calculator) */
        /* ------------------------------------ */
        //        n=2;
        //        numeric xs[] = {0,1E-12,0.01,0.5,1,1.5,3,200};
        //
        //        for(index = 0; index < kei_length(xs); index = index + 1){
        //            x = xs[index];
        //            println(x,sphericalbessely(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.NEGATIVE_INFINITY },
                { 1.E-12, -3.0000000000000000000000005E+36 },
                { 0.01, -3000050.001249979166753472 },
                { 0.5, -25.05992282483863575783765 },
                { 1, -3.6050175661599689547593802 },
                { 1.5, -1.3457126936204509990530067 },
                { 3, -0.26703833526449917564948667 },
                { 200, 0.0025012529769479515181873329 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_SUPPLIER.apply(n).sbesselY(dataPair[0]));
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
        //            println(x,sphericalbessely(n,x));
        //        }
        /* ------------------------------------ */

        /**
         * [x, y_n(x)]
         */
        @DataPoints
        public static double[][] dataPairs = {
                { 0, Double.NEGATIVE_INFINITY },
                { 1.E-12, Double.NEGATIVE_INFINITY },
                { 0.01, Double.NEGATIVE_INFINITY },
                { 0.5, -1.6911720011753779564595198E+217 },
                { 1, -6.6830794632586775137802682E+186 },
                { 1.5, -1.099318425413112238988602E+169 },
                { 3, -4.4102229953030095731108482E+138 },
                { 200, -0.0050166682419773059100559224 },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    S_BESSEL_SUPPLIER.apply(n).sbesselY(dataPair[0]));
        }
    }
}
