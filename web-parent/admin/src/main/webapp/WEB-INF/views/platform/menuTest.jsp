<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>后台管理中心</title>
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css">
    <script type="text/javascript" src="/static/layui/layui.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <style>
        .layui-layout-left-350 {
            position: absolute !important;
            left: 350px;
            top: 0
        }
    </style>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <%--引用头部文件--%>
    <div class="layui-header">
        <div class="layui-logo">在线商城 管理中心</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <ul class="layui-nav layui-layout-left" id="topMenu" lay-filter="test">
            <li class="layui-nav-item"><a  data="1" href="javascript:;">控制台</a></li>
        </ul>
        <ul class="layui-nav layui-layout-right" lay-filter="test">
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
                    贤心
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;">个人信息</a></dd>
                    <dd><a href="javascript:;">密码设置</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="">退了</a></li>
        </ul>
    </div>
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" id="menuChildren" lay-filter="test">
                <li class="layui-nav-item layui-nav-itemed">
                    <a href="javascript:;">权限管理</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;" data-src="/platform/list" target="_top" >系统权限</a></dd>
                        <dd><a href="javascript:;" data-src="/platform/roleList">系统角色</a></dd>
                        <dd><a href="javascript:;">用户权限</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item">
                    <a href="javascript:;">基本设置</a>
                    <dl class="layui-nav-child">
                        <dd><a href="javascript:;">个人信息</a></dd>
                        <dd><a href="javascript:;">头像设置</a></dd>
                        <dd><a href="javascript:;">密码设置</a></dd>
                    </dl>
                </li>
            </ul>
        </div>
    </div>


    <div class="layui-body">
        <div style="padding: 15px;">
            <%--中心页内容 --%>
            <blockquote class="layui-elem-quote">
                <span class="layui-breadcrumb" lay-separator="|">
                  <a><cite>这就是一个测试页面，用来实验各种小插件的……</cite></a>
                </span>
            </blockquote>
        </div>
        <iframe frameborder="0" scrolling="yes" style="width: 100%" src="" id="aa">
        </iframe>
    </div>

    <div class="layui-footer">
        <!-- 底部固定区域 -->
        © layui.com - 底部固定区域
    </div>
</div>

<script>
    //JavaScript代码区域
    //引用外部插件
    layui.config({
        base: '/static/lib/layui_ext/'
    }).extend({
        treetable: 'treetable-lay/treetable-2.0'
    }).use(['jquery', 'layer', 'element','table', 'treetable','form'], function(){
        var $ = layui.$ ,
            layer = layui.layer,
            element = layui.element,
            table = layui.table,
            form = layui.form,
            treetable = layui.treetable;
        //后面就跟你平时使用jQuery一样
        //初始化导航栏
        $.ajax({
            url: "/platform/getChildrenMenu?parentId=0",
            type : 'GET',
            success : function(data) {
                var html = "";
                $.each(data.data,function(index,item){
                    html += "<li class=\"layui-nav-item\"><a  data=\""+item.id+"\" href=\"javascript:;\">"+item.name+"</a></li>" ;
                });
                console.log(html);
                $("#topMenu").html(html);
                //重新渲染HTML页面
                element.init();
            }
        });

        //监听导航点击
        element.on('nav(test)', function(elem){
            var parentId = elem.attr("data");
            //layer.msg(elem.text());
            if (parentId){
                $.ajax({
                    url: "/platform/getChildrenMenu?parentId=" + parentId,
                    type : 'GET',
                    success : function(data) {
                        var html = "";
                        $.each(data.data,function(index,item){
                            html += "<li class=\"layui-nav-item layui-nav-itemed\"><a href=\"javascript:;\">"+item.name+"</a>" ;
                            if (item.children.length>0){
                                html += "<dl class=\"layui-nav-child\">";
                                $.each(item.children,function(index,next){
                                    html += "<dd><a href=\""+next.accessUrl+"\">"+next.name+"</a></dd>";
                                });
                                html += "</dl>";
                            }
                            html += "</li>";
                        });
                        $("#menuChildren").html(html);
                        //重新渲染HTML页面
                        element.init();
                    }
                });
            }

        });

        //获取src值
        $(".layui-nav-tree a").on("click",function(){
            var address =$(this).attr("data-src");
            layer.msg(address);
            $("iframe").attr("src",address);
            //一下代码是根据窗口高度在设置iframe的高度
            var frame = $("#aa");

            var frameheight = $(window).height();
            console.log(frameheight);
            frame.css("height",frameheight);
        });



    });
</script>
</body>
</html>