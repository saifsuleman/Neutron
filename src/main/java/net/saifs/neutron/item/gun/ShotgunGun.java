package net.saifs.neutron.item.gun;

import net.saifs.neutron.Neutron;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ShotgunGun extends GunItem {
    public ShotgunGun(String id, ItemStack itemStack, Neutron plugin) {
        super(id, itemStack, plugin, new Particle.DustOptions(Color.WHITE, 4), 2, 20, 4, 10000);
        this.range = 4;
    }

    @Override
    protected void handlePlayerFire(Player player) {
        super.handlePlayerFire(player);
        Vector vector = player.getLocation().getDirection().clone().normalize().multiply(-3);
        player.setVelocity(player.getVelocity().clone().add(vector));
    }

    @Override
    protected void renderBullet(Location location) {
        super.renderBullet(location);
        World world = location.getWorld();
        if (world != null) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY, 4);
            world.spawnParticle(Particle.REDSTONE, location, 8, 1, 1, 1, dustOptions);
        }
    }
}
