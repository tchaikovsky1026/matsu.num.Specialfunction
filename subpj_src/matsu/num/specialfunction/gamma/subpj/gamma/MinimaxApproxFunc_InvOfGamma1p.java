package matsu.num.specialfunction.gamma.subpj.gamma;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.gamma.LGammaCalculation;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * F(x) = 1/Gamma(1+x) のminimax近似のための関数. <br>
 * 第0近似: f(x) = 1 について, <br>
 * F(x) - f(x) に対してminimax近似を実行する.
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_InvOfGamma1p extends RawCoefficientCalculableFunction {

    private static final LGammaCalculation LGAMMA = new LGammaCalculation();

    private static final double LOWER_LIMIT_OF_INTERVAL = -0.5;
    private static final double UPPER_LIMIT_OF_INTERVAL = 0.5;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1
    };

    /**
     * 唯一のコンストラクタ.
     * 
     * @param interval interval
     */
    public MinimaxApproxFunc_InvOfGamma1p(DoubleFiniteClosedInterval interval) {
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

        double a = Exponentiation.expm1(LGAMMA.lgamma1p(-x));
        double b = b(x);

        return a * b + a + b;
    }

    @Override
    protected double calcScale(double x) {
        if (!this.accepts(x)) {
            return Double.NaN;
        }

        return 1;
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

    private static double b(double x) {
        double t = x * Math.PI;
        double t2 = t * t;

        int kMax = 50;

        double value = 0;
        for (int k = kMax; k >= 2; k--) {
            value *= -t2 / (2 * k * (2 * k + 1));
            value += 1d;
        }
        return -value * t2 / 6;
    }
}
