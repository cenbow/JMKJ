
layui.define(['table', 'form','laydate'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form
  ,element = layui.element;
  
  var data = layui.data("account");
  var sessionId = $("#sessionId").val();
  var menuId = $("#menuId").val();
  var project = $("#curl").val();
  var ctx = $("#ctx").val();
  
  
  var json = {};
  json.sessionId = sessionId;
  json.menuId = menuId ;
  json.orderId = $("input[name='orderId']").val();
  
  var  onlineOrderState = new Map();
  onlineOrderState.set(0,'待支付');
  onlineOrderState.set(6,'取消');
  onlineOrderState.set(2,'待发货');
  onlineOrderState.set(7,'待收货');
  onlineOrderState.set(8,'待评价');
  onlineOrderState.set(9,'已完成');
  onlineOrderState.set(4,'售后中');
  onlineOrderState.set(5,'售后成功');
  onlineOrderState.set(10,'售后关闭');
  
  var payTypeMap = new Map();
  payTypeMap.set(0,"支付宝支付");
  payTypeMap.set(1,"微信支付");
  payTypeMap.set(2,"余额支付");
  payTypeMap.set(3,"银行卡支付");
  
  var expressWay = new Map();
  expressWay.set(0,"快递");
  expressWay.set(1,"货到付款");
  
  var orderTypeMap = new Map();
  orderTypeMap.set(0,"普通订单");
  orderTypeMap.set(1,"拼团订单");
  orderTypeMap.set(2,"助力商品订单");
  
  var orderGoodsState = new Map();
  orderGoodsState.set(0,"正常订单");
  orderGoodsState.set(1,"退货退款中");
  orderGoodsState.set(2,"仅退款中");
  orderGoodsState.set(3,"已退款");
  orderGoodsState.set(4,"售后关闭");
  
  
  
  $.ajax({  
		type : 'post',  
        url :  project + '/detailData',
        data : json,  
        success : function(data){
	      	 var order = data.data;
	      	 var freight = order.freight != null ? order.freight: 0;
	      	 var expressNo = order.expressNo != null ? order.expressNo : '未发货';
	      	 $(".order_state").text(onlineOrderState.get(order.state));
	      	 $(".order_paid").text(`￥${order.money} 含运费(${freight}元)` );
	      	 $(".order_buyName").text(`${order.buyName.nick} (${order.buyName.mobile})`);
	      	 $(".order_payType").text(payTypeMap.get(order.payType));
	      	 $(".order_expressWay").text(expressWay.get(0));
	      	 $(".order_sure").text(order.addressName+' '+order.addressPhone+' '+order.address);
	      	 $(".order_remark").text(order.remark);
	      	 $(".order_expressInfo").text(expressNo);
	      	 
	      	 var asap = order.asap == 1?'是':'否';
	      	 var payTime = order.payTime != null?order.payTime : '未支付';
	      	 var orderType = orderTypeMap.get(order.orderType);
	      	 var html = `<tr>
	      				<td>${order.orderNo}</td>
	      				<td>${orderType}</td>
	      				<td>${order.createTime}</td>
	      				<td>${payTime}</td>
	      				<td>￥${order.money}</td>
	      				</tr>`;
	      	 
	      	
	      	$(".oderTable").append(`<table lay-filter="LAY-onlineOrder-detail" lay-skin="line">
								              <thead>
								                <tr>
								                  <th lay-data="{field:'orderNo', width:200}">订单号</th>
								                  <th lay-data="{field:'type', width: 100}">订单类型</th>
								                  <th lay-data="{field:'createTime', width:180}">下单时间</th>
								                  <th lay-data="{field:'payTime', width:180}">支付时间</th>
								                  <th lay-data="{field:'money', width: 100}">实付金额</th>
								                </tr> 
								              </thead>
								              <tbody>
								              </tbody>
								            </table>`);
	      	 
	      	$("table[lay-filter='LAY-onlineOrder-detail']").children("tbody").append(html);
	        table.init('LAY-onlineOrder-detail', { 
	            //height: 'full-500'
	         });
	        
	        html = "";
	        
	        order.orderGoodsList.forEach(goods=>{
	        	html+=`<tr>
	        			<td>${goods.goodsId}</td>
		      			<td>${goods.goodsName}</td>
	      				<td>￥${goods.price}</td>
	      				<td>${goods.num}</td>
	      				<td>${orderGoodsState.get(goods.state)}</td>
	      				</tr>`;
	        });
	        
	    	$(".goodsTable").append(`<table lay-filter="LAY-onlineOrderGoods-detail" lay-skin="line">
		              <thead>
		                <tr>
		                  <th lay-data="{field:'goodsId', width:100}">商品id</th>
		                  <th lay-data="{field:'name', width:480}">商品名称</th>
		                  <th lay-data="{field:'goodsPrice', width: 100}">商品单价</th>
		                  <th lay-data="{field:'num', width:100}">商品数量</th>
		                   <th lay-data="{field:'goodsState', width:200}">订单商品状态</th>
		                </tr> 
		              </thead>
		              <tbody>
		              </tbody>
		            </table>`);
	        
	    	$("table[lay-filter='LAY-onlineOrderGoods-detail']").children("tbody").append(html);
	        table.init('LAY-onlineOrderGoods-detail', { 
	            //height: 'full-500'
	         });
	        
	     
	        
	      	
        }
	})
 
  
  exports('onlineOrderDetail', {})
});






