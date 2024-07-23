/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.12
 */
package matsu.num.specialfunction.fraction;

/**
 * {@code long} 型の最大公約数を計算するユーティリティ
 * (非公開).
 * 
 * @author Matsuura Y.
 * @version 18.5
 * @deprecated このモジュールで取り扱う有理数はlong型では十分でないので,
 *                 このクラスは不使用である.
 */
@Deprecated
public final class LongGCD {

    private LongGCD() {
        //インスタンス化不可
        throw new AssertionError();
    }

    /**
     * 与えれた引数の正の最大公約数を返す. <br>
     * (0,0)の場合は0を返す.
     * 
     * @param v1 値1
     * @param v2 値2
     * @return gcd(abs(v1),abs(v2))
     */
    public static long gcd(long v1, long v2) {
        if (v1 == 0) {
            return Math.abs(v2);
        }
        if (v2 == 0) {
            return Math.abs(v1);
        }
        if (v1 == Long.MIN_VALUE || v2 == Long.MIN_VALUE) {
            return gcd_longMinValue(v1, v2);
        }

        v1 = Math.abs(v1);
        v2 = Math.abs(v2);
        //v1の方が大きくなるようにスイッチ
        if (v1 < v2) {
            long temp = v1;
            v1 = v2;
            v2 = temp;
        }
        while (true) {
            long r = v1 % v2;
            if (r == 0) {
                return v2;
            }
            v1 = v2;
            v2 = r;
        }
    }

    /**
     * 両方とも0でなく, どちらかに{@link Long#MIN_VALUE}が含まれるときの, 別処理.
     */
    private static long gcd_longMinValue(long v1, long v2) {
        if (v1 == Long.MIN_VALUE && v2 == Long.MIN_VALUE) {
            return v1;
        }

        //vはMIN_VALUEでないほうの値の絶対値
        long v = v1 != Long.MIN_VALUE
                ? Math.abs(v1)
                : Math.abs(v2);

        //MIN_VALUEは-2^{63}なので, 
        //最大公約数はvに含まれる2の数の分になる
        return Long.lowestOneBit(v);
    }
}
