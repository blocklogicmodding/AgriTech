package com.blocklogic.agritech.compat.jei;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.util.RegistryHelper;
import com.mojang.logging.LogUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
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

        for (Map.Entry<String, List<String>> entry : seedToSoilMap.entrySet()) {
            String seedId = entry.getKey();

            for (String soilId : entry.getValue()) {
                try {
                    Block soilBlock = RegistryHelper.getBlock(soilId);
                    if (soilBlock == null) {
                        LogUtils.getLogger().error("Invalid soil block in config: {} for seed {}", soilId, seedId);
                        continue;
                    }

                    PlanterRecipe recipe = PlanterRecipe.create(seedId, soilId);
                    if (recipe != null && !recipe.getOutputs().isEmpty()) {
                        recipes.add(recipe);
                    }
                } catch (Exception e) {
                    LogUtils.getLogger().error("Error creating recipe for seed {} and soil {}: {}",
                            seedId, soilId, e.getMessage(), e);
                }
            }
        }

        LogUtils.getLogger().info("Generated {} planter recipes for JEI", recipes.size());
        return recipes;
    }

    private static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        AgritechJeiPlugin.jeiRuntime = jeiRuntime;
    }

    public static IJeiRuntime getJeiRuntime() {
        return jeiRuntime;
    }
}