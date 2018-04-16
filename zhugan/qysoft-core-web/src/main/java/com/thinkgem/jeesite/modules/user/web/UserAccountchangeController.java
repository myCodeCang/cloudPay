/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.user.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.utils.StringUtils2;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.service.UserAccountchangeService;

import java.math.BigDecimal;

/**
 * 用户帐变明细Controller
 * @author xueyuliang
 * @version 2017-03-23
 */
@Controller
@RequestMapping(value = "${adminPath}/user/userAccountchange")
public class UserAccountchangeController extends BaseController {

	@Resource
	private UserAccountchangeService userAccountchangeService;
	
	/**
	 * 根据id获取这个对象
	 * @param id
	 * @return
	 */
	@ModelAttribute
	public UserAccountchange get(@RequestParam(required=false) String id) {

		UserAccountchange entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = userAccountchangeService.get(id);
		}
		if (entity == null){
			entity = new UserAccountchange();
		}
		return entity;
	}
	
	/**
	 * 获取列表
	 * @param userAccountchange
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user:userAccountchange:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserAccountchange userAccountchange, HttpServletRequest request, HttpServletResponse response, Model model) {

		userAccountchange.setOrderBy("id desc");
		Page<UserAccountchange> page = userAccountchangeService.findPage(new Page<UserAccountchange>(request, response), userAccountchange); 
		model.addAttribute("page", page);
		return "modules/user/userAccountchangeList";
	}

}