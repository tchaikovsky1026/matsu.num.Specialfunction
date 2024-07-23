package matsu.num.specialfunction.bessel.modbessel.subpj.simulation;

import java.util.ArrayList;
import java.util.List;

import matsu.num.specialfunction.ModifiedBesselFunction;
import matsu.num.specialfunction.fraction.DoubleContinuedFractionFunction;

/**
 * <p>
 * I(x)に含まれるexp(-x)の項を抜き出すシミュレーション. <br>
 * residual = Ic(x) - [Icの漸近展開を連分数に変換したもの] <br>
 * を評価する.
 * </p>
 * 
 * <p>
 * 所感: <br>
 * [Icの漸近展開を連分数に変換したもの] は連分数が収束しないようである.
 * </p>
 * 
 * @author Matsuura Y.
 */
final class FactorCoeffCalculation {

    public static void main(String[] args) {
        int order = 1;
        int kMax = 60;
        double minX = 2d;
        double maxX = 5d;
        double[][] data = exe(order, minX, maxX, kMax);

        System.out.printf("order=%s\n", order);
        System.out.println();
        System.out.println("x\tresidual of Ic");
        for (double[] result : data) {
            System.out.printf("%s\t%s\n", result[0], result[1]);
        }
    }

    private static double[][] exe(int order, double minX, double maxX, int kMax) {

        ModifiedBesselFunction mbessel = ModifiedBesselFunction.instanceOf(order);
        DoubleContinuedFractionFunction asymtotic =
                FractionFunctionForAsymtotic.from(order, 1, kMax).asDoubleFunction();

        List<double[]> list = new ArrayList<>();
        double deltaX = Math.abs(maxX - minX) / 100;

        for (double x = minX; x < maxX; x += deltaX) {
            double value = mbessel.besselIc(x)
                    - asymtotic.value(1 / (8 * x)) / Math.sqrt(x * 2 * Math.PI);
            list.add(new double[] { x, value });
        }

        return list.toArray(new double[][] {});
    }
}
