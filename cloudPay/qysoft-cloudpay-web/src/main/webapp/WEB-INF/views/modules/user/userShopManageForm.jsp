<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
                    $("#btnSubmit").attr("disabled", true);
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
				},
                rules:{
                    changeMoney:{
                        required:true,
                        checkName:true,
                    },

                },
            });

            $.validator.addMethod("checkName",function(value,element,params){
                var checkName = /^(-?)([1-9]\d*|0)(\.\d{1,2})?$/g;
                return this.optional(element)||(checkName.test(value));
            },"*只能输入整数或两位小数！");
        });

        layui.use('layer', function() {
		var layer = layui.layer;
	 	}); 

//        function charge(){
//			layui.layer.open({
//			 	content: '您确定要开通代理吗？'
//				,btn: ['确认', '取消']
//				,yes: function(index){
//
//					$("#inputForm").submit();
//					layui.layer.close(index);
//					return;
//				}
//			 });
//		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">开通商家</a></li>
	</ul><br/>
	<form id="inputForm"  action="${ctx}/user/userUserinfo/updateUserShopManage" method="post" class="form-horizontal">

		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">选择开通商家账号：</label>
			<div class="controls">
				<sys:userinfoSelect id="userName"  title="用户选择" cssClass="required"  userLevelId="3" value="${userName}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">代理区域:</label>--%>
			<%--<div class="controls">--%>
				<%--<sys:treeselect id="mainPerformance" name="mainPerformance" value="${area.parent.id}" labelName="parent.name" labelValue="${area.parent.name}"--%>
								<%--title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="" allowClear="true"/><span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>

		<div class="form-actions">
			<shiro:hasPermission name="user:UserChargeLog:edit"><input id="btnSubmit"  class="btn btn-primary" type="submit" value="开通商家"/>&nbsp;</shiro:hasPermission>

		</div>
	</form>
</body>
</html>