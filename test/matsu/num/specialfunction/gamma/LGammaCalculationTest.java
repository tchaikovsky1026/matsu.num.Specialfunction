package matsu.num.specialfunction.gamma;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import matsu.num.specialfunction.DoubleRelativeAssertion;

/**
 * {@link LGammaCalculation}クラスのテスト.
 *
 * @author Matsuura Y.
 */
final class LGammaCalculationTest {

    private static final DoubleRelativeAssertion DOUBLE_RELATIVE_ASSERTION =
            new DoubleRelativeAssertion(1E-12);

    private static final LGammaCalculation L_GAMMA = new LGammaCalculation();

    @RunWith(Theories.class)
    public static class メソッドlgammaに関する {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 0.5723649429247000000 },
                { 1, 0.0000000000000000000 },
                { 1.5, -0.1207822376352450000 },
                { 2, 0.0000000000000000000 },
                { 2.5, 0.2846828704729190000 },
                { 3, 0.6931471805599450000 },
                { 3.5, 1.2009736023470700000 },
                { 4, 1.7917594692280500000 },
                { 4.5, 2.4537365708424400000 },
                { 5, 3.1780538303479500000 },
                { 5.5, 3.9578139676187200000 },
                { 6, 4.7874917427820500000 },
                { 6.5, 5.6625620598571400000 },
                { 7, 6.5792512120101000000 },
                { 7.5, 7.5343642367587300000 },
                { 8, 8.5251613610654100000 },
                { 8.5, 9.5492672573010000000 },
                { 9, 10.6046029027453000000 },
                { 9.5, 11.6893334207973000000 },
                { 10, 12.8018274800815000000 },
                { 10.5, 13.9406252194038000000 },
                { 11, 15.1044125730755000000 },
                { 0d, Double.POSITIVE_INFINITY },
                { -1d, Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgamma(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgamma1pに関する {

        @DataPoints
        public static double[][] dataPairs = {
                { -0.5, 0.5723649429247000000 },
                { 0, 0.0000000000000000000 },
                { 0.5, -0.1207822376352450000 },
                { 1, 0.0000000000000000000 },
                { 1.5, 0.2846828704729190000 },
                { 2, 0.6931471805599450000 },
                { 2.5, 1.2009736023470700000 },
                { 3, 1.7917594692280500000 },
                { 3.5, 2.4537365708424400000 },
                { 4, 3.1780538303479500000 },
                { 4.5, 3.9578139676187200000 },
                { 5, 4.7874917427820500000 },
                { 5.5, 5.6625620598571400000 },
                { 6, 6.5792512120101000000 },
                { 6.5, 7.5343642367587300000 },
                { 7, 8.5251613610654100000 },
                { 7.5, 9.5492672573010000000 },
                { 8, 10.6046029027453000000 },
                { 8.5, 11.6893334207973000000 },
                { 9, 12.8018274800815000000 },
                { 9.5, 13.9406252194038000000 },
                { 10, 15.1044125730755000000 },
                { 10.5, 16.2920004765672000000 },
                { 11, 17.5023078458739000000 },
                { -1d, Double.POSITIVE_INFINITY },
                { Math.nextDown(-1d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgamma1p(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaStirlingに関する {

        @DataPoints
        public static double[][] dataPairs = {
                { 1d, -1d + 0.5 * Math.log(2 * Math.PI) },
                { 2d, 1.5 * Math.log(2) - 2d + 0.5 * Math.log(2 * Math.PI) },
                { 3d, 2.5 * Math.log(3) - 3d + 0.5 * Math.log(2 * Math.PI) },
                { 0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgammaStirling(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaStirlingResidualに関する {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 0.1534264097200270000 },
                { 1, 0.0810614667953273000 },
                { 1.5, 0.0548141210519177000 },
                { 2, 0.0413406959554095000 },
                { 2.5, 0.0331628735199363000 },
                { 3, 0.0276779256849982000 },
                { 3.5, 0.0237461636562974000 },
                { 4, 0.0207906721037654000 },
                { 4.5, 0.0184884505326730000 },
                { 5, 0.0166446911898226000 },
                { 5.5, 0.0151349732219175000 },
                { 6, 0.0138761288230715000 },
                { 6.5, 0.0128104652429193000 },
                { 7, 0.0118967099458920000 },
                { 7.5, 0.0111045597582087000 },
                { 8, 0.0104112652619714000 },
                { 8.5, 0.0097994161261580900 },
                { 9, 0.0092554621827125600 },
                { 9.5, 0.0087687001341385500 },
                { 10, 0.0083305634333625800 },
                { 10.5, 0.0079341145643137000 },
                { 11, 0.0075736754879520700 },
                { 11.5, 0.0072445543013208700 },
                { 12, 0.0069428401072122400 },
                { 0d, Double.POSITIVE_INFINITY },
                { Math.nextDown(0d), Double.NaN },
                { Double.POSITIVE_INFINITY, 0d }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[1],
                    L_GAMMA.lgammaStirlingResidual(dataPair[0]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaDiffに関する値のテスト {

        @DataPoints
        public static double[][] dataPairs = {
                { 0.5, 1, -0.693147180559945 },
                { 1, 1, 0.000000000000000 },
                { 1.5, 1, 0.405465108108164 },
                { 2, 1, 0.693147180559945 },
                { 2.5, 1, 0.916290731874155 },
                { 3, 1, 1.098612288668110 },
                { 3.5, 1, 1.252762968495370 },
                { 4, 1, 1.386294361119890 },
                { 4.5, 1, 1.504077396776270 },
                { 5, 1, 1.609437912434100 },
                { 5.5, 1, 1.704748092238420 },
                { 6, 1, 1.791759469228060 },
                { 6.5, 1, 1.871802176901590 },
                { 7, 1, 1.945910149055310 },
                { 7.5, 1, 2.014903020542260 },
                { 8, 1, 2.079441541679840 },
                { 8.5, 1, 2.140066163496270 },
                { 9, 1, 2.197224577336220 },
                { 9.5, 1, 2.251291798606490 },
                { 10, 1, 2.302585092994050 },
                { 10.5, 1, 2.351375257163480 },
                { 11, 1, 2.397895272798370 },
                { 11.5, 1, 2.442347035369210 },
                { 12, 1, 2.484906649787990 },
                { 12.5, 1, 2.525728644308250 },
                { 13, 1, 2.564949357461540 },

                { 0.5, 11, 15.7196355336425 },
                { 1, 11, 17.5023078458739 },
                { 1.5, 11, 18.8551297495717 },
                { 2, 11, 19.9872144956619 },
                { 2.5, 11, 20.9753932857718 },
                { 3, 11, 21.8590166725635 },
                { 3.5, 11, 22.6617922393420 },
                { 4, 11, 23.3994617135106 },
                { 4.5, 11, 24.0831779202732 },
                { 5, 11, 24.7212175534929 },
                { 5.5, 11, 25.3199405474221 },
                { 6, 11, 25.8843683632986 },
                { 6.5, 11, 26.4185528360902 },
                { 7, 11, 26.9258222381268 },
                { 7.5, 11, 27.4089515401181 },
                { 8, 11, 27.8702838469676 },
                { 8.5, 11, 28.3118192516601 },
                { 9, 11, 28.7352812844542 },
                { 9.5, 11, 29.1421675537335 },
                { 10, 11, 29.5337889806720 },
                { 10.5, 11, 29.9113006412714 },
                { 11, 11, 30.2757263254014 },
                { 11.5, 11, 30.6279783192415 },
                { 12, 11, 30.9688735059613 },
                { 12.5, 11, 31.2991465930827 },
                { 13, 11, 31.6194610721025 },
                { 13.5, 11, 31.9304183699246 },
                { 14, 11, 32.2325655449889 },
                { 14.5, 11, 32.5264018020308 },
                { 15, 11, 32.8123840402418 },

                { Math.nextDown(0d), 1d, Double.NaN },
                { 1d, Math.nextDown(-1d), Double.NaN },

                { Double.POSITIVE_INFINITY, 1d, Double.NaN },
                { Double.POSITIVE_INFINITY, -1d, Double.NaN },
                { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN },

                { 0d, 1d, Double.NEGATIVE_INFINITY }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                    dataPair[2],
                    L_GAMMA.lgammaDiff(dataPair[0], dataPair[1]));
        }
    }

    @RunWith(Theories.class)
    public static class メソッドlgammaDiffに関する境界での有限評価 {

        @DataPoints
        public static double[][] dataPairs = {
                { Double.MIN_VALUE, 0 },
                { Double.MIN_VALUE, Double.MIN_VALUE },
                { Double.MIN_VALUE, 2 * Double.MIN_VALUE },
                { 2 * Double.MIN_VALUE, Double.MIN_VALUE },
                { Double.MIN_NORMAL, Double.MIN_VALUE },
                { Double.MIN_VALUE, Double.MIN_NORMAL }
        };

        @Theory
        public void test_検証(double[] dataPair) {
            DOUBLE_RELATIVE_ASSERTION.assertFinite(
                    L_GAMMA.lgammaDiff(dataPair[0], dataPair[1]));
        }
    }

    @RunWith(Enclosed.class)
    public static class メソッドlbetaに関する {

        @RunWith(Theories.class)
        public static class 正常値セオリー {

            @DataPoints
            public static double[][] dataPairs = {
                    { 0.5, 1, 0.693147180559945 },
                    { 1, 1, 0.000000000000000 },
                    { 1.5, 1, -0.405465108108164 },
                    { 2, 1, -0.693147180559945 },
                    { 2.5, 1, -0.916290731874155 },
                    { 3, 1, -1.098612288668110 },
                    { 3.5, 1, -1.252762968495370 },
                    { 4, 1, -1.386294361119890 },
                    { 4.5, 1, -1.504077396776270 },
                    { 5, 1, -1.609437912434100 },
                    { 5.5, 1, -1.704748092238420 },
                    { 6, 1, -1.791759469228060 },
                    { 6.5, 1, -1.871802176901590 },
                    { 7, 1, -1.945910149055310 },
                    { 7.5, 1, -2.014903020542260 },
                    { 8, 1, -2.079441541679840 },
                    { 8.5, 1, -2.140066163496270 },
                    { 9, 1, -2.197224577336220 },
                    { 9.5, 1, -2.251291798606490 },
                    { 10, 1, -2.302585092994050 },
                    { 10.5, 1, -2.351375257163480 },
                    { 11, 1, -2.397895272798370 },
                    { 11.5, 1, -2.442347035369210 },
                    { 12, 1, -2.484906649787990 },
                    { 12.5, 1, -2.525728644308250 },
                    { 13, 1, -2.564949357461540 },

                    { Math.nextDown(0d), 1d, Double.NaN },
                    { Double.POSITIVE_INFINITY, 1d, Double.NaN },
                    { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN },
                    { 0d, 1d, Double.POSITIVE_INFINITY },
                    { 0d, 0d, Double.POSITIVE_INFINITY }

            };

            @Theory
            public void test_検証(double[] dataPair) {
                DOUBLE_RELATIVE_ASSERTION.compareAndAssert(
                        dataPair[2],
                        L_GAMMA.lbeta(dataPair[0], dataPair[1]));
            }
        }
    }
}
