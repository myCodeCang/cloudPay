/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.service;

import java.util.List;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.extra_user.entity.UserShopManage;
import com.thinkgem.jeesite.modules.extra_user.dao.UserShopManageDao;

/**
 * 店铺审核Service
 * @author luojie
 * @version 2017-07-12
 */
@Service
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class UserShopManageService extends CrudService<UserShopManageDao, UserShopManage> {
	@Autowired
	private UserUserinfoDao userUserinfoDao ;
	public UserShopManage get(String id) {
		return super.get(id);
	}
	
	public List<UserShopManage> findList(UserShopManage userShopManage) {
		return super.findList(userShopManage);
	}
	
	public Page<UserShopManage> findPage(Page<UserShopManage> page, UserShopManage userShopManage) {
		return super.findPage(page, userShopManage);
	}
	

	public void save(UserShopManage userShopManage) throws ValidationException {
		if (userShopManage.getIsNewRecord()){
			UserShopManage currUser = this.getByUserName(userShopManage.getUserName());
			if(currUser != null){
				throw new ValidationException("每个用户,只能有一家店铺");
			}
			userShopManage.preInsert();
			dao.insert(userShopManage);
		}else{
			userShopManage.preUpdate();
			dao.update(userShopManage);
		}
	}
	

	public void delete(UserShopManage userShopManage) {
		super.delete(userShopManage);
	}

	public void updateStatus(UserShopManage userShopManage, String status, String message) throws ValidationException {

		if(!userShopManage.getShopStatus ().equals (EnumUtil.CheckType.uncheck.toString())){
			throw new ValidationException ("已审核过,不要重复审核");
		}
		userShopManage.setMessage("审核已被驳回,驳回原因: "+message);
		if(status.equals(EnumUtil.YesNO.NO.toString())){
			userShopManage.setShopStatus(EnumUtil.CheckType.failed.toString());
		}
		else{
			userShopManage.setShopStatus(EnumUtil.CheckType.success.toString());
			userUserinfoDao.updateIsShop(userShopManage.getUserName(),EnumUtil.YesNO.YES.toString());
		}
		dao.updateStatus(userShopManage);
//		if( status.equals(EnumUtil.CheckType.success)){
//			userUserinfoService.updateUserMoney(userShopManage.getUserName(), new BigDecimal(userShopManage.getChangeMoney()), message, EnumUtil.MoneyChangeType.charget);
//		}

	}


	public UserShopManage getByUserName(String userName) {
		return dao.getByUserName(userName);
	}
}