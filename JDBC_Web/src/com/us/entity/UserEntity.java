package com.us.entity;

import com.ruanmou.vip.orm.annotation.Column;
import com.ruanmou.vip.orm.annotation.PK;
import com.ruanmou.vip.orm.annotation.Table;

@Table("t_user")
public class UserEntity {

    @PK
    @Column("user_id")
    private Integer userID;
    @Column("user_account")
    private String account;
    @Column("user_password")
    private String password;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
