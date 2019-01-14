package com.us.service.impl;

import annotation.MyAutoWired;
import annotation.MyRepository;
import com.us.dao.UserDao;
import com.us.entity.UserEntity;
import com.us.service.UserService;

@MyRepository
public class UserServiceImpl implements UserService {

    @MyAutoWired
    private UserDao ud;

    @Override
    public boolean login(UserEntity user) {
       return ud.login(user);
    }

    @Override
    public void setUserDao(UserDao ud) {
        this.ud = ud;
    }
}
