package net.saifs.neutron.shop.gui;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.shop.ShopItem;
import net.saifs.neutron.shop.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ShopGUI implements Listener {
    private final int SIZE;

    private final ShopManager shopManager;
    private List<Inventory> pages;

    public ShopGUI(ShopManager shopManager) {
        this.shopManager = shopManager;
        this.pages = new ArrayList<>();
        this.SIZE = 9;

        Neutron plugin = this.shopManager.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // returns -1 if not in the page
    private int getPage(Inventory inv) {
        return this.pages.indexOf(inv);
    }

    public void constructPages() {
        for (int i = 0; i < getPageCount(); i++) {
            Inventory page = this.construct(i);
            this.pages.add(page);
        }
    }

    private Inventory construct(int page) {
        Inventory inventory = Bukkit.createInventory(null, this.SIZE, "Neutron Shop - Page " + (page + 1));
        for (int i = 0; i < this.SIZE; i++) {
            if (i == SIZE - 9 && page != 0) {
                // back arrow
                ItemStack back = new ItemStack(Material.ARROW, 1);
                ItemMeta meta = back.getItemMeta();
                if (meta == null) continue;
                meta.setDisplayName("§2§lBACK");
                inventory.setItem(i, back);
            } else if (i == SIZE - 1 && page != getPageCount() - 1) {
                // front arrow
                ItemStack back = new ItemStack(Material.ARROW, 1);
                ItemMeta meta = back.getItemMeta();
                if (meta == null) continue;
                meta.setDisplayName("§2§lFORWARD");
                inventory.setItem(i, back);
            } else {
                ShopItem shopItem = getItemAt(page, i);
                if (shopItem != null)
                    inventory.setItem(i, shopItem.getDisplayItem());
            }
        }
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inventory = e.getClickedInventory();

        int page = getPage(inventory);
        if (page == -1) return;
        e.setCancelled(true);
        int slot = e.getSlot();

        Player player = (Player) e.getWhoClicked();

        ShopItem clicked = getItemAt(page, slot);
        if (clicked != null) {
            PurchaseGUI purchaseGUI = new PurchaseGUI(player, clicked, getShopManager());
            purchaseGUI.open();
        }
    }

    public ShopManager getShopManager() {
        return this.shopManager;
    }

    private int getPageCount() {
        int items = this.shopManager.getShopItems().size();
        int pages = (int) Math.ceil(items / (float) SIZE);
        items += 2 * (pages - 1);
        return (int) Math.ceil(items / (float) SIZE);
    }

    private ShopItem getItemAt(int page, int slot) {
        List<ShopItem> items = new ArrayList<>(getShopManager().getShopItems().values());
        int offset = 2 * page;
        if (slot < SIZE - 9 && page != 0) {
            offset++;
        }
        int i = ((page * SIZE) + slot) - offset;
        if (i >= items.size() || i < 0) return null;
        return items.get(i);
    }

    public Inventory getPage(int page) {
        if (page < 0 || page >= pages.size()) return null;
        return pages.get(page);
    }
}
