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
 * Decimal128 形式 (おおよそ4倍精度) の体構造.
 * 
 * @author Matsuura Y.
 * @version 18.5
 */
public final class Decimal128Type implements MathFieldType<Decimal128Type> {

    public static final Decimal128Type INSTANCE = new Decimal128Type();

    private Decimal128Type() {

    }

    @Override
    public boolean canBeInterpretedAsDouble() {
        return true;
    }
}
