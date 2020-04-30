<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>欢迎页面</title>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title">
    <legend>系统快捷入口</legend>
</fieldset>
<div class="layui-row layui-col-space10 panel_box">
    <div class="panel layui-col-xs12 layui-col-sm6 layui-col-md4 layui-col-lg2">
        <a href="javascript:;" data-url="page/user/userList.html">
            <div class="panel_icon layui-bg-orange">
                <i class="layui-anim layui-icon layui-icon-user" ></i>
            </div>
            <div class="panel_word userAll">
                <span>1111</span>
<%--                <em>用户总数</em>--%>
                <cite <%--class="layui-hide"--%>>用户中心</cite>
            </div>
        </a>
    </div>
    <div class="panel layui-col-xs12 layui-col-sm6 layui-col-md4 layui-col-lg2">
        <a href="javascript:;" data-url="page/systemSetting/icons.html">
            <div class="panel_icon layui-bg-cyan">
                <i class="layui-anim layui-icon layui-icon-website"></i>
            </div>
            <div class="panel_word outIcons">
                <span>商城</span>
<%--                <em>主站地址</em>--%>
                <cite <%--class="layui-hide"--%>>网站管理</cite>
            </div>
        </a>
    </div>
    <div class="panel layui-col-xs12 layui-col-sm6 layui-col-md4 layui-col-lg2">
        <a href="javascript:;">
            <div class="panel_icon layui-bg-blue">
                <i class="layui-anim layui-icon layui-icon-date"></i>
            </div>
            <div class="panel_word">
                <span class="loginTime">2019-11-04 15:11</span>
                <cite>上次登录时间</cite>
            </div>
        </a>
    </div>
</div>
<br/>
<fieldset class="layui-elem-field layui-field-title">
    <legend>Shiro标签示例</legend>
</fieldset>
<blockquote class="layui-elem-quote">
    user 标签：用户已经通过认证\记住我 登录后显示响应的内容
    <shiro:user>
        欢迎[<shiro:principal property = "nickname"/>]登录 <a href = "logout">退出</a>
    </shiro:user>
</blockquote>

<blockquote class="layui-elem-quote">
    authenticated标签：用户身份验证通过，即 Subjec.login 登录成功 不是记住我登录的
    <shiro:authenticated>
        用户[<shiro:principal property = "nickname"/>] 已身份验证通过
    </shiro:authenticated>
</blockquote>
<blockquote class="layui-elem-quote">
    principal 标签：显示用户身份信息，默认调用Subjec.getPrincipal()获取，即Primary Principal
    【<shiro:principal property = "nickname"/>】【<shiro:principal property = "securityPassword" defaultValue="xxx"/>】
    <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
    <img src="${file_show}<shiro:principal property = "portrait"/>" class="layui-nav-img">
</blockquote>
<blockquote class="layui-elem-quote">
    notAuthenticated标签：用户未进行身份验证，即没有调用Subject.login进行登录,包括"记住我"也属于未进行身份验证
    <shiro:notAuthenticated>
        未身份验证(包括"记住我")
    </shiro:notAuthenticated>
</blockquote>
<blockquote class="layui-elem-quote">
    hasRole标签：如果当前Subject有角色将显示body体内的内容
    <shiro:hasRole name = "超级管理员">
        用户[<shiro:principal property = "nickname"/>]拥有角色admin
    </shiro:hasRole>
</blockquote>
<blockquote class="layui-elem-quote">
    hashPermission:如果当前Subject有权限将显示body体内容
    <shiro:hasPermission name = "/platform/list">
        用户[<shiro:principal property = "nickname"/>] 拥有权限/platform/list
    </shiro:hasPermission>
</blockquote>





</body>
</html>
