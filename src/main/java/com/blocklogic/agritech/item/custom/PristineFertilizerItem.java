package com.blocklogic.agritech.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Optional;

public class PristineFertilizerItem extends Item {

    public PristineFertilizerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos1 = blockPos.relative(context.getClickedFace());
        Player player = context.getPlayer();

        if (applyPristineFertilizer(context.getItemInHand(), level, blockPos, player)) {
            if (!level.isClientSide) {
                level.levelEvent(1505, blockPos, 7);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            BlockState blockState = level.getBlockState(blockPos);
            boolean canPlaceAt = blockState.isFaceSturdy(level, blockPos, context.getClickedFace());

            if (canPlaceAt && BoneMealItem.growWaterPlant(context.getItemInHand(), level, blockPos1, context.getClickedFace())) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, blockPos1, 7);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static boolean applyPristineFertilizer(ItemStack stack, Level level, BlockPos pos, Player player) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();

        if (level instanceof ServerLevel serverLevel) {
            Optional<IntegerProperty> growthProperty = getGrowthProperty(blockState);
            if (growthProperty.isPresent()) {
                IntegerProperty ageProperty = growthProperty.get();
                int maxAge = getMaxAge(ageProperty, block);

                BlockState newState = blockState.setValue(ageProperty, maxAge);
                level.setBlock(pos, newState, 2);

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                return true;
            }
            else if (block instanceof BambooStalkBlock || block instanceof SugarCaneBlock) {
                int growAmount = 4 + level.getRandom().nextInt(3);
                for (int i = 0; i < growAmount; i++) {
                    BlockPos abovePos = pos.above(i);
                    if (level.getBlockState(abovePos).is(block) && level.isEmptyBlock(abovePos.above())) {
                        level.setBlockAndUpdate(abovePos.above(), block.defaultBlockState());
                    } else {
                        break;
                    }
                }

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                return true;
            }
            else if (block instanceof SaplingBlock) {

                if (((SaplingBlock) block).isValidBonemealTarget(serverLevel, pos, blockState)) {
                    if (serverLevel.random.nextFloat() < 0.9f) {
                        ((SaplingBlock) block).advanceTree(serverLevel, pos, blockState, serverLevel.random);
                    }

                    if (player != null && !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    return true;
                }
            }
            else if (block instanceof BonemealableBlock growable) {
                if (growable.isValidBonemealTarget(serverLevel, pos, blockState)) {
                    for (int i = 0; i < 8; i++) {
                        if (growable.isBonemealSuccess(serverLevel, serverLevel.random, pos, blockState)) {
                            growable.performBonemeal(serverLevel, serverLevel.random, pos, blockState);
                        }
                    }

                    if (player != null && !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    return true;
                }
            }
        }

        return false;
    }

    private static Optional<IntegerProperty> getGrowthProperty(BlockState state) {
        Block block = state.getBlock();

        if (block instanceof CropBlock) {
            return Optional.of(CropBlock.AGE);
        } else if (block instanceof StemBlock) {
            return Optional.of(StemBlock.AGE);
        } else if (block instanceof BeetrootBlock) {
            return Optional.of(BeetrootBlock.AGE);
        } else if (block instanceof NetherWartBlock) {
            return Optional.of(NetherWartBlock.AGE);
        }

        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals("age") && property instanceof IntegerProperty) {
                return Optional.of((IntegerProperty) property);
            }
        }

        return Optional.empty();
    }

    private static int getMaxAge(IntegerProperty ageProperty, Block block) {
        if (block instanceof CropBlock) {
            return ((CropBlock) block).getMaxAge();
        } else if (block instanceof StemBlock) {
            return 7;
        } else if (block instanceof BeetrootBlock) {
            return 3;
        } else if (block instanceof NetherWartBlock) {
            return 3;
        }

        return ageProperty.getPossibleValues().stream()
                .mapToInt(Integer.class::cast)
                .max()
                .orElse(0);
    }
}