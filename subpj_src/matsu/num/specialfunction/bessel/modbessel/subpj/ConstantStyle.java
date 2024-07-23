package matsu.num.specialfunction.bessel.modbessel.subpj;

import java.util.Objects;

/**
 * <p>
 * 係数を定数型の文字列に変換する. <br>
 * ソースに直接埋め込むことができる文字列となる.
 * </p>
 * 
 * <p>
 * 次のようなフォーマットである. <br>
 * {@code final double prefix0 = 0;} <br>
 * {@code final double prefix1 = 0;} <br>
 * {@code final double prefix2 = 0;} <br>
 * ...
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class ConstantStyle implements CoefficientToString {

    private final String prefix;

    /**
     * prefix を与えてフォーマットを生成する.
     * 
     * @param prefix prefix
     */
    public ConstantStyle(String prefix) {
        this.prefix = Objects.requireNonNull(prefix);
    }

    @Override
    public String execute(double[] coeff) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coeff.length; i++) {
            sb.append("final double ");
            sb.append(prefix);
            sb.append(i);
            sb.append(" = ");
            sb.append(coeff[i]);
            sb.append(";");

            if (i < coeff.length - 1) {
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

}
