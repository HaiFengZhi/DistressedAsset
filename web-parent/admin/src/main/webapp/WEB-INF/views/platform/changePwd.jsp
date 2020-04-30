<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>密码设置</title>
</head>
<body>
<blockquote class="layui-elem-quote">
    密码设置
</blockquote>
<form class="layui-form layui-form-pane layui-row changePwd">
    <div class="layui-col-xs12 layui-col-sm6 layui-col-md6">
        <div class="layui-form-item">
            <label class="layui-form-label">用户名</label>
            <div class="layui-input-block">
                <input type="text" value="${user.loginUsername}" disabled class="layui-input layui-disabled">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">旧密码</label>
            <div class="layui-input-block">
                <input type="password" value="" placeholder="请输入旧密码" lay-verify="required|oldPwd" class="layui-input pwd">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">新密码</label>
            <div class="layui-input-block">
                <input type="password" value="" placeholder="请输入新密码" lay-verify="required|newPwd" id="oldPwd" class="layui-input pwd">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">确认密码</label>
            <div class="layui-input-block">
                <input type="password" value="" placeholder="请输入确认密码" lay-verify="required|confirmPwd" name="changePwd" class="layui-input pwd">
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="changePwd">立即修改</button>
                <button type="reset" class="layui-btn layui-btn-sm layui-btn-width-sm-80">重置</button>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    layui.use(['jquery', 'layer', 'form'],function(){
        var $ = layui.$,
            layer = layui.layer,
            form = layui.form;

        //添加验证规则
        form.verify({
            oldPwd : function(value, item){
                var isFlag = false;
                $.ajax({
                    type: "Post",
                    url: "<%=request.getContextPath() %>/checkPwd?oldPwd=" + value ,
                    dataType: "json",
                    async: false,//改为同步请求，默认是异步处理的
                    success: function(data) {
                        isFlag = data.data;
                    }
                });
                if (!isFlag){
                    return "旧密码错误，请重新输入！";
                }
            },
            newPwd : function(value, item){
                if(value.length < 6){
                    return "密码长度不能小于6位";
                }
            },
            confirmPwd : function(value, item){
                if(!new RegExp($("#oldPwd").val()).test(value)){
                    return "两次输入密码不一致，请重新输入！";
                }
            }
        });

        //提交修改
        form.on("submit(changePwd)",function(data){
            // console.log(JSON.stringify(data.field));
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            $.ajax({
                url:'/admin/setAdminPwd',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    if (res.code === 1) {
                        layer.open({
                            type: 1
                            ,id: 'layerChangePwdTip' //防止重复弹出
                            ,content: '<div style="padding: 20px 100px;">'+ res.message +'</div>'
                            ,btn: '退出登陆'
                            ,btnAlign: 'c' //按钮居中
                            ,closeBtn: 0   //不显示关闭按钮，强行退出登陆
                            ,yes: function(){
                                window.parent.location.href = "/logout";
                            }
                        });
                    }else{
                        layer.msg(res.message);
                    }
                },
                error:function (data) {
                    layer.msg(data.message);
                }
            }) ;
            return false;
        });

    });


</script>
</body>
</html>
