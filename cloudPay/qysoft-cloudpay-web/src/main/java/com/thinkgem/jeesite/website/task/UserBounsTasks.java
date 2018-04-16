package com.thinkgem.jeesite.website.task;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.DateUtils2;
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
 * 日结算法
 */
@Service
@Lazy(false)
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class UserBounsTasks implements UserTaskBusiness {

    @Resource
    private UserUserinfoService userUserinfoService;

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

        //用户消费返利

        UserUserinfo searchUser = new UserUserinfo();
        List<UserUserinfo> userUserinfoList = userUserinfoService.findList(searchUser);
        //获取用户等级数据
        List<UserLevel> userLevelList = userLevelService.findList(new UserLevel());

        UserAccountchange userAccountchange = new UserAccountchange();
        userAccountchange.setMoneyType(EnumUtil.MoneyType.money.toString());
        userAccountchange.setCreateDate(DateUtils2.addDays(new Date(), -1));
        for (UserUserinfo currUser : userUserinfoList) {
            BigDecimal userBonus = BigDecimal.valueOf(0);
            BigDecimal shopBonus = BigDecimal.valueOf(0);

            List<UserAccountchange> accountchangeList = userAccountchangeService.findList(userAccountchange);
            for (UserAccountchange currChange : accountchangeList) {
                if (currUser.getUserName().equals(userUserinfoService.getByName(currChange.getUserName()).getParentName())) {
                    if (currChange.getChangeType().equals(EnumUtil.MoneyChangeType.CONSUMPTION.toString())) {
                        userBonus = userBonus.add(new BigDecimal(currChange.getChangeMoney()));
                    } else if (currChange.getChangeType().equals(EnumUtil.MoneyChangeType.BUSINESS.toString())) {
                        shopBonus = shopBonus.add(new BigDecimal(currChange.getChangeMoney()));
                    }
                }
            }
            //给该用户发送奖励
            try {
                Optional<UserLevel> subUserLevelOptional = userLevelList.stream().filter(s -> s.getLevelCode().equals(currUser.getUserLevelId())).findFirst();
                BigDecimal realityBonus = BigDecimal.valueOf(0);
                if (subUserLevelOptional.isPresent()) {
                    UserLevel subUserLevel = subUserLevelOptional.get();
                    realityBonus = userBonus.multiply(subUserLevel.getStaticMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                //营业额提成配置
                BigDecimal userExpand = new BigDecimal(Global.getOption("system_user_bonus", "user_expand", "0.03"));
                BigDecimal realityshopBonus = shopBonus.multiply(userExpand).setScale(2, BigDecimal.ROUND_HALF_UP);
                //直推消费奖
                userInfoExtYzfService.updateUserSilu(currUser.getUserName(), realityBonus.multiply(new BigDecimal(-1)), "直推用户消费额提成", EnumUtil.MoneyChangeType.PROMOTE_CONSUMPTION, true);
                //直推商家奖
                userInfoExtYzfService.updateUserSilu(currUser.getUserName(), realityshopBonus, "直推商家营业额提成", EnumUtil.MoneyChangeType.PROMOTE_BUSINESS, true);

            } catch (Exception e) {
                logger.error("用户消费返利执行失败: 失败用户:" + currUser.getUserName());
            }
        }

        /**
         * 丝路释放
         */
        searchUser = new UserUserinfo();
        userUserinfoList = userUserinfoService.findList(searchUser);
        BigDecimal levelMoney = new BigDecimal(0);
        //释放配置
        BigDecimal golad = new BigDecimal(Global.getOption("system_static_bonus", "red_packet_persent_gold", "0.8"));
        BigDecimal score = new BigDecimal(Global.getOption("system_static_bonus", "red_packet_persent_score", "0.2"));
        BigDecimal normalMoney = new BigDecimal(Global.getOption("system_static_bonus", "normal_user", "0.0003"));
        BigDecimal formalMoney = new BigDecimal(Global.getOption("system_static_bonus", "formal_user", "0.0005"));
        BigDecimal vipMoney = new BigDecimal(Global.getOption("system_static_bonus", "vip_user", "0.0008"));

        for (UserUserinfo freeUser : userUserinfoList) {
            if (freeUser.getMoney2().compareTo(new BigDecimal(100)) < 0) {
                continue;
            }
            if (freeUser.getUserLevelId().equals("1")) {
                levelMoney = normalMoney;
            } else if (freeUser.getUserLevelId().equals("2")) {
                levelMoney = formalMoney;
            } else {
                levelMoney = vipMoney;
            }
            BigDecimal freeMoney = freeUser.getMoney2().multiply(levelMoney).multiply(golad).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal freeScore = freeUser.getMoney2().multiply(levelMoney).multiply(score).setScale(2, BigDecimal.ROUND_HALF_UP);
            try {
                userInfoExtYzfService.updateUserSilu(freeUser.getUserName(), freeMoney.add(freeScore).multiply(new BigDecimal(-1)), "每日释放丝路", EnumUtil.MoneyChangeType.SILU_FREE, false);
                userUserinfoService.updateUserMoney(freeUser.getUserName(), freeMoney, "每日释放丝路", EnumUtil.MoneyChangeType.SILU_FREE);
                userUserinfoService.updateUserScore(freeUser.getUserName(), freeScore, "每日释放丝路", EnumUtil.MoneyChangeType.SILU_FREE);
            } catch (Exception e) {
                logger.error("用户丝路释放执行失败: 失败用户:" + freeUser.getUserName());
            }
        }


        logger.error("end..................");

        return true;
    }


}
