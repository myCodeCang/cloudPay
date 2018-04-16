/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.webapp;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.IdcardUtils;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.extra_user.entity.ShopClassify;
import com.thinkgem.jeesite.modules.extra_user.entity.UserShopManage;
import com.thinkgem.jeesite.modules.extra_user.service.ShopClassifyService;
import com.thinkgem.jeesite.modules.extra_user.service.UserShopManageService;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户个人信息
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/app/shopManage")
public class AppShopManageController extends BaseController {

    @Resource
    private ShopClassifyService shopClassifyService;

    @Resource
    private UserShopManageService userShopManageService;

    /**
     * 商铺分类
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/shopClassifyList", method = RequestMethod.POST)
    public String withdrawBankList(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("shopClassify", shopClassifyService.findList(new ShopClassify()));
        return success("成功!!", response, model);
    }

    /**
     * 个人商铺状态
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUserShopStatus", method = RequestMethod.POST)
    public String getUserShopStatus(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserShopManage shopManage = userShopManageService.getByUserName(UserInfoUtils.getUser().getUserName());
        String shopStatus = "2";
        if (shopManage != null) {
            shopStatus = shopManage.getShopStatus();
        }
        model.addAttribute("shopStatus", shopStatus);
        return success("成功!!", response, model);
    }

    /**
     * 个人商铺
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getUserShopInfo", method = RequestMethod.POST)
    public String getUserShopInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("shopId");
        UserShopManage userShopManage = null;
        if (StringUtils2.isNotBlank(id)) {
            userShopManage = userShopManageService.get(id);
        } else {
            userShopManage = userShopManageService.getByUserName(UserInfoUtils.getUser().getUserName());
        }
        model.addAttribute("shopInfo", userShopManage);
        return success("成功!!", response, model);
    }

    /**
     * 通过id获取商铺信息
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getShopInfoById", method = RequestMethod.POST)
    public String getShopInfoById(HttpServletRequest request, HttpServletResponse response, Model model) {
        String shopId = request.getParameter("shopId");
        model.addAttribute("shopInfo", userShopManageService.get(shopId));
        return success("成功!!", response, model);
    }

    /**
     * 附近商铺
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/getNearShopInfo", method = RequestMethod.POST)
    public String getNearShopInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserShopManage userShopManage = new UserShopManage();
        userShopManage.setShopStatus(EnumUtil.YesNO.YES.toString());
        List<UserShopManage> shopManageList = userShopManageService.findList(userShopManage);
        ArrayList nearlist = new ArrayList();
        int id = 0;
        for (UserShopManage nearShop : shopManageList) {
            id++;
            HashMap<String, String> map = new HashMap<>();
            map.put("name", nearShop.getShopName());
            map.put("lon", nearShop.getAddressX());
            map.put("lat", nearShop.getAddressY());
            map.put("address",nearShop.getShopAddress());
            nearlist.add(id - 1, map);
        }
        model.addAttribute("nearlist", nearlist);
        return success("成功!!", response, model);
    }

    /**
     * 审核提交
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveShopAddress", method = RequestMethod.POST)
    public String saveShopAddress(HttpServletRequest request, HttpServletResponse response, Model model) {
        String id = request.getParameter("id");
        String address = request.getParameter("address");
        String lon = request.getParameter("lon");
        String lat = request.getParameter("lat");
        if (StringUtils2.isNotBlank(id)) {
            UserShopManage userShopManage = userShopManageService.get(id);
            userShopManage.setShopAddress(address);
            userShopManage.setAddressX(lon);
            userShopManage.setAddressY(lat);
            try {
                userShopManageService.save(userShopManage);
                return success("成功!!", response, model);
            } catch (ValidationException e) {
                return error("失败: " + e.getMessage(), response, model);
            }
        }
        return error("失败: ", response, model);
    }

    /**
     * 保存商铺信息
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/saveUserShop", method = RequestMethod.POST)
    public String saveUserShop(UserShopManage userShopManage, HttpServletRequest request, HttpServletResponse response, Model model) {
        userShopManage.setUserName(UserInfoUtils.getUser().getUserName());
        try {
            userShopManage.setShopStatus(EnumUtil.YesNO.NO.toString());
            if(!IdcardUtils.validateCard(userShopManage.getIdCard())){
                return error("请填写真实的身份证号" , response, model);
            }
            if(!VerifyUtils.isMobile(userShopManage.getMobile())){
                return error("请填写正确的手机号" , response, model);
            }
            userShopManageService.save(userShopManage);
            return success("成功!!", response, model);
        } catch (ValidationException e) {
            return error("失败: " + e.getMessage(), response, model);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam(value = "file" ,required = false)MultipartFile file, @RequestParam("name")String name,HttpServletRequest request, HttpServletResponse response, Model model) {
        if (file == null) {
            return error("上传失败,请更换图片重试", response, model);
        }
        String path = request.getSession().getServletContext().getRealPath("AppUpload");
        String fileName = file.getOriginalFilename();
//        String fileName = new Date().getTime()+".jpg";
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        //保存
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            return error("失败:"+e.getMessage(), response, model);
        }
        model.addAttribute("path", request.getContextPath()+"/AppUpload/"+fileName);
        return success("成功!!", response, model);

    }


    /**
     * 根据分类id查询商户
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/findUserShopByClassifyId", method = RequestMethod.POST)
    public String findUserShopByClassifyId(HttpServletRequest request, HttpServletResponse response, Model model) {
        String classId = request.getParameter("classId");
        UserShopManage userShopManage = new UserShopManage();
        userShopManage.setclassifyId(classId);
        userShopManage.setShopStatus(EnumUtil.YesNO.YES.toString());
        Page<UserShopManage> userShops = userShopManageService.findPage(new Page<UserShopManage>(request, response), userShopManage);
        model.addAttribute("userShops", userShops);
        return success("成功!!", response, model);
    }

}
