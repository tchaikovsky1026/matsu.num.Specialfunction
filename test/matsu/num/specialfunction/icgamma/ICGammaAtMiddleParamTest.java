package matsu.num.specialfunction.icgamma;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;
import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@link ICGammaAtMiddleParam}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
final class ICGammaAtMiddleParamTest {

    public static final Class<?> TEST_CLASS = ICGammaAtMiddleParam.class;

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-8);

    @RunWith(Theories.class)
    public static class A_20のオッズ値のテスト {

        private static final IncompleteGammaFunction IC_GAMMA =
                new ICGammaAtMiddleParam(20);

        @DataPoints
        public static double[][] dataPairs = {
                { 5.00E+00, 0.00000034521370126 },
                { 7.00E+00, 0.00004440444820711 },
                { 10, 0.00346631581608188 },
                { 15, 0.14257145433317200 },
                { 20, 1.12649558128329000 },
                { 30, 44.71748658332190000 },

                { 0d, 0d },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY },
                { Double.NaN, Double.NaN }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    IC_GAMMA.rigammaOdds(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class 境界値テスト {

        @DataPoints
        public static final IncompleteGammaFunction[] icGammas = {
                new ICGammaAtMiddleParam(20)
        };

        @Theory
        public void test_0検証_P(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    0d, icgamma.rigammaP(0d));
        }

        @Theory
        public void test_正の無限大検証_P(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d, icgamma.rigammaP(Double.POSITIVE_INFINITY));
        }

        @Theory
        public void test_0検証_Q(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    1d, icgamma.rigammaQ(0d));
        }

        @Theory
        public void test_正の無限大検証_Q(IncompleteGammaFunction icgamma) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    0d, icgamma.rigammaQ(Double.POSITIVE_INFINITY));
        }
    }

    public static class toString表示 {

        @Test
        public void test_toSTring() {
            System.out.println(TEST_CLASS.getName());
            System.out.println(new ICGammaAtMiddleParam(20));
            System.out.println();
        }
    }
}
