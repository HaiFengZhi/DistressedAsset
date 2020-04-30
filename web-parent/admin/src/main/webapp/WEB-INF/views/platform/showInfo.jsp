<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>个人信息</title>
</head>
<body>

<blockquote class="layui-elem-quote">个人信息展示</blockquote>
<div class="layui-row layui-col-space10">
    <form class="layui-form layui-form-pane layui-row ">
        <div class="layui-col-xs12 layui-col-sm6 layui-col-md6">
            <div class="layui-form-item">
                <label class="layui-form-label">用户名</label>
                <div class="layui-input-block">
                    <input type="text" value="${user.loginUsername}" disabled class="layui-input layui-disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">昵称</label>
                <div class="layui-input-block">
                    <input type="text" name="nickname" value="${user.nickname}" placeholder="请输入昵称" lay-verify="required" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">注册时间</label>
                <div class="layui-input-block">
                    <input type="text" value="<fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" disabled class="layui-input layui-disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">最后登陆时间</label>
                <div class="layui-input-block">
                    <input type="text" value="<fmt:formatDate value="${user.lastLoginTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" disabled class="layui-input layui-disabled">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">绑定手机</label>
                <div class="layui-input-block">
                    <input type="text" name="boundCellphone" value="${user.boundCellphone}" placeholder="请输入绑定手机" lay-verify="required|phone" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">绑定邮箱</label>
                <div class="layui-input-block">
                    <input type="text" name="boundEmail" value="${user.boundEmail}" placeholder="请输入绑定邮箱" lay-verify="required|email" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="changeAdminInfo">立即修改</button>
                    <button type="reset" class="layui-btn layui-btn-sm layui-btn-width-sm-80">重置</button>
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    layui.use(['jquery', 'layer', 'form'],function(){
        var $ = layui.$,
            layer = layui.layer,
            form = layui.form;
        //提交修改
        form.on("submit(changeAdminInfo)",function(data){
            console.log(JSON.stringify(data.field));
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            $.ajax({
                url:'/admin/setAdminInfo',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    layer.msg(res.message);
                },
                error:function (data) {
                    layer.msg(data.message);
                }
            }) ;
            return false;
        });

    });
</script>
</body>
</html>
