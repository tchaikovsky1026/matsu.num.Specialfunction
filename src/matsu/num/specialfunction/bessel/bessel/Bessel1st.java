/*
 * Copyright © 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.22
 */
package matsu.num.specialfunction.bessel.bessel;

/**
 * 1次Bessel関数を表す.
 * 
 * @author Matsuura Y.
 */
abstract class Bessel1st extends SkeletalBessel {

    /**
     * 唯一のコンストラクタ.
     */
    Bessel1st() {
        super();
    }

    /**
     * @return 1
     */
    @Override
    public final int order() {
        return 1;
    }
}
