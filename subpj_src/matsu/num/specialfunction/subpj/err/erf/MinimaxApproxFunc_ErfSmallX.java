package matsu.num.specialfunction.subpj.err.erf;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * 誤差関数erf(x)をminimax近似する. <br>
 * {@literal 0 <= x <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erf(x) = 2/sqrt(pi) * sum_{k=0}^{inf}
 * (-1)^k / (k! * (2k+1)) * x^{2k+1} <br>
 * そこで, u = x^2 として, <br>
 * erf(x) = 2/sqrt(pi) * x F(u) <br>
 * としたときの, 多項式 F(u) を近似する. <br>
 * F(u) = sum_{k=0}^{inf} (-1)^k / (k! * (2k+1)) * u^k
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * さらに, F'(u) の1次近似をf'(u)とすれば, <br>
 * f'(u) = -1/3 + 1/10 * u <br>
 * であり, <br>
 * F'(u) - f'(u) = sum_{k=3}^{inf} (-1)^k / (k! * (2k+1)) * u^{k-1} <br>
 * に対する多項式近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_ErfSmallX extends RawCoeffCalculableDoubleFunction {

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX = 40;

    private static final double LOWER_LIMIT_OF_INTERVAL = 0d;
    private static final double UPPER_LIMIT_OF_INTERVAL = 1d;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            -1d / 3,
            1d / 10
    };

    public MinimaxApproxFunc_ErfSmallX(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        /*
         * F'(u) - f'(u) =
         * sum_{k=3}^{inf} (-1)^k / (k! * (2k+1)) * u^{k-1}
         */
        double value = 0d;
        for (int k = K_MAX + 1; k >= 3; k--) {
            value *= -u / (k + 1);
            value += 1d / (2 * k + 1);
        }

        return -value * u * u / 6d;
    }

    @Override
    protected double calcScale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 1 / SCALE_U_THRESHOLD;
        }
        return 1 / u;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffFp = thisCoeff.clone();
        for (int i = 0;
                i < coeffFp.length && i < ESTIMATED_COEFF.length;
                i++) {
            coeffFp[i] += ESTIMATED_COEFF[i];
        }

        double[] coeffF = new double[coeffFp.length + 1];
        coeffF[0] = 1d;
        System.arraycopy(coeffFp, 0, coeffF, 1, coeffFp.length);

        return coeffF;
    }

}
