/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.io.*;
import java.util.*;
import model.NhanVien;
import java.sql.*;
//import database.*;
/**
 *
 * @author ADMIN
 */
public class daoNV {

    private Connection connec;

    public daoNV(){
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


    // // doc du lieu tu file
    // public ArrayList<NhanVien> getlistNV() throws IOException {
    //     Scanner scanner=new Scanner(new File("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in"));
    //     ArrayList<NhanVien> list=new ArrayList<>();
    //     while (scanner.hasNextLine()) {
    //         list.add(new NhanVien(scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), Integer.valueOf(scanner.nextLine())));
    //     }
    //     return list;
    // }

    // doc du lieu tu co so du lieu mysql
    public ArrayList<NhanVien> getlistNV(){
        ArrayList<NhanVien> list=new ArrayList<>();
        String sql="select * from `nhanvien`";
        try {
            PreparedStatement ps=connec.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                String s1=rs.getString("maNV");
                String s2=rs.getString("hoTen");
                String s3=rs.getString("gioiTinh");
                String s4=rs.getString("ngaySinh");
                String s5=rs.getString("sdt");
                String s6=rs.getString("email");
                String s7=rs.getString("diaChi");
                String s8=rs.getString("phongBan");
                String s9=rs.getString("chucVu");
                int s10=rs.getInt("luongCB");
                list.add(new NhanVien(s1, s2, s3, s4, s5, s6, s7, s8, s9, s10));
            }
        } catch (Exception e) {}
        return list;
    }

    // // them nhan vien moi va ghi ket qua vao file
    // public void addNV(NhanVien nv) throws IOException{
    //     FileWriter writer=new FileWriter("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in",true);
    //     BufferedWriter bw=new BufferedWriter(writer);
    //     bw.newLine();bw.write(nv.getId());
    //     bw.newLine();bw.write(nv.getName());
    //     bw.newLine();bw.write(nv.getGender());
    //     bw.newLine();bw.write(nv.getDob());
    //     bw.newLine();bw.write(nv.getPhoneNumber());
    //     bw.newLine();bw.write(nv.getEmail());
    //     bw.newLine();bw.write(nv.getAddress());
    //     bw.newLine();bw.write(nv.getDepartment());
    //     bw.newLine();bw.write(nv.getPosition());
    //     bw.newLine();bw.write(String.valueOf(nv.getSalary()));
    //     bw.close();
    // }
    
    //  them nhan vien vao co so du lieu mysql
    public void addNV(NhanVien nv){
        String sql="insert into `nhanvien`(`maNV`,`hoTen`,`gioiTinh`,`ngaySinh`,`sdt`,`email`,`diaChi`,`phongBan`,`chucVu`,`luongCB`) values (?,?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement ps=connec.prepareStatement(sql);
            ps.setString(1, nv.getId());
            ps.setString(2, nv.getName());
            ps.setString(3, nv.getGender());
            ps.setString(4, nv.getDob());
            ps.setString(5, nv.getPhoneNumber());
            ps.setString(6, nv.getEmail());
            ps.setString(7, nv.getAddress());
            ps.setString(8, nv.getDepartment());
            ps.setString(9, nv.getPosition());
            ps.setInt(10, nv.getSalary());
            ps.executeUpdate();
            // Connection c=JDBCUtil.getConnection();
            // Statement st=c.createStatement();
            // st.executeUpdate(sql);
    
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    // // xoa nhan vien va luu ket qua vao file
    // public void deleteNV(ArrayList<NhanVien> tmp) throws IOException{
    //     try(FileWriter writer=new FileWriter("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in",false)) {
    //         writer.write("");
    //     } catch (IOException e) {
    //     }


    //     try (BufferedWriter w = new BufferedWriter(new FileWriter("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in", true))) {
    //         for(int i=0;i<tmp.size();i++){
    //             if(i!=tmp.size()-1){
    //                 w.write(tmp.get(i).getId());w.newLine(); w.write(tmp.get(i).getName());w.newLine();
    //                 w.write(tmp.get(i).getGender());w.newLine(); w.write(tmp.get(i).getDob());w.newLine();
    //                 w.write(tmp.get(i).getPhoneNumber());w.newLine(); w.write(tmp.get(i).getEmail());w.newLine();
    //                 w.write(tmp.get(i).getAddress());w.newLine(); w.write(tmp.get(i).getDepartment());w.newLine();
    //                 w.write(tmp.get(i).getPosition());w.newLine(); w.write(String.valueOf(tmp.get(i).getSalary()));w.newLine();
    //             } else{
    //                 w.write(tmp.get(i).getId());w.newLine(); w.write(tmp.get(i).getName());w.newLine();
    //                 w.write(tmp.get(i).getGender());w.newLine(); w.write(tmp.get(i).getDob());w.newLine();
    //                 w.write(tmp.get(i).getPhoneNumber());w.newLine(); w.write(tmp.get(i).getEmail());w.newLine();
    //                 w.write(tmp.get(i).getAddress());w.newLine(); w.write(tmp.get(i).getDepartment());w.newLine();
    //                 w.write(tmp.get(i).getPosition());w.newLine(); w.write(String.valueOf(tmp.get(i).getSalary()));
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }


    // xoa nhan vien va luu ket qua vao co so du lieu mysql
    public void deleteNV(String s){
        String sql="delete from nhanvien where maNV=?;";
        try {
            PreparedStatement ps=connec.prepareCall(sql);
            ps.setString(1, s);
            ps.executeUpdate();
            // Connection c=JDBCUtil.getConnection();
            // Statement st=c.createStatement();
            // st.executeUpdate(sql);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    // // sua thong tin nhan vien va ghi vao file
    // public void updateNV(ArrayList<NhanVien> tmp) throws IOException{
    //     try(FileWriter writer=new FileWriter("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in",false)) {
    //         writer.write("");
    //     } catch (IOException e) {}

    //     try (BufferedWriter w = new BufferedWriter(new FileWriter("C:\\WORKSPACE\\btldemo\\src\\data\\dataNV.in", true))) {
    //         for(int i=0;i<tmp.size();i++){
    //             if(i!=tmp.size()-1){
    //                 w.write(tmp.get(i).getId());w.newLine(); w.write(tmp.get(i).getName());w.newLine();
    //                 w.write(tmp.get(i).getGender());w.newLine(); w.write(tmp.get(i).getDob());w.newLine();
    //                 w.write(tmp.get(i).getPhoneNumber());w.newLine(); w.write(tmp.get(i).getEmail());w.newLine();
    //                 w.write(tmp.get(i).getAddress());w.newLine(); w.write(tmp.get(i).getDepartment());w.newLine();
    //                 w.write(tmp.get(i).getPosition());w.newLine(); w.write(String.valueOf(tmp.get(i).getSalary()));w.newLine();
    //             } else{
    //                 w.write(tmp.get(i).getId());w.newLine(); w.write(tmp.get(i).getName());w.newLine();
    //                 w.write(tmp.get(i).getGender());w.newLine(); w.write(tmp.get(i).getDob());w.newLine();
    //                 w.write(tmp.get(i).getPhoneNumber());w.newLine(); w.write(tmp.get(i).getEmail());w.newLine();
    //                 w.write(tmp.get(i).getAddress());w.newLine(); w.write(tmp.get(i).getDepartment());w.newLine();
    //                 w.write(tmp.get(i).getPosition());w.newLine(); w.write(String.valueOf(tmp.get(i).getSalary()));
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }



    
    
//    public static void main(String[] args) throws IOException {
//        new daoNV();
//    }
}
