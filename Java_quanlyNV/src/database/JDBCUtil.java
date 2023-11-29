/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;
import java.sql.*;
import com.mysql.jdbc.Driver;
/**
 *
 * @author ADMIN
 */
public class JDBCUtil {
    /**
     * @return
     */
    public static Connection getConnection(){
        Connection c=null;
       
        try {
            // Register JDBC driver (This is no longer needed for MySQL Connector/J 8.0.21 and later)
            // Class.forName("com.mysql.jdbc.Driver"); // You don't need this line anymore
            
            // Database URL
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/qlnv";
            String username = "root";
            String password = "Nho1nguoi";
            
            // Get connection
            c = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void closeConnection(Connection c){
        try {
            if(c!=null){
                c.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void printInfo(Connection c){
        try {
            if(c!=null){
                DatabaseMetaData dt=c.getMetaData();
                System.out.println(dt.getDatabaseProductName());
                System.out.println(dt.getDatabaseProductVersion());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
