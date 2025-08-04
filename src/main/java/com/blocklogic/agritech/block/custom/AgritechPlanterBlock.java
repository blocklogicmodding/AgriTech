package com.blocklogic.agritech.block.custom;

import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.block.entity.ModBlockEntities;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.item.ModItems;
import com.blocklogic.agritech.screen.custom.AgritechPlanterMenu;
import com.blocklogic.agritech.util.PlanterUpgradeHandler;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
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

public class AgritechPlanterBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.1, 15.0, 9.0, 15.0);
    public static final MapCodec<AgritechPlanterBlock> CODEC = simpleCodec(AgritechPlanterBlock::new);

    public AgritechPlanterBlock(Properties properties) {
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
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AgritechPlanterBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.getBlock() != newState.getBlock()) {
            if(level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity agritechPlanterBlock) {
                agritechPlanterBlock.drops();
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.getItem() == ModItems.HOPPING_UPGRADE.get()) {
            if (!level.isClientSide()) {
                boolean upgraded = PlanterUpgradeHandler.performUpgrade(level, pos, state, player, hand);
                if (upgraded) {
                    return ItemInteractionResult.SUCCESS;
                }
            }
            return ItemInteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity planterBlockEntity) {
            if (player.isCrouching()) {
                if (!level.isClientSide()) {
                    MenuProvider menuProvider = new SimpleMenuProvider(
                            (containerId, playerInventory, playerEntity) ->
                                    new AgritechPlanterMenu(containerId, playerInventory, planterBlockEntity),
                            Component.translatable("container.agritech.planter")
                    );

                    player.openMenu(menuProvider, pos);
                }
                return ItemInteractionResult.SUCCESS;
            }

            ItemStack heldItem = player.getItemInHand(hand);
            String heldItemId = RegistryHelper.getItemId(heldItem);

            if (AgritechCropConfig.isValidSeed(heldItemId) && !AgritechCropConfig.isValidSoil(heldItemId)) {
                if (level.isClientSide()) {
                    return ItemInteractionResult.SUCCESS;
                }

                if (planterBlockEntity.inventory.getStackInSlot(0).isEmpty()) {
                    ItemStack soilStack = planterBlockEntity.inventory.getStackInSlot(1);
                    String soilId = RegistryHelper.getItemId(soilStack);

                    if (soilStack.isEmpty() || AgritechCropConfig.isSoilValidForSeed(heldItemId, soilId)) {
                        ItemStack seedToInsert = heldItem.copy();
                        seedToInsert.setCount(1);
                        planterBlockEntity.inventory.setStackInSlot(0, seedToInsert);

                        if (!player.getAbilities().instabuild) {
                            heldItem.shrink(1);
                        }

                        level.playSound(player, pos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide());
                    } else {
                        player.displayClientMessage(Component.translatable("message.agritech.invalid_seed_soil_combination"), true);
                        return ItemInteractionResult.sidedSuccess(level.isClientSide());
                    }
                } else {
                    player.displayClientMessage(Component.translatable("message.agritech.slots_full"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }

            if (AgritechCropConfig.isValidSoil(heldItemId)) {
                if (level.isClientSide()) {
                    return ItemInteractionResult.SUCCESS;
                }

                if (planterBlockEntity.inventory.getStackInSlot(1).isEmpty()) {
                    ItemStack soilToInsert = heldItem.copy();
                    soilToInsert.setCount(1);
                    planterBlockEntity.inventory.setStackInSlot(1, soilToInsert);

                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }

                    level.playSound(player, pos, SoundEvents.GRAVEL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    player.displayClientMessage(Component.translatable("message.agritech.slots_full"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }

            if (heldItem.getItem() instanceof HoeItem) {
                if (level.isClientSide()) {
                    return ItemInteractionResult.SUCCESS;
                }

                if (planterBlockEntity.inventory.getStackInSlot(1).isEmpty()) {
                    ItemStack farmlandStack = new ItemStack(net.minecraft.world.level.block.Blocks.FARMLAND);
                    planterBlockEntity.inventory.setStackInSlot(1, farmlandStack);

                    level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                } else {
                    player.displayClientMessage(Component.translatable("message.agritech.invalid_soil"), true);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }

            if (ModList.get().isLoaded("mysticalagriculture") || ModList.get().isLoaded("mysticalagradditions")) {
                Map<String, String> essenceToFarmland = new HashMap<>();

                essenceToFarmland.put("mysticalagriculture:inferium_essence", "mysticalagriculture:inferium_farmland");
                essenceToFarmland.put("mysticalagriculture:prudentium_essence", "mysticalagriculture:prudentium_farmland");
                essenceToFarmland.put("mysticalagriculture:tertium_essence", "mysticalagriculture:tertium_farmland");
                essenceToFarmland.put("mysticalagriculture:imperium_essence", "mysticalagriculture:imperium_farmland");
                essenceToFarmland.put("mysticalagriculture:supremium_essence", "mysticalagriculture:supremium_farmland");

                essenceToFarmland.put("mysticalagradditions:insanium_essence", "mysticalagradditions:insanium_farmland");

                if (essenceToFarmland.containsKey(heldItemId)) {
                    if (!planterBlockEntity.inventory.getStackInSlot(1).isEmpty()) {
                        ItemStack soilStack = planterBlockEntity.inventory.getStackInSlot(1);
                        String soilId = RegistryHelper.getItemId(soilStack);

                        if ((soilId.startsWith("mysticalagriculture:") || soilId.startsWith("mysticalagradditions:")) && soilId.endsWith("_farmland")) {

                            String farmlandId = essenceToFarmland.get(heldItemId);
                            Block resultBlock = RegistryHelper.getBlock(farmlandId);

                            if (resultBlock != null) {
                                if (soilId.equals(farmlandId)) {
                                    if (!level.isClientSide()) {
                                        player.displayClientMessage(Component.translatable("message.agritech.same_farmland"), true);
                                    }
                                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                                }

                                ItemStack maFarmlandStack = new ItemStack(resultBlock);
                                planterBlockEntity.inventory.setStackInSlot(1, maFarmlandStack);

                                if (!player.getAbilities().instabuild) {
                                    stack.shrink(1);
                                }

                                level.playSound(player, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);

                                return ItemInteractionResult.sidedSuccess(level.isClientSide());
                            }
                        }
                    }
                }
            }

            if (!level.isClientSide()) {
                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) ->
                                new AgritechPlanterMenu(containerId, playerInventory, planterBlockEntity),
                        Component.translatable("container.agritech.planter")
                );

                player.openMenu(menuProvider, pos);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return ItemInteractionResult.FAIL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get() ?
                (lvl, pos, blockState, blockEntity) -> AgritechPlanterBlockEntity.tick(lvl, pos, blockState, (AgritechPlanterBlockEntity)blockEntity) :
                null;
    }

}
