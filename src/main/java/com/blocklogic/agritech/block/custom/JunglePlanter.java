package com.blocklogic.agritech.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BaseEntityBlock;

public class JunglePlanter extends AgritechPlanterBlock{
    public static final MapCodec<JunglePlanter> CODEC = simpleCodec(JunglePlanter::new);

    public JunglePlanter(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
