package matsu.num.specialfunction.subpj.gamma.component;

import java.math.BigDecimal;

import matsu.num.approximation.generalfield.PseudoRealNumber.Provider;
import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;

/**
 * ガンマ関数に関連する定数.
 * 
 * @author Matsuura Y.
 */
public final class GammaConstant {

    private static final Provider<DoubleDoubleFloatElement> PROVIDER =
            DoubleDoubleFloatElement.elementProvider();

    private static final String EULER_MASCHERONI_GAMMA_STRING =
            "0.57721566490153286060651209008240243";

    /**
     * オイラーマスケローニ定数.
     */
    public static final DoubleDoubleFloatElement EULER_MASCHERONI_GAMMA;

    static {
        BigDecimal value = new BigDecimal(EULER_MASCHERONI_GAMMA_STRING);
        double high = value.doubleValue();
        double low = value.subtract(new BigDecimal(high)).doubleValue();

        EULER_MASCHERONI_GAMMA = PROVIDER.fromDoubleValue(high).plus(low);
    }

    private GammaConstant() {
        //インスタンス化不可
        throw new AssertionError();
    }
}
