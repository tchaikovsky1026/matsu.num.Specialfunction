/**
 * 2023.3.22
 */
package matsu.num.specialfunction.icbeta;

import matsu.num.specialfunction.IncompleteBetaFunction;

/**
 * {@link IncompleteBetaFunction}の骨格実装.
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
abstract class SkeletalICBeta implements IncompleteBetaFunction {

    @Override
    public final double ribeta(double x) {
        double oddsX = x == 1 ? Double.POSITIVE_INFINITY : x / (1 - x);
        double odds = this.ribetaOdds(oddsX);
        return 1 / (1 + 1 / odds);
    }

    @Override
    public final double ribetaR(double x) {
        double oddsInvX = (1 - x) / x;
        double odds = this.ribetaOdds(oddsInvX);
        return 1 / (1 + odds);
    }

    @Override
    public final double ribetaOdds(double oddsX) {
        if (!(oddsX >= 0)) {
            return Double.NaN;
        }
        return this.oddsValue(oddsX);
    }

    /**
     * メインロジック. <br>
     * メインルーチンの正則化不完全ベータ関数の計算は全て、オッズ(lcp/upc)を経由して計算することとする.
     *
     * @param oddsX odds of x: o = x/(1-x)
     * @return I(a,b,o/(1+o))/I(b,a,1/(1+o)) = I(a,b,x)/(1 - I(a,b,x))
     */
    protected abstract double oddsValue(double oddsX);

}
