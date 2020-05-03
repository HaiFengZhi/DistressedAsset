/*
*@Name: 基于Layui模板，添加三级下拉方法。
*@Author: Super.zhao
*@Copyright:online.shop
*/
layui.define(['jquery', 'form'], function (exports) {
    var $ = layui.$, form = layui.form;

    var thirdSelect = {
        //加载一级下拉数据
        initSelect: function (url, firstName, secondName, thirdName, firstValue, secondValue, thirdValue) {
            var proHtml = '', that = this;
            $.post(url, function (res) {
                var firstSelect = $("select[name=" + firstName + "]");
                var data = res.data;
                for (var i = 0; i < data.length; i++) {
                    firstSelect.append(new Option(data[i].text, data[i].value));
                }
                //初始化省数据
                form.render();
                form.on('select(first)', function (proData) {
                    $("select[name=" + thirdName + "]").html(new Option("--- 请选择 ---", ""));
                    var value = proData.value;
                    if (value > 0) {
                        that.secondSelect(data[$(this).index() - 1].children, secondName, thirdName, secondValue, thirdValue);
                    } else {
                        $("select[name=" + secondName + "]").attr("disabled", "disabled");
                    }
                });
                //初始化一级下拉
                if (firstValue){
                    firstSelect.siblings("div.layui-form-select").find('dl dd[lay-value=' + firstValue + ']').click();
                }
            })
        },
        //加载二层下拉数据
        secondSelect: function (secondData, secondName, thirdName, secondValue, thirdValue) {
            var secondSelect = $("select[name=" + secondName + "]"), that = this;
            secondSelect.html(new Option("--- 请选择 ---", ""));
            for (var i = 0; i < secondData.length; i++) {
                secondSelect.append(new Option(secondData[i].text, secondData[i].value));
            }
            secondSelect.removeAttr("disabled");
            form.render();
            form.on('select(second)', function (cityData) {
                var value = cityData.value;
                if (value > 0) {
                    that.third(secondData[$(this).index() - 1].children, secondName, thirdName, thirdValue);
                } else {
                    $("select[name=" + thirdName + "]").attr("disabled", "disabled");
                }
            });
            //初始化二级下拉
            if (secondValue){
                secondSelect.siblings("div.layui-form-select").find('dl dd[lay-value=' + secondValue + ']').click();
            }
        },
        //加载三级下拉数据
        third: function (areas, secondName, thirdName,thirdValue) {
            var thirdSelect = $("select[name=" + thirdName + "]");
            thirdSelect.html(new Option("--- 请选择 ---", ""));
            for (var i = 0; i < areas.length; i++) {
                thirdSelect.append(new Option(areas[i].text, areas[i].value));
                if (thirdValue){
                    thirdSelect.find("option[value='" + thirdValue + "']").prop("selected", true);
                }
            }
            thirdSelect.removeAttr("disabled");
            form.render();
        }
    };

    exports('thirdSelect', thirdSelect);
});
