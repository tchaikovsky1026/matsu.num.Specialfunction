/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.22
 */
package matsu.num.specialfunction.fraction;

/**
 * <p>
 * {@code double} 型の連分数関数を扱うクラス.
 * </p>
 * 
 * <p>
 * 与える係数を [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
 * ... ,
 * <i>a</i><sub><i>n</i> - 1</sub>]
 * とすると, この連分数関数は, <br>
 * <i>f</i>(<i>t</i>) =
 * (1/1+)
 * (<i>a</i><sub>0</sub><i>t</i>/1+)
 * (<i>a</i><sub>1</sub><i>t</i>/1+)
 * ...
 * (<i>a</i><sub><i>n</i> - 2</sub><i>t</i>/1+)
 * (<i>a</i><sub><i>n</i> - 1</sub><i>t</i>/1)
 * <br>
 * である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.9
 */
public final class DoubleContinuedFractionFunction {

    private final double[] cfCoeff;

    /**
     * 連分数の係数
     * [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
     * ... ,
     * <i>a</i><sub><i>n</i> - 1</sub>]
     * を与えて連分数関数を生成する. <br>
     * 
     * 引数はコピーされないので, このコンストラクタを公開してはいけない.
     */
    private DoubleContinuedFractionFunction(double[] cfCoeff) {
        this.cfCoeff = cfCoeff;
    }

    /**
     * 連分数から値を計算する.
     * 
     * @param t t
     * @return 値
     */
    public double value(double t) {

        double value = 1d;

        for (int i = this.cfCoeff.length - 1; i >= 0; i--) {
            double coeff = this.cfCoeff[i];
            value = coeff * t / value;
            value += 1d;
        }
        return 1d / value;
    }

    /**
     * 連分数の係数
     * [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
     * ... ,
     * <i>a</i><sub><i>n</i> - 1</sub>]
     * を返す. <br>
     * 最初の1は含まない.
     * 
     * @return 係数
     */
    public double[] coeffOfContinuedFraction() {
        return this.cfCoeff.clone();
    }

    /**
     * 与えられた配列を係数
     * [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
     * ... ,
     * <i>a</i><sub><i>n</i> - 1</sub>]
     * とする連分数関数を返す.
     * 
     * @param coefficient 連分数関数の係数
     * @return 連分数関数
     * @throws NullPointerException nullが含まれる場合
     */
    public static DoubleContinuedFractionFunction of(double[] coefficient) {
        return new DoubleContinuedFractionFunction(coefficient.clone());
    }

    /**
     * 与えられた連分数関数を {@code double} 型の連分数関数に変換する.
     * 
     * @param <SET>
     * @param src ソース
     * @return double変換した連分数関数
     * @throws NullPointerException nullが含まれる場合
     */
    public static <SET extends RealMathField<SET>>
            DoubleContinuedFractionFunction from(ContinuedFractionFunction<SET> src) {

        return new DoubleContinuedFractionFunction(
                src.coeffOfContinuedFraction()
                        .stream()
                        .mapToDouble(et -> et.doubleValue())
                        .toArray());
    }
}
