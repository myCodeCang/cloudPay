package com.thinkgem.jeesite.modules.userExt_Yzf.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.modules.shop.service.ShopUCApiService;
import com.thinkgem.jeesite.modules.user.dao.UserAccountchangeDao;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.entity.UserLevel;
import com.thinkgem.jeesite.modules.user.entity.UserLevelLog;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserLevelLogService;
import com.thinkgem.jeesite.modules.user.service.UserLevelService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * ,用户业务层
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackForClassName = {"RuntimeException", "Exception", "ValidationException"})
public class UserInfoExtYzfService {
    @Resource
    private UserUserinfoService userUserinfoService;

    @Resource
    private UserLevelService userLevelService;
    @Resource
    private UserLevelLogService userLevelLogService;

    @Resource
    private ShopUCApiService shopUCApiService;

    @Resource
    private UserUserinfoDao userUserinfoDao;

    @Resource
    private UserAccountchangeDao userAccountchangeDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户升级业务处理
     *
     * @return
     */
    public void updateUserLevel(String userName, String levelId) {

        //用户升级,记录升级状态,标识类型为未激活
        UserUserinfo user = userUserinfoService.getByNameLock(userName);
        String oldlevel = user.getUserLevelId();
        UserUserinfo parentUser = userUserinfoService.getByNameLock(user.getParentName());
        if (null == user) {
            throw new ValidationException("升级用户不存在");
        }
        if (Integer.parseInt(oldlevel) >= Integer.parseInt(levelId)) {
            throw new ValidationException("升级等级错误");
        }
        UserLevel userLevel = userLevelService.findByLevalCode(levelId);
        if (null == userLevel) {
            throw new ValidationException("升级等级不存在");
        }
//      UserLevel currUserLevel = userLevelService.findByLevalCode(user.getUserLevelId());
//        if (Integer.parseInt(user.getUserLevelId()) > 0) {
//            throw new ValidationException("当前用户已经升级过,请勿重新升级!");
//        }
        if (user.getMoney().compareTo(userLevel.getMoney()) < 0) {
            throw new ValidationException("用户余额不足无法升级,请先去充值", 1001);
        }
        //扣除用户金钱
        userUserinfoService.updateUserMoney(userName, userLevel.getMoney().multiply(new BigDecimal(-1)), "用户升级", EnumUtil.MoneyChangeType.userLevel);
        //增加丝路
        this.updateUserSilu(userName,userLevel.getStaticMoneyHigh(),"用户升级增加丝路",EnumUtil.MoneyChangeType.userLevel,false);
        //升级
        userUserinfoDao.updateUserLevelId(user.getId(), levelId);

        //给上级返利
        if(parentUser != null){
            UserLevel parentLevel = userLevelService.findByLevalCode(parentUser.getUserLevelId());
            userUserinfoService.updateUserMoney(parentUser.getUserName(),parentLevel.getAmountPer().multiply(userLevel.getMoney()).setScale(2,BigDecimal.ROUND_HALF_UP),"推广佣金",EnumUtil.MoneyChangeType.spread);
        }

        //插入张变
        UserLevelLog userLevelLog = new UserLevelLog();
        userLevelLog.setUserName(userName);
        userLevelLog.setUpdateType("1");
        userLevelLog.setCommt("用户升级");
        userLevelLog.setOldLevel(oldlevel);
        userLevelLog.setNewLevel(userLevel.getLevelCode());
        userLevelLog.setPerformance(userLevel.getPerformance());
        userLevelLog.setIscheck("0");
        userLevelLogService.save(userLevelLog);

    }

    public void updateUserSilu(String userName, BigDecimal money, String message, EnumUtil.MoneyChangeType changeType,boolean levelUp) throws ValidationException {
        UserUserinfo userUserinfo = userUserinfoService.getByNameLock(userName);
        if (userUserinfo == null) {
            return;
        }
        if(money.compareTo(BigDecimal.ZERO) ==0){
            return;
        }
        //插入用户帐变
        UserAccountchange userAccountchange = new UserAccountchange();
        userAccountchange.setUserName(userUserinfo.getUserName());
        userAccountchange.setChangeMoney(money.toString());
        userAccountchange.setBeforeMoney(userUserinfo.getMoney2());
        userAccountchange.setAfterMoney(userUserinfo.getMoney2().add(money).toString());
        userAccountchange.setCommt(message);
        userAccountchange.setChangeType(changeType.toString());
        userAccountchange.setMoneyType(EnumUtil.MoneyType.money2.toString());
        userAccountchange.preInsert();
        userAccountchangeDao.insert(userAccountchange);

        //修改用户丝路
        userUserinfoDao.updateUserOtherMoney(userName, money,EnumUtil.MoneyType.money2.toString());

        //判断是否足够升级
        if(levelUp){
            this.autoUpUserLevel(userName);
        }
  }

        private void autoUpUserLevel(String userName) {
            UserUserinfo currUser = userUserinfoService.getByNameLock(userName);
            //获取自动升级配置
            BigDecimal comUpFormal = new BigDecimal(Global.getOption("system_user_set","common_up_formal","9999"));
            BigDecimal comUpVip = new BigDecimal( Global.getOption("system_user_set","common_up_vip","90000"));
            BigDecimal formalUpVip = new BigDecimal( Global.getOption("system_user_set","formal_up_vip","99999"));
            //TODO 捕获异常
            try {
                if(Integer.parseInt(currUser.getUserLevelId()) == 3){
                    return;
                }else if(Integer.parseInt(currUser.getUserLevelId()) == 2){
                    if(currUser.getMoney2().compareTo(formalUpVip) >= 0){
                        this.updateUserSilu(currUser.getUserName(),formalUpVip.multiply(new BigDecimal(-1)),"用户自动升级",EnumUtil.MoneyChangeType.AUTO_UP_LEVEL,false);
                        userUserinfoDao.updateUserLevelId(currUser.getId(),"3");
                    }
                    return;
                }else{
                    if(currUser.getMoney2().compareTo(comUpVip) >= 0){
                        this.updateUserSilu(currUser.getUserName(),comUpVip.multiply(new BigDecimal(-1)),"用户自动升级",EnumUtil.MoneyChangeType.AUTO_UP_LEVEL,false);
                        userUserinfoDao.updateUserLevelId(currUser.getId(),"3");
                        return;
                    }else if(currUser.getMoney2().compareTo(comUpFormal) >= 0){
                        this.updateUserSilu(currUser.getUserName(),comUpFormal.multiply(new BigDecimal(-1)),"用户自动升级",EnumUtil.MoneyChangeType.AUTO_UP_LEVEL,false);
                        userUserinfoDao.updateUserLevelId(currUser.getId(),"2");
                    }
                    return;
                }
            } catch (NumberFormatException e) {
                logger.error(e.getLocalizedMessage());
            }
        }

    public void userTransferAccounts(String userName, String currMobile, BigDecimal money ,String message) throws ValidationException{
        UserUserinfo user = userUserinfoService.getByNameLock(userName);
        UserUserinfo mobileUser = userUserinfoService.getByMobile(currMobile);
        String type = "转账:" + message;
        if(mobileUser == null){
            throw new ValidationException("被转用户不存在,请您核对后在转账");
        }
        if(money.compareTo(BigDecimal.ZERO)<1){
            throw new ValidationException("转账金额输入错误");
        }
        UserUserinfo currUser = userUserinfoService.getByNameLock(mobileUser.getUserName());
        if(currUser.getUserName().equals(userName)){
            throw new ValidationException("不能给自己转账");
        }
        BigDecimal transFee =  new BigDecimal(Global.getOption("system_user_bonus","transfer_fee","0.1"));
        if(user.getMoney().compareTo(money) < 0){
            throw new ValidationException("余额不足,无法转账");
        }
        EnumUtil.MoneyChangeType moneyChangeType = EnumUtil.MoneyChangeType.TRANSFER_ACCOUNTS;
        EnumUtil.MoneyChangeType currChangeType = EnumUtil.MoneyChangeType.GATHERING;
        if(currUser.getIsShop().equals(EnumUtil.YesNO.YES.toString())){
            moneyChangeType = EnumUtil.MoneyChangeType.CONSUMPTION;
            currChangeType = EnumUtil.MoneyChangeType.BUSINESS;
            type = "消费: " + message;
        }
        //转账方
        userUserinfoService.updateUserMoney(userName,money.multiply(new BigDecimal(-1)),"用户"+type, moneyChangeType);
        this.updateUserSilu(userName,money,"转账返还",moneyChangeType,true);

        //被转方
        userUserinfoService.updateUserMoney(currUser.getUserName(),money,"收到"+user.getUserName() + "的转账", currChangeType);
        userUserinfoService.updateUserMoney(currUser.getUserName(),money.multiply(transFee).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(-1)),"转账手续费",EnumUtil.MoneyChangeType.TRANSFER_POUNDAGE);
        this.updateUserSilu(currUser.getUserName(),money.multiply(transFee).setScale(2,BigDecimal.ROUND_HALF_UP),"手续费返还",moneyChangeType,true);
    }
}
