/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.extra_user.entity.ShopClassify;
import com.thinkgem.jeesite.modules.extra_user.dao.ShopClassifyDao;

/**
 * 商家分类Service
 * @author luojie
 * @version 2017-07-14
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class ShopClassifyService extends CrudService<ShopClassifyDao, ShopClassify> {

	public ShopClassify get(String id) {
		return super.get(id);
	}
	
	public List<ShopClassify> findList(ShopClassify shopClassify) {
		return super.findList(shopClassify);
	}
	
	public Page<ShopClassify> findPage(Page<ShopClassify> page, ShopClassify shopClassify) {
		return super.findPage(page, shopClassify);
	}
	

	public void save(ShopClassify shopClassify) {
		super.save(shopClassify);
	}
	

	public void delete(ShopClassify shopClassify) {
		super.delete(shopClassify);
	}
	
}