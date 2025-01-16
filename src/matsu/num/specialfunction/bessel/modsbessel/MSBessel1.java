/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modsbessel;

/**
 * 1次変形球Bessel関数の最低限の実装.
 * 
 * @author Matsuura Y.
 */
abstract class MSBessel1 extends SkeletalMSBessel {

    /**
     * 唯一のコンストラクタ.
     */
    protected MSBessel1() {
        super();
    }

    @Override
    public final int order() {
        return 1;
    }
}
