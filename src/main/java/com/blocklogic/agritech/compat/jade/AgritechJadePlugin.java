package com.blocklogic.agritech.compat.jade;

import com.blocklogic.agritech.block.custom.AgritechPlanterBlock;
import com.blocklogic.agritech.block.custom.AgritechHoppingPlanterBlock;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class AgritechJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(PlanterProvider.INSTANCE, AgritechPlanterBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(PlanterProvider.INSTANCE, AgritechPlanterBlock.class);
        registration.registerBlockComponent(PlanterProvider.INSTANCE, AgritechHoppingPlanterBlock.class);
    }
}