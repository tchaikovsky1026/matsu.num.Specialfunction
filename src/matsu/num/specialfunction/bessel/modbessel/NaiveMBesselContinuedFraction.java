/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.29
 */
package matsu.num.specialfunction.bessel.modbessel;

import java.util.List;
import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;

/**
 * 変形Bessel関数の計算で使う, 連分数の生成機能.
 * 
 * @author Matsuura Y.
 * @version 19.1
 * @deprecated {@link NaiveMBessel0}, {@link NaiveMBessel1}
 *                 が使用されていないので, 使用されていない.
 */
@Deprecated
final class NaiveMBesselContinuedFraction {

    /*
     * 商差法による連分数の係数の算出では, 非常に大きな桁を持つ有理数となる.
     * よって, 実質的に上限が無い {@link BigRationalElement} を用いている.
     */

    /**
     * 原理的に計算するか, 記録済み配列から計算するかを切り替えるフラグ. <br>
     * trueなら記録済み配列から生成.
     */
    private static final boolean CREATION_FROM_ARRAY = true;

    private static final double[] K_0_COEFFICIENT = {
            //K_0の漸近級数の連分数係数(50項)
            1.0, 3.5, 4.928571428571429, 7.419254658385094,
            8.901441388974831, 11.380737175397854, 12.886695957948879, 15.356859349063553,
            16.877298272701978, 19.340100703430558, 20.870739239418057, 23.3274483989265,
            24.865883651017484, 27.31742536288529, 28.86213739169189, 31.309209444659558,
            32.85915713669735, 35.302301009194274, 36.85672965504691, 39.296376142960256,
            40.85471503614514, 43.29121416179856, 44.853017416264265, 47.2866586725791,
            48.85156876963747, 51.28259523703814, 52.85031940677349, 55.27893786290162,
            56.84923213801939, 59.275620469893724, 60.84827854575954, 63.27259129793607,
            64.84743652322359, 67.26980912746657, 68.84668860260516, 71.26724065524223,
            72.84602079089903, 75.26485862935769, 76.84542174147758, 79.26264049635266,
            80.84488215314674, 83.2605674017921, 84.8443943266905, 87.25862343989043,
            88.8439518325683, 91.2567950818527, 92.84354925843213, 95.25507073460051,
            96.84318201486673, 99.25344039605268
    };

    private static final double[] K_1_COEFFICIENT = {
            //K_1の漸近級数の連分数係数(50項)
            -3.0, 5.5, 2.0454545454545454, 9.065656565656566,
            6.289167440420922, 13.008209512128444, 10.408223007859561, 16.99043728453666,
            14.482089484224927, 20.98316692955904, 18.5337031029106, 24.979786407804912,
            22.57242006136782, 28.978137754708715, 26.602866188021018, 32.977354354534604,
            30.627628696133684, 36.97703749359475, 34.648284484690805, 40.97698526293544,
            38.665857576906504, 44.977087930188745, 42.681046176052845, 48.97728258914858,
            46.69434510122937, 52.9775316767545, 50.70611623624396, 56.977812056020085,
            54.7166312412449, 60.97810914388254, 58.72609858688545, 64.97841360128024,
            62.734681294162826, 68.97871939310266, 66.74250893820007, 72.97902261294827,
            70.74968598837063, 76.97932075095484, 74.75629773796037, 80.97961222661401,
            78.7624146062652, 84.97989608450051, 82.76809531636395, 88.98017179260658,
            86.77338928029484, 92.98043910667795, 90.77833841526339, 96.98069797780244,
            94.78297854469398, 100.98094848881028
    };

    private NaiveMBesselContinuedFraction() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * K<sub>0</sub>(x)の漸近展開部分を連分数に変換したもの. <br>
     * t = 1/(8x) を引数として, <br>
     * 1 - ((1^2)/(1))*t + ((1^2)*(3^2)/(1*2))*t^2 + ... <br>
     * を計算する仕組みである.
     * 
     * @return 連分数
     */
    public static DoubleContinuedFractionFunction createK0Asymptotic() {
        if (CREATION_FROM_ARRAY) {
            return k0_fromArray();
        }

        return k0_inPrinciple().asDoubleFunction();
    }

    /**
     * K<sub>1</sub>(x)の漸近展開部分を連分数に変換したもの. <br>
     * t = 1/(8x) を引数として, <br>
     * 1 - ((1^2-4)/(1))*t + ((1^2-4)*(3^2-4)/(1*2))*t^2 + ... <br>
     * を計算する仕組みである.
     * 
     * @return 連分数
     */
    public static DoubleContinuedFractionFunction createK1Asymptotic() {
        if (CREATION_FROM_ARRAY) {
            return k1_fromArray();
        }

        return k1_inPrinciple().asDoubleFunction();
    }

    /**
     * 原理的, 直接的なK<sub>0</sub>の連分数.
     */
    private static ContinuedFractionFunction<BigRational> k0_inPrinciple() {
        final int kMax = 50;

        IntFunction<BigRational> func =
                k -> BigRational.of(-((2 * k + 1) * (2 * k + 1)), k + 1);

        return ContinuedFractionFunction.from(
                kMax, func,
                BigRational.constantSupplier());
    }

    /**
     * 計算済みの係数からのK<sub>0</sub>の連分数.
     */
    private static DoubleContinuedFractionFunction k0_fromArray() {
        return DoubleContinuedFractionFunction.of(K_0_COEFFICIENT);
    }

    /**
     * 原理的, 直接的なK<sub>1</sub>の連分数.
     */
    private static ContinuedFractionFunction<BigRational> k1_inPrinciple() {

        final int kMax = 50;
        IntFunction<BigRational> func =
                k -> BigRational.of(-((2 * k + 1) * (2 * k + 1) - 4), k + 1);

        return ContinuedFractionFunction.from(
                kMax, func,
                BigRational.constantSupplier());
    }

    /**
     * 計算済みの係数からのK<sub>1</sub>の連分数.
     * 
     */
    private static DoubleContinuedFractionFunction k1_fromArray() {
        return DoubleContinuedFractionFunction.of(K_1_COEFFICIENT);
    }

    /**
     * K<sub>0</sub>の連分数係数をコンソールに表示する仕組み. <br>
     * 実行可能である.
     */
    @SuppressWarnings("unused")
    private static final class K0Output {

        public static void main(String[] args) {
            ContinuedFractionFunction<BigRational> fraction =
                    k0_inPrinciple();

            System.out.println("K_0 output");
            System.out.println();
            List<? extends Object> coeffList = fraction.coeffOfContinuedFraction();
            System.out.printf("//K_0の漸近級数の連分数係数(%s項)_生値", coeffList.size());
            System.out.println();
            for (Object c : coeffList) {
                System.out.println(c);
            }
            System.out.println();
            System.out.println();

            double[] coeffs = fraction
                    .asDoubleFunction()
                    .coeffOfContinuedFraction();
            System.out.println("{");
            System.out.printf("//K_0の漸近級数の連分数係数(%s項)", coeffs.length);
            System.out.println();
            for (int i = 0; i < coeffs.length; i++) {
                System.out.print(coeffs[i]);
                if (i < coeffs.length - 1) {
                    System.out.print(",");
                    if (i % 4 == 3) {
                        System.out.println();
                    }
                }
            }
            System.out.println();
            System.out.println("}");
        }
    }

    /**
     * K<sub>1</sub>の連分数係数をコンソールに表示する仕組み. <br>
     * 実行可能である.
     */
    @SuppressWarnings("unused")
    private static final class K1Output {

        public static void main(String[] args) {
            ContinuedFractionFunction<BigRational> fraction =
                    k1_inPrinciple();

            System.out.println("K_1 output");
            System.out.println();
            List<? extends Object> coeffList = fraction.coeffOfContinuedFraction();
            System.out.printf("//K_1の漸近級数の連分数係数(%s項)_生値", coeffList.size());
            System.out.println();
            for (Object c : coeffList) {
                System.out.println(c);
            }
            System.out.println();
            System.out.println();

            double[] coeffs = fraction
                    .asDoubleFunction()
                    .coeffOfContinuedFraction();
            System.out.println("{");
            System.out.printf("//K_1の漸近級数の連分数係数(%s項)", coeffs.length);
            System.out.println();
            for (int i = 0; i < coeffs.length; i++) {
                System.out.print(coeffs[i]);
                if (i < coeffs.length - 1) {
                    System.out.print(",");
                    if (i % 4 == 3) {
                        System.out.println();
                    }
                }
            }
            System.out.println();
            System.out.println("}");
        }
    }

}
