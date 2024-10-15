package matsu.num.specialfunction.subpj.gamma.lgamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * 対数ガンマ関数に対するStirling近似の残差をminimax近似する. <br>
 * 
 * <p>
 * 残差を R(t) とする. (ただし, t = 1/x) <br>
 * R(t) = sum_{k=1}^{inf} B_{2k}/(2k(2k-1)) * t^{2k-1} <br>
 * R(t)の近似を考える. <br>
 * {@literal 1/10 <= t <= 1/4} が対象. <br>
 * ただし, 計算安定性のため, R(t)/t - 1/12 を最適化しよう.
 * </p>
 * 
 * <p>
 * スケールは, 1/12 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_LGammaResidual_Asymptotic
        extends RawCoeffCalculableDoubleFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = 1d / 10;
    private static final double UPPER_LIMIT_OF_INTERVAL = 1d / 2.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d / 12
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval 近似区間
     * @throws IllegalArgumentException
     *             区間が {@literal 1/10 <= t <= 1/4} の中に入っていない場合
     * @throws NullPointerException null
     */
    public MinimaxApproxFunc_LGammaResidual_Asymptotic(
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

        return LGammaResidual.value(1d / t) / t - 1d / 12;
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

    /**
     * [F'(u) - f'(u)] の近似多項式の係数を与え,
     * F(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F'(u) - f'(u)]の係数
     * @return F(u)の係数
     */
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
