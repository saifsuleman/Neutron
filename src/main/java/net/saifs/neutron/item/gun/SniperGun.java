package net.saifs.neutron.item.gun;

import net.saifs.neutron.Neutron;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class SniperGun extends GunItem {
    public SniperGun(String id, ItemStack itemStack, Neutron plugin) {
        super(id, itemStack, plugin, new Particle.DustOptions(Color.RED, 1), 0.75, 0, 20);
    }

    @Override
    protected void handlePlayerFire(Player player) {
        Location bulletLocation = player.getLocation().add(new Vector(0, 1, 0));
        Vector vector = player.getLocation().getDirection().normalize().multiply(this.speed);

        while (true) {
            List<LivingEntity> entities = this.getCollidingEntities(player, bulletLocation);

            bulletLocation.add(vector);
            this.renderBullet(bulletLocation);

            if (entities == null) continue;
            if (entities.size() > 0 || bulletLocation.getBlock().getType() != Material.AIR) {
                for (LivingEntity target : entities) {
                    target.setHealth(0);
                    if (target instanceof Player) {
                        target.sendMessage("§7You've been sniped by §a" + player.getName());
                    }
                }

                break;
            }
        }
    }

    private List<LivingEntity> getCollidingEntities(Player shooter, Location bullet) {
        World world = bullet.getWorld();
        if (world == null) return null;

        return world.getNearbyEntities(bullet, 1, 1, 1).stream().filter(entity -> entity instanceof LivingEntity && entity != shooter)
                .map(entity -> (LivingEntity) entity).collect(Collectors.toList());
    }
}
