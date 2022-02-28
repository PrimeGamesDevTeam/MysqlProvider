package net.primegames.mysqlprovider;

import lombok.Data;
import net.primegames.mysqlprovider.connection.MySqlConnectionBuilder;

import java.sql.Connection;

@Data
public class MysqlCredentials {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;


    public MysqlCredentials(String host, String user, String password, String database) {
        this.host = host;
        this.username = user;
        this.password = password;
        this.database = database;
        port = 3306;
    }

    public MysqlCredentials(String host, String user, String password, String database, int port) {
        this.host = host;
        this.username = user;
        this.password = password;
        this.database = database;
        this.port = port;
    }

    public Connection createConnection() {
        return new MySqlConnectionBuilder(this).getConnection();
    }
}
