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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

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

                pPoseStack.translate(0.25, 0.65, 0.25);

                float growthScale = 0.2f + (growthStage / 7.0f) * 0.4f;
                pPoseStack.scale(0.5f, growthScale, 0.5f);

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

    private BlockState getCropBlockState(ItemStack seedStack, int growthStage) {
        Item seedItem = seedStack.getItem();

        int stage = Math.min(growthStage, 7);

        if (seedItem == Items.WHEAT_SEEDS) {
            return Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, stage);
        } else if (seedItem == Items.POTATO) {
            return Blocks.POTATOES.defaultBlockState().setValue(CropBlock.AGE, stage);
        } else if (seedItem == Items.CARROT) {
            return Blocks.CARROTS.defaultBlockState().setValue(CropBlock.AGE, stage);
        } else if (seedItem == Items.BEETROOT_SEEDS) {
            return Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, stage);
        } else if (seedItem == Items.PUMPKIN_SEEDS) {
            return Blocks.PUMPKIN_STEM.defaultBlockState().setValue(StemBlock.AGE, stage);
        } else if (seedItem == Items.MELON_SEEDS) {
            return Blocks.MELON_STEM.defaultBlockState().setValue(StemBlock.AGE, stage);
        } else if (seedItem == Items.NETHER_WART) {
            int netherStage = Math.min(growthStage, 3); // Nether Wart max age is 3
            return Blocks.NETHER_WART.defaultBlockState().setValue(NetherWartBlock.AGE, netherStage);
        } else if (seedItem == Items.SUGAR_CANE) {
            return Blocks.SUGAR_CANE.defaultBlockState();
        } else if (seedItem == Items.BAMBOO) {
            return Blocks.BAMBOO.defaultBlockState();
        }

        return null;
    }
}
