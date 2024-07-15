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

/**
 * {@code double} 型の配列として表された係数を文字列に変換する仕組みを扱う.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
@FunctionalInterface
public interface CoefficientToString {

    /**
     * 係数を文字列に変換する.
     * 
     * @param coeff 係数
     * @return 文字列
     */
    public abstract String execute(double[] coeff);
}
