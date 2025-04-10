package com.blocklogic.agritech.item;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AgriTech.MODID);

    public static final Supplier<CreativeModeTab> AGRITECH = CREATIVE_MODE_TAB.register("agritech",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.AGRITECH_SIMPLE_MULCH.get()))
                    .title(Component.translatable("creativetab.agritech.agritech"))
                    .displayItems((ItemDisplayParameters, output) -> {
                        output.accept(ModBlocks.AGRITECH_SIMPLE_MULCH);
                        output.accept(ModBlocks.AGRITECH_FERTILE_MULCH);
                        output.accept(ModBlocks.AGRITECH_PREMIUM_MULCH);
                        output.accept(ModBlocks.AGRITECH_PRISTINE_MULCH);
                        output.accept(ModBlocks.AGRITECH_FERTILIZER_MAKER_BLOCK);

                        output.accept(ModBlocks.AGRITECH_PLANTER_BLOCK);
                        output.accept(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK);

                        output.accept(ModItems.AGRITECH_PREMIUM_FERTILIZER);
                        output.accept(ModItems.AGRITECH_PRISTINE_FERTILIZER);
                    })
                    .build());

    public static void register (IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
