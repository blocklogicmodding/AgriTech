package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.util.RegistryHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mojang.text2speech.Narrator.LOGGER;

@JeiPlugin
public class AgritechJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        LOGGER.info("JEI plugin loaded: {}", PLUGIN_ID);
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
        LOGGER.info("Planter Recipes Generated: {}", planterRecipes.size());
        registration.addRecipes(PlanterRecipeCategory.PLANTER_RECIPE_RECIPE_TYPE, planterRecipes);

        for (PlanterRecipe recipe : planterRecipes) {
            LOGGER.info("Registering Recipe with JEI - Seed: {}, Soil: {}, Outputs: {}",
                    recipe.getSeedIngredient(), recipe.getSoilIngredient(), recipe.getOutputs());
            for (ItemStack seed : recipe.getSeedIngredient().getItems()) {
                registration.addIngredientInfo(
                        seed,
                        VanillaTypes.ITEM_STACK,
                        Component.translatable("jei.agritech.planter.tooltip")
                );
            }
        }

        LOGGER.info("Registering {} planter recipes", planterRecipes.size());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Register our planter blocks as recipe catalysts
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

        // We'll need to get info from our config system to build recipes
        // For each valid seed and soil combination in the config
        Map<String, List<String>> seedToSoilMap = AgritechCropConfig.getAllSeedToSoilMappings();
        LOGGER.info("Seed-to-Soil Map: {}", seedToSoilMap);

        String seedId;
        for (Map.Entry<String, List<String>> entry : seedToSoilMap.entrySet()) {
            seedId = entry.getKey();
            LOGGER.info("Processing Seed ID: {}", seedId);

            for (String soilId : entry.getValue()) {
                Block soilBlock = RegistryHelper.getBlock(soilId);
                if (soilBlock == null) {
                    LOGGER.warn("Skipping recipe: Soil block not found for soil ID {}", soilId);
                    continue; // Skipping this combination gracefully
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