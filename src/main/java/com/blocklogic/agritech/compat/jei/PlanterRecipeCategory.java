package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static com.mojang.text2speech.Narrator.LOGGER;

public class PlanterRecipeCategory implements IRecipeCategory<PlanterRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "planter");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "textures/gui/jei/planter.png");

    public static final RecipeType<PlanterRecipe> PLANTER_RECIPE_RECIPE_TYPE = new RecipeType<>(UID, PlanterRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    @Override
    public int getWidth() {
        return 116; // Your texture width
    }

    @Override
    public int getHeight() {
        return 54; // Your texture height
    }


    public PlanterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE,0, 0, 116, 54);
        if (background == null) {
            LOGGER.error("Background drawable failed to load for texture: {}", TEXTURE);
        } else {
            LOGGER.info("Background drawable loaded successfully: {}", TEXTURE);
        }

        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get()));
    }

    @Override
    public RecipeType<PlanterRecipe> getRecipeType() {
        return PLANTER_RECIPE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.agritech.planter.tooltip");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PlanterRecipe recipe, IFocusGroup focuses) {
        // Seed slot
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 10)
                .addIngredients(VanillaTypes.ITEM_STACK, List.of(recipe.getSeedIngredient().getItems()));

        // Soil slot
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 28)
                .addIngredients(recipe.getSoilIngredient());

        // Output slots
        int outputIndex = 0;
        for (ItemStack output : recipe.getOutputs()) {
            int x = 54 + (outputIndex % 3) * 18;
            int y = 10 + (outputIndex / 3) * 18;
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, y)
                    .addItemStack(output);
            outputIndex++;
        }
    }

    @Override
    public void draw(PlanterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        background.draw(guiGraphics);
    }
}