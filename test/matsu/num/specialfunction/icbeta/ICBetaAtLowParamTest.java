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
 * {@link ICBetaAtLowParam}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ICBetaAtLowParamTest {

    @RunWith(Enclosed.class)
    public static class テスト_A_0_1_B_2 {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.0101010101010101, 2.2618139481095000 },
                    { 0.0152284263959391, 2.5943711647101700 },
                    { 0.0204081632653061, 2.8837241709004600 },
                    { 0.0309278350515464, 3.3963009833956000 },
                    { 0.0526315789473684, 4.3062283202275300 },
                    { 0.0752688172043011, 5.1644663605152000 },
                    { 0.1111111111111110, 6.4525519779590400 },
                    { 0.1764705882352940, 8.7569644638966400 },
                    { 0.2500000000000000, 11.4142048595421000 },
                    { 0.4285714285714290, 18.4658269921215000 },
                    { 1.0000000000000000, 48.2238412126597000 },
                    { 2.3333333333333300, 163.2016704620620000 },
                    { 4.0000000000000000, 397.6325172658630000 },
                    { 5.6666666666666700, 733.0248961113680000 },
                    { 9.0000000000000000, 1706.7983882116900000 },
                    { 13.2857142857143000, 3552.4589558747800000 },
                    { 19.0000000000000000, 7052.2866355305100000 },
                    { 32.3333333333333000, 19836.1378833026000000 },
                    { 49.0000000000000000, 44906.8512616208000000 },
                    { 65.6666666666666000, 80078.5715570533000000 },
                    { 98.9999999999999000, 180725.0393072710000000 }
            };

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = IncompleteBetaFunction.instanceOf(0.1, 2);
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ibf.ribetaOdds(dataPair[0]), is(closeTo(dataPair[1], Math.abs(dataPair[1]) * 1E-8)));
            }
        }

        public static class 特殊値の検証 {

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = IncompleteBetaFunction.instanceOf(0.1, 2);
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

    @RunWith(Enclosed.class)
    public static class テスト_A_2_B_15 {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.0101010101010101, 0.1627118027556140 },
                    { 0.0152284263959391, 0.2544592047327750 },
                    { 0.0204081632653061, 0.3539694477039510 },
                    { 0.0309278350515464, 0.5791521866119840 },
                    { 0.0526315789473684, 1.1584695217631800 },
                    { 0.0752688172043011, 1.9699953659361500 },
                    { 0.1111111111111110, 3.8569357496188600 },
                    { 0.1764705882352940, 10.4476439751686000 },
                    { 0.2500000000000000, 27.4217094304040000 },
                    { 0.4285714285714290, 209.6344484227650000 },
                    { 1.0000000000000000, 32767.0000000000000000 }
            };

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = IncompleteBetaFunction.instanceOf(1, 15);
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(ibf.ribetaOdds(dataPair[0]), is(closeTo(dataPair[1], Math.abs(dataPair[1]) * 1E-8)));
            }
        }

        public static class 特殊値の検証 {

            private IncompleteBetaFunction ibf;

            @Before
            public void before_不完全ベータ関数の作成() {
                ibf = IncompleteBetaFunction.instanceOf(1, 15);
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
}
