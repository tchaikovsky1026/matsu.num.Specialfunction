/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.8.16
 */
package matsu.num.specialfunction.err;

/**
 * 誤差関数の計算を実行する.
 * 
 * @author Matsuura Y.
 * @version 19.6
 */
public interface ErrorFunctionCalculation {

    /**
     * 誤差関数
     * erf(<i>x</i>).
     * 
     * @param x x
     * @return erf(x)
     */
    public abstract double erf(double x);

    /**
     * 相補誤差関数
     * erfc(<i>x</i>) = 1 - erf(<i>x</i>).
     * 
     * @param x x
     * @return erfc(x)
     */
    public abstract double erfc(double x);

    /**
     * スケーリング相補誤差関数
     * erfcx(<i>x</i>) =
     * exp(<i>x</i><sup>2</sup>)erfc(<i>x</i>).
     * 
     * @param x x
     * @return erfcx(x)
     */
    public abstract double erfcx(double x);
    
    /**
     * 新しいインスタンスを生成して返す.
     * 
     * @return インスタンス
     */
    public static ErrorFunctionCalculation createInstance() {
        return new ErfCalcOptimized();
    }

}
