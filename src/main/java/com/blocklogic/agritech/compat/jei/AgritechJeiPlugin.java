package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.util.RegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class AgritechJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new PlanterRecipeCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        List<PlanterRecipe> planterRecipes = generatePlanterRecipes();
        registration.addRecipes(PlanterRecipeCategory.PLANTER_RECIPE_RECIPE_TYPE, planterRecipes);

        for (PlanterRecipe recipe : planterRecipes) {
            for (ItemStack seed : recipe.getSeedIngredient().getItems()) {
                registration.addIngredientInfo(
                        seed,
                        VanillaTypes.ITEM_STACK,
                        Component.translatable("jei.agritech.planter.tooltip")
                );
            }
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.AGRITECH_PLANTER_BLOCK.get()),
                PlanterRecipeCategory.PLANTER_RECIPE_RECIPE_TYPE
        );
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get()),
                PlanterRecipeCategory.PLANTER_RECIPE_RECIPE_TYPE
        );
    }

    private List<PlanterRecipe> generatePlanterRecipes() {
        List<PlanterRecipe> recipes = new ArrayList<>();

        Map<String, List<String>> seedToSoilMap = AgritechCropConfig.getAllSeedToSoilMappings();

        String seedId;
        for (Map.Entry<String, List<String>> entry : seedToSoilMap.entrySet()) {
            seedId = entry.getKey();

            for (String soilId : entry.getValue()) {
                Block soilBlock = RegistryHelper.getBlock(soilId);
                if (soilBlock == null) {
                    continue;
                }
                PlanterRecipe recipe = PlanterRecipe.create(seedId, soilId);
                if (!recipe.getOutputs().isEmpty()) {
                    recipes.add(recipe);
                }
            }

        }

        return recipes;
    }
}