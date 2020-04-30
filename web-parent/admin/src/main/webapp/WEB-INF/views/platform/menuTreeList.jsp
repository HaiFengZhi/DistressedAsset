<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>系统权限树形列表</title>
    <style>
        .layui-layout-left-350 {
            position: absolute !important;
            left: 350px;
            top: 0
        }
    </style>
</head>
<body>
<blockquote class="layui-elem-quote">
    <span class="layui-breadcrumb" lay-separator="|">
      <a href="#">控制台</a>
      <a href="#">权限管理</a>
      <a><cite>系统权限</cite></a>
    </span>
</blockquote>
<%--定义表格--%>
<table id="permissionTable" class="layui-table layui-hide" lay-filter="permissionTable"></table>
<%--定义表格绑定按钮--%>
<script type="text/html" id="toolbarDemo">
    <div class="layui-btn-container">
        <button id="addEmplpyeeBtn" class="layui-btn layui-btn-sm layui-btn-normal" lay-event="add">
            <i class="layui-icon">&#xe654;</i>新增权限
        </button>
        <button class="layui-btn layui-btn-sm" lay-event="expand">全部展开</button>
        <button class="layui-btn layui-btn-sm" lay-event="fold">全部折叠</button>
        <button class="layui-btn layui-btn-sm" lay-event="refresh">刷新表格</button>
    </div>
    <div class="layui-field-box layui-layout-left-350">
        <label>权限名称：</label>
        <div class="layui-inline">
            <input class="layui-input" style="height: 30px;" name="key-search" id="key-search" autocomplete="off">
        </div>
        <button class="layui-btn layui-btn-normal layui-btn-sm layui-btn-width-sm-80" lay-event="reload">搜索</button>
    </div>
</script>
<%--定义是否显示按钮--%>
<script type="text/html" id="showOrTpl">
    <input type="checkbox" name="showOr" value="{{d.showOr}}" data-id="{{d.id}}" lay-skin="switch" lay-text="是|否" lay-filter="status"  {{ d.showOr == true ? 'checked' : '' }} />
</script>
<%--定义是否是主权限--%>
<script type="text/html" id="mainPermissionsOrTpl">
    <input type="checkbox" name="mainPermissionsOr" value="{{d.mainPermissionsOr}}" lay-skin="switch" lay-text="是|否" lay-filter="mainPermissionsOr"
           disabled="disabled" {{ d.mainPermissionsOr == true ? 'checked' : '' }} />
</script>

<script>
    //引用外部插件
    layui.config({
        base: '/static/lib/layui_ext/'
    }).extend({
        treetable: 'treetable-lay/treetable-2.0'
    }).use(['jquery', 'layer', 'element','table', 'treetable','form','common'], function(){
        var $ = layui.$ ,//Jquery
            layer = layui.layer,//弹窗
            element = layui.element,//页面元素
            table = layui.table,//表格
            form = layui.form,//Form表单
            common = layui.common,//自定义公共方法
            treetable = layui.treetable;//树形表结构

        // 渲染表格
        var renderTable = function () {//树桩表格参考文档：https://gitee.com/whvse/treetable-lay
            layer.load(2);
            treetable.render({
                treeColIndex: 1,//树形图标显示在第几列
                treeSpid: 0,//最上级的父级id
                treeIdName: 'id',//id字段的名称
                treePidName: 'parentId',//pid字段的名称
                treeDefaultClose: true,//是否默认折叠
                treeLinkage: true,//父级展开时是否自动展开所有子级
                elem: '#permissionTable',
                toolbar: '#toolbarDemo',
                url: '/platform/listTreeData',
                page: false,
                cols: [[
                    {type: 'numbers', title: '编号'},
                    {field: 'name', title: '权限名称'},
                    {field: 'accessUrl', title: '权限路径'},
                    {field: 'description', title: '权限简介'},
                    {field:'showOr', title:'是否显示', width:100, templet:'#showOrTpl', align:"center"},
                    {field: 'sequence', title: '排序',align:"center",width:100},
                    {field:'mainPermissionsOr', title:'是否为主权限', width:120, templet:'#mainPermissionsOrTpl', align:"center",hide:true},
                    {field: 'level', title: '类型', align:"center",width:100,
                        templet: function(d){
                            return common.showBtnByValues(d.level, "模块", "菜单", "按钮");
                        }
                    },
                    {templet: complain, title: '操作',align:"center",width:150}
                ]],
                done: function () {
                    layer.closeAll('loading');
                    $('th').css({'background-color': 'lightgray'  ,'font-weight':'bold' });
                }
            });
        };

        renderTable();

        //定义一个刷新方法
        var active = {
            reload: function(){
                renderTable();
            }
        };

        function complain(d){//操作中显示的内容
            return [
                '<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>',
                '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>'
            ].join('');
        }
        //监听工具条
        table.on('tool(permissionTable)', function (obj) {
            var data = obj.data;
            var layEvent = obj.event;
            if(data.name!=null){
                if (layEvent === 'del') {
                    //主权限不可删除
                    if (data.mainPermissionsOr){
                        layer.msg("当前菜单【" + data.name + "】为主权限，不可删除！");
                        return;
                    }
                    //layer.msg('删除' + data.id);
                    common.deleteConfirm("您确定要删除菜单【" + data.name + "】吗，删除后系统将无法撤消？", "/platform/deleteMenu?id=" + data.id, obj);

                } else if (layEvent === 'edit') {
                    // layer.msg('修改' + data.id);
                    /* 弹出添加界面 */
                    common.layerToPage("修改权限", "/platform/toAdd?id=" + data.id, true, true, active);
                }
            }
        });

        table.on('toolbar(permissionTable)', function(obj){
            // console.log("obj.event==>" + obj.event);
            switch(obj.event){
                case "expand":
                    treetable.expandAll('#permissionTable');
                    break;
                case "fold":
                    treetable.foldAll('#permissionTable');
                    break;
                case "refresh":
                    renderTable();
                    break;
                case "reload":
                    //搜索按钮点击事件；
                    var keyword = $('#key-search').val();
                    var searchCount = 0;
                    $('#permissionTable').next('.treeTable').find('.layui-table-body tbody tr td').each(function () {
                        $(this).css('background-color', 'transparent');
                        var text = $(this).text();
                        if (keyword !== '' && text.indexOf(keyword) >= 0) {
                            $(this).css('background-color', 'rgba(250,230,160,0.5)');
                            if (searchCount === 0) {
                                treetable.expandAll('#permissionTable');
                                $('html,body').stop(true);
                                $('html,body').animate({scrollTop: $(this).offset().top - 150}, 500);
                            }
                            searchCount++;
                        }
                    });
                    if (keyword === '') {
                        layer.msg("请输入搜索内容", {icon: 5});
                    } else if (searchCount === 0) {
                        layer.msg("没有匹配结果", {icon: 5});
                    }
                    break;
                case 'add':
                    //弹窗
                    /* 弹出添加界面 */
                    common.layerToPage("添加权限", "/platform/toAdd", true, true, active);
                    break;
            }
        });

        //监听指定开关
        form.on('switch(status)', function(data){
            var checked = data.elem.checked;
            var tipMsg = "您确定要关闭当前菜单吗？关闭后后台所有用户将不再显示当前菜单，可在此重新开启！";
            if (this.checked){
                tipMsg = "您确定要开启当前菜单吗？开启后后台权限用户将显示当前菜单，可在此重新关闭！";
            }
            var menuID = data.elem.attributes['data-id'].nodeValue;
            console.log("menuID===>" + menuID);
            layer.confirm(tipMsg, {
                icon: 3,
                //墨绿风格【layui-layer-molv】深蓝风格【layui-layer-lan】
                skin: 'layui-layer-lan',
                btn: ['确定','取消'] //按钮
            }, function(){
                $.ajax({
                    url: "/platform/openOrCloseMenu",
                    type : 'POST',
                    data:{id:menuID,status:checked},
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

    });
</script>

</body>
</html>
