package com.blocklogic.agritech.block.custom;

import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.block.entity.ModBlockEntities;
import com.blocklogic.agritech.screen.custom.AgritechPlanterMenu;
import com.blocklogic.agritech.util.RegistryHelper;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AgritechHoppingPlanterBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.1, 15.0, 11.0, 15.0);
    public static final MapCodec<AgritechHoppingPlanterBlock> CODEC = simpleCodec(AgritechHoppingPlanterBlock::new);

    public AgritechHoppingPlanterBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AgritechPlanterBlockEntity(pos, state);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity planter) {
                planter.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity agritechPlanterBlockEntity) {
            // Check if the player is holding essence
            String heldItemId = RegistryHelper.getItemId(stack);

            // Map essence types to their farmland versions
            Map<String, String> essenceToFarmland = new HashMap<>();
            essenceToFarmland.put("mysticalagriculture:inferium_essence", "mysticalagriculture:inferium_farmland");
            essenceToFarmland.put("mysticalagriculture:prudentium_essence", "mysticalagriculture:prudentium_farmland");
            essenceToFarmland.put("mysticalagriculture:tertium_essence", "mysticalagriculture:tertium_farmland");
            essenceToFarmland.put("mysticalagriculture:imperium_essence", "mysticalagriculture:imperium_farmland");
            essenceToFarmland.put("mysticalagriculture:supremium_essence", "mysticalagriculture:supremium_farmland");
            essenceToFarmland.put("mysticalagradditions:insanium_essence", "mysticalagradditions:insanium_farmland");

            if (essenceToFarmland.containsKey(heldItemId)) {
                ItemStack soilStack = agritechPlanterBlockEntity.inventory.getStackInSlot(1);

                if (!soilStack.isEmpty() && soilStack.getItem() instanceof BlockItem blockItem) {
                    Block soilBlock = blockItem.getBlock();
                    String soilId = RegistryHelper.getBlockId(soilBlock);

                    // Check if the soil is regular farmland
                    if (soilId.equals("minecraft:farmland")) {
                        String farmlandId = essenceToFarmland.get(heldItemId);
                        Block resultBlock = RegistryHelper.getBlock(farmlandId);

                        if (resultBlock != null) {
                            // Replace with MA farmland
                            ItemStack maFarmlandStack = new ItemStack(resultBlock);
                            agritechPlanterBlockEntity.inventory.setStackInSlot(1, maFarmlandStack);

                            // Consume one essence
                            if (!player.getAbilities().instabuild) {
                                stack.shrink(1);
                            }

                            // Play magical transformation sound
                            level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);

                            return ItemInteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }
            // Check if the player is holding a hoe
            else if (stack.getItem() instanceof HoeItem) {
                ItemStack soilStack = agritechPlanterBlockEntity.inventory.getStackInSlot(1);

                if (!soilStack.isEmpty() && soilStack.getItem() instanceof BlockItem blockItem) {
                    Block soilBlock = blockItem.getBlock();
                    String soilId = RegistryHelper.getBlockId(soilBlock);

                    Map<String, String> tillableBlocks = new HashMap<>();
                    // Vanilla blocks
                    tillableBlocks.put("minecraft:dirt", "minecraft:farmland");
                    tillableBlocks.put("minecraft:grass_block", "minecraft:farmland");
                    tillableBlocks.put("minecraft:mycelium", "minecraft:farmland");
                    tillableBlocks.put("minecraft:podzol", "minecraft:farmland");
                    tillableBlocks.put("minecraft:coarse_dirt", "minecraft:farmland");
                    tillableBlocks.put("minecraft:rooted_dirt", "minecraft:farmland");

                    // Modded blocks - check if the mod is loaded
                    if (ModList.get().isLoaded("farmersdelight")) {
                        tillableBlocks.put("farmersdelight:rich_soil", "farmersdelight:rich_soil_farmland");
                        tillableBlocks.put("farmersdelight:organic_compost", "farmersdelight:rich_soil_farmland");
                    }

                    if (tillableBlocks.containsKey(soilId)) {
                        Block resultBlock = RegistryHelper.getBlock(tillableBlocks.get(soilId));
                        if (resultBlock != null) {
                            ItemStack farmlandStack = new ItemStack(resultBlock);
                            agritechPlanterBlockEntity.inventory.setStackInSlot(1, farmlandStack);

                            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                            if (!player.getAbilities().instabuild) {
                                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            }

                            return ItemInteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }

            // If not hoeing or using essence, open the GUI
            if (!level.isClientSide()) {
                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) ->
                                new AgritechPlanterMenu(containerId, playerInventory, agritechPlanterBlockEntity),
                        Component.translatable("container.agritech.planter")
                );

                player.openMenu(menuProvider, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get()
                ? (lvl, pos, blockState, be) ->
                AgritechPlanterBlockEntity.tick(lvl, pos, blockState, (AgritechPlanterBlockEntity) be)
                : null;
    }
}
