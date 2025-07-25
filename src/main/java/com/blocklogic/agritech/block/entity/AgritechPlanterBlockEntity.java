package com.blocklogic.agritech.block.entity;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.config.AgritechCropConfig;
import com.blocklogic.agritech.screen.custom.AgritechPlanterMenu;
import com.blocklogic.agritech.util.RegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AgritechPlanterBlockEntity extends BlockEntity implements MenuProvider {

    private class OutputOnlyItemHandler implements IItemHandler {
        private final ItemStackHandler original;
        private final int firstOutputSlot;
        private final int lastOutputSlot;

        public OutputOnlyItemHandler(ItemStackHandler original, int firstOutputSlot, int lastOutputSlot) {
            this.original = original;
            this.firstOutputSlot = firstOutputSlot;
            this.lastOutputSlot = lastOutputSlot;
        }

        @Override
        public int getSlots() {
            return original.getSlots();
        }

        @NotNull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return original.getStackInSlot(slot);
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= firstOutputSlot && slot <= lastOutputSlot) {
                return original.extractItem(slot, amount, simulate);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return original.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    }

    public final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        public int getSlotLimit(int slot) {
            if (slot == 0) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final OutputOnlyItemHandler outputHandler;

    public IItemHandler getOutputHandler() {
        return outputHandler;
    }

    private int growthStage = 0;
    private int maxGrowthStage = 8;
    private int growthTicks = 0;
    private int ticksToNextStage = 100;
    private boolean readyToHarvest = false;

    public AgritechPlanterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get(), pos, blockState);
        this.outputHandler = new OutputOnlyItemHandler(inventory, 2, 7);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.agritech.name");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new AgritechPlanterMenu(i, inventory, this);
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

    public static void tick(Level level, BlockPos pos, BlockState state, AgritechPlanterBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        ItemStack seedStack = blockEntity.inventory.getStackInSlot(0);
        ItemStack soilStack = blockEntity.inventory.getStackInSlot(1);

        if (seedStack.isEmpty() || soilStack.isEmpty()) {
            blockEntity.resetGrowth();
            return;
        }

        float growthModifier = blockEntity.getGrowthModifier(soilStack);

        if (!blockEntity.readyToHarvest) {
            blockEntity.growthTicks++;

            if (blockEntity.growthTicks >= blockEntity.ticksToNextStage / growthModifier) {
                blockEntity.growthTicks = 0;
                blockEntity.growthStage++;

                if (blockEntity.growthStage >= blockEntity.maxGrowthStage) {
                    blockEntity.readyToHarvest = true;
                }

                level.sendBlockUpdated(pos, state, state, 3);
                blockEntity.setChanged();
            }
        }

        if (blockEntity.readyToHarvest && blockEntity.hasOutputSpace()) {
            blockEntity.harvestCrop();
        }

        if (state.is(ModBlocks.AGRITECH_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.ACACIA_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.BAMBOO_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.BIRCH_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.CHERRY_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.CRIMSON_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.DARK_OAK_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.JUNGLE_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.MANGROVE_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.SPRUCE_HOPPING_PLANTER_BLOCK.get()) ||
                state.is(ModBlocks.WARPED_HOPPING_PLANTER_BLOCK.get())

        ) {
            tryOutputItemsBelow(level, pos, blockEntity);
        }
    }

    public float getGrowthModifier(ItemStack soilStack) {
        if (soilStack.isEmpty()) return 1.0f;

        String soilId = RegistryHelper.getItemId(soilStack);
        return AgritechCropConfig.getSoilGrowthModifier(soilId);
    }

    private static void tryOutputItemsBelow(Level level, BlockPos pos, AgritechPlanterBlockEntity blockEntity) {
        BlockPos belowPos = pos.below();
        BlockEntity targetEntity = level.getBlockEntity(belowPos);

        if (targetEntity == null) {
            return;
        }

        IItemHandler targetInventory = level.getCapability(Capabilities.ItemHandler.BLOCK,
                belowPos,
                Direction.UP);

        if (targetInventory == null) {
            return;
        }

        boolean changed = false;

        for (int slot = 2; slot < 8; slot++) {
            if (!blockEntity.inventory.getStackInSlot(slot).isEmpty()) {
                var extractedItem = blockEntity.inventory.extractItem(slot, 64, true);

                if (!extractedItem.isEmpty()) {
                    var remaining = ItemHandlerHelper.insertItemStacked(targetInventory, extractedItem, false);
                    int insertedAmount = extractedItem.getCount() - remaining.getCount();

                    if (insertedAmount > 0) {
                        blockEntity.inventory.extractItem(slot, insertedAmount, false);
                        changed = true;
                    }
                }
            }
        }

        if (changed) {
            blockEntity.setChanged();
            level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        }
    }

    public boolean hasOutputSpace() {
        List<ItemStack> potentialDrops = getHarvestDrops(inventory.getStackInSlot(0));

        Map<Integer, ItemStack> simulatedSlots = new HashMap<>();
        for (int slot = 2; slot < 8; slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            simulatedSlots.put(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copy());
        }

        for (ItemStack drop : potentialDrops) {
            int remainingToPlace = drop.getCount();

            for (int slot = 2; slot < 8; slot++) {
                ItemStack existingStack = simulatedSlots.get(slot);
                if (!existingStack.isEmpty() && existingStack.is(drop.getItem()) &&
                        existingStack.getCount() < existingStack.getMaxStackSize()) {

                    int spaceAvailable = existingStack.getMaxStackSize() - existingStack.getCount();
                    int itemsToAdd = Math.min(spaceAvailable, remainingToPlace);

                    existingStack.grow(itemsToAdd);
                    remainingToPlace -= itemsToAdd;

                    if (remainingToPlace <= 0) {
                        break;
                    }
                }
            }

            if (remainingToPlace > 0) {
                for (int slot = 2; slot < 8; slot++) {
                    ItemStack existingStack = simulatedSlots.get(slot);
                    if (existingStack.isEmpty()) {
                        simulatedSlots.put(slot, new ItemStack(drop.getItem(), remainingToPlace));
                        remainingToPlace = 0;
                        break;
                    }
                }
            }

            if (remainingToPlace > 0) {
                return false;
            }
        }

        return true;
    }

    public void resetGrowth() {
        growthStage = 0;
        growthTicks = 0;
        readyToHarvest = false;
        setChanged();
    }

    public void harvestCrop() {
        if (!readyToHarvest) return;

        ItemStack seedStack = inventory.getStackInSlot(0);

        List<ItemStack> drops = getHarvestDrops(seedStack);

        for (ItemStack dropStack : drops) {
            int remainingItemsToPlace = dropStack.getCount();

            while (remainingItemsToPlace > 0) {
                boolean itemPlaced = false;

                for (int slot = 2; slot < 8; slot++) {
                    ItemStack existingStack = inventory.getStackInSlot(slot);

                    if (!existingStack.isEmpty() &&
                            existingStack.is(dropStack.getItem()) &&
                            existingStack.getCount() < existingStack.getMaxStackSize()) {

                        int spaceAvailable = existingStack.getMaxStackSize() - existingStack.getCount();
                        int itemsToAdd = Math.min(spaceAvailable, remainingItemsToPlace);

                        existingStack.grow(itemsToAdd);
                        inventory.setStackInSlot(slot, existingStack);

                        remainingItemsToPlace -= itemsToAdd;
                        itemPlaced = true;

                        if (remainingItemsToPlace <= 0) {
                            break;
                        }
                    }
                }

                if (remainingItemsToPlace > 0) {
                    for (int slot = 2; slot < 8; slot++) {
                        ItemStack existingStack = inventory.getStackInSlot(slot);

                        if (existingStack.isEmpty()) {
                            ItemStack newStack = new ItemStack(dropStack.getItem(), remainingItemsToPlace);
                            inventory.setStackInSlot(slot, newStack);

                            remainingItemsToPlace = 0;
                            itemPlaced = true;
                            break;
                        }
                    }
                }

                if (!itemPlaced) {
                    break;
                }
            }

            if (remainingItemsToPlace > 0) {
                break;
            }
        }

        resetGrowth();
    }

    private List<ItemStack> getHarvestDrops(ItemStack seedStack) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = new Random();

        if (seedStack.isEmpty()) return drops;

        String seedId = RegistryHelper.getItemId(seedStack);
        List<AgritechCropConfig.DropInfo> configDrops = AgritechCropConfig.getCropDrops(seedId);

        for (AgritechCropConfig.DropInfo dropInfo : configDrops) {
            if (random.nextFloat() <= dropInfo.chance) {
                int count = dropInfo.minCount;
                if (dropInfo.maxCount > dropInfo.minCount) {
                    count = dropInfo.minCount + random.nextInt(dropInfo.maxCount - dropInfo.minCount + 1);
                }

                Item item = RegistryHelper.getItem(dropInfo.item);
                if (item != null) {
                    drops.add(new ItemStack(item, count));
                }
            }
        }

        return drops;
    }

    public float getGrowthProgress() {
        if (maxGrowthStage <= 0 || readyToHarvest) {
            return 0.0f;
        }

        float stageProgress = (float) growthTicks / (ticksToNextStage / getGrowthModifier(inventory.getStackInSlot(1)));

        float overallProgress = ((float) growthStage + stageProgress) / (float) maxGrowthStage;

        return Math.min(overallProgress, 1.0f);
    }

    public int getProgressBarWidth(int maxWidth) {
        return (int) (getGrowthProgress() * maxWidth);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("growthStage", growthStage);
        tag.putInt("growthTicks", growthTicks);
        tag.putBoolean("readyToHarvest", readyToHarvest);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        growthStage = tag.getInt("growthStage");
        growthTicks = tag.getInt("growthTicks");
        readyToHarvest = tag.getBoolean("readyToHarvest");
    }

    public int getGrowthStage() {
        return growthStage;
    }
}