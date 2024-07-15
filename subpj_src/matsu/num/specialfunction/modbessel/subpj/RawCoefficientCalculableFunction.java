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

import matsu.num.approximation.TargetFunction;

/**
 * 近似ターゲット関数の近似多項式を, 元々扱いたい関数の係数へと変換する機能を持たせた,
 * {@link TargetFunction} の拡張.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
public interface RawCoefficientCalculableFunction extends TargetFunction {

    /**
     * 元々扱いたい係数を返す.
     * 
     * @param thisCoeff このターゲットファンクションにおける係数
     * @return 元々知りたかった係数
     */
    public abstract double[] rawCoeff(double[] thisCoeff);
}
