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
 * {@link ICGammaAtHighParam}クラスのテスト.
 *
 * @author Matsuura Y.
 */
@RunWith(Enclosed.class)
public class ICGammaAtHighParamTest {

    @RunWith(Enclosed.class)
    public static class テスト_A_50000 {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 4.9105572809000100E+04, 0.00002877914982021 },
                    { 4.9329179606750100E+04, 0.00129922048514770 },
                    { 4.9552786404500000E+04, 0.02302642083196370 },
                    { 4.9776393202250000E+04, 0.18857227426484500 },
                    { 5.0000000000000000E+04, 1.00238166521038000 },
                    { 5.0223606797750000E+04, 5.30300631662694000 },
                    { 5.0447213595500000E+04, 42.49519530662480000 },
                    { 5.0670820393249900E+04, 711.63832668614000000 },
                    { 5.0894427190999900E+04, 28762.90666680780000000 }
            };

            private IncompleteGammaFunction igf;

            @Before
            public void before_不完全ガンマ関数の作成() {
                igf = ICGammaAtHighParam.instanceOf(50000);
            }

            @Theory
            public void test_検証(double[] dataPair) {
                assertThat(igf.rigammaOdds(dataPair[0]), is(closeTo(dataPair[1], Math.abs(dataPair[1]) * 1E-6)));
            }
        }

        public static class 特殊値の検証 {

            private IncompleteGammaFunction igf;

            @Before
            public void before_不完全ガンマ関数の作成() {
                igf = ICGammaAtHighParam.instanceOf(500000);
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
