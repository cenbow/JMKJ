var typeMap = new Map();
typeMap.set(0,"投诉主播");
typeMap.set(1,"投诉客服");
typeMap.set(2,"物流状况");
typeMap.set(3,"优惠活动");
typeMap.set(4,"功能异常");
typeMap.set(5,"表扬");
typeMap.set(6,"建议");
typeMap.set(7,"其他");

 var sessionId = $("#sessionId").val();
 var menuId = $("#menuId").val();
 var ctx = $("#ctx").val();
layui.define(['table', 'form','laydate'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form
  ,element = layui.element;
  
  
  var json = {};
  json.sessionId = sessionId;
  
  var laydate = layui.laydate;
  var insStart = laydate.render({
      elem: '#order-startTime'
      ,min: '1949-01-01'
      ,done: function(value, date){
        //更新结束日期的最小日期
        insEnd.config.min = lay.extend({}, date, {
          month: date.month - 1
        });
        //自动弹出结束日期的选择器
       insEnd.config.elem[0].focus();
      }
    });
    //结束日期
    var insEnd = laydate.render({
      elem: '#order-endTime'
      ,min: 0
      ,done: function(value, date){
        //更新开始日期的最大日期
        insStart.config.max = lay.extend({}, date, {
          month: date.month - 1
        });
      }
    });
  
    table.render({
        elem: '#report-table'
        ,url: ctx + '/system/report/page' //模拟接口
        ,where:json
        ,cols: [[
          {field: 'id',title: 'id', minWidth: 100}
          ,{field: 'account',title: '举报人手机号', minWidth: 100}
          ,{field: 'type',title: '举报类型', width:250,templet:function (d){
	    		 return typeMap.get(d.type);
	    	 }}
		  ,{field: 'description',title: '举报理由'}
		  ,{field: 'targetAccount',title: '被举报人手机号'}
          ,{field: 'state',title: '状态',templet:function(d){
        	  if(d.state==0){
        		  return "待处理";
        	  }else{
        		  return "已处理";
        	  }
          }}
		  ,{field: 'createTime',title: '举报时间'}
          ,{title:"操作",align:'center', fixed: 'right', templet:function(d){
        	  var html = `<a class="layui-btn layui-btn layui-btn-xs" lay-event="detail"><i class="layui-icon"></i>详情</a>`;
        	  if(d.state==0){
        		  html+=`<a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="edit"><i class="layui-icon"></i>处理</a>`;
        	  }
        	  return html;
          }}
        ]]
        ,page: true
        ,limit: 10
        ,height: 'full-200'
        ,text: '对不起，加载出现异常！'
        ,parseData: function(res){ //res 即为原始返回的数据
      	    return {
      	        "code": res.code, //解析接口状态
      	        "msg": res.desc, //解析提示文本
      	        "count": res.data.totalRow, //解析数据长度
      	        "data": res.data.list //解析数据列表
      	      };
      	    }
    	 ,response: {
    	    statusName: 'code' //规定数据状态的字段名称，默认：code
    	      ,statusCode: 1 //规定成功的状态码，默认：0
    	      ,msgName: 'msg' //规定状态信息的字段名称，默认：msg
    	      ,countName: 'count' //规定数据总数的字段名称，默认：count
    	      ,dataName: 'data' //规定数据列表的字段名称，默认：data
    	    } 
    	 ,request: {
    	    pageName: 'pageNumber' //页码的参数名称，默认：page
    	      ,limitName: 'pageSize' //每页数据量的参数名，默认：limit
    	    }
      });
    
    
  //监听搜索
    form.on('submit(LAY-report-search)', function(data){
      var field = data.field;
      field.sessionId = sessionId;
      
      table.reload("report-table", {
          where: field
      });
      //执行重载
    });
    
    form.on('submit(LAY-report-refresh)', function(data){
  	  var field = data.field;
  	    field.sessionId = sessionId;
  	    
  	    
  	    table.reload("report-table", {
  	        where: field
  	    });
     });
    
	 
	 function reloadTable(){
	   table.reload("report-table", {
	   });
		 
	 }
    
    
    function openEdit(id){
    	layer.open({
            type: 2
            ,title: ''
            ,content: project + '/system/report/toEdit?id=' + id+' &sessionId='+sessionId
            ,area: ['100%', '100%'],
            offset:['0px','0px']
            ,btn: ['确定', '取消']
            ,yes: function(index, layero){
              var iframeWindow = window['layui-layer-iframe'+ index]
              ,submit = layero.find('iframe').contents().find("#LAY-delivery-submit");
              //监听提交
              iframeWindow.layui.form.on('submit(LAY-delivery-submit)', function(data){
                 var field = data.field; //获取提交的字段
                  field.sessionId  = sessionId;
                  $(iframeWindow.document).find("[name='sessionId']").val(sessionId);
                  var reportForm = $(iframeWindow.document).find("#reportForm");
    				$.ajax({  
    					  type : 'post',  
    			          url :  project + '/system/report/updatereport',
    			          data : new FormData(reportForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
    			          success : function(data){
    			        	  myfunction(data);
    			        	  table.reload("report-table", {
			        	  	    });
    			          },error:function(data){
    			        	  systemAlertError();
    			          }
    				})
                layer.close(index); //关闭弹层
              });  
              submit.trigger('click');
            }
          }); 
    }
	
	
	 function openAdd(){
		layer.open({
	        type: 2
	        ,title: ''
	        ,content: project + '/system/report/toAdd?sessionId='+sessionId
	        ,area: ['100%', '100%'],
            offset:['0px','0px']
	        ,btn: ['确定', '取消']
	        ,yes: function(index, layero){
	          var iframeWindow = window['layui-layer-iframe'+ index]
	          ,submit = layero.find('iframe').contents().find("#LAY-delivery-submit");
	          //监听提交
	          iframeWindow.layui.form.on('submit(LAY-delivery-submit)', function(data){
	        	  var field = data.field; //获取提交的字段
                  field.sessionId  = sessionId;
                  $(iframeWindow.document).find("[name='sessionId']").val(sessionId);
                  var reportForm = $(iframeWindow.document).find("#reportForm");
					$.ajax({  
						  type : 'post',  
				          url :  project + '/system/report/insertreport',
				          data : new FormData(reportForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
				          success : function(data){
				        	  myfunction(data);
				        	  table.reload("report-table", {
			        	  	    });
				          },error:function(data){
				        	  systemAlertError();
				          }
					})
	            layer.close(index); //关闭弹层
	          });  
	          submit.trigger('click');
	        }
	      }); 
	}
	
	
    
    function myfunction(data){
    	systemAlert(data,function(){})
    }
	
	  //监听工具条
	table.on('tool(report-table)', function(obj){
	      var data = obj.data;
	      if(obj.event === 'edit'){
	      	delreport(data);
	      }else if(obj.event === 'detail'){
	    	detail(data);  
	      }
	});
	
	function detail(data){
		var table = layui.table;
		layer.open({
	        type: 2
	        ,title: ''
	        ,content: ctx + '/system/report/toEdit?id='+data.id
	        ,area: ['600px', '490px']
	        ,btn: ['确定', '取消']
	        ,yes: function(index, layero){
	          var iframeWindow = window['layui-layer-iframe'+ index]
	          ,submit = layero.find('iframe').contents().find("#LAY-delivery-submit");
	            layer.close(index); //关闭弹层
	        }
	      }); 
		
	}
	
	function delreport(data){
		var table = layui.table
		layer.confirm('确定设置为已处理吗?', function(index){
			var req = {};
			req['id'] = data.id;
		    $.ajax({  
				type : 'post',  
		        url :  ctx + '/system/report/editState',
		        data : req,  
		        success : function(data){
		        	systemAlert(data,function(){
		         		 reloadTable();
		         	})
		        }
			})
	    });
	}
	  
  
  
  
  exports('report', {})
});










