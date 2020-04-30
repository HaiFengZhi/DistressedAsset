<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.url" var="file_url"></fmt:message>
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>后台用户信息</title>
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
</head>
<body>
<div class="layui-col-md10" style="padding: 15px;">
    <form class="layui-form" id="adminAddForm" lay-filter="adminAddForm">
        <div class="layui-form-item">
            <label class="layui-form-label">用户名：</label>
            <div class="layui-input-block">
                <input type="text" name="loginUsername" id="loginUsername" lay-verify="username" autocomplete="off" placeholder="请输入用户登陆名" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">用户昵称：</label>
            <div class="layui-input-block">
                <input type="text" name="nickname" id="nickname" lay-verify="required" autocomplete="off" placeholder="请输入用户昵称" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">密码：</label>
            <div class="layui-input-inline">
                <input type="password" name="password" id="password" ${adminUser!=null?'':'lay-verify="pass"'} ${adminUser!=null?"disabled":""} placeholder="请输入密码" autocomplete="off" class="layui-input ${adminUser!=null?"layui-disabled":""}">
            </div>
            <div class="layui-form-mid layui-word-aux">${adminUser!=null?"不能直接修改用户密码":"请填写6到12位密码"}</div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">头像：</label>
            <div class="layui-input-inline">
                <div class="layui-upload">
                    <button type="button" class="layui-btn layui-btn-sm" id="test1">上传头像</button>
                    <div class="layui-upload-list">
                        <img class="layui-upload-img ${adminUser==null&&adminUser.portrait==null?'layui-hide':''}"
                             id="demo1" width="100px" height="100px">
                        <input hidden name="portrait" id="portrait">
                        <p id="demoText"></p>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">绑定手机：</label>
                <div class="layui-input-inline">
                    <input type="tel" name="boundCellphone" lay-verify="phone" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">绑定邮箱：</label>
                <div class="layui-input-inline">
                    <input type="text" name="boundEmail" lay-verify="email" autocomplete="off" class="layui-input">
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">用户状态：</label>
                <div class="layui-input-inline">
                    <input type="checkbox" name="status" lay-skin="switch" lay-text="启用|禁用" lay-filter="status" checked />
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <input hidden name="id" id="id">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="addSubmit">提交</button>
                <button type="reset" class="layui-btn layui-btn-sm layui-btn-width-sm-80">重置</button>
                <button type="button" id="admin-btn-close" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80 layui-layer-close">关闭</button>
            </div>
        </div>

    </form>
</div>
<script type="text/javascript" src="/static/layui/layui.js"></script>
<script type="text/javascript">
    //引用外部插件
    layui.config({
        base: '/static/lib/layui_ext/'
    }).extend({
        dtree: 'dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
    }).use(['jquery', 'layer', 'form', 'upload', 'common'], function(){
        var $ = layui.$,
            layer = layui.layer,
            upload = layui.upload,
            common = layui.common,
            form = layui.form;

        //自定义验证规则
        form.verify({
            username: function(value){
                if(value.length < 4){
                    return '用户登陆名至少得4个字符啊';
                }
            }
            ,pass: [
                /^[\S]{6,12}$/
                ,'密码必须6到12位，且不能出现空格'
            ]
            // ,againPwd: function(value){
            //     var pwd = $("#password").val();
            //     if(value!==pwd){
            //         return '二次密码不一致，请重新输入！';
            //     }
            // }
        });

        //监听提交
        form.on('submit(addSubmit)', function(data){
            // console.log(JSON.stringify(data.field));
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            $.ajax({
                url:'/platform/saveAdmin',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    if (res.code === 0) {
                        // console.log("操作成功===>" + res);
                        layer.msg(res.msg);
                        if (res.data === "update"){
                            //修改完成后关闭当前弹窗
                            common.closePopDIV();
                        }else {
                            //重置当前表单
                            $("#adminAddForm")[0].reset();
                            $("#demo1").addClass("layui-hide");
                            form.render();
                        }
                    }else{
                        // console.log("操作失败。。。");
                        layer.msg(res.msg);
                        $("#adminAddForm")[0].reset();
                        form.render();
                    }
                },
                error:function (data) {
                    layer.msg(data.msg);
                }
            }) ;
        });

        //关闭当前弹窗
        layui.$('#admin-btn-close').on('click', function(){
            common.closePopDIV();
        });

        //初始化页面数据
        function initData() {
            if (${adminUser!=null}) {
                form.val("adminAddForm", {
                    "id": "${adminUser.id}"
                    , "loginUsername": "${adminUser.loginUsername}"
                    , "nickname": "${adminUser.nickname}"
                    , "portrait": "${adminUser.portrait}"
                    , "password": "${adminUser.password}"
                    , "boundCellphone": "${adminUser.boundCellphone}"
                    , "boundEmail": "${adminUser.boundEmail}"
                    , "status":${!empty adminUser?adminUser.status:true}
                });
                //初始化头像
                var portrait = '${adminUser.portrait}';
                if (portrait.trim().length > 0) {
                    $('#demo1').attr('src', "${file_show}${adminUser.portrait}");
                }else {
                    $("#demo1").addClass("layui-hide");
                }
            }
        }
        initData();

        //普通图片上传
        common.uploadImg("test1","${file_url}/file/upload",{"userId": $("#id").val(), "directory": "portrait", "description": "管理员头像"},
            "demo1","demoText","portrait");

    });
</script>
</body>
</html>
