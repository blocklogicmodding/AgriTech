package com.blocklogic.agritech.block.entity.renderer;

import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

public class AgritechPlanterBlockEntityRenderer implements BlockEntityRenderer<AgritechPlanterBlockEntity> {

    public AgritechPlanterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(AgritechPlanterBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        if (pBlockEntity.inventory.getSlots() > 1 && !pBlockEntity.inventory.getStackInSlot(1).isEmpty()) {
            ItemStack soilStack = pBlockEntity.inventory.getStackInSlot(1);


            if (soilStack.getItem() instanceof BlockItem blockItem) {
                BlockState soilState = blockItem.getBlock().defaultBlockState();

                pPoseStack.pushPose();

                pPoseStack.translate(0.2, 0.6, 0.2);
                pPoseStack.scale(0.6f, 0.05f, 0.6f);

                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

                dispatcher.renderSingleBlock(soilState, pPoseStack, pBufferSource, pPackedLight, OverlayTexture.NO_OVERLAY);

                pPoseStack.popPose();
            }
        }

        if (pBlockEntity.inventory.getSlots() > 0 && !pBlockEntity.inventory.getStackInSlot(0).isEmpty() &&  pBlockEntity.inventory.getSlots() > 1 && !pBlockEntity.inventory.getStackInSlot(1).isEmpty()) {

            ItemStack seedStack = pBlockEntity.inventory.getStackInSlot(0);
            int growthStage = pBlockEntity.getGrowthStage();

            if (growthStage > 0) {
                pPoseStack.pushPose();

                pPoseStack.translate(0.1725, 0.65, 0.1725);

                float growthScale = 0.2f + (growthStage / 7.0f) * 0.5f;
                pPoseStack.scale(0.65f, growthScale, 0.65f);

                BlockState cropState = getCropBlockState(seedStack, growthStage);

                if (cropState != null) {
                    BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

                    dispatcher.renderSingleBlock(cropState, pPoseStack, pBufferSource,
                            pPackedLight, OverlayTexture.NO_OVERLAY);
                }

                pPoseStack.popPose();
            }
        }
    }

    private BlockState getCropBlockState(ItemStack stack, int age) {
        if (!(stack.getItem() instanceof BlockItem blockItem)) return null;

        Block cropBlock = blockItem.getBlock();
        BlockState state = cropBlock.defaultBlockState();

        if (state.hasProperty(BlockStateProperties.AGE_1)) {
            return state.setValue(BlockStateProperties.AGE_1, Math.min(age, 1));
        } else if (state.hasProperty(BlockStateProperties.AGE_2)) {
            return state.setValue(BlockStateProperties.AGE_2, Math.min(age, 2));
        } else if (state.hasProperty(BlockStateProperties.AGE_3)) {
            return state.setValue(BlockStateProperties.AGE_3, Math.min(age, 3));
        } else if (state.hasProperty(BlockStateProperties.AGE_5)) {
            return state.setValue(BlockStateProperties.AGE_5, Math.min(age, 5));
        } else if (state.hasProperty(BlockStateProperties.AGE_7)) {
            return state.setValue(BlockStateProperties.AGE_7, Math.min(age, 7));
        }

        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals("age") && property instanceof IntegerProperty intProp) {
                int maxAge = intProp.getPossibleValues().stream()
                        .max(Integer::compareTo)
                        .orElse(0);
                int clampedAge = Math.min(age, maxAge);
                return setAgeProperty(state, intProp, clampedAge);
            }
        }

        return state;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> BlockState setAgeProperty(BlockState state, Property<T> property, int age) {
        return state.setValue(property, (T)(Integer)age);
    }
}
