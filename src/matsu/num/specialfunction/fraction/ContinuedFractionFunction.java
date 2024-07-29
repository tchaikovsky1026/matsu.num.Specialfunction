/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.29
 */
package matsu.num.specialfunction.fraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.IntFunction;

import matsu.num.specialfunction.fraction.MathField.ConstantSupplier;

/**
 * <p>
 * 任意の体構造に対する連分数関数を扱うクラス.
 * </p>
 * 
 * <p>
 * 与える係数を [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
 * ... ,
 * <i>a</i><sub><i>n</i> - 1</sub>]
 * とすると, この連分数関数は, <br>
 * <i>f</i>(<i>t</i>) =
 * (1/1+)
 * (<i>a</i><sub>0</sub><i>t</i>/1+)
 * (<i>a</i><sub>1</sub><i>t</i>/1+)
 * ...
 * (<i>a</i><sub><i>n</i> - 2</sub><i>t</i>/1+)
 * (<i>a</i><sub><i>n</i> - 1</sub><i>t</i>/1)
 * <br>
 * である.
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.1
 * @param <ET> このクラスが扱う体構造の元を表す型パラメータ
 */
public final class ContinuedFractionFunction<ET extends MathField<ET>> {

    private final List<ET> cfCoeff;

    private final ET one;

    //遅延初期化される
    private volatile DoubleContinuedFractionFunction doubleCF;

    //遅延初期化用のロック
    private final Object lock = new Object();

    /**
     * 公開してはいけない.
     * 
     * @param cfCoeff 係数a,
     *            イミュータブルかつ,ラップ元が漏洩していない状態にしなければならない
     */
    private ContinuedFractionFunction(
            ConstantSupplier<ET> constantSupplier, List<ET> cfCoeff) {
        this.cfCoeff = cfCoeff;
        this.one = constantSupplier.one();
    }

    /**
     * 連分数から値を計算する.
     * 
     * @param t t
     * @return 値
     */
    public ET value(ET t) {

        ET one = this.one;

        ET value = one;
        for (ListIterator<ET> ite = this.cfCoeff.listIterator(this.cfCoeff.size());
                ite.hasPrevious();) {
            ET coeff = ite.previous();
            value = coeff.times(t).dividedBy(value);
            value = value.plus(one);
        }
        return one.dividedBy(value);
    }

    /**
     * 連分数の係数
     * [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
     * ... ,
     * <i>a</i><sub><i>n</i> - 1</sub>]
     * を返す. <br>
     * 最初の1は含まない.
     * 
     * @return 係数
     */
    public List<ET> coeffOfContinuedFraction() {
        return new ArrayList<>(this.cfCoeff);
    }

    /**
     * <p>
     * 自身と同等の {@code double} 型連分数関数を返す.
     * </p>
     * 
     * <p>
     * このインスタンスが扱う体構造
     * (クラスの型パラメータの {@link MathField#fieldProperty()})
     * が {@code double} と互換性がない場合,
     * 例外をスローする.
     * </p>
     * 
     * @return {@code double} 型で表現された連分数関数
     * @throws UnsupportedOperationException 体構造が {@code double} と互換性がない場合
     */
    public DoubleContinuedFractionFunction asDoubleFunction() {

        //ダブルチェックイディオム
        DoubleContinuedFractionFunction out = this.doubleCF;
        if (Objects.nonNull(out)) {
            return out;
        }
        synchronized (this.lock) {
            out = this.doubleCF;
            if (Objects.nonNull(out)) {
                return out;
            }
            //ここで例外が発生する可能性がある
            double[] coeff = this.coeffsAsDouble();
            out = new DoubleContinuedFractionFunction(coeff);
            this.doubleCF = out;
            return out;
        }
    }

    /**
     * double表示された連分数の係数を返す.
     * 
     * @throws UnsupportedOperationException doubleと互換性がない場合
     */
    private double[] coeffsAsDouble() {
        double[] out = new double[this.cfCoeff.size()];

        int i = 0;
        for (ET f : this.cfCoeff) {
            out[i] = f.doubleValue();
            i++;
        }
        return out;
    }

    /**
     * べき級数の係数が, <br>
     * [1, c_0, c_0c_1, c_0c_1c_2, ...] <br>
     * であるときの [c_0, c_1, ...] を渡すことで, 等価な連分数表現を計算する仕組みを生成する.
     * 
     * @param <ET> この連分数が扱う体の元を表す
     * @param size サイズ, 0以上でなければならない
     * @param ratioSupplier [c_0, c_1, ...]の生成
     * @param constantSupplier 体構造の定数生成器
     * @return 連分数
     * @throws IllegalArgumentException サイズが負の場合, 展開に失敗した場合
     * @throws NullPointerException nullが含まれる場合
     */
    public static <ET extends MathField<ET>>
            ContinuedFractionFunction<ET> from(
                    int size, IntFunction<ET> ratioSupplier,
                    ConstantSupplier<ET> constantSupplier) {

        return ContinuedFractionFunction.of(
                constantSupplier,
                new CFCalculator<>(size, ratioSupplier, constantSupplier).cfCoeff);
    }

    /**
     * 与えた係数
     * [<i>a</i><sub>0</sub>, <i>a</i><sub>1</sub>,
     * ... ,
     * <i>a</i><sub><i>n</i> - 1</sub>]
     * の連分数を計算する仕組みを生成する.
     * 
     * @param <ET> この連分数が扱う体の元を表す
     * @param constantSupplier 体構造の定数生成器
     * @param cfCoeff 連分数の係数
     * @return 連分数
     * @throws NullPointerException nullが含まれる場合
     */
    public static <ET extends MathField<ET>>
            ContinuedFractionFunction<ET> of(
                    ConstantSupplier<ET> constantSupplier, List<ET> cfCoeff) {

        List<ET> list = new ArrayList<>(cfCoeff);
        if (list.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("リストにnullが含まれる");
        }

        return new ContinuedFractionFunction<ET>(
                Objects.requireNonNull(constantSupplier),
                Collections.unmodifiableList(list));
    }

    private static final class CFCalculator<ET extends MathField<ET>> {

        private final int size;
        private final IntFunction<ET> ratioSupplier;
        private final MathField.ConstantSupplier<ET> constantSupplier;

        final List<ET> cfCoeff;

        /**
         * べき級数の係数が, <br>
         * [1, c_0, c_0c_1, c_0c_1c_2, ...] <br>
         * であるときの [c_0, c_1, ...] を渡すことで, 等価な連分数表現を計算する仕組みを生成する.
         * 
         * @param size
         * @param fieldType 体構造
         * @param ratioSupplier [c_0, c_1, ...]の生成
         * @param constantSupplier 定数生成器
         * @throws IllegalArgumentException サイズが負の場合, 展開に失敗した場合
         * @throws NullPointerException null
         */
        CFCalculator(int size,
                IntFunction<ET> ratioSupplier, ConstantSupplier<ET> constantSupplier) {
            if (size < 0) {
                throw new IllegalArgumentException("サイズが負である");
            }
            this.size = size;
            this.ratioSupplier = Objects.requireNonNull(ratioSupplier);
            this.constantSupplier = Objects.requireNonNull(constantSupplier);

            try {
                this.cfCoeff = calcCoeffOfContinuedFraction();
            } catch (ArithmeticException nfe) {
                throw new IllegalArgumentException("連分数展開が破綻する");
            }
        }

        private List<ET> calcCoeffOfContinuedFraction() {
            if (this.size == 0) {
                return Collections.emptyList();
            }

            final ET zero = this.constantSupplier.zero();

            //商差法の結果
            List<ET> out = new ArrayList<>();

            //e0
            List<ET> e0 = new ArrayList<>();
            for (int j = 0; j < this.size; j++) {
                e0.add(zero);
            }

            //q0
            List<ET> q0 = new ArrayList<>();
            for (int j = 0; j < this.size; j++) {
                q0.add(this.ratioSupplier.apply(j));
            }
            out.add(q0.get(0).negated());

            int step = 1;
            while (step < this.size) {
                //q -> e
                List<ET> e = new ArrayList<>();
                for (int j = 0; j < this.size - step; j++) {
                    e.add(e0.get(j + 1).plus(q0.get(j + 1).minus(q0.get(j))));
                }
                out.add(e.get(0).negated());
                e0 = e;
                step++;

                //e -> q
                //もしq->eが最後なら, このループは回らないため問題ない
                List<ET> q = new ArrayList<>();
                for (int j = 0; j < size - step; j++) {
                    q.add(q0.get(j + 1).times(e0.get(j + 1).dividedBy(e0.get(j))));
                }
                if (!q.isEmpty()) {
                    out.add(q.get(0).negated());
                }
                q0 = q;
                step++;
            }

            return out;
        }
    }
}
