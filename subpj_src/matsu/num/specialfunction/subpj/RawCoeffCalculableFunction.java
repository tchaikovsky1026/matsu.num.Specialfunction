/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj;

import matsu.num.approximation.ApproxTarget;
import matsu.num.approximation.PseudoRealNumber;

/**
 * 近似ターゲット関数の近似多項式を, 元々扱いたい関数の係数へと変換する機能を持たせた,
 * {@link ApproxTarget} の拡張.
 * 
 * @author Matsuura Y.
 * @param <T> 体の元を表現する型パラメータ
 */
public abstract class RawCoeffCalculableFunction<T extends PseudoRealNumber<T>>
        extends ApproxTarget<T> {

    /**
     * 元々扱いたい係数を返す.
     * 
     * @param thisCoeff このターゲットファンクションにおける係数
     * @return 元々知りたかった係数
     */
    public abstract T[] rawCoeff(T[] thisCoeff);
}
