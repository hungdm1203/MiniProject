/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class NhanVien {
    private String id,name,gender,dob,phoneNumber,email,address,department,position;
    private int salary;

    public NhanVien(){
        
    }

    public NhanVien(String id, String name, String gender, String dob, String phoneNumber, String email, String address, String department, String position, int salary) {
        this.id = id.toUpperCase();
        this.gender='N'+gender.substring(1);
        this.phoneNumber=phoneNumber;
        this.address=address;
        if(email.contains("@gmail.com")) {
            this.email=email;
        } else this.email=email+"@gmail.com";
        this.position = position;
        this.department = department;
        this.salary = salary;

        String s[]=name.toUpperCase().split("\\s+"), res="";
        for (String string : s) res=res+string.charAt(0)+string.substring(1).toLowerCase()+" ";
        this.name=res.trim();
        if(dob.charAt(2)!='/') dob='0'+dob;
        if(dob.charAt(5)!='/') dob=dob.substring(0, 3)+'0'+dob.substring(3);
        this.dob=dob;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPosition() {
        return position;
    }

    public String getDepartment() {
        return department;
    }

    public int getSalary() {
        return salary;
    }

    public void setId(String id) {
        this.id = id.toUpperCase();
    }

    public void setName(String name) {
        String s[]=name.toUpperCase().split("\\s+"), res="";
        for (String string : s) res=res+string.charAt(0)+string.substring(1).toLowerCase()+" ";
        this.name=res.trim();
    }

    public void setDob(String dob) {
        if(dob.charAt(2)!='/') dob='0'+dob;
        if(dob.charAt(5)!='/') dob=dob.substring(0, 3)+'0'+dob.substring(3);
        this.dob=dob;
    }

    public void setGender(String gender) {
        this.gender = "N"+gender.substring(1);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if(email.contains("@gmail.com")) {
            this.email=email;
        } else this.email=email+"@gmail.com";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public void setSalary(int salary) {
        this.salary = salary;
    }
}
