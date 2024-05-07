/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import javax.swing.*;

import model.*;
import controller.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author ADMIN
 */
public class LoginView extends JFrame implements ActionListener {
    private JLabel usernamLabel=new JLabel("UserName");
    private JLabel passwordLabel=new JLabel("Password");
    private JTextField usernamField=new JTextField(15);
    private JPasswordField passwordField=new JPasswordField(15);
    private JPanel panel=new JPanel();
    private JButton ok=new JButton("OK");
    private ArrayList<User> listUser;

    public LoginView(String s) throws HeadlessException{
        super(s);

        SpringLayout layout=new SpringLayout();
        panel.add(usernamLabel);panel.add(usernamField);
        panel.add(passwordLabel);panel.add(passwordField);
        panel.add(ok);
        panel.setSize(400,300);
        panel.setLayout(layout);

        layout.putConstraint(SpringLayout.WEST, usernamLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, usernamLabel, 80, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, passwordLabel, 20, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, passwordLabel, 105, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, usernamField, 100, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, usernamField, 80, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, passwordField, 100, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, passwordField, 105, SpringLayout.NORTH, panel);        
        layout.putConstraint(SpringLayout.WEST, ok, 100, SpringLayout.WEST, panel);
        layout.putConstraint(SpringLayout.NORTH, ok, 130, SpringLayout.NORTH, panel);

        add(panel);
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);
        ok.addActionListener(this);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            listUser=new daoUser().getListUser();
        } catch (Exception ex) {}

        int ok=0;
        for (User user : listUser) {
            if(usernamField.getText().equals(user.getAcc()) && String.valueOf(passwordField.getPassword()).equals(user.getPass())){
                ok=1;break;
            }
        }
        if(ok==1){
            setVisible(false);
            new qlnvView().setVisible(true);
            
        } else {
            showMessage("Thong tin dang nhap khong dung");
        }        
    }

    
}
