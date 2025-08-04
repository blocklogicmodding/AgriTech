package com.blocklogic.agritech.datagen.custom;

import com.blocklogic.agritech.AgriTech;
import com.blocklogic.agritech.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ATCItemModelProvider extends ItemModelProvider {
    public ATCItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AgriTech.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.HOPPING_UPGRADE.get());
    }
}
