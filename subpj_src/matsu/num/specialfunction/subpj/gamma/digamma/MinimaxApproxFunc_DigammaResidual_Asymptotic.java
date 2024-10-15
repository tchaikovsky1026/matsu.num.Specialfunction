package matsu.num.specialfunction.subpj.gamma.digamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * ディガンマ関数 psi(x) に対するStirling近似の残差ををminimax近似する. <br>
 * 
 * <p>
 * 漸近展開は, <br>
 * psi(x) = log(x) - 1/(2x)
 * + sum_{k=1}^{inf} (-B_{2k})/(2k) * x^{-2k} <br>
 * Stirling近似の残差とは総和部分であり, t = 1/x として, <br>
 * R(t) = sum_{k=1}^{inf} (-B_{2k})/(2k) * t^{2k} <br>
 * に対し, tの多項式で近似を行う. <br>
 * ただし, 計算安定性のため, R(t)/t^2 + 1/12 に対して最適化する. <br>
 * スケールは 1/12 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_DigammaResidual_Asymptotic
        extends RawCoeffCalculableDoubleFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = 1d / 10;
    private static final double UPPER_LIMIT_OF_INTERVAL = 1d / 2.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            -1d / 12
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval 近似区間
     * @throws IllegalArgumentException
     *             区間が {@literal 1/10 <= t <= 1/4} の中に入っていない場合
     * @throws NullPointerException null
     */
    public MinimaxApproxFunc_DigammaResidual_Asymptotic(
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

        return DigammaResidual.value(1d / t) / (t * t) + 1d / 12;
    }

    @Override
    protected double calcScale(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        return 1d / 12;
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
