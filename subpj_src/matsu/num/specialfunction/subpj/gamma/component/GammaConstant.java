package matsu.num.specialfunction.subpj.gamma.component;

import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;

/**
 * ガンマ関数に関連する定数.
 * 
 * @author Matsuura Y.
 */
public final class GammaConstant {

    private static final String EULER_MASCHERONI_GAMMA_STRING =
            "0.57721566490153286060651209008240243";

    /**
     * オイラーマスケローニ定数.
     */
    public static final DoubleDoubleFloatElement EULER_MASCHERONI_GAMMA =
            DoubleDoubleFloatElement.fromDecimalExpression(EULER_MASCHERONI_GAMMA_STRING);

    private GammaConstant() {
        //インスタンス化不可
        throw new AssertionError();
    }
}
