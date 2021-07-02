package net.saifs.masonplugin;

import com.zaxxer.hikari.HikariDataSource;
import net.saifs.masonplugin.command.CommandHandler;
import net.saifs.masonplugin.data.Config;
import net.saifs.masonplugin.economy.Economy;
import net.saifs.masonplugin.item.GunItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public final class MasonPlugin extends JavaPlugin {
    private HikariDataSource hikari;
    private Config mainConfig;
    private Economy economy;
    private CommandHandler commandHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.mainConfig = new Config("config.yml", this);
        if (!this.connectHikari()) return;
        this.economy = new Economy(this, mainConfig.getConfig().getDouble("startingBalance"));
        this.commandHandler = new CommandHandler("mason", this);
        this.commandHandler.registerCommands();

        new GunItem(this);
    }

    private boolean connectHikari() {
        FileConfiguration config = this.mainConfig.getConfig();
        this.hikari = new HikariDataSource();

        String ip = config.getString("mysql.ip");
        int port = config.getInt("mysql.port");
        String db = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        hikari.setJdbcUrl("jdbc:mysql://" + ip + ":" + port);
        hikari.setUsername(username);
        hikari.setPassword(password);

        try {
            Connection connection = hikari.getConnection();
            PreparedStatement statement = connection.prepareStatement("CREATE DATABASE " + db);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hikari.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + db);

        return true;
    }

    public HikariDataSource getHikari() {
        return this.hikari;
    }

    public Economy getEconomy() {
        return this.economy;
    }
}
