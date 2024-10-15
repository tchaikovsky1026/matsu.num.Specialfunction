package matsu.num.specialfunction.subpj.gamma.trigamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;
import matsu.num.specialfunction.subpj.gamma.component.RiemannZetaParameter;

/**
 * psi1(2+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * F(x) = psi1(2+x) = sum_{m=2}^{inf}
 * (-1)^m * (m-1) * zetam1(m) * z^{m-2} <br>
 * に対して多項式近似を行う. <br>
 * ただし, 誤差低減のため, 第2次近似を f(x) として,
 * F(x) - f(x) を扱う. <br>
 * スケールは, 1 とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_Trigamma2p extends RawCoeffCalculableDoubleFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final DoubleFiniteClosedInterval interval;
    private static final double[] ESTIMATED_COEFF = {
            RiemannZetaParameter.zetam1(2),
            -2 * RiemannZetaParameter.zetam1(3),
            3 * RiemannZetaParameter.zetam1(4)
    };

    public MinimaxApproxFunc_Trigamma2p(DoubleFiniteClosedInterval interval) {
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

        /*
         * sum_{m=5}^{inf} (-1)^m * (m-1) * zetam1(m) * z^{m-2}
         */

        for (int k = RiemannZetaParameter.MAX_M; k >= 5; k--) {
            value *= -x;
            value += (k - 1) * RiemannZetaParameter.zetam1(k);
        }

        return -value * x * x * x;
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
