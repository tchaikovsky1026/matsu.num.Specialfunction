package matsu.num.specialfunction.bessel.modbessel.subpj;

import matsu.num.approximation.TargetFunction;

/**
 * 近似ターゲット関数の近似多項式を, 元々扱いたい関数の係数へと変換する機能を持たせた,
 * {@link TargetFunction} の拡張.
 * 
 * @author Matsuura Y.
 */
public interface RawCoefficientCalculableFunction extends TargetFunction {

    /**
     * 元々扱いたい係数を返す.
     * 
     * @param thisCoeff このターゲットファンクションにおける係数
     * @return 元々知りたかった係数
     */
    public abstract double[] rawCoeff(double[] thisCoeff);
}
