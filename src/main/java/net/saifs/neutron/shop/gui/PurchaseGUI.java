package net.saifs.neutron.shop.gui;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.helper.NeutronHelper;
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

public class PurchaseGUI implements Listener {
    private final Player player;
    private final ShopItem shopItem;
    private final Inventory inventory;
    private final ShopManager shopManager;

    public PurchaseGUI(Player player, ShopItem shopItem, ShopManager shopManager) {
        this.player = player;
        this.shopItem = shopItem;
        this.inventory = construct();
        this.shopManager = shopManager;

        Neutron plugin = shopManager.getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() != inventory) return;

        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ButtonType buttonType = this.getButtonType(e.getSlot());

        switch (buttonType) {
            case CANCEL:
                player.closeInventory();
                player.sendMessage("§cShop purchase cancelled.");
                break;
            case CONFIRM:
                this.shopManager.handlePlayerPurchase(player, this.shopItem);
                player.closeInventory();
                break;
        }
    }

    private Inventory construct() {
        Inventory inventory = Bukkit.createInventory(null, 27, "Purchase - " + this.shopItem.getProduct().getID());

        ItemStack cancel = NeutronHelper.constructItem(Material.RED_STAINED_GLASS_PANE, "§c§lCANCEL");
        ItemStack confirm = NeutronHelper.constructItem(Material.GREEN_STAINED_GLASS_PANE, "§a§lCONFIRM");
        ItemStack filler = NeutronHelper.constructItem(Material.BLACK_STAINED_GLASS_PANE, " ");

        for (int i = 0; i < inventory.getSize(); i++) {
            switch (getButtonType(i)) {
                case CANCEL:
                    inventory.setItem(i, cancel);
                    break;
                case CONFIRM:
                    inventory.setItem(i, confirm);
                    break;
                case NONE:
                    inventory.setItem(i, i == 13 ? this.shopItem.getDisplayItem() : filler);
                    break;
            }
        }

        return inventory;
    }

    private ButtonType getButtonType(int slot) {
        int mod = slot % 9;
        if (mod < 4) return ButtonType.CANCEL;
        if (mod > 4) return ButtonType.CONFIRM;
        return ButtonType.NONE;
    }

    public void open() {
        this.player.openInventory(this.inventory);
    }

    private enum ButtonType {
        CANCEL, CONFIRM, NONE
    }
}
