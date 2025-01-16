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
 * 実数として解釈可能な数体の元を表現する. <br>
 * 実質的にイミュータブルである. <br>
 * 値に基づく equalty に加え, comparability を提供する. <br>
 * equality と comparability は整合する.
 * </p>
 * 
 * <p>
 * 負のゼロと正の0は等価であるべきである.
 * </p>
 * 
 * <p>
 * この抽象の実装は, 可能な限り {@link Object#toString()} メソッドをオーバーライドし,
 * 文字列表現を提供すべきである.
 * </p>
 * 
 * @author Matsuura Y.
 * @param <ET> このクラスと二項演算が可能な体構造の元を表す型.
 *            体の定義より, 自身に一致する.
 */
public abstract class RealMathField<ET extends RealMathField<ET>>
        extends MathField<ET> implements Comparable<ET> {

    /**
     * 唯一のコンストラクタ.
     */
    protected RealMathField() {
        super();
    }

    /**
     * 絶対値を返す.
     * 
     * @return 絶対値
     */
    public abstract ET abs();

    /**
     * {@code double} 表現された値を返す. <br>
     * 無限大の場合があり得る.
     * 
     * @return double表現した値
     */
    public abstract double doubleValue();
}
