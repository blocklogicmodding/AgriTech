package com.blocklogic.agritech.config;

import com.blocklogic.agritech.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AgritechCropConfig {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Map<String, CropInfo> crops = new HashMap<>();
    private static Map<String, SoilInfo> soils = new HashMap<>();

    public static void loadConfig() {
        LOGGER.info("AgritechCropConfig.loadConfig() invoked.");
        Path configPath = FMLPaths.CONFIGDIR.get().resolve("agritech/crops_and_soil.json");
        if (!Files.exists(configPath)) {
            createDefaultConfig(configPath);
        }

        try {
            String jsonString = Files.readString(configPath);
            CropConfigData configData = GSON.fromJson(jsonString, CropConfigData.class);
            processConfig(configData);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error("Failed to load crop config file: {}", e.getMessage());
            LOGGER.info("Loading default crop configuration instead");
            processConfig(getDefaultConfig());
        }

        AgritechOverrideConfig.loadOverrides(crops, soils);
    }

    private static void createDefaultConfig(Path configPath) {
        try {
            Files.createDirectories(configPath.getParent());
            CropConfigData defaultConfig = getDefaultConfig();
            String json = GSON.toJson(defaultConfig);
            Files.writeString(configPath, json);
        } catch (IOException e) {
            LOGGER.error("Failed to create default config file: {}", e.getMessage());
        }
    }

    private static CropConfigData getDefaultConfig() {
        LOGGER.info("Generating default crop config.");

        CropConfigData config = new CropConfigData();

        List<CropEntry> defaultCrops = new ArrayList<>();

        addVanillaCrops(defaultCrops);

        if (Config.enableMysticalAgriculture) {
            LOGGER.info("Adding Mystical Agriculture crops to AgriTech config");
            addMysticalAgricultureCrops(defaultCrops);
        }

        if (Config.enableFarmersDelight) {
            LOGGER.info("Adding Farmer's Delight crops to AgriTech config");
            addFarmersDelightCrops(defaultCrops);
        }

        if (Config.enableArsNouveau) {
            LOGGER.info("Adding Ars Nouveau crops to AgriTech config");
            addArsNouveauCrops(defaultCrops);
        }

        if (Config.enableSilentGear) {
            LOGGER.info("Adding Silent Gear crops to AgriTech config");
            addSilentGearCrops(defaultCrops);
        }

        config.allowedCrops = defaultCrops;

        List<SoilEntry> defaultSoils = new ArrayList<>();
        addVanillaSoils(defaultSoils);

        if (Config.enableMysticalAgriculture) {
            LOGGER.info("Adding Mystical Agriculture soils to AgriTech config");
            addMysticalAgricultureSoils(defaultSoils);
        }

        if (Config.enableFarmersDelight) {
            LOGGER.info("Adding Farmer's Delight soils to AgriTech config");
            addFarmersDelightSoils(defaultSoils);
        }

        if(Config.enableJustDireThingSoils) {
            LOGGER.info("Adding Just Dire Things soils to AgriTech config");
            addJustDireThingsSoils(defaultSoils);
        }

        if(Config.enableImmersiveEngineering) {
            LOGGER.info("Adding Immersive Engineering Hemp Fiber to AgriTech config");
            addImmersiveEngineering(defaultCrops);
        }

        config.allowedSoils = defaultSoils;

        return config;
    }

    private static void addVanillaCrops(List<CropEntry> crops) {
        // Wheat
        CropEntry wheat = new CropEntry();
        wheat.seed = "minecraft:wheat_seeds";
        wheat.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland");
        wheat.drops = new ArrayList<>();

        DropEntry wheatDrop = new DropEntry();
        wheatDrop.item = "minecraft:wheat";
        wheatDrop.count = new CountRange(1, 1);
        wheat.drops.add(wheatDrop);

        DropEntry wheatSeedsDrop = new DropEntry();
        wheatSeedsDrop.item = "minecraft:wheat_seeds";
        wheatSeedsDrop.count = new CountRange(1, 2);
        wheatSeedsDrop.chance = 0.5f;
        wheat.drops.add(wheatSeedsDrop);

        crops.add(wheat);

        // Beetroot
        CropEntry beetroot = new CropEntry();
        beetroot.seed = "minecraft:beetroot_seeds";
        beetroot.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland");
        beetroot.drops = new ArrayList<>();

        DropEntry beetrootDrop = new DropEntry();
        beetrootDrop.item = "minecraft:beetroot";
        beetrootDrop.count = new CountRange(1, 1);
        beetroot.drops.add(beetrootDrop);

        DropEntry beetrootSeedsDrop = new DropEntry();
        beetrootSeedsDrop.item = "minecraft:beetroot_seeds";
        beetrootSeedsDrop.count = new CountRange(1, 2);
        beetrootSeedsDrop.chance = 0.5f;
        beetroot.drops.add(beetrootSeedsDrop);

        crops.add(beetroot);

        // Carrot
        CropEntry carrot = new CropEntry();
        carrot.seed = "minecraft:carrot";
        carrot.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland");
        carrot.drops = new ArrayList<>();

        DropEntry carrotDrop = new DropEntry();
        carrotDrop.item = "minecraft:carrot";
        carrotDrop.count = new CountRange(2, 5);
        carrot.drops.add(carrotDrop);

        crops.add(carrot);

        // Potato
        CropEntry potato = new CropEntry();
        potato.seed = "minecraft:potato";
        potato.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland");
        potato.drops = new ArrayList<>();

        DropEntry potatoDrop = new DropEntry();
        potatoDrop.item = "minecraft:potato";
        potatoDrop.count = new CountRange(2, 5);
        potato.drops.add(potatoDrop);

        DropEntry poisonousPotatoDrop = new DropEntry();
        poisonousPotatoDrop.item = "minecraft:poisonous_potato";
        poisonousPotatoDrop.count = new CountRange(1, 1);
        poisonousPotatoDrop.chance = 0.02f;
        potato.drops.add(poisonousPotatoDrop);

        crops.add(potato);

        // Melon
        CropEntry melon = new CropEntry();
        melon.seed = "minecraft:melon_seeds";
        melon.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        melon.drops = new ArrayList<>();

        DropEntry melonSliceDrop = new DropEntry();
        melonSliceDrop.item = "minecraft:melon_slice";
        melonSliceDrop.count = new CountRange(3, 7);
        melon.drops.add(melonSliceDrop);

        crops.add(melon);

        // Pumpkin
        CropEntry pumpkin = new CropEntry();
        pumpkin.seed = "minecraft:pumpkin_seeds";
        pumpkin.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        pumpkin.drops = new ArrayList<>();

        DropEntry pumpkinDrop = new DropEntry();
        pumpkinDrop.item = "minecraft:pumpkin";
        pumpkinDrop.count = new CountRange(1, 1);
        pumpkin.drops.add(pumpkinDrop);

        crops.add(pumpkin);

        // Sugar Cane
        CropEntry sugarCane = new CropEntry();
        sugarCane.seed = "minecraft:sugar_cane";
        sugarCane.validSoils = List.of(
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:sand",
                "minecraft:red_sand",
                "agritechevolved:mulch"

        );
        sugarCane.drops = new ArrayList<>();

        DropEntry sugarCaneDrop = new DropEntry();
        sugarCaneDrop.item = "minecraft:sugar_cane";
        sugarCaneDrop.count = new CountRange(1, 3);
        sugarCane.drops.add(sugarCaneDrop);

        crops.add(sugarCane);

        // Cactus
        CropEntry cactus = new CropEntry();
        cactus.seed = "minecraft:cactus";
        cactus.validSoils = List.of(
                "minecraft:sand",
                "minecraft:red_sand"
        );
        cactus.drops = new ArrayList<>();

        DropEntry cactusDrop = new DropEntry();
        cactusDrop.item = "minecraft:cactus";
        cactusDrop.count = new CountRange(1, 3);
        cactus.drops.add(cactusDrop);

        crops.add(cactus);

        // Bamboo
        CropEntry bamboo = new CropEntry();
        bamboo.seed = "minecraft:bamboo";
        bamboo.validSoils = List.of(
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",
                "farmersdelight:rich_soil",

                "agritechevolved:mulch"
        );
        bamboo.drops = new ArrayList<>();

        DropEntry bambooDrop = new DropEntry();
        bambooDrop.item = "minecraft:bamboo";
        bambooDrop.count = new CountRange(2, 4);
        bamboo.drops.add(bambooDrop);

        crops.add(bamboo);

        // Sweet Berries
        CropEntry sweetBerries = new CropEntry();
        sweetBerries.seed = "minecraft:sweet_berries";
        sweetBerries.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland",
                "farmersdelight:rich_soil",
                "farmersdelight:organic_compost"
        );
        sweetBerries.drops = new ArrayList<>();

        DropEntry sweetBerriesDrop = new DropEntry();
        sweetBerriesDrop.item = "minecraft:sweet_berries";
        sweetBerriesDrop.count = new CountRange(2, 4);
        sweetBerries.drops.add(sweetBerriesDrop);

        crops.add(sweetBerries);

        // Glow Berries
        CropEntry glowBerries = new CropEntry();
        glowBerries.seed = "minecraft:glow_berries";
        glowBerries.validSoils = List.of(
                "minecraft:moss_block"
        );
        glowBerries.drops = new ArrayList<>();

        DropEntry glowBerriesDrop = new DropEntry();
        glowBerriesDrop.item = "minecraft:glow_berries";
        glowBerriesDrop.count = new CountRange(2, 4);
        glowBerries.drops.add(glowBerriesDrop);

        crops.add(glowBerries);

        // Nether Wart
        CropEntry netherWart = new CropEntry();
        netherWart.seed = "minecraft:nether_wart";
        netherWart.validSoils = List.of("minecraft:soul_sand");
        netherWart.drops = new ArrayList<>();

        DropEntry netherWartDrop = new DropEntry();
        netherWartDrop.item = "minecraft:nether_wart";
        netherWartDrop.count = new CountRange(1, 3);
        netherWart.drops.add(netherWartDrop);

        crops.add(netherWart);

        // Chorus Fruit
        CropEntry chorusFlower = new CropEntry();
        chorusFlower.seed = "minecraft:chorus_flower";
        chorusFlower.validSoils = List.of("minecraft:end_stone");
        chorusFlower.drops = new ArrayList<>();

        DropEntry chorusFruitDrop = new DropEntry();
        chorusFruitDrop.item = "minecraft:chorus_fruit";
        chorusFruitDrop.count = new CountRange(1, 3);
        chorusFlower.drops.add(chorusFruitDrop);

        DropEntry chorusFlowerDrop = new DropEntry();
        chorusFlowerDrop.item = "minecraft:chorus_flower";
        chorusFlowerDrop.count = new CountRange(1, 1);
        chorusFlowerDrop.chance = 0.02f;
        chorusFlower.drops.add(chorusFlowerDrop);

        crops.add(chorusFlower);

        // Kelp
        CropEntry kelp = new CropEntry();
        kelp.seed = "minecraft:kelp";
        kelp.validSoils = List.of("minecraft:mud");
        kelp.drops = new ArrayList<>();

        DropEntry kelpDrop = new DropEntry();
        kelpDrop.item = "minecraft:kelp";
        kelpDrop.count = new CountRange(1, 2);
        kelp.drops.add(kelpDrop);

        crops.add(kelp);

        // Brown Mushroom
        CropEntry brownMushroom = new CropEntry();
        brownMushroom.seed = "minecraft:brown_mushroom";
        brownMushroom.validSoils = List.of(
                "minecraft:mycelium",
                "minecraft:podzol",
                "farmersdelight:rich_soil",
                "farmersdelight:organic_compost",
                "agritechevolved:mulch"
        );
        brownMushroom.drops = new ArrayList<>();

        DropEntry brownMushroomDrop = new DropEntry();
        brownMushroomDrop.item = "minecraft:brown_mushroom";
        brownMushroomDrop.count = new CountRange(1, 1);
        brownMushroom.drops.add(brownMushroomDrop);

        crops.add(brownMushroom);

        // Red Mushroom
        CropEntry redMushroom = new CropEntry();
        redMushroom.seed = "minecraft:red_mushroom";
        redMushroom.validSoils = List.of(
                "minecraft:mycelium",
                "minecraft:podzol",
                "farmersdelight:rich_soil",
                "farmersdelight:organic_compost",
                "agritechevolved:mulch"
        );
        redMushroom.drops = new ArrayList<>();

        DropEntry redMushroomDrop = new DropEntry();
        redMushroomDrop.item = "minecraft:red_mushroom";
        redMushroomDrop.count = new CountRange(1, 1);
        redMushroom.drops.add(redMushroomDrop);

        crops.add(redMushroom);

        // Cocoa
        CropEntry cocoa = new CropEntry();
        cocoa.seed = "minecraft:cocoa_beans";
        cocoa.validSoils = List.of(
                "minecraft:jungle_log",
                "minecraft:jungle_wood",
                "minecraft:stripped_jungle_log",
                "minecraft:stripped_jungle_wood"
        );
        cocoa.drops = new ArrayList<>();

        DropEntry cocoaDrop = new DropEntry();
        cocoaDrop.item = "minecraft:cocoa_beans";
        cocoaDrop.count = new CountRange(1, 3);
        cocoa.drops.add(cocoaDrop);

        crops.add(cocoa);

        //pitcher_plant
        CropEntry pitcherCrop = new CropEntry();
        pitcherCrop.seed = "minecraft:pitcher_pod";
        pitcherCrop.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        pitcherCrop.drops = new ArrayList<>();

        DropEntry pitcherCropDrop = new DropEntry();
        pitcherCropDrop.item = "minecraft:pitcher_plant";
        pitcherCropDrop.count = new CountRange(1, 1);
        pitcherCrop.drops.add(pitcherCropDrop);

        crops.add(pitcherCrop);

        //torchflower
        CropEntry torchFlower = new CropEntry();
        torchFlower.seed = "minecraft:torchflower_seeds";
        torchFlower.validSoils = List.of(
                "minecraft:farmland",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
                );
        torchFlower.drops = new ArrayList<>();

        DropEntry torchFlowerDrop = new DropEntry();
        torchFlowerDrop.item = "minecraft:torchflower";
        torchFlowerDrop.count = new CountRange(1, 1);
        torchFlower.drops.add(torchFlowerDrop);

        crops.add(torchFlower);

        // Flowers Start
        CropEntry allium = new CropEntry();
        allium.seed = "minecraft:allium";
        allium.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        allium.drops = new ArrayList<>();

        DropEntry alliumDrop = new DropEntry();
        alliumDrop.item = "minecraft:allium";
        alliumDrop.count = new CountRange(1, 1);
        allium.drops.add(alliumDrop);

        crops.add(allium);

        CropEntry azureBluet = new CropEntry();
        azureBluet.seed = "minecraft:azure_bluet";
        azureBluet.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        azureBluet.drops = new ArrayList<>();

        DropEntry azureBluetDrop = new DropEntry();
        azureBluetDrop.item = "minecraft:azure_bluet";
        azureBluetDrop.count = new CountRange(1, 1);
        azureBluet.drops.add(azureBluetDrop);

        crops.add(azureBluet);

        CropEntry blueOrchid = new CropEntry();
        blueOrchid.seed = "minecraft:blue_orchid";
        blueOrchid.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        blueOrchid.drops = new ArrayList<>();

        DropEntry blueOrchidDrop = new DropEntry();
        blueOrchidDrop.item = "minecraft:blue_orchid";
        blueOrchidDrop.count = new CountRange(1, 1);
        blueOrchid.drops.add(blueOrchidDrop);

        crops.add(blueOrchid);

        CropEntry cornflower = new CropEntry();
        cornflower.seed = "minecraft:cornflower";
        cornflower.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        cornflower.drops = new ArrayList<>();

        DropEntry cornflowerDrop = new DropEntry();
        cornflowerDrop.item = "minecraft:cornflower";
        cornflowerDrop.count = new CountRange(1, 1);
        cornflower.drops.add(cornflowerDrop);

        crops.add(cornflower);

        CropEntry dandelion = new CropEntry();
        dandelion.seed = "minecraft:dandelion";
        dandelion.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        dandelion.drops = new ArrayList<>();

        DropEntry dandelionDrop = new DropEntry();
        dandelionDrop.item = "minecraft:dandelion";
        dandelionDrop.count = new CountRange(1, 1);
        dandelion.drops.add(dandelionDrop);

        crops.add(dandelion);

        CropEntry lilyOfTheValley = new CropEntry();
        lilyOfTheValley.seed = "minecraft:lily_of_the_valley";
        lilyOfTheValley.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        lilyOfTheValley.drops = new ArrayList<>();

        DropEntry lilyOfTheValleyDrop = new DropEntry();
        lilyOfTheValleyDrop.item = "minecraft:lily_of_the_valley";
        lilyOfTheValleyDrop.count = new CountRange(1, 1);
        lilyOfTheValley.drops.add(lilyOfTheValleyDrop);

        crops.add(lilyOfTheValley);

        CropEntry oxeyeDaisy = new CropEntry();
        oxeyeDaisy.seed = "minecraft:oxeye_daisy";
        oxeyeDaisy.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        oxeyeDaisy.drops = new ArrayList<>();

        DropEntry oxeyeDaisyDrop = new DropEntry();
        oxeyeDaisyDrop.item = "minecraft:oxeye_daisy";
        oxeyeDaisyDrop.count = new CountRange(1, 1);
        oxeyeDaisy.drops.add(oxeyeDaisyDrop);

        crops.add(oxeyeDaisy);

        CropEntry poppy = new CropEntry();
        poppy.seed = "minecraft:poppy";
        poppy.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        poppy.drops = new ArrayList<>();

        DropEntry poppyDrop = new DropEntry();
        poppyDrop.item = "minecraft:poppy";
        poppyDrop.count = new CountRange(1, 1);
        poppy.drops.add(poppyDrop);

        crops.add(poppy);

        CropEntry redTulip = new CropEntry();
        redTulip.seed = "minecraft:red_tulip";
        redTulip.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        redTulip.drops = new ArrayList<>();

        DropEntry redTulipDrop = new DropEntry();
        redTulipDrop.item = "minecraft:red_tulip";
        redTulipDrop.count = new CountRange(1, 1);
        redTulip.drops.add(redTulipDrop);

        crops.add(redTulip);

        CropEntry orangeTulip = new CropEntry();
        orangeTulip.seed = "minecraft:orange_tulip";
        orangeTulip.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        orangeTulip.drops = new ArrayList<>();

        DropEntry orangeTulipDrop = new DropEntry();
        orangeTulipDrop.item = "minecraft:orange_tulip";
        orangeTulipDrop.count = new CountRange(1, 1);
        orangeTulip.drops.add(orangeTulipDrop);

        crops.add(orangeTulip);

        CropEntry whiteTulip = new CropEntry();
        whiteTulip.seed = "minecraft:white_tulip";
        whiteTulip.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        whiteTulip.drops = new ArrayList<>();

        DropEntry whiteTulipDrop = new DropEntry();
        whiteTulipDrop.item = "minecraft:white_tulip";
        whiteTulipDrop.count = new CountRange(1, 1);
        whiteTulip.drops.add(whiteTulipDrop);

        crops.add(whiteTulip);

        CropEntry pinkTulip = new CropEntry();
        pinkTulip.seed = "minecraft:pink_tulip";
        pinkTulip.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        pinkTulip.drops = new ArrayList<>();

        DropEntry pinkTulipDrop = new DropEntry();
        pinkTulipDrop.item = "minecraft:pink_tulip";
        pinkTulipDrop.count = new CountRange(1, 1);
        pinkTulip.drops.add(pinkTulipDrop);

        crops.add(pinkTulip);

        CropEntry witherRose = new CropEntry();
        witherRose.seed = "minecraft:wither_rose";
        witherRose.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        witherRose.drops = new ArrayList<>();

        DropEntry witherRoseDrop = new DropEntry();
        witherRoseDrop.item = "minecraft:wither_rose";
        witherRoseDrop.count = new CountRange(1, 1);
        witherRose.drops.add(witherRoseDrop);

        crops.add(witherRose);

        CropEntry lilac = new CropEntry();
        lilac.seed = "minecraft:lilac";
        lilac.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        lilac.drops = new ArrayList<>();

        DropEntry lilacDrop = new DropEntry();
        lilacDrop.item = "minecraft:lilac";
        lilacDrop.count = new CountRange(1, 1);
        lilac.drops.add(lilacDrop);

        crops.add(lilac);

        CropEntry peony = new CropEntry();
        peony.seed = "minecraft:peony";
        peony.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        peony.drops = new ArrayList<>();

        DropEntry peonyDrop = new DropEntry();
        peonyDrop.item = "minecraft:peony";
        peonyDrop.count = new CountRange(1, 1);
        peony.drops.add(peonyDrop);

        crops.add(peony);

        CropEntry roseBush = new CropEntry();
        roseBush.seed = "minecraft:rose_bush";
        roseBush.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        roseBush.drops = new ArrayList<>();

        DropEntry roseBushDrop = new DropEntry();
        roseBushDrop.item = "minecraft:rose_bush";
        roseBushDrop.count = new CountRange(1, 1);
        roseBush.drops.add(roseBushDrop);

        crops.add(roseBush);

        CropEntry sunflower = new CropEntry();
        sunflower.seed = "minecraft:sunflower";
        sunflower.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",

                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",

                "mysticalagradditions:insanium_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch",

                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",

                "farmersdelight:rich_soil_farmland"
        );
        sunflower.drops = new ArrayList<>();

        DropEntry sunflowerDrop = new DropEntry();
        sunflowerDrop.item = "minecraft:sunflower";
        sunflowerDrop.count = new CountRange(1, 1);
        sunflower.drops.add(sunflowerDrop);

        crops.add(sunflower);

        // Flowers END

    }

    private static void addVanillaSoils(List<SoilEntry> soils) {
        SoilEntry dirt = new SoilEntry();
        dirt.soil = "minecraft:dirt";
        dirt.growthModifier = 0.475f;
        soils.add(dirt);

        SoilEntry coarseDirt = new SoilEntry();
        coarseDirt.soil = "minecraft:coarse_dirt";
        coarseDirt.growthModifier = 0.475f;
        soils.add(coarseDirt);

        SoilEntry podzol = new SoilEntry();
        podzol.soil = "minecraft:podzol";
        podzol.growthModifier = 0.475f;
        soils.add(podzol);

        SoilEntry mycelium = new SoilEntry();
        mycelium.soil = "minecraft:mycelium";
        mycelium.growthModifier = 0.475f;
        soils.add(mycelium);

        SoilEntry mud = new SoilEntry();
        mud.soil = "minecraft:mud";
        mud.growthModifier = 0.5f;
        soils.add(mud);

        SoilEntry muddyMangroveRoots = new SoilEntry();
        muddyMangroveRoots.soil = "minecraft:muddy_mangrove_roots";
        muddyMangroveRoots.growthModifier = 0.5f;
        soils.add(muddyMangroveRoots);

        SoilEntry rootedDirt = new SoilEntry();
        rootedDirt.soil = "minecraft:rooted_dirt";
        rootedDirt.growthModifier = 0.475f;
        soils.add(rootedDirt);

        SoilEntry moss = new SoilEntry();
        moss.soil = "minecraft:moss_block";
        moss.growthModifier = 0.475f;
        soils.add(moss);

        SoilEntry farmland = new SoilEntry();
        farmland.soil = "minecraft:farmland";
        farmland.growthModifier = 0.5f;
        soils.add(farmland);

        SoilEntry sand = new SoilEntry();
        sand.soil = "minecraft:sand";
        sand.growthModifier = 0.5f;
        soils.add(sand);

        SoilEntry redSand = new SoilEntry();
        redSand.soil = "minecraft:red_sand";
        redSand.growthModifier = 0.5f;
        soils.add(redSand);

        SoilEntry grass = new SoilEntry();
        grass.soil = "minecraft:grass_block";
        grass.growthModifier = 0.475f;
        soils.add(grass);

        SoilEntry soulSand = new SoilEntry();
        soulSand.soil = "minecraft:soul_sand";
        soulSand.growthModifier = 0.5f;
        soils.add(soulSand);

        SoilEntry endStone = new SoilEntry();
        endStone.soil = "minecraft:end_stone";
        endStone.growthModifier = 0.5f;
        soils.add(endStone);

        SoilEntry jungleLog = new SoilEntry();
        jungleLog.soil = "minecraft:jungle_log";
        jungleLog.growthModifier = 0.5f;
        soils.add(jungleLog);

        SoilEntry jungleWood = new SoilEntry();
        jungleWood.soil = "minecraft:jungle_wood";
        jungleWood.growthModifier = 0.5f;
        soils.add(jungleWood);

        SoilEntry strippedJungleLog = new SoilEntry();
        strippedJungleLog.soil = "minecraft:stripped_jungle_log";
        strippedJungleLog.growthModifier = 0.5f;
        soils.add(strippedJungleLog);

        SoilEntry strippedJungleWood = new SoilEntry();
        strippedJungleWood.soil = "minecraft:stripped_jungle_wood";
        strippedJungleWood.growthModifier = 0.5f;
        soils.add(strippedJungleWood);

        SoilEntry infusedFarmland = new SoilEntry();
        infusedFarmland.soil = "agritechevolved:infused_farmland";
        infusedFarmland.growthModifier = 2.0f;
        soils.add(infusedFarmland);

        SoilEntry agritechEvolvedMulch = new SoilEntry();
        agritechEvolvedMulch.soil = "agritechevolved:mulch";
        agritechEvolvedMulch.growthModifier = 1.5f;
        soils.add(agritechEvolvedMulch);
    }

    private static void addMysticalAgricultureCrops(List<CropEntry> crops) {
        // Tier 1 crops - can grow on any valid MA farmland
        String[] tier1Seeds = {
                "mysticalagriculture:air_seeds",
                "mysticalagriculture:earth_seeds",
                "mysticalagriculture:water_seeds",
                "mysticalagriculture:fire_seeds",
                "mysticalagriculture:inferium_seeds",
                "mysticalagriculture:stone_seeds",
                "mysticalagriculture:dirt_seeds",
                "mysticalagriculture:wood_seeds",
                "mysticalagriculture:ice_seeds",
                "mysticalagriculture:deepslate_seeds"
        };

        List<String> tier1Soils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",

                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );

        for (String seedId : tier1Seeds) {
            addMysticalAgricultureCrop(crops, seedId, tier1Soils);
        }

        // Tier 2 crops - require at least prudentium farmland
        String[] tier2Seeds = {
                "mysticalagriculture:nature_seeds",
                "mysticalagriculture:dye_seeds",
                "mysticalagriculture:nether_seeds",
                "mysticalagriculture:coal_seeds",
                "mysticalagriculture:coral_seeds",
                "mysticalagriculture:honey_seeds",
                "mysticalagriculture:amethyst_seeds",
                "mysticalagriculture:pig_seeds",
                "mysticalagriculture:chicken_seeds",
                "mysticalagriculture:cow_seeds",
                "mysticalagriculture:sheep_seeds",
                "mysticalagriculture:squid_seeds",
                "mysticalagriculture:fish_seeds",
                "mysticalagriculture:slime_seeds",
                "mysticalagriculture:turtle_seeds",
                "mysticalagriculture:armadillo_seeds",
                "mysticalagriculture:rubber_seeds",
                "mysticalagriculture:silicon_seeds",
                "mysticalagriculture:sulfur_seeds",
                "mysticalagriculture:aluminum_seeds",
                "mysticalagriculture:saltpeter_seeds",
                "mysticalagriculture:apatite_seeds",
                "mysticalagriculture:grains_of_infinity_seeds",
                "mysticalagriculture:mystical_flower_seeds",
                "mysticalagriculture:marble_seeds",
                "mysticalagriculture:limestone_seeds",
                "mysticalagriculture:basalt_seeds",
                "mysticalagriculture:menril_seeds"
        };

        List<String> tier2Soils = List.of(
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );

        for (String seedId : tier2Seeds) {
            addMysticalAgricultureCrop(crops, seedId, tier2Soils);
        }

        // Tier 3 crops - require at least tertium farmland
        String[] tier3Seeds = {
                "mysticalagriculture:iron_seeds",
                "mysticalagriculture:copper_seeds",
                "mysticalagriculture:nether_quartz_seeds",
                "mysticalagriculture:glowstone_seeds",
                "mysticalagriculture:redstone_seeds",
                "mysticalagriculture:obsidian_seeds",
                "mysticalagriculture:prismarine_seeds",
                "mysticalagriculture:zombie_seeds",
                "mysticalagriculture:skeleton_seeds",
                "mysticalagriculture:creeper_seeds",
                "mysticalagriculture:spider_seeds",
                "mysticalagriculture:rabbit_seeds",
                "mysticalagriculture:tin_seeds",
                "mysticalagriculture:bronze_seeds",
                "mysticalagriculture:zinc_seeds",
                "mysticalagriculture:brass_seeds",
                "mysticalagriculture:silver_seeds",
                "mysticalagriculture:lead_seeds",
                "mysticalagriculture:graphite_seeds",
                "mysticalagriculture:blizz_seeds",
                "mysticalagriculture:blitz_seeds",
                "mysticalagriculture:basalz_seeds",
                "mysticalagriculture:amethyst_bronze_seeds",
                "mysticalagriculture:slimesteel_seeds",
                "mysticalagriculture:pig_iron_seeds",
                "mysticalagriculture:copper_alloy_seeds",
                "mysticalagriculture:redstone_alloy_seeds",
                "mysticalagriculture:conductive_alloy_seeds",
                "mysticalagriculture:steeleaf_seeds",
                "mysticalagriculture:ironwood_seeds",
                "mysticalagriculture:sky_stone_seeds",
                "mysticalagriculture:certus_quartz_seeds",
                "mysticalagriculture:quartz_enriched_iron_seeds",
                "mysticalagriculture:manasteel_seeds",
                "mysticalagriculture:aquamarine_seeds"
        };

        List<String> tier3Soils = List.of(
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );

        for (String seedId : tier3Seeds) {
            addMysticalAgricultureCrop(crops, seedId, tier3Soils);
        }

        // Tier 4 crops - require at least imperium farmland
        String[] tier4Seeds = {
                "mysticalagriculture:gold_seeds",
                "mysticalagriculture:lapis_lazuli_seeds",
                "mysticalagriculture:end_seeds",
                "mysticalagriculture:experience_seeds",
                "mysticalagriculture:breeze_seeds",
                "mysticalagriculture:blaze_seeds",
                "mysticalagriculture:ghast_seeds",
                "mysticalagriculture:enderman_seeds",
                "mysticalagriculture:steel_seeds",
                "mysticalagriculture:nickel_seeds",
                "mysticalagriculture:constantan_seeds",
                "mysticalagriculture:electrum_seeds",
                "mysticalagriculture:invar_seeds",
                "mysticalagriculture:uranium_seeds",
                "mysticalagriculture:ruby_seeds",
                "mysticalagriculture:sapphire_seeds",
                "mysticalagriculture:peridot_seeds",
                "mysticalagriculture:soulium_seeds",
                "mysticalagriculture:signalum_seeds",
                "mysticalagriculture:lumium_seeds",
                "mysticalagriculture:flux_infused_ingot_seeds",
                "mysticalagriculture:hop_graphite_seeds",
                "mysticalagriculture:cobalt_seeds",
                "mysticalagriculture:rose_gold_seeds",
                "mysticalagriculture:soularium_seeds",
                "mysticalagriculture:dark_steel_seeds",
                "mysticalagriculture:pulsating_alloy_seeds",
                "mysticalagriculture:energetic_alloy_seeds",
                "mysticalagriculture:elementium_seeds",
                "mysticalagriculture:osmium_seeds",
                "mysticalagriculture:fluorite_seeds",
                "mysticalagriculture:refined_glowstone_seeds",
                "mysticalagriculture:refined_obsidian_seeds",
                "mysticalagriculture:knightmetal_seeds",
                "mysticalagriculture:fiery_ingot_seeds",
                "mysticalagriculture:compressed_iron_seeds",
                "mysticalagriculture:starmetal_seeds",
                "mysticalagriculture:fluix_seeds",
                "mysticalagriculture:energized_steel_seeds",
                "mysticalagriculture:blazing_crystal_seeds"
        };

        List<String> tier4Soils = List.of(
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );

        for (String seedId : tier4Seeds) {
            addMysticalAgricultureCrop(crops, seedId, tier4Soils);
        }

        // Tier 5 crops - require supremium farmland
        String[] tier5Seeds = {
                "mysticalagriculture:diamond_seeds",
                "mysticalagriculture:emerald_seeds",
                "mysticalagriculture:netherite_seeds",
                "mysticalagriculture:wither_skeleton_seeds",
                "mysticalagriculture:platinum_seeds",
                "mysticalagriculture:iridium_seeds",
                "mysticalagriculture:enderium_seeds",
                "mysticalagriculture:flux_infused_gem_seeds",
                "mysticalagriculture:manyullyn_seeds",
                "mysticalagriculture:queens_slime_seeds",
                "mysticalagriculture:hepatizon_seeds",
                "mysticalagriculture:vibrant_alloy_seeds",
                "mysticalagriculture:end_steel_seeds",
                "mysticalagriculture:terrasteel_seeds",
                "mysticalagriculture:rock_crystal_seeds",
                "mysticalagriculture:draconium_seeds",
                "mysticalagriculture:yellorium_seeds",
                "mysticalagriculture:cyanite_seeds",
                "mysticalagriculture:niotic_crystal_seeds",
                "mysticalagriculture:spirited_crystal_seeds",
                "mysticalagriculture:uraninite_seeds",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        };

        List<String> tier5Soils = List.of(
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier4"
        );

        for (String seedId : tier5Seeds) {
            addMysticalAgricultureCrop(crops, seedId, tier5Soils);
        }

        // Special crops that require cruxes
        if (Config.enableMysticalAgradditions) {
            addSpecialCruxCrop(crops, "mysticalagriculture:nether_star_seeds", "mysticalagradditions:nether_star_crux");
            addSpecialCruxCrop(crops, "mysticalagriculture:dragon_egg_seeds", "mysticalagradditions:dragon_egg_crux");
            addSpecialCruxCrop(crops, "mysticalagriculture:gaia_spirit_seeds", "mysticalagradditions:gaia_spirit_crux");
            addSpecialCruxCrop(crops, "mysticalagriculture:awakened_draconium_seeds", "mysticalagradditions:awakened_draconium_crux");
            addSpecialCruxCrop(crops, "mysticalagriculture:neutronium_seeds", "mysticalagradditions:neutronium_crux");
            addSpecialCruxCrop(crops, "mysticalagriculture:nitro_crystal_seeds", "mysticalagradditions:nitro_crystal_crux");
        }
    }

    private static void addSpecialCruxCrop(List<CropEntry> crops, String seedId, String cruxId) {
        CropEntry crop = new CropEntry();
        crop.seed = seedId;
        crop.validSoils = List.of(cruxId);
        crop.drops = new ArrayList<>();

        String essence = seedId.replace("_seeds", "_essence");

        DropEntry essenceDrop = new DropEntry();
        essenceDrop.item = essence;
        essenceDrop.count = new CountRange(1, 1);
        essenceDrop.chance = 1.0f;
        crop.drops.add(essenceDrop);

        crops.add(crop);
    }

    private static void addMysticalAgricultureCrop(List<CropEntry> crops, String seedId, List<String> validSoils) {
        CropEntry crop = new CropEntry();
        crop.seed = seedId;
        crop.validSoils = new ArrayList<>(validSoils);
        crop.drops = new ArrayList<>();

        String essence = seedId.replace("_seeds", "_essence");

        DropEntry essenceDrop = new DropEntry();
        essenceDrop.item = essence;
        essenceDrop.count = new CountRange(1, 1);
        essenceDrop.chance = 1.0f;
        crop.drops.add(essenceDrop);

        DropEntry seedDrop = new DropEntry();
        seedDrop.item = seedId;
        seedDrop.count = new CountRange(1, 1);
        seedDrop.chance = 0.2f;
        crop.drops.add(seedDrop);

        crops.add(crop);
    }

    private static void addMysticalAgricultureSoils(List<SoilEntry> soils) {
        // Add MA soils with appropriate growth modifiers
        SoilEntry inferiumFarmland = new SoilEntry();
        inferiumFarmland.soil = "mysticalagriculture:inferium_farmland";
        inferiumFarmland.growthModifier = 0.55f;
        soils.add(inferiumFarmland);

        SoilEntry prudentiumFarmland = new SoilEntry();
        prudentiumFarmland.soil = "mysticalagriculture:prudentium_farmland";
        prudentiumFarmland.growthModifier = 0.625f;
        soils.add(prudentiumFarmland);

        SoilEntry tertiumFarmland = new SoilEntry();
        tertiumFarmland.soil = "mysticalagriculture:tertium_farmland";
        tertiumFarmland.growthModifier = 0.75f;
        soils.add(tertiumFarmland);

        SoilEntry imperiumFarmland = new SoilEntry();
        imperiumFarmland.soil = "mysticalagriculture:imperium_farmland";
        imperiumFarmland.growthModifier = 0.875f;
        soils.add(imperiumFarmland);

        SoilEntry supremiumFarmland = new SoilEntry();
        supremiumFarmland.soil = "mysticalagriculture:supremium_farmland";
        supremiumFarmland.growthModifier = 1f;
        soils.add(supremiumFarmland);

        if (Config.enableMysticalAgradditions) {
            SoilEntry insaniumFarmland = new SoilEntry();
            insaniumFarmland.soil = "mysticalagradditions:insanium_farmland";
            insaniumFarmland.growthModifier = 1.75f;
            soils.add(insaniumFarmland);

            SoilEntry netherStarCrux = new SoilEntry();
            netherStarCrux.soil = "mysticalagradditions:nether_star_crux";
            netherStarCrux.growthModifier = 1.75f;
            soils.add(netherStarCrux);

            SoilEntry dragonEggCrux = new SoilEntry();
            dragonEggCrux.soil = "mysticalagradditions:dragon_egg_crux";
            dragonEggCrux.growthModifier = 1.75f;
            soils.add(dragonEggCrux);

            SoilEntry awakenedDraconiumCrux = new SoilEntry();
            awakenedDraconiumCrux.soil = "mysticalagradditions:awakened_draconium_crux";
            awakenedDraconiumCrux.growthModifier = 1.75f;
            soils.add(awakenedDraconiumCrux);

            SoilEntry neutroniumCrux = new SoilEntry();
            neutroniumCrux.soil = "mysticalagradditions:neutronium_crux";
            neutroniumCrux.growthModifier = 1.75f;
            soils.add(neutroniumCrux);

            SoilEntry nitroCrystalCrux = new SoilEntry();
            nitroCrystalCrux.soil = "mysticalagradditions:nitro_crystal_crux";
            nitroCrystalCrux.growthModifier = 1.75f;
            soils.add(nitroCrystalCrux);
        }
    }

    private static void addFarmersDelightCrops(List<CropEntry> crops) {
        // Cabbage
        CropEntry cabbage = new CropEntry();
        cabbage.seed = "farmersdelight:cabbage_seeds";
        cabbage.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        cabbage.drops = new ArrayList<>();

        DropEntry cabbageDrop = new DropEntry();
        cabbageDrop.item = "farmersdelight:cabbage";
        cabbageDrop.count = new CountRange(1, 1);
        cabbageDrop.chance = 1.0f;
        cabbage.drops.add(cabbageDrop);

        DropEntry cabbageSeedsDrop = new DropEntry();
        cabbageSeedsDrop.item = "farmersdelight:cabbage_seeds";
        cabbageSeedsDrop.count = new CountRange(1, 2);
        cabbageSeedsDrop.chance = 1.0f;
        cabbage.drops.add(cabbageSeedsDrop);

        crops.add(cabbage);

        // Tomato
        CropEntry tomato = new CropEntry();
        tomato.seed = "farmersdelight:tomato_seeds";
        tomato.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        tomato.drops = new ArrayList<>();

        DropEntry tomatoDrop = new DropEntry();
        tomatoDrop.item = "farmersdelight:tomato";
        tomatoDrop.count = new CountRange(1, 2);
        tomatoDrop.chance = 1.0f;
        tomato.drops.add(tomatoDrop);

        crops.add(tomato);

        // Onion
        CropEntry onion = new CropEntry();
        onion.seed = "farmersdelight:onion";
        onion.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        onion.drops = new ArrayList<>();

        DropEntry onionDrop = new DropEntry();
        onionDrop.item = "farmersdelight:onion";
        onionDrop.count = new CountRange(1, 3);
        onionDrop.chance = 1.0f;
        onion.drops.add(onionDrop);

        crops.add(onion);
    }

    private static void addFarmersDelightSoils(List<SoilEntry> soils) {
        SoilEntry richSoil = new SoilEntry();
        richSoil.soil = "farmersdelight:rich_soil";
        richSoil.growthModifier = 0.525f;
        soils.add(richSoil);

        SoilEntry richSoilFarmland = new SoilEntry();
        richSoilFarmland.soil = "farmersdelight:rich_soil_farmland";
        richSoilFarmland.growthModifier = 0.525f;
        soils.add(richSoilFarmland);

        SoilEntry organicCompost = new SoilEntry();
        organicCompost.soil = "farmersdelight:organic_compost";
        organicCompost.growthModifier = 0.525f;
        soils.add(organicCompost);
    }

    private static void addImmersiveEngineering(List<CropEntry> crops) {
        // Immersive Engineering Hemp Fiber
        CropEntry immersiveHempFiber = new CropEntry();
        immersiveHempFiber.seed = "immersiveengineering:seed";
        immersiveHempFiber.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        immersiveHempFiber.drops = new ArrayList<>();

        DropEntry immersiveHempDrop = new DropEntry();
        immersiveHempDrop.item = "immersiveengineering:hemp_fiber";
        immersiveHempDrop.count = new CountRange(4, 8);
        immersiveHempDrop.chance = 1.0f;
        immersiveHempFiber.drops.add(immersiveHempDrop);

        DropEntry immersiveSeedDrop = new DropEntry();
        immersiveSeedDrop.item = "immersiveengineering:seed";
        immersiveSeedDrop.count = new CountRange(1, 1);
        immersiveSeedDrop.chance = 0.5f;
        immersiveHempFiber.drops.add(immersiveSeedDrop);

        crops.add(immersiveHempFiber);

    }

    private static void addArsNouveauCrops(List<CropEntry> crops) {
        // Magebloom
        CropEntry magebloom = new CropEntry();
        magebloom.seed = "ars_nouveau:magebloom_crop";
        magebloom.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        magebloom.drops = new ArrayList<>();

        DropEntry magebloomDrop = new DropEntry();
        magebloomDrop.item = "ars_nouveau:magebloom";
        magebloomDrop.count = new CountRange(1, 1);
        magebloomDrop.chance = 1.0f;
        magebloom.drops.add(magebloomDrop);

        crops.add(magebloom);

        // Sourceberry Bush
        CropEntry sourceberry = new CropEntry();
        sourceberry.seed = "ars_nouveau:sourceberry_bush";
        sourceberry.validSoils = List.of(
                "minecraft:farmland",
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:rooted_dirt",
                "minecraft:coarse_dirt",
                "minecraft:podzol",
                "minecraft:mycelium",
                "minecraft:mud",
                "minecraft:moss_block",
                "minecraft:muddy_mangrove_roots",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "farmersdelight:organic_compost",
                "farmersdelight:rich_soil",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        sourceberry.drops = new ArrayList<>();

        DropEntry sourceberryDrop = new DropEntry();
        sourceberryDrop.item = "ars_nouveau:sourceberry_bush";
        sourceberryDrop.count = new CountRange(2, 4);
        sourceberryDrop.chance = 1.0f;
        sourceberry.drops.add(sourceberryDrop);

        crops.add(sourceberry);
    }

    private static void addSilentGearCrops(List<CropEntry> crops) {
        // Fluffy Puff
        CropEntry fluffyPuff = new CropEntry();
        fluffyPuff.seed = "silentgear:fluffy_seeds";
        fluffyPuff.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        fluffyPuff.drops = new ArrayList<>();

        DropEntry fluffyPuffDrop = new DropEntry();
        fluffyPuffDrop.item = "silentgear:fluffy_puff";
        fluffyPuffDrop.count = new CountRange(1, 4);
        fluffyPuffDrop.chance = 1.0f;
        fluffyPuff.drops.add(fluffyPuffDrop);

        DropEntry fluffySeedsDrop = new DropEntry();
        fluffySeedsDrop.item = "silentgear:fluffy_seeds";
        fluffySeedsDrop.count = new CountRange(1, 1);
        fluffySeedsDrop.chance = 1.0f;
        fluffyPuff.drops.add(fluffySeedsDrop);

        crops.add(fluffyPuff);

        // Flax
        CropEntry flax = new CropEntry();
        flax.seed = "silentgear:flax_seeds";
        flax.validSoils = List.of(
                "minecraft:farmland",
                "mysticalagriculture:inferium_farmland",
                "mysticalagriculture:prudentium_farmland",
                "mysticalagriculture:tertium_farmland",
                "mysticalagriculture:imperium_farmland",
                "mysticalagriculture:supremium_farmland",
                "mysticalagradditions:insanium_farmland",
                "justdirethings:goosoil_tier1",
                "justdirethings:goosoil_tier2",
                "justdirethings:goosoil_tier3",
                "justdirethings:goosoil_tier4",
                "farmersdelight:rich_soil_farmland",
                "agritechevolved:infused_farmland",
                "agritechevolved:mulch"
        );
        flax.drops = new ArrayList<>();

        DropEntry flaxFiberDrop = new DropEntry();
        flaxFiberDrop.item = "silentgear:flax_fiber";
        flaxFiberDrop.count = new CountRange(1, 4);
        flaxFiberDrop.chance = 1.0f;
        flax.drops.add(flaxFiberDrop);

        DropEntry flaxSeedsDrop = new DropEntry();
        flaxSeedsDrop.item = "silentgear:flax_seeds";
        flaxSeedsDrop.count = new CountRange(1, 1);
        flaxSeedsDrop.chance = 0.2f;
        flax.drops.add(flaxSeedsDrop);

        DropEntry flaxFlowersDrop = new DropEntry();
        flaxFlowersDrop.item = "silentgear:flax_flowers";
        flaxFlowersDrop.count = new CountRange(1, 1);
        flaxFlowersDrop.chance = 0.2f;
        flax.drops.add(flaxFlowersDrop);

        crops.add(flax);
    }

    private static void addJustDireThingsSoils(List<SoilEntry> soils) {
        SoilEntry goosoilTier1 = new SoilEntry();
        goosoilTier1.soil = "justdirethings:goosoil_tier1";
        goosoilTier1.growthModifier = 0.575f;
        soils.add(goosoilTier1);

        SoilEntry goosoilTier2 = new SoilEntry();
        goosoilTier2.soil = "justdirethings:goosoil_tier2";
        goosoilTier2.growthModifier = 0.75f;
        soils.add(goosoilTier2);

        SoilEntry goosoilTier3 = new SoilEntry();
        goosoilTier3.soil = "justdirethings:goosoil_tier3";
        goosoilTier3.growthModifier = 1.0f;
        soils.add(goosoilTier3);

        SoilEntry goosoilTier4 = new SoilEntry();
        goosoilTier4.soil = "justdirethings:goosoil_tier4";
        goosoilTier4.growthModifier = 1.5f;
        soils.add(goosoilTier4);
    }

    private static void processConfig(CropConfigData configData) {
        crops.clear();
        soils.clear();

        if (configData.allowedCrops != null) {
            for (CropEntry entry : configData.allowedCrops) {
                if (entry.seed != null && !entry.seed.isEmpty()) {
                    CropInfo cropInfo = new CropInfo();
                    cropInfo.drops = new ArrayList<>();

                    if (entry.validSoils != null && !entry.validSoils.isEmpty()) {
                        cropInfo.validSoils.addAll(entry.validSoils);
                    } else if (entry.soil != null && !entry.soil.isEmpty()) {
                        cropInfo.validSoils.add(entry.soil);
                    }

                    if (entry.drops != null) {
                        for (DropEntry dropEntry : entry.drops) {
                            DropInfo dropInfo = new DropInfo(
                                    dropEntry.item,
                                    dropEntry.count != null ? dropEntry.count.min : 1,
                                    dropEntry.count != null ? dropEntry.count.max : 1,
                                    dropEntry.chance
                            );
                            cropInfo.drops.add(dropInfo);
                        }
                    }

                    crops.put(entry.seed, cropInfo);
                }
            }
        }

        if (configData.allowedSoils != null) {
            for (SoilEntry entry : configData.allowedSoils) {
                if (entry.soil != null && !entry.soil.isEmpty()) {
                    soils.put(entry.soil, new SoilInfo(entry.growthModifier));
                }
            }
        }

        LOGGER.info("Loaded {} crops and {} soils from config", crops.size(), soils.size());
    }

    public static boolean isSoilValidForSeed(String soilId, String seedId) {
        CropInfo cropInfo = crops.get(seedId);
        if (cropInfo == null || cropInfo.validSoils.isEmpty()) {
            return false;
        }

        return cropInfo.validSoils.contains(soilId);
    }


    public static boolean isValidSeed(String itemId) {
        return crops.containsKey(itemId);
    }

    public static boolean isValidSoil(String blockId) {
        return soils.containsKey(blockId);
    }

    public static float getSoilGrowthModifier(String blockId) {
        SoilInfo info = soils.get(blockId);
        return info != null ? info.growthModifier : 1.0f;
    }

    public static List<DropInfo> getCropDrops(String seedId) {
        CropInfo info = crops.get(seedId);
        return info != null ? info.drops : Collections.emptyList();
    }

    public static class CropConfigData {
        public List<CropEntry> allowedCrops;
        public List<SoilEntry> allowedSoils;
    }

    public static class CropEntry {
        public String seed;
        public String soil;
        public List<String> validSoils;
        public List<DropEntry> drops;
    }

    public static class DropEntry {
        public String item;
        public CountRange count;
        public float chance = 1.0f;
    }

    public static class CountRange {
        public int min;
        public int max;

        public CountRange() {
            this.min = 1;
            this.max = 1;
        }

        public CountRange(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }

    public static class SoilEntry {
        public String soil;
        public float growthModifier;
    }

    public static class CropInfo {
        public List<DropInfo> drops;
        public List<String> validSoils = new ArrayList<>();
    }

    public static class DropInfo {
        public final String item;
        public final int minCount;
        public final int maxCount;
        public final float chance;

        public DropInfo(String item, int minCount, int maxCount, float chance) {
            this.item = item;
            this.minCount = minCount;
            this.maxCount = maxCount;
            this.chance = chance;
        }
    }

    public static class SoilInfo {
        public final float growthModifier;

        public SoilInfo(float growthModifier) {
            this.growthModifier = growthModifier;
        }
    }

    public static Map<String, List<String>> getAllSeedToSoilMappings() {
        Map<String, List<String>> seedToSoilMap = new HashMap<>();

        for (Map.Entry<String, CropInfo> entry : crops.entrySet()) {
            String seedId = entry.getKey();
            CropInfo cropInfo = entry.getValue();

            if (!cropInfo.validSoils.isEmpty()) {
                seedToSoilMap.put(seedId, new ArrayList<>(cropInfo.validSoils));
            }
        }

        return seedToSoilMap;
    }
}