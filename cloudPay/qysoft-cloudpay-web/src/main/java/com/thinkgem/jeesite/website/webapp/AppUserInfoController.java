/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO2;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.extra_user.entity.UserShopManage;
import com.thinkgem.jeesite.modules.extra_user.service.UserShopManageService;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.user.entity.*;
import com.thinkgem.jeesite.modules.user.service.*;
import com.thinkgem.jeesite.modules.userExt_Yzf.service.UserInfoExtYzfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * 用户个人信息
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/app/userinfo")
public class AppUserInfoController extends BaseController {
    @Resource
    private SessionDAO2 sessionDAO2;
    @Resource
    private UserAccountchangeService userAccountchangeService;

    @Autowired
    UserMsmService userMsmService;
    @Resource
    private UserChargeLogService userChargeLogService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private UserUserinfoBankService userUserinfoBankService;
    @Resource
    private UserUserinfoService userUserinfoService;
    @Resource
    private UserBankWithdrawService userBankWithdrawService;

    @Resource
    private UserLevelService userLevelService;

    @Resource
    private UserReportService userReportService;

    @Resource
    private UserPhotoService userPhotoService;

    @Resource
    private UserInfoExtYzfService userInfoExtYzfService;

    @Resource
    private AreaService areaService;

    @Resource
    private UserBankChargeService userBankChargeServie;
    @Autowired
    private UserWithdrawService userWithdrawService ;

    /**
     * 验证专区密码
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/verifyZhuanquPass", method = RequestMethod.POST)
    public String verifyZhuanquPass(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        String zhuanquPass = request.getParameter ("zhuanquPass");
        if (!SystemService.validatePassword (zhuanquPass, userinfo.getZhuanquPass ())) {
            return error ("二级密码输入错误", response, model);
        }else{
            return success ("成功!!", response, model);
        }
    }

    /**
     * 转账
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/transferAccounts", method = RequestMethod.POST)
    public String transferAccounts(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = userUserinfoService.get(UserInfoUtils.getUser().getId());
        String currMobile = request.getParameter("mobile");
        String money = request.getParameter("money");
        String message = request.getParameter("message");
        String password = request.getParameter("password");
        if(!SystemService.validatePassword (password,userinfo.getBankPassword ())){
            return error ("支付密码输入错误", response, model);
        }
        try{
            userInfoExtYzfService.userTransferAccounts(userinfo.getUserName(),currMobile,new BigDecimal(money),message);
            return success("转账成功",response,model);
        }catch (ValidationException e){
            return error("失败:"+ e.getMessage(),response,model);
        }
    }


    /**
     * 等级列表
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userLevelInfo", method = RequestMethod.POST)
    public String userLevelList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String levelId = request.getParameter("levelId");
        model.addAttribute("userLevelInfo",userLevelService.findByLevalCode(levelId));
        return success ("成功!!", response, model);

    }

    /**
     * 团队列表
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/teamlistCount", method = RequestMethod.POST)
    public String teamlistCount(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = new UserUserinfo();
        userinfo.setParentListLike(UserInfoUtils.getUser().getId());
        model.addAttribute("teamlistCount",userUserinfoService.findList(userinfo).size());
        return success ("成功!!", response, model);

    }

    /**
     * 根据关键字查询图片
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/getPhotoBykey", method = RequestMethod.POST)
    public String getPhotoBykey(HttpServletRequest request, HttpServletResponse response, Model model) {
        String keyword = request.getParameter("keyword");
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setkeyword(keyword);
        model.addAttribute("photoList",userPhotoService.findList(userPhoto));
        return success ("成功!!", response, model);

    }

    /**
     * 用户升级
     * 表名: userinfo
     * 条件:
     * 其他:
     */
   @RequestMapping(value = "/userLevelUp", method = RequestMethod.POST)
   public String userLevelUp(HttpServletRequest request, HttpServletResponse response, Model model) {
       UserUserinfo userinfo = userUserinfoService.get(UserInfoUtils.getUser().getId());
        String leveId = request.getParameter("levelId");
       String password = request.getParameter("password");
       if(!SystemService.validatePassword (password,userinfo.getBankPassword ())){
           return error ("支付密码输入错误", response, model);
       }
        try{
           userInfoExtYzfService.updateUserLevel(UserInfoUtils.getUser().getUserName(),leveId);
            return success ("升级成功!!", response, model);
        }catch (ValidationException e){
            model.addAttribute("errorCode",e.getErrorCode());
           return error ("失败: "+ e.getMessage(), response, model);
       }
   }

    /**
     * 用户团队管理列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/teamManageList", method = RequestMethod.POST)
    public String teamManageList(HttpServletRequest request, HttpServletResponse response, Model model) {
        String  name = request.getParameter("userName");
        if(StringUtils2.isBlank(name)){
            name = UserInfoUtils.getUser().getUserName();
        }
        String userStatus = request.getParameter("status");
        UserUserinfo userinfo = new UserUserinfo();
        if(StringUtils2.isNotBlank(userStatus)){
            if(EnumUtil.UserStatusEnum.status_enable.toString().equals(userStatus)){
                userinfo.setUserStatus(userStatus);
            }else if(EnumUtil.UserStatusEnum.status_disable.toString().equals(userStatus)){
                userinfo.setLevelIdBegin("1");
                userinfo.setUserStatus(userStatus);
            }else{
                userinfo.setUserLevelId(EnumUtil.YesNO.NO.toString());
            }
        }
        userinfo.setParentName(name);
        model.addAttribute("userName", UserInfoUtils.getUser().getUserName());
        model.addAttribute("tuijianList",userUserinfoService.findPage(new Page<UserUserinfo>(request, response) ,userinfo));
        return success ("成功!!", response, model);
    }
/**
     *
     * 查询用户
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/selectUserByMobile", method = RequestMethod.POST)
    public String selectUserByMobile(HttpServletRequest request, HttpServletResponse response, Model model) {
       String mobile = request.getParameter("mobile");
        UserUserinfo userinfo = userUserinfoService.getByMobile(mobile);
        if(userinfo != null){
            if(userinfo.getId().equals(UserInfoUtils.getUser().getId())){
                return error ("对方账户不可填写自己,请重新输入信息", response, model);
            }else{
                return success ("信息填写正确", response, model);
            }
        }else{
            return error ("没有找到该用户,请检查您输入的信息!!", response, model);
        }

    }
    /**
     * 云支付会员管理
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/userManager", method = RequestMethod.POST)
    public String userManager(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = UserInfoUtils.getUser().getUserName();
        UserUserinfo userUserinfo = new UserUserinfo();
        userUserinfo.setParentName(userName);

        String userLevelId = request.getParameter("userLevelId");
        String isShop = request.getParameter("isShop");
        if(StringUtils2.isNotBlank(userLevelId)){
            userUserinfo.setUserLevelId(userLevelId);
        }
        if(StringUtils2.isNotBlank(isShop)){
            userUserinfo.setIsShop(isShop);
        }

        model.addAttribute("parentName", UserInfoUtils.getUser().getParentName());
        model.addAttribute("fenXiangNum", userUserinfoService.findPage(new Page<UserUserinfo>(request, response) ,userUserinfo));
        return success ("成功!!", response, model);
    }


    /**
     * 查询当前用户奖金列表
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userReportList", method = RequestMethod.POST)
    public String userReportList(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserReport userReport = new UserReport();
        userReport.setUserName(UserInfoUtils.getUser().getUserName());
        model.addAttribute ("userReportList", userReportService.findPage(new Page<UserReport>(request, response), userReport));
        return success ("成功!!", response, model);
    }


    /**
     * 查询当前用户奖金详情
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userReportInfo", method = RequestMethod.POST)
    public String userReportInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        model.addAttribute ("userReportInfo", userReportService.get (id));
        return success ("成功!!", response, model);
    }

    /**
     * 查询当前用户个人信息
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public String userinfo(HttpServletRequest request, HttpServletResponse response, Model  model) {
        StringBuilder areaName = areaService.getAreaById(UserInfoUtils.getUser().getAreaId(), new StringBuilder());
        String type = EnumUtil.YesNO.NO.toString();
        if(UserInfoUtils.getUser ().getActiveStatus().equals(EnumUtil.YesNO.YES.toString())){
            type = areaService.get(UserInfoUtils.getUser ().getMainPerformance().toString()).getType();
        }
        model.addAttribute("type", type);
        model.addAttribute("area", areaName.toString());
        model.addAttribute ("user", userUserinfoService.get (UserInfoUtils.getUser ().getId ()));
        //model.addAttribute("area",areaService.getAreaNameById(UserInfoUtils.getUser().getAreaId(),""));
        return success ("成功!!", response, model);
    }

    /**
     * 密码修改
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        String oldPwd = request.getParameter ("oldPwd");
        String newPwd = request.getParameter ("newPwd");
        if (!SystemService.validatePassword (oldPwd, userinfo.getUserPass ())) {
            return error ("原始密码输入错误", response, model);
        }
        try {
            userUserinfoService.updateUserPwd (newPwd);
            return success ("修改密码成功!!", response, model);
        } catch (ValidationException e) {
            return error (e.getMessage(), response, model);
        }

    }

    /**
     * 支付密码修改
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/updatePayPassword", method = RequestMethod.POST)
    public String updatePayPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        String oldPwd = request.getParameter ("oldPwd");
        String newPwd = request.getParameter ("newPwd");
        if (!SystemService.validatePassword (oldPwd, userinfo.getBankPassword())) {
            return error ("原始支付密码输入错误", response, model);
        }
        try {
            userUserinfoService.updateUserPaypass (newPwd);
            return success ("修改支付密码成功!!", response, model);
        } catch (ValidationException e) {
            return error (e.getMessage(), response, model);
        }

    }

    /**
     * 二级密码修改
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/updateOtherPassword", method = RequestMethod.POST)
    public String updateOtherPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserUserinfo userinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        String oldPwd = request.getParameter ("oldPwd");
        String newPwd = request.getParameter ("newPwd");
        if (!SystemService.validatePassword (oldPwd, userinfo.getZhuanquPass())) {
            return error ("原始二级密码输入错误", response, model);
        }
        try {
            userUserinfoService.updateUserZhuanqupass (newPwd);
            return success ("修改二级密码成功!!", response, model);
        } catch (ValidationException e) {
            return error (e.getMessage(), response, model);
        }

    }

    /**
     * 实名认证
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/AuthRealName", method = RequestMethod.POST)
    public String authRealName(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        UserUserinfo userUserinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        String trueName = request.getParameter ("trueName");
        String idCard = request.getParameter ("idCard");
        if (!IdcardUtils.validateCard (idCard)) {
            return error ("身份证输入有误!!", response, model);
        }
        userUserinfo.setTrueName (trueName);
        userUserinfo.setIdCard (idCard);
        userUserinfo.setRenZheng ("1");
        userUserinfoService.save (userUserinfo);
        return success ("认证成功!!", response, model);
    }

    /**
     * 个人银行卡修改/添加
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/updateUserBank", method = RequestMethod.POST)
    public String updateUserBank(UserUserinfoBank userUserinfoBank, HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        UserUserinfo userUserinfo = userUserinfoService.get (UserInfoUtils.getUser ().getId ());
        if("1".equals (userUserinfo.getRenZheng ())){

            if(!userUserinfoBank.getBankUserName().equals(userUserinfo.getTrueName())){
                return error ("只可绑定本人的银行卡", response, model);
            }
            if(!VerifyUtils.isBankCard (userUserinfoBank.getBankUserNum ())){
                return error ("请输入正确的银行卡号", response, model);
            }
            userUserinfoBank.setUserName (userUserinfo.getUserName());
            userUserinfoBank.setBankUserName(userUserinfo.getTrueName());
            userUserinfoBank.setBankName (userBankWithdrawService.getByBankCode (userUserinfoBank.getBankCode ()).getBankName ());
            try{
                userUserinfoBankService.save (userUserinfoBank);
            }catch (ValidationException e){
                return error (e.getMessage (), response, model);
            }
        }
        else{
            return error ("请先实名认证后,方可绑定银行卡!", response, model);
        }
        return success ("个人银行卡添加成功!!", response, model);
    }

    /**
     * 个人银行卡列表
     * 表名:
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userBankList", method = RequestMethod.POST)
    public String userBankList( HttpServletRequest request, HttpServletResponse response, Model model) {

        UserUserinfoBank userinfoBank = new UserUserinfoBank();
        userinfoBank.setUserName (UserInfoUtils.getUser ().getUserName ());
        Page<UserUserinfoBank> page = userUserinfoBankService.findPage (new Page<UserUserinfoBank>(request, response), userinfoBank);
        model.addAttribute ("userBankList", page);
        return success ("成功!!", response, model);
    }

    /**
     * 充值银行
     * 表名:
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/userChargeBank", method = RequestMethod.POST)
    public String userChargeBank( HttpServletRequest request, HttpServletResponse response, Model model) {

        UserBankCharge userBankCharge = new UserBankCharge();
        userBankCharge.setStatus(EnumUtil.YesNO.YES.toString());
        model.addAttribute ("userChargeBank", userBankChargeServie.findList(userBankCharge));
        return success ("成功!!", response, model);
    }

    /**
     * 数据
     * 表名:
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/dataReport", method = RequestMethod.POST)
    public String dataReport( HttpServletRequest request, HttpServletResponse response, Model model) {
        Date date = DateUtils2.getDiffDatetime(new Date(),-1);
        model.addAttribute ("score", userAccountchangeService.sumAddMoneyByMoneyType(EnumUtil.MoneyType.score.toString(),date));
        model.addAttribute ("silu", userAccountchangeService.sumAddMoneyByMoneyType(EnumUtil.MoneyType.money2.toString(),date));
        return success ("成功!!", response, model);
    }

    /**
     * 个人银行卡删除
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/deleteUserBank", method = RequestMethod.POST)
    public String deleteUserBank( HttpServletRequest request, HttpServletResponse response, Model model)throws ValidationException {

        String userName = UserInfoUtils.getUser ().getUserName ();
        String id = request.getParameter ("id");
        UserUserinfoBank userinfoBank = userUserinfoBankService.get (id);
        if(!userName.equals (userinfoBank.getUserName ())){
            return error ("只能删除自己的银行卡!!", response, model);
        }
        userUserinfoBankService.delete (userinfoBank);
        return success ("删除成功!!", response, model);
    }


    /**
     * 个人地址修改/添加
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/updateUserAddress", method = RequestMethod.POST)
    public String updateUserAddress(UserAddress userAddress, HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        if (StringUtils2.isNotBlank (userAddress.getId ())) {
            String userName = userAddressService.get (userAddress.getId ()).getUserName ();
            if (!UserInfoUtils.getUser ().getUserName ().equals (userName)) {
                return error ("只可修改本人地址!!", response, model);
            }
        }
        userAddress.setUserName (UserInfoUtils.getUser ().getUserName ());
        userAddress.setPostcode("");//手动设置空邮编
        try {
            userAddressService.save (userAddress);
        }catch (ValidationException e){
            return error (e.getMessage (), response, model);
        }
        return success ("个人地址修改成功!!", response, model);
    }


    /**
     * 个人地址删除
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/deleteUserAddress", method = RequestMethod.POST)
    public String deleteUserAddress(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {
        String id = request.getParameter ("id");
        if (StringUtils2.isBlank(id)) {
            return error ("失败!!", response, model);
        }
        UserAddress userAddress = userAddressService.get (id);

        if(!userAddress.getUserName().equals(UserInfoUtils.getUser().getUserName())){
            return error ("只可删除本人地址!!", response, model);
        }

        userAddressService.delete (userAddress);
        return success ("成功!!", response, model);

    }

    /**
     * 个人账变查询
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/userAccountChange", method = RequestMethod.POST)
    public String userAccountchange(HttpServletRequest request, HttpServletResponse response, Model model) {
        String moneyType = request.getParameter("moneyType");
        String changeType = request.getParameter ("changeType");
        String date = request.getParameter ("date");
        UserAccountchange userAccountchange = new UserAccountchange();
        if(StringUtils2.isNotBlank (changeType)) {
            if(changeType.equals (EnumUtil.YesNO.YES.toString ())){
                userAccountchange.setChangeMoneyEnd (new BigDecimal (0));
            }else{
                userAccountchange.setChangeMoneyBegin (new BigDecimal (0));
            }
        }
        if(StringUtils2.isNotBlank (date)) {
            Date afterDate = DateUtils2.getDiffDatetime (new Date (),Integer.parseInt (date)*(-1));
            userAccountchange.setCreateDateBegin (afterDate);
        }
        if(StringUtils2.isNotBlank(moneyType)){
            userAccountchange.setMoneyType(moneyType);
        }
        userAccountchange.setUserName (UserInfoUtils.getUser().getUserName ());
        Page<UserAccountchange> page = userAccountchangeService.findPage (new Page<UserAccountchange>(request, response), userAccountchange);
        model.addAttribute ("userAccountchange", page);
        return success ("成功!!", response, model);

    }


    /**
     * 充值记录查询
     * 表名: userinfo
     * 条件:
     * 其他:
     */

    @RequestMapping(value = "/userChargeLog", method = RequestMethod.POST)
    public String userChargeLog(HttpServletRequest request, HttpServletResponse response, Model model) {

        UserChargeLog userChargeLog = new UserChargeLog();
        userChargeLog.setUserName (UserInfoUtils.getUser ().getUserName ());
        Page<UserChargeLog> page = userChargeLogService.findPage (new Page<UserChargeLog>(request, response), userChargeLog);
        model.addAttribute ("userChargeLogList", page);
        return success ("成功!!", response, model);

    }

    /**
     * 个人地址查询
     * 表名:
     * 条件: 用户名
     * 其他:
     */
    @RequestMapping(value = "/userAddressList", method = RequestMethod.POST)
    public String userAddressList(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserName (UserInfoUtils.getUser ().getUserName ());
        Page<UserAddress> page = userAddressService.findPage (new Page<UserAddress>(request, response), userAddress);
        model.addAttribute ("userAddressList", page);
        return success ("成功!!", response, model);
    }

    /**
     * 地址详细信息查询
     * 表名:
     * 条件:id
     * 其他:
     */
    @RequestMapping(value = "/addressaInfo", method = RequestMethod.POST)
    public String addressaInfo(HttpServletRequest request, HttpServletResponse response, Model model) {

        UserAddress address =  userAddressService.get (request.getParameter ("id"));
        if(!address.getUserName().equals(UserInfoUtils.getUser().getUserName())){
            return error ("只可查询个人地址!!", response, model);
        }
        model.addAttribute ("addressaInfo",address );
        return success ("成功!!", response, model);
    }


    /**
     * 银行列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/bankList", method = RequestMethod.POST)
    public String withdrawBankList(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserBankWithdraw userBankWithdraw = new UserBankWithdraw();
        userBankWithdraw.setStatus ("1");
        model.addAttribute ("bankList",userBankWithdrawService .findList (userBankWithdraw));
        return success ("成功!!", response, model);
    }


    @RequestMapping(value = "/withdrawBankByUserBankid", method = RequestMethod.POST)
    public String withdrawBankByUserBankid(HttpServletRequest request, HttpServletResponse response, Model model) {
        String bankId = request.getParameter("bankId");
        UserUserinfo userinfo = userUserinfoService.get(UserInfoUtils.getUser().getId());
        UserBankWithdraw userBankWithdraw =  userBankWithdrawService.getByBankCode(userUserinfoBankService.get (bankId).getBankCode ());
        model.addAttribute ("userInfo", userinfo);
        model.addAttribute ("poundage", Global.getOption("system_user_set","poundage"));
        model.addAttribute ("min", Global.getOption("system_user_set","minMoney"));
        model.addAttribute ("max", Global.getOption("system_user_set","maxMoney"));
        model.addAttribute("multiple", Global.getOption("system_user_set","multiple"));
        model.addAttribute ("bankInfo",userBankWithdraw);
        return success ("成功!!", response, model);
    }

    @RequestMapping(value = "/withdrawApply", method = RequestMethod.POST)
    public String withdrawApply(HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {

        String money = request.getParameter ("money");
        String password = request.getParameter ("password");
        String bankCode = request.getParameter ("bankCode");
        String userBankId = request.getParameter ("userBankId");
        BigDecimal changeMoney = new BigDecimal(money);
        BigDecimal multiple = new BigDecimal(Global.getOption("system_user_set","multiple"));

        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setLimitDate(new Date());
        userWithdraw.setUserName(UserInfoUtils.getUser().getUserName());
        if(userWithdrawService.findList(userWithdraw).size()>0){
            return error ("每人每天仅限提现一次", response, model);
        }
        if (!VerifyUtils.isInteger(changeMoney.divide(multiple))) {
            return error ("提现金额必须为"+multiple+"的倍数!", response, model);
        }
        UserBankWithdraw userBankWithdraw =  userBankWithdrawService.getByBankCode(bankCode);
        UserUserinfo userinfo = userUserinfoService.get(UserInfoUtils.getUser ().getId ());
        if(!SystemService.validatePassword (password,userinfo.getBankPassword ())){
            return error ("密码输入错误", response, model);
        }
        try {
            userUserinfoService.withdrawApply(UserInfoUtils.getUser().getUserName(),userBankWithdraw,money,userBankId);
            return success ("成功!!", response, model);
        }catch (ValidationException e)
        {
            return success (e.getMessage (), response, model);
        }
    }

    /**
     * 获取昨日收益和累计释放
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/incomeYesterday", method = RequestMethod.POST)
    public String getIncomeYesterday(HttpServletRequest request, HttpServletResponse response, Model model) {

        UserAccountchange userAccountchangeUnfreeze = new UserAccountchange();
        userAccountchangeUnfreeze.setUserName(UserInfoUtils.getUser().getUserName());
        userAccountchangeUnfreeze.setChangeType(EnumUtil.MoneyChangeType.SILU_FREE.toString());
        userAccountchangeUnfreeze.setMoneyType(EnumUtil.MoneyType.money.toString());
        userAccountchangeUnfreeze.setOrderBy("a.create_date desc");

//        UserAccountchange userAccountchangePoundage = new UserAccountchange();
//        userAccountchangePoundage.setUserName(UserInfoUtils.getUser().getUserName());
//        userAccountchangePoundage.setChangeType(EnumUtil.MoneyChangeType.UNFREEZE_POUNDAGE.toString());
//        userAccountchangePoundage.setMoneyType(EnumUtil.MoneyType.money.toString());
//        userAccountchangePoundage.setOrderBy("a.create_date desc");

        List<UserAccountchange> userAccountchangeUnfreezeList = userAccountchangeService.findList(userAccountchangeUnfreeze);
        //List<UserAccountchange> userAccountchangePoundageList = userAccountchangeService.findList(userAccountchangePoundage);
       // userAccountchangeUnfreezeList.addAll(userAccountchangePoundageList);
        if (Collections3.isEmpty(userAccountchangeUnfreezeList)) {
            model.addAttribute("userAccountchange", "0.00");
            model.addAttribute("totalRelease", "0");
        } else {
            Map<String, List<UserAccountchange>> accountChangeCollect = userAccountchangeUnfreezeList.stream().collect(groupingBy(accountChange -> {
                LocalDate localDate = LocalDateTime.ofInstant(accountChange.getCreateDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                return localDate.format(formatter);
            }));

            //按日期排序
            TreeMap<String, List<UserAccountchange>> sortedCollect = new TreeMap<>(accountChangeCollect);
            DecimalFormat format = new DecimalFormat("#####0.00");

            //昨日收益
            LocalDate now = LocalDate.now();
            LocalDateTime latestDate = LocalDateTime.ofInstant(userAccountchangeUnfreezeList.get(0).getCreateDate().toInstant(), ZoneId.systemDefault());
            //当前时间与获取的最近一次记录收益的日期相差天数
            long perid = now.toEpochDay() - latestDate.toLocalDate().toEpochDay();
            if (perid > 1) {
                model.addAttribute("userAccountchange", "0.00");
            } else {
                List<UserAccountchange> value = sortedCollect.lastEntry().getValue();
                double staticMoney = value.stream().mapToDouble(ele ->
                    OptionalUtils.strToDouble(ele.getChangeMoney()).orElse(Double.valueOf(0))
                ).sum();

                model.addAttribute("userAccountchange", format.format(staticMoney));
            }

            //收益走势图
            List<String> xAxis = new ArrayList<>();
            List<String> yAxis = new ArrayList<>();
            sortedCollect.forEach((date, accountchangeList) -> {
                double money = accountchangeList.stream().mapToDouble(ele ->
                        OptionalUtils.strToDouble(ele.getChangeMoney()).orElse(Double.valueOf(0))
                ).sum();

                yAxis.add(format.format(money));
                xAxis.add(date);
            });

            model.addAttribute("xAxis", xAxis.toArray(new String[xAxis.size()]));
            model.addAttribute("yAxis", yAxis.toArray(new String[yAxis.size()]));

            //累计释放
            try {
                double totalRelease = sortedCollect.values().stream().flatMap(List::stream).mapToDouble(ele ->
                        OptionalUtils.strToDouble(ele.getChangeMoney()).orElse(Double.valueOf(0))).sum();

                model.addAttribute("totalRelease", format.format(totalRelease));
            } catch (Exception ex) {
                logger.error(ex.getLocalizedMessage());
                model.addAttribute("totalRelease", "0");
            }
        }

        return success ("成功!!", response, model);
    }

    /**
     * 通过userName获得个人信息
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUserByName", method = RequestMethod.POST)
    public String getUserByName(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userName = request.getParameter("userName");
        UserUserinfo user = userUserinfoService.getByName(userName);
        model.addAttribute ("user", user);
        return success ("成功!!", response, model);

    }

    /**
     * 忘记密码
     * 表名: userinfo
     * 条件:
     * 其他:
     */
    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public String forgetPassword(HttpServletRequest request, HttpServletResponse response, Model model) {
        String newPwd = request.getParameter ("newPwd");
        String newPwds = request.getParameter ("newPwds");
        String mobile = request.getParameter ("mobile");
        String verifyCode = request.getParameter ("code");
        UserUserinfo userinfo = userUserinfoService.getByMobile(mobile);
        if (StringUtils2.isBlank(mobile)) {
            return error ("手机号不能为空!", response, model);
        }
        if(null == userinfo){
            return error ("该用户不存在!", response, model);
        }
        if (StringUtils2.isBlank(verifyCode)) {
            return error ("验证码不能为空!", response, model);
        }else{
            if (!userMsmService.checkVerifyCode(mobile, verifyCode)) {
                return error ("验证码错误!", response, model);
            }
        }
        if (StringUtils2.isBlank(newPwd) || StringUtils2.isBlank(newPwds)) {
            return error ("请输入新密码!", response, model);
        }else{
            if (!newPwds.equals(newPwd)) {
                return error ("两次输入密码不一致!请重新输入!", response, model);
            }
        }

        try {
            userUserinfoService.forgetUserPwd(userinfo.getUserName(),newPwds);
            return success ("恭喜你已成功设置新密码!", response, model);
        } catch (ValidationException e) {
            return error (e.getMessage(), response, model);
        }

    }

    /**
     * 后台查询升级配置
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUpdateSetting", method = RequestMethod.POST)
    public String getUpdateSetting(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String,String> map = new HashMap();

        map.put("sellerBonus",Global.getOption("system_user_bonus","user_expand"));

        map.put("everyBonusOne",Global.getOption("system_static_bonus","normal_user"));
        map.put("everyBonusTwo",Global.getOption("system_static_bonus","formal_user"));
        map.put("everyBonusThree",Global.getOption("system_static_bonus","vip_user"));
        for(int i = 1 ; i<4 ;i++){
            UserLevel userLevel = new UserLevel();
            userLevel.setLevelCode(Integer.toString(i));
            map.put("staticBonus"+i,userLevelService.findList(userLevel).get(0).getStaticMoney().toString());
            map.put("amountPer"+i,userLevelService.findList(userLevel).get(0).getAmountPer().toString());
        }

        model.addAttribute ("map", map);
        return success ("成功!!", response, model);

    }
}
