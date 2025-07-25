package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class WarpedPlanter extends AgritechPlanterBlock{
    public static final MapCodec<WarpedPlanter> CODEC = simpleCodec(WarpedPlanter::new);

    public WarpedPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
