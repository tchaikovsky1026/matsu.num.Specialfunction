/*
 * 2023.12.6
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.commons.Exponentiation;
import matsu.num.specialfunction.GammaFunction;
import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * 不完全ベータ関数の計算(およそ倍精度未満). <br>
 * Min(a,b)が11以下を扱う.
 * 
 * <p>
 * ある閾値を境に, I(a,b,x)とI(b,a,1-x)のどちらを計算するかを切り替える.
 * </p>
 *
 * @author Matsuura Y.
 * @version 17.0
 */
final class ICBetaAtLowParam extends SkeletalICBeta implements IncompleteBetaFunction {

    private final double a;
    private final double b;
    private final double lnBetaAB;
    private final double oddsThreshold;

    /**
     * パラメータが対応外ならアサーションエラー
     * 
     * @param a
     * @param b
     */
    ICBetaAtLowParam(double a, double b) {
        super();
        if (!(ICBetaFactory.LOWER_LIMIT_OF_PARAMETER_AB <= Math.min(a, b)
                && Math.max(a, b) <= ICBetaFactory.UPPER_LIMIT_OF_PARAMETER_AB
                && Math.min(a, b) <= ICBetaFactory.AB_THRESHOLD_FIRST)) {
            throw new AssertionError(
                    String.format(
                            "Bug:パラメータが範囲外もしくは, Math.min(a, b)<=threshold_1stでない:(a,b)=(%s,%s)",
                            a, b));
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
     * x^a y^b / B(a,b) を返す.
     */
    private double coeffToICBeta(double x, double y) {
        return Exponentiation.exp(this.a * Exponentiation.log(x) + this.b * Exponentiation.log(y) - this.lnBetaAB);
    }

}