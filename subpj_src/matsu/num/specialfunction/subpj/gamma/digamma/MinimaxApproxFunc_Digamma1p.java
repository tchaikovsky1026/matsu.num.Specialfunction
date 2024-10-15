package matsu.num.specialfunction.subpj.gamma.digamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameter;

/**
 * psi(1+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * F(x) = psi(1+x) = -gamma + sum_{m=2}^{inf} (-1)^m * zeta(m) * z^{m-1} <br>
 * に対して多項式近似を行う. <br>
 * ただし, 誤差低減のため, 第2次近似を f(x) として,
 * F(x) - f(x) を扱う. <br>
 * スケールは, 1 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_Digamma1p extends RawCoeffCalculableDoubleFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private static final double GAMMA = GammaFunction.EULER_MASCHERONI_GAMMA;

    private final DoubleFiniteClosedInterval interval;
    private static final double[] ESTIMATED_COEFF = {
            -GAMMA,
            RiemannZetaParameter.zeta(2),
            -RiemannZetaParameter.zeta(3)
    };

    public MinimaxApproxFunc_Digamma1p(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    protected double calcValue(double x) {
        if (!this.accepts(x)) {
            return Double.NaN;
        }

        double value = 0;

        for (int k = RiemannZetaParameter.MAX_M; k >= 4; k--) {
            value *= -x;
            value += RiemannZetaParameter.zeta(k);
        }

        return value * x * x * x;
    }

    @Override
    protected double calcScale(double x) {
        if (!this.accepts(x)) {
            return Double.NaN;
        }

        return 1d;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffF = thisCoeff.clone();
        for (int i = 0;
                i < coeffF.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffF[i] += ESTIMATED_COEFF[i];
        }

        return coeffF;
    }

}
