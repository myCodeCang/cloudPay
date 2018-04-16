package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.tasks.entity.UserTaskBusiness;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.service.ShopCommOrderService;
import com.thinkgem.jeesite.modules.user.service.UserAccountchangeService;
import com.thinkgem.jeesite.modules.user.service.UserReportService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Service
@Lazy(false)
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class UserReportsTasks implements UserTaskBusiness {


    @Resource
    private UserReportService userReportService;

    @Resource
    private UserAccountchangeService userAccountchangeService;

    @Resource
    private ShopCommOrderService shopCommOrderService;

    protected Logger logger = null;
    @Override
    public boolean doBusiness() {
        logger = getLogger();
        logger.error("start..................");
//每个用户每月记录一条统计记录
        //统计 1.
        //每次进来查询id asc

        // 充值提现统计
        UserAccountchange accountchange = new UserAccountchange();
        accountchange.setIscheck (EnumUtil.YesNO.NO.toString ());
        accountchange.setMoneyType (EnumUtil.MoneyType.money.toString ());
        List<UserAccountchange> accountchangeList = userAccountchangeService.findList (accountchange);
        for (UserAccountchange userchange : accountchangeList) {
            //丝路释放
            if (userchange.getChangeType ().equals (EnumUtil.MoneyChangeType.SILU_FREE.toString ())) {
                userReportService.updateUserReportByType (userchange.getUserName (), "1", new BigDecimal (userchange.getChangeMoney ()));
            }
            //直推50%
            if (userchange.getChangeType ().equals (EnumUtil.MoneyChangeType.spread.toString ())||userchange.getChangeType ().equals (EnumUtil.MoneyChangeType.poundage.toString ())) {
                userReportService.updateUserReportByType (userchange.getUserName (), "2", new BigDecimal (userchange.getChangeMoney ()));
            }
            //代理收益
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.AGENT_EARNINGS.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"3" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //充值
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.charget.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"4" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //提现
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.widthdraw.toString())||userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.widthdrawReject.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"5" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //升级
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.userLevel.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"6" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //转账
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.TRANSFER_ACCOUNTS.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"7" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //消费
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.CONSUMPTION.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"8" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            //手续费
            if(userchange.getChangeType ().equals(EnumUtil.MoneyChangeType.TRANSFER_POUNDAGE.toString())){
                userReportService.updateUserReportByType(userchange.getUserName (),"10" ,new BigDecimal (userchange.getChangeMoney ()));
            }
            userchange.setIscheck (EnumUtil.YesNO.YES.toString ());
            userAccountchangeService.save (userchange);
        }
        logger.error("end..................");

        return true;
    }




}
