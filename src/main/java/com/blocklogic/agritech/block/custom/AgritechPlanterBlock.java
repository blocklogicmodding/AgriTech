package com.blocklogic.agritech.block.custom;

import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.block.entity.ModBlockEntities;
import com.blocklogic.agritech.screen.custom.AgritechPlanterMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import org.jetbrains.annotations.Nullable;

public class AgritechPlanterBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.1, 15.0, 13.0, 15.00);
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
        if (level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity planterBlockEntity) {
            if (!level.isClientSide()) {
                if (planterBlockEntity.isReadyToHarvest() && stack.getItem() instanceof HoeItem) {
                    planterBlockEntity.harvestCrop();

                    level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);

                    if (!player.getAbilities().instabuild) {
                        stack.hurtAndBreak(1, player, null);
                    }

                    return ItemInteractionResult.SUCCESS;
                }

                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) -> new AgritechPlanterMenu(containerId, playerInventory, planterBlockEntity),
                        Component.literal("Agritech Planter")
                );

                ((ServerPlayer) player).openMenu(menuProvider, pos);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get() ?
                (lvl, pos, blockState, blockEntity) -> AgritechPlanterBlockEntity.tick(lvl, pos, blockState, (AgritechPlanterBlockEntity)blockEntity) :
                null;
    }

}
