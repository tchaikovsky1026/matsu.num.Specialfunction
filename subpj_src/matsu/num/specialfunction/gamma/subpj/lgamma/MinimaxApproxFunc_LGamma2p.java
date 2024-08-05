package matsu.num.specialfunction.gamma.subpj.lgamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.bessel.subpj.RawCoefficientCalculableFunction;
import matsu.num.specialfunction.gamma.subpj.component.RiemannZetaParameter;

/**
 * logGamma(2+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * logGamma(2+x) = x * F(x) とし, F(x) に対して多項式近似する. <br>
 * F(x) = (1-gamma)
 * + sum_{k=2}^{inf} (-1)^k (zeta(k)-1)/k * x^{k-1} <br>
 * F(x)の第0次近似をf(x)とすると, <br>
 * f(x) = 1-gamma <br>
 * F(x) - f(x) = sum_{k=2}^{inf} (-1)^k (zeta(k)-1)/k * x^{k-1} <br>
 * スケールは, (1-gamma) とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_LGamma2p implements RawCoefficientCalculableFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1 - GammaFunction.EULER_MASCHERONI_GAMMA
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval interval
     */
    public MinimaxApproxFunc_LGamma2p(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    public double value(double x) {

        double value = 0;

        for (int k = RiemannZetaParameter.MAX_M; k >= 2; k--) {
            value *= -x;
            value += RiemannZetaParameter.zetam1(k) / k;
        }

        return value * x;
    }

    @Override
    public double scale(double x) {
        return 1 - GammaFunction.EULER_MASCHERONI_GAMMA;
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
