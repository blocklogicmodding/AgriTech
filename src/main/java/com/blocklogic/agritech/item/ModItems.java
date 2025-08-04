package com.blocklogic.agritech.item;

import com.blocklogic.agritech.AgriTech;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AgriTech.MODID);

    public static final DeferredItem<Item> HOPPING_UPGRADE = ITEMS.register("hopping_upgrade",
            () -> new Item(new Item.Properties())
            {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("item.agritech.hopping_upgrade.subtitle").withStyle(ChatFormatting.LIGHT_PURPLE));
                }
            }
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
