package com.vishalchavda.myapplication.Models;

public class ModelSign_up {
    String  userName,email,password ;

    public ModelSign_up( String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public ModelSign_up() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
