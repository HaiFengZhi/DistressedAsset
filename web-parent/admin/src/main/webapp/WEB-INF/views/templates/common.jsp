<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--声明为HTML5，不然有部分样式无法解析--%>
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
<body>
<div style="padding: 15px;" id="contentShow">
    <sitemesh:write property='body'/>
</div>

<script>
    //JavaScript代码区域，这个模板是用来引用公共JS和样式的，但不需要其它模板渲染
    layui.use('element', function(){
        var element = layui.element;

    });
</script>

</body>
</html>
