package com.blocklogic.agritech.screen.custom;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.block.entity.AgritechPlanterBlockEntity;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.screen.ModMenuTypes;
import com.blocklogic.agritech.util.RegistryHelper;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class AgritechPlanterMenu extends AbstractContainerMenu {
    private static final Logger LOGGER = LogUtils.getLogger();
    public final AgritechPlanterBlockEntity blockEntity;

    private final Level level;

    public AgritechPlanterMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public static class SeedSlot extends SlotItemHandler {
        private final AgritechPlanterBlockEntity blockEntity;

        public SeedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, AgritechPlanterBlockEntity blockEntity) {
            super(itemHandler, index, xPosition, yPosition);
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            String seedId = RegistryHelper.getItemId(stack);

            if (!AgritechCropConfig.isValidSeed(seedId)) {
                return false;
            }

            ItemStack soilStack = blockEntity.inventory.getStackInSlot(1);
            if (!soilStack.isEmpty()) {
                String soilId = RegistryHelper.getItemId(soilStack);
                return AgritechCropConfig.isSoilValidForSeed(soilId, seedId);
            }

            return true;
        }
    }

    public static class SoilSlot extends SlotItemHandler {
        private final AgritechPlanterBlockEntity blockEntity;

        public SoilSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, AgritechPlanterBlockEntity blockEntity) {
            super(itemHandler, index, xPosition, yPosition);
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            String soilId = RegistryHelper.getItemId(stack);

            if (!AgritechCropConfig.isValidSoil(soilId)) {
                return false;
            }

            ItemStack seedStack = blockEntity.inventory.getStackInSlot(0);
            if (!seedStack.isEmpty()) {
                String seedId = RegistryHelper.getItemId(seedStack);
                return AgritechCropConfig.isSoilValidForSeed(soilId, seedId);
            }

            return true;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack) {
            return 1;
        }
    }

    public static class OutputSlot extends SlotItemHandler {
        public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public AgritechPlanterMenu(int containerId, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.AGRITECH_PLANTER_MENU.get(), containerId);
        this.blockEntity = ((AgritechPlanterBlockEntity) blockEntity);
        this.level = inv.player.level();

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new SeedSlot(this.blockEntity.inventory, 0, 26, 18, this.blockEntity));
        this.addSlot(new SoilSlot(this.blockEntity.inventory, 1, 26, 54, this.blockEntity));

        int outputSlotIndex = 2;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new OutputSlot(this.blockEntity.inventory, outputSlotIndex++,
                        80 + col * 18, 27 + row * 18));
            }
        }
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 8;

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            boolean movedItem = false;

            if (slots.get(VANILLA_SLOT_COUNT).mayPlace(sourceStack)) {
                if (moveItemStackTo(sourceStack, VANILLA_SLOT_COUNT, VANILLA_SLOT_COUNT + 1, false)) {
                    movedItem = true;
                }
            }
            else if (slots.get(VANILLA_SLOT_COUNT + 1).mayPlace(sourceStack)) {
                if (moveItemStackTo(sourceStack, VANILLA_SLOT_COUNT + 1, VANILLA_SLOT_COUNT + 2, false)) {
                    movedItem = true;
                }
            }

            if (!movedItem) {
                return ItemStack.EMPTY;
            }
        }
        else if (index < VANILLA_SLOT_COUNT + TE_INVENTORY_SLOT_COUNT) {
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            LOGGER.error("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        Block block = blockEntity.getBlockState().getBlock();

        if (block == ModBlocks.AGRITECH_PLANTER_BLOCK.get() || block == ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get()) {
            return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, block);
        }

        return false;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 87 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 146));
        }
    }
}
