/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import model.*;

/**
 *
 * @author ADMIN
 */
public class daoUser {
    
    private Connection connec;

    public daoUser(){
        try {
            String url="jdbc:mysql://localhost:3306/qlnv";
            String user="root";
            String pass="";
//            String jdbc_driver="com.mysql.cj.jdbc.Driver";
            connec=DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public ArrayList<User> getListUser() throws IOException{
//        ArrayList<User> listUsers=new ArrayList<>();
//        Scanner scanner=new Scanner(new File("C:\\WORKSPACE\\MiniProject\\Java_quanlyNV\\src\\data\\dataUser.in"));
//        while (scanner.hasNextLine()) {
//            listUsers.add(new User(scanner.nextLine(), scanner.nextLine()));
//        }
//        return listUsers;
//    }
    
    public ArrayList<User> getListUser(){
        ArrayList<User> list=new ArrayList<>();
        String sql="select * from `user`";
        try {
            PreparedStatement ps=connec.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                String s1=rs.getString("username");
                String s2=rs.getString("password");
                list.add(new User(s1,s2));
            }
        } catch (Exception e) {}
        return list;
    }
}
