/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * {@code double} 型の配列として表された係数を文字列に変換する仕組みを扱う.
 * 
 * @author Matsuura Y.
 */
@FunctionalInterface
public interface CoefficientToString {

    /**
     * 係数を文字列に変換する.
     * 
     * @param coeff 係数
     * @return 文字列
     */
    public default String execute(double[] coeff) {
        Object[] coeffObj = Arrays.stream(coeff)
                .mapToObj(d -> Double.valueOf(d))
                .collect(Collectors.toList())
                .toArray();

        return this.execute(coeffObj);
    }

    /**
     * 係数を文字列に変換する.
     * 
     * @param coeff 係数
     * @return 文字列
     */
    public abstract String execute(Object[] coeff);
}
