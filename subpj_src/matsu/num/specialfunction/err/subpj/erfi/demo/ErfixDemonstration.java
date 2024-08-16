package matsu.num.specialfunction.err.subpj.erfi.demo;

import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;

/**
 * erfix(x) の計算に関する試し
 * 
 * @author Matsuura Y.
 */
final class ErfixDemonstration {

    private final List<Erfix> list;

    public static void main(String[] args) {

        final double xmin = 0d;
        final double xmax = 25d;
        final double delta = 0.25d;

        final int kMax = 1000;

        new ErfixDemonstration(new ErfixTaylor(kMax), new ErfixFraction(kMax))
                .execute(() -> DoubleStream.iterate(xmin, x -> x <= xmax, x -> x + delta));
    }

    public ErfixDemonstration(Erfix... erfixs) {
        this.list = Arrays.asList(erfixs);
    }

    public void execute(Supplier<DoubleStream> xs) {

        System.out.print("x\t");
        for (Erfix erfix : list) {
            System.out.print(erfix);
            System.out.print("\t");
        }
        System.out.println();

        for (PrimitiveIterator.OfDouble ite = xs.get().limit(1_000_000).iterator();
                ite.hasNext();) {
            final double x = ite.nextDouble();

            System.out.print(x);
            System.out.print("\t");
            for (Erfix erfix : list) {
                System.out.print(erfix.erfix(x));
                System.out.print("\t");
            }
            System.out.println();
        }
    }

}
