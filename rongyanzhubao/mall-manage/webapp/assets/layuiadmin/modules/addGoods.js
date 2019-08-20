var menuId = $("#menuId").val();
var curl = $("#curl").val();
var sessionId = $("#sessionId").val();
layui.define(['table', 'form', 'element','laydate', 'upload'], function(exports){
	var $ = layui.$, admin = layui.admin, element = layui.element, layer = layui.layer, laydate = layui.laydate, form = layui.form,upload = layui.upload;
	laydate.render({
		elem : '#onyear',
		type : 'year'
	});
	specAttr();
	form.on('select(shopMdId)',function(shopMdId){
		specAttr();
	});
	
  
  
	form.on('checkbox(attrChecked)',function(attrId){
		skuAttr();
	});
	
	$("#submit").click(function(event) {
		if (valid.check() == false) {
			parent.layer.msg("存在错误的填框的信息",{offset:'80%'});
			return;
		}
		//var field = $("#form").serialize();
		var formData = new FormData($("#form")[0]);
		$.ajax({
			type : 'post',
			url : curl+"/save",
			data : formData,
			async : false,
			processData:false,
            contentType:false,
            cache: false,
			success : function(data) {
				if (data.code == 1) {
//					window.location.href=curl;
					window.location.reload();
//					location.href = curl+'/page?sessionId='+sessionId;
				} else if (data.code == 2) {
					location.href = '#(ctx)/system/toLogin';
				}
				parent.layer.msg(data.desc,{offset:'80%'});
				layer.closeAll('loading');
			},
			error : function() {
				layer.closeAll('loading');
			}
		});
	 });
  
  exports('addGoods', {})
});

function getData(index,html){
	var inputArray = $(".specAttrId:eq("+index+")").children("input");
	var text="";
	if(inputArray != undefined){
		index++;
		
		for(var i = 0;i<inputArray.length;i++){
			if($(inputArray[i])[0].checked){
				html += "<p>"+inputArray[i].title+"</p>"
				text += getData(index++,html);
				index--;
			}
			
		}
	 }
	return text;
}

function getData2(i,j,attrs,list){
	
	var attr = attrs[i];
	var attrsList = attr.attrs;
	var attrN ;
	if(j>=attrsList.lenght ){
		attrN = attrsList[attrsList.lenght -1 ];
	}else{
		attrN = attrsList[j];
	}
	list.add(attrN);
	getData2(i++,j++,attrs,list);
}

function cartesianProduct(Matrix) {
    if(Matrix.length === 0) {return []}
    if(Matrix.length === 1) {return Matrix}
    return Matrix.reduce((A,B) => {
       const product = []
       for(let i = 0; i < A.length; i++){
           for(let j = 0; j < B.length; j++) {
               product.push(
                   Array.isArray(A[i]) ? [...A[i],B[j]] : [A[i],B[j]]
               )
           }
       }
       return product
    })
}

function specAttr(){
	var form = layui.form;
	var specAttrs = $("#specAttrs");
	$.ajax({
		type : 'post',
		url : curl+"/listSpec?shopModId="+shopMdId.value,
		data : "",
		async : false,
		processData:false,
        contentType:false,
        cache: false,
		headers : {
			sessionId : sessionId,
			menuId : menuId
		},
		beforeSend : function(request) {
			layer.load(2);
		},
		success : function(data) {
			if (data.code == 1) {
				specAttrs.html("");
				$("#skuAttrs").html("");
				var list = data.data;
				var htmlString = "";
				for(i=0;i<list.length;i++){
					var spec = list[i];
					
					htmlString += '<div class="layui-form-item">';
					htmlString += '<label class="layui-form-label">'+spec.value+'</label>';
					htmlString += '<div class="layui-input-block specAttrId">';
					var attrs = spec.attrs;
					for(j=0;j<attrs.length;j++){
						 htmlString += '<input type="checkbox" name="specAttrs" title="'+attrs[j].name+'" value="'+attrs[j].id+'" lay-filter="attrChecked" checked>'; 
					}
					htmlString  += '</div></div>';
				}
				specAttrs.append(htmlString);
				form.render();
				skuAttr();
				//window.location.reload();
			} else if (data.code == 2) {
				location.href = '#(ctx)/system/toLogin';
			}
			parent.layer.msg(data.desc,{offset:'80%'});
			layer.closeAll('loading');
		},
		error : function() {
			layer.closeAll('loading');
		}
	});
}


function skuAttr(){
	var form = layui.form;
	var specAttrs = $("#skuAttrs");
	var field = $("#specAttrs").find("input[name='specAttrs']").serialize();
	$.ajax({
		type : 'post',
		url : curl+"/listSKUAttrs?shopModId="+shopMdId.value+"&"+field,
		data : "",
		async : false,
		processData:false,
        contentType:false,
        cache: false,
		headers : {
			sessionId : sessionId,
			menuId : menuId
		},
		beforeSend : function(request) {
			layer.load(2);
		},
		success : function(data) {
			if (data.code == 1) {
				var array = new Array();
				for(i=0;i<data.data.attrList.length;i++){
					array[i] = data.data.attrList[i];
			}
			var result = cartesianProduct(array);
			var html1 = '<input type="hidden" name = "skuNum" value="'+(result.length == 1 && array.length == 1 ? result[0].length : result.length)+'"';
				
			//打印内容

			/*if(result.length == 1 && data.data.specList.length == 1){*/
				for(n=0;n<result[0].length;n++){
					html1 += `<div class="layui-form-item">
								<input type="hidden" name = "attr` + n + `" value="` + result[0][n].id + `" />
								<label class="layui-form-label">` + result[0][n].name + `</label>
								<div class="layui-input-block specAttrId">
									<textarea rows="2" cols="95%" name="name` + n + `" datatype="*2-200"></textarea>
								</div>
							</div>`;
				}
			/*}else{
				for(k=0;k<result.length;k++){
					html1 += `<div class="layui-form-item">
						<input type="hidden" name = "attr` + k + `" value="` + result[k].id + `" />
						<label class="layui-form-label">` + result[k].name + `</label>
						<div class="layui-input-block specAttrId">
							<textarea rows="2" cols="95%" name="name` + k + `"></textarea>
						</div>
					</div>`;
				}
			}*/
			specAttrs.html("");
			specAttrs.append(html1);
			form.render();
			//window.location.reload();
			} else if (data.code == 2) {
				location.href = '#(ctx)/system/toLogin';
			}
			parent.layer.msg(data.desc,{offset:'80%'});
			layer.closeAll('loading');
		},
		error : function() {
			layer.closeAll('loading');
		}
	});
	
}
