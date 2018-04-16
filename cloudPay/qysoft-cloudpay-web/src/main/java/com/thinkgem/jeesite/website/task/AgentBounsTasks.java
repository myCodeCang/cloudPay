package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.modules.sys.entity.Area;
import com.thinkgem.jeesite.modules.sys.service.AreaService;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.entity.UserLevel;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserAccountchangeService;
import com.thinkgem.jeesite.modules.user.service.UserLevelService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import com.thinkgem.jeesite.modules.userExt_Yzf.service.UserInfoExtYzfService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 月结算法
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class AgentBounsTasks implements UserTaskBusiness {

    @Resource
    private UserUserinfoService userUserinfoService;

    @Resource
    private AreaService areaService;

    @Resource
    private UserInfoExtYzfService userInfoExtYzfService;

    @Resource
    private UserAccountchangeService userAccountchangeService;

    @Resource
    private UserLevelService userLevelService;

    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");

        //代理返利

        UserUserinfo searchUser = new UserUserinfo();
        searchUser.setActiveStatus(EnumUtil.YesNO.YES.toString());
        List<UserUserinfo> userUserinfoList = userUserinfoService.findList(searchUser);
        //代理配置
        BigDecimal contryBouns = new BigDecimal(Global.getOption("system_lead_bonus","contry_earnings","0.0007"));
        BigDecimal provinceBouns = new BigDecimal(Global.getOption("system_lead_bonus","province_earnings","0.003"));
        BigDecimal cityBouns =  new BigDecimal(Global.getOption("system_lead_bonus","city_earnings","0.005"));
        BigDecimal countyBouns =   new BigDecimal(Global.getOption("system_lead_bonus","county_earnings","0.007"));


        UserAccountchange userAccountchange = new UserAccountchange();
        userAccountchange.setMoneyType(EnumUtil.MoneyType.money.toString());
        userAccountchange.setChangeType(EnumUtil.MoneyChangeType.CONSUMPTION.toString());
        userAccountchange.setCreateDateMonth(DateUtils2.addDays(new Date(),-1));
        for(UserUserinfo currUser : userUserinfoList) {
            //获取用户代理地区
            Area currArea = areaService.get(currUser.getMainPerformance().toString());
            BigDecimal areaBouns = new BigDecimal(0);
            int isCountry = 0;
            if(EnumUtil.AreaType.country.toString().equals(currArea.getType())){
                isCountry = 1;
                areaBouns = contryBouns;
            }else if(EnumUtil.AreaType.province.toString().equals(currArea.getType())){
                areaBouns = provinceBouns;
            }else if(EnumUtil.AreaType.city.toString().equals(currArea.getType())){
                areaBouns = cityBouns;
            }else if(EnumUtil.AreaType.county.toString().equals(currArea.getType())){
                areaBouns = countyBouns;
            }
            BigDecimal userBonus = new BigDecimal(0);
            List<UserAccountchange> accountchangeList = userAccountchangeService.findList(userAccountchange);
            for (UserAccountchange currChange : accountchangeList) {
                if(isCountry == 1){
                    userBonus = userBonus.add(new BigDecimal(currChange.getChangeMoney()));
                }else {
                    if (areaService.pIdContainzId(currArea.getId(),userUserinfoService.getByName(currChange.getUserName()).getAreaId())) {
                        userBonus = userBonus.add(new BigDecimal(currChange.getChangeMoney()));
                    }
                }
            }
            //给该用户发送奖励
            try {
                BigDecimal realityBonus = userBonus.multiply(areaBouns).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1));
                //代理收益
                userInfoExtYzfService.updateUserSilu(currUser.getUserName(), realityBonus, "代理收益", EnumUtil.MoneyChangeType.AGENT_EARNINGS, true);

            } catch (Exception e) {
                logger.error("代理收益执行失败: 失败用户:" + currUser.getUserName());
            }
        }

        logger.error("end..................");

        return true;
    }




}
