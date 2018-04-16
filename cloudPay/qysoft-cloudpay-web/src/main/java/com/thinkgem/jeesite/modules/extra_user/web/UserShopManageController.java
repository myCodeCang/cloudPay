/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.modules.extra_user.entity.ShopClassify;
import com.thinkgem.jeesite.modules.extra_user.service.ShopClassifyService;
import com.thinkgem.jeesite.modules.user.entity.UserLevel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.modules.extra_user.entity.UserShopManage;
import com.thinkgem.jeesite.modules.extra_user.service.UserShopManageService;

import java.util.List;

/**
 * 店铺审核Controller
 * @author luojie
 * @version 2017-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/extra_user/userShopManage")
public class UserShopManageController extends BaseController {

	@Autowired
	private UserShopManageService userShopManageService;

	@Autowired
	private ShopClassifyService shopClassifyService;
	@ModelAttribute
	public UserShopManage get(@RequestParam(required=false) String id) {
		UserShopManage entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = userShopManageService.get(id);
		}
		if (entity == null){
			entity = new UserShopManage();
		}
		return entity;
	}
	
	@RequiresPermissions("user:userShopManage:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserShopManage userShopManage, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserShopManage> page = userShopManageService.findPage(new Page<UserShopManage>(request, response), userShopManage); 
		model.addAttribute("page", page);
		return "modules/extra_user/userShopManageList";
	}

	@RequiresPermissions("user:userShopManage:view")
	@RequestMapping(value = "form")
	public String form(UserShopManage userShopManage, Model model) {
		java.util.List<ShopClassify> shopClassifies = shopClassifyService.findList(new ShopClassify());
		model.addAttribute("shopClassifys", shopClassifies);
		model.addAttribute("userShopManage", userShopManage);
		return "modules/extra_user/userShopManageForm";
	}

	@RequiresPermissions("user:userShopManage:edit")
	@RequestMapping(value = "save")
	public String save(UserShopManage userShopManage, Model model, RedirectAttributes redirectAttributes){
		if (!beanValidator(model, userShopManage)){
			return form(userShopManage, model);
		}
		try {
			userShopManageService.save(userShopManage);
			addMessage(redirectAttributes, "保存店铺信息修改成功");
			return "redirect:"+Global.getAdminPath()+"/extra_user/userShopManage/?repage";
		}catch (ValidationException e){
			addMessage(redirectAttributes, "失败:"+e.getMessage());
			return "redirect:"+Global.getAdminPath()+"/extra_user/userShopManage/?repage";
		}


	}
	
	@RequiresPermissions("user:userShopManage:edit")
	@RequestMapping(value = "delete")
	public String delete(UserShopManage userShopManage, RedirectAttributes redirectAttributes) {
		userShopManageService.delete(userShopManage);
		addMessage(redirectAttributes, "删除店铺信息修改成功");
		return "redirect:"+Global.getAdminPath()+"/extra_user/userShopManage/?repage";
	}
	@RequiresPermissions("user:userShopManage:edit")
	@RequestMapping(value = "updatestatus")
	public String updetstatus(UserShopManage userShopManage, String promValue, String promMsg, Model model, RedirectAttributes redirectAttributes) {
		try {
			userShopManageService.updateStatus(userShopManage,promValue,promMsg);
			addMessage(model, "成功");
		} catch (ValidationException e) {
			addMessage(model, "失败:"+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/extra_user/userShopManage/list";
	}
}