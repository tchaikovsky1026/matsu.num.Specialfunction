package matsu.num.specialfunction.subpj.err.erfi;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * erfix(x)のテイラー展開をminimax近似する. <br>
 * {@literal 0.5 <= x <= 8.5} の部分区間を扱う. <br>
 * ただし, 区間の中央を0にシフトし, xp = x - x_shift の形で扱う.
 * 
 * <p>
 * 厳密式:
 * erfix(x) = 2/sqrt(pi) * x *
 * sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^k <br>
 * に対し, 漸近展開の形: erfix(x) = 1/(x*sqrt(pi)) * F(x) を適用する. <br>
 * このF(x)は {@literal 1 <= x <= 8} で2倍も変化しないことがわかっている.
 * これを使い, <br>
 * F(x) = 2x^2 * sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^k <br>
 * に対して近似を適用する. <br>
 * スケールは1である.
 * </p>
 * 
 * <p>
 * F(x) = 2x^2 * sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^k
 * の級数は {@literal 1 <= x <= 8} において交代的で素性が良くない. <br>
 * そこで, この右辺を連分数に変換した, 次を用いる (u = 2x^2 とする). <br>
 * F(t) = u/(1+) (a_1*u)/(1+) (a_2*u)/(1+) ..., <br>
 * a_1 = 1/(1*3), a_2 = -2/(3*5), <br>
 * a_3 = 3/(5*7), a_4 = -4/(7*9), <br>
 * a_5 = 5/(9*11), a_6 = -6/(11*13), <br>
 * ...
 * </p>
 * 
 * <p>
 * F(x) がほぼ1であることがわかっているので, F(x)-1に対しての近似とする.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class ErfixFuncLargeX extends RawCoeffCalculableDoubleFunction {

    private static final int K_MAX = 1000;

    private static final double LOWER_LIMIT_OF_X_MIN = 0.5d;
    private static final double UPPER_LIMIT_OF_X_MAX = 8.5d;

    private final double x_shift;
    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d
    };

    public static void main(String[] args) {
        ErfixFuncLargeX func = new ErfixFuncLargeX(1d, 8d);
        System.out.println("x\tf");
        for (double x = 1d - 4.5d; x <= 8d - 4.5d; x += 0.125) {
            System.out.println(x + "\t" + func.value(x));
        }

    }

    public ErfixFuncLargeX(double xmin, double xmax) {
        super();

        if (!(LOWER_LIMIT_OF_X_MIN <= xmin
                && xmin < xmax
                && xmax <= UPPER_LIMIT_OF_X_MAX)) {
            throw new IllegalArgumentException("区間異常");
        }

        this.x_shift = (xmin + xmax) * 0.5;

        this.interval = DoubleFiniteClosedInterval.from(xmin - x_shift, xmax - x_shift);
    }

    @Override
    protected double calcValue(double xp) {
        if (!this.accepts(xp)) {
            return Double.NaN;
        }
        final double x = xp + this.x_shift;

        /*
         * u = 2x^2 = 2/t^2 とする.
         * F(t) = u/(1+) (a_1*u)/(1+) (a_2*u)/(1+) ...,
         * a_1 = 1/(1*3), a_2 = -2/(3*5),
         * a_3 = 3/(5*7), a_4 = -4/(7*9),
         * a_5 = 5/(9*11), a_6 = -6/(11*13),
         * ...
         */

        final double u = 2 * x * x;

        double value = 1d;
        for (int k = K_MAX + 1; k >= 1; k--) {
            value *= u * k / (2 * k - 1) / (2 * k + 1);
            value = k % 2 == 1 ? value : -value;
            value += 1d;
            value = 1 / value;
        }

        return value * u - 1d;
    }

    @Override
    protected double calcScale(double xp) {
        if (!this.accepts(xp)) {
            return Double.NaN;
        }

        return 1;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    @Override
    public String toString() {
        return "x_shift = " + this.x_shift + ", " + this.interval.toString();
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
