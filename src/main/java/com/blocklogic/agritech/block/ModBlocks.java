package com.blocklogic.agritech.block;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.custom.AcaciaHoppingPlanter;
import com.blocklogic.agritech.block.custom.AcaciaPlanter;
import com.blocklogic.agritech.block.custom.AgritechHoppingPlanterBlock;
import com.blocklogic.agritech.block.custom.AgritechPlanterBlock;
import com.blocklogic.agritech.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AgriTech.MODID);

    public static final DeferredBlock<Block> AGRITECH_PLANTER_BLOCK = registerBlock("agritech_planter_block",
            () -> new AgritechPlanterBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> AGRITECH_HOPPING_PLANTER_BLOCK = registerBlock("agritech_hopping_planter_block",
            () -> new AgritechHoppingPlanterBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    //WOODS
    public static final DeferredBlock<Block> ACACIA_PLANTER_BLOCK = registerBlock("acacia_planter",
            () -> new AcaciaPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> ACACIA_HOPPING_PLANTER_BLOCK = registerBlock("acacia_hopping_planter",
            () -> new AcaciaHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    private static <T extends Block>DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
