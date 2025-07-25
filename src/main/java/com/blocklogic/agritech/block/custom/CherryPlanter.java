package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class CherryPlanter extends AgritechPlanterBlock{
    public static final MapCodec<CherryPlanter> CODEC = simpleCodec(CherryPlanter::new);

    public CherryPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
