package com.blocklogic.agritech.item;

import com.blocklogic.agritech.AgriTech;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AgriTech.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
