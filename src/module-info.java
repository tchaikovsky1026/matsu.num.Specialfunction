/**
 * 特殊関数を扱うモジュール.
 * 
 * <p>
 * <i>必須モジュール:</i> <br>
 * {@code matsu.num.Commons}
 * </p>
 * 
 * @author Matsuura Y.
 * @version 19.2
 */
module matsu.num.Specialfunction {
    requires matsu.num.Commons;

    exports matsu.num.specialfunction;
    exports matsu.num.specialfunction.bessel;
}
