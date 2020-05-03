/*
*@Name: 基于Layui模板，添加常用方法。
*@Author: Super.zhao
*@Copyright:layui.com
*/
layui.define(['layer', 'jquery', 'form', 'laytpl', 'util', 'upload'], function (exports) {
    var $ = layui.$, layer = layui.layer, form = layui.form, laytpl = layui.laytpl,
        util = layui.util, upload = layui.upload;
    var common = {
        //获取是否的中文方法
        getBoolTextByValue: function (value) {
            return this.getTextByValue(value,"是","否");
        },

        //将Bool值转为对应的自定义文字，添加了颜色区分
        getTextByValue: function (value, Tvalue, Fvalue) {
            //将Bool类型也转为1和0
            value = (value || 0);
            if (typeof(value) == "boolean"){
                value = value === true ? 1 : 0;
            }
            if (value === 0) {
                return "<span class=\"layui-red\">" + Fvalue + "</span>";
            } else {
                return "<span class=\"layui-blue\">" + Tvalue + "</span>";
            }
        },

        //将当前文字展示为红色
        showTextByRed: function (value) {
            return "<span class=\"layui-red\">" + value + "</span>";
        },
        //将当前文字展示为蓝色
        showTextByBlue: function (value) {
            return "<span class=\"layui-blue\">" + value + "</span>";
        },
        //将当前文字展示为灰色
        showTextByGray: function (value) {
            return "<span class=\"layui-gray\">" + value + "</span>";
        },
        //将当前文字展示为绿色
        showTextByGreen: function (value) {
            return "<span class=\"layui-green\">" + value + "</span>";
        },
        //将当前文字展示为橙色
        showTextByOrange: function (value) {
            return "<span class=\"layui-orange\">" + value + "</span>";
        },

        //转换输出时间类型数据
        showTime2String: function (value, pattern) {
            return value != null ? util.toDateString(value, pattern) : "-";
        },

        //转换输出时间类型数据
        showMoney2String: function (value) {
            if (value < 10000) {
                return value + "元";
            } else if (value < 100000000) {
                return Math.floor(value / 10000).toString() + "万元";
            } else {
                return Math.floor(value / 100000000).toString() + "亿元";
            }
        },

        //金额格式化
        formatCurrency: function (num) {
            num = (num || 0);
            num = num.toString().replace(/\$|\,/g, '');
            if (isNaN(num)){
                num = "0";
            }
            var sign = (num == (num = Math.abs(num)));
            num = Math.floor(num * 100 + 0.50000000001);
            var cents = num % 100;
            num = Math.floor(num / 100).toString();
            if (cents < 10){
                cents = "0" + cents;
            }
            for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++){
                num = num.substring(0, num.length - (4 * i + 3)) + ',' +
                    num.substring(num.length - (4 * i + 3));
            }
            return (((sign) ? '' : '-') + num + '.' + cents);
        },

        //强制保留两位小数，如：2，会在2后面补上00.即2.00
        toDecimal2: function (x) {
            var f = parseFloat(x);
            if (isNaN(f)) {
                return false;
            }
            f = Math.round(x * 100) / 100;
            var s = f.toString();
            var rs = s.indexOf('.');
            if (rs < 0) {
                rs = s.length;
                s += '.';
            }
            while (s.length <= rs + 2) {
                s += '0';
            }
            return s;
        },


        //将对应数字转换为不同按钮，第一个是按钮值，后面可以添加多种按钮显示文字
        //第一个参数必须是数字，超过五种按钮，后面皆是一样的白色按钮
        //后面的按钮文字必须对应好前面的值
        showBtnByValues: function () {
            //接收所有的不定参数
            var params = arguments;
            var value = params[0];
            if (value < params.length) {
                if (value === 0) {
                    return this.showBtnDefault(params[1]);
                } else if (value === 1) {
                    return this.showBtnNormal(params[2]);
                } else if (value === 2) {
                    return this.showBtnPrimary(params[3]);
                } else if (value === 3) {
                    return this.showBtnWarm(params[4]);
                } else if (value === 4) {
                    return this.showBtnDanger(params[5]);
                } else {
                    return this.showBtnPrimary(params[value + 1]);
                }
            }
            return value;
        },

        //将文字转为默认按钮【墨绿】
        showBtnDefault:function(btnTip){
            return "<button class=\"layui-btn layui-btn-xs\">" + btnTip + "</button>";
        },

        //将文字转为普通按钮【深蓝】
        showBtnNormal:function(btnTip){
            return "<button class=\"layui-btn layui-btn-normal layui-btn-xs\">" + btnTip + "</button>";
        },

        //将文字转为基本按钮【白色】
        showBtnPrimary:function(btnTip){
            return "<button class=\"layui-btn layui-btn-primary layui-btn-xs\">" + btnTip + "</button>";
        },
        //将文字转为温馨按钮【黄色】
        showBtnWarm:function(btnTip){
            return "<button class=\"layui-btn layui-btn-warm layui-btn-xs\">" + btnTip + "</button>";
        },
        //将文字转为警示按钮【红色】
        showBtnDanger:function(btnTip){
            return "<button class=\"layui-btn layui-btn-danger layui-btn-xs\">" + btnTip + "</button>";
        },

        //新增页面弹窗公用方法，【active】需要在页面自定义重载方法，默认弹窗为【65%,85%】
        layerToPage: function (title, url, isform, refresh, active, width, height) {
            var index = layer.open({
                type: 2,
                title: title,
                skin: 'layui-layer-lan',
                area: [width || "65%", height || "85%"],
                offset: '100px',//只定义top坐标，水平保持居中
                maxmin: true,//窗口最大化最小化
                content: url,
                success: function (layero, index) {
                    //layer.iframeAuto(index);
                },
                end: function () {
                    layer.closeAll();
                    if (refresh) {
                        active.reload();
                    }
                }
            });
            /* 渲染表单 */
            if (isform) {
                form.render();
            }
            return index;
        },

        //弹窗显示当前页面的HTML信息，不知名原因导致type为1时，样式有问题，所以重新定义了一下相关样式
        layerCurrentHtml: function (title, url, isform, refresh, active, width, height) {
            var index = layer.open({
                type: 1,
                title: title,
                skin: 'demo-class',
                area: [width || "65%", height || "70%"],
                offset: '100px',//只定义top坐标，水平保持居中
                maxmin: true,//窗口最大化最小化
                content: url,
                success: function (layero, index) {
                    layer.iframeAuto(index);
                },
                end: function () {
                    layer.close(index);
                    if (refresh) {
                        active.reload();
                    }
                }
            });
            /* 渲染表单 */
            if (isform) {
                form.render();
            }
            return index;
        },

        //加载模板引擎数据，模板要定义在各自的页面，【templateId】即为模板的标识ID
        renderHtml: function (templateId, data) {
            var template = laytpl(templateId.innerHTML);
            return template.render(data);
        },

        //POST提交方法，仅弹窗提示信息，并不能赋值，返回值以【LayUIResult】为准
        postAjax: function (url, data) {
            $.ajax({
                url: url,
                method: 'post',
                data: data || '',
                dataType: 'JSON',
                success: function (res) {
                    layer.msg(res.msg);
                },
                error: function (data) {
                    layer.msg(data.msg);
                }
            });
        },

        //POST提交Form方法，提供刷新页面元素和关闭弹窗功能
        postAjaxForm: function (url, data, isRefresh, formId, isClose) {
            $.ajax({
                url: url,
                method: 'post',
                data: data || '',
                dataType: 'JSON',
                success: function (res) {
                    if (res.code === 0) {
                        layer.msg(res.msg);
                        if (isRefresh) {
                            //重置当前表单
                            $("#" + formId)[0].reset();
                            layui.form.render();
                        }
                        if (isClose) {
                            //先得到当前iframe层的索引
                            var index = parent.layer.getFrameIndex(window.name);
                            //再执行关闭
                            parent.layer.close(index);
                        }
                    } else {
                        layer.msg(res.msg);
                    }
                },
                error: function (data) {
                    layer.msg(data.msg);
                }
            });
        },

        //layer表格中删除提示
        deleteConfirm: function (tips, url, obj) {
            layer.confirm(tips,
                {
                    icon: 3,
                    skin: 'layui-layer-lan'
                },
                function (index) {
                    $.ajax({
                        url: url,
                        method: 'post',
                        dataType: 'JSON',
                        success: function (res) {
                            if (res.code === 0) {
                                layer.msg(res.msg);
                                obj.del();
                                layer.close(index);
                            } else {
                                layer.msg(res.msg);
                            }
                        },
                        error: function (data) {
                            layer.msg(data.msg);
                        }
                    });
                });
        },

        //layer中的询问请求
        layerConfirm: function (tips, url, active) {
            layer.confirm(tips,
                {
                    icon: 3,
                    skin: 'layui-layer-lan'
                },
                function (index) {
                    $.ajax({
                        url: url,
                        method: 'post',
                        dataType: 'JSON',
                        success: function (res) {
                            if (res.code === 0) {
                                layer.msg(res.msg);
                                layer.close(index);
                            } else {
                                layer.msg(res.msg);
                            }
                            if (active){
                                active.reload();
                            }
                        },
                        error: function (data) {
                            layer.msg(data.msg);
                        }
                    });
                });
        },

        //switch插件监听事件方法，提供了switch按钮的状态变更
        switchConfirm: function (tipMsg, url, urlData, data) {
            var checked = data.elem.checked;
            layer.confirm(tipMsg, {
                icon: 3,
                //墨绿风格【layui-layer-molv】深蓝风格【layui-layer-lan】
                skin: 'layui-layer-lan',
                btn: ['确定', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: url,
                    type: 'POST',
                    data: urlData,
                    success: function (data) {
                        layer.msg(data.msg);
                    }
                });
            }, function () {
                data.elem.checked = !checked;
                form.render();
            });
        },

        //关闭弹窗
        closePopDIV: function () {
            //先得到当前iframe层的索引
            var index = parent.layer.getFrameIndex(window.name);
            //再执行关闭
            parent.layer.close(index);
        },

        //自定义单先、复选按钮必选验证
        verify_otherReq:function (value,item) {
            var $ = layui.$;
            var verifyName=$(item).attr('name')
                , verifyType=$(item).attr('type')
                ,formElem=$(item).parents('.layui-form')//获取当前所在的form元素，如果存在的话
                ,verifyElem=formElem.find('input[name='+verifyName+']')//获取需要校验的元素
                ,isTrue= verifyElem.is(':checked')//是否命中校验
                ,focusElem = verifyElem.next().find('i.layui-icon');//焦点元素
            if(!isTrue || !value){
                //定位焦点
                focusElem.css(verifyType==='radio'?{"color":"#FF5722"}:{"border-color":"#FF5722"});
                //对非输入框设置焦点
                focusElem.first().attr("tabIndex","1").css("outline","0").blur(function() {
                    focusElem.css(verifyType==='radio'?{"color":""}:{"border-color":""});
                }).focus();
                return '必填项不能为空';
            }
        },

        //览图片
        previewImg: function (obj) {
            var img = new Image();
            img.src = obj.src;
            var height = img.height; //获取图片高度
            var width = img.width; //获取图片宽度
            var imgHtml = "<img src='" + obj.src + "' />";
            //弹出层
            layer.open({
                type: 1,
                shade: 0.8,
                offset: 'auto',
                area: [width + 'px', height + 'px'],
                shadeClose: true,//点击外围关闭弹窗
                scrollbar: false,//不现实滚动条
                title: false, //不显示标题
                content: imgHtml, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
                cancel: function () {
                    //layer.msg('捕获就是从页面已经存在的元素上，包裹layer的结构', { time: 5000, icon: 6 });
                }
            });
        },

        //上传图片
        uploadImg: function (uploadBtn, url, data, previewImg, showTip, hideInput, isUrl, size) {
            var uploadInst = upload.render({
                elem: '#' + uploadBtn
                , url: url
                , size: size || 1024 //限制文件大小，单位 KB
                , data: data
                , before: function (obj) {
                    //预读本地文件示例，不支持ie8
                    obj.preview(function (index, file, result) {
                        var demo1 = $("#" + previewImg);
                        demo1.removeClass("layui-hide");
                        demo1.attr('src', result); //图片链接（base64）
                    });
                }
                , done: function (res) {
                    //如果上传成功
                    if (res.code === 1) {
                        //清空错误提示
                        var demoText = $('#' + showTip);
                        demoText.html("");
                        //设置UUID或是URL；
                        var hideValue = res.data.uuid;
                        isUrl = (isUrl || false);
                        if (isUrl){
                            hideValue = res.data.url;
                        }
                        $("#" + hideInput).val(hideValue);
                        return layer.msg('上传成功');
                    }
                }
                , error: function () {
                    //演示失败状态，并实现重传
                    var demoText = $('#' + showTip);
                    demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-xs demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            });
            return uploadInst;
        },

        //动态初始化单级下拉列表
        initSelect: function (url, elem, value, isBatch) {
            $.ajax({
                url: url
                , method: "POST"
                , success: function (data) {
                    var selectElement = $('#' + elem);
                    isBatch = (isBatch || false);
                    if (isBatch){
                        selectElement = $("." + elem);
                    }
                    $.each(data.data, function (index, item) {
                        selectElement.append(new Option(item.name, item.value));// 下拉菜单里添加元素
                        if (value) {
                            selectElement.find("option[value='" + value + "']").prop("selected", true);
                        }
                    });
                    form.render("select");
                }
            });
        },

        //锁定导航选中样式
        lockNav:function (navName) {
            $('.nav li a').removeClass('current');
            $("." + navName).addClass("current");
        }


    };

    exports('common', common);
});