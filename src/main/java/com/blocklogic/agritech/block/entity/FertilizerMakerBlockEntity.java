package com.blocklogic.agritech.block.entity;

import com.blocklogic.agritech.item.ModItems;
import com.blocklogic.agritech.screen.custom.FertilizerMakerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class FertilizerMakerBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private int progress = 0;
    private int maxProgress = 100;

    public FertilizerMakerBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.FERTILIZER_MAKER_BLOCK_ENTITY.get(), pos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Fertilizer Maker");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FertilizerMakerMenu(containerId, inventory, this, ContainerLevelAccess.create(level, worldPosition));
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveWithFullMetadata(pRegistries);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FertilizerMakerBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        if (blockEntity.hasRecipe()) {
            blockEntity.progress++;
            setChanged(level, pos, state);

            if (blockEntity.progress >= blockEntity.maxProgress) {
                blockEntity.craftItem();
                blockEntity.resetProgress();
            }
        } else {
            blockEntity.resetProgress();
            setChanged(level, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasRecipe() {
        boolean hasOrganicMaterials = hasOrganicMaterials();
        boolean hasMineralItem = hasMineralItem();
        boolean hasOutputSpace = hasOutputSpace();

        return hasOrganicMaterials && hasMineralItem && hasOutputSpace;
    }

    private boolean hasOutputSpace() {
        return this.inventory.getStackInSlot(4).isEmpty() ||
                this.inventory.getStackInSlot(4).getCount() < this.inventory.getStackInSlot(4).getMaxStackSize();
    }

    private boolean hasOrganicMaterials() {
        return !this.inventory.getStackInSlot(0).isEmpty() &&
                !this.inventory.getStackInSlot(1).isEmpty() &&
                !this.inventory.getStackInSlot(2).isEmpty() &&
                isOrganicMaterial(this.inventory.getStackInSlot(0).getItem()) &&
                isOrganicMaterial(this.inventory.getStackInSlot(1).getItem()) &&
                isOrganicMaterial(this.inventory.getStackInSlot(2).getItem());
    }

    private boolean hasMineralItem() {
        ItemStack mineralStack = this.inventory.getStackInSlot(3);
        Item mineralItem = mineralStack.getItem();
        return !mineralStack.isEmpty() && (mineralItem == Items.DIAMOND || mineralItem == Items.EMERALD);
    }

    private boolean isOrganicMaterial(Item item) {
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

    private void craftItem() {
        if (this.hasRecipe()) {
            ItemStack mineralStack = this.inventory.getStackInSlot(3);
            Item outputItem;

            if (mineralStack.getItem() == Items.DIAMOND) {
                outputItem = ModItems.AGRITECH_PREMIUM_FERTILIZER.get();
            } else if (mineralStack.getItem() == Items.EMERALD) {
                outputItem = ModItems.AGRITECH_PRISTINE_FERTILIZER.get();
            } else {
                return;
            }

            ItemStack outputStack = new ItemStack(outputItem, 1);

            ItemStack currentOutput = this.inventory.getStackInSlot(4);
            if (currentOutput.isEmpty()) {
                this.inventory.setStackInSlot(4, outputStack);
            } else if (currentOutput.getItem() == outputItem) {
                currentOutput.grow(1);
            }

            this.inventory.extractItem(0, 1, false);
            this.inventory.extractItem(1, 1, false);
            this.inventory.extractItem(2, 1, false);
            this.inventory.extractItem(3, 1, false);
        }
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(this.inventory.getSlots());
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            inventory.setItem(i, this.inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("fertilizer_maker.progress", progress);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("fertilizer_maker.progress");
    }
}
