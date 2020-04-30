<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title><sitemesh:write property='title' default="后台管理中心"/></title>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css">
<%--    <link rel="stylesheet" type="text/css" href="/static/css/public.css" media="all">--%>
    <script type="text/javascript" src="/static/layui/layui.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <sitemesh:write property='head'/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <%--引用头部文件--%>
    <%@include file="included/header.jsp" %>

    <div class="layui-body">
        <div style="padding: 15px;" id="welcome">
            <%--中心页内容 --%>
            <sitemesh:write property='body'/>
        </div>
        <iframe frameborder="0" scrolling="yes" style="width: 100%;height: auto;" src="" id="contentShow"></iframe>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © layui.com - 底部固定区域
    </div>
</div>

<script>
    //JavaScript代码区域
    layui.use('element', function(){
        var element = layui.element;

    });
</script>
</body>
</html>
