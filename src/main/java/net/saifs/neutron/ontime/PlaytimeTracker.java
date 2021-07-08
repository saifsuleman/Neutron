package net.saifs.neutron.ontime;

import net.saifs.neutron.Neutron;
import net.saifs.neutron.helper.NeutronHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PlaytimeTracker extends BukkitRunnable implements Listener {
    private final Neutron plugin;
    private final Map<UUID, Long> joinTimes;

    // checks all players (and modifies bal asynchronously) at intervals of 60secs
    public PlaytimeTracker(Neutron plugin) {
        this.plugin = plugin;
        this.joinTimes = new HashMap<>();

        this.createTables();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.runTaskTimerAsynchronously(plugin, 1200, 1200);
    }

    private void createTables() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS play_times (uuid VARCHAR(255) PRIMARY KEY, playtime BIGINT(8) NOT NULL)";
            Connection connection = plugin.getHikari().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // On player join, get their playtime from DB - pull it into cache asynchronously
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Connection connection = plugin.getHikari().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT playtime FROM play_times WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();

                long playTime = results.next() ? results.getLong("playtime") : 0;
                this.joinTimes.put(e.getPlayer().getUniqueId(), System.currentTimeMillis() - playTime);
                long delay = 6000 - (playTime % 6000);

                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        rewardPlayer(uuid);
                    }
                };
                runnable.runTaskLaterAsynchronously(plugin, delay);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void rewardPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            player.sendMessage("§aYou've played for five minutes and have received $50!");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> plugin.getEconomy().giveMoney(player, 50));
        }
    }

    // Every minute, dump playtime cache into database
    @Override
    public void run() {
        try {
            StringBuilder queryBuilder = new StringBuilder("INSERT INTO play_times (uuid, playtime) VALUES ");
            for (int i = 0; i < joinTimes.size(); i++) {
                queryBuilder.append("(?, ?)");
                if (i < joinTimes.size() - 1) queryBuilder.append(", ");
            }
            queryBuilder.append(" ON DUPLICATE KEY UPDATE uuid=VALUES(uuid), playtime=VALUES(playtime)");

            Connection connection = plugin.getHikari().getConnection();
            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());

            int counter = 1;
            for (UUID uuid : joinTimes.keySet()) {
                long played = System.currentTimeMillis() - joinTimes.get(uuid);
                statement.setString(counter, uuid.toString());
                counter++;
                statement.setLong(counter, played);
                counter++;
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            for (UUID uuid : joinTimes.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player == null || !player.isOnline()) {
                    this.joinTimes.remove(uuid);
                }
            }
        });
    }
}
