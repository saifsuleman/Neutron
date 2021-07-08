package net.saifs.neutron.shop;

import net.saifs.neutron.item.Item;
import org.bukkit.inventory.ItemStack;

public class ShopItem {
    private final Item product;
    private final ItemStack displayItem;
    private final double price;

    public ShopItem(Item product, ItemStack displayItem, double price) {
        this.product = product;
        this.displayItem = displayItem;
        this.price = price;
    }

    public ShopItem(Item product, double price) {
        this.product = product;
        this.displayItem = product.getItemStack();
        this.price = price;
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
