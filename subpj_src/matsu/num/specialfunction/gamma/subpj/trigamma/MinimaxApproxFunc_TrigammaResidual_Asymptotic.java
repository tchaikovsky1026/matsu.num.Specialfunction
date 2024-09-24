package matsu.num.specialfunction.gamma.subpj.trigamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * トリガンマ関数 psi1(x) に対するStirling近似の残差ををminimax近似する. <br>
 * 
 * <p>
 * 漸近展開は, <br>
 * psi(x) = 1/x + 1/(2x^2)
 * + sum_{k=1}^{inf} B_{2k} * x^{-2k-1} <br>
 * Stirling近似の残差とは総和部分であり, t = 1/x として, <br>
 * R(t) = sum_{k=1}^{inf} B_{2k} * t^{2k+1} <br>
 * に対し, tの多項式で近似を行う. <br>
 * ただし, 計算安定性のため, R(t)/t^3 - 1/6 に対して最適化する. <br>
 * スケールは 1/6 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_TrigammaResidual_Asymptotic
        extends RawCoefficientCalculableFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = 1d / 10;
    private static final double UPPER_LIMIT_OF_INTERVAL = 1d / 2.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d / 6
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval 近似区間
     * @throws IllegalArgumentException
     *             区間が {@literal 1/10 <= t <= 1/4} の中に入っていない場合
     * @throws NullPointerException null
     */
    public MinimaxApproxFunc_TrigammaResidual_Asymptotic(
            DoubleFiniteClosedInterval interval) {

        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    protected double calcValue(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        return TrigammaResidual.value(1d / t) / (t * t * t) - 1d / 6;
    }

    @Override
    protected double calcScale(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        return 1d / 6;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffR = thisCoeff.clone();
        for (int i = 0;
                i < coeffR.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffR[i] += ESTIMATED_COEFF[i];
        }

        return coeffR;
    }
}
