/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.28
 */
package matsu.num.specialfunction.fraction;

import java.util.Objects;

/**
 * {@code double} 型として解釈できないことを表現する. <br>
 * 内部状態を持たないので, 実質的にシングルトンである.
 * 
 * @author Matsuura Y.
 * @version 19.1
 */
final class DoubleUninterpretable implements MathFieldProperty {

    /**
     * このクラスのインスタンス.
     */
    public static final DoubleUninterpretable INSTANCE = new DoubleUninterpretable();

    private DoubleUninterpretable() {
        if (Objects.nonNull(INSTANCE)) {
            throw new AssertionError("シングルトンを強制する");
        }
    }

    @Override
    public boolean canBeInterpretedAsDouble() {
        return false;
    }

    /**
     * このインスタンスの文字列表現を返す.
     * 
     * @return 文字列表現
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
