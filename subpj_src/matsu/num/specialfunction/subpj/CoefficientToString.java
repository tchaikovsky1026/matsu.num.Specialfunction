package matsu.num.specialfunction.subpj;

/**
 * {@code double} 型の配列として表された係数を文字列に変換する仕組みを扱う.
 * 
 * @author Matsuura Y.
 */
@FunctionalInterface
public interface CoefficientToString {

    /**
     * 係数を文字列に変換する.
     * 
     * @param coeff 係数
     * @return 文字列
     */
    public abstract String execute(double[] coeff);
}
