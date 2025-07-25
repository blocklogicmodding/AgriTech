package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class AcaciaHoppingPlanter extends AgritechHoppingPlanterBlock{

    public static final MapCodec<AcaciaHoppingPlanter> CODEC = simpleCodec(AcaciaHoppingPlanter::new);

    public AcaciaHoppingPlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
