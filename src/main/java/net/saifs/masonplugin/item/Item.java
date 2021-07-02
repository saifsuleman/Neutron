package net.saifs.masonplugin.item;

import net.saifs.masonplugin.MasonPlugin;
import net.saifs.masonplugin.nms.NMSHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Item {
    public static final String KEY = "MASON_ITEM";

    private final String id;
    private final MasonPlugin plugin;
    protected ItemStack itemStack;

    public Item(String id, ItemStack itemStack, MasonPlugin plugin) {
        this.id = id;
        this.itemStack = NMSHandler.writeNBT(itemStack, KEY, id);
        this.plugin = plugin;

        if (this instanceof Listener) {
            plugin.getServer().getPluginManager().registerEvents((Listener) this, plugin);
        }
    }

    public String getID() {
        return this.id;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }

    protected MasonPlugin getPlugin() {
        return this.plugin;
    }

    public boolean isItem(ItemStack itemStack) {
        String id = NMSHandler.readNBT(itemStack, KEY);
        return getID().equals(id);
    }
}
