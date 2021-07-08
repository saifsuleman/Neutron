package net.saifs.neutron.item.gun.registry;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.helper.NeutronHelper;
import net.saifs.neutron.item.gun.GunItem;
import net.saifs.neutron.item.gun.ShotgunGun;
import net.saifs.neutron.item.gun.SniperGun;
import net.saifs.neutron.shop.ShopManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GunRegistry {
    private Neutron plugin;
    private final Map<GunItem, Double> guns;

    public GunRegistry(Neutron plugin) {
        this.plugin = plugin;
        this.guns = new HashMap<>();

        GunItem basicGun = createGun("gun", "§c§lBASIC GUN", new Particle.DustOptions(Color.RED, 4), 5, 20);
        basicGun.setExplosive(true);
        GunItem pistolGun = createGun("pistol", "§3§lPISTOL", new Particle.DustOptions(Color.GRAY, 4), 2, 10, 500);

        ItemStack sniperItem = NeutronHelper.constructItem(Material.TRIPWIRE_HOOK, "§4§lSNIPER RIFLE");
        SniperGun sniperGun = new SniperGun("sniper", sniperItem, plugin);
        ShotgunGun shotgunGun = new ShotgunGun("shotgun", NeutronHelper.constructItem(Material.STICK, "§9§lSHOTGUN"), plugin);

        guns.put(pistolGun, 100.0);
        guns.put(basicGun, 200.0);
        guns.put(sniperGun, 500.0);
        guns.put(shotgunGun, 500.0);
    }

    private GunItem createGun(String id, String name, Particle.DustOptions dustOptions, double damage, int ammo) {
        ItemStack itemStack = NeutronHelper.constructItem(Material.TRIPWIRE_HOOK, name);
        return new GunItem(id, itemStack, plugin, dustOptions, 0.75, damage, ammo);
    }

    private GunItem createGun(String id, String name, Particle.DustOptions dustOptions, double damage, int ammo, double delay) {
        ItemStack itemStack = NeutronHelper.constructItem(Material.TRIPWIRE_HOOK, name);
        return new GunItem(id, itemStack, plugin, dustOptions, 0.75, damage, ammo, delay);
    }

    public void registerGuns(ShopManager shopManager) {
        for (GunItem gun : this.guns.keySet()) {
            double price = guns.get(gun);
            shopManager.registerItem(gun.getID(), gun, price);
        }
    }
}
