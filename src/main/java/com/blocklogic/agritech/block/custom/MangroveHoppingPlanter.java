
package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class MangroveHoppingPlanter extends AgritechHoppingPlanterBlock{

    public static final MapCodec<MangroveHoppingPlanter> CODEC = simpleCodec(MangroveHoppingPlanter::new);

    public MangroveHoppingPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
