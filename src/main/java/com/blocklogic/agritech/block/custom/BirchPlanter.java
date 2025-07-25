package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BirchPlanter extends AgritechPlanterBlock{
    public static final MapCodec<BirchPlanter> CODEC = simpleCodec(BirchPlanter::new);

    public BirchPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
