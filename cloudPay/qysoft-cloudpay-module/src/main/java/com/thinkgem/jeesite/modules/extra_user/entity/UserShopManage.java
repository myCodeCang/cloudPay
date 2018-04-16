/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.entity;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 店铺审核Entity
 * @author luojie
 * @version 2017-07-12
 */
public class UserShopManage extends DataEntity<UserShopManage> {
	
	private static final long serialVersionUID = 1L;
	

	
	private String userName;		// 用户名
	private String shopName;		// 店铺名称
	private String storefrontImg;		// 店铺图片
	private String storefrontImg1;		// 店铺图片
	private String storefrontImg2;		// 店铺图片
	private String businessLicImg;		// 营业执照
	private String idCardImg1;		// 身份证图片
	private String idCardImg2;		// 身份证图片
	private String idCardImg3;		// 身份证图片
	private String idCard;     //身份证号码
	private String mobile; //手机号
	private String classifyId;   //分类Id
	private String classifyName; // 商铺分类名称
	private String shopAddress;		// 店铺地址
	private String shopStatus;		// 店铺审核状态(0未审核,1通过,2驳回)
	private String addressX; //地址坐标
	private String addressY;
	//扩展字段
	private String trueName;
	private String message;  //驳回的原因

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	public  void validateModule(boolean isInsert) throws ValidationException {
		if(StringUtils2.isBlank(userName)){
			throw new ValidationException("用户名不能为空!");
		}
		if(StringUtils2.isBlank(shopName)){
			throw new ValidationException("店铺名称不能为空!");
		}
		if(StringUtils2.isBlank(mobile)){
			throw new ValidationException("手机号不能为空!");
		}
//		if(StringUtils2.isBlank(shopAddress)){
//			throw new ValidationException("店铺地址不能为空!");
//		}

	}

	@Override
	public void preInsert() throws ValidationException {
		validateModule(true);
		if(StringUtils2.isBlank(shopStatus)){
			this.setShopStatus("0");
		}
		super.preInsert();

	}


	@Override
	public void preUpdate() throws ValidationException {
		validateModule(false);
		super.preUpdate();

	}

	public String getStorefrontImg1() {
		return storefrontImg1;
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setStorefrontImg1(String storefrontImg1) {
		this.storefrontImg1 = storefrontImg1;
	}

	public String getStorefrontImg2() {
		return storefrontImg2;
	}

	public void setStorefrontImg2(String storefrontImg2) {
		this.storefrontImg2 = storefrontImg2;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public UserShopManage() {
		super();
	}

	public UserShopManage(String id){
		super(id);
	}

	@Length(min=0, max=225, message="用户名长度必须介于 0 和 225 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	@Length(min=0, max=255, message="店铺名称长度必须介于 0 和 255 之间")
	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getIdCardImg1() {
		return idCardImg1;
	}

	public String getIdCard() {
		return idCard;
	}

	public String getAddressX() {
		return addressX;
	}

	public void setAddressX(String addressX) {
		this.addressX = addressX;
	}

	public String getAddressY() {
		return addressY;
	}

	public void setAddressY(String addressY) {
		this.addressY = addressY;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public void setIdCardImg1(String idCardImg1) {
		this.idCardImg1 = idCardImg1;
	}

	public String getIdCardImg2() {
		return idCardImg2;
	}

	public void setIdCardImg2(String idCardImg2) {
		this.idCardImg2 = idCardImg2;
	}

	public String getIdCardImg3() {
		return idCardImg3;
	}

	public void setIdCardImg3(String idCardImg3) {
		this.idCardImg3 = idCardImg3;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getclassifyId() {
		return classifyId;
	}

	public void setclassifyId(String classifyId) {
		this.classifyId = classifyId;
	}

	@Length(min=0, max=255, message="店铺图片长度必须介于 0 和 255 之间")
	public String getStorefrontImg() {
		return storefrontImg;
	}

	public void setStorefrontImg(String storefrontImg) {
		this.storefrontImg = storefrontImg;
	}
	
	@Length(min=0, max=255, message="营业执照长度必须介于 0 和 255 之间")
	public String getBusinessLicImg() {
		return businessLicImg;
	}

	public void setBusinessLicImg(String businessLicImg) {
		this.businessLicImg = businessLicImg;
	}
	
	@Length(min=0, max=255, message="身份证图片长度必须介于 0 和 255 之间")
	public String getIdCardImg() {
		return idCardImg1;
	}

	public void setIdCardImg(String idCardImg) {
		this.idCardImg1 = idCardImg;
	}
	
	@Length(min=0, max=255, message="店铺地址长度必须介于 0 和 255 之间")
	public String getShopAddress() {
		return shopAddress;
	}

	public void setShopAddress(String shopAddress) {
		this.shopAddress = shopAddress;
	}
	
	@Length(min=0, max=1, message="店铺审核状态(0未审核,1通过,2驳回)长度必须介于 0 和 1 之间")
	public String getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(String shopStatus) {
		this.shopStatus = shopStatus;
	}
	
	

	
}