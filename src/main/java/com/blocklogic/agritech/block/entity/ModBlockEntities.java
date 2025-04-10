package com.blocklogic.agritech.block.entity;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AgriTech.MODID);

    public static final Supplier<BlockEntityType<AgriTechPlanterEntity>> AGRITECH_PLANTER_BE =
            BLOCK_ENTITIES.register("agritech_planter_be", () -> BlockEntityType.Builder.of(
               AgriTechPlanterEntity::new, ModBlocks.AGRITECH_PLANTER_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
