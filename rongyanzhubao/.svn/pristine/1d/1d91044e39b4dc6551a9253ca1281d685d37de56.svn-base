var stateMap = new Map();
stateMap.set(0,"直播中");
stateMap.set(1,"冻结");
stateMap.set(2,"离线");
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
        elem: '#room-table'
        ,url: ctx + '/system/room/page' //模拟接口
        ,where:json
        ,cols: [[
          {field: 'id',type: 'checkbox', fixed: 'left'},
          {field: 'id',title: 'id', minWidth: 100}
          ,{field: 'roomNumber',title: '房间号', minWidth: 100}
          ,{field: 'mobile',title: '主播手机号', width:250}
		  ,{field: 'roomIntroduction',title: '房间简介'}
          ,{field: 'name',title: '房间名称',}
		  ,{field: 'merchantName',title: '商家昵称'}
		  ,{field: 'merchantMobile',title: '商家账号'}
		 /* ,{field: 'peakNumber',title: '最高峰房间人数'}*/
		  ,{field: 'robotNumber',title: '机器人数量'}
		  ,{field: 'isRecommend',title: '是否是精选直播',templet:function(d){
			  if(d.isRecommend==0){
				  return '否';
			  }else{
				  return '是';
			  }
		  }}
		  ,{field: 'state',title: '状态',templet:function(d){
			  return stateMap.get(d.state);
		  }}
		  ,{field: 'createTime',title: '创建时间'}
		  ,{title:"操作",align:'center', fixed: 'right',width:350, templet: function(d){
			   var text ="";
			   if(d.state==0){
				   text ="冻结";
			   }else if(d.state==2){
				   text = "冻结";
			   }else{
				   text = "解冻";
			   }
	           var html = `<a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="detail"><i class="layui-icon "></i>房间详情</a>
	        	   <a class="layui-btn layui-btn layui-btn-xs" lay-event="fz"><i class="layui-icon "></i>${text}</a>
	        	   <a class="layui-btn layui-btn layui-btn-xs" lay-event="add"><i class="layui-icon "></i>一键加人</a>`;
	           if(d.isRecommend==1){
	        	   html+=`<a class="layui-btn layui-btn layui-btn-xs" lay-event="cancelRecommend"><i class="layui-icon "></i>取消精选</a>`; 
	           }else{
	        	   html+=`<a class="layui-btn layui-btn layui-btn-xs" lay-event="setRecommend"><i class="layui-icon ">设为精选</i></a>`; 
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
    form.on('submit(LAY-room-search)', function(data){
      var field = data.field;
      console.log(field);
      table.reload("room-table", {
          where: field
      });
      //执行重载
    });
    
    form.on('submit(LAY-room-refresh)', function(data){
  	  	var field = data.field;
  	  	console.log(field);
  	    table.reload("room-table", {
  	        where: field
  	    });
     });
    
	 
	 function reloadTable(){
	   table.reload("room-table", {
	   });
	 }
    
    
    function openEdit(id){
    	layer.open({
            type: 2
            ,title: ''
            ,content: ctx + '/system/room/toEdit?id=' + id+' &sessionId='+sessionId
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
                  var roomForm = $(iframeWindow.document).find("#roomForm");
    				$.ajax({  
    					  type : 'post',  
    			          url :  ctx + '/system/room/updateroom',
    			          data : new FormData(roomForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
    			          success : function(data){
    			        	  myfunction(data);
    			        	  table.reload("room-table", {
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
	        ,content: ctx + '/system/room/toAdd?sessionId='+sessionId
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
                  var roomForm = $(iframeWindow.document).find("#roomForm");
					$.ajax({  
						  type : 'post',  
				          url :  ctx + '/system/room/insertroom',
				          data : new FormData(roomForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
				          success : function(data){
				        	  myfunction(data);
				        	  table.reload("room-table", {
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
	table.on('tool(room-table)', function(obj){
	      var data = obj.data;
	      if(obj.event === 'fz'){
	    	  layer.confirm('确定冻结吗?', function(index){
		  			var req = {};
		  			req['id'] = data.id;
		  		    $.ajax({  
		  				type : 'post',  
		  		        url :  ctx + '/system/room/editState',
		  		        data : req,  
		  		        success : function(data){
		  		        	systemAlert(data,function(){
		  		         		 reloadTable();
		  		         	})
		  		        }
		  			})
		  	    });
	      }else if(obj.event === 'add'){
	    	 var id = obj.data.id;
			 layer.open({
				id:1,
			        type: 1,
			        title:'添加机器人',
			        skin:'layui-layer-rim',
			        area:['450px', 'auto'],
			        
			        content: ' <div class="row" style="width: 420px;  margin-left:7px; margin-top:10px;">'
			            +'<div class="col-sm-12">'
			            +'<div class="input-group">'
			            +'<span class="input-group-addon">请输入添加的机器人数量:</span>'
			            +'<input id="ratio" type="number" class="form-control" placeholder="数量">'
			            +'</div>'
			            +'</div>'
			              +'</div>'
			        ,
			        btn:['确认','取消'],
			        btn1: function (index,layero) {
			        	var req = {};
			  			req['id'] = data.id;
			  			req['number'] = document.getElementById("ratio").value;
			  		    $.ajax({  
			  				type : 'post',  
			  		        url :  ctx + '/system/room/editRobotNumber',
			  		        data : req,  
			  		        success : function(data){
			  		        	systemAlert(data,function(){
			  		         		 reloadTable();
			  		         		 layer.close(index);
			  		         	})
			  		        }
			  			})
			    	},
			        btn2:function (index,layero) {
			        	 layer.close(index);
			        }
			    });

	      }else if(obj.event === 'cancelRecommend'){
	    	  layer.confirm('确定取消精选吗?', function(index){
		  			var req = {};
		  			req['id'] = data.id;
		  		    $.ajax({  
		  				type : 'post',  
		  		        url :  ctx + '/system/room/editRecommend',
		  		        data : req,  
		  		        success : function(data){
		  		        	systemAlert(data,function(){
		  		         		 reloadTable();
		  		         	})
		  		        }
		  			})
		  	    });
	      }else if(obj.event === 'setRecommend'){
	    	  layer.confirm('确定设为精选吗?', function(index){
		  			var req = {};
		  			req['id'] = data.id;
		  		    $.ajax({  
		  				type : 'post',  
		  		        url :  ctx + '/system/room/editRecommend',
		  		        data : req,  
		  		        success : function(data){
		  		        	systemAlert(data,function(){
		  		         		 reloadTable();
		  		         	})
		  		        }
		  			})
		  	    });
	      }else if(obj.event === 'detail'){
	    	  var id = obj.data.id;
	    	  openDetail(id);
	      }
	});
	
	
	 function openDetail(id){
			layer.open({
		        type: 2
		        ,title: ''
		        ,content: ctx + '/system/room/toDetail?id='+id
		        ,area: ['60%', '80%']
		        ,btn: ['确定', '取消']
		        ,yes: function(index, layero){
		          var iframeWindow = window['layui-layer-iframe'+ index]
		          ,submit = layero.find('iframe').contents().find("#LAY-room-submit");
		          //监听提交
		          iframeWindow.layui.form.on('submit(LAY-room-submit)', function(data){
	                  var roomForm = $(iframeWindow.document).find("#roomForm");
						$.ajax({  
							  type : 'post',  
					          url :  ctx + '/system/room/editRoom',
					          data : new FormData(roomForm[0]),
	    			          cache: false,
	    			          processData: false,
	    			          contentType: false,
					          success : function(data){
					        	  myfunction(data);
					        	  table.reload("room-table", {
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
	 
	 $(".batchFz").click(function(){
    	 var checkStatus = layui.table.checkStatus('room-table')
         ,checkData = checkStatus.data; //得到选中的数据
	     if(checkData.length === 0){
	       return layer.msg('请选择数据');
	     }
	     var ids = [];
	     
		 checkData.forEach(value=>{
		 ids.push(value.id)})
	  	
	     var obj ={};
	     obj.sessionId = sessionId;
	     obj.ids = ids.join(",");
	     obj.state = 1;
	     console.log(obj);
	     layer.confirm('确定批量冻结吗？', function(index) {
	     	  $.ajax({  
	  	          type : 'post',  
	  	          url :  ctx + '/system/room/batchEditState',
	  	          data : obj,
	  	          success : function(data){
	  	        	  myfunction(data);
	  	        	  reloadTable();
	  	          },error:function(data){
	  	        	  systemAlertError();
	  	          }
	  		  })
	     });    
    })
    
     $(".batchUFz").click(function(){
    	 var checkStatus = layui.table.checkStatus('room-table')
         ,checkData = checkStatus.data; //得到选中的数据
	     if(checkData.length === 0){
	       return layer.msg('请选择数据');
	     }
	     var ids = [];
	     
		 checkData.forEach(value=>{
		 ids.push(value.id)})
	  	
	     var obj ={};
	     obj.sessionId = sessionId;
	     obj.ids = ids.join(",");
	     obj.state = 2;
	     console.log(obj);
	     layer.confirm('确定批量解冻吗？', function(index) {
	     	  $.ajax({  
	  	          type : 'post',  
	  	          url :  ctx + '/system/room/batchEditState',
	  	          data : obj,
	  	          success : function(data){
	  	        	  myfunction(data);
	  	        	  reloadTable();
	  	          },error:function(data){
	  	        	  systemAlertError();
	  	          }
	  		  })
	     });    
    })
    
     $(".batchRecommend").click(function(){
    	 var checkStatus = layui.table.checkStatus('room-table')
         ,checkData = checkStatus.data; //得到选中的数据
	     if(checkData.length === 0){
	       return layer.msg('请选择数据');
	     }
	     var ids = [];
	     
		 checkData.forEach(value=>{
		 ids.push(value.id)})
	  	
	     var obj ={};
	     obj.sessionId = sessionId;
	     obj.ids = ids.join(",");
	     obj.isRecommend = 1;
	     console.log(obj);
	     layer.confirm('确定批量设为精选吗？', function(index) {
	     	  $.ajax({  
	  	          type : 'post',  
	  	          url :  ctx + '/system/room/batchEditRecommend',
	  	          data : obj,
	  	          success : function(data){
	  	        	  myfunction(data);
	  	        	  reloadTable();
	  	          },error:function(data){
	  	        	  systemAlertError();
	  	          }
	  		  })
	     });    
    })
    
     $(".batchCancelRecommend").click(function(){
    	 var checkStatus = layui.table.checkStatus('room-table')
         ,checkData = checkStatus.data; //得到选中的数据
	     if(checkData.length === 0){
	       return layer.msg('请选择数据');
	     }
	     var ids = [];
	     
		 checkData.forEach(value=>{
		 ids.push(value.id)})
	  	
	     var obj ={};
	     obj.sessionId = sessionId;
	     obj.ids = ids.join(",");
	     obj.isRecommend = 0;
	     console.log(obj);
	     layer.confirm('确定批量取消精选吗？', function(index) {
	     	  $.ajax({  
	  	          type : 'post',  
	  	          url :  ctx + '/system/room/batchEditRecommend',
	  	          data : obj,
	  	          success : function(data){
	  	        	  myfunction(data);
	  	        	  reloadTable();
	  	          },error:function(data){
	  	        	  systemAlertError();
	  	          }
	  		  })
	     });    
    })
	  
    function reloadTable(){
	   table.reload("room-table", {
	   });
	}
  
  
  exports('room', {})
});










