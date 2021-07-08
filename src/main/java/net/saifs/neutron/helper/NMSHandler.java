package net.saifs.neutron.helper;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSHandler {
    public static String readNBT(ItemStack i, String key) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        return nbt.getString(key);
    }

    public static ItemStack writeNBT(ItemStack i, String key, String value) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        nbt.setString(key, value);
        itemStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public static int readInt(ItemStack i, String key) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        return nbt.getInt(key);
    }

    public static ItemStack writeInt(ItemStack i, String key, int value) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        nbt.setInt(key, value);
        itemStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public static long readLong(ItemStack i, String key) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        return nbt.getLong(key);
    }

    public static ItemStack writeLong(ItemStack i, String key, long value) {
        net.minecraft.server.v1_16_R3.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        nbt.setLong(key, value);
        itemStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(itemStack);
    }
}
