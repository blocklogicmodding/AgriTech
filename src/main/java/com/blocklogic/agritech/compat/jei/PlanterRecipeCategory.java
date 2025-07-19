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

public class PlanterRecipeCategory implements IRecipeCategory<PlanterRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "planter");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "textures/gui/jei/planter.png");

    public static final RecipeType<PlanterRecipe> PLANTER_RECIPE_RECIPE_TYPE = new RecipeType<>(UID, PlanterRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    @Override
    public int getWidth() {
        return 134;
    }

    @Override
    public int getHeight() {
        return 72;
    }


    public PlanterRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE,0, 0, 134, 72);
        if (background == null) {
        } else {
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
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 10)
                .addIngredients(VanillaTypes.ITEM_STACK, List.of(recipe.getSeedIngredient().getItems()));

        builder.addSlot(RecipeIngredientRole.INPUT, 10, 46)
                .addIngredients(recipe.getSoilIngredient());

        int outputIndex = 0;
        for (ItemStack output : recipe.getOutputs()) {
            int x = 54 + (outputIndex % 3) * 18;
            int y = 19 + (outputIndex / 3) * 18;
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