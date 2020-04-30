<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/10/26 0026
  Time: 下午 5:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>权限菜单列表</title>
</head>
<body>
<blockquote class="layui-elem-quote">
    <span class="layui-breadcrumb" lay-separator="|">
      <a href="">控制台</a>
      <a href="">用户权限管理</a>
      <a><cite>系统权限</cite></a>
    </span>
</blockquote>

<fieldset class="layui-elem-field">
    <div class="layui-field-box">
        <label>权限名称：</label>
        <div class="layui-inline">
            <input class="layui-input" style="height: 30px;" name="name" id="name" autocomplete="off">
        </div>
        <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" data-type="reload">搜索</button>
    </div>
</fieldset>
<table class="layui-hide" id="menuList" lay-filter="menuList"></table>

<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button id="addEmplpyeeBtn" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">
            <i class="layui-icon">&#xe654;</i>新增权限
        </button>

    </div>
</script>

<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>

<!-- 权限等级显示模板 -->
<script type="text/html" id="levelShow">
    {{#  if(d.level==0){ }}
    <button class="layui-btn layui-btn-xs">模块</button>
    {{#  } else if(d.level==1){ }}
    <button class="layui-btn layui-btn-normal layui-btn-xs">菜单</button>
    {{#  } else { }}
    <button class="layui-btn layui-btn-primary layui-btn-xs">按钮</button>
    {{#  } }}
</script>
<!-- 这里的 checked 的状态只是演示 -->
<script type="text/html" id="showOrTpl">
    <input type="checkbox" name="showOr" value="{{d.showOr}}" lay-skin="switch" lay-text="是|否" lay-filter="showOr" disabled="disabled" {{ d.showOr == true ? 'checked' : '' }} />
</script>
<script type="text/html" id="mainPermissionsOrTpl">
    <input type="checkbox" name="mainPermissionsOr" value="{{d.mainPermissionsOr}}" lay-skin="switch" lay-text="是|否" lay-filter="mainPermissionsOr"
           disabled="disabled" {{ d.mainPermissionsOr == true ? 'checked' : '' }} />
</script>


<script>
    layui.use(['table','layer','jquery','form'], function(){
        var table = layui.table,
            layer=layui.layer,
            form = layui.form,
            $ = layui.$;

        table.render({
            elem: '#menuList'
            ,url:'/platform/listData'
            ,method:"POST"
            ,cellMinWidth: 100
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }]
            ,title: '系统权限列表'
            ,cols: [[
                {type: 'checkbox', fixed: 'left'}
                ,{field:'id', title:'ID', width:80, fixed: 'left', unresize: true, sort: true}
                ,{field:'name', title:'权限名称', width:120}
                ,{field:'accessUrl', title:'权限地址', width:300}
                ,{field:'parentId', title:'父节点ID', width:100, sort: true}
                ,{field:'description', title:'权限描述'}
                ,{field:'level', title:'权限等级', width:100, sort: true, templet:'#levelShow', align:"center"}
                ,{field:'showOr', title:'是否显示', width:100, sort: true, templet:'#showOrTpl', align:"center"}
                ,{field:'sequence', title:'排序', width:100, sort: true, align:"center"}
                ,{field:'mainPermissionsOr', title:'是否主权限', width:120, sort: true, templet:'#mainPermissionsOrTpl', align:"center",hide:true}
                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:150}
            ]]
            ,page: true
            ,id: 'menuTable'//用来刷新表格的标识
        });

        //头工具栏事件
        table.on('toolbar(menuList)', function(obj){
            var checkStatus = table.checkStatus(obj.config.id);
            switch(obj.event){
                case 'getCheckData':
                    var data = checkStatus.data;
                    layer.alert(JSON.stringify(data));
                    break;
                case 'getCheckLength':
                    var data = checkStatus.data;
                    layer.msg('选中了：'+ data.length + ' 个');
                    break;
                case 'isAll':
                    layer.msg(checkStatus.isAll ? '全选': '未全选');
                    break;

                //自定义头工具栏右侧图标 - 提示
                case 'LAYTABLE_TIPS':
                    layer.alert('这是一个后台系统权限列表！');
                    break;
                //自定义按钮event事件
                case 'add':
                    //弹窗
                    /* 再弹出添加界面 */
                    layer.open({
                        type:2,
                        title:"添加权限",
                        //skin:"myclass",
                        area:["50%"],
                        offset: '100px',//只定义top坐标，水平保持居中
                        content:"/platform/toAdd",
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
        table.on('tool(menuList)', function(obj){
            var data = obj.data;
            //console.log(obj)
            if(obj.event === 'del'){
                layer.confirm('真的要删除当前行么？', function(index){
                    obj.del();
                    layer.close(index);
                });
            } else if(obj.event === 'edit'){
                layer.prompt({
                    formType: 2
                    ,value: data.name
                }, function(value, index){
                    obj.update({
                        name: value
                    });
                    layer.close(index);
                });
            }
        });

        var active = {
            reload: function(){
                var name = $('#name');
                //执行重载
                table.reload('menuTable', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        name: name.val()
                    }
                }, 'data');
            }
        };

        $('.layui-elem-field .layui-field-box .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });


    });
</script>

</body>
</html>
