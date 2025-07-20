/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.icgamma;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.function.DoubleFunction;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@link ICGammaAtHighParam} クラスのテスト.
 */
@RunWith(Enclosed.class)
@Deprecated
final class ICGammaAtHighParamTest {

    public static final Class<?> TEST_CLASS = ICGammaAtHighParam.class;

    private static final DoubleFunction<IncompleteGammaFunction> IC_GAMMA_GETTER =
            a -> new ICGammaAtMiddleParam(a);

    public static class A_50000のオッズ値のテスト extends IcgammaAt50000 {

        @Override
        DoubleFunction<IncompleteGammaFunction> icgammaGetter() {
            return IC_GAMMA_GETTER;
        }

        @Override
        double acceptableRelativeError() {
            return 1E-5;
        }
    }

    public static class A_100000のオッズ値のテスト extends IcgammaAt100000 {

        @Override
        DoubleFunction<IncompleteGammaFunction> icgammaGetter() {
            return IC_GAMMA_GETTER;
        }

        @Override
        double acceptableRelativeError() {
            return 1E-5;
        }
    }

    @RunWith(Theories.class)
    public static class 境界値テスト {

        @DataPoints
        public static final IncompleteGammaFunction[] icGammas = {
                new ICGammaAtHighParam(50000)
        };

        @Theory
        public void test_0検証_P(IncompleteGammaFunction icgamma) {
            assertThat(icgamma.rigammaP(0d), is(0d));
        }

        @Theory
        public void test_正の無限大検証_P(IncompleteGammaFunction icgamma) {
            assertThat(icgamma.rigammaP(Double.POSITIVE_INFINITY), is(1d));
        }

        @Theory
        public void test_0検証_Q(IncompleteGammaFunction icgamma) {
            assertThat(icgamma.rigammaQ(0d), is(1d));
        }

        @Theory
        public void test_正の無限大検証_Q(IncompleteGammaFunction icgamma) {
            assertThat(icgamma.rigammaQ(Double.POSITIVE_INFINITY), is(0d));
        }
    }
}
