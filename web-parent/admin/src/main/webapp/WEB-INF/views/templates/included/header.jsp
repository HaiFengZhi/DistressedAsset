<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<div class="layui-header">
    <div class="layui-logo">在线商城 管理中心</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left" id="topMenu" lay-filter="test">
        <li class="layui-nav-item"><a href="javascript:;">控制台</a></li>
    </ul>
    <ul class="layui-nav layui-layout-right" lay-filter="test">
        <li class="layui-nav-item">
            <a href="javascript:;">
                <img src="${portrait}" class="layui-nav-img">
                【${nickname}】
            </a>
            <dl class="layui-nav-child user_right">
                <dd><a href="/logout">退出登陆</a></dd>
            </dl>
        </li>
    </ul>
</div>
<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree" id="menuChildren" lay-filter="test">
            <li class="layui-nav-item layui-nav-itemed">
                <a href="javascript:;">权限管理</a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;">系统权限</a></dd>
                    <dd><a href="javascript:;">系统角色</a></dd>
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
<script type="text/javascript">
    layui.use(['jquery', 'layer', 'element'], function(){
        //初始化Jquery和layer插件，导航的hover效果、二级菜单等功能，需要依赖element模块
        var $ = layui.$ ,layer = layui.layer,element = layui.element;
        //后面就跟你平时使用jQuery一样

        var firstIndex = 1;
        //初始化导航栏
        $.ajax({
            url: "/platform/getChildrenMenu?parentId=0",
            type : 'GET',
            success : function(data) {
                var html = "";
                $.each(data.data,function(index,item){
                    if (index === 0){
                        firstIndex = item.id;
                    }
                    html += "<li class=\"layui-nav-item\"><a  data=\""+item.id+"\" href=\"javascript:;\">"+item.name+"</a></li>" ;
                });
                $("#topMenu").html(html);
                //重新渲染HTML页面
                element.init();
            }
        });

        //根据根目录初始化左侧菜单
        function initMenuChildren(parentId) {
            $.ajax({
                url: "/platform/getChildrenMenu?parentId=" + parentId,
                type: 'GET',
                success: function (data) {
                    var html = "";
                    $.each(data.data, function (index, item) {
                        if (item.accessUrl == null || item.accessUrl.trim().length === 0) {
                            html += "<li class=\"layui-nav-item layui-nav-itemed\"><a href=\"javascript:void(0);\" >" + item.name + "</a>";
                        }else {
                            html += "<li class=\"layui-nav-item layui-nav-itemed\"><a href=\"javascript:;\" data-src=\"" + item.accessUrl + "\" target=\"contentShow\">" + item.name + "</a>";
                        }
                        if (item.children.length > 0) {
                            html += "<dl class=\"layui-nav-child\">";
                            $.each(item.children, function (index, next) {
                                html += "<dd><a href=\"javascript:;\" data-src=\"" + next.accessUrl + "\">" + next.name + "</a></dd>";
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

        //监听导航点击
        element.on('nav(test)', function (elem) {
            var parentId = elem.attr("data");
            //layer.msg(elem.text());
            if (parentId) {
                //初始化左侧菜单
                initMenuChildren(parentId);
            }

            //左侧菜单获取对应右侧内容，页面只刷新内容，不再刷新菜单
            var menuUrl = elem.attr("data-src");
            if (menuUrl) {
                //layer.msg(address);
                //获取右侧内容
                $("iframe").attr("src", menuUrl);
                //清空原内容
                $("#welcome").css("display", "none");
                // element.init();
                //以下代码是根据窗口高度在设置iframe的高度
                var frame = $("#contentShow");
                var frameheight = $(window).height();
                // console.log(frameheight);
                frame.css("height", frameheight - 115);
            }
        });

        //第一次初始化左侧菜单
        initMenuChildren(firstIndex);



    });

</script>
