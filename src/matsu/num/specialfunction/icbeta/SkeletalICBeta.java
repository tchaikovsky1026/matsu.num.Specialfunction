/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.10.23
 */
package matsu.num.specialfunction.icbeta;

/**
 * {@link IncompleteBetaFunction}の骨格実装.
 * 
 * <p>
 * この抽象クラスでは,
 * {@linkplain IncompleteBetaFunction} に定義された正則化不完全ベータ関数の計算を,
 * すべてオッズを経由して計算するように設計されている. <br>
 * 正則化不完全ベータ関数の定義域, 値域が0以上の実数 (+ 正負の無限大) に変換し,
 * 計算安定性を向上させている.
 * </p>
 * 
 * <p>
 * さらに, 引数のチェックを個の骨格実装内で行っており,
 * 具体的な計算を行う抽象メソッドには, 正当な引数のみが渡される.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 20.0
 */
abstract class SkeletalICBeta implements IncompleteBetaFunction {

    /**
     * 骨格実装を構築する唯一のコンストラクタ.
     */
    protected SkeletalICBeta() {
        super();
    }

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
     * 引数として x のオッズを与えて,
     * 正則化不完全ベータ関数のオッズを計算する抽象メソッド.
     * 
     * <p>
     * この抽象メソッドは内部から呼ばれることを想定している. <br>
     * 内部から呼ばれた場合, 引数には0以上の実数値 (あるいは +inf) であることが保証される. <br>
     * この抽象メソッドは公開すべきでない.
     * </p>
     *
     * @param oddsX o = x / (1-x),
     *            0以上であることが保証された実数 (あるいは +inf)
     * @return オッズ I(a, b, o/(1+o)) / I(b,a,1/(1+o)) =
     *             I(a, b, x) / (1 - I(a, b, x))
     */
    protected abstract double oddsValue(double oddsX);

    /**
     * {@linkplain IncompleteBetaFunction}向けの文字列表現を提供する.
     * 
     * <p>
     * 概ね, 次のような表現であろう.
     * ただし, バージョン間の互換性は担保されていない. <br>
     * {@code ICBeta(%a,%b)}
     * </p>
     * 
     * @return IncompleteBetaFunction向け文字列表現
     */
    @Override
    public String toString() {
        return String.format(
                "ICBeta(%s, %s)", this.a(), this.b());
    }
}
