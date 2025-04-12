package com.blocklogic.agritech.config;

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
        CropConfigData config = new CropConfigData();

        List<CropEntry> defaultCrops = new ArrayList<>();

        CropEntry wheat = new CropEntry();
        wheat.seed = "minecraft:wheat_seeds";
        wheat.validSoils = List.of("minecraft:farmland");
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

        defaultCrops.add(wheat);

        CropEntry beetroot = new CropEntry();
        beetroot.seed = "minecraft:beetroot_seeds";
        beetroot.validSoils = List.of("minecraft:farmland");
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

        defaultCrops.add(beetroot);

        CropEntry carrot = new CropEntry();
        carrot.seed = "minecraft:carrot";
        carrot.validSoils = List.of("minecraft:farmland");
        carrot.drops = new ArrayList<>();

        DropEntry carrotDrop = new DropEntry();
        carrotDrop.item = "minecraft:carrot";
        carrotDrop.count = new CountRange(2, 5);
        carrot.drops.add(carrotDrop);

        defaultCrops.add(carrot);

        CropEntry potato = new CropEntry();
        potato.seed = "minecraft:potato";
        potato.validSoils = List.of("minecraft:farmland");
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

        defaultCrops.add(potato);

        CropEntry melon = new CropEntry();
        melon.seed = "minecraft:melon_seeds";
        melon.validSoils = List.of("minecraft:farmland");
        melon.drops = new ArrayList<>();

        DropEntry melonSliceDrop = new DropEntry();
        melonSliceDrop.item = "minecraft:melon_slice";
        melonSliceDrop.count = new CountRange(3, 7);
        melon.drops.add(melonSliceDrop);

        defaultCrops.add(melon);

        CropEntry pumpkin = new CropEntry();
        pumpkin.seed = "minecraft:pumpkin_seeds";
        pumpkin.validSoils = List.of("minecraft:farmland");
        pumpkin.drops = new ArrayList<>();

        DropEntry pumpkinDrop = new DropEntry();
        pumpkinDrop.item = "minecraft:pumpkin";
        pumpkinDrop.count = new CountRange(1, 1);
        pumpkin.drops.add(pumpkinDrop);

        defaultCrops.add(pumpkin);

        CropEntry sugarCane = new CropEntry();
        sugarCane.seed = "minecraft:sugar_cane";
        sugarCane.validSoils = List.of(
                "minecraft:dirt",
                "minecraft:grass_block",
                "minecraft:sand",
                "minecraft:red_sand"
        );
        sugarCane.drops = new ArrayList<>();

        DropEntry sugarCaneDrop = new DropEntry();
        sugarCaneDrop.item = "minecraft:sugar_cane";
        sugarCaneDrop.count = new CountRange(1, 3);
        sugarCane.drops.add(sugarCaneDrop);

        defaultCrops.add(sugarCane);

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

        defaultCrops.add(cactus);

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
                "minecraft:muddy_mangrove_roots"
        );
        bamboo.drops = new ArrayList<>();

        DropEntry bambooDrop = new DropEntry();
        bambooDrop.item = "minecraft:bamboo";
        bambooDrop.count = new CountRange(2, 4);
        bamboo.drops.add(bambooDrop);

        defaultCrops.add(bamboo);

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
                "minecraft:muddy_mangrove_roots"
        );
        sweetBerries.drops = new ArrayList<>();

        DropEntry sweetBerriesDrop = new DropEntry();
        sweetBerriesDrop.item = "minecraft:sweet_berries";
        sweetBerriesDrop.count = new CountRange(2, 4);
        sweetBerries.drops.add(sweetBerriesDrop);

        defaultCrops.add(sweetBerries);

        CropEntry netherWart = new CropEntry();
        netherWart.seed = "minecraft:nether_wart";
        netherWart.validSoils = List.of("minecraft:soul_sand");
        netherWart.drops = new ArrayList<>();

        DropEntry netherWartDrop = new DropEntry();
        netherWartDrop.item = "minecraft:nether_wart";
        netherWartDrop.count = new CountRange(1, 3);
        netherWart.drops.add(netherWartDrop);

        defaultCrops.add(netherWart);

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

        defaultCrops.add(chorusFlower);

        CropEntry kelp = new CropEntry();
        kelp.seed = "minecraft:kelp";
        kelp.validSoils = List.of("minecraft:mud");
        kelp.drops = new ArrayList<>();

        DropEntry kelpDrop = new DropEntry();
        kelpDrop.item = "minecraft:kelp";
        kelpDrop.count = new CountRange(1, 2);
        kelp.drops.add(kelpDrop);

        defaultCrops.add(kelp);

        CropEntry brownMushroom = new CropEntry();
        brownMushroom.seed = "minecraft:brown_mushroom";
        brownMushroom.validSoils = List.of(
                "minecraft:mycelium",
                "minecraft:podzol"
        );
        brownMushroom.drops = new ArrayList<>();

        DropEntry brownMushroomDrop = new DropEntry();
        brownMushroomDrop.item = "minecraft:brown_mushroom";
        brownMushroomDrop.count = new CountRange(1, 1);
        brownMushroom.drops.add(brownMushroomDrop);

        defaultCrops.add(brownMushroom);

        CropEntry redMushroom = new CropEntry();
        redMushroom.seed = "minecraft:red_mushroom";
        redMushroom.validSoils = List.of(
                "minecraft:mycelium",
                "minecraft:podzol"
        );
        redMushroom.drops = new ArrayList<>();

        DropEntry redMushroomDrop = new DropEntry();
        redMushroomDrop.item = "minecraft:red_mushroom";
        redMushroomDrop.count = new CountRange(1, 1);
        redMushroom.drops.add(redMushroomDrop);

        defaultCrops.add(redMushroom);

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

        defaultCrops.add(cocoa);

        CropEntry pitcherCrop = new CropEntry();
        pitcherCrop.seed = "minecraft:pitcher_pod";
        pitcherCrop.validSoils = List.of("minecraft:farmland");
        pitcherCrop.drops = new ArrayList<>();

        DropEntry pitcherCropDrop = new DropEntry();
        pitcherCropDrop.item = "minecraft:pitcher_plant";
        pitcherCropDrop.count = new CountRange(1, 1);
        pitcherCrop.drops.add(pitcherCropDrop);

        defaultCrops.add(pitcherCrop);

        CropEntry torchFlower = new CropEntry();
        torchFlower.seed = "minecraft:torchflower_seeds";
        torchFlower.validSoils = List.of("minecraft:farmland");
        torchFlower.drops = new ArrayList<>();

        DropEntry torchFlowerDrop = new DropEntry();
        torchFlowerDrop.item = "minecraft:torchflower";
        torchFlowerDrop.count = new CountRange(1, 1);
        torchFlower.drops.add(torchFlowerDrop);

        defaultCrops.add(torchFlower);

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
                "minecraft:muddy_mangrove_roots"
        );
        allium.drops = new ArrayList<>();

        DropEntry alliumDrop = new DropEntry();
        alliumDrop.item = "minecraft:allium";
        alliumDrop.count = new CountRange(1, 1);
        allium.drops.add(alliumDrop);

        defaultCrops.add(allium);

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
                "minecraft:muddy_mangrove_roots"
        );
        azureBluet.drops = new ArrayList<>();

        DropEntry azureBluetDrop = new DropEntry();
        azureBluetDrop.item = "minecraft:azure_bluet";
        azureBluetDrop.count = new CountRange(1, 1);
        azureBluet.drops.add(azureBluetDrop);

        defaultCrops.add(azureBluet);

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
                "minecraft:muddy_mangrove_roots"
        );
        blueOrchid.drops = new ArrayList<>();

        DropEntry blueOrchidDrop = new DropEntry();
        blueOrchidDrop.item = "minecraft:blue_orchid";
        blueOrchidDrop.count = new CountRange(1, 1);
        blueOrchid.drops.add(blueOrchidDrop);

        defaultCrops.add(blueOrchid);

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
                "minecraft:muddy_mangrove_roots"
        );
        cornflower.drops = new ArrayList<>();

        DropEntry cornflowerDrop = new DropEntry();
        cornflowerDrop.item = "minecraft:cornflower";
        cornflowerDrop.count = new CountRange(1, 1);
        cornflower.drops.add(cornflowerDrop);

        defaultCrops.add(cornflower);

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
                "minecraft:muddy_mangrove_roots"
        );
        dandelion.drops = new ArrayList<>();

        DropEntry dandelionDrop = new DropEntry();
        dandelionDrop.item = "minecraft:dandelion";
        dandelionDrop.count = new CountRange(1, 1);
        dandelion.drops.add(dandelionDrop);

        defaultCrops.add(dandelion);

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
                "minecraft:muddy_mangrove_roots"
        );
        lilyOfTheValley.drops = new ArrayList<>();

        DropEntry lilyOfTheValleyDrop = new DropEntry();
        lilyOfTheValleyDrop.item = "minecraft:lily_of_the_valley";
        lilyOfTheValleyDrop.count = new CountRange(1, 1);
        lilyOfTheValley.drops.add(lilyOfTheValleyDrop);

        defaultCrops.add(lilyOfTheValley);

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
                "minecraft:muddy_mangrove_roots"
        );
        oxeyeDaisy.drops = new ArrayList<>();

        DropEntry oxeyeDaisyDrop = new DropEntry();
        oxeyeDaisyDrop.item = "minecraft:oxeye_daisy";
        oxeyeDaisyDrop.count = new CountRange(1, 1);
        oxeyeDaisy.drops.add(oxeyeDaisyDrop);

        defaultCrops.add(oxeyeDaisy);

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
                "minecraft:muddy_mangrove_roots"
        );
        poppy.drops = new ArrayList<>();

        DropEntry poppyDrop = new DropEntry();
        poppyDrop.item = "minecraft:poppy";
        poppyDrop.count = new CountRange(1, 1);
        poppy.drops.add(poppyDrop);

        defaultCrops.add(poppy);

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
                "minecraft:muddy_mangrove_roots"
        );
        redTulip.drops = new ArrayList<>();

        DropEntry redTulipDrop = new DropEntry();
        redTulipDrop.item = "minecraft:red_tulip";
        redTulipDrop.count = new CountRange(1, 1);
        redTulip.drops.add(redTulipDrop);

        defaultCrops.add(redTulip);

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
                "minecraft:muddy_mangrove_roots"
        );
        orangeTulip.drops = new ArrayList<>();

        DropEntry orangeTulipDrop = new DropEntry();
        orangeTulipDrop.item = "minecraft:orange_tulip";
        orangeTulipDrop.count = new CountRange(1, 1);
        orangeTulip.drops.add(orangeTulipDrop);

        defaultCrops.add(orangeTulip);

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
                "minecraft:muddy_mangrove_roots"
        );
        whiteTulip.drops = new ArrayList<>();

        DropEntry whiteTulipDrop = new DropEntry();
        whiteTulipDrop.item = "minecraft:white_tulip";
        whiteTulipDrop.count = new CountRange(1, 1);
        whiteTulip.drops.add(whiteTulipDrop);

        defaultCrops.add(whiteTulip);

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
                "minecraft:muddy_mangrove_roots"
        );
        pinkTulip.drops = new ArrayList<>();

        DropEntry pinkTulipDrop = new DropEntry();
        pinkTulipDrop.item = "minecraft:pink_tulip";
        pinkTulipDrop.count = new CountRange(1, 1);
        pinkTulip.drops.add(pinkTulipDrop);

        defaultCrops.add(pinkTulip);

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
                "minecraft:muddy_mangrove_roots"
        );
        witherRose.drops = new ArrayList<>();

        DropEntry witherRoseDrop = new DropEntry();
        witherRoseDrop.item = "minecraft:wither_rose";
        witherRoseDrop.count = new CountRange(1, 1);
        witherRose.drops.add(witherRoseDrop);

        defaultCrops.add(witherRose);

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
                "minecraft:muddy_mangrove_roots"
        );
        lilac.drops = new ArrayList<>();

        DropEntry lilacDrop = new DropEntry();
        lilacDrop.item = "minecraft:lilac";
        lilacDrop.count = new CountRange(1, 1);
        lilac.drops.add(lilacDrop);

        defaultCrops.add(lilac);

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
                "minecraft:muddy_mangrove_roots"
        );
        peony.drops = new ArrayList<>();

        DropEntry peonyDrop = new DropEntry();
        peonyDrop.item = "minecraft:peony";
        peonyDrop.count = new CountRange(1, 1);
        peony.drops.add(peonyDrop);

        defaultCrops.add(peony);

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
                "minecraft:muddy_mangrove_roots"
        );
        roseBush.drops = new ArrayList<>();

        DropEntry roseBushDrop = new DropEntry();
        roseBushDrop.item = "minecraft:rose_bush";
        roseBushDrop.count = new CountRange(1, 1);
        roseBush.drops.add(roseBushDrop);

        defaultCrops.add(roseBush);

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
                "minecraft:muddy_mangrove_roots"
        );
        sunflower.drops = new ArrayList<>();

        DropEntry sunflowerDrop = new DropEntry();
        sunflowerDrop.item = "minecraft:sunflower";
        sunflowerDrop.count = new CountRange(1, 1);
        sunflower.drops.add(sunflowerDrop);

        defaultCrops.add(sunflower);

        config.allowedCrops = defaultCrops;

        List<SoilEntry> defaultSoils = new ArrayList<>();

        SoilEntry dirt = new SoilEntry();
        dirt.soil = "minecraft:dirt";
        dirt.growthModifier = 1.0f;
        defaultSoils.add(dirt);

        SoilEntry coarseDirt = new SoilEntry();
        coarseDirt.soil = "minecraft:coarse_dirt";
        coarseDirt.growthModifier = 1.0f;
        defaultSoils.add(coarseDirt);

        SoilEntry podzol = new SoilEntry();
        podzol.soil = "minecraft:podzol";
        podzol.growthModifier = 1.0f;
        defaultSoils.add(podzol);

        SoilEntry mycelium = new SoilEntry();
        mycelium.soil = "minecraft:mycelium";
        mycelium.growthModifier = 1.0f;
        defaultSoils.add(mycelium);

        SoilEntry mud = new SoilEntry();
        mud.soil = "minecraft:mud";
        mud.growthModifier = 1.5f;
        defaultSoils.add(mud);

        SoilEntry muddyMangroveRoots = new SoilEntry();
        muddyMangroveRoots.soil = "minecraft:muddy_mangrove_roots";
        muddyMangroveRoots.growthModifier = 1.5f;
        defaultSoils.add(muddyMangroveRoots);

        SoilEntry rootedDirt = new SoilEntry();
        rootedDirt.soil = "minecraft:rooted_dirt";
        rootedDirt.growthModifier = 1.0f;
        defaultSoils.add(rootedDirt);

        SoilEntry moss = new SoilEntry();
        moss.soil = "minecraft:moss_block";
        moss.growthModifier = 1.0f;
        defaultSoils.add(moss);

        SoilEntry farmland = new SoilEntry();
        farmland.soil = "minecraft:farmland";
        farmland.growthModifier = 1.5f;
        defaultSoils.add(farmland);

        SoilEntry sand = new SoilEntry();
        sand.soil = "minecraft:sand";
        sand.growthModifier = 1f;
        defaultSoils.add(sand);

        SoilEntry redSand = new SoilEntry();
        redSand.soil = "minecraft:red_sand";
        redSand.growthModifier = 1f;
        defaultSoils.add(redSand);

        SoilEntry grass = new SoilEntry();
        grass.soil = "minecraft:grass_block";
        grass.growthModifier = 1f;
        defaultSoils.add(grass);

        SoilEntry soulSand = new SoilEntry();
        soulSand.soil = "minecraft:soul_sand";
        soulSand.growthModifier = 1.5f;
        defaultSoils.add(soulSand);

        SoilEntry endStone = new SoilEntry();
        endStone.soil = "minecraft:end_stone";
        endStone.growthModifier = 1f;
        defaultSoils.add(endStone);

        SoilEntry jungleLog = new SoilEntry();
        jungleLog.soil = "minecraft:jungle_log";
        jungleLog.growthModifier = 1f;
        defaultSoils.add(jungleLog);

        SoilEntry jungleWood = new SoilEntry();
        jungleWood.soil = "minecraft:jungle_wood";
        jungleWood.growthModifier = 1f;
        defaultSoils.add(jungleWood);

        SoilEntry strippedJungleLog = new SoilEntry();
        strippedJungleLog.soil = "minecraft:stripped_jungle_log";
        strippedJungleLog.growthModifier = 1f;
        defaultSoils.add(strippedJungleLog);

        SoilEntry strippedJungleWood = new SoilEntry();
        strippedJungleWood.soil = "minecraft:stripped_jungle_wood";
        strippedJungleWood.growthModifier = 1f;
        defaultSoils.add(strippedJungleWood);

        config.allowedSoils = defaultSoils;

        return config;
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
        public String soil;  // For backward compatibility
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
}