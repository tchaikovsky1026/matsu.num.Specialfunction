/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.15
 */
package matsu.num.specialfunction.modbessel.subpj;

/**
 * <p>
 * 係数を配列型の文字列に変換する. <br>
 * ソースに直接埋め込むことができる文字列となる.
 * </p>
 * 
 * <p>
 * 次のようなフォーマットである. <br>
 * { <br>
 * {@code 0,} <br>
 * {@code 0,} <br>
 * ... <br>
 * }
 * </p>
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
public final class ArrayStyle implements CoefficientToString {

    public static final ArrayStyle INSTANCE = new ArrayStyle();

    private ArrayStyle() {
    }

    @Override
    public String execute(double[] coeff) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(System.lineSeparator());

        for (int i = 0; i < coeff.length; i++) {
            sb.append(coeff[i]);
            if (i < coeff.length - 1) {
                sb.append(",");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("}");

        return sb.toString();
    }

}
