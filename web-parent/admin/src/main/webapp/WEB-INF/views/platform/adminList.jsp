<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>用户权限管理</title>
    <script type="text/javascript" src="/static/lib/layui_ext/xm-select.js"></script>

</head>
<body>
<blockquote class="layui-elem-quote">
    <span class="layui-breadcrumb" lay-separator="|">
      <a href="#">控制台</a>
      <a href="#">权限管理</a>
      <a><cite>系统用户</cite></a>
    </span>
</blockquote>

<blockquote class="layui-elem-quote search-demo">

    <div class="layui-form-item">
        <form class="layui-form" >
            <div class="layui-inline">
                <label class="layui-form-label">用户名：</label>
                <div class="layui-inline">
                    <input class="layui-input" name="loginUsername" id="loginUsername" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">昵称：</label>
                <div class="layui-inline">
                    <input class="layui-input" name="nickname" id="nickname" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">绑定手机：</label>
                <div class="layui-inline">
                    <input class="layui-input" name="boundCellphone" id="boundCellphone" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">绑定邮箱：</label>
                <div class="layui-inline">
                    <input class="layui-input" name="boundEmail" id="boundEmail" autocomplete="off">
                </div>
            </div>
            <div class="layui-inline layui-inline-select">
                <label class="layui-form-label">用户状态：</label>
                <div class="layui-input-block">
                    <select name="userStatus" id="userStatus" lay-filter="" lay-search>
                        <option value="">选择状态</option>
                        <option value="1">启用</option>
                        <option value="0">禁用</option>
                    </select>
                </div>
            </div>

            <div class="layui-inline layui-inline-btn">
                <div class="layui-inline">
                    <a class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" id="adminUserSearchBtn" data-type="reload">搜索</a>
                    <button class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80" type="reset">重置</button>
                </div>
            </div>
        </form>
    </div>

</blockquote>

<table class="layui-hide" id="adminList" lay-filter="adminList"></table>
<%--头部菜单--%>
<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">
            <i class="layui-icon">&#xe654;</i>新增用户
        </button>
    </div>
</script>
<%--操作按钮组--%>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="editRole">分配角色</a>
    <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
    <%--需要精确控制到按钮层面的权限，可以添加Shiro标签控制--%>
    <shiro:hasPermission name = "/platform/generatePwd">
        <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="generate">重置密码</a>
    </shiro:hasPermission>

</script>
<%--启用禁用开关--%>
<script type="text/html" id="showOrTpl">
    <input type="checkbox" name="status" value="{{d.status}}" data-id="{{d.id}}" lay-skin="switch" lay-text="启用|禁用" lay-filter="status" {{ d.status == true ? 'checked' : '' }} />
</script>
<%--头像显示--%>
<script type="text/html" id="showPortrait">
    {{# if (d.portrait!=null && d.portrait.trim().length !== 0){ }}
        <img src="${file_show}{{d.portrait}}" class="layui-nav-img">
    {{# } }}
</script>
<%--角色菜单权限选择页面--%>
<div class="layui-col-md10" style="padding: 15px;display: none;" id="adminRoleDiv">
    <div class="layui-fluid">
        <div class="layui-form-item">
            <label class="layui-form-label">用户名：</label>
            <div class="layui-input-block">
                <input type="text" name="loginUsername" id="loginUsernameShow" autocomplete="off" class="layui-input" readonly="">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">昵称：</label>
            <div class="layui-input-block">
                <input type="text" name="nickname" id="nicknameShow" autocomplete="off" class="layui-input" readonly="">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色列表：</label>
            <div class="layui-input-block">
                <div id="demo1" class="xm-select-demo"></div>
                <input hidden name="roleIds" id="roleIds" value="0">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <input hidden name="adminId" id="adminId">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="editAdminRole">提交</button>
                <%--<button type="button" id="roleMenuBtn" class="layui-btn layui-btn-sm layui-btn-width-sm-80">赋值</button>--%>
                <%--layui-layer-close 通过该样式可以直接触发弹窗关闭事件--%>
                <button type="button" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80 layui-layer-close">关闭</button>
            </div>
        </div>
    </div>
</div>
<script>
    layui.config({
        base: '/static/lib/layui_ext/'
    }).extend({
        dtree: 'dtree/dtree',
        eleTree: 'eleTree/eleTree'
    }).use(['table','layer','jquery','form', 'dtree', 'eleTree', 'common'], function(){
        var table = layui.table,
            layer=layui.layer,
            form = layui.form,
            dtree = layui.dtree,
            eleTree = layui.eleTree,
            common = layui.common,
            $ = layui.$;

        table.render({
            elem: '#adminList'
            ,url:'/platform/getAdminList'
            ,method:"POST"
            ,cellMinWidth: 100
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }]
            ,title: '系统用户列表'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'}
                ,{title: '编号', width: 100, templet:"<div>{{d.LAY_INDEX}}</div>"}
                ,{field:'loginUsername', title:'用户名'}
                ,{field:'nickname', title:'昵称'}
                ,{field:'portrait', title:'头像', align:"center",templet:"#showPortrait", width:100}
                ,{field:'boundCellphone', title:'绑定手机', hide:false}
                ,{field:'boundEmail', title:'绑定邮箱', hide:true}
                ,{field:'createTime', title:'创建时间' , templet:function (d) {
                        return common.showTime2String(d.createTime,"yyyy-MM-dd HH:mm:ss");
                    }, width:170 , align:"center"}
                ,{field:'lastLoginTime', title:'上次登陆时间' , templet:function (d) {
                        return common.showTime2String(d.lastLoginTime,"yyyy-MM-dd HH:mm:ss");
                    }, width:170 , align:"center"}
                ,{field:'status', title:'状态', width:100, sort: true, templet:'#showOrTpl', align:"center"}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:250}
            ]]
            ,done:function () {
                $('th').css({'background-color': 'lightgray'  ,'font-weight':'bold' })
            }
            ,page: true
            ,id: 'adminTable'//用来刷新表格的标识
        });

        //先渲染多选
        var demo1 = xmSelect.render({
            el: '#demo1',
            data: []
        });

        initRoleSelect();
        function initRoleSelect() {
            $.ajax({
                url: "/platform/getRoleSelectList",
                type : 'POST',
                success : function(data) {
                    // layer.msg(data.msg);
                    // 更新下拉列表数据
                    demo1.update({
                        data: data.data,
                        autoRow: true
                    });
                }
            });
        }

        //头工具栏事件
        table.on('toolbar(adminList)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            switch(obj.event){
                //自定义头工具栏右侧图标 - 提示
                case 'LAYTABLE_TIPS':
                    layer.alert('这是一个后台系统用户列表！');
                    break;
                case 'add':
                    //弹窗
                    /* 再弹出添加界面 */
                    common.layerToPage("添加用户", "/platform/toAdmin", true, true, active);
                    break;
            }
        });

        //监听操作栏工具事件
        table.on('tool(adminList)', function(obj){
            var data = obj.data;
            if(obj.event === 'del'){
                common.deleteConfirm("您确定要删除当前用户【" + data.nickname + "】吗？删除后系统不可撤回！",
                    "/platform/deleteAdmin?id=" + data.id ,obj);
            }else if (obj.event === 'edit'){
                //弹窗显示用户详情并编辑
                common.layerToPage("编辑用户", "/platform/toAdmin?id=" + data.id, true, true, active);
            }else if (obj.event === 'editRole'){
                // layer.msg("为当前用户分配角色！");
                layer.open({
                    type:1,
                    title:"分配用户角色",
                    skin: 'demo-class',
                    area:["50%","50%"],
                    offset: '100px',//只定义top坐标，水平保持居中
                    content:$("#adminRoleDiv"),
                    success: function(layero, index) {
                        //自动根据弹窗内容拉伸高度
                        layer.iframeAuto(index);
                        //为弹窗页面赋值
                        $("#adminId").val(data.id);
                        $("#loginUsernameShow").val(data.loginUsername);
                        $("#nicknameShow").val(data.nickname);
                        //获取当前角色下的权限关联数据
                        $.ajax({
                            url:'/platform/getAdminRole',
                            method:'post',
                            data:{adminId:data.id},
                            dataType:'JSON',
                            success:function(res){
                                if (res.code === 0) {
                                    var arr = res.data;
                                    if (arr != null){
                                        //初始化下拉列表
                                        demo1.setValue(arr);
                                    }

                                }else{
                                    layer.msg(res.msg);
                                }
                            },
                            error:function (data) {
                                layer.msg(data.msg);
                            }
                        }) ;

                    },
                    end : function() {
                        layer.closeAll();
                        active.reload();
                        $("#adminId").val();
                        $("#loginUsernameShow").val();
                        $("#nicknameShow").val();
                        //清空所有选项
                        demo1.setValue([]);
                    }
                });
                /* 渲染表单 */
                form.render();
            }else if (obj.event === 'generate'){
                // layer.msg("为当前用户重置密码！");
                //prompt层
                layer.prompt({title: '输入新的密码，并确认', formType: 1}, function(pass, index){
                    layer.close(index);
                    layer.prompt({title: '请重新输入密码，并确认', formType: 1}, function(text, index){
                        if (pass === text){
                            //layer.msg("验证通过，用户密码已重新初始化！");
                            $.ajax({
                                url:'/platform/generatePwd',
                                method:'post',
                                data:{adminId:data.id,password:text},
                                dataType:'JSON',
                                async: false,//改为同步请求，默认是异步处理的
                                success:function(res){
                                    layer.msg(res.msg);
                                },
                                error:function (data) {
                                    layer.msg(data.msg);
                                }
                            });
                        }else {
                            layer.msg("两次密码输入不一致，请重新初始化！");
                        }
                        layer.close(index);
                    });
                });
            }

        });

        var active = {
            reload: function(){
                var loginUsername = $('#loginUsername');
                var nickname = $('#nickname');
                var boundCellphone = $('#boundCellphone');
                var boundEmail = $('#boundEmail');
                var userStatus = $('#userStatus');
                //执行重载
                table.reload('adminTable', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        loginUsername: loginUsername.val()
                        ,nickname: nickname.val()
                        ,boundCellphone: boundCellphone.val()
                        ,boundEmail: boundEmail.val()
                        ,status: userStatus.val()
                    }
                }, 'data');
            }
        };

        //搜索按钮监听事件
        $('#adminUserSearchBtn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //监听指定开关
        form.on('switch(status)', function(data){
            var checked = data.elem.checked;
            var tipMsg = "您确定要禁用当前用户吗？禁用后当前用户将不能再去访问后台管理中心，可在此重新开启！";
            if (this.checked){
                tipMsg = "您确定要启用当前用户吗？启用后当前用户将拥有后台管理中心的角色权限，可在此重新关闭！";
            }
            var adminID = data.elem.attributes['data-id'].nodeValue;
            console.log("adminID===>" + adminID);
            layer.confirm(tipMsg, {
                icon: 3,
                //墨绿风格【layui-layer-molv】深蓝风格【layui-layer-lan】
                skin: 'layui-layer-lan',
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/platform/openOrCloseAdmin",
                    type : 'POST',
                    data:{id:adminID,status:checked},
                    success : function(data) {
                        layer.msg(data.msg);
                    }
                });
            },function(){
                data.elem.checked=!checked;
                form.render();
            });


        });

        //分配角色权限提交
        form.on('submit(editAdminRole)', function(data){
            var selectArr = demo1.getValue('valueStr');
            if (JSON.stringify(selectArr, null, 2).length < 3) {
                layer.msg("请至少选择一项角色再提交！");
                return;
            }
            var adminId = $("#adminId").val();
            $.ajax({
                url:'/platform/saveAdminRoles',
                method:'post',
                data:{adminId:adminId,roleIds:selectArr},
                dataType:'JSON',
                success:function(res){
                    if (res.code === 0) {
                        layer.msg(res.msg);
                    }else{
                        layer.msg(res.msg);
                    }
                },
                error:function (data) {
                    layer.msg(data.msg);
                }
            }) ;
        });


    });
</script>

</body>
</html>
