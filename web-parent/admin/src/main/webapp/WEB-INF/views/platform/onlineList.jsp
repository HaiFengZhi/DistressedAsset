<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>在线管理员列表</title>
</head>
<body>
<blockquote class="layui-elem-quote">
    系统当前在线管理员列表，提供在线踢人功能！
</blockquote>

<table class="layui-hide" id="adminList" lay-filter="adminList"></table>

<%--操作按钮组--%>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="kickout">在线踢人</a>
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="refresh">刷新权限</a>
</script>
<%--头像显示--%>
<script type="text/html" id="showPortrait">
    {{# if (d.portrait!=null && d.portrait.trim().length !== 0){ }}
    <img src="${file_show}{{d.portrait}}" class="layui-nav-img">
    {{# } }}
</script>

<script type="text/javascript">
    layui.use(['table','layer','jquery','form'],function(){
        var table = layui.table,
            layer=layui.layer,
            form = layui.form,
            $ = layui.$;

        table.render({
            elem: '#adminList'
            ,url:'/admin/showList'
            ,method:"POST"
            ,cellMinWidth: 100
            ,title: '在线管理员列表'
            ,cols: [[
                // {type: 'checkbox', fixed: 'left'}
                {title: '编号', width: 100, templet:"<div>{{d.LAY_INDEX}}</div>"}
                ,{field:'loginUsername', title:'用户名'}
                ,{field:'nickname', title:'昵称'}
                ,{field:'portrait', title:'头像', align:"center",templet:"#showPortrait"}
                ,{field:'boundCellphone', title:'绑定手机'}
                ,{field:'createTime', title:'创建时间' , templet:function (d) {
                        return layui.util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
                    }, width:200 , align:"center"}
                ,{field:'lastLoginTime', title:'上次登陆时间' , templet:function (d) {
                        return d.lastLoginTime!=null? layui.util.toDateString(d.lastLoginTime, "yyyy-MM-dd HH:mm:ss"):"-";
                    }, width:200 , align:"center"}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:250}
            ]]
            ,done:function () {
                $('th').css({'background-color': 'lightgray'  ,'font-weight':'bold' })
            }
            ,page: true
        });

        //监听操作栏工具事件
        table.on('tool(adminList)', function(obj){
            var data = obj.data;
            if(obj.event === 'kickout'){
                layer.confirm("您确定要踢出当前用户【" + data.nickname + "】吗？踢出登陆后当前用户需要重新登陆！", {
                        icon: 3,
                        skin: 'layui-layer-lan'},
                    function(index){
                        $.ajax({
                            url: "/admin/kickoutAdmin",
                            type : 'POST',
                            data:{adminId:data.id,remove:true},
                            success : function(data) {
                                layer.msg("当前用户已被踢出登陆状态！");
                            }
                        });
                        obj.del();
                        layer.close(index);
                    });
            }else if (obj.event === 'refresh'){
                layer.confirm("您确定要刷新当前用户【" + data.nickname + "】的权限列表吗？", {
                        icon: 3,
                        skin: 'layui-layer-lan'},
                    function(index){
                        $.ajax({
                            url: "/admin/kickoutAdmin",
                            type : 'POST',
                            data:{adminId:data.id,remove:false},
                            success : function(data) {
                                layer.msg("当前用户已权限已刷新！");
                            }
                        });
                        layer.close(index);
                    });
            }
        });



    });

</script>

</body>
</html>
