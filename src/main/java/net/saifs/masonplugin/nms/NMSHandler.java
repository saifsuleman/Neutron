package net.saifs.masonplugin.nms;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NMSHandler {
    public static String readNBT(ItemStack i, String key) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        return nbt.getString(key);
    }
    public static boolean readNBTBoolean(ItemStack i, String key) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        return nbt.getBoolean(key);
    }

    public static ItemStack writeNBT(ItemStack i, String key, String value) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        nbt.setString(key, value);
        itemStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    public static ItemStack writeNBT(ItemStack i, String key, boolean value) {
        net.minecraft.world.item.ItemStack itemStack = CraftItemStack.asNMSCopy(i);
        NBTTagCompound nbt = itemStack.getOrCreateTag();
        nbt.setBoolean(key, value);
        itemStack.setTag(nbt);
        return CraftItemStack.asBukkitCopy(itemStack);
    }
}
