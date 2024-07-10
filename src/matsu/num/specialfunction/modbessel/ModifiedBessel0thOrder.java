/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.9
 */
package matsu.num.specialfunction.modbessel;

/**
 * 0次の変形ベッセル関数を表現する.
 * 
 * @author Matsuura Y.
 * @version 18.2
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
