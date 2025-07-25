package com.blocklogic.agritech.block;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.custom.*;
import com.blocklogic.agritech.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

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


    public static final DeferredBlock<Block> BAMBOO_PLANTER_BLOCK = registerBlock("bamboo_planter",
            () -> new BambooPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> BAMBOO_HOPPING_PLANTER_BLOCK = registerBlock("bamboo_hopping_planter",
            () -> new BambooHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> BIRCH_PLANTER_BLOCK = registerBlock("birch_planter",
            () -> new BirchPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> BIRCH_HOPPING_PLANTER_BLOCK = registerBlock("birch_hopping_planter",
            () -> new BirchHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CHERRY_PLANTER_BLOCK = registerBlock("cherry_planter",
            () -> new CherryPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CHERRY_HOPPING_PLANTER_BLOCK = registerBlock("cherry_hopping_planter",
            () -> new CherryHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CRIMSON_PLANTER_BLOCK = registerBlock("crimson_planter",
            () -> new CrimsonPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> CRIMSON_HOPPING_PLANTER_BLOCK = registerBlock("crimson_hopping_planter",
            () -> new CrimsonHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> DARK_OAK_PLANTER_BLOCK = registerBlock("dark_oak_planter",
            () -> new DarkOakPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> DARK_OAK_HOPPING_PLANTER_BLOCK = registerBlock("dark_oak_hopping_planter",
            () -> new DarkOakHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> JUNGLE_PLANTER_BLOCK = registerBlock("jungle_planter",
            () -> new JunglePlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> JUNGLE_HOPPING_PLANTER_BLOCK = registerBlock("jungle_hopping_planter",
            () -> new JungleHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> MANGROVE_PLANTER_BLOCK = registerBlock("mangrove_planter",
            () -> new MangrovePlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> MANGROVE_HOPPING_PLANTER_BLOCK = registerBlock("mangrove_hopping_planter",
            () -> new MangroveHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> SPRUCE_PLANTER_BLOCK = registerBlock("spruce_planter",
            () -> new SprucePlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> SPRUCE_HOPPING_PLANTER_BLOCK = registerBlock("spruce_hopping_planter",
            () -> new SpruceHoppingPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> WARPED_PLANTER_BLOCK = registerBlock("warped_planter",
            () -> new WarpedPlanter(BlockBehaviour.Properties.of()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .noOcclusion()));

    public static final DeferredBlock<Block> WARPED_HOPPING_PLANTER_BLOCK = registerBlock("warped_hopping_planter",
            () -> new WarpedHoppingPlanter(BlockBehaviour.Properties.of()
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
