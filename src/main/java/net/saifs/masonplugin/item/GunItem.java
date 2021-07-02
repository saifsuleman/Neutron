package net.saifs.masonplugin.item;

import net.saifs.masonplugin.MasonPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Collection;

public class GunItem extends Item implements Listener {
    public GunItem(MasonPlugin plugin) {
        super("gun", constructItem(), plugin);
    }

    // static function to create item for super constructor
    private static ItemStack constructItem() {
        ItemStack itemStack = new ItemStack(Material.TRIPWIRE_HOOK, 1);
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack;
        meta.setDisplayName("§c§lJUST A GUN");
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!isItem(e.getItem())) {
            if (e.getPlayer().isSneaking()) {
                e.getPlayer().getInventory().addItem(getItemStack());
            }
            return;
        }

        Player player = e.getPlayer();
        Location bulletLocation = player.getLocation();
        double speed = 1.25;
        Vector vector = player.getLocation().getDirection().normalize().multiply(speed);

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                // move the bullet
                bulletLocation.add(vector);
                bulletLocation.getWorld().spawnParticle(Particle.REDSTONE, bulletLocation, 1);
                Collection<Entity> targets = bulletLocation.getWorld().getNearbyEntities(bulletLocation, 2, 2, 2);
                if (targets.size() > 0) {
                    for (Entity entity : targets) {
                        if (!(entity instanceof LivingEntity)) continue;
                        LivingEntity target = (LivingEntity) entity;
                        target.damage(4, player);
                        if (target instanceof Player) {
                            target.sendMessage("§7You've been hit by §a" + player.getName());
                        }
                    }
                    this.cancel();
                }
            }
        };
        runnable.runTaskTimer(getPlugin(), 0L, 0L);
    }
}
