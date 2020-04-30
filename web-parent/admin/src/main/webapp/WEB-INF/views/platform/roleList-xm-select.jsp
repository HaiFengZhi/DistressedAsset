<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>角色列表</title>
    <link rel="stylesheet" href="/static/lib/layui_ext/dtree/dtree.css">
    <link rel="stylesheet" href="/static/lib/layui_ext/dtree/font/dtreefont.css">
    <link rel="stylesheet" href="/static/lib/layui_ext/eleTree/eleTree.css">
    <script type="text/javascript" src="/static/lib/layui_ext/xm-select.js"></script>
    <style>
        .layui-layout-left-150 {
            position: absolute !important;
            left: 150px;
            top: 0
        }
    </style>
</head>
<body>
<blockquote class="layui-elem-quote">
    <span class="layui-breadcrumb" lay-separator="|">
      <a href="#">控制台</a>
      <a href="#">用户权限管理</a>
      <a><cite>系统角色</cite></a>
    </span>
</blockquote>

<table class="layui-hide" id="roleList" lay-filter="roleList"></table>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-xs layui-btn-normal" lay-event="editRole">分配权限</a>
    <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del">删除</a>
</script>

<script type="text/html" id="showOrTpl">
    <input type="checkbox" name="status" value="{{d.status}}" data-id="{{d.id}}" lay-skin="switch" lay-text="是|否" lay-filter="status" {{ d.status == true ? 'checked' : '' }} />
</script>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">
            <i class="layui-icon">&#xe654;</i>新增角色
        </button>
    </div>
    <div class="layui-field-box layui-layout-left-150">
        <label>角色名称：</label>
        <div class="layui-inline">
            <input class="layui-input" style="height: 30px;" name="key-search" id="key-search" autocomplete="off">
        </div>
        <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-event="reload">搜索</button>
    </div>
</script>

<div class="layui-col-md10" style="padding: 15px;display: none;" id="roleDiv">
    <form class="layui-form" id="roleForm" lay-filter="roleForm">
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称：</label>
            <div class="layui-input-block">
                <input type="text" name="name" id="name" lay-verify="name" autocomplete="off" placeholder="请输入权限名称" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item layui-form-text">
            <label class="layui-form-label">角色描述：</label>
            <div class="layui-input-block">
                <textarea name="description" id="description" placeholder="请输入权限描述内容" class="layui-textarea"></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">是否可用：</label>
            <div class="layui-input-block">
                <input type="radio" name="status" value="1" title="是" checked>
                <input type="radio" name="status" value="0" title="否">
            </div>
        </div>

        <div class="layui-form-item">
            <div class="layui-input-block">
                <input hidden name="id" id="id">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="addSubmit">提交</button>
                <button type="reset" class="layui-btn layui-btn-sm layui-btn-width-sm-80">重置</button>
                <%--layui-layer-close 通过该样式可以直接触发弹窗关闭事件--%>
                <button type="button" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80 layui-layer-close">关闭</button>
            </div>
        </div>
    </form>
</div>

<%--角色菜单权限选择页面--%>
<div class="layui-col-md10" style="padding: 15px;display: none;" id="roleMenuDiv">
    <div class="layui-fluid">
        <div class="layui-form-item">
            <label class="layui-form-label">角色名称：</label>
            <div class="layui-input-block">
                <input type="text" name="roleName" id="roleName" autocomplete="off" class="layui-input" readonly="">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">角色描述：</label>
            <div class="layui-input-block">
                <textarea name="roleDesc" id="roleDesc" placeholder="请输入权限描述内容" class="layui-textarea" readonly=""></textarea>
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">权限列表：</label>
            <div class="layui-input-block">
                <div id="demo1" class="xm-select-demo"></div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-input-block">
                <input hidden name="roleId" id="roleId">
                <button type="button" class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-submit lay-filter="editRole">提交</button>
                <%--<button type="button" id="roleMenuBtn" class="layui-btn layui-btn-sm layui-btn-width-sm-80">赋值</button>--%>
                <%--layui-layer-close 通过该样式可以直接触发弹窗关闭事件--%>
                <button type="button" class="layui-btn layui-btn-primary layui-btn-sm layui-btn-width-sm-80 layui-layer-close">关闭</button>
            </div>
        </div>
    </div>
</div>

<script>
    layui.extend({
        dtree: '/static/lib/layui_ext/dtree/dtree',
        eleTree: '/static/lib/layui_ext/eleTree/eleTree'
    }).use(['table','layer','jquery','form', 'dtree', 'eleTree'], function(){
        var table = layui.table,
            layer=layui.layer,
            form = layui.form,
            dtree = layui.dtree,
            eleTree = layui.eleTree,
            $ = layui.$;

        table.render({
            elem: '#roleList'
            ,url:'/platform/getRoleList'
            ,method:"POST"
            ,cellMinWidth: 100
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }]
            ,title: '系统角色列表'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'}
                ,{field:'id', title:'ID', width:80, fixed: 'left', unresize: true, sort: true}
                ,{field:'name', title:'角色名称', width:200}
                ,{field:'description', title:'角色描述'}
                ,{field:'createTime', title:'变更时间' , templet:function (d) {
                        return layui.util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss");
                    }, hide:true, width:200 }
                ,{field:'status', title:'是否生效', width:100, sort: true, templet:'#showOrTpl', align:"center"}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:200}
            ]]
            ,page: true
            ,id: 'roleTable'//用来刷新表格的标识
        });

        //先渲染多选
        var demo1 = xmSelect.render({
            el: '#demo1',
            model: {
                //删除的级联容易出问题，暂时屏蔽
                label: {
                    type: 'block',
                    block: {
                        //最大显示数量, 0:不限制
                        showCount: 0,
                        //是否显示删除图标
                        showIcon: false
                    }
                }
            },
            theme: {
                color: '#5FB878'
            },
            content: '<div id="ele1" lay-filter="ele1"></div>'
        });

        //渲染自定义内容
        var ele = eleTree.render({
            elem: '#ele1',
            url: "/platform/getMenuListByParentId",
            method: "POST",
            emptText: "暂无数据",
            showCheckbox: true,
            checkStrictly: true,
            defaultExpandAll: true,
            response: {   // 对于后台数据重新定义名字
                statusName: "code",
                statusCode: 0,
                dataName: "data"
            },
            request: {     // 对后台返回的数据格式重新定义
                name: "title",
                key: "id",
                children: "children",
                checked: "checked",
                disabled: "disabled",
                isLeaf: "isLeaf"
            }
        });

        //监听树的选择
        layui.eleTree.on("nodeChecked(ele1)", function(d) {
            //获取包含父节点在内的数据；ele.getChecked(true, false)只取最后一层节点数据；
            var arr = ele.getChecked(false, true);
            // console.log(arr);
            demo1.update({
                prop: {
                    name: 'title',
                    value: 'id'
                },
                data: arr,
                autoRow: true
            }).setValue(arr);
        });

        //头工具栏事件
        table.on('toolbar(roleList)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            switch(obj.event){

                //自定义头工具栏右侧图标 - 提示
                case 'LAYTABLE_TIPS':
                    layer.alert('这是一个后台系统角色列表！');
                    break;
                case 'reload':
                    // layer.msg('搜索！');
                    active.reload();
                    break;
                //自定义按钮event事件
                case 'add':
                    //弹窗
                    /* 再弹出添加界面 */
                    //layer.msg("新增角色！");
                    layer.open({
                        type:1,
                        title:"添加角色",
                        skin: 'demo-class',
                        area:["50%"],
                        offset: '100px',//只定义top坐标，水平保持居中
                        content:$("#roleDiv"),
                        success: function(layero, index) {
                            //自动根据弹窗内容拉伸高度
                            layer.iframeAuto(index);
                        },
                        end : function() {
                            layer.closeAll();
                            active.reload();
                        }
                    });
                    /* 渲染表单 */
                    form.render();
                    break;
            }
        });

        //监听行工具事件
        table.on('tool(roleList)', function(obj){
            var data = obj.data;
            //console.log(obj)
            if(obj.event === 'del'){
                layer.confirm("您确定要删除当前角色【" + data.name + "】吗？删除后系统不可撤回，角色相关用户都会失去当前角色相关权限！", {
                        icon: 3,
                        skin: 'layui-layer-lan'},
                    function(index){
                    obj.del();
                    layer.close(index);
                });
            } else if(obj.event === 'edit'){
                form.val("roleForm", {
                    "id": data.id
                    , "name": data.name
                    , "description": data.description
                    , "status":data.status
                });
                layer.open({
                    type:1,
                    title:"编辑角色",
                    skin: 'demo-class',
                    area:["50%"],
                    offset: '100px',//只定义top坐标，水平保持居中
                    content:$("#roleDiv"),
                    success: function(layero, index) {
                        //自动根据弹窗内容拉伸高度
                        layer.iframeAuto(index);
                    },
                    end : function() {
                        layer.closeAll();
                        active.reload();
                    }
                });
                /* 渲染表单 */
                form.render();
            } else if (obj.event === 'editRole'){
                //layer.msg("分配权限" + data.id);
                layer.open({
                    type:1,
                    title:"分配角色权限",
                    skin: 'demo-class',
                    area:["50%","50%"],
                    offset: '100px',//只定义top坐标，水平保持居中
                    content:$("#roleMenuDiv"),
                    success: function(layero, index) {
                        //自动根据弹窗内容拉伸高度
                        layer.iframeAuto(index);
                        //为弹窗页面赋值
                        $("#roleId").val(data.id);
                        $("#roleName").val(data.name);
                        $("#roleDesc").val(data.description);
                        //获取当前角色下的权限关联数据
                        $.ajax({
                            url:'/platform/getRoleMenu',
                            method:'post',
                            data:{roleId:data.id},
                            dataType:'JSON',
                            success:function(res){
                                if (res.code === 0) {
                                    var arr = res.data;
                                    if (arr != null){
                                        //初始化权限树
                                        generateXmSelect(arr);
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
                        $("#roleId").val();
                        $("#roleName").val();
                        $("#roleDesc").val();
                        //清空所有选项
                        demo1.setValue([]);
                        //清空所有
                        ele.unCheckNodes();
                    }
                });
                /* 渲染表单 */
                form.render();

            }
        });



        var active = {
            reload: function(){
                var name = $('#key-search');
                //执行重载
                table.reload('roleTable', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        name: name.val()
                    }
                }, 'data');
            }
        };

        //自定义验证规则
        form.verify({
            name: function(value){
                if(value.length < 3){
                    return '角色名称至少得3个字符啊';
                }
            }
        });

        form.on('submit(addSubmit)', function(data){
            // layer.alert(JSON.stringify(data.field), {
            //     title: '最终的提交信息'
            // });
            $.ajax({
                url:'/platform/saveRole',
                method:'post',
                data:data.field,
                dataType:'JSON',
                success:function(res){
                    if (res.code === 0) {
                        // console.log("操作成功===>" + res);
                        layer.msg(res.msg);
                        if (res.data === "update"){
                            //修改完成后关闭当前弹窗
                            layer.closeAll();
                        }else {
                            //重置当前表单
                            $("#roleForm")[0].reset();
                            layui.form.render();
                        }
                    }else{
                        // console.log("操作失败。。。");
                        layer.msg(res.msg);
                        $("#roleForm")[0].reset();
                        layui.form.render();
                    }
                },
                error:function (data) {
                    layer.msg(data.msg);
                }
            }) ;
        });

        //分配角色权限提交
        form.on('submit(editRole)', function(data){
            var selectArr = demo1.getValue('valueStr');
            if (JSON.stringify(selectArr, null, 2).length<3){
                layer.msg("请至少选择一项权限再提交！");
                return;
            }
            var roleId = $("#roleId").val();
            // layer.alert(JSON.stringify(selectArr) + ";roleId=" + roleId, {
            //     title: '最终的提交信息'
            // });
            $.ajax({
                url:'/platform/saveRoleMenu',
                method:'post',
                data:{roleId:roleId,menuIds:selectArr},
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

        //清空一下表单数据
        $(".layui-layer-close").on("click",function () {
            $("#roleForm")[0].reset();
        });

        //监听指定开关
        form.on('switch(status)', function(data){
            var checked = data.elem.checked;
            var tipMsg = "您确定要关闭当前角色吗？关闭后后台所有角色用户将不再拥有当前角色下的权限，可在此重新开启！";
            if (this.checked){
                tipMsg = "您确定要开启当前角色吗？开启后后台所有角色用户将拥有当前角色下的权限，可在此重新关闭！";
            }
            var roleID = data.elem.attributes['data-id'].nodeValue;
            //console.log("roleID===>" + roleID);
            layer.confirm(tipMsg, {
                icon: 3,
                //墨绿风格【layui-layer-molv】深蓝风格【layui-layer-lan】
                skin: 'layui-layer-lan',
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/platform/openOrCloseRole",
                    type : 'POST',
                    data:{id:roleID,status:checked},
                    success : function(data) {
                        layer.msg(data.msg);
                    }
                });
                // data.elem.checked = checked;
                // form.render();
            },function(){
                data.elem.checked=!checked;
                form.render();
            });


        });

        // 为xm-select下拉树赋值；
        function generateXmSelect(data) {
            // console.log("data==>" + data);
            ele.setChecked(data,true);
            var arr = ele.getChecked(false, true);
            // console.log("arr==>" + arr);
            demo1.update({
                prop: {
                    name: 'title',
                    value: 'id'
                },
                data: arr,
                autoRow: true
            }).setValue(arr);
            //ele.config("checkStrictly",false);
        }





    });

</script>

</body>
</html>
