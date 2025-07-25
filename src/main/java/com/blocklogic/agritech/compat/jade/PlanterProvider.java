package com.blocklogic.agritech.compat.jade;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PlanterProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(AgriTech.MODID, "planter_info");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();

        if (!data.contains("hasCrop") || !data.getBoolean("hasCrop")) {
            return;
        }

        String cropName = data.getString("cropName");
        int currentStage = data.getInt("currentStage");
        int maxStage = data.getInt("maxStage");
        float progressPercent = data.getFloat("progressPercent");
        String soilName = data.getString("soilName");
        float growthModifier = data.getFloat("growthModifier");

        if (currentStage >= maxStage) {
            tooltip.add(Component.translatable("jade.agritech.crop_ready", cropName));
        } else {
            tooltip.add(Component.translatable("jade.agritech.crop_progress",
                    cropName, currentStage, maxStage, Math.round(progressPercent)).withStyle(ChatFormatting.DARK_GREEN));
        }

        tooltip.add(Component.translatable("jade.agritech.soil_info",
                soilName, String.format("%.2fx", growthModifier)));
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof AgritechPlanterBlockEntity planter)) {
            return;
        }

        ItemStack seedStack = planter.inventory.getStackInSlot(0);
        ItemStack soilStack = planter.inventory.getStackInSlot(1);

        if (!seedStack.isEmpty() && !soilStack.isEmpty()) {
            data.putBoolean("hasCrop", true);
            data.putString("cropName", seedStack.getDisplayName().getString());
            data.putInt("currentStage", planter.getGrowthStage());
            data.putInt("maxStage", planter.maxGrowthStage);
            data.putFloat("progressPercent", planter.getGrowthProgress() * 100);
            data.putString("soilName", soilStack.getDisplayName().getString());
            data.putFloat("growthModifier", planter.getGrowthModifier(soilStack));
        } else {
            data.putBoolean("hasCrop", false);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }
}