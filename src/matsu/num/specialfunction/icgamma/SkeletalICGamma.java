/**
 * 2023.12.6
 */
package matsu.num.specialfunction.icgamma;

import matsu.num.specialfunction.IncompleteGammaFunction;

/**
 * {@linkplain IncompleteGammaFunction} の骨格実装.
 * 
 * <p>
 * この抽象クラスでは,
 * {@linkplain IncompleteGammaFunction} に定義された正則化不完全ガンマ関数の計算を,
 * すべてオッズを経由して計算するように設計されている. <br>
 * 正則化不完全ガンマ関数の値域が0以上の実数 (+ 正負の無限大) に変換し,
 * 計算安定性を向上させている.
 * </p>
 * 
 * <p>
 * さらに, 引数のチェックを個の骨格実装内で行っており,
 * 具体的な計算を行う抽象メソッドには, 正当な引数のみが渡される.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 17.0
 */
abstract class SkeletalICGamma implements IncompleteGammaFunction {

    /**
     * 骨格実装を構築する唯一のコンストラクタ.
     */
    protected SkeletalICGamma() {
        super();
    }

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
     * 正則化不完全ガンマ関数のオッズを計算する抽象メソッド.
     * 
     * <p>
     * この抽象メソッドは内部から呼ばれることを想定している. <br>
     * 内部から呼ばれた場合, 引数には0以上の実数値 (あるいは +inf) であることが保証される. <br>
     * この抽象メソッドは公開すべきでない.
     * </p>
     *
     * @param x x, 0以上であることが保証された実数 (あるいは +inf)
     * @return オッズ(P(x) / Q(x))
     */
    protected abstract double oddsValue(double x);

    /**
     * {@linkplain IncompleteGammaFunction}向けの文字列表現を提供する.
     * 
     * <p>
     * 概ね, 次のような表現であろう.
     * ただし, バージョン間の互換性は担保されていない. <br>
     * {@code ICGamma(%a)}
     * </p>
     * 
     * @return IncompleteGammaFunction向け文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "ICGamma(%s)", this.a());
    }
}
