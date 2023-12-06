package matsu.num.specialfunction.icbeta;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * {@link ICBetaAtHighParam}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ICBetaAtHighParamTest {

    public static final Class<?> TEST_CLASS = ICBetaAtHighParam.class;

    @RunWith(Enclosed.class)
    public static class テスト_A_50000_B_100000 {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.4925373134328360, 0.00305695366854363000 },
                    { 0.4947683109118090, 0.0282914458944163000 },
                    { 0.4970059880239520, 0.158257260613666000 },
                    { 0.4992503748125940, 0.6455815888148510 },
                    { 0.5015015015015020, 2.4270617768003100 },
                    { 0.5037593984962410, 10.6911981207634000 },
                    { 0.5060240963855420, 68.8663826343325000 },
                    { 0.5082956259426850, 758.5770952477130000 },
                    { 0.5105740181268880, 15345.2156398790000000 },
                    { 4.9726638719582300E-01, 1.8857436718872800E-01 },
                    { 4.9781231238454100E-01, 2.6890624298428100E-01 },
                    { 4.9835863582305900E-01, 3.7813897132271900E-01 },
                    { 4.9890535794731700E-01, 5.2617520914924000E-01 },
                    { 4.9945247919389200E-01, 7.2702371941777200E-01 },
                    { 5.0000000000000000E-01, 1.0009716269499800E+00 },
                    { 5.0054792080349500E-01, 1.3780521039618700E+00 },
                    { 5.0109624204287000E-01, 1.9036787889144100E+00 },
                    { 5.0164496415726100E-01, 2.6479815223034400E+00 },
                    { 5.0219408758644500E-01, 3.7215964459124600E+00 },
                    { 5.0274361277084400E-01, 5.3029476814728000E+00 }
            };

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = new ICBetaAtHighParam(50000, 100000);
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ibf.ribetaOdds(dataPair[0]), is(closeTo(dataPair[1], Math.abs(dataPair[1]) * 1E-5)));
            }
        }

        public static class 特殊値の検証 {

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = new ICBetaAtHighParam(50000, 100000);
            }

            @Test
            public void test_0検証_オッズ() {
                assertThat(ibf.ribetaOdds(0), is(0.0));
            }

            @Test
            public void test_正の無限大検証_オッズ() {
                assertThat(ibf.ribetaOdds(Double.POSITIVE_INFINITY), is(Double.POSITIVE_INFINITY));
            }

            @Test
            public void test_0検証_beta() {
                assertThat(ibf.ribeta(0), is(0.0));
            }

            @Test
            public void test_1検証_beta() {
                assertThat(ibf.ribeta(1.0), is(1.0));
            }

            @Test
            public void test_0検証_betaR() {
                assertThat(ibf.ribetaR(0), is(0.0));
            }

            @Test
            public void test_1検証_betaR() {
                assertThat(ibf.ribetaR(1.0), is(1.0));
            }
        }
    }

    public static class toString表示 {
        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new ICBetaAtHighParam(50000, 100000));
            System.out.println();

        }
    }
}
