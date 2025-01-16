/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.16
 */
package matsu.num.specialfunction.lambert;

/**
 * Lambert-W関数の-1分枝 (Wm) の計算を扱う.
 * 
 * @author Matsuura Y.
 */
public interface LambertWmCalculation {

    /**
     * Wmの計算. {@literal -1/e <= z <= 0}で意味を持つ.
     * 
     * @param z z
     * @return wm(z)
     */
    public abstract double wm(double z);

    /**
     * インスタンスを生成する.
     * 
     * @return インスタンス
     */
    public static LambertWmCalculation createInstance() {
        return new LambertWmImplByHalley();
    }
}
