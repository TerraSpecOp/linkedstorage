package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.LinkedInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashMap;

public class StorageManager implements StorageManagerComponent {
    private HashMap<String, LinkedInventory> inventories = new HashMap<>();

    @Override
    public StorageManager getValue() {
        return this;
    }

    public static String getRandomKey() {
        String key;
        //do {
        key = RandomStringUtils.randomAlphanumeric(7);
        // } while (LinkedStorageMod.SMAN.get().containsKey(key));
        return key;
    }

    public LinkedInventory getInv(String channel) {
        if (!inventories.containsKey(channel))
            inventories.put(channel, new LinkedInventory());
        return inventories.get(channel);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        inventories.clear();
        CompoundTag invs = tag.getCompound("invs");
        for (String key : invs.getKeys()) {
            inventories.put(key, fromList(invs.getCompound(key)));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invs = new CompoundTag();
        for (String key : inventories.keySet()) {
            if (!inventories.get(key).isInvEmpty())
                invs.put(key, Inventories.toTag(new CompoundTag(), toList(inventories.get(key))));
        }
        tag.put("invs", invs);
        return tag;
    }

    private DefaultedList<ItemStack> toList(Inventory inv) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inv.getInvSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getInvSize(); i++)
            stacks.set(i, inv.getInvStack(i));
        return stacks;
    }

    private LinkedInventory fromList(CompoundTag tag) {
        LinkedInventory inventory = new LinkedInventory();
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, stacks);
        for (int i = 0; i < stacks.size(); i++)
            inventory.setInvStack(i, stacks.get(i));
        return inventory;
    }
}