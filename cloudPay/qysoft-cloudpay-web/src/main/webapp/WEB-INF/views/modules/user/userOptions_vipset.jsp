<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>配置管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("[name=inputForm]").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

            if(${options.system_user_set.double_line_open == "on"}){
                $("#isOpenSwitch").attr("checked",true);
            }else{
                $("#isOpenSwitch").attr("checked",false);
            }
            if(${options.system_user_set.center_open == "on"}){
                $("#isOpenCenterSwitch").attr("checked",true);
            }else{
                $("#isOpenCenterSwitch").attr("checked",false);
            }
        });


    </script>


</head>
<body>
<sys:message content="${message}"/>
<div class="layui-tab layui-tab-brief" lay-filter="optionlayuitab">
    <ul class="layui-tab-title">
        <c:forEach items="${optionList}" var="dic" varStatus="status">

            <li lay-id="${dic.optionName}"
                class="${dic.optionName  eq selOptLabel ? 'layui-this':''}">${dic.optionLabel } [${dic.optionName }]
            </li>
        </c:forEach>
    </ul>
    <div class="layui-tab-content">

        <div class="layui-tab-item layui-show">

            <form name="inputForm" modelAttribute="optionValue" action="${ctx}/user/userOptions/saveUserSet" method="post" class="form-horizontal layui-form">
                <input type="hidden" name="optName" value="system_user_set"/>

                <blockquote class="layui-elem-quote">会员注册配置</blockquote>

                <input type="hidden" value="" name="double_line_id" id="double_line_id">

                <div class="control-group">
                    <label class="control-label" id="lables">开启双线限制：</label>
                    <div class="controls">
                        <input type="hidden"  name="double_line_open" value="${options.system_user_set.double_line_open}" id="istransopen"/>
                        <input type="checkbox"  lay-skin="switch" lay-text="开启|关闭" lay-filter="doubleLineLimit" id="isOpenSwitch">
                        <span class="help-inline"><font color="red">*</font> 标识:[double_line_open]</span>
                    </div>

                </div>

                <div class="control-group">
                    <label class="control-label" id="lable">启用报单中心：</label>
                    <div class="controls">
                        <input type="hidden"  name="center_open" value="${options.system_user_set.center_open}" id="isCenteropen"/>
                        <input type="checkbox"  lay-skin="switch" lay-text="开启|关闭" lay-filter="centerReport" id="isOpenCenterSwitch">
                        <span class="help-inline"><font color="red">*</font> 标识:[center_open]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >会员账号生成方式：</label>
                    <div class="controls">
                        <input type="radio" name="user_create_type" value="rand" title="随机生成" ${options.system_user_set.user_create_type == 'rand'?'checked':''}>
                        <input type="radio" name="user_create_type" value="sequence" title="序列生成" ${options.system_user_set.user_create_type == 'sequence'?'checked':''}>
                        <span class="help-inline"><font color="red">*</font> 标识:[user_create_type]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" >会员账号生成前缀：</label>
                    <div class="controls">
                        <input type="text" name="user_id_prefix"  value="${options.system_user_set.user_id_prefix}" placeholder="请输入前缀" style="width: 80px;" class="input-xlarge" >
                        <span class="help-inline"><font color="red">*</font> 标识:[user_id_prefix]</span>
                    </div>
                </div>
                <blockquote class="layui-elem-quote">会员自动升级配置</blockquote>
                <div class="control-group">
                    <label class="control-label" style="width: auto"> 普通用户升级正式用户(单位:丝路)：</label>
                    <div class="controls">
                        <input type="number" name="common_up_formal" id="common_up_formal"
                               value="${options.system_user_set.common_up_formal}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[common_up_formal]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label" style="width: auto"> 普通用户升级vip用户(单位:丝路)：</label>
                    <div class="controls">
                        <input type="number" name="common_up_vip" id="common_up_vip"
                               value="${options.system_user_set.common_up_vip}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[common_up_vip]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label" style="width: auto"> 正式用户升级vip用户(单位:丝路)：</label>
                    <div class="controls">
                        <input type="number" name="formal_up_vip" id="formal_up_vip"
                               value="${options.system_user_set.formal_up_vip}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[formal_up_vip]</span>
                    </div>
                </div>
                <blockquote class="layui-elem-quote">提现配置</blockquote>

                <label class="control-label"> 提现日期配置：</label>
                <input type="checkbox" name="Sunday" title="星期日" ${options.system_user_set.Sunday == 'on'?'checked':''}>
                <input type="checkbox" name="Monday" title="星期一" ${options.system_user_set.Monday == 'on'?'checked':''}>
                <input type="checkbox" name="Tuesday" title="星期二" ${options.system_user_set.Tuesday == 'on'?'checked':''}>
                <input type="checkbox" name="Wensday" title="星期三" ${options.system_user_set.Wensday == 'on'?'checked':''}>
                <input type="checkbox" name="Thursday" title="星期四" ${options.system_user_set.Thursday == 'on'?'checked':''}>
                <input type="checkbox" name="Friday" title="星期五" ${options.system_user_set.Friday == 'on'?'checked':''}>
                <input type="checkbox" name="Saturday" title="星期六" ${options.system_user_set.Saturday == 'on'?'checked':''}>
                <div class="layui-field-box">
                    <div class="control-group">
                        <label class="control-label">提现开始时间：</label>
                        <div class="controls">
                            <input name="withdraw_time_begin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
                                   value="${options.system_user_set.withdraw_time_begin}"
                                   onclick="WdatePicker({dateFmt:' HH:mm:ss',isShowClear:false});"/>
                            <span class="help-inline"><font
                                    color="red">*</font> 标识:[withdraw_time_begin] </span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">提现结束时间：</label>
                        <div class="controls">
                            <input name="withdraw_time_end" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
                                   value="${options.system_user_set.withdraw_time_end}"
                                   onclick="WdatePicker({dateFmt:' HH:mm:ss',isShowClear:false});"/>
                            <span class="help-inline"><font
                                    color="red">*</font> 标识:[withdraw_time_end] </span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">手续费配置：</label>
                        <div class="controls">
                            <input type="number" name="poundage" id="poundage"
                                   value="${options.system_user_set.poundage}" htmlEscape="false"
                                   class="input-xlarge required"/> <span class="help-inline"><font
                                color="red">*</font> 标识:[poundage]</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">提现最小额度配置：</label>
                        <div class="controls">
                            <input type="number" name="minMoney" id="minMoney"
                                   value="${options.system_user_set.minMoney}" htmlEscape="false"
                                   class="input-xlarge required"/> <span class="help-inline"><font
                                color="red">*</font> 标识:[minMoney]</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">提现最大额度配置：</label>
                        <div class="controls">
                            <input type="number" name="maxMoney" id="maxMoney"
                                   value="${options.system_user_set.maxMoney}" htmlEscape="false"
                                   class="input-xlarge required"/> <span class="help-inline"><font
                                color="red">*</font> 标识:[maxMoney]</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">提现倍数配置：</label>
                        <div class="controls">
                            <input name="multiple" id="multiple"
                                   value="${options.system_user_set.multiple}" htmlEscape="false"
                                   class="input-xlarge required"
                                   onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
                                   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                            <span class="help-inline"><font
                                    color="red">*</font> 标识:[multiple]</span>
                        </div>
                    </div>
                </div>

                <div class="form-actions">
                    <shiro:hasPermission name="user:userUserinfo:edit">
                        <input id="btnSubmit" class="btn btn-primary" type="submit"
                               value="保 存 配 置"/>&nbsp;</shiro:hasPermission>
                </div>

            </form>
        </div>

        <div class="layui-tab-item layui-show">

            <form name="bounsForm" modelAttribute="optionValue" action="${ctx}/user/userOptions/saveUserSet" method="post" class="form-horizontal layui-form">
                <input type="hidden" name="optName" value="system_user_bonus"/>
                <blockquote class="layui-elem-quote">转账/消费手续费配置</blockquote>

                <div class="control-group">
                    <label class="control-label">转账/消费手续费: </label>
                    <div class="controls">
                        <input type="text" name="transfer_fee" id="transfer_fee"
                               value="${options.system_user_bonus.transfer_fee}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[transfer_fee]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">用户直推商家奖励: </label>
                    <div class="controls">
                        <input type="text" name="user_expand" id="user_expand"
                               value="${options.system_user_bonus.user_expand}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[user_expand]</span>
                    </div>
                </div>


                <div class="form-actions">
                    <shiro:hasPermission name="user:userUserinfo:edit">
                        <input id="btnSubmit" class="btn btn-primary" type="submit"
                               value="保 存 配 置"/>&nbsp;</shiro:hasPermission>
                </div>

            </form>
        </div>

        <div class="layui-tab-item layui-show">

            <form name="bounsForm" modelAttribute="optionValue" action="${ctx}/user/userOptions/saveUserSet" method="post" class="form-horizontal layui-form">
                <input type="hidden" name="optName" value="system_static_bonus"/>
                <blockquote class="layui-elem-quote">静态奖金配置</blockquote>

                <div class="control-group">
                    <label class="control-label">丝路账户解冻比例-现金: </label>
                    <div class="controls">
                        <input type="text" name="red_packet_persent_gold" id="red_packet_persent_gold"
                               value="${options.system_static_bonus.red_packet_persent_gold}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[red_packet_persent_gold]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">丝路账户解冻比例-积分: </label>
                    <div class="controls">
                        <input type="text" name="red_packet_persent_score" id="red_packet_persent_score"
                               value="${options.system_static_bonus.red_packet_persent_score}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[red_packet_persent_score]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">普通用户: </label>
                    <div class="controls">
                        <input type="text" name="normal_user" id="normal_user"
                               value="${options.system_static_bonus.normal_user}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[normal_user]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">正式用户: </label>
                    <div class="controls">
                        <input type="text" name="formal_user" id="formal_user"
                               value="${options.system_static_bonus.formal_user}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[formal_user]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">vip用户: </label>
                    <div class="controls">
                        <input type="text" name="vip_user" id="vip_user"
                               value="${options.system_static_bonus.vip_user}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[vip_user]</span>
                    </div>
                </div>
                <%--<div class="control-group">--%>
                    <%--<label class="control-label">红包金额解冻比例-商城积分: </label>--%>
                    <%--<div class="controls">--%>
                        <%--<input type="text" name="red_packet_persent_points" id="red_packet_persent_points"--%>
                               <%--value="${options.system_static_bonus.red_packet_persent_points}" htmlEscape="false"--%>
                               <%--class="input-xlarge required"/> <span class="help-inline"><font--%>
                            <%--color="red">*</font> 标识:[red_packet_persent_points]</span>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <%--<div class="control-group">--%>
                    <%--<label class="control-label">红包金额解冻比例-手续费: </label>--%>
                    <%--<div class="controls">--%>
                        <%--<input type="text" name="red_packet_persent_charge" id="red_packet_persent_charge"--%>
                               <%--value="${options.system_static_bonus.red_packet_persent_charge}" htmlEscape="false"--%>
                               <%--class="input-xlarge required"/> <span class="help-inline"><font--%>
                            <%--color="red">*</font> 标识:[red_packet_persent_charge]</span>--%>
                    <%--</div>--%>
                <%--</div>--%>


                <div class="form-actions">
                    <shiro:hasPermission name="user:userUserinfo:edit">
                        <input id="btnSubmit" class="btn btn-primary" type="submit"
                               value="保 存 配 置"/>&nbsp;</shiro:hasPermission>
                </div>

            </form>
        </div>

        <div class="layui-tab-item layui-show">

            <form name="bounsForm" modelAttribute="optionValue" action="${ctx}/user/userOptions/saveUserSet" method="post" class="form-horizontal layui-form">
                <input type="hidden" name="optName" value="system_lead_bonus"/>
                <blockquote class="layui-elem-quote">代理等级配置</blockquote>

                <div class="control-group">
                    <label class="control-label">国家代理条件(单位:万): </label>
                    <div class="controls">
                        <input type="text" name="contry_criteria" id="contry_criteria"
                               value="${options.system_lead_bonus.contry_criteria}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[contry_criteria]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">国家代理丝路收益: </label>
                    <div class="controls">
                        <input type="text" name="contry_earnings" id="contry_earnings"
                               value="${options.system_lead_bonus.contry_earnings}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[contry_earnings]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">省级代理条件(单位:万): </label>
                    <div class="controls">
                        <input type="text" name="province_criteria" id="province_criteria"
                               value="${options.system_lead_bonus.province_criteria}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[province_criteria]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">省级代理丝路收益: </label>
                    <div class="controls">
                        <input type="text" name="province_earnings" id="province_earnings"
                               value="${options.system_lead_bonus.province_earnings}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[province_earnings]</span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">市级代理条件(单位:万): </label>
                    <div class="controls">
                        <input type="text" name="city_criteria" id="city_criteria"
                               value="${options.system_lead_bonus.city_criteria}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[city_criteria]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">市级代理思路受益: </label>
                    <div class="controls">
                        <input type="text" name="city_earnings" id="city_earnings"
                               value="${options.system_lead_bonus.city_earnings}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[city_earnings]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">区县级代理条件(单位:万): </label>
                    <div class="controls">
                        <input type="text" name="county_criteria" id="county_criteria"
                               value="${options.system_lead_bonus.county_criteria}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[county_criteria]</span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">区县级代理丝路收益: </label>
                    <div class="controls">
                        <input type="text" name="county_earnings" id="county_earnings"
                               value="${options.system_lead_bonus.county_earnings}" htmlEscape="false"
                               class="input-xlarge required"/> <span class="help-inline"><font
                            color="red">*</font> 标识:[county_earnings]</span>
                    </div>
                </div>

                <div class="form-actions">
                    <shiro:hasPermission name="user:userUserinfo:edit">
                        <input id="btnSubmit" class="btn btn-primary" type="submit"
                               value="保 存 配 置"/>&nbsp;</shiro:hasPermission>
                </div>

            </form>
        </div>



        <div class="layui-tab-item">
        </div>
        <div class="layui-tab-item">其他设置</div>
        <div class="layui-tab-item">4</div>
        <div class="layui-tab-item">5</div>
        <div class="layui-tab-item">6</div>
    </div>
</div>



<script type="text/javascript">
    var form = null;
    layui.use(['element', 'form', 'template'], function () {
        var element = layui.element();
        form = layui.form();
        element.tabChange('optionlayuitab', '${selOptLabel}'); //假设当前地址为：http://a.com#test1=222，那么选项卡会自动切换到“发送消息”这一项

        form.on('switch(doubleLineLimit)', function(data){
            if(data.elem.checked){
                $("#istransopen").val("on");
            }
            else{
                $("#istransopen").val("off");
            }
        });

        form.on('switch(centerReport)', function(data){
            if(data.elem.checked){
                $("#isCenteropen").val("on");
            }
            else{
                $("#isCenteropen").val("off");
            }
        });
    });
</script>
</body>
</html>