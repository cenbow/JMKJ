 var sessionId = $("#sessionId").val();
 var menuId = $("#menuId").val();
 var project = $("#curl").val();
 var ctx = $("#ctx").val();
layui.define(['table', 'form','laydate'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form
  ,element = layui.element;
  
  
  var data = layui.data("account");
  /*var sessionId = data.account.sessionId;
  var pd = layui.data("project");
  var project = pd.project;*/
  
  //var pd = layui.data("project");
  
  
  var json = {};
  json.sessionId = sessionId;
  json.states="0";
  json.orderId = $("input[name='orderId']").val();
  
  var refundState = new Map();
  refundState.set(0,"<span class='green'>申请中</span>");
  refundState.set(1,"<span class='green'>已通过等待退款</span>");
  refundState.set(2,"<span class='red'>不通过申请</span>");
  refundState.set(3,"<span class='green'>关闭申请</span>");
  refundState.set(4,"<span class='green'>退货|退款完成</span>");
  refundState.set(5,"<span class='green'>已通过等待上传物流单号退货</span>");
  refundState.set(6,"<span class='green'>已上传物流单号</span>");
  refundState.set(7,"<span class='green'>上传物流单号后不通过</span>");
  refundState.set(8,"<span class='green'>上传物流单号后通过等待退款</span>");
  refundState.set(10,"<span class='green'>超时自动退款</span>");
  
  
  var payTypeMap = new Map();
  payTypeMap.set(0,"支付宝支付");
  payTypeMap.set(1,"微信支付");
  payTypeMap.set(2,"余额支付");
  payTypeMap.set(4,"银行卡支付");
  
  var reasonTypeMap = new Map();
  reasonTypeMap.set(0,"质量问题");
  reasonTypeMap.set(1,"商品描述不符");
  reasonTypeMap.set(2,"卖家发错货");
  reasonTypeMap.set(2,"少发");
  reasonTypeMap.set(2,"商品破损");
  
  var orderTypeMap = new Map();
  orderTypeMap.set(0,"普通订单");
  orderTypeMap.set(1,"拼团订单");
  orderTypeMap.set(2,"助力商品订单");
  
  var refundTypeMap = new Map();
  refundTypeMap.set(0,"仅退款（未发货）");
  refundTypeMap.set(1,"退货退款（已发货）");
  refundTypeMap.set(2,"仅退款（已发货后未收到货）");
  
  $.ajax({  
		type : 'post',  
        url :  project + '/selectRefundDetail',
        data : json,  
        success : function(data){
	      	var refundList = data.data;
	      	 console.log(refundList);
	      	if(refundList == null){
	      		return;
	      	}
	      	
	      	refundList.forEach((refund, index)=>{
	      		
	      		 var freight = refund.freight != null ? refund.freight: 0;
		      	 var refundStateHtml = refundState.get(refund.state);
		      	 var refundType = refundTypeMap.get(refund.type);
		      	 var payType = payTypeMap.get(refund.order.payType);
		      	 var reasonType = reasonTypeMap.get(refund.reasonType);
		      	 var orderType = orderTypeMap.get(refund.orderType);
		      	 var trList = "";
		      	
		      	refund.refundGoodsList.forEach(goods=>{
		      		trList+=`<tr>
			      			<td><div class="Lay-order-img">
				    		 	<img src="${goods.skuImg}"/>
				    		 	<span class="goodsName canClick">
				    		 		<p>${goods.goodsName}</p>
				    		 	</span>
			        			</div>
		        			</td>
		      				<td>￥${goods.price}</td>
		      				</tr>`;
		        });
		      	
		      	var first = index == 0 ?`
		      			<div class="layui-col-md12"  style="padding: 20px 0 0 0;">
					<div class="layui-card oderTable">
						<table lay-filter="LAY-onlineRefundOrder-detail" lay-skin="line">
					              <thead>
					                <tr>
					                  <th lay-data="{field:'orderNo', width:200}">订单号</th>
					                  <th lay-data="{field:'orderType', width: 100}">订单类型</th>
					                  <th lay-data="{field:'orderTime', width:180}">下单时间</th>
					                  <th lay-data="{field:'paid'}">实付金额</th>
					                </tr> 
					              </thead>
					              <tbody>
					              	<tr>
						   				<td>${refund.order.orderNo}</td>
						   				<td>${refund.order.type}</td>
						   				<td>${refund.order.createTime}</td>
						   				<td>￥${refund.order.money}</td>
						   			</tr>
					              </tbody>
					      </table>
				    </div>
				</div>` :'';
		      		
		      	var jsonRefund = JSON.stringify(refund).replace(/"/g, '&quot;')
		      	var handleHtml = refund.state == 0 || refund.state == 6? `<a href="javascript:;" style="padding:20px 20px 20px 20px" onclick="handle(${jsonRefund})"><span class="canClick">同意/拒绝</span></a>`:'';
		      	var refundlogisticsInfo = refund.logisticsNo != null ? refund.logisticsNo : '未上传单号';
		      	var html = `${first}
		      	<div class="layui-col-md12" style="padding: 20px 0 0 0;">
					<div class="layui-card">
							<div class="layui-card-header">售后概况
								${handleHtml}
							</div>
							<div class="layui-card-body">
								<div class="orderDetail">
									<div class="orderLeft">
									<p><span class="slabel">售后单号：</span><span class='online_refund_refundNo'>${refund.no}</span></p>
										<p><span class="slabel">订单状态：</span><span class='online_refund_state'>${refundStateHtml}</span></p>
										<p><span class="slabel">退款类型：</span><span  class='online_refund_refundType' >${refundType}</span></p>
										<p><span class="slabel">退款金额：</span><span class='online_refund_price'>￥${refund.money} 含运费(${freight}元)</span></p>
										<p><span class="slabel">退款用户：</span><span class='online_refund_buyName' >${refund.buyUser.nick} (${refund.buyUser.mobile})</span></p>
										<p><span class="slabel">支付方式：</span><span class='online_refund_payType' >${payType}</span></p>
										<p><span class="slabel">退款原因：</span><span  class='online_refund_reasonType'>${refund.reason}</span></p>
										<p><span class="slabel">售后物流：</span><span  class='online_refund_logisticsInfo'>${refundlogisticsInfo}</span></p>
									</div>
									<div class="orderRight">
										<p><span>买家描述：</span>
											<span class='online_refund_content'>${!refund.content?"":refund.content}</span>
										</p>
									</div>
								
								</div>
								
						   </div>
				    </div>
				</div>
				
				
				<div class="layui-col-md12" style="padding: 20px 0 20px 0;">
					<div class="layui-card goodsTable">
						<table lay-filter="LAY-onlineRefundOrderGoods-detail" lay-skin="line">
				            <thead>
				                <tr>
				                  <th lay-data="{field:'name', width:480}">商品名称</th>
				                  <th lay-data="{field:'goodsPrice', width: 100}">商品单价</th>
				                </tr> 
				              </thead>
				              <tbody>
				              		${trList}
				              </tbody>
				          </table>
				    </div>
				</div>`;
		      		
		      	$(".refundDetail").append(html);
	      		
	      	
		      		
	      	})

	   		table.init('LAY-onlineRefundOrder-detail', { 
	         //height: 'full-500'
	   		});

	        
	        table.init(`LAY-onlineRefundOrderGoods-detail`, { 
	            //height: 'full-500'
	         });
	      	 
	      	 
	      	 
	      	 
        }
	})
  
  

  
  exports('onlineRefundDetail', {})
});

function handle(refund){
	 layer.open({
	        type: 2
	        ,title: '审核售后订单'
	        ,content:project + '/toHandleRefund?id=' + refund.id +'&sessionId='+sessionId
	        ,area: ['450px', '400px']
	        ,btn: ['确定', '取消']
	        ,yes: function(index, layero){
	          var iframeWindow = window['layui-layer-iframe'+ index]
	          ,submit = layero.find('iframe').contents().find("#LAY-refundHandle-submit");
	          //监听提交
	          iframeWindow.layui.form.on('submit(LAY-refundHandle-submit)', function(data){
	             var field = data.field; //获取提交的字段
	              field.sessionId  = sessionId;
					$.ajax({  
						  type : 'post',  
				          url :  project + '/handleRefund',
				          data : field,  
				          success : function(data){
				        	  
				        	  parent.layer.msg(data.desc,{offset:'80%'});
				        	  location.reload(); //数据刷新
				        	  /*systemAlert(data,function(){
				           		  if(data.code === 1)
				           			  location.reload(); //数据刷新
				           	  })*/
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






