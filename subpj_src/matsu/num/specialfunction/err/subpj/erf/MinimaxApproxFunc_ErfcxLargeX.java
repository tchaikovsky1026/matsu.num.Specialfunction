package matsu.num.specialfunction.err.subpj.erf;

import java.util.Objects;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.bessel.subpj.RawCoefficientCalculableFunction;

/**
 * スケーリング相補誤差関数erfcx(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfcx(x) = 1/(x*sqrt(pi)) *
 * sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k} <br>
 * erfcx(x) = 1/(x*sqrt(pi)) * F(t) <br>
 * としたときの, 多項式 F(t) を近似する. <br>
 * F(t) = sum_{k=0}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k}
 * </p>
 * 
 * <p>
 * F(t) の5次近似をf(u)とすれば, <br>
 * f(t) = 1 - 1/2 t^2 + 3/4 * t^4 <br>
 * であり, <br>
 * F(t) - f(t) = sum_{k=3}^{inf} (-1)^k * (2k-1)!! / 2^k * t^{2k} <br>
 * に対する多項式近似を行う. <br>
 * スケールは 1 とする.
 * </p>
 * 
 * <p>
 * F(t) - f(t)は漸近級数であり, 収束しない. <br>
 * そこで, 連分数に変形する. <br>
 * F(t) - f(t) = -(1*3*5)/2^3 * t^6 * G(t) <br>
 * とすると, <br>
 * G(t) = 1 - c_1*t^2 + c_1*c_2*t^4 - ... <br>
 * の形をしており, <br>
 * c_1 = 7/2, c_2 = 9/2, ... <br>
 * である. これを商差法により連分数に変換すれば, 最上段の係数1を除いて, <br>
 * a_1 = 7/2, a_2 = 1, <br>
 * a_3 = 9/2, a_4 = 2, <br>
 * a_5 = 11/2, a_6 = 3, ... <br>
 * となる. ただし, <br>
 * G(t) = 1/(1+) a_1*t^2/(1+) a_2*t^2/(1+) ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_ErfcxLargeX implements RawCoefficientCalculableFunction {

    private static final int K_MAX = 100;

    private static final double LOWER_LIMIT_OF_INTERVAL = 0d;
    private static final double UPPER_LIMIT_OF_INTERVAL = 1d;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d,
            0d,
            -1d / 2,
            0d,
            3d / 4,
            0d
    };

    public MinimaxApproxFunc_ErfcxLargeX(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = Objects.requireNonNull(interval);

        if (!(LOWER_LIMIT_OF_INTERVAL <= interval.lower()
                && interval.upper() <= UPPER_LIMIT_OF_INTERVAL)) {
            throw new IllegalArgumentException("区間異常");
        }
    }

    @Override
    public double value(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        /*
         * F(t) - f(t) = -(1*3*5)/2^3 * t^6 * G(t)
         * G(t) = 1/(1+) a_1*t^2/(1+) a_2*t^2/(1+) ...
         * a_1 = 7/2, a_2 = 1,
         * a_3 = 9/2, a_4 = 2,
         * a_5 = 11/2, a_6 = 3, ...
         */
        final double u = t * t;

        double value = 1d;
        for (int k = K_MAX + 1; k >= 1; k--) {
            value *= k * u;
            value += 1;
            value = 1 / value;

            value *= (k + 2.5) * u;
            value += 1;
            value = 1 / value;
        }

        return -value * u * u * u * 15d / 8d;
    }

    @Override
    public double scale(double t) {
        if (!this.accepts(t)) {
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
