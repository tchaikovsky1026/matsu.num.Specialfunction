package matsu.num.specialfunction.err.subpj.erf;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * スケーリング相補誤差関数erfcx(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfcx(x) = 1/(x*sqrt(pi)) *
 * sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k} <br>
 * そこで, u = t^2 として, <br>
 * erfcx(x) = 1/(x*sqrt(pi)) * F(u) <br>
 * としたときの, 多項式 F(u) を近似する. <br>
 * F(u) = sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * u^k
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * さらに, F'(u) の1次近似をf'(u)とすれば, <br>
 * f'(u) = -1/2 + 3/4 * u <br>
 * であり, <br>
 * F'(u) - f'(u) = sum_{k=3}^{inf} (-1)^k * (2k-1)!! / 2^k * u^{k-1} <br>
 * に対する多項式近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * <p>
 * F'(u) - f'(u)は漸近級数であり, 収束しない. <br>
 * そこで, 連分数に変形する. <br>
 * F'(u) - f'(u) = -(1*3*5)/2^3 * u^2 * G(u) <br>
 * とすると, <br>
 * G(u) = 1 - c_1*u + c_1*c_2*u^2 - ... <br>
 * の形をしており, <br>
 * c_1 = 7/2, c_2 = 9/2, ... <br>
 * である. これを商差法により連分数に変換すれば, 最上段の係数1を除いて, <br>
 * a_1 = 7/2, a_2 = 1, <br>
 * a_3 = 9/2, a_4 = 2, <br>
 * a_5 = 11/2, a_6 = 3, ... <br>
 * となる. ただし, <br>
 * G(u) = 1/(1+) a_1*u/(1+) a_2*u/(1+) ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_ErfcxLargeX_accuracy extends RawCoefficientCalculableFunction {

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX = 100;

    private static final double UPPER_LIMIT_OF_T_MAX = 1d;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            -1d / 2,
            3d / 4
    };

    public MinimaxApproxFunc_ErfcxLargeX_accuracy(double tmax) {
        super();

        if (!(0 < tmax
                && tmax <= UPPER_LIMIT_OF_T_MAX)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.interval = DoubleFiniteClosedInterval.from(0d, tmax * tmax);
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        /*
         * F'(u) - f'(u) = -(1*3*5)/2^3 * u^2 * G(u)
         * G(u) = 1/(1+) a_1*u/(1+) a_2*u/(1+) ...
         * a_1 = 7/2, a_2 = 1,
         * a_3 = 9/2, a_4 = 2,
         * a_5 = 11/2, a_6 = 3, ...
         */
        double value = 1d;
        for (int k = K_MAX + 1; k >= 1; k--) {
            value *= k * u;
            value += 1;
            value = 1 / value;

            value *= (k + 2.5) * u;
            value += 1;
            value = 1 / value;
        }

        return -value * u * u * 15d / 8d;
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
