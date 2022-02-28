/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.mysqlprovider;

import lombok.Getter;
import net.primegames.mysqlprovider.connection.ConnectionId;
import net.primegames.mysqlprovider.connection.MySqlConnectionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.util.Map;

public class MySqlProvider {

    public static final String PREFIX = ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "MySql" + ChatColor.DARK_PURPLE + "] " + ChatColor.RESET;

    @Getter
    private final Plugin plugin;
    private Map<ConnectionId, Connection> connections;

    public MySqlProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    public void scheduleTask(BukkitRunnable mySqlTask) {
        mySqlTask.runTaskAsynchronously(this.plugin);
    }


    public Map<ConnectionId, Connection> getConnections() {
        return connections;
    }

    public Connection getConnection(ConnectionId connectionId){
        return this.connections.get(connectionId);
    }

    public void createConnection(ConnectionId connectionId, MysqlCredentials credentials){
        try {
            this.connections.put(connectionId, credentials.createConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public MySqlConnectionBuilder buildCoreConnection(Plugin plugin) {
        plugin.saveDefaultConfig();
        return new MySqlConnectionBuilder(getCredentials(plugin));
    }

    private MysqlCredentials getCredentials(Plugin plugin) {
        FileConfiguration config = plugin.getConfig();
        if (config.getString("core.mysql.host") == null || config.getString("core.mysql.port") == null || config.getString("core.mysql.database") == null || config.getString("core.mysql.username") == null || config.getString("core.mysql.password") == null) {
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are missing in config.yml" + " of " + plugin.getName() + " plugin. Setting defaults...");
            setDefaults(config);
            plugin.saveConfig();
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "MySQL Credentials are set to defaults, Make sure to set them correctly in config.yml of " + plugin.getName() + " plugin.");
        }
        String host = config.getString("core.mysql.host");
        int port = config.getInt("core.mysql.port");
        String database = config.getString("core.mysql.database");
        String username = config.getString("core.mysql.username");
        String password = config.getString("core.mysql.password");
        return new MysqlCredentials(host, username, password, database, port);
    }

    private static void setDefaults(FileConfiguration config) {
        config.set("core.mysql.host", "127.0.0.1");
        config.set("core.mysql.port", 3306);
        config.set("core.mysql.database", "core");
        config.set("core.mysql.username", "root");
        config.set("core.mysql.password", "password");
    }
}
