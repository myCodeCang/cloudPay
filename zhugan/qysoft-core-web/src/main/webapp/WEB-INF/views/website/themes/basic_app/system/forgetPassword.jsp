<%--
  Created by IntelliJ IDEA.
  User: kevin
  Date: 2017/8/25
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE >
<html>
<head>
    <title>找回密码</title>
    <meta charset="utf-8">
    <title>${fns:getConfig('productName')}</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <%@ include file="/WEB-INF/views/website/themes/basic_app/layouts/apicloud_header.jsp" %>

    <style type="text/css">
        .home.aui-bar-nav {
            background-color: ${fns:getConfig('app.page.mainColor')};
            padding-top: 20px;
        }

        .cz-one .cz-one1st {
            background: #f3f3f3;
        }

        .cz-one .aui-list p {
            height: 1.8rem;
            line-height: 1.8rem;
            font-size: 0.65rem;
            background: #f3f3f3;
            padding-left: 0.75rem;
        }

        .cz-one input::-webkit-input-placeholder {
            font-size: 0.7rem;
        }

        .cz-one .aui-list-item-label {
            font-size: 0.75rem;
        }

        .cz-one .cz-one2st {
            height: 2.75rem;
            line-height: 2.75rem;
        }

        .cz-one .cz-one2st .aui-list-item-label {
            width: 100%;
            font-size: 0.75rem;
        }

        .cz-one .cz-one2st .aui-list-item-label img {
            width: 2rem;
        }

        .cz-one .cz-one2st .aui-list-item-label span {
            margin-left: 0.5rem;
            font-size: 0.75rem;
        }

        .cz-one .cz-one2st .aui-radio {
            margin-top: 0.25rem;
        }

        .aui-radio:checked {
            background-color: ${fns:getConfig('app.page.mainColor')};
            border: solid 1px ${fns:getConfig('app.page.mainColor')};
        }

        .fanshi-but {
            padding: 0 0.75rem;
            margin: 1.5rem 0;
        }

        .fanshi-but .aui-btn {
            background: ${fns:getConfig('app.page.mainColor')};
            color: #fff;
            height: 2.25rem;
            line-height: 2.25rem;
        }
    </style>
</head>
<body>
<header class="aui-bar aui-bar-nav home">
    <a class="aui-pull-left aui-btn back" href="#">
        <span class="aui-iconfont aui-icon-left"></span>
    </a>
    <div class="aui-title">找回密码</div>
</header>
<div class="height-one"></div>
<div class="aui-content">
    <ul class="aui-list aui-form-list regs_lst">
        <li class="aui-list-item">
            <div class="aui-list-item-inner">
                <div class="aui-list-item-label-icon">
                    <i class="aui-iconfont aui-icon-mobile"></i>
                </div>
                <div class="aui-list-item-input">
                    <input type="text" placeholder="请输入手机号" id="mobile">
                </div>
            </div>
        </li>
        <li class="aui-list-item">
            <div class="aui-list-item-inner">
                <div class="aui-list-item-label-icon">
                    <i class="aui-iconfont aui-icon-lock"></i>
                </div>
                <div class="aui-list-item-input">
                    <input type="text" placeholder="请输入验证码" id="code">
                </div>
                <div class="aui-list-item-label" style="width:12rem;display: inline-block;">
                    <div class="aui-btn btn_green aui-font-size-12 aui-pull-right" style="padding: 0 0.4rem;"  id="getVerifyCode">发送验证码</div>
                </div>
            </div>
        </li>
        <li class="aui-list-item">
            <div class="aui-list-item-inner">
                <div class="aui-list-item-label-icon">
                    <i class="aui-iconfont aui-icon-lock"></i>
                </div>
                <div class="aui-list-item-input">
                    <input type="password" placeholder="请输入新登录密码" id="newPwd">
                </div>
            </div>
        </li>
        <li class="aui-list-item">
            <div class="aui-list-item-inner">
                <div class="aui-list-item-label-icon">
                    <i class="aui-iconfont aui-icon-lock"></i>
                </div>
                <div class="aui-list-item-input">
                    <input type="password" placeholder="再次输入新登录密码" id="newPwds">
                </div>
            </div>
        </li>
    </ul>
    <div class="blank_15"></div>
    <div class="log-box">
        <div class="aui-btn aui-btn-block btn-login aui-margin-t-15" onclick="save()">确认修改</div>
    </div>
</div>
<div class="aui-content xiugai-but" onclick="save()">
    <div class="aui-btn aui-btn-block">保存</div>
</div>
<%@ include file="/WEB-INF/views/website/themes/basic_app/layouts/apicloud_footer.jsp" %>
</body>
</html>
