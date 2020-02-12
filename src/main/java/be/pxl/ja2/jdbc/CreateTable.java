package be.pxl.ja2.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {

    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/musicdb?useSSL=false", "user", "password");
            Statement statement = conn.createStatement()) {
            statement.execute("CREATE TABLE contacts (id INTEGER NOT NULL AUTO_INCREMENT, name TEXT, phone INTEGER, email TEXT, PRIMARY KEY (id))");
        } catch (SQLException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
