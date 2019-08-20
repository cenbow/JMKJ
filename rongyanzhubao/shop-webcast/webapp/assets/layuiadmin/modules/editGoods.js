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
		$("#isEditSku").val(false);
		$("#skuAttrs").html("");
		specAttr();
	});
	
	form.on('checkbox(attrChecked)',function(attrId){
		$("#isEditSku").val(false);
		skuAttr();
	});
	
	$("#submit").click(function(event) {
		if (valid.check() == false) {
			parent.layer.msg("存在错误的填框的信息",{offset:'80%'});
			return;
		}
		$("#submit").attr("disabled",true);
		//var field = $("#form").serialize();
		var formData = new FormData($("#form")[0]);
		$.ajax({
			type : 'post',
			url : curl+"/update",
			data : formData,
			async : false,
			processData:false,
            contentType:false,
            cache: false,
			success : function(data) {
				if (data.code == 1) {
//					window.location.href=curl;
					window.location.reload();
				} else if (data.code == 2) {
					location.href = '#(ctx)/system/toLogin';
				}
				$("#submit").attr("disabled",false);
				parent.layer.msg(data.desc,{offset:'80%'});
				layer.closeAll('loading');
			},
			error : function() {
				layer.closeAll('loading');
			}
		});
	 });
  
  exports('editGoods', {})
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
		beforeSend : function(request) {
			layer.load(2);
		},
		success : function(data) {
			if (data.code == 1) {
				var skuAttrIds = $("#skuAttrIds").val();
				var specAttrs = $("#skuAttrs");
				specAttrs.html("");
				var list = data.data;
				var htmlString = "";
				var nowJ = 0;
				var oldJ = 0;
				for(i=0;i<list.length;i++){
					var spec = list[i];
					
					htmlString += '<div class="layui-form-item" hidden>';
					htmlString += '<label class="layui-form-label">'+spec.value+'</label>';
					htmlString += '<div class="layui-input-block specAttrId">';
					var attrs = spec.attrs;
					if(attrs != null){
						for(j=0;j<attrs.length;j++){
							if(attrs[j] != null){
								if(skuAttrIds.indexOf(attrs[j].id) != -1){
									htmlString += '<input type="checkbox" checked="checked" name="specAttrs" title="'+attrs[j].name+'" value="'+attrs[j].id+'" lay-filter="attrChecked">'; 
									oldJ++;
								}else{
									htmlString += '<input type="checkbox" checked="checked" name="specAttrs" title="'+attrs[j].name+'" value="'+attrs[j].id+'" lay-filter="attrChecked">'; 
									specAttrs.append(`<div class="layui-form-item">
														<input type="hidden" name = "attr` + nowJ + `" value="` + attrs[j].id + `" />
														<label class="layui-form-label">` + attrs[j].name + `</label>
														<div class="layui-input-block specAttrId">
															<textarea rows="2" cols="95%" name="nowName` + nowJ + `" datatype="*2-200"></textarea>
														</div>
													</div>`);
									nowJ++;
								}
							}else{
								htmlString += '<input type="checkbox" name="specAttrs" title="无" value="" lay-filter="attrChecked" >'; 
							}
						}
					}
					htmlString  += '</div></div>';
				}
				htmlString += '<input type="hidden" name="nowSkuNum" value="'+nowJ+'">';
				htmlString += '<input type="hidden" name="oldSkuNum" value="'+oldJ+'">';
				specAttrs.append(htmlString);
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
				var html1 = '';
				
				//打印内容
				/*if(result.length == 1 && data.data.specList.length == 1){*/
					for(n=0;n<result[0].length;n++){
						html1 += `<div class="layui-form-item">
									<label class="layui-form-label">` + result[0][n].name + `</label>
									<div class="layui-input-block specAttrId">
										<textarea rows="2" cols="95%" name="name` + n + `"></textarea>
									</div>
								</div>`;
					}
				/*}else{
					for(k=0;k<result.length;k++){
						html1 += `<div class="layui-form-item">
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
