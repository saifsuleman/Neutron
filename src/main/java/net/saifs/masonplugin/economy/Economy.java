package net.saifs.masonplugin.economy;

import net.saifs.masonplugin.MasonPlugin;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Economy {
    private final MasonPlugin plugin;
    private final double defaultBalance;

    public Economy(MasonPlugin plugin, double defaultBalance) {
        this.plugin = plugin;
        this.defaultBalance = defaultBalance;
    }

    public double getPlayerBalance(OfflinePlayer player) {
        try {
            Connection connection = plugin.getHikari().getConnection();
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
        return this.defaultBalance;
    }

    public void setPlayerBalance(OfflinePlayer player, double balance) {
        try {
            Connection connection = plugin.getHikari().getConnection();
            boolean exists = this.playerEntryExists(player);

            String query = exists ? "UPDATE coins SET balance=? WHERE uuid=?" :
                    "INSERT INTO coins (balance, uuid) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, balance);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void giveMoney(OfflinePlayer player, double amount) {
        double balance = this.getPlayerBalance(player) + amount;
        this.setPlayerBalance(player, balance);
    }

    public void removeMoney(OfflinePlayer player, double amount) {
        double balance = this.getPlayerBalance(player) - amount;
        this.setPlayerBalance(player, balance);
    }

    private boolean playerEntryExists(OfflinePlayer player) {
        try {
            Connection connection = plugin.getHikari().getConnection();
            String query = "SELECT * FROM coins WHERE uuid=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            return results.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
