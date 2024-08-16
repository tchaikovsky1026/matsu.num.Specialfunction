/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.16
 */
package matsu.num.specialfunction.lambert;

/**
 * Lambert-W関数の主枝 (Wp) の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 19.6
 */
public interface LambertWpCalculation {

    /**
     * Wp の計算. {@literal z >= -1/e}で意味を持つ.
     * 
     * @param z z
     * @return wp(z)
     */
    public abstract double wp(double z);

    /**
     * インスタンスを生成する.
     * 
     * @return インスタンス
     */
    public static LambertWpCalculation createInstance() {
        return new LambertWpImplByHalley();
    }
}
