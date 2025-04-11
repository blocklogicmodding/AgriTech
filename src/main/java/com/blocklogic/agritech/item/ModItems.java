package com.blocklogic.agritech.item;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.item.custom.PremiumFertilizerItem;
import com.blocklogic.agritech.item.custom.PristineFertilizerItem;
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

    public static final DeferredItem<Item> AGRITECH_PREMIUM_FERTILIZER = ITEMS.register("agritech_premium_fertilizer",
            () -> new PremiumFertilizerItem(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_premium_fertilizer"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static final DeferredItem<Item> AGRITECH_PRISTINE_FERTILIZER = ITEMS.register("agritech_pristine_fertilizer",
            () -> new PristineFertilizerItem(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.agritech.agritech_pristine_fertilizer"));
                    super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
                }
            });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
