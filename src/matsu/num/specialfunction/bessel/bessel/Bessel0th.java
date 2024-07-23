/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.bessel;

/**
 * 0次Bessel関数を表す.
 * 
 * @author Matsuura Y.
 * @version 18.9
 */
abstract class Bessel0th extends SkeletalBessel {

    /**
     * 唯一のコンストラクタ.
     */
    Bessel0th() {
        super();
    }

    /**
     * @return 0
     */
    @Override
    public final int order() {
        return 0;
    }
}
