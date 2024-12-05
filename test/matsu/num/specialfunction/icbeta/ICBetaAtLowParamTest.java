package matsu.num.specialfunction.icbeta;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * {@link ICBetaAtLowParam}クラスのテスト.
 * 
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ICBetaAtLowParamTest {

    public static final Class<?> TEST_CLASS = ICBetaAtLowParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-8);

    @RunWith(Theories.class)
    public static class A_0_1_B_2のオッズ値のテスト {

        private static final IncompleteBetaFunction IC_BETA =
                new ICBetaAtLowParam(0.1, 2);

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
                { 98.9999999999999000, 180725.0393072710000000 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_BETA.ribetaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class A_2_B_15のオッズ値のテスト {

        private static final IncompleteBetaFunction IC_BETA =
                new ICBetaAtLowParam(1, 15);

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
                { 1.0000000000000000, 32767.0000000000000000 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_BETA.ribetaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 境界値テスト {

        @DataPoints
        public static final IncompleteBetaFunction[] icbetas = {
                new ICBetaAtLowParam(0.1, 2),
                new ICBetaAtLowParam(1, 15)
        };

        @Theory
        public void test_0検証_beta(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(0d, icbeta.ribeta(0d));
        }

        @Theory
        public void test_1検証_beta(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, icbeta.ribeta(1d));
        }

        @Theory
        public void test_0検証_betaR(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(0d, icbeta.ribetaR(0d));
        }

        @Theory
        public void test_1検証_betaR(IncompleteBetaFunction icbeta) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(1d, icbeta.ribetaR(1d));
        }
    }

    public static class toString表示 {
        @Test
        public void test_toString() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new ICBetaAtLowParam(1, 15));
            System.out.println();

        }
    }
}
