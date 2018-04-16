package com.thinkgem.jeesite.modules.userExt_Yzf.service;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.modules.payment.entity.PaymentCallback;
import com.thinkgem.jeesite.modules.user.entity.UserChargeLog;
import com.thinkgem.jeesite.modules.user.service.UserChargeLogService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by yankai on 2017/6/10.
 */
@Service("alipayCallback")
@Transactional(readOnly = false,propagation= Propagation.REQUIRED,rollbackForClassName={"RuntimeException","Exception","ValidationException"})
public class AlipayCallbackService implements PaymentCallback {
    @Resource
    private UserUserinfoService userUserinfoService;

    @Resource
    private UserChargeLogService userChargeLogService;

    public AlipayCallbackService() {}

    @Override
    public boolean callback(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return false;
        }

        String userName = params.get("body");   //支付时，userName放在请求参数的body中
        String amount = params.get("total_fee");
        String tradeNo = params.get("trade_no");
        //String outTradeNo = (String) param.get("out_trade_no");

        UserChargeLog chargeLog = userChargeLogService.getByRecordCode(tradeNo);
        if (null != chargeLog) {
            return true;
        }

        userUserinfoService.updateUserMoney(userName, new BigDecimal(amount), tradeNo, EnumUtil.MoneyChangeType.ALIPAY_RECHARGE);
        return true;
    }
}

