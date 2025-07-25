package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class DarkOakHoppingPlanter extends AgritechHoppingPlanterBlock{

    public static final MapCodec<DarkOakHoppingPlanter> CODEC = simpleCodec(DarkOakHoppingPlanter::new);

    public DarkOakHoppingPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
