package net.saifs.neutron;

import com.zaxxer.hikari.HikariDataSource;
import net.saifs.neutron.command.CommandHandler;
import net.saifs.neutron.data.Config;
import net.saifs.neutron.economy.Economy;
import net.saifs.neutron.item.gun.registry.GunRegistry;
import net.saifs.neutron.ontime.PlaytimeTracker;
import net.saifs.neutron.shop.ShopManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Neutron extends JavaPlugin {
    private HikariDataSource hikari;
    private Config mainConfig;
    private Economy economy;
    private ShopManager shopManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.mainConfig = new Config("config.yml", this);
        if (!this.connectHikari()) return;
        this.economy = new Economy(this, mainConfig.getConfig().getDouble("startingBalance"));

        CommandHandler commandHandler = new CommandHandler("neutron", this);
        commandHandler.registerCommands();

        new PlaytimeTracker(this);

        this.shopManager = new ShopManager(this);

        GunRegistry gunRegistry = new GunRegistry(this);
        gunRegistry.registerGuns(shopManager);
        shopManager.constructShopGUI();
    }

    private boolean connectHikari() {
        FileConfiguration config = this.mainConfig.getConfig();
        this.hikari = new HikariDataSource();

        String ip = config.getString("mysql.ip");
        int port = config.getInt("mysql.port");
        String db = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        hikari.setJdbcUrl("jdbc:mysql://" + ip + ":" + port + "/" + db);
        hikari.setUsername(username);
        hikari.setPassword(password);
        hikari.addDataSourceProperty("useSSL", false);
        hikari.addDataSourceProperty("allowPublicKeyRetrieval", true);
        hikari.addDataSourceProperty("autoReconnect", false);
        hikari.addDataSourceProperty("idleTimeout", 30000);

        return true;
    }

    public HikariDataSource getHikari() {
        return this.hikari;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }
}
