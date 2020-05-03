<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property='title' default="在线商城"/></title>
    <link rel="icon" href="favicon.ico">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/menu.css">
    <script type="text/javascript" src="/static/layui/layui.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <sitemesh:write property='head'/>
</head>
<body>
    <%--引用头部文件--%>
    <%@include file="included/header.jsp" %>
    <%--中心页内容 --%>
    <div class="content">
        <!--导航 Start-->
        <div class="menu">
            <div class="all-sort"><h2><a href="/productList">全部商品分类</a></h2></div>
            <div class="nav">
                <ul class="clearfix">
                    <li><a href="/index" class="index">首页</a></li>
                    <li><a href="/productList" class="productList">所有商品</a></li>
                    <li><a href="/buytoday" class="buytoday">今日团购</a></li>
                    <li><a href="/information" class="information">资讯列表</a></li>
                    <li><a href="/about" class="about">关于我们</a></li>
                </ul>
            </div>
        </div>
        <sitemesh:write property='body'/>
    </div>

    <%--引用底部文件--%>
    <%@include file="included/footer.jsp" %>
</body>
</html>