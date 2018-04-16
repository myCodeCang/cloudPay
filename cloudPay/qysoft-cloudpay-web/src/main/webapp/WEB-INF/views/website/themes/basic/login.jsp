<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/themes/basic/layouts/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">

<head>
    <title>登录到 ${fns:getOption("system_sys","site_name")} 系统</title>
    <%@ include file="/WEB-INF/views/website/themes/basic/layouts/head.jsp" %>
    <style>
        html {
            height: 100%;
        }
        .page-login {
            height: 100%;
            min-height: 600px;
            padding-top: 0;
            overflow: hidden;
        }
        .page-login:before {
            background-image: url("${ctxStatic}/qysoftui/images/login.jpg");
        }
    </style>
    <!-- 自定义 -->
    <%--<link rel="stylesheet" href="${ctxStatic}/qysoftui/css/login.css">--%>
    <!-- 插件 -->
    <%--<link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/animsition/animsition.css">--%>
    <!-- 图标 -->
    <%--<link rel="stylesheet" href="${ctxStatic}/qysoftui/fonts/web-icons/web-icons.css">--%>
    <!-- 插件 -->
    <%--<link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/formvalidation/formValidation.css">--%>
</head>

<body class="page-login layout-full page-dark">
<div class=" page animation-fade vertical-align text-center animsition-fade height-full">
    <div class="page-content vertical-align-middle row">
        <%--<div class="col-xs-12 col-md-12 col-lg-12 margin-bottom-60">--%>
            <%--<img src="${ctxStatic}/qysoftui/images/logo-haiyu.png"style="width:60%;"/>--%>
        <%--</div>--%>

        <a href="${pageContext.request.contextPath}/a/login">
        <button type="submit" class="btn btn-success btn-block btn-outline col-xs-6 col-md-12 col-lg-6 margin-bottom-10 font-size-18">会员</button>
        </a>
        <a href="${fns:getOption("auth_shop","url" )}">
        <button type="submit" class="btn btn-warning btn-block btn-outline col-xs-6 col-md-12 col-lg-6 font-size-18">商城</button>
        </a>
    </div>
</div>

<!-- JS -->
<%--<script src="${ctxStatic}/qysoftui/vendor/jquery/jquery.min.js"></script>--%>
<%--<script src="${ctxStatic}/qysoftui/vendor/bootstrap/bootstrap.min.js"></script>--%>
<%--<script src="${ctxStatic}/qysoftui/vendor/formvalidation/formValidation.min.js" data-name="formValidation"></script>--%>
<%--<script src="${ctxStatic}/qysoftui/vendor/formvalidation/framework/bootstrap.min.js" data-deps="formValidation"></script>--%>
<%--<script src="${ctxStatic}/qysoftui/js/login.js"></script>--%>
</body>

</html>
