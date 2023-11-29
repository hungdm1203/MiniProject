/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.*;
import java.util.*;
import model.*;

/**
 *
 * @author ADMIN
 */
public class daoUser {
    public ArrayList<User> getListUser() throws IOException{
        ArrayList<User> listUsers=new ArrayList<>();
        Scanner scanner=new Scanner(new File("C:\\WORKSPACE\\MiniProject\\Java_quanlyNV\\src\\data\\dataUser.in"));
        while (scanner.hasNextLine()) {
            listUsers.add(new User(scanner.nextLine(), scanner.nextLine()));
        }
        return listUsers;
    }
}
