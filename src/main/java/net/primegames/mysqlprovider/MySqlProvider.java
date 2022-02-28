/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.mysqlprovider;

import lombok.Getter;
import net.primegames.mysqlprovider.connection.MySqlConnectionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;

public class MySqlProvider {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "MySql" + ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

    @Getter
    private final Plugin plugin;
    @Getter 
    private final Connection connection;

    public MySqlProvider(Plugin plugin) {
        this.plugin = plugin;
        connection = init().getConnection();
    }

    public void scheduleTask(BukkitRunnable mySqlTask) {
        mySqlTask.runTaskAsynchronously(this.plugin);
    }

    public MySqlConnectionBuilder init() {
        plugin.saveResource("config.yml", false);
        return new MySqlConnectionBuilder(getCredentials());
    }

    private MysqlCredentials getCredentials() {
        FileConfiguration config = plugin.getConfig();
        if (config.getString("mysql.host") == null || config.getString("mysql.port") == null || config.getString("mysql.database") == null || config.getString("mysql.username") == null || config.getString("mysql.password") == null) {
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are missing in config.yml" + " of " + plugin.getName() + " plugin. Setting defaults...");
            setDefaults(config);
            plugin.saveConfig();
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are set to defaults, Make sure to set them correctly in config.yml of " + plugin.getName() + " plugin.");
        }
        String host = config.getString("mysql.host");
        int port = config.getInt("mysql.port");
        String database = config.getString("mysql.database");
        String username = config.getString("mysql.username");
        String password = config.getString("mysql.password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(FileConfiguration config) {
        config.set("mysql.host", "127.0.0.1");
        config.set("mysql.port", 3306);
        config.set("mysql.database", "core");
        config.set("mysql.username", "root");
        config.set("mysql.password", "password");
    }
}
