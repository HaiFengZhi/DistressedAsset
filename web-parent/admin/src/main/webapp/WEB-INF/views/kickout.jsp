<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>被踢出</title>
</head>
<body>
您的账号在另一台设备上登录,如非本人操作，请立即修改密码！
<input type="button" id="login" class="layui-btn layui-btn-sm layui-btn-width-sm-80" value="重新登录"/>
<script type="text/javascript">
    layui.use(['jquery', 'layer'], function () {
        var $ = layui.$,
            layer = layui.layer;
        $("#login").on('click', function () {
            window.parent.location.href = "/login";
        });

    });

</script>
</body>

</html>