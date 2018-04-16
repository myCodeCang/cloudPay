<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/themes/basic/layouts/taglib.jsp" %>

<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html class="no-js css-menubar" lang="zh-cn">

<head>
    <title>主页</title>
    <%@ include file="/WEB-INF/views/website/themes/basic/layouts/head.jsp" %>

    <%@ include file="/WEB-INF/views/website/themes/basic/layouts/script.jsp" %>
</head>

<body class="site-contabs-open">

<nav class="site-navbar navbar navbar-default navbar-fixed-top navbar-inverse" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle hamburger hamburger-close navbar-toggle-left hided" data-toggle="menubar">
            <span class="sr-only">切换菜单</span> <span class="hamburger-bar"></span>
        </button>
        <button type="button" class="navbar-toggle collapsed" data-target="#admui-navbarCollapse" data-toggle="collapse">
            <i class="icon wb-more-horizontal" aria-hidden="true"></i>
        </button>
        <div class="navbar-brand navbar-brand-center site-gridmenu-toggle" data-toggle="gridmenu">
            <img class="navbar-brand-logo visible-lg visible-xs navbar-logo" src="${ctxStatic}/qysoftui/images/logo-white.svg" title="Admui">
            <img class="navbar-brand-logo hidden-xs hidden-lg navbar-logo-mini" src="${ctxStatic}/qysoftui/images/logo-white-min.svg" title="Admui">
        </div>
    </div>
    <div class="navbar-container container-fluid">
        <div class="collapse navbar-collapse navbar-collapse-toolbar" id="admui-navbarCollapse">
            <ul class="nav navbar-toolbar navbar-left">
                <li class="hidden-float">
                    <a data-toggle="menubar" class="hidden-float" href="javascript:;" role="button" id="admui-toggleMenubar">
                        <i class="icon hamburger hamburger-arrow-left"> <span class="sr-only">切换目录</span>
                            <span class="hamburger-bar"></span> </i>
                    </a>
                </li>
                <li class="navbar-menu nav-tabs-horizontal nav-tabs-animate is-load" id="admui-navMenu">
                    <ul class="nav navbar-toolbar nav-tabs" role="tablist">
                        <li role="presentation" class="active">
                            <a data-toggle="tab" href="#admui-navTabsItem-2" aria-controls="admui-navTabsItem-2" role="tab" aria-expanded="false">
                                <i class="icon wb-library"></i> <span>会员管理</span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <ul class="nav navbar-toolbar navbar-right navbar-toolbar-right">
                <li class="dropdown" id="admui-navbarMessage">
                    <a data-toggle="dropdown" href="javascript:;" class="msg-btn" aria-expanded="false" data-animation="scale-up" role="button">
                        <i class="icon wb-bell" aria-hidden="true"></i>
                        <span class="badge badge-danger up msg-num"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-right dropdown-menu-media" role="menu">
                        <li class="dropdown-menu-header" role="presentation">
                            <h5>最新消息</h5>
                            <span class="label label-round label-danger"></span>
                        </li>
                        <li class="list-group" role="presentation">
                            <div id="admui-messageContent" data-height="220px" data-plugin="slimScroll">
                                <p class="text-center height-200 vertical-align">
                                    <small class="vertical-align-middle opacity-four">没有新消息</small>
                                </p>
                                <script type="text/html" id="admui-messageTpl">
                                    {{each msgList}}
                                    <a class="list-group-item" href="/system/account.html" target="_blank" data-pjax role="menuitem">
                                        <div class="media">
                                            <div class="media-left padding-right-10">
                                                <i class="icon {{$value.type | iconType}} bg-red-600 white icon-circle" aria-hidden="true"></i>
                                            </div>
                                            <div class="media-body">
                                                <h6 class="media-heading">{{$value.content}}</h6>
                                                <time class="media-meta" datetime="{{$value.sendTime}}">
                                                    {{$value.sendTime | timeMsg}}
                                                </time>
                                            </div>
                                        </div>
                                    </a>
                                    {{/each}}
                                </script>
                            </div>
                        </li>
                        <li class="dropdown-menu-footer" role="presentation">
                            <a href="/system/account/index.html" target="_blank" data-pjax>
                                <i class="icon fa-navicon"></i> 所有消息
                            </a>
                        </li>
                    </ul>
                </li>
                <li class="hidden-xs" id="admui-navbarDisplay" data-toggle="tooltip" data-placement="bottom" title="设置主题与布局等">
                    <a class="icon wb-layout" href="/system/settings/display.html" target="_blank" data-pjax>
                        <span class="sr-only">主题与布局</span>
                    </a>
                </li>
                <li class="hidden-xs" id="admui-navbarFullscreen" data-toggle="tooltip" data-placement="bottom" title="全屏">
                    <a class="icon icon-fullscreen" data-toggle="fullscreen" href="#" role="button">
                        <span class="sr-only">全屏</span>
                    </a>
                </li>
                <li>
                    <a class="icon fa-sign-out" id="admui-signOut" data-ctx="" data-user="5" href="/f/logout" role="button">
                        <span class="sr-only">退出</span>
                    </a>
                </li>

            </ul>
        </div>
    </div>
</nav>
<nav class="site-menubar">
    <div class="site-menubar-body">
        <div class="tab-content height-full" id="admui-navTabs">
            <div class="tab-pane animation-fade height-full active" id="admui-navTabsItem-2" role="tabpanel">
                <ul class="site-menu">
                    <li class="site-menu-category">会员管理</li>
                    <li class="site-menu-item">
                        <a data-pjax href="${ctx}" target="_blank">
                            <i class="site-menu-icon fa-home" aria-hidden="true"></i><span class="site-menu-title">会员首页</span>
                        </a>
                    </li>
                    <li class="site-menu-category">个人信息</li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/updateUserinfo" target="_blank">
                            <i class="site-menu-icon fa-indent" aria-hidden="true"></i><span class="site-menu-title">资料修改</span>
                        </a>
                    </li>
                    <li class="site-menu-item">
                        <a data-pjax href="${ctx}/user/updatePwd" target="_blank">
                            <i class="site-menu-icon fa-edit" aria-hidden="true"></i><span class="site-menu-title">修改密码</span>
                        </a>
                    </li>
                    <li class="site-menu-category">会员管理</li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/user/userGrid" target="_blank">
                            <i class="site-menu-icon fa-area-chart" aria-hidden="true"></i><span class="site-menu-title">会员网络图</span>
                        </a>
                    </li>
                    <li class="site-menu-category">报单中心</li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/applyUserCenter" target="_blank">
                            <i class="site-menu-icon fa-list-alt" aria-hidden="true"></i><span class="site-menu-title">申请报单中心</span>
                        </a>
                    </li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/searchUserCenter" target="_blank">
                            <i class="site-menu-icon fa-cubes" aria-hidden="true"></i><span class="site-menu-title">申请进度查询</span>
                        </a>
                    </li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/addSubuser" target="_blank">
                            <i class="site-menu-icon fa-check-square-o" aria-hidden="true"></i><span class="site-menu-title">会员注册</span>
                        </a>
                    </li>
                    <li class="site-menu-category">财务管理</li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/bonusReport" target="_blank">
                            <i class="site-menu-icon fa-files-o" aria-hidden="true"></i><span class="site-menu-title">奖金报表</span>
                        </a>
                    </li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/accoundChange" target="_blank">
                            <i class="site-menu-icon fa-cube" aria-hidden="true"></i><span class="site-menu-title">账变列表</span>
                        </a>
                    </li>
                    <li class="site-menu-category">充值提现</li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/applyWithdraw" target="_blank">
                            <i class="site-menu-icon fa-bars" aria-hidden="true"></i><span class="site-menu-title">提现申请</span>
                        </a>
                    </li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/withdrawDetail" target="_blank">
                            <i class="site-menu-icon fa-table" aria-hidden="true"></i><span class="site-menu-title">提现明细</span>
                        </a>
                    </li>
                    <li class="site-menu-item has-sub">
                        <a data-pjax href="${ctx}/user/updatePwd" target="_blank">
                            <i class="site-menu-icon fa-puzzle-piece" aria-hidden="true"></i><span class="site-menu-title">二级密码</span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</nav>
<nav class="site-contabs" id="admui-siteConTabs">
    <button type="button" class="btn btn-icon btn-default pull-left hide">
        <i class="icon fa-angle-double-left"></i>
    </button>
    <div class="contabs-scroll pull-left">
        <ul class="nav con-tabs">
            <li class="active">
                <a data-pjax href="/f" rel="contents"><span>首页</span>
                </a>
            </li>
        </ul>
    </div>
    <div class="btn-group pull-right">
        <button type="button" class="btn btn-icon btn-default hide">
            <i class="icon fa-angle-double-right"></i>
        </button>
        <button type="button" class="btn btn-default dropdown-toggle btn-outline" data-toggle="dropdown" aria-expanded="false">
            <span class="caret"></span> <span class="sr-only">切换菜单</span>
        </button>
        <ul class="dropdown-menu dropdown-menu-right" aria-labelledby="conTabsDropdown" role="menu">
            <li class="reload-page" role="presentation">
                <a href="javascript:;" role="menuitem"><i class="icon wb-reload"></i> 刷新当前</a>
            </li>
            <li class="close-other" role="presentation">
                <a href="javascript:;" role="menuitem"><i class="icon wb-close"></i> 关闭其他</a>
            </li>
            <li class="close-all" role="presentation">
                <a href="javascript:;" role="menuitem"><i class="icon wb-power"></i> 关闭所有</a>
            </li>
        </ul>
    </div>
</nav>

<main class="site-page">
    <div class="page-container" id="admui-pageContent">
        <title><sitemesh:title /></title>
        <sitemesh:body/>
    </div>
    <div class="page-loading vertical-align text-center">
        <div class="page-loader loader-default loader vertical-align-middle" data-type="default"></div>
    </div>
</main>

<footer class="site-footer">
    <div class="site-footer-legal">&copy; 2016
        <a href="http://admui.com" target="_blank">Admui</a>
    </div>
    <div class="site-footer-right">
        当前版本：v1.0.0
        <a class="margin-left-5" data-toggle="tooltip" title="升级" href="#" target="_blank">
            <i class="icon fa-cloud-upload"></i>
        </a>
    </div>
</footer>

<%@ include file="/WEB-INF/views/website/themes/basic/layouts/footer.jsp" %>

</body>
</html>

