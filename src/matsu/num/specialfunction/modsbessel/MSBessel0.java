/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.17
 */
package matsu.num.specialfunction.modsbessel;

/**
 * 0次変形球Bessel関数の最低限の実装.
 * 
 * @author Matsuura Y.
 * @version 18.8
 */
abstract class MSBessel0 extends SkeletalMSBessel {

    /**
     * 唯一のコンストラクタ.
     */
    protected MSBessel0() {
        super();
    }

    @Override
    public final int order() {
        return 0;
    }
}
