package matsu.num.specialfunction.subpj.bessel.modbessel.power;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * <p>
 * I0の計算の近似のためのターゲット. <br>
 * {@literal 2 <= x <= 24}を対象.
 * </p>
 * 
 * <p>
 * I0はexpの発散が強くなるので, I0(x)*exp(-x)に対して近似を行う.
 * スケールは1である.
 * </p>
 * 
 * <p>
 * 丸め誤差への警戒のため, minX=0となるようにシフトする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class BesselI0Power_ExpTerm extends RawCoeffCalculableDoubleFunction {

    private static final double MIN_MIN_X = 2d;
    private static final double MAX_MAX_X = 24d;

    private static final int K_MAX_FOR_BESSEL_K_BY_POWER = 40;

    private static final double[] ESTIMATED_COEFF = {
    };

    private final DoubleFiniteClosedInterval interval;
    private final double minX;

    /**
     * @param interval 区間[0, maxX - minX]を与える.
     * @param minX minX
     */
    public BesselI0Power_ExpTerm(DoubleFiniteClosedInterval interval, double minX) {
        super();
        this.interval = interval;

        if (!(interval.lower() == 0 &&
                MIN_MIN_X <= minX &&
                interval.upper() + minX <= MAX_MAX_X)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
        this.minX = minX;
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        final double x = u + minX;
        final double halfX = x / 2;
        final double sqHalfX = halfX * halfX;

        double value = 0;
        for (int k = K_MAX_FOR_BESSEL_K_BY_POWER + 1; k >= 1; k--) {
            value *= sqHalfX / (k * k);
            value += 1;
        }

        return value * Math.exp(-x);
    }

    @Override
    protected double calcScale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        return 1;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F(u) - f(u)] の近似多項式の係数を与え,
     * F(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F(u) - f(u)]の係数
     * @return F(u)の係数
     */
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
