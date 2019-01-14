package com.us.service;

import com.us.dao.UserDao;
import com.us.entity.UserEntity;

public interface UserService {
    public boolean login(UserEntity user);

    public void setUserDao(UserDao ud);
}
