package com.blocklogic.agritech;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.block.entity.ModBlockEntities;
import com.blocklogic.agritech.block.entity.renderer.AgritechPlanterBlockEntityRenderer;
import com.blocklogic.agritech.item.ModCreativeModeTabs;
import com.blocklogic.agritech.item.ModItems;
import com.blocklogic.agritech.screen.ModMenuTypes;
import com.blocklogic.agritech.screen.custom.AgritechPlanterScreen;
import com.blocklogic.agritech.screen.custom.FertilizerMakerScreen;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(AgriTech.MODID)
public class AgriTech
{
    public static final String MODID = "agritech";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AgriTech(IEventBus modEventBus, ModContainer modContainer)
    {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            
        }

        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get(), AgritechPlanterBlockEntityRenderer::new);
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.AGRITECH_PLANTER_MENU.get(), AgritechPlanterScreen::new);
            event.register(ModMenuTypes.FERTILIZER_MAKER_MENU.get(), FertilizerMakerScreen::new);
        }
    }
}
