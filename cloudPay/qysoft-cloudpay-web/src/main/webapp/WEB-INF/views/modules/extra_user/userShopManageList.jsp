<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>店铺信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/extra_user/userShopManage/">店铺信息列表</a></li>
		<%--<shiro:hasPermission name="user:userShopManage:edit"><li><a href="${ctx}/extra_user/userShopManage/form">店铺信息添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="userShopManage" action="${ctx}/extra_user/userShopManage/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="225" class="input-medium"/>
			</li>
			<li><label>店铺名称：</label>
				<form:input path="shopName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

				<th>编号</th>
					
				<th>开铺人</th>

				<th>真实姓名</th>
					
				<th>店铺名称</th>
					
				<%--<th>店铺图片</th>--%>
					<%----%>
				<%--<th>营业执照</th>--%>
					<%----%>
				<%--<th>身份证图片</th>--%>
					
				<%--<th>店铺地址</th>--%>
					
				<th>店铺审核状态</th>

				<th>申请日期</th>

				<%--<th>审核时间</th>--%>
					<%----%>
				<%--<th>备注</th>--%>
				<shiro:hasPermission name="user:userShopManage:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userShopManage">
			<tr>
				<td>
						${userShopManage.id}
				</td>
				<td>
					${userShopManage.userName}
				</td>
				<td>
						${userShopManage.trueName}
				</td>
				<td>
					${userShopManage.shopName}
				</td>
				<%--<td>--%>
					<%--${userShopManage.shopAddress}--%>
				<%--</td>--%>
				<td>
						${fns:getDictLabel(userShopManage.shopStatus, 'usercenter_type', '')}

				</td>
				<td>
					<fmt:formatDate value="${userShopManage.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${userShopManage.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${userShopManage.remarks}--%>
				<%--</td>--%>
				<shiro:hasPermission name="user:userShopManage:edit"><td>
    				<a href="${ctx}/extra_user/userShopManage/form?id=${userShopManage.id}" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal">修改</a>
					<%--<a href="${ctx}/extra_user/userShopManage/delete?id=${userShopManage.id}" onclick="return confirmx('确认要删除该店铺信息修改吗？', this.href)">删除</a>--%>
				&nbsp;|&nbsp;
				<c:if test="${userShopManage.shopStatus==0}">
					<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/extra_user/userShopManage/updatestatus?id=${userShopManage.id}" onclick="layui.qyframe.openPromptMsg('店铺审核','是否同意','${userShopManage.userName}',[{'label':'是','value':'1'},{'label':'否','value':'0'}],'1',this.href);return  false;">处理</a>
				</c:if>
				<c:if test="${userShopManage.shopStatus==1}">
					已同意
				</c:if>
				<c:if test="${userShopManage.shopStatus==2}">
					已驳回
				</c:if></td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
		<script type="text/javascript">

            layui.use('qyframe', function() {
                var qyframe = layui.qyframe;
            });

		</script>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>