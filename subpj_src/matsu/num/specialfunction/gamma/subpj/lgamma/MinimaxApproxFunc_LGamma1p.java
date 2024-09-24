package matsu.num.specialfunction.gamma.subpj.lgamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.gamma.subpj.component.RiemannZetaParameter;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * logGamma(1+x)をminimax近似する. <br>
 * {@literal -1/2 <= x <= 1/2} を扱う.
 * 
 * <p>
 * logGamma(1+x) = x * F(x) とし, F(x) に対して多項式近似する. <br>
 * F(x) = -gamma
 * + sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k-1} <br>
 * F(x)の第0次近似をf(x)とすると, <br>
 * f(x) = -gamma <br>
 * F(x) - f(x) = sum_{k=2}^{inf} (-1)^k zeta(k)/k * x^{k-1} <br>
 * スケールは, gamma とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_LGamma1p extends RawCoefficientCalculableFunction {

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            -GammaFunction.EULER_MASCHERONI_GAMMA
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval interval
     */
    public MinimaxApproxFunc_LGamma1p(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    protected double calcValue(double x) {

        double value = 0;

        for (int k = RiemannZetaParameter.MAX_M; k >= 2; k--) {
            value *= -x;
            value += RiemannZetaParameter.zeta(k) / k;
        }

        return value * x;
    }

    @Override
    protected double calcScale(double x) {
        return GammaFunction.EULER_MASCHERONI_GAMMA;
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
