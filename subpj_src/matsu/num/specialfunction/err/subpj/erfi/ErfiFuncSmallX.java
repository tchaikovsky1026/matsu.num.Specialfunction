package matsu.num.specialfunction.err.subpj.erfi;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * erfi(x)のminimax近似を行うための関数. <br>
 * {@literal -1 <= x <= 1} を扱う.
 * 
 * <p>
 * u = x^2 とすると, <br>
 * erfi(x) = 2/sqrt(pi) * x * F(u) <br>
 * F(u) = sum_{k=0}^{inf} 1/(k!(2k+1)) * u^k <br>
 * である. F(u) を近似する.
 * </p>
 * 
 * <p>
 * F(u) をu=0で厳密にしたいので, <br>
 * F'(u) = (F(u)-1) / u <br>
 * に対して多項式近似を行う (スケールは1/u). <br>
 * さらに, F'(u) の2次近似を f'(u) とすると, <br>
 * f'(u) = 1/(3*1!) + 1/(5*2!) * u + 1/(7*3!) * u^2 <br>
 * F'(u) - f'(u) = sum_{k=4}^{inf} 1/(k!(2k+1)) * u^{k-1}
 * </p>
 * 
 * @author Matsuura Y.
 */
final class ErfiFuncSmallX extends RawCoefficientCalculableFunction {

    private static final double UPPER_LIMIT_OF_X_MAX = 1d;

    private final DoubleFiniteClosedInterval interval;

    private static final int K_MAX = 40;

    private static final double[] ESTIMATED_COEFF = {
            1d / (3 * 1),
            1d / (5 * 2),
            1d / (7 * 6)
    };

    public ErfiFuncSmallX(double xmax) {
        if (!(0 <= xmax && xmax <= UPPER_LIMIT_OF_X_MAX)) {
            throw new IllegalArgumentException("引数異常");
        }
        this.interval = DoubleFiniteClosedInterval.from(0, xmax * xmax);
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        double value = 0d;
        for (int k = K_MAX + 1; k >= 5; k--) {
            value *= u / k;
            value += 1d / (2 * k - 1);
        }

        return value * u * u * u / 24;
    }

    @Override
    protected double calcScale(double u) {
        if (!this.accepts(u)) {
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
