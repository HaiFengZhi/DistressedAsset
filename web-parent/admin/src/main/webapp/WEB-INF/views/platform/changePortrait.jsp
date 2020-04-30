<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:bundle basename="application">
    <fmt:message key="distressed.asset.file.manager.url" var="file_url"></fmt:message>
    <fmt:message key="distressed.asset.file.manager.show" var="file_show"></fmt:message>
</fmt:bundle>
<html>
<head>
    <title>修改头像</title>
    <link rel="stylesheet" type="text/css" href="/static/css/public.css" media="all">
</head>
<body>
<blockquote class="layui-elem-quote">
    头像设置
</blockquote>

<div class="layui-col-md4 layui-col-xs12 user_right avatar-add">
    <div class="layui-upload-list">
        <img class="layui-upload-img layui-circle userFaceBtn userAvatar" src="${portrait}" id="userFace">
        <input hidden name="portrait" id="portrait">
        <input hidden name="id" id="id" value="${user.id}">
    </div>
    <br/>
    <button type="button" id="userFaceBtn" class="layui-btn layui-btn-primary"><i class="layui-icon">&#xe67c;</i> 选择头像</button>
    <p>建议尺寸168*168，支持jpg、png、gif，最大不能超过50KB</p>
    <p id="demoText"></p>
</div>
<script type="text/javascript">
    layui.use(['jquery', 'layer', 'form', 'upload'],function(){
        var $ = layui.$,
            layer = layui.layer,
            upload = layui.upload,
            form = layui.form;
        //普通图片上传
        var uploadInst = upload.render({
            elem: '#userFaceBtn'
            , url: '${file_url}/file/upload'
            , size: 1024 //限制文件大小，单位 KB
            , data: {"userId": $("#id").val(), "directory": "portrait", "description": "管理员修改头像"}
            , before: function (obj) {
                //预读本地文件示例，不支持ie8
                obj.preview(function (index, file, result) {
                    var demo1 = $("#userFace");
                    demo1.removeClass("layui-hide");
                    demo1.attr('src', result); //图片链接（base64）
                });
            }
            , done: function (res) {
                //如果上传失败
                if (res.code === 1) {
                    //清空错误提示
                    var demoText = $('#demoText');
                    demoText.html("");
                    //将头像路径保存下来
                    $.ajax({
                        url:'/admin/setPortrait',
                        method:'post',
                        data:{"portrait": res.data.uuid},
                        dataType:'JSON',
                        success:function(res){
                            return layer.msg(res.message);
                        },
                        error:function (data) {
                            return layer.msg(data.message);
                        }
                    }) ;
                }
                //上传成功
                // console.log(res);
            }
            , error: function () {
                //演示失败状态，并实现重传
                var demoText = $('#demoText');
                demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                demoText.find('.demo-reload').on('click', function () {
                    uploadInst.upload();
                });
            }
        });

    });
</script>
</body>
</html>
