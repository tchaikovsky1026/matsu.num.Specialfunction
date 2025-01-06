/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.15
 */
package matsu.num.specialfunction.err;

/**
 * 虚数誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 19.5
 */
public interface ErrorFunctionImaginaryCalculation {

    /**
     * 虚数誤差関数
     * erfi(x)
     * 
     * @param x x
     * @return erfi(x)
     */
    public abstract double erfi(double x);

    /**
     * スケーリングされた虚数誤差関数
     * erfix(x) = exp(-x^2)erfi(x)
     * 
     * @param x x
     * @return erfix(x)
     */
    public abstract double erfix(double x);

    /**
     * 新しいインスタンスを生成して返す.
     * 
     * @return インスタンス
     */
    public static ErrorFunctionImaginaryCalculation createInstance() {
        return new ErfiCalcOptimized();
    }
}
