package net.saifs.neutron.economy;

import net.saifs.neutron.Neutron;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Economy implements Listener {
    private final Neutron plugin;
    private final double defaultBalance;

    public Economy(Neutron plugin, double defaultBalance) {
        this.plugin = plugin;
        this.defaultBalance = defaultBalance;
        try {
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    // alerts via logger if function is called from main thread (not async)
    private void monitorActiveThread() {
        if (Bukkit.isPrimaryThread()) {
            try {
                throw new IllegalAccessException("async function called from sync thread");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void createTables() throws SQLException {
        try (Connection connection = plugin.getHikari().getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS coins (uuid VARCHAR(255) PRIMARY KEY, balance INT(10) NOT NULL)";
            connection.prepareStatement(query).executeUpdate();
        }
    }

    public double getPlayerBalance(OfflinePlayer player) {
        monitorActiveThread();
        try (Connection connection = plugin.getHikari().getConnection()) {
            String query = "SELECT balance FROM coins WHERE uuid=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setPlayerBalance(player, this.defaultBalance);
        return this.defaultBalance;
    }

    public void setPlayerBalance(OfflinePlayer player, double balance) {
        monitorActiveThread();
        try (Connection connection = plugin.getHikari().getConnection()) {
            String query = "INSERT INTO coins (uuid, balance) VALUES (?, ?) ON DUPLICATE KEY UPDATE balance = VALUES(balance)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, player.getUniqueId().toString());
            statement.setDouble(2, balance);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void giveMoney(OfflinePlayer player, double amount) {
        monitorActiveThread();
        double balance = this.getPlayerBalance(player) + amount;
        this.setPlayerBalance(player, balance);
    }

    public void removeMoney(OfflinePlayer player, double amount) {
        monitorActiveThread();
        double balance = this.getPlayerBalance(player) - amount;
        this.setPlayerBalance(player, balance);
    }

}
