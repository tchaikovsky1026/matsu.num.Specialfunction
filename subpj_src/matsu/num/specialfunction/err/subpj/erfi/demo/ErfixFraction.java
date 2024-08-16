package matsu.num.specialfunction.err.subpj.erfi.demo;

/**
 * スケーリング虚数誤差関数の連分数による計算.
 * 
 * <p>
 * erfi(x) = 2x/sqrt(pi) *
 * sum_{k=0}^{inf} (-1)^k / (2k+1)!! * (2x^2)^k <br>
 * であり, この交代級数部分は厳密に次式に一致する (t = 2x^2 とする). <br>
 * sum_{k=0}^{inf} (-1)^k / (2k+1)!! * t^k <br>
 * = 1/(1+) (a_1*t)/(1+) (a_2*t)/(1+) ..., <br>
 * a_1 = 1/(1*3), a_2 = -2/(3*5), <br>
 * a_3 = 3/(5*7), a_4 = -4/(7*9), <br>
 * a_5 = 5/(9*11), a_6 = -6/(11*13), <br>
 * ...
 * </p>
 * 
 * @author Matsuura Y.
 */
final class ErfixFraction implements Erfix {

    private static final double TWO_OVER_SQRT_PI = 2d / Math.sqrt(Math.PI);

    private final int kMax;

    public ErfixFraction(int kMax) {
        super();
        this.kMax = kMax;
    }

    @Override
    public double erfix(double x) {

        final double x2Double = 2 * x * x;

        double value = 1d;
        for (int k = kMax + 1; k >= 1; k--) {
            value *= x2Double * k / (2 * k - 1) / (2 * k + 1);
            value = k % 2 == 1 ? value : -value;
            value += 1d;
            value = 1 / value;
        }

        return TWO_OVER_SQRT_PI * x * value;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), this.kMax);
    }

}
