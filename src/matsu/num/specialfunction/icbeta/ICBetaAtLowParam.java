/*
 * 2023.3.22
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満), 片方のパラメータが小さい場合. <br>
 * Min(a,b)が11以下を扱う. <br>
 * <br>
 * ある閾値を境に, I(a,b,x)とI(b,a,1-x)のどちらを計算するかを切り替える. 
 *
 * @author Matsuura Y.
 * @version 11.0
 */
final class ICBetaAtLowParam extends SkeletalICBeta implements IncompleteBetaFunction {

    private final double a;
    private final double b;
    private final double lnBetaAB;
    private final double oddsThreshold;

    private ICBetaAtLowParam(double a, double b) {
        if (!(Math.min(a, b) <= ICBetaFactory.AB_THRESHOLD_FIRST)) {
            throw new AssertionError(String.format(
                    "Bug:Math.min(a, b)<=threshold_1stでない:(a,b)=(%.16G,%.16G)", a, b));
        }

        this.a = a;
        this.b = b;
        this.lnBetaAB = GammaFunction.lbeta(a, b);

        final double coeff = (ICBetaFactory.AB_THRESHOLD_FIRST - 3) / ICBetaFactory.AB_THRESHOLD_FIRST;

        double ap = a < ICBetaFactory.AB_THRESHOLD_FIRST ? coeff * a + 2 : a;
        double bp = b < ICBetaFactory.AB_THRESHOLD_FIRST ? coeff * b + 2 : b;
        this.oddsThreshold = ap / bp;
    }

    @Override
    public final double a() {
        return this.a;
    }

    @Override
    public final double b() {
        return this.b;
    }

    @Override
    protected final double oddsValue(double oddsX) {

        final double x = 1 / (1 + 1 / oddsX);
        final double y = 1 / (1 + oddsX);

        if (oddsX < this.oddsThreshold) {
            double lcp = ICBContinuedFractionFactor.factorLowerSide(x, this.a, this.b) * coeffToICBeta(x, y);
            return lcp / (1 - lcp);
        } else {
            double ucp = ICBContinuedFractionFactor.factorLowerSide(y, this.b, this.a) * coeffToICBeta(x, y);
            return (1 - ucp) / ucp;
        }
    }

    /*
     x^a y^b / B(a,b) を返す. 
     */
    private double coeffToICBeta(double x, double y) {
        return Exponentiation.exp(this.a * Exponentiation.log(x) + this.b * Exponentiation.log(y) - this.lnBetaAB);
    }

    /**
     * 
     * @param a a
     * @param b b
     * @return インスタンス
     */
    public static IncompleteBetaFunction instanceOf(double a, double b) {
        return new ICBetaAtLowParam(a, b);
    }

}
