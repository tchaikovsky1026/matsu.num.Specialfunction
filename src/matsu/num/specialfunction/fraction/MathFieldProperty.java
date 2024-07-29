/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.29
 */
package matsu.num.specialfunction.fraction;

/**
 * <p>
 * 体の性質を表現する. <br>
 * このインターフェースは内部状態を持たない.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.1
 */
public interface MathFieldProperty {

    /**
     * 体が {@code double} 値として解釈可能かどうかを返す.
     * 
     * @return 解釈可能な場合はtrue
     */
    public abstract boolean canBeInterpretedAsDouble();
}
