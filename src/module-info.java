/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */

/**
 * 特殊関数を扱うモジュール.
 * 
 * <p>
 * このモジュールでは, ガンマ関数, 誤差関数, Bessel 関数などの特殊関数を計算する機能を提供する. <br>
 * ガンマ関数, 誤差関数などのパラメータを持たない特殊関数は,
 * ほとんどが {@code static} メソッドとして公開されている. <br>
 * Bessel 関数などの次数等のパラメータを持つ特殊関数は,
 * パラメータを属性に持つインスタンスが提供され, 計算はインスタンスメソッドとして公開されている.
 * </p>
 * 
 * <p>
 * <i>依存モジュール:</i> <br>
 * (無し)
 * </p>
 * 
 * @author Matsuura Y.
 * @version 23.4.0
 */
module matsu.num.Specialfunction {
    exports matsu.num.specialfunction;
    exports matsu.num.specialfunction.bessel;
}
