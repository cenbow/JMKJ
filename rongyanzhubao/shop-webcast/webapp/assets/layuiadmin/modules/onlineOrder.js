 var onlineOrderState = [
	 {code:0,name:'待支付'},
	 {code:1,name:'待支付'},
	 {code:6,name:'取消'},
	 {code:3,name:'取消支付'},
	 {code:2,name:'待发货'},
	 {code:7,name:'待收货'},
	 {code:8,name:'待评价'},
	 {code:9,name:'已完成'},
	 {code:4,name:'存在售后'},
	 {code:5,name:'售后完成'},
	 {code:10,name:'售后关闭'},
	 {code:-1,name:'全部'}];
 
 var buttonText = new Map();
 buttonText.set(0,"取消订单");
 buttonText.set(1,"支付通过");
 buttonText.set(2,"去发货");
 buttonText.set(4,"处理售后");
 buttonText.set(5,"售后详情");
 
 var sessionId = $("#sessionId").val();
 var menuId = $("#menuId").val();
 var project = $("#curl").val();
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
  
  initTable(table,json,'#onlineOrder-table')

  element.on('tab(onlineOrder-tabs-hash)', function(data){
	 var layId=$(this).attr('lay-id');
	 var tagTable = layui.table;
	 var b = $(".layui-tab-item:eq("+data.index+")").find("tbody").length == 0;
	 
	 json.states= layId== -1?'':layId;
	 
	 var tableName = "onlineOrder-table"+(layId == -1 ? '':"-"+layId);
	 if(!b){
		tagTable.reload(tableName);
     }else{
    	 initTable(tagTable,json,"#"+tableName);
     }
	
  });
  
  //监听搜索
  form.on('submit(LAY-order-search)', function(data){
    var field = data.field;
    field.sessionId = sessionId;
    
    var layId = $("#order-state li.layui-this").attr("lay-id");
    if(field.states == '')
    	field.states = layId== -1?'':layId;
    
    var tableName = "onlineOrder-table"+(layId == -1 ? '':"-"+layId);
    
    table.reload(tableName, {
        where: field
    });
    //执行重载
  });
  
  form.on('submit(LAY-orderTable-refresh)', function(data){
	  var field = data.field;
	    field.sessionId = sessionId;
	    
	    var layId = $("#order-state li.layui-this").attr("lay-id");
	    if(field.states == '')
	    	field.states = layId== -1?'':layId;
	    
	    var tableName = "onlineOrder-table"+(layId == -1 ? '':"-"+layId);
	    
	    table.reload(tableName, {
	        where: field
	    });
   });
  
  
  
  
  exports('onlineOrder', {})
});

function openTag(e,orderGoods){
	var table = layui.table
	var state = orderGoods.state;
	if(orderGoods.state == 1 && orderGoods.payType == 4){
		state = 1;
	}
	var t = buttonFunction.get(state);
	t(orderGoods.id,function(data){
		 systemAlert(data,function(){
   		  if(data.code === 1)
   			  var tableName = getTableName();
   			  table.reload(tableName); //数据刷新
   	  	  })
	});
}

function getTableName(){
	var layId = $("#order-state li.layui-this").attr("lay-id");
    return "onlineOrder-table"+(layId == -1 ? '':"-"+layId);
}

function initTable(table,json,tableid){
	  table.render({
	      elem: tableid
	      ,url: project + '/pageData' //模拟接口
	      ,where:json
          ,treeDefaultClose: true
          ,treeLinkage: false
          ,toolbar:true
          ,defaultToolbar: ['exports']
	      ,cols: [[
	    	 {field: 'name',title: '商品', width:250,templet:function (d){
	    		 var orderType = d.type == 1 ? "(异常订单)":"";
	    		 return `
	    		 <div class="Lay-order-img">
	    		 	<img src="${d.skuImg}?x-oss-process=image/resize,m_fill,h_200,w_200"/>
	    		 	<span class="goodsName canCick">
	    		 	<p>${d.goodsName}</p>` + orderType + `
	    		 	</span>
	    		 </div>`
	    	 }}
	    	,{field: 'num',title: '单价/数量', width: 100,align:"center",templet:function (d){
	    		var text = '<div class="Lay-order-num">￥'+ d.price +"</br>("+d.num+"件)</div>" 
	        	return text; 
	        }}
	    	,{field: 'orderNo',title:'订单号',width:250}
	    	,{field: 'addressName',title: '收货人', width: 100}
	    	,{field: 'addressPhone',title: '收货人电话', width: 100}
	    	,{field: 'nick',title: '商家名称', width: 100}
	    	,{field: 'mobile',title: '商家账号', width: 100}
	        ,{field: 'money',title: '实付金额', width: 100,templet: function (d){
	        	return '￥'+(d.state == 1 && d.payType != 4 ? 0:d.money); 
	        }}
	        ,{field: 'orderType',title: '订单类型', width: 100,templet: function (d){
	        	return d.orderType == 0 ? '商城订单' : '直播订单'; 
	        }}
	        ,{field:'state', align:'center',width:100,title:'订单状态',templet: function (d) {
	        	var name = '';
	        	var n = '';
	        	onlineOrderState.some(v=> {
	        		
	        		if(v.code == d.state){
	        			var refundState = d.state;
	        			if(d.refundState == 1){
	        				refundState = 4;
	        			}else if(d.refundState == 2){
	        				refundState = 5;
	        			}else if(d.state == 1 && d.payType == 4){
        					refundState = 1;
        				}else if(d.state == 2) {}
        				else {
        					refundState = undefined;
        				}
	        			
	        			var text = buttonText.get(refundState);
	        			var buttonHtml = '';
	        			
	        			if(text != undefined) {
	        				
	        				if(d.refundState == 1)
	        					buttonHtml = `<span class="layui-btn layui-btn-primary layui-btn-xs" style="width:70px;color:red;"><a lay-href="${project}/toRefundDetail?orderId=${d.id}&sessionId=${sessionId}&menuId=${menuId}">${text}</a></span>`;
	        				else{
	        					var js = JSON.stringify(d).replace(/"/g, '&quot;')
		        				buttonHtml = `<span class="layui-btn layui-btn-primary layui-btn-xs" style="width:70px;color:blue;" onclick="openTag(event,${js})">${text}</span>`;
	        				}
	        			}
	        			
	        			n= `<div class="stateButton">
	        				${v.name}
	        				${buttonHtml}
	        				</div>`
	        			return v.code == d.type
	        		}
	        	});
	        	return n;
	        }}
	        ,{field: 'createTime', title: '下单时间',width:180}
	        ,{field:'orderId',title:'操作',align:'center',width:150,templet: function (d){
	        	var url =  project + '/toDetail?orderId=' + d.id+'&sessionId='+sessionId+'&menuId='+menuId;
	        	return `<a lay-href='${url}' style='color:#38f'>订单详情</a>`;
	        }}
	      ]]
	      ,page: true
	      ,limit: 10
	      ,height: 'full-200'
	      ,text: '暂无记录'
	      ,done: function (res, curr, count) {
	    	  $("#LAY-onlineOrder-form .layui-show .layui-table-body>.layui-table").rowspan(2,['name','num']);
	    	  $("#LAY-onlineOrder-form .layui-table-body>.layui-table").attr("lay-skin","line");
	      }
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
	  
	  
}


$.fn.rowspan = function(combined,exInclude) {
	var table = this;
	return this.each(function(){
	var that;
	var lastRow;
	$('tr', this).each(function(row) {
			var tr = this;
			$('td:eq('+combined+')', this).filter(':visible').each(function(col) {
					if (that!=null && $(this).html() == $(that).html()) {
						rowspan = $(that).attr("rowSpan");
						if (rowspan == undefined) {
							$(that).attr("rowSpan",1);
							rowspan = $(that).attr("rowSpan"); }
							rowspan = Number(rowspan)+1;
							$(that).attr("rowSpan",rowspan);
							$(this).hide();
							
							$('td', tr).each(function(col) {
								if(col != combined){
									var last = $('tr:eq('+(lastRow)+') td:eq('+col+')', table);
									var b = last.text() == $(this).text();
									if(exInclude!= undefined){
										if(b && exInclude.indexOf(last.attr("data-field")) != -1){
											b = false;
										}
									}
									if(b){
										last.attr("rowSpan",rowspan);
										$(this).hide();
									};
								}
							})
							
					} else {
						that = this;
						lastRow = row;
					}
			});
			
	    });
  });
}

function delivery(orderId,myfunction){
	 layer.open({
        type: 2
        ,title: '填写物流信息'
        ,content: project + '/toDelivery?orderId=' + orderId+'&sessionId='+sessionId+"&menuId="+menuId
        ,area: ['450px', '400px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submit = layero.find('iframe').contents().find("#LAY-delivery-submit");
          //监听提交
          iframeWindow.layui.form.on('submit(LAY-delivery-submit)', function(data){
             var field = data.field; //获取提交的字段
              field.sessionId  = sessionId;
				$.ajax({  
					  type : 'post',  
			          url :  project + '/deliveryOrder',
			          data : field,  
			          success : function(data){
			        	  myfunction(data);
			        	  location.reload();
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

function cancel(orderId,myfunction){
	 layer.confirm('确定取消该订单？', function(index){
      	 var json = {};
      	 json.sessionId = sessionId;
		 json.orderId = orderId;
      	 var url = project + "/cancel";
      	 $.ajax({  
	          type : 'post',  
	          url :  url,
	          data : json,  
	          success : function(data){
	        	  myfunction(data);
	        	  location.reload();
	          },error:function(data){
	        	  systemAlertError();
	          }
		}) 
        layer.close(index);
      });
}

function paySuccess(orderId,myfunction){
	layer.confirm('确定让改订单支付成功？', function(index){
		var json = {};
		json.sessionId = sessionId;
		json.orderId = orderId;
		var url = project + "/paySuccess";
		$.ajax({  
			type : 'post',  
			url :  url,
			data : json,  
			success : function(data){
				myfunction(data);
				location.reload();
			},error:function(data){
				systemAlertError();
			}
		}) 
		layer.close(index);
	});
}

var buttonFunction = new Map();

buttonFunction.set(2,function(orderId,myfunction){
	delivery(orderId,myfunction)
});

buttonFunction.set(0,function(orderId,myfunction){
	cancel(orderId,myfunction)
});

buttonFunction.set(1,function(orderId,myfunction){
	paySuccess(orderId,myfunction)
});







