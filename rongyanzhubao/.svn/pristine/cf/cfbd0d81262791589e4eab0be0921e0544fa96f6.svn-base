var typeMap = new Map();
typeMap.set(-1,"超级管理员");
typeMap.set(0,"管理员");
typeMap.set(1,"普通用户");
typeMap.set(2,"商家");
typeMap.set(3,"主播");
typeMap.set(4,"房管");
var stateMap = new Map();
stateMap.set(0,"正常");
stateMap.set(1,"冻结");
var registerTypeMap = new Map();
registerTypeMap.set(0,"用户端");
registerTypeMap.set(1,"直播端");
registerTypeMap.set(2,"商家端");
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
      elem: '#account-startTime'
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
      elem: '#account-endTime'
      ,min: 0
      ,done: function(value, date){
        //更新开始日期的最大日期
        insStart.config.max = lay.extend({}, date, {
          month: date.month - 1
        });
      }
    });
  
    table.render({
        elem: '#account-table'
        ,url: ctx + '/system/account/page' //模拟接口
        ,where:json
        ,cols: [[
          {field: 'id',type: 'checkbox', fixed: 'left'}
          ,{field: 'id',title: 'id', minWidth: 100}
          ,{field: 'nick',title: '昵称', minWidth: 100}
          ,{field: 'mobile',title: '手机号',width:150}
          ,{field: 'registerType',title: '账号注册类型',templet:function(d){
			  return registerTypeMap.get(d.registerType);
		  }}
		  ,{field: 'type',title: '用户身份',templet:function(d){
			  return typeMap.get(d.type);
		  }}
		  ,{field: 'anchorMobile',title:'关联主播手机号',width:150}
		  ,{field: 'manageMobile',title:'关联房管手机号',width:150}
		  ,{field: 'merchantMobile',title:'关联商家手机号',width:150}
          ,{field: 'amount',title: '余额'}
		  ,{field: 'state',title: '状态',templet:function(d){
			  return stateMap.get(d.state);
		  }}
		  ,{field: 'createTime',title: '注册时间'}
		  ,{ title:"操作",align:'center', fixed: 'right',width:350, templet: function(d){
           var html = `<a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
			`;
           if(d.state==0){
        	   html+=`<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="fz"><i class="layui-icon layui-icon-delete"></i>冻结</a>`;
           }else{
        	   html+=`<a class="layui-btn layui-btn layui-btn-xs" lay-event="ufz"><i class="layui-icon layui-icon-delete"></i>解冻</a>`;  
           }
           if(d.type==1){
        	   html+=`<a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="set"><i class="layui-icon layui-icon-set"></i>设置</a>`;
           }
           if(d.type==2){
        	   html+=`<a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="editManage"><i class="layui-icon layui-icon-set"></i>更换房管</a>`;
        	   html+=`<a class="layui-btn layui-btn-warm layui-btn-xs" lay-event="editAnchor"><i class="layui-icon layui-icon-set"></i>更换主播</a>`;
           }
           
           return html;
          }}
        ]]
        ,page: true
//        ,toolbar:true
//        ,defaultToolbar: ['filter', 'print', 'exports']
        ,limit: 10
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
    form.on('submit(LAY-account-search)', function(data){
      var field = data.field;
      field.sessionId = sessionId;
      
      table.reload("account-table", {
          where: field
      });
      //执行重载
    });
    
    
    
    
    form.on('submit(LAY-account-refresh)', function(data){
  	  var field = data.field;
  	    field.sessionId = sessionId;
  	    table.reload("account-table", {
  	        where: field
  	    });
     });
    
    $(".add").click(function(){
    	openAdd();
    })
    
    $(".batchFz").click(function(){
    	 var checkStatus = layui.table.checkStatus('account-table')
         ,checkData = checkStatus.data; //得到选中的数据
	     if(checkData.length === 0){
	       return layer.msg('请选择数据');
	     }
	     var ids = [];
	     
		 checkData.forEach(value=>{
		 ids.push(value.id)})
	  	
	     var obj ={};
	     obj.ids = ids.join(",");
	     obj.state = 1;
	     console.log(obj);
	     layer.confirm('确定批量冻结吗？', function(index) {
	     	  $.ajax({  
	  	          type : 'post',  
	  	          url :  ctx + '/system/account/fzs',
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
    
    
    $(".batchUfz").click(function(){
		 var checkStatus = layui.table.checkStatus('account-table')
		 ,checkData = checkStatus.data; //得到选中的数据
		 if(checkData.length === 0){
		   return layer.msg('请选择数据');
		 }
		 var ids = [];
		 
		 checkData.forEach(value=>{
		 ids.push(value.id)})
		
		 var obj ={};
		 obj.ids = ids.join(",");
		 obj.state = 0;
		 console.log(obj);
		 layer.confirm('确定批量解冻吗？', function(index) {
		 	  $.ajax({  
		          type : 'post',  
		          url :  ctx + '/system/account/fzs',
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
	   table.reload("account-table", {
	   });
	 }
    
    
    function openEdit(id){
    	layer.open({
            type: 2
            ,title: ''
            ,content: ctx + '/system/account/toEdit?id=' + id+' &sessionId='+sessionId
            ,area: ['500px', '600px']
            ,btn: ['确定', '取消']
            ,yes: function(index, layero){
              var iframeWindow = window['layui-layer-iframe'+ index]
              ,submit = layero.find('iframe').contents().find("#LAY-account-submit");
              //监听提交
              iframeWindow.layui.form.on('submit(LAY-account-submit)', function(data){
                  var accountForm = $(iframeWindow.document).find("#accountForm");
    				$.ajax({  
    					  type : 'post',  
    			          url :  ctx + '/system/account/editAccount',
    			          data : new FormData(accountForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
    			          success : function(data){
    			        	  myfunction(data);
    			        	  table.reload("account-table", {
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
	        ,content: ctx + '/system/account/toAdd?sessionId='+sessionId
	        ,area: ['500px', '600px']
	        ,btn: ['确定', '取消']
	        ,yes: function(index, layero){
	          var iframeWindow = window['layui-layer-iframe'+ index]
	          ,submit = layero.find('iframe').contents().find("#LAY-account-submit");
	          //监听提交
	          iframeWindow.layui.form.on('submit(LAY-account-submit)', function(data){
                  var accountForm = $(iframeWindow.document).find("#accountForm");
					$.ajax({  
						  type : 'post',  
				          url :  ctx + '/system/account/insertAccount',
				          data : new FormData(accountForm[0]),
    			          cache: false,
    			          processData: false,
    			          contentType: false,
				          success : function(data){
				        	  myfunction(data);
				        	  table.reload("account-table", {
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
	 
	 function openBind(id){
		 layer.open({
		        type: 2
		        ,title: ''
		        ,content: ctx + '/system/account/toBind?id='+id
		        ,area: ['500px', '400px']
		        ,btn: ['确定', '取消']
		        ,yes: function(index, layero){
		          var iframeWindow = window['layui-layer-iframe'+ index]
		          ,submit = layero.find('iframe').contents().find("#LAY-account-submit");
		          //监听提交
		          iframeWindow.layui.form.on('submit(LAY-account-submit)', function(data){
	                  var accountForm = $(iframeWindow.document).find("#accountForm");
	                  var field = data.field; //获取提交的字段
						$.ajax({  
							  type : 'post',  
					          url :  ctx + '/system/account/bind',
					          data : field,
					          success : function(data){
					        	  myfunction(data);
					        	  table.reload("account-table", {
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
	table.on('tool(account-table)', function(obj){
	      var data = obj.data;
	      if(obj.event === 'fz'){
	    	  layer.confirm('确定冻结吗?', function(index){
	  			var req = {};
	  			req['id'] = data.id;
	  		    $.ajax({  
	  				type : 'post',  
	  		        url :  ctx + '/system/account/editState',
	  		        data : req,  
	  		        success : function(data){
	  		        	systemAlert(data,function(){
	  		         		 reloadTable();
	  		         	})
	  		        }
	  			})
	  	    });
	      }else if(obj.event === 'ufz'){
	    	  layer.confirm('确定解冻吗?', function(index){
		  			var req = {};
		  			req['id'] = data.id;
		  		    $.ajax({  
		  				type : 'post',  
		  		        url :  ctx + '/system/account/editState',
		  		        data : req,  
		  		        success : function(data){
		  		        	systemAlert(data,function(){
		  		         		 reloadTable();
		  		         	})
		  		        }
		  			})
		  	    });
	      }else if(obj.event === 'set'){
	    	  openBind(data.id);
	      }else if(obj.event === 'edit'){
	    	  openEdit(data.id);
	      }else if(obj.event === 'editManage'){
	    	  editManage(data.id);
	      }else if(obj.event === 'editAnchor'){
	    	  editAnchor(data.id);
	      }
	});
	
	 function editManage(id){
		 layer.open({
		        type: 2
		        ,title: ''
		        ,content: ctx + '/system/account/toEditManage?id='+id
		        ,area: ['400px', '300px']
		        ,btn: ['确定', '取消']
		        ,yes: function(index, layero){
		          var iframeWindow = window['layui-layer-iframe'+ index]
		          ,submit = layero.find('iframe').contents().find("#LAY-account-submit");
		          //监听提交
		          iframeWindow.layui.form.on('submit(LAY-account-submit)', function(data){
	                  var accountForm = $(iframeWindow.document).find("#accountForm");
	                  var field = data.field; //获取提交的字段
						$.ajax({  
							  type : 'post',  
					          url :  ctx + '/system/account/editManage',
					          data : field,
					          success : function(data){
					        	  myfunction(data);
					        	  table.reload("account-table", {
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
	
	 function editAnchor(id){
		 layer.open({
		        type: 2
		        ,title: ''
		        ,content: ctx + '/system/account/toEditAnchor?id='+id
		        ,area: ['400px', '300px']
		        ,btn: ['确定', '取消']
		        ,yes: function(index, layero){
		          var iframeWindow = window['layui-layer-iframe'+ index]
		          ,submit = layero.find('iframe').contents().find("#LAY-account-submit");
		          //监听提交
		          iframeWindow.layui.form.on('submit(LAY-account-submit)', function(data){
	                  var accountForm = $(iframeWindow.document).find("#accountForm");
	                  var field = data.field; //获取提交的字段
						$.ajax({  
							  type : 'post',  
					          url :  ctx + '/system/account/editAnchor',
					          data : field,
					          success : function(data){
					        	  myfunction(data);
					        	  table.reload("account-table", {
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
	
	  
  
  
  
  exports('account', {})
});










