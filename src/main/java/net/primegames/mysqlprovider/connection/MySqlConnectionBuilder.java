/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.mysqlprovider.connection;


import net.primegames.mysqlprovider.MySqlProvider;
import net.primegames.mysqlprovider.MysqlCredentials;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySqlConnectionBuilder {

    private Connection connection;

    public MySqlConnectionBuilder(MysqlCredentials credentials) {
        Bukkit.getLogger().info(MySqlProvider.PREFIX + "Connecting to MySQL...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionUri = "jdbc:mysql://" + credentials.getHost() + ":" + credentials.getPort() + "/" + credentials.getDatabase() + "?autoReconnect=true&useGmtMillisForDatetimes=true&serverTimezone=GMT";
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(connectionUri, credentials.getUsername(), credentials.getPassword());
            connection.setAutoCommit(true);
            Bukkit.getLogger().info(MySqlProvider.PREFIX + "Mysql Connection to Core Database successfully established");
        } catch (SQLException ex) {
            Bukkit.getLogger().warning(MySqlProvider.PREFIX + "Could not Establish connection with MySQL database");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Bukkit.getLogger().log(Level.SEVERE, MySqlProvider.PREFIX + "MySQL Driver is missing.. Needs dblib");
            ex.printStackTrace();
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

}
