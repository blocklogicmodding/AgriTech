package com.blocklogic.agritech.block;

import com.blocklogic.agritech.AgriTech;
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

    public static final DeferredBlock<Block> AGRITECH_SIMPLE_MULCH = registerBlock("agritech_simple_mulch",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.ROOTED_DIRT)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_simple_mulch"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_FERTILE_MULCH = registerBlock("agritech_fertile_mulch",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.ROOTED_DIRT)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_fertile_mulch"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_PREMIUM_MULCH = registerBlock("agritech_premium_mulch",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.ROOTED_DIRT)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_premium_mulch"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_PRISTINE_MULCH = registerBlock("agritech_pristine_mulch",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2f)
                    .sound(SoundType.ROOTED_DIRT)) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_pristine_mulch"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_PLANTER_BLOCK = registerBlock("agritech_planter_block",
            () -> new AgritechPlanterBlock(BlockBehaviour.Properties.of()
                    .strength(3f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_planter_block"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_HOPPING_PLANTER_BLOCK = registerBlock("agritech_hopping_planter_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_planter_block"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredBlock<Block> AGRITECH_FERTILIZER_MAKER_BLOCK = registerBlock("agritech_fertilizer_maker_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(3f)
                    .sound(SoundType.WOOD)
                    .noOcclusion()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_fertilizer_maker_block"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

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
