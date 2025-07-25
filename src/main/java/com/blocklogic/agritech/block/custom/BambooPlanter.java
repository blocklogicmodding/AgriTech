package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class BambooPlanter extends AgritechPlanterBlock{
    public static final MapCodec<BambooPlanter> CODEC = simpleCodec(BambooPlanter::new);

    public BambooPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
