package com.us.dao;

import com.ruanmou.vip.orm.core.handler.HandlerTemplate;
import com.us.entity.UserEntity;

public interface UserDao {
    public void setTemplate(HandlerTemplate template);

    public boolean login(UserEntity user);
}
