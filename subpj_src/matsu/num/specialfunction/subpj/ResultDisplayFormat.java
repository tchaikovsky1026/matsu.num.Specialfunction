package matsu.num.specialfunction.subpj;

import java.util.Objects;

import matsu.num.approximation.PseudoRealNumber;
import matsu.num.approximation.polynomial.DoublePolynomial;
import matsu.num.approximation.polynomial.Polynomial;

/**
 * 近似誤差の可視化と, 近似結果の係数を文字列として出力する,
 * その形式を扱う.
 * 
 * @author Matsuura Y.
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
            RawCoeffCalculableDoubleFunction target, DoublePolynomial result) {

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

    @Override
    public <T extends PseudoRealNumber<T>> String resultToString(
            RawCoeffCalculableFunction<T> target, Polynomial<T> result) {

        StringBuilder sb = new StringBuilder();

        int resolution = 100;
        T xmin = target.interval().lower();
        T xmax = target.interval().upper();
        T delta = xmax.minus(xmin).dividedBy(resolution);

        sb.append("x,u\terror");
        sb.append(System.lineSeparator());
        for (int i = 0; i < resolution / 2; i++) {
            T x = xmin.plus(delta.times(i));
            sb.append(
                    String.format(
                            "%s\t%s",
                            x,
                            result.value(x).minus(target.value(x)).dividedBy(target.scale(x))));

            sb.append(System.lineSeparator());
        }
        for (int i = resolution / 2; i <= resolution; i++) {
            T x = xmax.plus(delta.times(i - resolution));
            sb.append(
                    String.format(
                            "%s\t%s",
                            x,
                            result.value(x).minus(target.value(x)).dividedBy(target.scale(x))));

            sb.append(System.lineSeparator());
        }

        sb.append(System.lineSeparator());

        sb.append(
                this.coefficientDisplay.execute(
                        target.rawCoeff(result.coefficient())));

        return sb.toString();
    }
}
