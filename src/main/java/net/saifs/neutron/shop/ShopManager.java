package net.saifs.neutron.shop;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.helper.NeutronHelper;
import net.saifs.neutron.item.Item;
import net.saifs.neutron.shop.gui.ShopGUI;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

public class ShopManager {
    private final Neutron plugin;
    private final Map<String, ShopItem> shopItems;
    private final ShopGUI shopGUI;

    public ShopManager(Neutron plugin) {
        this.plugin = plugin;
        this.shopItems = new HashMap<>();
        this.shopGUI = new ShopGUI(this);
    }

    public Neutron getPlugin() {
        return plugin;
    }

    public Map<String, ShopItem> getShopItems() {
        return shopItems;
    }

    public void registerItem(String id, Item item, double price) {
        ShopItem shopItem = new ShopItem(item, price);
        this.shopItems.put(id, shopItem);
    }

    public void constructShopGUI() {
        shopGUI.constructPages();
    }

    public void openShopGUI(Player player) {
        player.openInventory(shopGUI.getPage(0));
    }

    public void handlePlayerPurchase(Player player, ShopItem shopItem) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {
            double price = shopItem.getPrice();
            double balance = plugin.getEconomy().getPlayerBalance(player);
            boolean canAfford = price < balance;

            if (canAfford) plugin.getEconomy().removeMoney(player, price);

            scheduler.runTask(plugin, () -> {
                if (canAfford) NeutronHelper.giveItem(player, shopItem.getProduct().getItemStack());
                String response = canAfford ? "§aYou have successfully bought that item!" : "§cYou do not have the sufficient funds for that!";
                player.sendMessage(response);
            });
        });
    }
}
