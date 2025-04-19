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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

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
        if (level.getBlockEntity(pos) instanceof AgritechPlanterBlockEntity agritechPlanterBlockEntity) {
            // Check if the player is holding a hoe
            if (stack.getItem() instanceof HoeItem) {
                // Get the soil item from slot 1
                ItemStack soilStack = agritechPlanterBlockEntity.inventory.getStackInSlot(1);

                if (!soilStack.isEmpty() && soilStack.getItem() instanceof BlockItem blockItem) {
                    Block soilBlock = blockItem.getBlock();
                    String soilId = RegistryHelper.getBlockId(soilBlock);

                    // Map of tillable blocks to their farmland versions
                    Map<String, String> tillableBlocks = Map.of(
                            "minecraft:dirt", "minecraft:farmland",
                            "minecraft:grass_block", "minecraft:farmland",
                            "minecraft:mycelium", "minecraft:farmland",
                            "minecraft:podzol", "minecraft:farmland",
                            "minecraft:coarse_dirt", "minecraft:farmland",
                            "minecraft:rooted_dirt", "minecraft:farmland",
                            "farmersdelight:rich_soil", "farmersdelight:rich_soil_farmland",
                            "farmersdelight:organic_compost", "farmersdelight:rich_soil_farmland"
                    );

                    // Check if the soil can be tilled
                    if (tillableBlocks.containsKey(soilId)) {
                        // Replace with appropriate farmland
                        Block resultBlock = RegistryHelper.getBlock(tillableBlocks.get(soilId));
                        if (resultBlock != null) {
                            ItemStack farmlandStack = new ItemStack(resultBlock);
                            agritechPlanterBlockEntity.inventory.setStackInSlot(1, farmlandStack);

                            // Play sound
                            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);

                            // Damage the hoe
                            if (!player.getAbilities().instabuild) {
                                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                            }

                            return ItemInteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }

            // If not hoeing, open the GUI
            if (!level.isClientSide()) {
                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) ->
                                new AgritechPlanterMenu(containerId, playerInventory, agritechPlanterBlockEntity),
                        Component.literal("Planter")
                );

                player.openMenu(menuProvider, pos);
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
