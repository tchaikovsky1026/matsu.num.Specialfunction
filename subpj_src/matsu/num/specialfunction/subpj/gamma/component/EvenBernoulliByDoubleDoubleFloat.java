/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package matsu.num.specialfunction.subpj.gamma.component;

import java.util.Arrays;

import matsu.num.specialfunction.subpj.DoubleDoubleFloatElement;

/**
 * 偶数次のBernoulli数 <i>B</i><sub>2<i>m</i></sub> を扱う.
 * 
 * <p>
 * <i>m</i> = 30 (<i>B</i><sub>60</sub>) まで. <br>
 * (バージョンアップ時にドキュメントが更新されていないかもしれない)
 * </p>
 * 
 * @author Matsuura Y.
 */
public final class EvenBernoulliByDoubleDoubleFloat {

    /**
     * 偶数indexのBernoulli数. <br>
     * [
     * B<sub>0</sub>, B<sub>2</sub>, B<sub>4</sub>, ... ,
     * B<sub>60</sub>
     * ]
     */
    private static final String[] BERNOULLI_STRING = {
            "1",
            "0.16666666666666666666666666666666666667",
            "-0.033333333333333333333333333333333333333",
            "0.023809523809523809523809523809523809524",
            "-0.033333333333333333333333333333333333333",
            "0.075757575757575757575757575757575757576",
            "-0.25311355311355311355311355311355311355",
            "1.1666666666666666666666666666666666667",
            "-7.0921568627450980392156862745098039216",
            "54.971177944862155388471177944862155388",
            "-529.12424242424242424242424242424242424",
            "6192.1231884057971014492753623188405797",
            "-86580.253113553113553113553113553113553",
            "1425517.1666666666666666666666666666667",
            "-27298231.067816091954022988505747126437",
            "601580873.90064236838430386817483591677",
            "-15116315767.092156862745098039215686275",
            "429614643061.16666666666666666666666667",
            "-13711655205088.332772159087948561632772",
            "488332318973593.16666666666666666666667",
            "-19296579341940068.148632668144863266815",
            "841693047573682615.00055370985603543743",
            "-40338071854059455413.076811594202898551",
            "2115074863808199160560.1453900709219858",
            "-120866265222965259346027.31193708252532",
            "7500866746076964366855720.0757575757576",
            "-503877810148106891413789303.05220125786",
            "36528776484818123335110430842.971177945",
            "-2849876930245088222626914643291.0678161",
            "238654274996836276446459819192192.14972",
            "-21399949257225333665810744765191097.393"
    };

    /**
     * 偶数indexのBernoulli数. <br>
     * [
     * B<sub>0</sub>, B<sub>2</sub>, B<sub>4</sub>, ... ,
     * B<sub>60</sub>
     * ]
     */
    private static final DoubleDoubleFloatElement[] BERNOULLI;

    /**
     * サポートしているmの最小値.
     */
    public static final int MIN_M = 0;

    /**
     * サポートしているmの最大値.
     */
    public static final int MAX_M = BERNOULLI_STRING.length - 1;

    static {
        BERNOULLI = new DoubleDoubleFloatElement[BERNOULLI_STRING.length];

        for (int i = 0; i < BERNOULLI.length; i++) {
            BERNOULLI[i] = DoubleDoubleFloatElement.fromDecimalExpression(BERNOULLI_STRING[i]);
        }
    }

    /**
     * mがサポートされているかを判定する.
     * 
     * @param m m
     * @return サポートされているならtrue
     */
    public static boolean accepts(int m) {
        return m >= MIN_M && m <= MAX_M;
    }

    /**
     * <i>m</i> を与え, 偶数次のBernoulli数 <i>B</i><sub>2<i>m</i></sub> を返す.
     * 
     * @param m <i>m</i>
     * @return <i>B</i><sub>2<i>m</i></sub>
     * @throws IllegalArgumentException <i>m</i> がサポート外
     */
    public static DoubleDoubleFloatElement evenBernoulli(int m) {
        if (!accepts(m)) {
            throw new IllegalArgumentException("サポート外のm");
        }
        return BERNOULLI[m];
    }

    /**
     * ベルヌーイ数が計算できているかのテスト
     * 
     * @param args
     */
    public static void main(String[] args) {
        Arrays.stream(BERNOULLI).forEach(System.out::println);
    }
}
