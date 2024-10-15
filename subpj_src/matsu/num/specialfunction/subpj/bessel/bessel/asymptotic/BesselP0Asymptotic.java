package matsu.num.specialfunction.subpj.bessel.bessel.asymptotic;

import java.util.function.IntFunction;

import matsu.num.approximation.DoubleFiniteClosedInterval;
import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.fraction.BigRational;
import matsu.num.specialfunction.fraction.ComplexContinuedFractionFunction;
import matsu.num.specialfunction.fraction.ComplexNumber;
import matsu.num.specialfunction.fraction.ContinuedFractionFunction;
import matsu.num.specialfunction.subpj.RawCoeffCalculableDoubleFunction;

/**
 * <p>
 * P0の計算の近似のためのターゲット. <br>
 * t = 1/(8x) として, {@literal 0 <= t <= 1/16} を引数とする形で計算する. <br>
 * J0(x) = sqrt(2/(pi*x)) * (P0(t)cos + Q0(t)sin) <br>
 * Y0(x) = sqrt(2/(pi*x)) * (P0(t)sin - Q0(t)cos) <br>
 * として, P0(t) を推定する. <br>
 * (P0(x)^2 + Q0(x)^2) は x が0から1/16までで2割程度しか変化しない. <br>
 * よって, スケールは1とする.
 * </p>
 * 
 * <p>
 * P0はtの0次, 2次, ... しかないが, 奇数次も含むとして推定する.
 * </p>
 * 
 * <p>
 * P0(t)の第3次近似をp(u)とする. <br>
 * p(u) = 1 - (1^2 * 3^2 /2!)t^2
 * 
 * @author Matsuura Y.
 */
final class BesselP0Asymptotic extends RawCoeffCalculableDoubleFunction {

    private static final ComplexContinuedFractionFunction H0_UPPER4 = h0_upper4();

    private static final double MIN_T = 0d;
    private static final double MAX_T = 1d / 16;

    private final DoubleFiniteClosedInterval interval;

    private static final double[] ESTIMATED_COEFF = {
            1d,
            0d,
            -(3 * 3) / 2d,
            0d
    };

    public BesselP0Asymptotic(DoubleFiniteClosedInterval interval) {
        super();
        this.interval = interval;

        if (!(MIN_T <= interval.lower() &&
                interval.upper() <= MAX_T)) {
            throw new IllegalArgumentException("区間がサポート外");
        }
    }

    @Override
    protected double calcValue(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        ComplexNumber vc = H0_UPPER4.value(ComplexNumber.ofImaginary(t));

        double v = vc.real();
        v *= 3 * 3;
        v *= 5 * 5;
        v *= 7 * 7;
        v /= 24;
        v *= Exponentiation.pow(t, 4);

        return v;
    }

    @Override
    protected double calcScale(double t) {
        if (!this.accepts(t)) {
            return Double.NaN;
        }

        return 1d;
    }

    @Override
    public DoubleFiniteClosedInterval interval() {
        return this.interval;
    }

    /**
     * [F(t) - f(t)] の近似多項式の係数を与え,
     * F(t) の近似多項式係数を計算して返す.
     * 
     * @param thisCoeff [F(t) - f(t)]の係数
     * @return F(t)の係数
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

    /**
     * P0+iQの漸近連分数の {@literal m >= 4} でのもの. <br>
     * 引数はtである. <br>
     * 4次が定数項1に対応するため, 実際は全体に, <br>
     * (1^2 * 3^2 * 5^2 * 7^2 /4!)t^4
     * <br>
     * が乗算される.
     */
    private static ComplexContinuedFractionFunction h0_upper4() {

        final int kMax = 60;

        IntFunction<BigRational> func =
                k -> BigRational.of(((2 * k + 9) * (2 * k + 9)), k + 5);

        return new ComplexContinuedFractionFunction(
                ContinuedFractionFunction.from(
                        kMax, func,
                        BigRational.constantSupplier()).asDoubleFunction());
    }

}
