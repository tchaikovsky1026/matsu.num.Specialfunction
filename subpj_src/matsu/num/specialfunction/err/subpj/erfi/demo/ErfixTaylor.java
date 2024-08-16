package matsu.num.specialfunction.err.subpj.erfi.demo;

/**
 * スケーリング虚数誤差関数のTaylor級数による計算.
 * 
 * @author Matsuura Y.
 */
final class ErfixTaylor implements Erfix {

    private static final double TWO_OVER_SQRT_PI = 2d / Math.sqrt(Math.PI);

    private final int kMax;

    public ErfixTaylor(int kMax) {
        super();
        this.kMax = kMax;
    }

    @Override
    public double erfix(double x) {

        final double x2Double = 2 * x * x;

        double value = 0d;
        for (int k = kMax + 1; k >= 1; k--) {
            value *= -x2Double / (2 * k + 1);
            value += 1d;
        }

        return TWO_OVER_SQRT_PI * x * value;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), this.kMax);
    }

}
