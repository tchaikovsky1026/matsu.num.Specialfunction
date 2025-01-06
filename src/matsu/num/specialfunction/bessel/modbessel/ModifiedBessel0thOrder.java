/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.modbessel;

/**
 * 0次の変形ベッセル関数を表現する.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class ModifiedBessel0thOrder extends SkeletalModifiedBessel {

    ModifiedBessel0thOrder() {
        super();
    }

    @Override
    public final int order() {
        return 0;
    }
}
