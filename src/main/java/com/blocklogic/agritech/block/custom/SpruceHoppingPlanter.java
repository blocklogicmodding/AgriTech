package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class SpruceHoppingPlanter extends AgritechHoppingPlanterBlock{

    public static final MapCodec<SpruceHoppingPlanter> CODEC = simpleCodec(SpruceHoppingPlanter::new);

    public SpruceHoppingPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
