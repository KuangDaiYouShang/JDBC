package com.us.dao.impl;

import annotation.MyAutoWired;
import annotation.MyRepository;
import com.ruanmou.vip.orm.core.handler.HandlerTemplate;
import com.us.dao.UserDao;
import com.us.entity.UserEntity;

@MyRepository
public class UserDaoImpl implements UserDao {

    @MyAutoWired
    private HandlerTemplate template;

    @Override
    public boolean login(UserEntity user) {
        return template.queryForList(UserEntity.class, user).size() > 0;
    }
}
