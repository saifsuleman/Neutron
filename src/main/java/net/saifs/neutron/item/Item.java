package net.saifs.neutron.item;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.helper.NMSHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class Item {
    public static final String KEY = "NEUTRON_ITEM";

    private final String id;
    private final Neutron plugin;
    protected ItemStack itemStack;

    public Item(String id, ItemStack itemStack, Neutron plugin) {
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

    protected Neutron getPlugin() {
        return this.plugin;
    }

    public boolean isItem(ItemStack itemStack) {
        String id = NMSHandler.readNBT(itemStack, KEY);
        return getID().equals(id);
    }
}
