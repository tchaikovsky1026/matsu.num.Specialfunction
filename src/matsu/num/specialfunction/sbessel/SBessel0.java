/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.16
 */
package matsu.num.specialfunction.sbessel;

/**
 * 0次球Bessel関数の最低限の実装.
 * 
 * @author Matsuura Y.
 * @version 18.7
 */
abstract class SBessel0 extends SkeletalSBessel {

    /**
     * 唯一のコンストラクタ.
     */
    protected SBessel0() {
        super();
    }

    @Override
    public final int order() {
        return 0;
    }
}
