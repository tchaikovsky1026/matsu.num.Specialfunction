package matsu.num.specialfunction.subpj;

import matsu.num.approximation.ApproxResult;
import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.polynomial.MinimaxPolynomialApproxExecutor;
import matsu.num.approximation.polynomial.Polynomial;

/**
 * 近似の実行を扱う.
 * 
 * @author Matsuura Y.
 */
public final class EachApproxExecutor {

    private final MinimaxPolynomialApproxExecutor executor;

    /**
     * 
     * @param order 近似多項式の次数
     * @throws IllegalArgumentException 次数が不適の場合
     */
    public EachApproxExecutor(int order) {
        this.executor = MinimaxPolynomialApproxExecutor.of(order);
    }

    public <T extends PseudoRealNumber<T>> void execute(RawCoeffCalculableFunction<T> target) {

        System.out.println(target.interval());
        System.out.println();

        ApproxResult<Polynomial<T>> approx = executor.apply(target);
        Polynomial<T> resultPolynomial = approx
                .orElseThrow(() -> new RuntimeException(approx.message()));

        System.out.println(
                new ResultDisplayFormat(new ConstantStyle("C"))
                        .resultToString(target, resultPolynomial));

        System.out.println();
        System.out.println();
    }
}
