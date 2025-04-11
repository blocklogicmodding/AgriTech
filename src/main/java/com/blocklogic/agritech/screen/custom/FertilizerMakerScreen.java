package com.blocklogic.agritech.screen.custom;

import com.blocklogic.agritech.AgriTech;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FertilizerMakerScreen extends AbstractContainerScreen<FertilizerMakerMenu> {

    public static final ResourceLocation FERTILIZER_MAKER_TEXTURE = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "textures/gui/fertilizer_maker/agritech_fertilizer_maker_gui.png");

    public FertilizerMakerScreen(FertilizerMakerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, FERTILIZER_MAKER_TEXTURE);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(FERTILIZER_MAKER_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            int progress = menu.getScaledProgress();
            int barX = x + 158;
            int barYBottom = y + 73;
            int barYTop = barYBottom - progress;

            guiGraphics.fill(barX, barYTop, barX + 4, barYBottom, 0xFFFF0000);

        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics, mouseX, mouseY, delta);

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}