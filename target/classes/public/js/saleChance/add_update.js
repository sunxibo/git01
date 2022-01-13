layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单事件
     */
    form.on("submit(addOrUpdateSaleChance)",function(obj){
        /*加载层*/
        var index=layer.msg("数据正在提交中，请稍等。。。。。。。",{icon: 16,time:false,shade:0.8 });

        console.log(obj.field+"<<");

        //判断是添加还是修改，id==null,添加，id!=null 修改

        var url=ctx+"/sale_chance/save";

        //判断当前页面的隐藏域有没有id,有id做修改，否则添加操作
        if($("input[name=id]").val()){
            url=ctx+"/sale_chance/update"
        }

        /*发送ajax*/
        $.ajax({
            type:"post",
            url:url,
            data:obj.field,
            dataType:"json",
            success:function (obj){
                if(obj.code==200){
                    //提示一下
                    if ($("input[name=id]").val()){
                        layer.msg("修改成功",{icon: 6 });
                    }else {
                        layer.msg("添加成功",{icon: 6 });
                    }
                    //关闭加载层
                    layer.close(index);
                    //关闭iframe
                    layer.closeAll("iframe");
                    //刷新页面
                    window.parent.location.reload();
                }else{
                    layer.msg(obj.msg,{icon : 6 });
                }
            }
        });
        //取消跳转
        return false;
    });


    /*取消功能*/
    $("#closeBtn").click(function(){
        //假设这是iframe页
        // var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        // parent.layer.close(index); //再执行关闭
        //获取当前弹出层的索引
       var idx= parent.layer.getFrameIndex(window.name);
       //根据索引关闭
       parent.layer.close(idx);
    });

    /*添加下拉框*/
    $.post(ctx + "/user/sales",function (data) {
            // 如果是修改操作，判断当前修改记录的指派人的值
        var assignMan = $("input[name='man']").val();
        for (var i = 0; i < data.length; i++) {
            // 当前修改记录的指派人的值 与 循环到的值 相等，下拉框则选中
            if (assignMan == data[i].id) {
                $("#assignMan").append('<option value="'+data[i].id+'"selected>'+data[i].uname+'</option>');
            } else {
                $("#assignMan").append('<optionvalue="'+data[i].id+'">'+data[i].uname+'</option>');
            }
        }
        // 重新渲染下拉框内容
        layui.form.render("select");
    });



            });