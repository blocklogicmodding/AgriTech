package com.blocklogic.agritech;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.blocklogic.agritech.config.AgritechCropConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import static com.mojang.text2speech.Narrator.LOGGER;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = AgriTech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue MESSAGE = BUILDER
            .comment("INFO: If you change any of the values below, delete 'config/agritech/crops_and_soils.json' and restart your client to regenerate the crop config!")
            .define("infoIs", true);

    // Mod compatibility section
    private static final ModConfigSpec.BooleanValue ENABLE_MYSTICAL_AGRICULTURE = BUILDER
            .comment("Enable Mystical Agriculture. Default: true")
            .define("enableMysticalAgriculture", true);

    private static final ModConfigSpec.BooleanValue ENABLE_MYSTICAL_AGRADDITIONS = BUILDER
            .comment("Enable Mystical Agradditions. Default: true")
            .define("enableMysticalAgradditions", true);

    private static final ModConfigSpec.BooleanValue ENABLE_FARMERS_DELIGHT = BUILDER
            .comment("Enable Farmer's Delight. Default: true")
            .define("enableFarmersDelight", true);

    private static final ModConfigSpec.BooleanValue ENABLE_ARS_NOUVEAU = BUILDER
            .comment("Enable Ars Nouveau. Default: true")
            .define("enableArsNouveau", true);

    private static final ModConfigSpec.BooleanValue ENABLE_SILENT_GEAR = BUILDER
            .comment("Enable Silent Gear. Default: true")
            .define("enableSilentGear", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    // Mod compatibility flags
    public static boolean enableMysticalAgriculture;
    public static boolean enableMysticalAgradditions;
    public static boolean enableFarmersDelight;
    public static boolean enableArsNouveau;
    public static boolean enableSilentGear;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        LOGGER.info("Config onLoad() invoked.");
        // Load mod compatibility settings
        // Only enable if the mod is actually present
        enableMysticalAgriculture = ENABLE_MYSTICAL_AGRICULTURE.get() && ModList.get().isLoaded("mysticalagriculture");
        enableMysticalAgradditions = ENABLE_MYSTICAL_AGRADDITIONS.get() && ModList.get().isLoaded("mysticalagradditions");
        enableFarmersDelight = ENABLE_FARMERS_DELIGHT.get() && ModList.get().isLoaded("farmersdelight");
        enableArsNouveau = ENABLE_ARS_NOUVEAU.get() && ModList.get().isLoaded("ars_nouveau");
        enableSilentGear = ENABLE_SILENT_GEAR.get() && ModList.get().isLoaded("silentgear");

        AgritechCropConfig.loadConfig();
    }
}