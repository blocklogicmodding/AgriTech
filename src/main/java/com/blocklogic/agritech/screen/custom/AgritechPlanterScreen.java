package com.blocklogic.agritech.screen.custom;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.compat.jei.AgritechJeiPlugin;
import com.blocklogic.agritech.compat.jei.PlanterRecipeCategory;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class AgritechPlanterScreen extends AbstractContainerScreen<AgritechPlanterMenu> {

    public static final ResourceLocation AGRITECH_PLANTER_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "textures/gui/agritech_planter/agritech_planter_gui.png");

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public AgritechPlanterScreen(AgritechPlanterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageHeight = 172;
        this.inventoryLabelY = this.imageHeight - 96;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F,1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AGRITECH_PLANTER_GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) /2;

        guiGraphics.blit(AGRITECH_PLANTER_GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(guiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int guiX, int guiY) {
        AgritechPlanterBlockEntity blockEntity = this.menu.blockEntity;

        if (!blockEntity.inventory.getStackInSlot(0).isEmpty() &&
                !blockEntity.inventory.getStackInSlot(1).isEmpty()) {

            int progressX = guiX + 51;
            int progressY = guiY + 38;

            int maxProgressWidth = 21;
            int progressWidth = blockEntity.getProgressBarWidth(maxProgressWidth);

            if (progressWidth > 0) {
                guiGraphics.blit(AGRITECH_PLANTER_GUI_TEXTURE,
                        progressX, progressY,
                        0, 172,
                        progressWidth, 11);
            }
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);

        int guiX = (width - imageWidth) / 2;
        int guiY = (height - imageHeight) / 2;

        if (x >= guiX + 51 && x <= guiX + 51 + 21 && y >= guiY + 37 && y <= guiY + 38 + 11) {
            List<Component> tooltip = new ArrayList<>();
            float progress = this.menu.blockEntity.getGrowthProgress();
            tooltip.add(Component.translatable("tooltip.agritech.growth_progress"));
            tooltip.add(Component.literal(String.format("%.1f%%", progress * 100)).withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("tooltip.agritech.view_recipes").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));

            guiGraphics.renderComponentTooltip(this.font, tooltip, x, y);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            int guiX = (width - imageWidth) / 2;
            int guiY = (height - imageHeight) / 2;

            if (mouseX >= guiX + 51 && mouseX <= guiX + 51 + 21 && mouseY >= guiY + 37 && mouseY <= guiY + 38 + 11) {
                if (minecraft != null && minecraft.player != null) {
                    IJeiRuntime runtime = AgritechJeiPlugin.getJeiRuntime();
                    if (runtime != null) {
                        runtime.getRecipesGui().showTypes(List.of(PlanterRecipeCategory.PLANTER_RECIPE_RECIPE_TYPE));
                    }
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
