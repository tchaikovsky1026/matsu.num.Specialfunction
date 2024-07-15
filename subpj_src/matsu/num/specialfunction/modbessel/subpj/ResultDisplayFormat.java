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

import java.util.Objects;

import matsu.num.approximation.PolynomialFunction;

/**
 * 近似誤差の可視化と, 近似結果の係数を文字列として出力する,
 * その形式を扱う.
 * 
 * @author Matsuura Y.
 * @version 18.6
 */
public final class ResultDisplayFormat implements DisplayFormat {

    private final CoefficientToString coefficientDisplay;

    /**
     * 係数を文字列に変換する仕組みを与えて, インスタンスを生成する.
     * 
     * @param coefficientDisplay 係数出力時に使用される, 文字列変換の仕組み
     */
    public ResultDisplayFormat(CoefficientToString coefficientDisplay) {
        super();
        this.coefficientDisplay = Objects.requireNonNull(coefficientDisplay);
    }

    @Override
    public String resultToString(
            RawCoefficientCalculableFunction target, PolynomialFunction result) {

        StringBuilder sb = new StringBuilder();

        int resolution = 100;
        double xmin = target.interval().lower();
        double xmax = target.interval().upper();
        double delta = (xmax - xmin) / resolution;

        sb.append("x,u\terror");
        sb.append(System.lineSeparator());
        for (int i = 0; i < resolution / 2; i++) {
            double x = xmin + delta * i;
            sb.append(
                    String.format(
                            "%s\t%s",
                            x,
                            (result.value(x) - target.value(x)) / target.scale(x)));

            sb.append(System.lineSeparator());
        }
        for (int i = resolution / 2; i <= resolution; i++) {
            double x = xmax + delta * (i - resolution);
            sb.append(
                    String.format(
                            "%s\t%s",
                            x,
                            (result.value(x) - target.value(x)) / target.scale(x)));

            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        sb.append(
                this.coefficientDisplay.execute(
                        target.rawCoeff(result.coefficient())));

        return sb.toString();
    }

}
