package matsu.num.specialfunction.subpj;

import java.util.Objects;

import matsu.num.approximation.FiniteClosedInterval;
import matsu.num.approximation.PseudoRealNumber;

/**
 * 一般体の閉区間を作成するファクトリクラス.
 * 
 * @author Matsuura Y.
 * @param <T> 体
 */
public final class FiniteClosedIntervalFactory<T extends PseudoRealNumber<T>> {

    private final PseudoRealNumber.Provider<T> provider;

    /**
     * 唯一のコンストラクタ.
     * 
     * @param provider Tのプロバイダ
     */
    public FiniteClosedIntervalFactory(PseudoRealNumber.Provider<T> provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    /**
     * 区間を作成する.
     * ([x1, x2] または [x2, x1])
     * 
     * @param x1 x1
     * @param x2 x2
     * @return [x1, x2] または [x2, x1]
     * @throws IllegalArgumentException 値が不適当な場合
     */
    public FiniteClosedInterval<T> createInterval(double x1, double x2) {
        return FiniteClosedInterval.from(
                this.provider.fromDoubleValue(x1),
                this.provider.fromDoubleValue(x2));
    }

}
