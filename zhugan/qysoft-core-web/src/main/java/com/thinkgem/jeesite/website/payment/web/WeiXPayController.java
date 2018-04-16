package com.thinkgem.jeesite.website.payment.web;
import com.github.binarywang.wxpay.bean.WxPayOrderNotifyResponse;
import com.github.binarywang.wxpay.bean.result.WxPayBaseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.payment.entity.PaymentCallback;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/7/25.
 */
@Controller
@RequestMapping("/payment/weiXPay")
public class WeiXPayController extends BaseController {
    private static final String RETURN_SUCCESS = "SUCCESS";
    private static final String RETURN_FAILED = "FAIL";
    @Qualifier("wxPayService")
    private WxPayService wxPayService;

    @Autowired(required = false)
    @Qualifier("wxpayCallback")
    private PaymentCallback paymentCallback;

    @ResponseBody
    @RequestMapping(value = "payNotify")
    public String payNotify(HttpServletRequest request, HttpServletResponse response) {
        String resultCode = request.getParameter("result_code");
        if (!RETURN_SUCCESS.equals(resultCode)){
            return RETURN_FAILED;
        }
        //使用微信TOOLS校验
        /*if(!verify(request)){
            return RETURN_FAILED;
        }*/
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult result = wxPayService.getOrderNotifyResult(xmlResult);
            // 结果正确
            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<String, String> resultMap = new HashMap<>();
            parameterMap.forEach((key, value) -> resultMap.putIfAbsent(key, value[0]));

            //调用业务处理接口
            if (paymentCallback.callback(resultMap)) {
                return WxPayOrderNotifyResponse.success("处理成功!");
            }
            return WxPayOrderNotifyResponse.fail("处理失败!");
        } catch (Exception e) {
            logger.error("微信回调结果异常,异常原因{}", e.getMessage());
            return WxPayOrderNotifyResponse.fail(e.getMessage());
        }
    }

}
