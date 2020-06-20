package org.project.app.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBHandler extends Configs{

    private static String connectionString = "jdbc:mysql://" +Configs.dbhost + ":" + Configs.dbport + "/" + Configs.dbname+"?useUnicode=true&serverTimezone=UTC&useSSL=false&autoReconnect=true";

    public Connection getConnection()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection dbconnection = null;
        try {
            dbconnection = DriverManager.getConnection(connectionString, Configs.dbuser, Configs.dbpass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbconnection;
    }

    public void setConnectionString(String connectionString){
        this.connectionString = connectionString;
    }
}
