/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modbessel;

/**
 * 1次の変形ベッセル関数を表現する.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class ModifiedBessel1stOrder extends SkeletalModifiedBessel {

    ModifiedBessel1stOrder() {
        super();
    }

    @Override
    public final int order() {
        return 1;
    }
}
