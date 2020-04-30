<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/27 0027
  Time: 下午 5:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权限资源-新增</title>
    <link rel="stylesheet" type="text/css" href="/static/layui/css/layui.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <link rel="stylesheet" href="/static/lib/layui_ext/dtree/dtree.css">
    <link rel="stylesheet" href="/static/lib/layui_ext/dtree/font/dtreefont.css">
</head>
<body>
<div class="layui-col-md10" style="padding: 15px;">
    <form class="layui-form" id="addMenuForm" lay-filter="addMenuForm">
        <div class="layui-form-item">
            <label class="layui-form-label">权限名称：</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="name" autocomplete="off" placeholder="请输入权限名称" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">权限地址：</label>
            <div class="layui-input-block">
                <input type="text" name="accessUrl" id="accessUrl" autocomplete="off" placeholder="请输入权限地址" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">父节点：</label>
            <div class="layui-input-block">
                <input hidden name="parentId" id="parentId" value="0">
                <ul id="selTree1" class="dtree" data-id="0"></ul>
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">权限描述：</label>
            <div class="layui-input-block">
                <textarea name="description" id="description" placeholder="请输入权限描述内容" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">权限等级：</label>
            <div class="layui-input-block">
                <input type="radio" name="level" value="0" title="模块" checked>
                <input type="radio" name="level" value="1" title="菜单">
                <input type="radio" name="level" value="2" title="按钮">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">权限排序：</label>
            <div class="layui-input-block">
                <input type="text" name="sequence" id="sequence" lay-verify="number" autocomplete="off" placeholder="请输入权限排序值" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否显示：</label>
            <div class="layui-input-block">
                <input type="radio" name="showOr" value="1" title="是" checked>
                <input type="radio" name="showOr" value="0" title="否">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否主菜单：</label>
            <div class="layui-input-block">
                <input type="radio" name="mainPermissionsOr" value="1" title="是" checked>
                <input type="radio" name="mainPermissionsOr" value="0" title="否">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input hidden name="id" id="id">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="addSubmit">提交</button>
                <button type="reset" class="layui-btn layui-btn-sm layui-btn-width-sm-80">重置</button>
                <button type="button" id="LAY-component-form-getval" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80">关闭</button>
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
    }).use(['jquery', 'layer', 'form', 'dtree', 'common'], function(){
        //初始化Jquery和layer插件，导航的hover效果、二级菜单等功能，需要依赖element模块
        var $ = layui.$,
            layer = layui.layer,
            dtree = layui.dtree,
            common = layui.common,
            form = layui.form;

        // 初始化树
        dtree.renderSelect({
            elem: "#selTree1",
            width: "100%", // 可以在这里指定树的宽度来填满div
            //selectInitVal:"5",//单独用selectInitVal设置默认值点击下拉时会被清掉，建议使用下面的done方式
            url: "/platform/getMenuListByParentId"
            ,done: function(){
                //设置默认值
                if (${baseResource!=null}){
                    dtree.dataInit("selTree1", "${baseResource.parentId}");
                    // 也可以在这里指定，第二个参数如果不填，则会自动返显当前选中的数据
                    var selectParam = dtree.selectVal("selTree1");
                }

            }
        });

        // 绑定节点点击
        dtree.on("node('selTree1')" ,function(obj){
            //layer.msg(JSON.stringify(obj.param.nodeId));
            //选择后给隐藏框赋值
            $("#parentId").val(obj.param.nodeId);
        });

        //自定义验证规则
        form.verify({
            name: function(value){
                if(value.length < 2){
                    return '标题至少得2个字符啊';
                }
            }
        });

        //监听提交
        form.on('submit(addSubmit)', function(data){
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            $.ajax({
                url:'/platform/saveMenu',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    if (res.code === 0) {
                        console.log("操作成功===>" + res);
                        layer.msg(res.msg);
                        if (res.data === "update"){
                            //修改完成后关闭当前弹窗
                            common.closePopDIV();
                        }else {
                            //重置当前表单
                            $("#addMenuForm")[0].reset();
                            layui.form.render();
                        }
                    }else{
                        console.log("操作失败。。。");
                        layer.msg(res.msg);
                        $("#addMenuForm")[0].reset();
                        layui.form.render();
                    }
                },
                error:function (data) {
                    layer.msg(data.msg);
                }
            }) ;
        });

        //关闭当前弹窗
        layui.$('#LAY-component-form-getval').on('click', function(){
            common.closePopDIV();
        });

        //给当前表单元素赋值
        function initData() {
            if (${baseResource!=null}) {
                form.val("addMenuForm", {
                    "id": "${baseResource.id}"
                    , "name": "${baseResource.name}"
                    , "accessUrl": "${baseResource.accessUrl}"
                    , "parentId": "${baseResource.parentId}"
                    , "description": "${baseResource.description}"
                    , "level": "${baseResource.level}"
                    , "sequence": "${baseResource.sequence}"
                    , "showOr":${!empty baseResource?baseResource.showOr:true}
                    , "mainPermissionsOr":${!empty baseResource? baseResource.mainPermissionsOr:true}
                });
            }
        }
        initData();

    });

</script>

</body>
</html>
