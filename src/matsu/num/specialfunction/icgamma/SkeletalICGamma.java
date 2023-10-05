/**
 * 2023.3.21
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@link IncompleteGammaFunction}インターフェースの骨格実装.
 * 
 * @author Matsuura Y.
 * @version 11.0
 */
abstract class SkeletalICGamma implements IncompleteGammaFunction {

    @Override
    public final double rigammaP(double x) {
        return 1 / (1 + (1 / this.rigammaOdds(x)));
    }

    @Override
    public final double rigammaQ(double x) {
        return 1 / (1 + this.rigammaOdds(x));
    }

    @Override
    public final double rigammaOdds(double x) {
        if (!(x >= 0)) {
            return Double.NaN;
        }
        return this.oddsValue(x);
    }

    /**
     *
     * メインロジック. <br>
     * メインルーチンの正則化不完全ガンマ関数の計算は全て、オッズ値(lcp/upc)を経由して計算することとする
     *
     * @param x x, 引数
     * @return オッズ
     */
    protected abstract double oddsValue(double x);
}
