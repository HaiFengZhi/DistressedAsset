<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理中心</title>
</head>
<body>
<blockquote class="layui-elem-quote">
    本页面只是演示静态表格以及常用表单：${msg}
</blockquote>
<img src="/static/images/upload.gif" class="layui-nav-img-l"/><br/>

<br/>
<%--<table class="layui-hide" id="test" lay-filter="test"></table>--%>

<%--<script type="text/html" id="toolbarDemo">--%>
<%--    <div class="layui-btn-container">--%>
<%--        <button class="layui-btn layui-btn-sm" lay-event="getCheckData">获取选中行数据</button>--%>
<%--        <button class="layui-btn layui-btn-sm" lay-event="getCheckLength">获取选中数目</button>--%>
<%--        <button class="layui-btn layui-btn-sm" lay-event="isAll">验证是否全选</button>--%>
<%--    </div>--%>
<%--</script>--%>

<%--<script type="text/html" id="barDemo">--%>
<%--    <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>--%>
<%--    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>--%>
<%--</script>--%>
<%--<script>--%>
<%--    layui.use('table', function(){--%>
<%--        var table = layui.table;--%>

<%--        table.render({--%>
<%--            elem: '#test'--%>
<%--            ,url:'http://www.layui.com/test/table/demo1.json'--%>
<%--            ,crossDomain: true--%>
<%--            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板--%>
<%--            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可--%>
<%--                title: '提示'--%>
<%--                ,layEvent: 'LAYTABLE_TIPS'--%>
<%--                ,icon: 'layui-icon-tips'--%>
<%--            }]--%>
<%--            ,title: '用户数据表'--%>
<%--            ,cols: [[--%>
<%--                {type: 'checkbox', fixed: 'left'}--%>
<%--                ,{field:'id', title:'ID', width:80, fixed: 'left', unresize: true, sort: true}--%>
<%--                ,{field:'username', title:'用户名', width:120, edit: 'text'}--%>
<%--                ,{field:'email', title:'邮箱', width:150, edit: 'text', templet: function(res){--%>
<%--                        return '<em>'+ res.email +'</em>'--%>
<%--                    }}--%>
<%--                ,{field:'sex', title:'性别', width:80, edit: 'text', sort: true}--%>
<%--                ,{field:'city', title:'城市', width:100}--%>
<%--                ,{field:'sign', title:'签名'}--%>
<%--                ,{field:'experience', title:'积分', width:80, sort: true}--%>
<%--                ,{field:'ip', title:'IP', width:120}--%>
<%--                ,{field:'logins', title:'登入次数', width:100, sort: true}--%>
<%--                ,{field:'joinTime', title:'加入时间', width:120}--%>
<%--                ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:150}--%>
<%--            ]]--%>
<%--            ,page: true--%>
<%--        });--%>

<%--        //头工具栏事件--%>
<%--        table.on('toolbar(test)', function(obj){--%>
<%--            var checkStatus = table.checkStatus(obj.config.id);--%>
<%--            switch(obj.event){--%>
<%--                case 'getCheckData':--%>
<%--                    var data = checkStatus.data;--%>
<%--                    layer.alert(JSON.stringify(data));--%>
<%--                    break;--%>
<%--                case 'getCheckLength':--%>
<%--                    var data = checkStatus.data;--%>
<%--                    layer.msg('选中了：'+ data.length + ' 个');--%>
<%--                    break;--%>
<%--                case 'isAll':--%>
<%--                    layer.msg(checkStatus.isAll ? '全选': '未全选');--%>
<%--                    break;--%>

<%--                //自定义头工具栏右侧图标 - 提示--%>
<%--                case 'LAYTABLE_TIPS':--%>
<%--                    layer.alert('这是工具栏右侧自定义的一个图标按钮');--%>
<%--                    break;--%>
<%--            };--%>
<%--        });--%>

<%--        //监听行工具事件--%>
<%--        table.on('tool(test)', function(obj){--%>
<%--            var data = obj.data;--%>
<%--            //console.log(obj)--%>
<%--            if(obj.event === 'del'){--%>
<%--                layer.confirm('真的删除行么', function(index){--%>
<%--                    obj.del();--%>
<%--                    layer.close(index);--%>
<%--                });--%>
<%--            } else if(obj.event === 'edit'){--%>
<%--                layer.prompt({--%>
<%--                    formType: 2--%>
<%--                    ,value: data.email--%>
<%--                }, function(value, index){--%>
<%--                    obj.update({--%>
<%--                        email: value--%>
<%--                    });--%>
<%--                    layer.close(index);--%>
<%--                });--%>
<%--            }--%>
<%--        });--%>
<%--    });--%>
<%--</script>--%>


<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>表单集合演示</legend>
</fieldset>

<form class="layui-form" action="" lay-filter="example">
    <div class="layui-form-item">
        <label class="layui-form-label">单行输入框</label>
        <div class="layui-input-block">
            <input type="text" name="title" lay-verify="title" autocomplete="off" placeholder="请输入标题" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">验证必填项</label>
        <div class="layui-input-block">
            <input type="text" name="username" lay-verify="required" lay-reqtext="用户名是必填项，岂能为空？" placeholder="请输入" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">验证手机</label>
            <div class="layui-input-inline">
                <input type="tel" name="phone" lay-verify="required|phone" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">验证邮箱</label>
            <div class="layui-input-inline">
                <input type="text" name="email" lay-verify="email" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">多规则验证</label>
            <div class="layui-input-inline">
                <input type="text" name="number" lay-verify="required|number" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">验证日期</label>
            <div class="layui-input-inline">
                <input type="text" name="date" id="date" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">验证链接</label>
            <div class="layui-input-inline">
                <input type="tel" name="url" lay-verify="url" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">验证身份证</label>
        <div class="layui-input-block">
            <input type="text" name="identity" lay-verify="identity" placeholder="" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">自定义验证</label>
        <div class="layui-input-inline">
            <input type="password" name="password" lay-verify="pass" placeholder="请输入密码" autocomplete="off" class="layui-input">
        </div>
        <div class="layui-form-mid layui-word-aux">请填写6到12位密码</div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">范围</label>
            <div class="layui-input-inline" style="width: 100px;">
                <input type="text" name="price_min" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
            <div class="layui-form-mid">-</div>
            <div class="layui-input-inline" style="width: 100px;">
                <input type="text" name="price_max" placeholder="￥" autocomplete="off" class="layui-input">
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">单行选择框</label>
        <div class="layui-input-block">
            <select name="interest" lay-filter="aihao">
                <option value=""></option>
                <option value="0">写作</option>
                <option value="1" selected="">阅读</option>
                <option value="2">游戏</option>
                <option value="3">音乐</option>
                <option value="4">旅行</option>
            </select>
        </div>
    </div>


    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">分组选择框</label>
            <div class="layui-input-inline">
                <select name="quiz">
                    <option value="">请选择问题</option>
                    <optgroup label="城市记忆">
                        <option value="你工作的第一个城市">你工作的第一个城市</option>
                    </optgroup>
                    <optgroup label="学生时代">
                        <option value="你的工号">你的工号</option>
                        <option value="你最喜欢的老师">你最喜欢的老师</option>
                    </optgroup>
                </select>
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">搜索选择框</label>
            <div class="layui-input-inline">
                <select name="modules" lay-verify="required" lay-search="">
                    <option value="">直接选择或搜索选择</option>
                    <option value="1">layer</option>
                    <option value="2">form</option>
                    <option value="3">layim</option>
                    <option value="4">element</option>
                    <option value="5">laytpl</option>
                    <option value="6">upload</option>
                    <option value="7">laydate</option>
                    <option value="8">laypage</option>
                    <option value="9">flow</option>
                    <option value="10">util</option>
                    <option value="11">code</option>
                    <option value="12">tree</option>
                    <option value="13">layedit</option>
                    <option value="14">nav</option>
                    <option value="15">tab</option>
                    <option value="16">table</option>
                    <option value="17">select</option>
                    <option value="18">checkbox</option>
                    <option value="19">switch</option>
                    <option value="20">radio</option>
                </select>
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">联动选择框</label>
        <div class="layui-input-inline">
            <select name="quiz1">
                <option value="">请选择省</option>
                <option value="浙江" selected="">浙江省</option>
                <option value="你的工号">江西省</option>
                <option value="你最喜欢的老师">福建省</option>
            </select>
        </div>
        <div class="layui-input-inline">
            <select name="quiz2">
                <option value="">请选择市</option>
                <option value="杭州">杭州</option>
                <option value="宁波" disabled="">宁波</option>
                <option value="温州">温州</option>
                <option value="温州">台州</option>
                <option value="温州">绍兴</option>
            </select>
        </div>
        <div class="layui-input-inline">
            <select name="quiz3">
                <option value="">请选择县/区</option>
                <option value="西湖区">西湖区</option>
                <option value="余杭区">余杭区</option>
                <option value="拱墅区">临安市</option>
            </select>
        </div>
        <div class="layui-form-mid layui-word-aux">此处只是演示联动排版，并未做联动交互</div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">复选框</label>
        <div class="layui-input-block">
            <input type="checkbox" name="like[write]" title="写作">
            <input type="checkbox" name="like[read]" title="阅读" checked="">
            <input type="checkbox" name="like[game]" title="游戏">
        </div>
    </div>

    <div class="layui-form-item" pane="">
        <label class="layui-form-label">原始复选框</label>
        <div class="layui-input-block">
            <input type="checkbox" name="like1[write]" lay-skin="primary" title="写作" checked="">
            <input type="checkbox" name="like1[read]" lay-skin="primary" title="阅读">
            <input type="checkbox" name="like1[game]" lay-skin="primary" title="游戏" disabled="">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">开关-默认关</label>
        <div class="layui-input-block">
            <input type="checkbox" name="close" lay-skin="switch" lay-text="ON|OFF">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">开关-默认开</label>
        <div class="layui-input-block">
            <input type="checkbox" checked="" name="open" lay-skin="switch" lay-filter="switchTest" lay-text="ON|OFF">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">单选框</label>
        <div class="layui-input-block">
            <input type="radio" name="sex" value="男" title="男" checked="">
            <input type="radio" name="sex" value="女" title="女">
            <input type="radio" name="sex" value="禁" title="禁用" disabled="">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">普通文本域</label>
        <div class="layui-input-block">
            <textarea placeholder="请输入内容" class="layui-textarea"></textarea>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
      <label class="layui-form-label">编辑器</label>
      <div class="layui-input-block">
        <textarea class="layui-textarea layui-hide" name="content" lay-verify="content" id="LAY_demo_editor"></textarea>
      </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button type="submit" class="layui-btn" lay-submit="" lay-filter="demo1">立即提交</button>
            <button type="button" class="layui-btn layui-btn-normal" id="LAY-component-form-setval">赋值</button>
            <button type="button" id="LAY-component-form-getval" class="layui-btn layui-btn-normal">表单取值</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
<script>
    layui.use(['form', 'layedit', 'laydate'], function(){
        var form = layui.form
            ,layer = layui.layer
            ,layedit = layui.layedit
            ,laydate = layui.laydate;

        //日期
        laydate.render({
            elem: '#date'
        });
        laydate.render({
            elem: '#date1'
        });

        //创建一个编辑器
        var editIndex = layedit.build('LAY_demo_editor');

        //自定义验证规则
        form.verify({
            title: function(value){
                if(value.length < 5){
                    return '标题至少得5个字符啊';
                }
            }
            ,pass: [
                /^[\S]{6,12}$/
                ,'密码必须6到12位，且不能出现空格'
            ]
            ,content: function(value){
                layedit.sync(editIndex);
            }
        });

        //监听指定开关
        form.on('switch(switchTest)', function(data){
            layer.msg('开关checked：'+ (this.checked ? 'true' : 'false'), {
                offset: '6px'
            });
            layer.tips('温馨提示：请注意开关状态的文字可以随意定义，而不仅仅是ON|OFF', data.othis)
        });

        //监听提交
        form.on('submit(demo1)', function(data){
            layer.alert(JSON.stringify(data.field), {
                title: '最终的提交信息'
            });
            return false;
        });

        //表单赋值
        layui.$('#LAY-component-form-setval').on('click', function(){
            form.val('example', {
                "username": "贤心" // "name": "value"
                ,"password": "123456"
                ,"interest": 1
                ,"like[write]": true //复选框选中状态
                ,"close": true //开关状态
                ,"sex": "女"
                ,"desc": "我爱 layui"
            });
        });

        //表单取值
        layui.$('#LAY-component-form-getval').on('click', function(){
            var data = form.val('example');
            console.log("data==>" + data);
            alert(JSON.stringify(data));
        });

    });
</script>

</body>
</html>