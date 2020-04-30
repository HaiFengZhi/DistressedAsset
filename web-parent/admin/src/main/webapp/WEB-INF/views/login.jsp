<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--声明为HTML5，不然有部分样式无法解析--%>
<!DOCTYPE html>
<html class="loginHtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>后台管理登陆</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
<%--    <link rel="icon" href="/favicon.ico">--%>
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css" media="all">
    <link rel="stylesheet" type="text/css" href="/static/css/public.css" media="all">

</head>
<body class="loginBody">
    <form class="layui-form">
        <div class="login_face"><img src="/static/images/face.jpg" class="userAvatar"></div>
        <div class="layui-form-item input-item">
            <label for="userName">用户名</label>
            <input type="text" placeholder="请输入用户名" autocomplete="off" id="username" name="username" class="layui-input" lay-verify="required">
        </div>
        <div class="layui-form-item input-item">
            <label for="password">密码</label>
            <input type="password" placeholder="请输入密码" autocomplete="off" id="password" name="password" class="layui-input" lay-verify="required">
        </div>
        <div class="layui-form-item input-item" id="imgCode">
            <label for="code">验证码</label>
            <input type="hidden" name="randomPageId" value="${randomPageId}">
            <input type="text" placeholder="请输入验证码" autocomplete="off" maxlength="4" id="code" name="code" class="layui-input" lay-verify="checkVCode">
            <img id="codeImage" title="字母不区分大小写，如果看不清可点击图片更换一个" src="<%=request.getContextPath() %>/code?randomPageId=${randomPageId}">
        </div>
        <div class="layui-form-item input-item">
            <span class="layui-form-label" style="width: 56px; padding: 9px;">记住我：</span>
            <input type="checkbox" id="rememberMe" name="rememberMe" lay-skin="switch" lay-text="是|否" lay-filter="rememberMe" checked />
        </div>
        <div class="layui-form-item">
            <button id="loginBtn" class="layui-btn layui-block" lay-filter="login" lay-submit>登录</button>
        </div>
<%--        <div class="layui-form-item layui-row">--%>
<%--            <a href="javascript:;" class="seraph icon-qq layui-icon layui-icon-login-qq layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>--%>
<%--            <a href="javascript:;" class="seraph icon-wechat layui-icon layui-icon-login-wechat layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>--%>
<%--            <a href="javascript:;" class="seraph icon-sina layui-icon layui-icon-login-weibo layui-col-xs4 layui-col-sm4 layui-col-md4 layui-col-lg4"></a>--%>
<%--        </div>--%>
    </form>

    <script type="text/javascript" src="/static/layui/layui.js"></script>
<script>
    //JavaScript代码区域，这个模板是用来引用公共JS和样式的，但不需要其它模板渲染
    layui.use(['form','layer','jquery'], function(){
        var form = layui.form,
            layer = layui.layer;
            $ = layui.jquery;


        $(".loginBody .seraph").click(function(){
            layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
                time:5000
            });
        });

        $("#codeImage").click(function() {
            $("#codeImage").attr("src","<%=request.getContextPath() %>/code?randomPageId=${randomPageId}" + "&c=" + Math.random());
        });

        //自定义验证规则
        form.verify({
            checkVCode: function(value){
                var checkCode = $("#code").val();
                if (checkCode === "" || checkCode.length !== 4) return "图形验证码必须为四位！";
                var checkResult = null;
                $.ajax({
                    type: "Post",
                    url: "<%=request.getContextPath() %>/checkCode?checkCode=" + checkCode + "&randomPageId=${randomPageId}",
                    dataType: "json",
                    async: false,//改为同步请求，默认是异步处理的
                    success: function(data) {
                        if(data === 0){
                            // layer.msg("图形验证码输入错误，请重新输入。");
                            checkResult = "图形验证码输入错误，请重新输入！";
                        } else if (data === 2){
                            // layer.msg("图形验证码已失效，请重新获取！");
                            checkResult = "图形验证码已失效，请重新获取！";
                        }
                    }
                });
                if (checkResult!==null){
                    return checkResult;
                }
            }
        });

        //登录按钮
        form.on("submit(login)",function(data){
            // console.log(JSON.stringify(data.field));
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            $(this).text("登录中...").attr("disabled","disabled").addClass("layui-disabled");
            $.ajax({
                url:'/user/login',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    if (res.code === 1) {
                        window.location.href = "/welcome";
                    }else{
                        layer.msg(res.message);
                        $("#loginBtn").text("登录").removeAttr("disabled").removeClass("layui-disabled");
                    }
                },
                error:function (data) {
                    layer.msg(data.message);
                }
            }) ;
            return false;
        });

        //表单输入效果
        $(".loginBody .input-item").click(function(e){
            e.stopPropagation();
            $(this).addClass("layui-input-focus").find(".layui-input").focus();
        });
        var layInput = $(".loginBody .layui-form-item .layui-input");
        layInput.focus(function(){
            $(this).parent().addClass("layui-input-focus");
        });
        layInput.blur(function(){
            $(this).parent().removeClass("layui-input-focus");
            if($(this).val() !== ''){
                $(this).parent().addClass("layui-input-active");
            }else{
                $(this).parent().removeClass("layui-input-active");
            }
        });
    });
</script>
</body>
</html>
