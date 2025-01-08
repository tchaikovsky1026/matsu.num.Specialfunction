/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj;

import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.Polynomial;

/**
 * ターゲット関数と近似結果から, 結果出力の文字列を得るための,
 * 結果出力の形式を扱う.
 * 
 * @author Matsuura Y.
 */
public interface DisplayFormat {

    /**
     * 結果出力の文字列を返す.
     * 
     * @param target ターゲット
     * @param result 近似結果
     * @return 結果出力の文字列
     */
    public abstract String resultToString(
            RawCoeffCalculableDoubleFunction target,
            DoublePolynomial result);

    /**
     * 結果出力の文字列を返す.
     * 
     * @param target ターゲット
     * @param result 近似結果
     * @return 結果出力の文字列
     */
    public abstract <T extends PseudoRealNumber<T>> String resultToString(
            RawCoeffCalculableFunction<T> target,
            Polynomial<T> result);
}
