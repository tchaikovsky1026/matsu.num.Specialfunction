package matsu.num.specialfunction.err.subpj.erfi;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * erfix(x)の漸近展開をminimax近似する. <br>
 * t=1/xとして, {@literal 0 <= t <= 1/8} を扱う.
 * 
 * <p>
 * 厳密式:
 * erfix(x) = 1/(x*sqrt(pi)) *
 * sum_{k=0}^{inf} (2k-1)!! / 2^k * t^{2k} <br>
 * そこで, u = t^2 として, <br>
 * erfix(x) = 1/(x*sqrt(pi)) * F(u) <br>
 * としたときの, 多項式 F(u) を近似する. <br>
 * F(u) = sum_{k=0}^{inf} (2k-1)!! / 2^k * u^k
 * </p>
 * 
 * <p>
 * F(u) を u = 0 で厳密にしたいので, <br>
 * F'(u) = (F(u) - 1) / u <br>
 * に対して近似を行う. <br>
 * さらに, F'(u) の1次近似をf'(u)とすれば, <br>
 * f'(u) = 1/2 + 3/4 * u <br>
 * であり, <br>
 * F'(u) - f'(u) = sum_{k=3}^{inf} (2k-1)!! / 2^k * u^{k-1} <br>
 * に対する多項式近似を行う. <br>
 * スケールは 1/u とする.
 * </p>
 * 
 * <p>
 * F'(u) - f'(u)は漸近級数であり, 連分数にも変換できない. <br>
 * そこで, 級数の上限を50とし, tを1/8以下に限定する. <br>
 * 倍精度はこれで十分.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class ErfixFuncLargeX_accuracy extends RawCoefficientCalculableFunction {

    private static final double SCALE_U_THRESHOLD = 1d / 1024;

    private static final int K_MAX = 50;

    private static final double UPPER_LIMIT_OF_T_MAX = 1d / 8;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d / 2,
            3d / 4
    };

    public ErfixFuncLargeX_accuracy(double tmax) {
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
         * F'(u) - f'(u) = sum_{k=3}^{kMax} (2k-1)!! / 2^k * u^{k-1}
         */
        double value = 0d;
        for (int k = K_MAX + 1; k >= 4; k--) {
            value *= u * (2 * k - 1) / 2d;
            value += 1d;
        }

        return value * u * u * 15d / 8d;
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
