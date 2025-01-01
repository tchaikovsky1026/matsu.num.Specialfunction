/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.12.31
 */
package matsu.num.specialfunction.common;

/**
 * 三角関数の計算を扱う.
 * 
 * @author Matsuura Y.
 * @version 22.0
 */
public final class Trigonometry {
    private Trigonometry() {
        //インスタンス化不可
        throw new AssertionError();
    }

    public static double cos(double x) {
        return Math.cos(x);
    }

    public static double sin(double x) {
        return Math.sin(x);
    }
}
