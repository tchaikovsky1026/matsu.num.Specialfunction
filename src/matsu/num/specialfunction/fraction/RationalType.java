/*
 * Copyright (c) 2024 Matsuura Y.
 * 
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
/*
 * 2024.7.11
 */
package matsu.num.specialfunction.fraction;

/**
 * 有理数構造.
 * 
 * @author Matsuura Y.
 * @version 18.5
 */
public final class RationalType implements MathFieldType<RationalType> {

    public static final RationalType INSTANCE = new RationalType();

    private RationalType() {

    }

    @Override
    public boolean canBeInterpretedAsDouble() {
        return true;
    }

}
