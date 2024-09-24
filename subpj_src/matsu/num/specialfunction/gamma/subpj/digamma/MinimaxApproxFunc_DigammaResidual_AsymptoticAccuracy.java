package matsu.num.specialfunction.gamma.subpj.digamma;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.gamma.subpj.component.EvenBernoulli;
import matsu.num.specialfunction.subpj.RawCoefficientCalculableFunction;

/**
 * ディガンマ関数 psi(x) に対するStirling近似の残差を高精度にminimax近似する.
 * 
 * <p>
 * 漸近展開は, <br>
 * psi(x) = log(x) - 1/(2x)
 * + sum_{k=1}^{inf} (-B_{2k})/(2k) * x^{-2k} <br>
 * Stirling近似の残差とは総和部分であり, t = 1/x として, <br>
 * R(t) = sum_{k=1}^{inf} (-B_{2k})/(2k) * t^{2k}
 * </p>
 * 
 * <p>
 * u = t＾2 の 関数と見て, <br>
 * R(t) = u * G(u), <br>
 * G(u) = sum_{k=0}^{inf} (-B_{2k+2})/(2k+2) * u^k <br>
 * としたときのG(u)の近似を考える.
 * </p>
 * 
 * <p>
 * G(u)を {@literal u -> 0} で厳密にしたいので, <br>
 * G'(u) = (G(u) + 1/12) / u <br>
 * を多項式近似する. <br>
 * G'(u) = sum_{k=0}^{inf} (-B_{2k+4})/(2k+4) * u^k <br>
 * スケールは, 1/(12u) とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class MinimaxApproxFunc_DigammaResidual_AsymptoticAccuracy
        extends RawCoefficientCalculableFunction {

    private static final double SCALE_U_THRESHOLD = 1d / 65536;

    private final DoubleFiniteClosedInterval interval;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param tmax tに関する区間の最大値, u = t^2 となることに注意.
     */
    public MinimaxApproxFunc_DigammaResidual_AsymptoticAccuracy(double tmax) {

        double umax = tmax * tmax;
        if (!(umax > 0)) {
            throw new IllegalArgumentException("tmax異常または小さすぎ");
        }

        this.interval = DoubleFiniteClosedInterval.from(0d, umax);
    }

    @Override
    protected double calcValue(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        // sum_{k=0}^{inf} (-B_{2k+2})/((2k+2)) * u^k
        // の計算.
        double value = 0;
        for (int k = EvenBernoulli.MAX_M; k >= 2; k--) {
            value *= u;
            value += -EvenBernoulli.evenBernoulli(k) / (2 * k);
        }
        return value;
    }

    @Override
    protected double calcScale(double u) {
        if (!this.accepts(u)) {
            return Double.NaN;
        }

        if (u < SCALE_U_THRESHOLD) {
            return 1 / (12 * SCALE_U_THRESHOLD);
        }

        return 1 / (12 * u);
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * G'(u) の近似多項式の係数を与え,
     * G(u) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff G'(u)の係数
     * @return G(u)の係数
     */
    @Override
    public double[] rawCoeff(double[] thisCoeff) {
        double[] coeffGp = thisCoeff.clone();

        double[] coeffF = new double[coeffGp.length + 1];
        coeffF[0] = -1d / 12;
        System.arraycopy(coeffGp, 0, coeffF, 1, coeffGp.length);

        return coeffF;
    }
}
