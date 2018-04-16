<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>店铺信息修改管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/extra_user/userShopManage/">店铺信息修改列表</a></li>
		<li class="active"><a href="${ctx}/extra_user/userShopManage/form?id=${userShopManage.id}">店铺信息修改<shiro:hasPermission name="extra_user:userShopManage:edit">${not empty userShopManage.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="extra_user:userShopManage:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="userShopManage" action="${ctx}/extra_user/userShopManage/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">开铺人：</label>--%>
			<%--<div class="controls">--%>
				<%--<sys:userinfoSelect id="userName"  title="用户选择" cssClass="required"  value="${userShopManage.userName}"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">开铺人：</label>
			<div class="controls">
				<form:input path="userName" htmlEscape="false" maxlength="225" readonly="true" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">真实姓名：</label>
			<div class="controls">
				<form:input path="trueName" htmlEscape="false" maxlength="225" readonly="true" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证号码：</label>
			<div class="controls">
				<form:input path="idCard" htmlEscape="false" maxlength="225" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺名称：</label>
			<div class="controls">
				<form:input path="shopName" htmlEscape="false" maxlength="255" class="input-xlarge required "/>
			</div>
		</div>


		<div class="control-group">
			<label class="control-label">联系电话：</label>
			<div class="controls">
				<form:input path="mobile" htmlEscape="false" maxlength="225" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺地址：</label>
			<div class="controls">
				<form:input path="shopAddress" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务分类:</label>
			<div class="controls">
				<form:select path="classifyId" class="input-xlarge required">
					<%--<form:option value="" label="请选择"/>--%>
					<form:options items="${shopClassifys}" itemLabel="classifyname" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">服务介绍：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">店铺图片:</label>
			<div class="controls">
				<form:hidden id="storefrontImg" path="storefrontImg" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="storefrontImg" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺图片:</label>
			<div class="controls">
				<form:hidden id="storefrontImg1" path="storefrontImg1" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="storefrontImg1" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">店铺图片:</label>
			<div class="controls">
				<form:hidden id="storefrontImg2" path="storefrontImg2" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="storefrontImg2" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">营业执照:</label>
			<div class="controls">
				<form:hidden id="businessLicImg" path="businessLicImg" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="businessLicImg" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>

			</div>
		</div>

		<div class="control-group">
			<label class="control-label">手持身份证照片:</label>
			<div class="controls">
				<form:hidden id="idCardImg1" path="idCardImg1" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="idCardImg1" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证正面照片:</label>
			<div class="controls">
				<form:hidden id="idCardImg2" path="idCardImg2" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="idCardImg2" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>

			</div>
		</div>
		<div class="control-group">
			<label class="control-label">身份证背面照片:</label>
			<div class="controls">
				<form:hidden id="idCardImg3" path="idCardImg3" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="idCardImg3" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>

			</div>
		</div>


		<div class="form-actions">
			<shiro:hasPermission name="user:userShopManage:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>