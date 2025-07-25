package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class WarpedHoppingPlanter extends AgritechHoppingPlanterBlock{

    public static final MapCodec<WarpedHoppingPlanter> CODEC = simpleCodec(WarpedHoppingPlanter::new);

    public WarpedHoppingPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
