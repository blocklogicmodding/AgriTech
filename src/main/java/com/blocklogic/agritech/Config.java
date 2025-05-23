package com.blocklogic.agritech;

import com.blocklogic.agritech.config.AgritechCropConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = AgriTech.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue MESSAGE = BUILDER
            .comment("INFO: If you change any of the values below, delete 'config/agritech/crops_and_soils.json' and restart your client to regenerate the crop config!")
            .define("infoIs", true);

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

    private static final ModConfigSpec.BooleanValue ENABLE_JUST_DIRE_THINGS_SOIL = BUILDER
            .comment("Enable Just Dire Things Soils. Default: true")
            .define("enableJustDireThingsSoils", true);

    private static final ModConfigSpec.BooleanValue ENABLE_IMMERSIVE_ENGINEERING = BUILDER
            .comment("Enable Immersive Engineering Fiber. Default: true")
            .define("enableImmersiveEngineering", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableMysticalAgriculture;
    public static boolean enableMysticalAgradditions;
    public static boolean enableFarmersDelight;
    public static boolean enableArsNouveau;
    public static boolean enableSilentGear;
    public static boolean enableJustDireThingSoils;
    public static boolean enableImmersiveEngineering;

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    public static void loadConfig() {
        enableMysticalAgriculture = ENABLE_MYSTICAL_AGRICULTURE.get() && ModList.get().isLoaded("mysticalagriculture");
        enableMysticalAgradditions = ENABLE_MYSTICAL_AGRADDITIONS.get() && ModList.get().isLoaded("mysticalagradditions");
        enableFarmersDelight = ENABLE_FARMERS_DELIGHT.get() && ModList.get().isLoaded("farmersdelight");
        enableArsNouveau = ENABLE_ARS_NOUVEAU.get() && ModList.get().isLoaded("ars_nouveau");
        enableSilentGear = ENABLE_SILENT_GEAR.get() && ModList.get().isLoaded("silentgear");
        enableJustDireThingSoils = ENABLE_JUST_DIRE_THINGS_SOIL.get() && ModList.get().isLoaded("justdirethings");
        enableImmersiveEngineering = ENABLE_IMMERSIVE_ENGINEERING.get() && ModList.get().isLoaded("immersiveengineering");
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        loadConfig();
        AgritechCropConfig.loadConfig();
    }
}