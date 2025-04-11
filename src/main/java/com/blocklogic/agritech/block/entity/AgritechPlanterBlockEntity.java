package com.blocklogic.agritech.block.entity;

import com.blocklogic.agritech.block.ModBlocks;
import com.blocklogic.agritech.screen.custom.AgritechPlanterMenu;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AgritechPlanterBlockEntity extends BlockEntity implements MenuProvider {

    public final ItemStackHandler inventory = new ItemStackHandler(8) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            if (slot == 0) {
                return 1;
            }
            return super.getStackLimit(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

   private float rotation;

    private int growthStage = 0;
    private int maxGrowthStage = 7;
    private int growthTicks = 0;
    private int ticksToNextStage = 100;
    private boolean readyToHarvest = false;

    public AgritechPlanterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.AGRITECH_PLANTER_BLOCK_ENTITY.get(), pos, blockState);
    }

     @Override
    public Component getDisplayName() {
        return Component.literal("Agritech Planter");
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
                        // Simulate creating a new stack
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

    private float getGrowthModifier(ItemStack soilStack) {
        if (soilStack.isEmpty()) return 1.0f;

        if (soilStack.getItem() == ModBlocks.AGRITECH_SIMPLE_MULCH.get().asItem()) {
            return 1.5f;
        } else if (soilStack.getItem() == ModBlocks.AGRITECH_FERTILE_MULCH.get().asItem()) {
            return 2.0f;
        } else if (soilStack.getItem() == ModBlocks.AGRITECH_PREMIUM_MULCH.get().asItem()) {
            return 3.5f;
        } else if (soilStack.getItem() == ModBlocks.AGRITECH_PRISTINE_MULCH.get().asItem()) {
            return 6.0f;
        }

        return 1.0f;
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

        boolean allItemsPlaced = true;

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
                    allItemsPlaced = false;
                    break;
                }
            }

            if (remainingItemsToPlace > 0) {
                allItemsPlaced = false;
                break;
            }
        }

        resetGrowth();
    }

    private List<ItemStack> getHarvestDrops(ItemStack seedStack) {
        List<ItemStack> drops = new ArrayList<>();
        Random random = new Random();

        if (seedStack.isEmpty()) return drops;

        Item seedItem = seedStack.getItem();

        if (seedItem == Items.WHEAT_SEEDS) {
            drops.add(new ItemStack(Items.WHEAT, 1));
            drops.add(new ItemStack(Items.WHEAT_SEEDS, random.nextInt(3) + 1));
        } else if (seedItem == Items.POTATO) {
            int count = random.nextInt(4) + 1;
            drops.add(new ItemStack(Items.POTATO, count));
            if (random.nextFloat() < 0.02f) {
                drops.add(new ItemStack(Items.POISONOUS_POTATO, 1));
            }
        } else if (seedItem == Items.CARROT) {
            drops.add(new ItemStack(Items.CARROT, random.nextInt(4) + 1));
        } else if (seedItem == Items.BEETROOT_SEEDS) {
            drops.add(new ItemStack(Items.BEETROOT, 1));
            drops.add(new ItemStack(Items.BEETROOT_SEEDS, random.nextInt(3) + 1));
        } else if (seedItem == Items.PUMPKIN_SEEDS) {
            drops.add(new ItemStack(Items.PUMPKIN, 1));
        } else if (seedItem == Items.MELON_SEEDS) {
            drops.add(new ItemStack(Items.MELON_SLICE, random.nextInt(5) + 3));
        } else if (seedItem == Items.NETHER_WART) {
            drops.add(new ItemStack(Items.NETHER_WART, random.nextInt(3) + 2));
        } else if (seedItem == Items.SUGAR_CANE) {
            drops.add(new ItemStack(Items.SUGAR_CANE, random.nextInt(3) + 2));
        } else if (seedItem == Items.BAMBOO) {
            drops.add(new ItemStack(Items.BAMBOO, random.nextInt(3) + 2));
        }

        return drops;
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

    // Getters for renderer
    public int getGrowthStage() {
        return growthStage;
    }

    public boolean isReadyToHarvest() {
        return readyToHarvest;
    }
}
