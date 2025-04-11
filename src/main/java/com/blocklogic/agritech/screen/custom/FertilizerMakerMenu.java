package com.blocklogic.agritech.screen.custom;

import com.blocklogic.agritech.block.entity.FertilizerMakerBlockEntity;
import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FertilizerMakerMenu extends AbstractContainerMenu {
    public final FertilizerMakerBlockEntity blockEntity;
    private final ContainerLevelAccess levelAccess;
    private final DataSlot progressData;
    private final DataSlot maxProgressData;

    public static class OrganicMaterialSlot extends SlotItemHandler {
        public OrganicMaterialSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            Item item = stack.getItem();
            return item == Items.DIRT ||
                    item == Items.GRASS_BLOCK ||
                    item == Items.OAK_LEAVES ||
                    item == Items.SPRUCE_LEAVES ||
                    item == Items.BIRCH_LEAVES ||
                    item == Items.JUNGLE_LEAVES ||
                    item == Items.ACACIA_LEAVES ||
                    item == Items.DARK_OAK_LEAVES ||
                    item == Items.WHEAT ||
                    item == Items.WHEAT_SEEDS ||
                    item == Items.POTATO ||
                    item == Items.CARROT ||
                    item == Items.BEETROOT ||
                    item == Items.BEETROOT_SEEDS ||
                    item == Items.PUMPKIN ||
                    item == Items.MELON ||
                    item == Items.SUGAR_CANE ||
                    item == Items.BAMBOO ||
                    item == Items.OAK_SAPLING ||
                    item == Items.SPRUCE_SAPLING ||
                    item == Items.BIRCH_SAPLING ||
                    item == Items.JUNGLE_SAPLING ||
                    item == Items.ACACIA_SAPLING ||
                    item == Items.DARK_OAK_SAPLING ||
                    item == Items.SWEET_BERRIES;
        }
    }

    public static class MineralSlot extends SlotItemHandler {
        public MineralSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            Item item = stack.getItem();
            return item == Items.DIAMOND || item == Items.EMERALD;
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

    public FertilizerMakerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, playerInventory.player.level().getBlockEntity(extraData.readBlockPos()), ContainerLevelAccess.NULL);
    }

    public FertilizerMakerMenu(int containerId, Inventory playerInventory, BlockEntity entity, ContainerLevelAccess levelAccess) {
        super(ModMenuTypes.FERTILIZER_MAKER_MENU.get(), containerId);
        this.blockEntity = (FertilizerMakerBlockEntity) entity;
        this.levelAccess = levelAccess;

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        this.addSlot(new OrganicMaterialSlot(this.blockEntity.inventory, 0, 44, 30));
        this.addSlot(new OrganicMaterialSlot(this.blockEntity.inventory, 1, 62, 30));
        this.addSlot(new OrganicMaterialSlot(this.blockEntity.inventory, 2, 80, 30));

        this.addSlot(new MineralSlot(this.blockEntity.inventory, 3, 116, 30));

        this.addSlot(new OutputSlot(this.blockEntity.inventory, 4, 80, 57));

        this.progressData = addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return blockEntity.getProgress();
            }

            @Override
            public void set(int value) {
                blockEntity.setProgress(value);
            }
        });

        this.maxProgressData = addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return blockEntity.getMaxProgress();
            }

            @Override
            public void set(int value) {
                blockEntity.setMaxProgress(value);
            }
        });
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.levelAccess, player, ModBlocks.AGRITECH_FERTILIZER_MAKER_BLOCK.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < 36) {
            if (sourceStack.getItem() == Items.DIAMOND || sourceStack.getItem() == Items.EMERALD) {
                if (!moveItemStackTo(sourceStack, 39, 40, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (new OrganicMaterialSlot(null, 0, 0, 0).mayPlace(sourceStack)) {
                if (!moveItemStackTo(sourceStack, 36, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }
        } else if (index < 41) {
            if (!moveItemStackTo(sourceStack, 0, 36, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slot index: " + index);
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

    public boolean isCrafting() {
        return progressData.get() > 0;
    }

    public int getScaledProgress() {
        int progress = progressData.get();
        int maxProgress = maxProgressData.get();
        int progressBarHeight = 44;

        return maxProgress != 0 && progress != 0
                ? (progress * progressBarHeight) / maxProgress
                : 0;
    }
}