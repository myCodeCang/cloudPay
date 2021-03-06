/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.extra_user.entity.ShopClassify;

/**
 * 商家分类DAO接口
 * @author luojie
 * @version 2017-07-14
 */
@MyBatisDao
public interface ShopClassifyDao extends CrudDao<ShopClassify> {
	
}