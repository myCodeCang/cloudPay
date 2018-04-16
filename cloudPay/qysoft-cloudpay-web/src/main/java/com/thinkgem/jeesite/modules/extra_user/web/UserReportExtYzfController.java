/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.extra_user.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.user.entity.UserReport;
import com.thinkgem.jeesite.modules.user.service.UserReportService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户奖金Controller
 * @author xueyuliang
 * @version 2017-04-25
 */
@Controller
@RequestMapping(value = "${adminPath}/user/yzfReport")
public class UserReportExtYzfController extends BaseController {

	@Resource
	private UserReportService userReportService;
	@Resource
	private UserUserinfoService userUserinfoService;
	
	@ModelAttribute
	public UserReport get(@RequestParam(required=false) String id) {
		UserReport entity = null;
		if (StringUtils2.isNotBlank(id)){
			entity = userReportService.get(id);
		}
		if (entity == null){
			entity = new UserReport();
		}
		return entity;
	}
	
	@RequiresPermissions("user:userReport:view")
	@RequestMapping(value = {"list", ""})
	public String list(UserReport userReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<UserReport> page = userReportService.findPage(new Page<UserReport>(request, response), userReport);
		model.addAttribute("page", page);
		return "modules/user/userReportList";
	}

	@RequiresPermissions("user:userReport:view")
	@RequestMapping(value = "organList")
	public String organList(UserReport userReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		UserReport report = userReportService.getOrganReport(userReport);
		model.addAttribute("organ",userReportService.countBonusReport(report));
		return "modules/user/userReportOrganList";
	}

	@RequiresPermissions("user:userReport:view")
	@RequestMapping(value = "teamList")
	public String teamList(UserReport userReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		UserReport report = userReportService.getTeamReport(userReport);
		model.addAttribute("team", userReportService.countBonusReport(report));
		return "modules/user/userReportTeamList";
	}

	@RequiresPermissions("user:userReport:view")
	@RequestMapping(value = "yzfReportView")
	public String yzfReportView(UserReport userReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		UserReport yzfReport = new UserReport();
		if(userReport.getCreateDateBegin()!= null && userReport.getCreateDateEnd()!= null && userReportService.sumUserReport(userReport)!=null){
			yzfReport = userReportService.sumUserReport(userReport);
		}
		yzfReport.setDividendNine(userUserinfoService.getSumMoney2());
		model.addAttribute("yzfReport",yzfReport);
		model.addAttribute("createDateBegin", userReport.getCreateDateBegin());
		model.addAttribute("createDateEnd", userReport.getCreateDateEnd());
		return "modules/user/reportView";
	}

}