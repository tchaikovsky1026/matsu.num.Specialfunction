/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.11
 */
package matsu.num.specialfunction.fraction;

/**
 * <p>
 * 体構造を表す. <br>
 * このインターフェースは内部状態を持たない.
 * </p>
 * 
 * <p>
 * 体とは, 四則演算が定義され, ...
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.5
 * @param <T> 二項演算が可能な相手を表す型, すなわち自分自身の型
 */
public interface MathFieldType<T extends MathFieldType<T>> {

    /**
     * このタイプが {@code double} 値として解釈可能かどうかを返す.
     * 
     * @return 解釈可能な場合はtrue
     */
    public abstract boolean canBeInterpretedAsDouble();
}
