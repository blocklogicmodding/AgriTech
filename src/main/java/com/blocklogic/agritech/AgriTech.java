package com.blocklogic.agritech;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.block.entity.ModBlockEntities;
import com.blocklogic.agritech.block.entity.renderer.AgritechPlanterBlockEntityRenderer;
import com.blocklogic.agritech.command.AgritechCommands;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.item.ModCreativeModeTabs;
import com.blocklogic.agritech.item.ModItems;
import com.blocklogic.agritech.screen.ModMenuTypes;
import com.blocklogic.agritech.screen.custom.AgritechPlanterScreen;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
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
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.register(Config.class);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        AgritechCommands.register(event.getDispatcher());
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
        }

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.registerBlock(
                    Capabilities.ItemHandler.BLOCK,
                    (level, pos, state, blockEntity, side) -> {
                        if (state.is(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get())) {
                            if (blockEntity instanceof AgritechPlanterBlockEntity planter) {
                                if (side == Direction.UP) {
                                    return null;
                                } else if (side != null) {
                                    return planter.getOutputHandler();
                                } else {
                                    return planter.inventory;
                                }
                            }
                        }
                        return null;
                    },
                    ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get()
            );
        }
    }
}
