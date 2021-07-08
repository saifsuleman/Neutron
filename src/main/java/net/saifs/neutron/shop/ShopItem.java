package net.saifs.neutron.shop;

import net.saifs.neutron.item.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShopItem {
    private final Item product;
    private final ItemStack displayItem;
    private final double price;

    public ShopItem(Item product, ItemStack displayItem, double price) {
        this.product = product;
        this.price = price;
        this.displayItem = addShopItemLore(displayItem);
    }

    public ShopItem(Item product, double price) {
        this(product, product.getItemStack(), price);
    }

    private ItemStack addShopItemLore(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            } else {
                lore.add("");
            }
            lore.add("§aPrice: §2$" + this.getPrice());
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public Item getProduct() {
        return product;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public double getPrice() {
        return price;
    }

}
