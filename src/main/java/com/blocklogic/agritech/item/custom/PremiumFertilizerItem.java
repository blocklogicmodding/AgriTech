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

public class PremiumFertilizerItem extends Item {

    public PremiumFertilizerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockPos blockPos1 = blockPos.relative(context.getClickedFace());
        Player player = context.getPlayer();

        if (applyPremiumFertilizer(context.getItemInHand(), level, blockPos, player)) {
            if (!level.isClientSide) {
                level.levelEvent(1505, blockPos, 3);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            BlockState blockState = level.getBlockState(blockPos);
            boolean canPlaceAt = blockState.isFaceSturdy(level, blockPos, context.getClickedFace());

            if (canPlaceAt && BoneMealItem.growWaterPlant(context.getItemInHand(), level, blockPos1, context.getClickedFace())) {
                if (!level.isClientSide) {
                    level.levelEvent(1505, blockPos1, 3);
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static boolean applyPremiumFertilizer(ItemStack stack, Level level, BlockPos pos, Player player) {
        BlockState blockState = level.getBlockState(pos);
        Block block = blockState.getBlock();

        if (level instanceof ServerLevel serverLevel) {
            Optional<IntegerProperty> growthProperty = getGrowthProperty(blockState);
            if (growthProperty.isPresent()) {
                IntegerProperty ageProperty = growthProperty.get();
                int currentAge = blockState.getValue(ageProperty);
                int maxAge = getMaxAge(ageProperty, block);

                int newAge = Math.min(maxAge, Math.max(currentAge + (maxAge * 3 / 4), currentAge + 1));

                BlockState newState = blockState.setValue(ageProperty, newAge);
                level.setBlock(pos, newState, 2);

                if (player != null && !player.getAbilities().instabuild) {
                    stack.shrink(1);
                }

                return true;
            }
            else if (block instanceof BambooStalkBlock || block instanceof SugarCaneBlock) {
                int growAmount = 2 + level.getRandom().nextInt(2);
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
            else if (block instanceof BonemealableBlock growable) {
                if (growable.isValidBonemealTarget(serverLevel, pos, blockState)) {
                    for (int i = 0; i < 3; i++) {
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