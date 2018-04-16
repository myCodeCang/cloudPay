/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.extra_user.entity.UserShopManage;

/**
 * 店铺审核DAO接口
 * @author luojie
 * @version 2017-07-12
 */
@MyBatisDao
public interface UserShopManageDao extends CrudDao<UserShopManage> {
    public void updateStatus(UserShopManage userShopManage);

    public UserShopManage getByUserName(String userName);
}

