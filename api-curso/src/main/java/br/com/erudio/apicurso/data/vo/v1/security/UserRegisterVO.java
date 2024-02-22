package br.com.erudio.apicurso.data.vo.v1.security;

import java.io.Serializable;

public class UserRegisterVO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userName;
    private String fullName;
    private String password;

    public UserRegisterVO(String userName, String fullName, String password) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
