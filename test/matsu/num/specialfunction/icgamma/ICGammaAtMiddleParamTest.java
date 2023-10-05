package matsu.num.specialfunction.icgamma;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@link ICGammaAtMiddleParam}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ICGammaAtMiddleParamTest {

    @RunWith(Enclosed.class)
    public static class テスト_A_20 {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 5.00E+00, 0.00000034521370126 },
                    { 7.00E+00, 0.00004440444820711 },
                    { 10, 0.00346631581608188 },
                    { 15, 0.14257145433317200 },
                    { 20, 1.12649558128329000 },
                    { 30, 44.71748658332190000 }
            };

            private IncompleteGammaFunction igf;

            @Before
            public void before_不完全ガンマ関数の作成() {
                igf = ICGammaAtMiddleParam.instanceOf(20);
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(igf.rigammaOdds(dataPair[0]), is(closeTo(dataPair[1], Math.abs(dataPair[1]) * 1E-8)));
            }
        }

        public static class 特殊値の検証 {

            private IncompleteGammaFunction igf;

            @Before
            public void before_不完全ガンマ関数の作成() {
                igf = ICGammaAtMiddleParam.instanceOf(20);
            }

            @Test
            public void test_0検証_オッズ() {
                assertThat(igf.rigammaOdds(0), is(0.0));
            }

            @Test
            public void test_正の無限大検証_オッズ() {
                assertThat(igf.rigammaOdds(Double.POSITIVE_INFINITY), is(Double.POSITIVE_INFINITY));
            }

            @Test
            public void test_0検証_P() {
                assertThat(igf.rigammaP(0), is(0.0));
            }

            @Test
            public void test_正の無限大検証_P() {
                assertThat(igf.rigammaP(Double.POSITIVE_INFINITY), is(1.0));
            }

            @Test
            public void test_0検証_Q() {
                assertThat(igf.rigammaQ(0), is(1.0));
            }

            @Test
            public void test_正の無限大検証_Q() {
                assertThat(igf.rigammaQ(Double.POSITIVE_INFINITY), is(0.0));
            }
        }
    }
}
