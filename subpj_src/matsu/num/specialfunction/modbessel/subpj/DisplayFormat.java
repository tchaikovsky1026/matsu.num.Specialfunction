/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.15
 */
package matsu.num.specialfunction.modbessel.subpj;

import matsu.num.approximation.PolynomialFunction;

/**
 * ターゲット関数と近似結果から, 結果出力の文字列を得るための, 
 * 結果出力の形式を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
@FunctionalInterface
public interface DisplayFormat {

    /**
     * 結果出力の文字列を返す.
     * 
     * @param target ターゲット
     * @param result 近似結果
     * @return 結果出力の文字列
     */
    public abstract String resultToString(
            RawCoefficientCalculableFunction target,
            PolynomialFunction result);
}